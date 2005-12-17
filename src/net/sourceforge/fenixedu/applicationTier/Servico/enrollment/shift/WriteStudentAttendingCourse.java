package net.sourceforge.fenixedu.applicationTier.Servico.enrollment.shift;

import java.util.Iterator;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoStudent;
import net.sourceforge.fenixedu.domain.DomainFactory;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.IAttends;
import net.sourceforge.fenixedu.domain.ICurricularCourse;
import net.sourceforge.fenixedu.domain.IEnrolment;
import net.sourceforge.fenixedu.domain.IExecutionCourse;
import net.sourceforge.fenixedu.domain.IStudent;
import net.sourceforge.fenixedu.domain.IStudentCurricularPlan;
import net.sourceforge.fenixedu.domain.Student;
import net.sourceforge.fenixedu.domain.degree.DegreeType;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IFrequentaPersistente;
import net.sourceforge.fenixedu.persistenceTier.IPersistentEnrollment;
import net.sourceforge.fenixedu.persistenceTier.IPersistentExecutionCourse;
import net.sourceforge.fenixedu.persistenceTier.IPersistentStudent;
import net.sourceforge.fenixedu.persistenceTier.IPersistentStudentCurricularPlan;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;
import pt.utl.ist.berserk.logic.serviceManager.IService;

public class WriteStudentAttendingCourse implements IService {

	public class ReachedAttendsLimitServiceException extends
			FenixServiceException {

	}

	public Boolean run(InfoStudent infoStudent, Integer executionCourseId)
			throws FenixServiceException, ExcepcaoPersistencia {
		ISuportePersistente sp = PersistenceSupportFactory
				.getDefaultPersistenceSupport();
		IFrequentaPersistente persistentAttend = sp.getIFrequentaPersistente();
		IPersistentExecutionCourse persistentExecutionCourse = sp
				.getIPersistentExecutionCourse();
		IPersistentEnrollment persistentEnrolment = sp
				.getIPersistentEnrolment();
		IPersistentStudentCurricularPlan persistentStudentCurricularPlan = sp
				.getIStudentCurricularPlanPersistente();

		IPersistentStudent studentDAO = sp.getIPersistentStudent();
		IStudent student = (IStudent) studentDAO.readByOID(Student.class,
				infoStudent.getIdInternal());
		infoStudent.setNumber(student.getNumber());

		if (infoStudent == null) {
			return new Boolean(false);
		}

		if (executionCourseId != null) {
			IStudentCurricularPlan studentCurricularPlan = findStudentCurricularPlan(
					infoStudent, persistentStudentCurricularPlan);

			IExecutionCourse executionCourse = findExecutionCourse(
					executionCourseId, persistentExecutionCourse);

			// Read every course the student attends to:
			List attends = persistentAttend
					.readByStudentNumberInCurrentExecutionPeriod(studentCurricularPlan
							.getStudent().getNumber());

			if (attends != null && attends.size() >= 8) {
				throw new ReachedAttendsLimitServiceException();
			}

			IAttends attendsEntry = persistentAttend
					.readByAlunoAndDisciplinaExecucao(studentCurricularPlan
							.getStudent().getIdInternal(), executionCourse
							.getIdInternal());

			if (attendsEntry == null) {
				attendsEntry = DomainFactory.makeAttends();
				attendsEntry.setAluno(studentCurricularPlan.getStudent());
				attendsEntry.setDisciplinaExecucao(executionCourse);

				findEnrollmentForAttend(persistentEnrolment,
						studentCurricularPlan, executionCourse, attendsEntry);

			}
		}
		return new Boolean(true);
	}

	private void findEnrollmentForAttend(
			IPersistentEnrollment persistentEnrolment,
			IStudentCurricularPlan studentCurricularPlan,
			IExecutionCourse executionCourse, IAttends attendsEntry)
			throws ExcepcaoPersistencia {
		// checks if there is an enrollment for this attend
		Iterator iterCurricularCourses = executionCourse
				.getAssociatedCurricularCourses().iterator();
		while (iterCurricularCourses.hasNext()) {
			ICurricularCourse curricularCourseElem = (ICurricularCourse) iterCurricularCourses
					.next();

			IEnrolment enrollment = persistentEnrolment
					.readByStudentCurricularPlanAndCurricularCourseAndExecutionPeriod(
							studentCurricularPlan.getIdInternal(),
							curricularCourseElem.getIdInternal(),
							executionCourse.getExecutionPeriod()
									.getIdInternal());
			if (enrollment != null) {
				attendsEntry.setEnrolment(enrollment);
				break;
			}
		}
	}

	private IExecutionCourse findExecutionCourse(Integer executionCourseId,
			IPersistentExecutionCourse persistentExecutionCourse)
			throws FenixServiceException, ExcepcaoPersistencia {
		IExecutionCourse executionCourse = (IExecutionCourse) persistentExecutionCourse
				.readByOID(ExecutionCourse.class, executionCourseId);

		if (executionCourse == null) {
			throw new FenixServiceException("noExecutionCourse");
		}
		return executionCourse;
	}

	private IStudentCurricularPlan findStudentCurricularPlan(
			InfoStudent infoStudent,
			IPersistentStudentCurricularPlan persistentStudentCurricularPlan)
			throws FenixServiceException, ExcepcaoPersistencia {
		IStudentCurricularPlan studentCurricularPlan = persistentStudentCurricularPlan
				.readActiveByStudentNumberAndDegreeType(
						infoStudent.getNumber(), DegreeType.DEGREE);

		if (studentCurricularPlan == null) {

			studentCurricularPlan = persistentStudentCurricularPlan
					.readActiveByStudentNumberAndDegreeType(infoStudent
							.getNumber(), DegreeType.MASTER_DEGREE);

		}
		if (studentCurricularPlan == null) {

			throw new FenixServiceException("noStudent");

		}

		return studentCurricularPlan;
	}
}