package ServidorAplicacao.Servico.equivalence;

import java.util.Date;
import java.util.Iterator;

import DataBeans.InfoCurricularCourseScope;
import DataBeans.InfoEnrolment;
import DataBeans.InfoExecutionPeriod;
import DataBeans.InfoStudentCurricularPlan;
import DataBeans.equivalence.InfoCurricularCourseScopeGrade;
import DataBeans.equivalence.InfoEquivalenceContext;
import DataBeans.util.Cloner;
import Dominio.Enrolment;
import Dominio.EnrolmentEquivalence;
import Dominio.EquivalentEnrolmentForEnrolmentEquivalence;
import Dominio.EnrolmentEvaluation;
import Dominio.ICurricularCourseScope;
import Dominio.IEmployee;
import Dominio.IEnrolment;
import Dominio.IEnrolmentEquivalence;
import Dominio.IEquivalentEnrolmentForEnrolmentEquivalence;
import Dominio.IEnrolmentEvaluation;
import Dominio.IExecutionPeriod;
import Dominio.IPessoa;
import Dominio.IStudentCurricularPlan;
import ServidorAplicacao.IServico;
import ServidorAplicacao.IUserView;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentEmployee;
import ServidorPersistente.IPersistentEnrolment;
import ServidorPersistente.IPersistentEnrolmentEquivalence;
import ServidorPersistente.IPersistentEquivalentEnrolmentForEnrolmentEquivalence;
import ServidorPersistente.IPersistentEnrolmentEvaluation;
import ServidorPersistente.IPessoaPersistente;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;
import ServidorPersistente.exceptions.ExistingPersistentException;
import ServidorPersistenteJDBC.SuportePersistente;
import Util.EnrolmentEvaluationState;
import Util.EnrolmentEvaluationType;
import Util.EnrolmentState;
/**
 * @author David Santos
 * 9/Jul/2003
 */

public class ConfirmEquivalence implements IServico {

	private static ConfirmEquivalence service = new ConfirmEquivalence();

	public static ConfirmEquivalence getService() {
		return ConfirmEquivalence.service;
	}

	private ConfirmEquivalence() {
	}

	public final String getNome() {
		return "ConfirmEquivalence";
	}

	public InfoEquivalenceContext run(InfoEquivalenceContext infoEquivalenceContext) throws FenixServiceException {

		try {
			ISuportePersistente persistentSupport = SuportePersistenteOJB.getInstance();
			IPersistentEnrolment persistentEnrolment = persistentSupport.getIPersistentEnrolment();
			IPersistentEnrolmentEquivalence persistentEnrolmentEquivalence = persistentSupport.getIPersistentEnrolmentEquivalence();
			IPersistentEquivalentEnrolmentForEnrolmentEquivalence persistentEnrolmentEquivalenceRestriction =
				persistentSupport.getIPersistentEquivalentEnrolmentForEnrolmentEquivalence();
			IPersistentEnrolmentEvaluation persistentEnrolmentEvaluation = persistentSupport.getIPersistentEnrolmentEvaluation();
			IPessoaPersistente pessoaPersistente = persistentSupport.getIPessoaPersistente();
			SuportePersistente sp = SuportePersistente.getInstance();

			Iterator scopesToGetEquivalenceIterator =
				infoEquivalenceContext.getChosenInfoCurricularCourseScopesToGetEquivalenceWithGrade().iterator();
			while (scopesToGetEquivalenceIterator.hasNext()) {
				InfoCurricularCourseScopeGrade infoCurricularCourseScopeGrade =
					(InfoCurricularCourseScopeGrade) scopesToGetEquivalenceIterator.next();

				IEnrolment newEnrolment = createNewEnrolment(infoEquivalenceContext, infoCurricularCourseScopeGrade, persistentEnrolment);

				createNewEnrolmentEvaluation(
					infoEquivalenceContext,
					newEnrolment,
					infoCurricularCourseScopeGrade,
					pessoaPersistente,
					persistentEnrolmentEvaluation,
					sp);

				IEnrolmentEquivalence enrolmentEquivalence = createNewEnrolmentEquivalence(newEnrolment, persistentEnrolmentEquivalence);

				Iterator infoEnrolmentsToGiveEquivalenceIterator =
					infoEquivalenceContext.getChosenInfoEnrolmentsToGiveEquivalence().iterator();
				while (infoEnrolmentsToGiveEquivalenceIterator.hasNext()) {
					InfoEnrolment infoEnrolmentToGiveEquivalence = (InfoEnrolment) infoEnrolmentsToGiveEquivalenceIterator.next();
					createNewEnrolmentEquivalenceRestriction(
						enrolmentEquivalence,
						infoEnrolmentToGiveEquivalence,
						persistentEnrolmentEquivalenceRestriction);
				}
			}
		} catch (ExistingPersistentException e1) {
			throw new FenixServiceException(e1);
		} catch (ExcepcaoPersistencia e1) {
			throw new FenixServiceException(e1);
		}

		return infoEquivalenceContext;
	}

