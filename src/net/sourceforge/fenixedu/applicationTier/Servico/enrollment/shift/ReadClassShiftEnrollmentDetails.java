/*
 * Created on 11/Fev/2004
 */
package net.sourceforge.fenixedu.applicationTier.Servico.enrollment.shift;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoClass;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionCourse;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionCourseWithExecutionPeriod;
import net.sourceforge.fenixedu.dataTransferObject.InfoShift;
import net.sourceforge.fenixedu.dataTransferObject.InfoShiftWithInfoExecutionCourse;
import net.sourceforge.fenixedu.dataTransferObject.InfoShiftWithInfoLessons;
import net.sourceforge.fenixedu.dataTransferObject.InfoStudent;
import net.sourceforge.fenixedu.dataTransferObject.enrollment.shift.ExecutionCourseShiftEnrollmentDetails;
import net.sourceforge.fenixedu.dataTransferObject.enrollment.shift.InfoClassEnrollmentDetails;
import net.sourceforge.fenixedu.dataTransferObject.enrollment.shift.ShiftEnrollmentDetails;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.ExecutionPeriod;
import net.sourceforge.fenixedu.domain.SchoolClass;
import net.sourceforge.fenixedu.domain.Shift;
import net.sourceforge.fenixedu.domain.Student;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentExecutionPeriod;
import net.sourceforge.fenixedu.persistenceTier.IPersistentStudent;
import net.sourceforge.fenixedu.persistenceTier.ITurmaPersistente;
import net.sourceforge.fenixedu.persistenceTier.ITurnoPersistente;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Transformer;

/**
 * @author jpvl
 */
public class ReadClassShiftEnrollmentDetails extends Service {

	public class StudentNotFoundServiceException extends FenixServiceException {
	}

	public InfoClassEnrollmentDetails run(InfoStudent infoStudent,
			Integer klassId) throws FenixServiceException, ExcepcaoPersistencia {

		InfoClassEnrollmentDetails enrollmentDetails = null;

		IPersistentStudent studentDAO = persistentSupport.getIPersistentStudent();
		ITurmaPersistente classDAO = persistentSupport.getITurmaPersistente();
		IPersistentExecutionPeriod executionPeriodDAO = persistentSupport
				.getIPersistentExecutionPeriod();
		ITurnoPersistente shiftDAO = persistentSupport.getITurnoPersistente();

		// Current Execution OccupationPeriod
		ExecutionPeriod executionPeriod = executionPeriodDAO
				.readActualExecutionPeriod();

		// Student
		Student student = readStudent(infoStudent, studentDAO);

		// Classes
		List classList = classDAO
				.readClassesThatContainsStudentAttendsOnExecutionPeriod(student
						.getIdInternal(), executionPeriod.getIdInternal());

		// Class selected
		SchoolClass klass = null;
		if (klassId != null) {
			klass = (SchoolClass) classDAO.readByOID(SchoolClass.class,
					klassId);
		}

		// Shifts correspond to student attends
		List shiftAttendList = shiftDAO
				.readShiftsThatContainsStudentAttendsOnExecutionPeriod(student
						.getIdInternal(), executionPeriod.getIdInternal());

		// Shifts enrolment
		List<Shift> shifts = student.getShifts();
		List studentShifts = new ArrayList();
		for (Shift shift : shifts) {
			if (shift.getDisciplinaExecucao().getExecutionPeriod()
					.getIdInternal().equals(executionPeriod.getIdInternal()))
				studentShifts.add(shift);
		}

		List infoShifts = collectInfoShifts(studentShifts);

		List infoClassList = new ArrayList();
		Map classExecutionCourseShiftEnrollmentDetailsMap = createMapAndPopulateInfoClassList(
				classList, shiftAttendList, infoClassList, klass);

		enrollmentDetails = new InfoClassEnrollmentDetails();
		enrollmentDetails.setInfoStudent(copyIStudent2InfoStudent(student));
		enrollmentDetails.setInfoShiftEnrolledList(infoShifts);
		enrollmentDetails.setInfoClassList(infoClassList);
		enrollmentDetails
				.setClassExecutionCourseShiftEnrollmentDetailsMap(classExecutionCourseShiftEnrollmentDetailsMap);

		return enrollmentDetails;
	}

	/**
	 * @param shifts
	 */
	private List collectInfoShifts(List shifts) {
		/* Prepare return */
		List infoShifts = (List) CollectionUtils.collect(shifts,
				new Transformer() {

					public Object transform(Object input) {
						Shift shift = (Shift) input;
						InfoShift infoShift = InfoShiftWithInfoExecutionCourse
								.newInfoFromDomain(shift);
						return infoShift;
					}
				});

		return infoShifts;
	}

