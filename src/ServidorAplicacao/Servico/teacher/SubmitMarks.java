package ServidorAplicacao.Servico.teacher;

import java.io.File;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.apache.commons.beanutils.BeanComparator;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MultiHashMap;
import org.apache.commons.collections.Predicate;
import org.apache.commons.collections.Transformer;

import DataBeans.ISiteComponent;
import DataBeans.InfoEvaluation;
import DataBeans.InfoFrequentaWithInfoStudentAndPerson;
import DataBeans.InfoSiteCommon;
import DataBeans.InfoSiteSubmitMarks;
import DataBeans.TeacherAdministrationSiteView;
import Dominio.EnrolmentEvaluation;
import Dominio.Evaluation;
import Dominio.ExecutionCourse;
import Dominio.IEmployee;
import Dominio.IEnrollment;
import Dominio.IEnrolmentEvaluation;
import Dominio.IEvaluation;
import Dominio.IExecutionCourse;
import Dominio.IExecutionPeriod;
import Dominio.IFrequenta;
import Dominio.IMark;
import Dominio.IPessoa;
import Dominio.ISite;
import Dominio.ITeacher;
import Dominio.Mark;
import Dominio.ResponsibleFor;
import ServidorAplicacao.IServico;
import ServidorAplicacao.Factory.TeacherAdministrationSiteComponentBuilder;
import ServidorAplicacao.Servico.UserView;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IFrequentaPersistente;
import ServidorPersistente.IPersistentEmployee;
import ServidorPersistente.IPersistentEnrolmentEvaluation;
import ServidorPersistente.IPersistentEvaluation;
import ServidorPersistente.IPersistentExecutionCourse;
import ServidorPersistente.IPersistentMark;
import ServidorPersistente.IPersistentResponsibleFor;
import ServidorPersistente.IPersistentSite;
import ServidorPersistente.IPessoaPersistente;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;
import Util.EnrolmentEvaluationState;
import Util.EnrolmentEvaluationType;
import Util.Ftp;
import Util.TipoCurso;
import Util.middleware.CreateFile;

/**
 * @author T�nia Pous�o
 *  
 */
public class SubmitMarks implements IServico {
    
    private MultiHashMap enrolmentEvaluationTableByDegree;
    private MultiHashMap improvmentEnrolmentEvaluationTableByDegree;
    private List notEnrolled;
    private List postGraduate;
    private Integer nSubmited;
    /**
     * The actor of this class.
     */
    public SubmitMarks() {

    }

    /**
     * Returns Service Name
     */
    public String getNome() {
        return "SubmitMarks";
    }

    public Object run(Integer executionCourseCode, Integer evaluationCode, Date evaluationDate,
            UserView userView) throws FenixServiceException {
        enrolmentEvaluationTableByDegree = new MultiHashMap();
        improvmentEnrolmentEvaluationTableByDegree = new MultiHashMap();
        notEnrolled = new ArrayList();
        postGraduate = new ArrayList();

        try {
            ISuportePersistente sp = SuportePersistenteOJB.getInstance();
            
            //execution course and execution course's site
            IPersistentExecutionCourse persistentExecutionCourse = sp.getIPersistentExecutionCourse();

            IExecutionCourse executionCourse = (IExecutionCourse) persistentExecutionCourse.readByOID(
                    ExecutionCourse.class, executionCourseCode);

            IPersistentSite persistentSite = sp.getIPersistentSite();
            ISite site = persistentSite.readByExecutionCourse(executionCourse);

            //evaluation
            IPersistentEvaluation persistentEvaluation = sp.getIPersistentEvaluation();
            IEvaluation evaluation = (IEvaluation) persistentEvaluation.readByOID(Evaluation.class,
                    evaluationCode);

            //attend list
            IFrequentaPersistente persistentAttend = sp.getIFrequentaPersistente();
            List attendList = persistentAttend.readByExecutionCourse(executionCourse);

            IPersistentResponsibleFor persistentResponsibleFor = sp.getIPersistentResponsibleFor();
            List professors = persistentResponsibleFor.readByExecutionCourse(executionCourse);

            //employee logged
            IPessoaPersistente pessoaPersistente = sp.getIPessoaPersistente();
            IPessoa pessoa = pessoaPersistente.lerPessoaPorUsername(userView.getUtilizador());
            IEmployee employee = readEmployee(pessoa);
            ITeacher teacher = ((ResponsibleFor) professors.get(0)).getTeacher();

            
            //Separate improvments/normal/not enrolled/postGraduate attends
            separateAttends(userView, executionCourse, attendList, evaluation, evaluationDate, employee, teacher);
            
            
            List fileList = submitMarksAndCreateFiles(enrolmentEvaluationTableByDegree);
            Ftp.enviarFicheiros("/DegreeGradesFtpServerConfig.properties", fileList, "notas/");
            
            List improvmentFileList = submitMarksAndCreateFiles(improvmentEnrolmentEvaluationTableByDegree);

            //Send the files via FPT
            
            Ftp.enviarFicheiros("/DegreeGradesFtpServerConfig.properties", improvmentFileList, "melhoria/");

            return createSiteView(site, evaluation, nSubmited, notEnrolled, postGraduate);
        } catch (Exception e) {
            e.printStackTrace();
            throw new FenixServiceException(e.getMessage());
        }
    }

