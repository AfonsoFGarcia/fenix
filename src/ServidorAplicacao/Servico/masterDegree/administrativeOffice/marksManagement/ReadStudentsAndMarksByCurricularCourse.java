package ServidorAplicacao.Servico.masterDegree.administrativeOffice.marksManagement;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

import org.apache.commons.beanutils.BeanComparator;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;
import org.apache.commons.collections.comparators.ComparatorChain;

import DataBeans.InfoEnrolmentEvaluation;
import DataBeans.InfoSiteEnrolmentEvaluation;
import DataBeans.InfoTeacher;
import DataBeans.util.Cloner;
import Dominio.CurricularCourse;
import Dominio.ICurricularCourse;
import Dominio.IEnrollment;
import Dominio.IEnrolmentEvaluation;
import Dominio.IExam;
import Dominio.IExecutionCourse;
import Dominio.IPessoa;
import Dominio.ITeacher;
import ServidorAplicacao.IServico;
import ServidorAplicacao.Servico.exceptions.ExistingServiceException;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorAplicacao.Servico.exceptions.NonExistingServiceException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentCurricularCourse;
import ServidorPersistente.IPersistentEnrollment;
import ServidorPersistente.IPersistentEnrolmentEvaluation;
import ServidorPersistente.IPersistentTeacher;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;
import Util.EnrolmentEvaluationState;
import Util.TipoCurso;

/**
 * @author Fernanda Quit�rio 01/07/2003
 *  
 */
public class ReadStudentsAndMarksByCurricularCourse implements IServico {

    private static ReadStudentsAndMarksByCurricularCourse servico = new ReadStudentsAndMarksByCurricularCourse();

    /**
     * The singleton access method of this class.
     */
    public static ReadStudentsAndMarksByCurricularCourse getService() {
        return servico;
    }

    /**
     * The actor of this class.
     */
    private ReadStudentsAndMarksByCurricularCourse() {
    }

    /**
     * Returns The Service Name
     */

    public final String getNome() {
        return "ReadStudentsAndMarksByCurricularCourse";
    }

    public InfoSiteEnrolmentEvaluation run(Integer curricularCourseCode,
            String yearString) throws FenixServiceException {

        List infoEnrolmentEvaluations = new ArrayList();
        InfoTeacher infoTeacher = new InfoTeacher();
        Date lastEvaluationDate = null;
        try {
            ISuportePersistente sp = SuportePersistenteOJB.getInstance();
            IPersistentCurricularCourse persistentCurricularCourse = sp
                    .getIPersistentCurricularCourse();
            IPersistentEnrolmentEvaluation persistentEnrolmentEvaluation = sp
                    .getIPersistentEnrolmentEvaluation();
            IPersistentEnrollment persistentEnrolment = sp
                    .getIPersistentEnrolment();
            IPersistentTeacher persistentTeacher = sp.getIPersistentTeacher();

            ICurricularCourse curricularCourse = (ICurricularCourse) persistentCurricularCourse
                    .readByOID(CurricularCourse.class, curricularCourseCode);

            List enrolments = null;
            if (yearString != null) {
                enrolments = persistentEnrolment.readByCurricularCourseAndYear(
                        curricularCourse, yearString);
            } else {
                enrolments = persistentEnrolment
                        .readByCurricularCourse(curricularCourse);
            }
            List enrolmentEvaluations = new ArrayList();
            Iterator iterEnrolment = enrolments.listIterator();
            while (iterEnrolment.hasNext()) {
                IEnrollment enrolment = (IEnrollment) iterEnrolment.next();
                if (enrolment.getStudentCurricularPlan()
                        .getDegreeCurricularPlan().getDegree().getTipoCurso()
                        .equals(TipoCurso.MESTRADO_OBJ)) {
                    List allEnrolmentEvaluations = persistentEnrolmentEvaluation
                            .readEnrolmentEvaluationByEnrolment(enrolment);
                    IEnrolmentEvaluation enrolmentEvaluation = (IEnrolmentEvaluation) allEnrolmentEvaluations
                            .get(allEnrolmentEvaluations.size() - 1);
                    enrolmentEvaluations.add(enrolmentEvaluation);

                }
            }

            if (enrolmentEvaluations != null && enrolmentEvaluations.size() > 0) {
                //			in case we have evaluations they can be submitted only if
                // they are temporary
                List temporaryEnrolmentEvaluations = (List) CollectionUtils
                        .select(enrolmentEvaluations, new Predicate() {
                            public boolean evaluate(Object arg0) {
                                IEnrolmentEvaluation enrolmentEvaluation = (IEnrolmentEvaluation) arg0;
                                if (enrolmentEvaluation
                                        .getEnrolmentEvaluationState()
                                        .equals(
                                                EnrolmentEvaluationState.TEMPORARY_OBJ))
                                    return true;
                                return false;
                            }
                        });

                if (temporaryEnrolmentEvaluations == null
                        || temporaryEnrolmentEvaluations.size() == 0) {
                    throw new ExistingServiceException();
                }

                //				if (((IEnrolmentEvaluation) enrolmentEvaluations.get(0))
                //					.getEnrolmentEvaluationState()
                //					.equals(EnrolmentEvaluationState.FINAL_OBJ)) {
                //					throw new ExistingServiceException();
                //				}

                //				get teacher responsible for final evaluation - he is
                // responsible for all evaluations for this
                //				curricularCourse
                List enrolmentEvaluationsWithResponsiblePerson = (List) CollectionUtils
                        .select(enrolmentEvaluations, new Predicate() {
                            public boolean evaluate(Object arg0) {
                                IEnrolmentEvaluation enrolEval = (IEnrolmentEvaluation) arg0;
                                if (enrolEval.getPersonResponsibleForGrade() != null) {
                                    return true;
                                }
                                return false;
                            }
                        });
                if (enrolmentEvaluationsWithResponsiblePerson.size() > 0) {
                    IPessoa person = ((IEnrolmentEvaluation) enrolmentEvaluationsWithResponsiblePerson
                            .get(0)).getPersonResponsibleForGrade();
                    ITeacher teacher = persistentTeacher
                            .readTeacherByUsername(person.getUsername());
                    infoTeacher = Cloner.copyITeacher2InfoTeacher(teacher);
                }

                //				transform evaluations in databeans
                ListIterator iter = temporaryEnrolmentEvaluations
                        .listIterator();
                while (iter.hasNext()) {
                    IEnrolmentEvaluation elem = (IEnrolmentEvaluation) iter
                            .next();
                    InfoEnrolmentEvaluation infoEnrolmentEvaluation = Cloner
                            .copyIEnrolmentEvaluation2InfoEnrolmentEvaluation(elem);

                    infoEnrolmentEvaluation.setInfoEnrolment(Cloner
                            .copyIEnrolment2InfoEnrolment(elem.getEnrolment()));
                    infoEnrolmentEvaluations.add(infoEnrolmentEvaluation);
                }
            }

            if (infoEnrolmentEvaluations.size() == 0) {
                throw new NonExistingServiceException();
            }

            //				get last evaluation date to show in interface
            if (((InfoEnrolmentEvaluation) infoEnrolmentEvaluations.get(0))
                    .getExamDate() == null) {
                lastEvaluationDate = getLastEvaluationDate(yearString,
                        curricularCourse);
            } else {
                lastEvaluationDate = ((InfoEnrolmentEvaluation) infoEnrolmentEvaluations
                        .get(0)).getExamDate();
            }

        } catch (ExcepcaoPersistencia ex) {
            FenixServiceException newEx = new FenixServiceException(
                    "Persistence layer error");
            newEx.fillInStackTrace();
            throw newEx;
        }

        InfoSiteEnrolmentEvaluation infoSiteEnrolmentEvaluation = new InfoSiteEnrolmentEvaluation();
        infoSiteEnrolmentEvaluation
                .setEnrolmentEvaluations(infoEnrolmentEvaluations);
        infoSiteEnrolmentEvaluation.setInfoTeacher(infoTeacher);
        infoSiteEnrolmentEvaluation.setLastEvaluationDate(lastEvaluationDate);
        return infoSiteEnrolmentEvaluation;
    }

