package ServidorAplicacao.Servico.degreeAdministrativeOffice.enrolment.withoutRules;

import java.util.Iterator;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;

import pt.utl.ist.berserk.logic.serviceManager.IService;
import Dominio.ICurricularCourse;
import Dominio.IEnrolment;
import Dominio.IEnrolmentInOptionalCurricularCourse;
import ServidorAplicacao.Servico.enrolment.degree.ChangeEnrolmentStateFromTemporarilyToEnroled;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorAplicacao.strategy.enrolment.context.EnrolmentContext;
import ServidorAplicacao.strategy.enrolment.context.EnrolmentContextManager;
import ServidorAplicacao.strategy.enrolment.context.InfoEnrolmentContext;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentEnrolment;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;
import ServidorPersistente.exceptions.ExistingPersistentException;
import Util.CurricularCourseType;
import Util.EnrolmentEvaluationType;
import Util.EnrolmentState;

/**
 * @author David Santos
 */

public class ConfirmActualOptionalEnrolmentWithoutRules implements IService
{

    public ConfirmActualOptionalEnrolmentWithoutRules()
    {
    }

    public InfoEnrolmentContext run(InfoEnrolmentContext infoEnrolmentContext)
        throws FenixServiceException
    {
        EnrolmentContext enrolmentContext =
            EnrolmentContextManager.getEnrolmentContext(infoEnrolmentContext);
        try
        {
            this.writeTemporaryEnrolment(enrolmentContext);
        }
        catch (ExcepcaoPersistencia e)
        {
            throw new FenixServiceException(e);
        }
        return infoEnrolmentContext;
    }

    private void writeTemporaryEnrolment(EnrolmentContext enrolmentContext) throws ExcepcaoPersistencia
    {
        ISuportePersistente sp = null;
        IPersistentEnrolment persistentEnrolment = null;
        try
        {
            sp = SuportePersistenteOJB.getInstance();
            persistentEnrolment = sp.getIPersistentEnrolment();

            List studentEnrolments =
                persistentEnrolment.readAllByStudentCurricularPlan(
                    enrolmentContext.getStudentActiveCurricularPlan());

            // List of all enrolments in optional curricular courses with state
			// 'enrolled' and 'temporarily enrolled'.
            List studentEnroledAndTemporarilyEnroledOptionalEnrolments =
                (List) CollectionUtils.select(studentEnrolments, new Predicate()
            {
                public boolean evaluate(Object obj)
                {
                    IEnrolment enrolment = (IEnrolment) obj;
                    return (
                        enrolment.getEnrolmentState().equals(EnrolmentState.ENROLED)
                            || enrolment.getEnrolmentState().equals(EnrolmentState.TEMPORARILY_ENROLED))
                        && enrolment.getCurricularCourse().getType().equals(
                            CurricularCourseType.OPTIONAL_COURSE_OBJ);
                }
            });

            List enrolmentsToDelete =
                (List) CollectionUtils.subtract(
                    studentEnroledAndTemporarilyEnroledOptionalEnrolments,
                    enrolmentContext.getOptionalCurricularCoursesEnrolments());
            // Delete from data base the enrolments that don't mather.
            Iterator iterator = enrolmentsToDelete.iterator();
            while (iterator.hasNext())
            {
                IEnrolment enrolment = (IEnrolment) iterator.next();
                ConfirmActualEnrolmentWithoutRules.deleteAttendAndEnrolmentEvaluations(enrolment);
                persistentEnrolment.delete(enrolment);
            }

            iterator = enrolmentContext.getOptionalCurricularCoursesEnrolments().iterator();
            while (iterator.hasNext())
            {
                IEnrolmentInOptionalCurricularCourse enrolmentInOptionalCurricularCourse =
                    (IEnrolmentInOptionalCurricularCourse) iterator.next();

                IEnrolmentInOptionalCurricularCourse enrolment =
                    //				(IEnrolmentInOptionalCurricularCourse)
					// persistentEnrolment
        //						.readByStudentCurricularPlanAndCurricularCourseScopeAndExecutionPeriod(
        //					enrolmentInOptionalCurricularCourse.getStudentCurricularPlan(),
        //					enrolmentInOptionalCurricularCourse.getCurricularCourseScope(),
        //					enrolmentInOptionalCurricularCourse.getExecutionPeriod());

    (
        IEnrolmentInOptionalCurricularCourse) persistentEnrolment
            .readByStudentCurricularPlanAndCurricularCourseAndExecutionPeriod(
        enrolmentInOptionalCurricularCourse.getStudentCurricularPlan(),
        enrolmentInOptionalCurricularCourse.getCurricularCourse(),
        enrolmentInOptionalCurricularCourse.getExecutionPeriod());

                ICurricularCourse chosenCurricularCourseForOption =
                    enrolmentInOptionalCurricularCourse.getCurricularCourseForOption();

                //PROBLEMAS NO IF!!
                if (enrolment == null)
                {

                    enrolmentInOptionalCurricularCourse.setCurricularCourse(
                        chosenCurricularCourseForOption);
                    enrolmentInOptionalCurricularCourse.setCurricularCourseForOption(
                        chosenCurricularCourseForOption);
                    enrolmentInOptionalCurricularCourse.setExecutionPeriod(
                        enrolmentContext.getExecutionPeriod());
                    enrolmentInOptionalCurricularCourse.setStudentCurricularPlan(
                        enrolmentContext.getStudentActiveCurricularPlan());
                    enrolmentInOptionalCurricularCourse.setEnrolmentEvaluationType(
                        EnrolmentEvaluationType.NORMAL_OBJ);
                    enrolmentInOptionalCurricularCourse.setEnrolmentState(EnrolmentState.ENROLED);
                    persistentEnrolment.lockWrite(enrolmentInOptionalCurricularCourse);
                    ChangeEnrolmentStateFromTemporarilyToEnroled.createAttend(
                        enrolmentInOptionalCurricularCourse);
                    ChangeEnrolmentStateFromTemporarilyToEnroled.createEnrolmentEvaluation(
                        enrolmentInOptionalCurricularCourse);
                }
                else
                {
                    persistentEnrolment.lockWrite(enrolment);
                    enrolment.setCurricularCourseForOption(chosenCurricularCourseForOption);
                    enrolment.setEnrolmentState(EnrolmentState.ENROLED);
                }
            }
        }
        catch (ExistingPersistentException e1)
        {
            e1.printStackTrace();
            throw e1;
        }
        catch (ExcepcaoPersistencia e)
        {
            e.printStackTrace();
            throw e;
        }
    }
}