    private void separateAttends(UserView userView,
            IExecutionCourse executionCourse, List attendList, IEvaluation evaluation,
            Date evaluationDate, IEmployee employee, ITeacher teacher) throws FenixServiceException {

        try {
            MultiHashMap enrolmentEvaluationsByDegree = new MultiHashMap();
            int submited = 0;
            ISuportePersistente sp = SuportePersistenteOJB.getInstance();
            IPersistentMark persistentMark = sp.getIPersistentMark();
            IPersistentEnrolmentEvaluation enrolmentEvaluationDAO = sp
                    .getIPersistentEnrolmentEvaluation();
            Iterator iter = attendList.iterator();

            verifyAlreadySubmittedMarks(attendList, executionCourse.getExecutionPeriod(), enrolmentEvaluationDAO);

            List markList = persistentMark.readBy(evaluation);

            //Check if there is any mark. If not, we can not submit
            if (markList.isEmpty()) {
                throw new FenixServiceException("errors.submitMarks.noMarks");
            }

            while (iter.hasNext()) {
                IFrequenta attend = (IFrequenta) iter.next();
                IEnrollment enrolment = attend.getEnrolment();
                IEnrolmentEvaluation enrolmentEvaluation = null;

                //check student�s degree type
                if (attend.getAluno().getDegreeType().equals(TipoCurso.MESTRADO_OBJ)) {
                    
                    postGraduate.add(InfoFrequentaWithInfoStudentAndPerson.newInfoFromDomain(attend));
                    continue;
                }

                //check if this student is enrolled
                if (enrolment == null) {
                    notEnrolled.add(InfoFrequentaWithInfoStudentAndPerson.newInfoFromDomain(attend));
                    continue;
                }
                
                IMark mark = getMark(evaluation, markList, attend);
                if(isImprovment(enrolment, executionCourse)) {
                    enrolmentEvaluation = getEnrolmentEvaluation(userView, executionCourse, enrolment, evaluationDate, employee, teacher, mark, EnrolmentEvaluationType.IMPROVEMENT_OBJ);
                    improvmentEnrolmentEvaluationTableByDegree.put(enrolment.getStudentCurricularPlan().getDegreeCurricularPlan().getDegree().getIdInternal(), enrolmentEvaluation);
                } else {
                    enrolmentEvaluation = getEnrolmentEvaluation(userView, executionCourse, enrolment, evaluationDate, employee, teacher, mark, EnrolmentEvaluationType.NORMAL_OBJ);
                    enrolmentEvaluationTableByDegree.put(enrolment.getStudentCurricularPlan().getDegreeCurricularPlan().getDegree().getIdInternal(), enrolmentEvaluation);
                }
                submited++;
            }
            
            nSubmited = new Integer(submited);
        } catch (Exception e) {
            e.printStackTrace();
            throw new FenixServiceException(e.getMessage());
        }
    }