	private IEquivalentEnrolmentForEnrolmentEquivalence createNewEnrolmentEquivalenceRestriction(
		IEnrolmentEquivalence enrolmentEquivalence,
		InfoEnrolment infoEnrolmentToGiveEquivalence,
		IPersistentEquivalentEnrolmentForEnrolmentEquivalence persistentEnrolmentEquivalenceRestriction)
		throws ExistingPersistentException, ExcepcaoPersistencia {
		try {
			IEquivalentEnrolmentForEnrolmentEquivalence enrolmentEquivalenceRestriction = new EquivalentEnrolmentForEnrolmentEquivalence();
			enrolmentEquivalenceRestriction.setEnrolmentEquivalence(enrolmentEquivalence);
			enrolmentEquivalenceRestriction.setEquivalentEnrolment(Cloner.copyInfoEnrolment2IEnrolment(infoEnrolmentToGiveEquivalence));
			persistentEnrolmentEquivalenceRestriction.lockWrite(enrolmentEquivalenceRestriction);
			return enrolmentEquivalenceRestriction;
		} catch (ExistingPersistentException e) {
			throw e;
		} catch (ExcepcaoPersistencia e) {
			throw e;
		}
	}

	private IEnrolmentEquivalence createNewEnrolmentEquivalence(
		IEnrolment newEnrolment,
		IPersistentEnrolmentEquivalence persistentEnrolmentEquivalence)
		throws ExistingPersistentException, ExcepcaoPersistencia {
		try {
			IEnrolmentEquivalence enrolmentEquivalence = new EnrolmentEquivalence();
			enrolmentEquivalence.setEnrolment(newEnrolment);
			persistentEnrolmentEquivalence.lockWrite(enrolmentEquivalence);
			return enrolmentEquivalence;
		} catch (ExistingPersistentException e) {
			throw e;
		} catch (ExcepcaoPersistencia e) {
			throw e;
		}
	}

	private IEnrolmentEvaluation createNewEnrolmentEvaluation(
		InfoEquivalenceContext infoEquivalenceContext,
		IEnrolment newEnrolment,
		InfoCurricularCourseScopeGrade infoCurricularCourseScopeGrade,
		IPessoaPersistente pessoaPersistente,
		IPersistentEnrolmentEvaluation persistentEnrolmentEvaluation,
		SuportePersistente spJdbc)
		throws ExistingPersistentException, ExcepcaoPersistencia {
		try {
			IUserView userView = infoEquivalenceContext.getResponsible();
			IPessoa pessoa = pessoaPersistente.lerPessoaPorUsername(userView.getUtilizador());
			IEmployee employee = readEmployee(pessoa);

			IEnrolmentEvaluation enrolmentEvaluation = new EnrolmentEvaluation();
			enrolmentEvaluation.setEnrolment(newEnrolment);
			enrolmentEvaluation.setEnrolmentEvaluationState(EnrolmentEvaluationState.FINAL_OBJ);
			enrolmentEvaluation.setEnrolmentEvaluationType(EnrolmentEvaluationType.EQUIVALENCE_OBJ);
			enrolmentEvaluation.setGrade(infoCurricularCourseScopeGrade.getGrade());
			enrolmentEvaluation.setPersonResponsibleForGrade(pessoa);
			enrolmentEvaluation.setEmployee(employee);
			enrolmentEvaluation.setWhen(new Date());
			// TODO [DAVID]: Quando o algoritmo do checksum estiver feito tem de ser actualizar este campo
			enrolmentEvaluation.setCheckSum(null);
			persistentEnrolmentEvaluation.lockWrite(enrolmentEvaluation);
			return enrolmentEvaluation;
		} catch (ExistingPersistentException e) {
			throw e;
		} catch (ExcepcaoPersistencia e) {
			throw e;
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

	private IEnrolment createNewEnrolment(
		InfoEquivalenceContext infoEquivalenceContext,
		InfoCurricularCourseScopeGrade infoCurricularCourseScopeGrade,
		IPersistentEnrolment persistentEnrolment)
		throws ExistingPersistentException, ExcepcaoPersistencia {
		IEnrolment newEnrolment;
		try {
			InfoCurricularCourseScope infoCurricularCourseScopeToEnrol = infoCurricularCourseScopeGrade.getInfoCurricularCourseScope();
			ICurricularCourseScope curricularCourseScopeToEnrol =
				Cloner.copyInfoCurricularCourseScope2ICurricularCourseScope(infoCurricularCourseScopeToEnrol);

			InfoExecutionPeriod infoExecutionPeriod = infoEquivalenceContext.getCurrentInfoExecutionPeriod();
			IExecutionPeriod executionPeriod = Cloner.copyInfoExecutionPeriod2IExecutionPeriod(infoExecutionPeriod);

			InfoStudentCurricularPlan infoStudentCurricularPlan = infoEquivalenceContext.getInfoStudentCurricularPlan();
			IStudentCurricularPlan studentCurricularPlan =
				Cloner.copyInfoStudentCurricularPlan2IStudentCurricularPlan(infoStudentCurricularPlan);

			newEnrolment = new Enrolment();

			newEnrolment.setCurricularCourseScope(curricularCourseScopeToEnrol);
			newEnrolment.setEnrolmentEvaluationType(EnrolmentEvaluationType.EQUIVALENCE_OBJ);
			newEnrolment.setEnrolmentState(EnrolmentState.APROVED);
			newEnrolment.setExecutionPeriod(executionPeriod);
			newEnrolment.setStudentCurricularPlan(studentCurricularPlan);

			persistentEnrolment.lockWrite(newEnrolment);
			return newEnrolment;
		} catch (ExistingPersistentException e) {
			throw e;
		} catch (ExcepcaoPersistencia e) {
			throw e;
		}
	}
}