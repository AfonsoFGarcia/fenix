package ServidorAplicacao.Servico.masterDegree.administrativeOffice.student.studentCurricularPlan;

import java.util.Calendar;
import java.util.Iterator;
import java.util.List;

import Dominio.Enrolment;
import Dominio.EnrolmentInExtraCurricularCourse;
import Dominio.IEmployee;
import Dominio.IEnrollment;
import Dominio.IPessoa;
import Dominio.IStudentCurricularPlan;
import Dominio.StudentCurricularPlan;
import ServidorAplicacao.IServico;
import ServidorAplicacao.IUserView;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorAplicacao.Servico.exceptions.InvalidArgumentsServiceException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentEmployee;
import ServidorPersistente.IPersistentEnrolment;
import ServidorPersistente.IPessoaPersistente;
import ServidorPersistente.IStudentCurricularPlanPersistente;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;
import Util.EnrolmentState;
import Util.StudentCurricularPlanState;

/**
 * @author Jo�o Mota
 * 15/Out/2003
 */

public class EditPosGradStudentCurricularPlanStateAndCredits implements IServico {

	private static EditPosGradStudentCurricularPlanStateAndCredits servico =
		new EditPosGradStudentCurricularPlanStateAndCredits();

	public static EditPosGradStudentCurricularPlanStateAndCredits getService() {
		return servico;
	}

	private EditPosGradStudentCurricularPlanStateAndCredits() {
	}

	public final String getNome() {
		return "EditPosGradStudentCurricularPlanStateAndCredits";
	}

	public void run(
		IUserView userView,
		Integer studentCurricularPlanId,
		String currentState,
		Double credits,
		String startDate,
		List extraCurricularCourses,
		String observations)
		throws FenixServiceException {

		try {
			ISuportePersistente sp = SuportePersistenteOJB.getInstance();
			
			IStudentCurricularPlanPersistente persistentStudentCurricularPlan =
				sp.getIStudentCurricularPlanPersistente();
			
			IStudentCurricularPlan studentCurricularPlanTemp = new StudentCurricularPlan();
			studentCurricularPlanTemp.setIdInternal(studentCurricularPlanId);

			IStudentCurricularPlan studentCurricularPlan =
				(IStudentCurricularPlan) persistentStudentCurricularPlan.readByOId(
					studentCurricularPlanTemp,
					true);
			if (studentCurricularPlan == null) {
				throw new InvalidArgumentsServiceException();
			}
			
			StudentCurricularPlanState newState = new StudentCurricularPlanState(currentState);
			
			IEmployee employee = null;
			IPersistentEmployee persistentEmployee = sp.getIPersistentEmployee();
			IPessoaPersistente persistentPerson = sp.getIPessoaPersistente();
			IPersistentEnrolment persistentEnrolment = sp.getIPersistentEnrolment();
			
			IPessoa person = persistentPerson.lerPessoaPorUsername(userView.getUtilizador());
			if (person == null) {
				throw new InvalidArgumentsServiceException();
			}
			
			employee = persistentEmployee.readByPerson(person.getIdInternal().intValue());
			if (employee == null) {
				throw new InvalidArgumentsServiceException();
			}

			studentCurricularPlan.setStartDate(stringDateToCalendar(startDate).getTime());
			studentCurricularPlan.setCurrentState(newState);
			studentCurricularPlan.setEmployee(employee);
			studentCurricularPlan.setGivenCredits(credits);
			studentCurricularPlan.setObservations(observations);
			
			List enrollments = studentCurricularPlan.getEnrolments();
			Iterator iterator = enrollments.iterator();
			if (newState.getState().intValue() == StudentCurricularPlanState.INACTIVE) {
				while (iterator.hasNext()) {
					IEnrollment enrolment = (IEnrollment) iterator.next();
					if (enrolment.getEnrolmentState().getValue() ==  EnrolmentState.ENROLED_TYPE
							|| enrolment.getEnrolmentState().getValue() ==  EnrolmentState.TEMPORARILY_ENROLED_TYPE) {
						persistentEnrolment.simpleLockWrite(enrolment);
						enrolment.setEnrolmentState(EnrolmentState.ANNULED);
					}
				}
			} else {
			   
				while (iterator.hasNext()) {
					IEnrollment enrolment = (IEnrollment) iterator.next();
					
					if (extraCurricularCourses.contains(enrolment.getIdInternal())) {
						if (!(enrolment instanceof EnrolmentInExtraCurricularCourse)) {
						    persistentEnrolment.delete(enrolment);
						    
							IEnrollment auxEnrolment = new EnrolmentInExtraCurricularCourse();	
							persistentEnrolment.simpleLockWrite(auxEnrolment);
						    
							copyEnrollment(enrolment, auxEnrolment);
							changeAnnulled2ActiveIfActivePlan(newState, persistentEnrolment, auxEnrolment);							
						} else {
							changeAnnulled2ActiveIfActivePlan(newState, persistentEnrolment, enrolment);						
						}
					} else {
						if (enrolment instanceof EnrolmentInExtraCurricularCourse) {
						    persistentEnrolment.delete(enrolment);

						    IEnrollment auxEnrolment = new Enrolment();
							persistentEnrolment.simpleLockWrite(auxEnrolment);
							
							copyEnrollment(enrolment, auxEnrolment);
							changeAnnulled2ActiveIfActivePlan(newState, persistentEnrolment, auxEnrolment);
						} else {
							changeAnnulled2ActiveIfActivePlan(newState, persistentEnrolment, enrolment);						
						}
					}
				}
			}
		} catch (ExcepcaoPersistencia e) {
			throw new FenixServiceException(e);
		}

	}

