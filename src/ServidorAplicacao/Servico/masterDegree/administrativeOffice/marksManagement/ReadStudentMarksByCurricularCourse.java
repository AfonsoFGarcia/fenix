package ServidorAplicacao.Servico.masterDegree.administrativeOffice.marksManagement;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;

import pt.utl.ist.berserk.logic.serviceManager.IService;
import DataBeans.InfoEnrolmentEvaluation;
import DataBeans.InfoSiteEnrolmentEvaluation;
import DataBeans.InfoTeacher;
import DataBeans.util.Cloner;
import Dominio.CurricularCourse;
import Dominio.Enrolment;
import Dominio.EnrolmentEvaluation;
import Dominio.ICurricularCourse;
import Dominio.IEnrolment;
import Dominio.IEnrolmentEvaluation;
import Dominio.IPessoa;
import Dominio.IStudentCurricularPlan;
import Dominio.ITeacher;
import Dominio.Pessoa;
import ServidorAplicacao.Servico.exceptions.ExistingServiceException;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorPersistente.ExcepcaoPersistencia;
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
public class ReadStudentMarksByCurricularCourse implements IService
{

	/**
	 * The actor of this class.
	 */
	public ReadStudentMarksByCurricularCourse()
	{
	}

	public List run(Integer curricularCourseID, Integer studentNumber, String executionYear)
		throws FenixServiceException
	{
		List enrolmentEvaluations = null;
		InfoTeacher infoTeacher = null;
		List infoSiteEnrolmentEvaluations = new ArrayList();
		IEnrolment enrolment = new Enrolment();
		IEnrolmentEvaluation enrolmentEvaluation = new EnrolmentEvaluation();

		try
		{
			ISuportePersistente sp = SuportePersistenteOJB.getInstance();
			IPersistentEnrolmentEvaluation persistentEnrolmentEvaluation =
				sp.getIPersistentEnrolmentEvaluation();
			IPersistentTeacher persistentTeacher = sp.getIPersistentTeacher();

			//read curricularCourse by ID
			ICurricularCourse curricularCourse = new CurricularCourse();
			curricularCourse.setIdInternal(curricularCourseID);
			curricularCourse =
				(ICurricularCourse) sp.getIPersistentCurricularCourse().readByOId(
					curricularCourse,
					false);

			final ICurricularCourse curricularCourseTemp = curricularCourse;

			//			IStudentCurricularPlan studentCurricularPlan =
			//				sp.getIStudentCurricularPlanPersistente().readActiveStudentCurricularPlan(
			//					studentNumber,
			//					TipoCurso.MESTRADO_OBJ);
			//
			//			if (studentCurricularPlan == null)
			//			{
			//
			//				throw new ExistingServiceException();
			//			}

			// get student curricular Plan
			// in case student has school part concluded his curricular plan is not in active state
			
			List studentCurricularPlans =
				sp.getIStudentCurricularPlanPersistente().readByStudentNumberAndDegreeType(
					studentNumber,
					TipoCurso.MESTRADO_OBJ);

			IStudentCurricularPlan studentCurricularPlan =
				(IStudentCurricularPlan) CollectionUtils.find(studentCurricularPlans, new Predicate()
			{
				public boolean evaluate(Object object)
				{
					IStudentCurricularPlan studentCurricularPlanElem = (IStudentCurricularPlan) object;
					if (studentCurricularPlanElem
						.getDegreeCurricularPlan()
						.equals(curricularCourseTemp.getDegreeCurricularPlan()))
					{
						return true;
					}
					return false;
				}
			});
			if (studentCurricularPlan == null)
			{
				throw new ExistingServiceException();
			}
			//			}
			if (executionYear != null)
			{
				enrolment =
					sp
						.getIPersistentEnrolment()
						.readEnrolmentByStudentCurricularPlanAndCurricularCourse(
						studentCurricularPlan,
						curricularCourse,
						executionYear);
			}
			else
			{
				// TODO: N�o se sabe se este comportamento est� correcto!
				List enrollments =
					sp.getIPersistentEnrolment().readByStudentCurricularPlanAndCurricularCourse(
						studentCurricularPlan,
						curricularCourse);
				
				enrolment = (IEnrolment) enrollments.get(0);
			}

			if (enrolment != null)
			{
				//						ListIterator iter1 = enrolments.listIterator();
				//						while (iter1.hasNext()) {
				//							enrolment = (IEnrolment) iter1.next();

				EnrolmentEvaluationState enrolmentEvaluationState =
					new EnrolmentEvaluationState(EnrolmentEvaluationState.FINAL);
				enrolmentEvaluations =
					persistentEnrolmentEvaluation.readEnrolmentEvaluationByEnrolmentEvaluationState(
						enrolment,
						enrolmentEvaluationState);
				//				enrolmentEvaluations = enrolment.getEvaluations();

				List infoTeachers = new ArrayList();
				if (enrolmentEvaluations != null && enrolmentEvaluations.size() > 0)
				{
					IPessoa person =
						((IEnrolmentEvaluation) enrolmentEvaluations.get(0))
							.getPersonResponsibleForGrade();
					ITeacher teacher = persistentTeacher.readTeacherByUsername(person.getUsername());
					infoTeacher = Cloner.copyITeacher2InfoTeacher(teacher);
					infoTeachers.add(infoTeacher);
				}

				List infoEnrolmentEvaluations = new ArrayList();
				if (enrolmentEvaluations != null && enrolmentEvaluations.size() > 0)
				{
					ListIterator iter = enrolmentEvaluations.listIterator();
					while (iter.hasNext())
					{
						enrolmentEvaluation = (IEnrolmentEvaluation) iter.next();
						InfoEnrolmentEvaluation infoEnrolmentEvaluation =
							Cloner.copyIEnrolmentEvaluation2InfoEnrolmentEvaluation(enrolmentEvaluation);
						infoEnrolmentEvaluation.setInfoEnrolment(
							Cloner.copyIEnrolment2InfoEnrolment(enrolmentEvaluation.getEnrolment()));

						if (enrolmentEvaluation != null)
						{
							if (enrolmentEvaluation.getEmployee() != null)
							{
								IPessoa person = new Pessoa();
								person.setIdInternal(
									enrolmentEvaluation.getEmployee().getPerson().getIdInternal());
								IPessoa person2 =
									(IPessoa) sp.getIPessoaPersistente().readByOId(person, false);
								infoEnrolmentEvaluation.setInfoEmployee(
									Cloner.copyIPerson2InfoPerson(person2));
							}

						}
						infoEnrolmentEvaluations.add(infoEnrolmentEvaluation);
					}

				}
				InfoSiteEnrolmentEvaluation infoSiteEnrolmentEvaluation =
					new InfoSiteEnrolmentEvaluation();
				infoSiteEnrolmentEvaluation.setEnrolmentEvaluations(infoEnrolmentEvaluations);
				infoSiteEnrolmentEvaluation.setInfoTeacher(infoTeacher);
				infoSiteEnrolmentEvaluations.add(infoSiteEnrolmentEvaluation);

			}
		}
		catch (ExcepcaoPersistencia ex)
		{
			ex.printStackTrace();
			FenixServiceException newEx = new FenixServiceException("Persistence layer error");
			newEx.fillInStackTrace();
			throw newEx;
		}
		return infoSiteEnrolmentEvaluations;
	}
}