    private Date getLastEvaluationDate(String yearString,
            ICurricularCourse curricularCourse)
    //        throws ExcepcaoPersistencia, NonExistingServiceException
    {

        Date lastEvaluationDate = null;
        Iterator iterator = curricularCourse.getAssociatedExecutionCourses()
                .listIterator();
        while (iterator.hasNext()) {
            IExecutionCourse executionCourse = (IExecutionCourse) iterator
                    .next();
            if (executionCourse.getExecutionPeriod().getExecutionYear()
                    .getYear().equals(yearString)) {

                if (executionCourse.getAssociatedEvaluations() != null
                        && executionCourse.getAssociatedEvaluations().size() > 0) {
                    List evaluationsWithoutFinal = (List) CollectionUtils
                            .select(executionCourse.getAssociatedEvaluations(),
                                    new Predicate() {
                                        public boolean evaluate(Object input) {
                                            //for now returns only exams
                                            if (input instanceof IExam)
                                                return true;
                                            return false;
                                        }
                                    });

                    ComparatorChain comparatorChain = new ComparatorChain();
                    comparatorChain
                            .addComparator(new BeanComparator("day.time"));
                    comparatorChain.addComparator(new BeanComparator(
                            "beginning.time"));
                    Collections.sort(evaluationsWithoutFinal, comparatorChain);

                    if (evaluationsWithoutFinal.get(evaluationsWithoutFinal
                            .size() - 1) instanceof IExam) {
                        IExam lastEvaluation = (IExam) (evaluationsWithoutFinal
                                .get(evaluationsWithoutFinal.size() - 1));
                        if (lastEvaluationDate != null) {
                            if (lastEvaluationDate.before(lastEvaluation
                                    .getDay().getTime())) {
                                lastEvaluationDate = lastEvaluation.getDay()
                                        .getTime();
                            }
                        } else {
                            lastEvaluationDate = lastEvaluation.getDay()
                                    .getTime();
                        }
                    }
                }
            }
        }

        if (lastEvaluationDate == null) {
            Calendar calendar = Calendar.getInstance();
            lastEvaluationDate = calendar.getTime();
        }
        return lastEvaluationDate;
    }
}