	/**
     * @param enrolment
     * @param auxEnrolment
     * @throws FenixServiceException
     */
    private void copyEnrollment(IEnrollment enrolment, IEnrollment auxEnrolment) throws FenixServiceException
    {
	    auxEnrolment.setIdInternal(enrolment.getIdInternal());
        //auxEnrolment.setCurricularCourseScope(
        //	enrolment.getCurricularCourseScope());
        auxEnrolment.setCurricularCourse(
        	enrolment.getCurricularCourse());
        auxEnrolment.setExecutionPeriod(enrolment.getExecutionPeriod());
        auxEnrolment.setStudentCurricularPlan(
        	enrolment.getStudentCurricularPlan());
        try {
        	auxEnrolment.setEnrolmentEvaluationType(
        		enrolment.getEnrolmentEvaluationType());
        	auxEnrolment.setEnrolmentState(enrolment.getEnrolmentState());
        	auxEnrolment.setEvaluations(enrolment.getEvaluations());
        } catch (Exception e1) {
        	throw new FenixServiceException(e1);
        }
    }

    /**
     * @param newState
     * @param persistentEnrolment
     * @param enrolment
     * @throws ExcepcaoPersistencia
     */
    private void changeAnnulled2ActiveIfActivePlan(StudentCurricularPlanState newState, IPersistentEnrolment persistentEnrolment, IEnrollment enrolment) throws ExcepcaoPersistencia
    {
        if (newState.getState().intValue() == StudentCurricularPlanState.ACTIVE) {
            if (enrolment.getEnrolmentState().getValue() ==  EnrolmentState.ANNULED_TYPE) {
        		persistentEnrolment.simpleLockWrite(enrolment);		
        		enrolment.setEnrolmentState(EnrolmentState.ENROLED);
        	}					    
        }
    }

    private Calendar stringDateToCalendar(String startDate) throws NumberFormatException {
		Calendar calendar = Calendar.getInstance();
		String[] aux = startDate.split("/");
		calendar.set(Calendar.DAY_OF_MONTH, (new Integer(aux[0])).intValue());
		calendar.set(Calendar.MONTH, (new Integer(aux[1])).intValue() - 1);
		calendar.set(Calendar.YEAR, (new Integer(aux[2])).intValue());
//		calendar.set(Calendar.HOUR_OF_DAY, 0);
//		calendar.set(Calendar.MINUTE, 0);
//		calendar.set(Calendar.SECOND, 0);
		return calendar;
	}

}