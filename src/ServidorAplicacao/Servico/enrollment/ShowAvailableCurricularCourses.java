package ServidorAplicacao.Servico.enrollment;

import java.util.Date;

import Dominio.IEnrolmentPeriod;
import Dominio.IExecutionPeriod;
import Dominio.IStudent;
import Dominio.IStudentCurricularPlan;
import ServidorAplicacao.Servico.exceptions.ExistingServiceException;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorAplicacao.Servico.exceptions.OutOfCurricularCourseEnrolmentPeriod;
import ServidorAplicacao.strategy.enrolment.context.InfoStudentEnrolmentContext;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentEnrolmentPeriod;
import ServidorPersistente.IPersistentExecutionPeriod;
import ServidorPersistente.IPersistentStudent;
import ServidorPersistente.IStudentCurricularPlanPersistente;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;
import Util.TipoCurso;

/*
 * 
 * @author Fernanda Quit�rio 12/Fev/2004
 *  
 */
public class ShowAvailableCurricularCourses extends
		ShowAvailableCurricularCoursesWithoutEnrollmentPeriod {
	public ShowAvailableCurricularCourses() {
	}

	// some of these arguments may be null. they are only needed for filter
	public InfoStudentEnrolmentContext run(Integer executionDegreeId,
			Integer studentCurricularPlanId, Integer studentNumber)
			throws FenixServiceException {
		try {
			IStudent student = getStudent(studentNumber);

			if (student != null) {
				IStudentCurricularPlan studentCurricularPlan = getStudentCurricularPlan(student);

				if (studentCurricularPlan != null) {
					IEnrolmentPeriod enrolmentPeriod = getEnrolmentPeriod(studentCurricularPlan);
					IExecutionPeriod executionPeriod = getCurrentExecutionPeriod();
					if (executionPeriod.equals(enrolmentPeriod
							.getExecutionPeriod())) {
						try {
							return executeServiceLogics(studentCurricularPlan);
						} catch (IllegalArgumentException e) {
							throw new FenixServiceException("degree");
						}
					} else {
						throw new FenixServiceException("enrolmentPeriod");
					}
				} else {
					throw new ExistingServiceException("studentCurricularPlan");
				}
			} else {
				throw new ExistingServiceException("student");
			}
		} catch (ExcepcaoPersistencia e) {
			e.printStackTrace();
			throw new FenixServiceException(e);
		}
	}

	/**
	 * @param studentActiveCurricularPlan
	 * @return IEnrolmentPeriod
	 * @throws ExcepcaoPersistencia
	 * @throws OutOfCurricularCourseEnrolmentPeriod
	 */
	public static IEnrolmentPeriod getEnrolmentPeriod(
			IStudentCurricularPlan studentActiveCurricularPlan)
			throws ExcepcaoPersistencia, OutOfCurricularCourseEnrolmentPeriod {
		ISuportePersistente persistentSuport = SuportePersistenteOJB
				.getInstance();
		IPersistentEnrolmentPeriod enrolmentPeriodDAO = persistentSuport
				.getIPersistentEnrolmentPeriod();
		IEnrolmentPeriod enrolmentPeriod = enrolmentPeriodDAO
				.readActualEnrolmentPeriodForDegreeCurricularPlan(studentActiveCurricularPlan
						.getDegreeCurricularPlan());
		if (enrolmentPeriod == null) {
			IEnrolmentPeriod nextEnrolmentPeriod = enrolmentPeriodDAO
					.readNextEnrolmentPeriodForDegreeCurricularPlan(studentActiveCurricularPlan
							.getDegreeCurricularPlan());
			Date startDate = null;
			Date endDate = null;
			if (nextEnrolmentPeriod != null) {
				startDate = nextEnrolmentPeriod.getStartDate();
				endDate = nextEnrolmentPeriod.getEndDate();
			}
			throw new OutOfCurricularCourseEnrolmentPeriod(startDate, endDate);
		}
		return enrolmentPeriod;
	}

	/**
	 * @param studentNumber
	 * @return IStudent
	 * @throws ExcepcaoPersistencia
	 */
	private IStudent getStudent(Integer studentNumber)
			throws ExcepcaoPersistencia {
		ISuportePersistente persistentSuport = SuportePersistenteOJB
				.getInstance();
		IPersistentStudent studentDAO = persistentSuport
				.getIPersistentStudent();

		return studentDAO.readStudentByNumberAndDegreeType(studentNumber,
				TipoCurso.LICENCIATURA_OBJ);
	}

	/**
	 * @param student
	 * @return IStudentCurricularPlan
	 * @throws ExcepcaoPersistencia
	 */
	private IStudentCurricularPlan getStudentCurricularPlan(IStudent student)
			throws ExcepcaoPersistencia {
		ISuportePersistente persistentSuport = SuportePersistenteOJB
				.getInstance();
		IStudentCurricularPlanPersistente studentCurricularPlanDAO = persistentSuport
				.getIStudentCurricularPlanPersistente();

		return studentCurricularPlanDAO.readActiveStudentCurricularPlan(student
				.getNumber(), student.getDegreeType());
	}

	/**
	 * @return IExecutionPeriod
	 * @throws ExcepcaoPersistencia
	 */
	private IExecutionPeriod getCurrentExecutionPeriod()
			throws ExcepcaoPersistencia {
		ISuportePersistente persistentSuport = SuportePersistenteOJB
				.getInstance();
		IPersistentExecutionPeriod executionPeriodDAO = persistentSuport
				.getIPersistentExecutionPeriod();

		return executionPeriodDAO.readActualExecutionPeriod();
	}

}