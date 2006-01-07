/*
 * Created on Mar 18, 2005
 * 
 */
package net.sourceforge.fenixedu.domain.inquiries;

import net.sourceforge.fenixedu.dataTransferObject.inquiries.InfoInquiriesCourse;
import net.sourceforge.fenixedu.dataTransferObject.inquiries.InfoInquiriesRoom;
import net.sourceforge.fenixedu.dataTransferObject.inquiries.InfoInquiriesTeacher;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.ExecutionDegree;
import net.sourceforge.fenixedu.domain.ExecutionPeriod;
import net.sourceforge.fenixedu.domain.NonAffiliatedTeacher;
import net.sourceforge.fenixedu.domain.SchoolClass;
import net.sourceforge.fenixedu.domain.Teacher;
import net.sourceforge.fenixedu.domain.ShiftType;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.space.Room;


/**
 * @author Jo�o Fialho & Rita Ferreira
 * 
 */
public class InquiriesCourse extends InquiriesCourse_Base {
	
	public InquiriesCourse() {
		super();
	}

	/**
	 * 
	 * @param executionCourse
	 * @param executionDegreeCourse
	 * @param executionDegreeStudent
	 * @param executionPeriod
	 * @param schoolClass
	 * @param infoInquiriesCourse
	 * 
	 * Preconditions: 
	 *   - executionCourse != null
	 *   - executionDegreeCourse != null
	 *   - executionDegreeStudent != null
	 *   - executionPeriod != null
	 * Postcondition:
	 *   - A new inquiriesCourse is created, referencing correctly the other domainObjects and its basic properties
	 *     are initialized
	 * Invariants:
	 *   - None
	 */
	public InquiriesCourse(ExecutionCourse executionCourse, ExecutionDegree executionDegreeCourse,
			ExecutionDegree executionDegreeStudent, ExecutionPeriod executionPeriod,
			SchoolClass schoolClass, InfoInquiriesCourse infoInquiriesCourse,
            Character entryGradeClassification, Character approvationRatioClassification, 
            Character arithmeticMeanClassification) {
		if((executionCourse == null) || (executionDegreeCourse == null) || (executionDegreeStudent == null) || (executionPeriod == null)) {
			throw new DomainException("Neither the executionCourse, executionDegreeCourse, executionDegreeStudent nor executionPeriod should not be null!");
		}
		this.setExecutionCourse(executionCourse);
		this.setExecutionDegreeCourse(executionDegreeCourse);
		this.setExecutionDegreeStudent(executionDegreeStudent);
		this.setExecutionPeriod(executionPeriod);
		this.setStudentSchoolClass(schoolClass);
        this.setEntryGradeClassification(entryGradeClassification);
        this.setApprovationRatioClassification(approvationRatioClassification);
        this.setArithmeticMeanClassification(arithmeticMeanClassification);
		
		this.setBasicProperties(infoInquiriesCourse);

	}

	public void createInquiriesTeacher(Teacher teacher, ShiftType shiftType, InfoInquiriesTeacher infoInquiriesTeacher) {        
		new InquiriesTeacher(this, teacher, shiftType, infoInquiriesTeacher);
	}
	
	public void createInquiriesTeacher(NonAffiliatedTeacher nonAffiliatedTeacher, ShiftType shiftType, InfoInquiriesTeacher infoInquiriesTeacher) {        
		new InquiriesTeacher(this, nonAffiliatedTeacher, shiftType, infoInquiriesTeacher);
	}
	
	public void createInquiriesRoom(Room room, InfoInquiriesRoom infoInquiriesRoom) {
		new InquiriesRoom(this, room, infoInquiriesRoom);
	}
	
	private void setBasicProperties(InfoInquiriesCourse infoInquiriesCourse) {
		this.setStudentCurricularYear(infoInquiriesCourse.getStudentCurricularYear());
		this.setStudentFirstEnrollment(infoInquiriesCourse.getStudentFirstEnrollment());
		this.setClassCoordination(infoInquiriesCourse.getClassCoordination());
		this.setStudyElementsContribution(infoInquiriesCourse.getStudyElementsContribution());
		this.setPreviousKnowledgeArticulation(infoInquiriesCourse.getPreviousKnowledgeArticulation());
		this.setContributionForGraduation(infoInquiriesCourse.getContributionForGraduation());
		this.setEvaluationMethodAdequation(infoInquiriesCourse.getEvaluationMethodAdequation());
		this.setWeeklySpentHours(infoInquiriesCourse.getWeeklySpentHours());
		this.setGlobalAppreciation(infoInquiriesCourse.getGlobalAppreciation());
	}
	
	
}