    private IEnrolmentEvaluation getEnrolmentEvaluation(UserView userView,
            IExecutionCourse executionCourse, IEnrollment enrolment, Date evaluationDate, IEmployee employee, ITeacher teacher, IMark mark, EnrolmentEvaluationType enrolmentEvaluationType) throws FenixServiceException {
        
        IEnrolmentEvaluation newEnrolmentEvaluation = null;
        
        
 
        if ((mark == null) || (mark.getMark().length() == 0)) {

            newEnrolmentEvaluation = getEnrolmentEvaluationByEnrolment(userView, executionCourse,
                    enrolment, evaluationDate, "NA", employee, teacher, enrolmentEvaluationType);
 

        } else {

            newEnrolmentEvaluation = getEnrolmentEvaluationByEnrolment(userView, executionCourse,
                    enrolment, evaluationDate, mark.getMark().toUpperCase(), employee, teacher, enrolmentEvaluationType);
        }
        
        return newEnrolmentEvaluation;
    }
    /**
     * @param enrolment
     * @param executionCourse
     * @return
     */
    private boolean isImprovment(IEnrollment enrolment, IExecutionCourse executionCourse) {
        if(enrolment.getExecutionPeriod().equals(executionCourse.getExecutionPeriod()))
            return false;
        return true;
    }

    private void verifyAlreadySubmittedMarks(List attendList, final IExecutionPeriod executionPeriod,
            IPersistentEnrolmentEvaluation enrolmentEvaluationDAO) throws ExcepcaoPersistencia,
            FenixServiceException {
        List enrolmentListIds = (List) CollectionUtils.collect(attendList, new Transformer() {

            public Object transform(Object input) {
                IFrequenta attend = (IFrequenta) input;
                IEnrollment enrolment = attend.getEnrolment();
                if(enrolment != null) {
                    if(enrolment.getExecutionPeriod().equals(executionPeriod))
                        return enrolment.getIdInternal();
                }
                return null;
            }
        });

        enrolmentListIds = (List) CollectionUtils.select(enrolmentListIds, new Predicate() {
            public boolean evaluate(Object arg0) {
                return arg0 != null;
            }
        });
        List alreadySubmiteMarks = new ArrayList();
        if(!enrolmentListIds.isEmpty()) {
            alreadySubmiteMarks = enrolmentEvaluationDAO.readAlreadySubmitedMarks(enrolmentListIds);
        }

        if (!alreadySubmiteMarks.isEmpty()) {
            throw new FenixServiceException("errors.submitMarks.yetSubmited");
        }
    }

    private IMark getMark(IEvaluation evaluation, List markList, IFrequenta attend) {
        //                IMark mark = persistentMark.readBy(evaluation, attend);
        IMark mark = new Mark();
        mark.setAttend(attend);
        mark.setEvaluation(evaluation);
        int indexOf = markList.indexOf(mark);
        if (indexOf != -1) {
            mark = (IMark) markList.get(indexOf);
        } else {
            mark = null;
        }
        return mark;
    }

