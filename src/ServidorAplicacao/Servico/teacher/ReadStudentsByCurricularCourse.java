package ServidorAplicacao.Servico.teacher;

import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Transformer;

import pt.utl.ist.berserk.logic.serviceManager.IService;
import DataBeans.ISiteComponent;
import DataBeans.InfoCurricularCourse;
import DataBeans.InfoSiteCommon;
import DataBeans.InfoSiteStudents;
import DataBeans.InfoStudent;
import DataBeans.InfoStudentWithInfoPerson;
import DataBeans.TeacherAdministrationSiteView;
import Dominio.CurricularCourse;
import Dominio.ExecutionCourse;
import Dominio.ICurricularCourse;
import Dominio.IEnrollment;
import Dominio.IExecutionCourse;
import Dominio.IFrequenta;
import Dominio.ISite;
import Dominio.IStudent;
import ServidorAplicacao.Factory.TeacherAdministrationSiteComponentBuilder;
import ServidorAplicacao.Servico.ExcepcaoInexistente;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IFrequentaPersistente;
import ServidorPersistente.IPersistentCurricularCourse;
import ServidorPersistente.IPersistentEnrollment;
import ServidorPersistente.IPersistentSite;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;

/**
 * @author Fernanda Quit�rio
 * @author T�nia Pous�o
 * @author �ngela
 *  
 */
public class ReadStudentsByCurricularCourse implements IService {

    public ReadStudentsByCurricularCourse() {

    }

    public Object run(Integer executionCourseCode, Integer courseCode) throws ExcepcaoInexistente,
            FenixServiceException {
        List infoStudentList = null;
        ISite site = null;
        ICurricularCourse curricularCourse = null;
        try {
            ISuportePersistente sp = SuportePersistenteOJB.getInstance();

            //execution course's site
            IExecutionCourse executionCourse = new ExecutionCourse();
            executionCourse.setIdInternal(executionCourseCode);

            IPersistentSite persistentSite = sp.getIPersistentSite();
            site = persistentSite.readByExecutionCourse(executionCourse);

            if (courseCode == null) {
                infoStudentList = getAllAttendingStudents(sp, executionCourse);
            } else {
                IPersistentCurricularCourse persistentCurricularCourse = sp
                        .getIPersistentCurricularCourse();
                curricularCourse = (ICurricularCourse) persistentCurricularCourse.readByOID(
                        CurricularCourse.class, courseCode);

                infoStudentList = getCurricularCourseStudents(curricularCourse, sp);

            }

            TeacherAdministrationSiteView siteView = createSiteView(infoStudentList, site,
                    curricularCourse);
            return siteView;

        } catch (ExcepcaoPersistencia ex) {
            ex.printStackTrace();
            FenixServiceException newEx = new FenixServiceException("");
            newEx.fillInStackTrace();
            throw newEx;
        }

    }

    private List getCurricularCourseStudents(ICurricularCourse curricularCourse, ISuportePersistente sp)
            throws ExcepcaoPersistencia {
        List infoStudentList;
        IPersistentEnrollment persistentEnrolment = sp.getIPersistentEnrolment();

        List enrolments = persistentEnrolment.readByCurricularCourse(curricularCourse);

        infoStudentList = (List) CollectionUtils.collect(enrolments, new Transformer() {
            public Object transform(Object input) {
                IEnrollment enrolment = (IEnrollment) input;
                IStudent student = enrolment.getStudentCurricularPlan().getStudent();
                //CLONER
                //InfoStudent infoStudent =
                // Cloner.copyIStudent2InfoStudent(student);
                InfoStudent infoStudent = InfoStudentWithInfoPerson.newInfoFromDomain(student);
                return infoStudent;
            }
        });
        return infoStudentList;
    }

    private List getAllAttendingStudents(ISuportePersistente sp, IExecutionCourse executionCourse)
            throws ExcepcaoPersistencia {
        List infoStudentList;
        //	all students that attend this execution course
        IFrequentaPersistente frequentaPersistente = sp.getIFrequentaPersistente();
        List attendList = frequentaPersistente.readByExecutionCourse(executionCourse);

        infoStudentList = (List) CollectionUtils.collect(attendList, new Transformer() {

            public Object transform(Object input) {
                IFrequenta attend = (IFrequenta) input;
                IStudent student = attend.getAluno();
                //CLONER
                //InfoStudent infoStudent =
                // Cloner.copyIStudent2InfoStudent(student);
                InfoStudent infoStudent = InfoStudentWithInfoPerson.newInfoFromDomain(student);
                return infoStudent;
            }
        });
        return infoStudentList;
    }

    private TeacherAdministrationSiteView createSiteView(List infoStudentList, ISite site,
            ICurricularCourse curricularCourse) throws FenixServiceException {
        InfoSiteStudents infoSiteStudents = new InfoSiteStudents();
        infoSiteStudents.setStudents(infoStudentList);

        if (curricularCourse != null) {
            //CLONER
            //infoSiteStudents.setInfoCurricularCourse(Cloner.copyCurricularCourse2InfoCurricularCourse(curricularCourse));
            infoSiteStudents.setInfoCurricularCourse(InfoCurricularCourse
                    .newInfoFromDomain(curricularCourse));
        }

        TeacherAdministrationSiteComponentBuilder componentBuilder = new TeacherAdministrationSiteComponentBuilder();
        ISiteComponent commonComponent = componentBuilder.getComponent(new InfoSiteCommon(), site, null,
                null, null);

        TeacherAdministrationSiteView siteView = new TeacherAdministrationSiteView(commonComponent,
                infoSiteStudents);
        return siteView;
    }
}