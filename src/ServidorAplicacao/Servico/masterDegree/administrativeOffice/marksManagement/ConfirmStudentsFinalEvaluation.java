package ServidorAplicacao.Servico.masterDegree.administrativeOffice.marksManagement;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

import Dominio.CurricularCourse;
import Dominio.Funcionario;
import Dominio.ICurricularCourse;
import Dominio.IEnrolment;
import Dominio.IEnrolmentEvaluation;
import Dominio.IPessoa;
import ServidorAplicacao.IServico;
import ServidorAplicacao.IUserView;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentCurricularCourse;
import ServidorPersistente.IPersistentEnrolment;
import ServidorPersistente.IPersistentEnrolmentEvaluation;
import ServidorPersistente.IPessoaPersistente;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;
import ServidorPersistenteJDBC.IFuncionarioPersistente;
import ServidorPersistenteJDBC.SuportePersistente;
import Util.EnrolmentEvaluationState;
import Util.EnrolmentState;

/**
 * @author Fernanda Quit�rio
 * 10/07/2003
 *
 */
public class ConfirmStudentsFinalEvaluation implements IServico {
	private static ConfirmStudentsFinalEvaluation _servico = new ConfirmStudentsFinalEvaluation();

	/**
		* The actor of this class.
		**/
	private ConfirmStudentsFinalEvaluation() {

	}

	/**
	 * Returns Service Name
	 */
	public String getNome() {
		return "ConfirmStudentsFinalEvaluation";
	}

	/**
	 * Returns the _servico.
	 * @return ReadExecutionCourse
	 */
	public static ConfirmStudentsFinalEvaluation getService() {
		return _servico;
	}

	public Boolean run(Integer curricularCourseCode, String yearString, IUserView userView) throws FenixServiceException {

		try {
			ISuportePersistente sp = SuportePersistenteOJB.getInstance();
			IPersistentCurricularCourse persistentCurricularCourse = sp.getIPersistentCurricularCourse();
			IPersistentEnrolmentEvaluation persistentEnrolmentEvaluation = sp.getIPersistentEnrolmentEvaluation();
			IPersistentEnrolment persistentEnrolment = sp.getIPersistentEnrolment();
			IPessoaPersistente persistentPerson = sp.getIPessoaPersistente();

			//			employee
			IPessoa person = persistentPerson.lerPessoaPorUsername(userView.getUtilizador());
			Funcionario employee = readEmployee(person);

			ICurricularCourse curricularCourse = new CurricularCourse();
			curricularCourse.setIdInternal(curricularCourseCode);
			curricularCourse = (ICurricularCourse) persistentCurricularCourse.readByOId(curricularCourse, false);

			List enrolments = persistentEnrolment.readByCurricularCourse(curricularCourse, yearString);
			List enrolmentEvaluations = new ArrayList();
			Iterator iterEnrolment = enrolments.listIterator();
			while (iterEnrolment.hasNext()) {
				IEnrolment enrolment = (IEnrolment) iterEnrolment.next();
				List allEnrolmentEvaluations = (List) persistentEnrolmentEvaluation.readEnrolmentEvaluationByEnrolment(enrolment);
				IEnrolmentEvaluation enrolmentEvaluation =
					(IEnrolmentEvaluation) allEnrolmentEvaluations.get(allEnrolmentEvaluations.size() - 1);
				enrolmentEvaluations.add(enrolmentEvaluation);
			}

			if (enrolmentEvaluations != null && enrolmentEvaluations.size() > 0) {
				ListIterator iterEnrolmentEvaluations = enrolmentEvaluations.listIterator();
				while (iterEnrolmentEvaluations.hasNext()) {
					IEnrolmentEvaluation enrolmentEvaluationElem = (IEnrolmentEvaluation) iterEnrolmentEvaluations.next();
					persistentEnrolmentEvaluation.simpleLockWrite(enrolmentEvaluationElem);

					enrolmentEvaluationElem.setEnrolmentEvaluationState(EnrolmentEvaluationState.FINAL_OBJ);
					Calendar calendar = Calendar.getInstance();
					enrolmentEvaluationElem.setWhen(calendar.getTime());
					enrolmentEvaluationElem.setEmployee(employee);
//					TODO: checksum
					enrolmentEvaluationElem.setCheckSum("");
					
					// update state of enrolment: aproved or notAproved
					IEnrolment enrolmentToEdit = enrolmentEvaluationElem.getEnrolment();
					persistentEnrolment.simpleLockWrite(enrolmentToEdit);
					
					EnrolmentState newEnrolmentState = EnrolmentState.APROVED;
					try{
						Integer grade = new Integer(enrolmentEvaluationElem.getGrade()); 
					} catch(NumberFormatException e){
						newEnrolmentState = EnrolmentState.NOT_APROVED;
					}
					enrolmentToEdit.setEnrolmentState(newEnrolmentState);
				}
			}
		} catch (ExcepcaoPersistencia ex) {
			ex.printStackTrace();
			FenixServiceException newEx = new FenixServiceException("");
			newEx.fillInStackTrace();
			throw newEx;
		}

		return Boolean.TRUE;
	}

	private Funcionario readEmployee(IPessoa person) {
		Funcionario employee = null;
		SuportePersistente spJDBC = SuportePersistente.getInstance();
		IFuncionarioPersistente persistentEmployee = spJDBC.iFuncionarioPersistente();

		try {
			spJDBC.iniciarTransaccao();

			try {
				employee = persistentEmployee.lerFuncionarioPorPessoa(person.getIdInternal().intValue());

			} catch (Exception e) {
				spJDBC.cancelarTransaccao();
				e.printStackTrace();
				return employee;
			}

			spJDBC.confirmarTransaccao();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			return employee;
		}
	}
}