    private IEnrolmentEvaluation getEnrolmentEvaluationByEnrolment(UserView userView,
            IExecutionCourse executionCourse, IEnrollment enrolment, Date evaluationDate,
            String publishedMark, IEmployee employee, ITeacher teacher, EnrolmentEvaluationType enrolmentEvaluationType) throws FenixServiceException {
        ISuportePersistente sp;
        IEnrolmentEvaluation enrolmentEvaluation = null;

        try {
            sp = SuportePersistenteOJB.getInstance();

            IPersistentEnrolmentEvaluation persistentEnrolmentEvaluation = sp
                    .getIPersistentEnrolmentEvaluation();

            //Verify if this mark has been already submited
            //verifyYetSubmitMarks(enrolment);

            enrolmentEvaluation = persistentEnrolmentEvaluation
                    .readEnrolmentEvaluationByEnrolmentEvaluationStateAndType(enrolment,
                            EnrolmentEvaluationState.TEMPORARY_OBJ, enrolmentEvaluationType);

            //There can exist only one enrolmentEvaluation with Temporary State
            if (enrolmentEvaluation == null ) {
                enrolmentEvaluation = new EnrolmentEvaluation();
            }

            //teacher responsible for execution course

            enrolmentEvaluation.setEnrolment(enrolment);
            persistentEnrolmentEvaluation.simpleLockWrite(enrolmentEvaluation);

            enrolmentEvaluation.setGrade(publishedMark);

            enrolmentEvaluation.setEnrolmentEvaluationType(enrolmentEvaluationType);
            enrolmentEvaluation.setEnrolmentEvaluationState(EnrolmentEvaluationState.TEMPORARY_OBJ);
            enrolmentEvaluation.setObservation(new String("Submiss�o da Pauta"));

            enrolmentEvaluation.setPersonResponsibleForGrade(teacher.getPerson());

            enrolmentEvaluation.setEmployee(employee);

            Calendar calendar = Calendar.getInstance();
            enrolmentEvaluation.setWhen(new Timestamp(calendar.getTimeInMillis()));
            enrolmentEvaluation.setGradeAvailableDate(calendar.getTime());
            if (evaluationDate != null) {
                enrolmentEvaluation.setExamDate(evaluationDate);
            } else {
                enrolmentEvaluation.setExamDate(calendar.getTime());
            }

            enrolmentEvaluation.setCheckSum("");

            return enrolmentEvaluation;
        } catch (ExcepcaoPersistencia e) {
            e.printStackTrace();
            throw new FenixServiceException(e.getMessage());
        }
    }

    private List submitMarksAndCreateFiles(MultiHashMap enrolmentEvaluationTableByDegree)
            throws FenixServiceException {
        Set degrees = enrolmentEvaluationTableByDegree.keySet();
        Iterator iter = degrees.iterator();
        List fileList = new ArrayList();
        try {
            //degrees
            while (iter.hasNext()) {
                List enrolmentEvaluationsByDegree = (List) enrolmentEvaluationTableByDegree.get(iter
                        .next());
                File file = CreateFile.buildFile(enrolmentEvaluationsByDegree);
                if (file != null) {
                    fileList.add(file);
                }
            }
            return fileList;
        } catch (Exception e) {
            e.printStackTrace();
            throw new FenixServiceException(e.getMessage());
        }
    }

    private IEmployee readEmployee(IPessoa person) {
        IEmployee employee = null;
        IPersistentEmployee persistentEmployee;
        try {
            persistentEmployee = SuportePersistenteOJB.getInstance().getIPersistentEmployee();
            employee = persistentEmployee.readByPerson(person.getIdInternal().intValue());
        } catch (ExcepcaoPersistencia e) {
            e.printStackTrace();
        }
        return employee;
    }

    private Object createSiteView(ISite site, IEvaluation evaluation, Integer submited,
            List notEnrolledList, List mestradoList) throws FenixServiceException {

        InfoSiteSubmitMarks infoSiteSubmitMarks = new InfoSiteSubmitMarks();

        //infoSiteSubmitMarks.setInfoEvaluation(Cloner
        //.copyIEvaluation2InfoEvaluation(evaluation));
        infoSiteSubmitMarks.setInfoEvaluation(InfoEvaluation.newInfoFromDomain(evaluation));
        infoSiteSubmitMarks.setSubmited(submited);

        // order errorsNotEnrolmented list by student's number
        if (notEnrolledList != null) {
            Collections.sort(notEnrolledList, new BeanComparator("aluno.number"));
            infoSiteSubmitMarks.setNotEnrolmented(notEnrolledList);
        }

        // order errorsMarkNotPublished list by student's number
        if (mestradoList != null) {
            Collections.sort(mestradoList, new BeanComparator("aluno.number"));
            infoSiteSubmitMarks.setMestrado(mestradoList);
        }

        TeacherAdministrationSiteComponentBuilder componentBuilder = new TeacherAdministrationSiteComponentBuilder();
        ISiteComponent commonComponent = componentBuilder.getComponent(new InfoSiteCommon(), site, null,
                null, null);

        TeacherAdministrationSiteView siteView = new TeacherAdministrationSiteView(commonComponent,
                infoSiteSubmitMarks);
        return siteView;
    }
}