	/**
	 * @param shiftStudentDAO
	 * @param classList
	 * @param shifts
	 * @param infoClassList
	 * @return
	 * @throws ExcepcaoPersistencia
	 */
	private Map createMapAndPopulateInfoClassList(List classList,
			List shiftsAttendList, List infoClassList, SchoolClass klassToTreat)
			throws ExcepcaoPersistencia {
		Map classExecutionCourseShiftEnrollmentDetailsMap = new HashMap();

		/* shift id -> ShiftEnrollmentDetails */
		Map shiftsTreated = new HashMap();

		/* executionCourse id -> ExecutionCourseShiftEnrollmentDetails */
		Map executionCourseTreated = new HashMap();

		for (int i = 0; i < classList.size(); i++) {
			// Clean auxiliar
			shiftsTreated.clear();
			executionCourseTreated.clear();

			// Clone class
			SchoolClass klass = (SchoolClass) classList.get(i);
			InfoClass infoClass = InfoClass.newInfoFromDomain(klass);
			infoClassList.add(infoClass);

			Integer klassId = klass.getIdInternal();
			if ((klassToTreat == null && i == 0)
					|| (klassToTreat != null && klassId.equals(klassToTreat
							.getIdInternal()))) {

				List shiftsRequired = (List) CollectionUtils.intersection(klass
						.getAssociatedShifts(), shiftsAttendList);
				if (shiftsRequired != null) {
					for (int j = 0; j < shiftsRequired.size(); j++) {
						Shift shift = (Shift) shiftsRequired.get(j);

						ShiftEnrollmentDetails shiftEnrollmentDetails = createShiftEnrollmentDetails(
								shiftsTreated, shift);

						ExecutionCourseShiftEnrollmentDetails executionCourseShiftEnrollmentDetails = createExecutionCourseShiftEnrollmentDetails(
								executionCourseTreated, shift);
						executionCourseShiftEnrollmentDetails
								.addShiftEnrollmentDetails(shiftEnrollmentDetails);

						List executionCourseShiftEnrollmentDetailsList = (List) classExecutionCourseShiftEnrollmentDetailsMap
								.get(klassId);
						if (executionCourseShiftEnrollmentDetailsList == null) {
							executionCourseShiftEnrollmentDetailsList = new ArrayList();
							executionCourseShiftEnrollmentDetailsList
									.add(executionCourseShiftEnrollmentDetails);
							classExecutionCourseShiftEnrollmentDetailsMap.put(
									klassId,
									executionCourseShiftEnrollmentDetailsList);
						} else {
							if (!executionCourseShiftEnrollmentDetailsList
									.contains(executionCourseShiftEnrollmentDetails)) {
								executionCourseShiftEnrollmentDetailsList
										.add(executionCourseShiftEnrollmentDetails);
							}
						}
					}
				}
			}
		}
		return classExecutionCourseShiftEnrollmentDetailsMap;
	}

	/**
	 * @param executionCourseTreated
	 * @param shift
	 * @return
	 */
	private ExecutionCourseShiftEnrollmentDetails createExecutionCourseShiftEnrollmentDetails(
			Map executionCourseTreated, Shift shift) {
		ExecutionCourse executionCourse = shift.getDisciplinaExecucao();
		ExecutionCourseShiftEnrollmentDetails executionCourseShiftEnrollmentDetails = (ExecutionCourseShiftEnrollmentDetails) executionCourseTreated
				.get(executionCourse.getIdInternal());

		if (executionCourseShiftEnrollmentDetails == null) {
			executionCourseShiftEnrollmentDetails = new ExecutionCourseShiftEnrollmentDetails();
			InfoExecutionCourse infoExecutionCourse = InfoExecutionCourseWithExecutionPeriod
					.newInfoFromDomain(executionCourse);
			executionCourseShiftEnrollmentDetails
					.setInfoExecutionCourse(infoExecutionCourse);

			executionCourseTreated.put(executionCourse.getIdInternal(),
					executionCourseShiftEnrollmentDetails);
		}

		return executionCourseShiftEnrollmentDetails;
	}

	/**
	 * @param shiftStudentDAO
	 * @param shiftsTreated
	 * @param shift
	 * @return
	 * @throws ExcepcaoPersistencia
	 */
	private ShiftEnrollmentDetails createShiftEnrollmentDetails(
			Map shiftsTreated, Shift shift) throws ExcepcaoPersistencia {
		ShiftEnrollmentDetails shiftEnrollmentDetails = (ShiftEnrollmentDetails) shiftsTreated
				.get(shift.getIdInternal());
		if (shiftEnrollmentDetails == null) {
			shiftEnrollmentDetails = new ShiftEnrollmentDetails();

			InfoShift infoShift = InfoShiftWithInfoLessons
					.newInfoFromDomain(shift);
			int occupation = shift.getStudents().size();
			shiftEnrollmentDetails.setInfoShift(infoShift);
			shiftEnrollmentDetails.setVacancies(new Integer(shift.getLotacao()
					.intValue()
					- occupation));

			shiftsTreated.put(shift.getIdInternal(), shiftEnrollmentDetails);
		}
		return shiftEnrollmentDetails;
	}

	/**
	 * @param infoStudent
	 * @param studentDAO
	 * @return
	 * @throws StudentNotFoundServiceException
	 * @throws ExcepcaoPersistencia
	 */
	private Student readStudent(InfoStudent infoStudent,
			IPersistentStudent studentDAO)
			throws StudentNotFoundServiceException, ExcepcaoPersistencia {
		Student student = (Student) studentDAO.readByOID(Student.class,
				infoStudent.getIdInternal());
		if (student == null) {
			throw new StudentNotFoundServiceException();
		}
		return student;
	}

	/**
	 * @param student
	 * @return
	 */
	private InfoStudent copyIStudent2InfoStudent(Student student) {
		InfoStudent infoStudent = null;
		if (student != null) {
			infoStudent = new InfoStudent();
			infoStudent.setIdInternal(student.getIdInternal());

		}
		return infoStudent;
	}

}
