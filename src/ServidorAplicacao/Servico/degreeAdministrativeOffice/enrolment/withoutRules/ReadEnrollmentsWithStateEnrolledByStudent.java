/*
 * Created on 17/Fev/2004
 *  
 */
package ServidorAplicacao.Servico.degreeAdministrativeOffice.enrolment.withoutRules;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.commons.beanutils.BeanComparator;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Transformer;

import pt.utl.ist.berserk.logic.serviceManager.IService;
import DataBeans.InfoEnrolmentWithCourseAndDegreeAndExecutionPeriodAndYear;
import DataBeans.InfoStudent;
import DataBeans.InfoStudentCurricularPlan;
import DataBeans.InfoStudentCurricularPlanWithInfoStudent;
import Dominio.ICursoExecucao;
import Dominio.IEnrollment;
import Dominio.IExecutionYear;
import Dominio.IStudentCurricularPlan;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorAplicacao.strategy.enrolment.context.InfoStudentEnrollmentContext;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.ICursoExecucaoPersistente;
import ServidorPersistente.IPersistentEnrolment;
import ServidorPersistente.IPersistentExecutionYear;
import ServidorPersistente.IStudentCurricularPlanPersistente;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;
import Util.EnrolmentState;
import Util.TipoCurso;

/**
 * @author T�nia Pous�o
 *  
 */
public class ReadEnrollmentsWithStateEnrolledByStudent implements IService {

    public ReadEnrollmentsWithStateEnrolledByStudent() {
    }

    public Object run(InfoStudent infoStudent, TipoCurso degreeType,
            String executionYear) throws FenixServiceException {
        InfoStudentEnrollmentContext infoStudentEnrolmentContext = null;
        try {
            ISuportePersistente sp = SuportePersistenteOJB.getInstance();
            IStudentCurricularPlanPersistente persistentStudentCurricularPlan = sp
                    .getIStudentCurricularPlanPersistente();

            IStudentCurricularPlan studentCurricularPlan = null;
            if (infoStudent != null && infoStudent.getNumber() != null) {
                studentCurricularPlan = persistentStudentCurricularPlan
                        .readActiveByStudentNumberAndDegreeType(infoStudent
                                .getNumber(), degreeType);
            }
            if (studentCurricularPlan == null) {
                throw new FenixServiceException(
                        "error.student.curriculum.noCurricularPlans");
            }

            if (isStudentCurricularPlanFromChosenExecutionYear(
                    studentCurricularPlan, executionYear)) {
                IPersistentEnrolment persistentEnrolment = sp
                        .getIPersistentEnrolment();
                List enrollments = persistentEnrolment
                        .readEnrolmentsByStudentCurricularPlanAndEnrolmentState(
                                studentCurricularPlan, EnrolmentState.ENROLLED);

                infoStudentEnrolmentContext = buildResult(
                        studentCurricularPlan, enrollments);

                if (infoStudentEnrolmentContext == null) {
                    throw new FenixServiceException();
                }
            } else {
                throw new FenixServiceException(
                        "error.student.curriculum.not.from.chosen.execution.year");
            }
        } catch (ExcepcaoPersistencia e) {
            
            throw new FenixServiceException(e);
        }

        return infoStudentEnrolmentContext;
    }

    /**
     * @param studentCurricularPlan
     * @param enrollments
     * @return
     */
    private InfoStudentEnrollmentContext buildResult(
            IStudentCurricularPlan studentCurricularPlan, List enrollments) {
        InfoStudentCurricularPlan infoStudentCurricularPlan = InfoStudentCurricularPlanWithInfoStudent.newInfoFromDomain(studentCurricularPlan);

        List infoEnrollments = new ArrayList();
        if (enrollments != null && enrollments.size() > 0) {
            infoEnrollments = (List) CollectionUtils.collect(enrollments,
                    new Transformer() {
                        public Object transform(Object input) {
                            IEnrollment enrolment = (IEnrollment) input;
                            return InfoEnrolmentWithCourseAndDegreeAndExecutionPeriodAndYear.newInfoFromDomain(enrolment);
                        }
                    });
            Collections.sort(infoEnrollments, new BeanComparator(
                    ("infoCurricularCourse.name")));
        }

        InfoStudentEnrollmentContext infoStudentEnrolmentContext = new InfoStudentEnrollmentContext();
        infoStudentEnrolmentContext
                .setInfoStudentCurricularPlan(infoStudentCurricularPlan);
        infoStudentEnrolmentContext
                .setStudentInfoEnrollmentsWithStateEnrolled(infoEnrollments);

        return infoStudentEnrolmentContext;
    }

    /**
     * @param studentCurricularPlan
     * @param year
     * @return true/false
     * @throws ExcepcaoPersistencia
     */
    private boolean isStudentCurricularPlanFromChosenExecutionYear(
            IStudentCurricularPlan studentCurricularPlan, String year)
            throws ExcepcaoPersistencia {
        ISuportePersistente sp = SuportePersistenteOJB.getInstance();
        ICursoExecucaoPersistente executionDegreeDAO = sp
                .getICursoExecucaoPersistente();
        IPersistentExecutionYear executionYearDAO = sp
                .getIPersistentExecutionYear();

        IExecutionYear executionYear = executionYearDAO
                .readExecutionYearByName(year);
        if (executionYear != null) {
            ICursoExecucao executionDegree = executionDegreeDAO
                    .readByDegreeCurricularPlanAndExecutionYear(
                            studentCurricularPlan.getDegreeCurricularPlan(),
                            executionYear);

            return executionDegree != null;
        }
        return false;

    }
}