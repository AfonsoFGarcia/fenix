/*
 * Created on Mar 18, 2005
 * 
 */
package net.sourceforge.fenixedu.domain.inquiries;

import java.util.Map;

import net.sourceforge.fenixedu.dataTransferObject.inquiries.AffiliatedTeacherDTO;
import net.sourceforge.fenixedu.dataTransferObject.inquiries.InfoInquiriesTeacher;
import net.sourceforge.fenixedu.dataTransferObject.inquiries.InquiriesQuestion;
import net.sourceforge.fenixedu.dataTransferObject.inquiries.NonAffiliatedTeacherDTO;
import net.sourceforge.fenixedu.dataTransferObject.inquiries.TeacherInquiryDTO;
import net.sourceforge.fenixedu.domain.NonAffiliatedTeacher;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.Professorship;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.ShiftType;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;

/**
 * @author Jo�o Fialho & Rita Ferreira
 * 
 */
public class InquiriesTeacher extends InquiriesTeacher_Base {

    public InquiriesTeacher() {
	super();
	setRootDomainObject(RootDomainObject.getInstance());
    }

    protected InquiriesTeacher(InquiriesCourse inquiriesCourse, Professorship professorship, ShiftType shiftType,
	    InfoInquiriesTeacher infoInquiriesTeacher) {
	this();
	if ((inquiriesCourse == null) || (professorship == null) || (shiftType == null))
	    throw new DomainException("The inquiriesCourse, teacher and shiftType should not be null!");

	this.setInquiriesCourse(inquiriesCourse);
	this.setProfessorship(professorship);
	this.setBasicProperties(shiftType, infoInquiriesTeacher);
    }

    protected InquiriesTeacher(InquiriesCourse inquiriesCourse, NonAffiliatedTeacher nonAffiliatedTeacher, ShiftType shiftType,
	    InfoInquiriesTeacher infoInquiriesTeacher) {
	this();
	if ((inquiriesCourse == null) || (nonAffiliatedTeacher == null) || (shiftType == null))
	    throw new DomainException("The inquiriesCourse, nonAffiliatedTeacher and shiftType should not be null!");

	this.setInquiriesCourse(inquiriesCourse);
	this.setNonAffiliatedTeacher(nonAffiliatedTeacher);
	this.setBasicProperties(shiftType, infoInquiriesTeacher);
    }

    private void setBasicProperties(ShiftType shiftType, InfoInquiriesTeacher infoInquiriesTeacher) {
	this.setShiftType(shiftType);
	this.setStudentAssiduity(infoInquiriesTeacher.getStudentAssiduity());
	this.setTeacherAssiduity(infoInquiriesTeacher.getTeacherAssiduity());
	this.setTeacherPunctuality(infoInquiriesTeacher.getTeacherPunctuality());
	this.setTeacherClarity(infoInquiriesTeacher.getTeacherClarity());
	this.setTeacherAssurance(infoInquiriesTeacher.getTeacherAssurance());
	this.setTeacherInterestStimulation(infoInquiriesTeacher.getTeacherInterestStimulation());
	this.setTeacherAvailability(infoInquiriesTeacher.getTeacherAvailability());
	this.setTeacherReasoningStimulation(infoInquiriesTeacher.getTeacherReasoningStimulation());
	this.setGlobalAppreciation(infoInquiriesTeacher.getGlobalAppreciation());

    }

    public static InquiriesTeacher makeNew(TeacherInquiryDTO inquiryDTO) {
	InquiriesTeacher inquiriesTeacher = new InquiriesTeacher();
	inquiriesTeacher.setShiftType(inquiryDTO.getShiftType());

	setAnswers(inquiryDTO, inquiriesTeacher);

	if (inquiryDTO.getTeacherDTO() instanceof AffiliatedTeacherDTO) {
	    final Professorship professorship = inquiryDTO.getExecutionCourse().getProfessorship((Person) inquiryDTO.getTeacherDTO().getTeacher());
	    if (professorship == null) {
		throw new Error("This should never be possible.");
	    }
	    inquiriesTeacher.setProfessorship(professorship);
//	    inquiriesTeacher.setTeacher((Teacher) inquiryDTO.getTeacherDTO().getTeacher());
	} else if (inquiryDTO.getTeacherDTO() instanceof NonAffiliatedTeacherDTO) {
	    inquiriesTeacher.setNonAffiliatedTeacher((NonAffiliatedTeacher) inquiryDTO.getTeacherDTO().getTeacher());
	}

	return inquiriesTeacher;
    }

    private static void setAnswers(TeacherInquiryDTO inquiryDTO, InquiriesTeacher inquiriesTeacher) {
	Map<String, InquiriesQuestion> answersMap = inquiryDTO.buildAnswersMap(false);

	inquiriesTeacher.setClassesFrequency(answersMap.get("classesFrequency").getValueAsInteger());
	inquiriesTeacher.setLowClassesFrequencyReasonSchedule(answersMap.get("lowClassesFrequencyReasonSchedule")
		.getValueAsBoolean());
	inquiriesTeacher.setLowClassesFrequencyReasonTeacher(answersMap.get("lowClassesFrequencyReasonTeacher")
		.getValueAsBoolean());
	inquiriesTeacher.setLowClassesFrequencyReasonContents(answersMap.get("lowClassesFrequencyReasonContents")
		.getValueAsBoolean());
	inquiriesTeacher.setLowClassesFrequencyReasonFlunkeeStudent(answersMap.get("lowClassesFrequencyReasonFlunkeeStudent")
		.getValueAsBoolean());
	inquiriesTeacher.setLowClassesFrequencyReasonOther(answersMap.get("lowClassesFrequencyReasonOther").getValueAsBoolean());
	inquiriesTeacher.setTeacherAcomplishedScheduleAndActivities(answersMap.get("teacherAcomplishedScheduleAndActivities")
		.getValueAsInteger());
	inquiriesTeacher.setSuitedClassesRythm(answersMap.get("suitedClassesRythm").getValueAsInteger());
	inquiriesTeacher.setTeacherCommited(answersMap.get("teacherCommited").getValueAsInteger());
	inquiriesTeacher.setTeacherExposedContentsAtractively(answersMap.get("teacherExposedContentsAtractively")
		.getValueAsInteger());
	inquiriesTeacher.setTeacherShowedSecurity(answersMap.get("teacherShowedSecurity").getValueAsInteger());
	inquiriesTeacher.setTeacherExposedContentsClearly(answersMap.get("teacherExposedContentsClearly").getValueAsInteger());
	inquiriesTeacher.setTeacherStimulatedParticipation(answersMap.get("teacherStimulatedParticipation").getValueAsInteger());
	inquiriesTeacher.setTeacherOpenToClearDoubts(answersMap.get("teacherOpenToClearDoubts").getValueAsInteger());
	inquiriesTeacher.setTeacherGlobalClassification(answersMap.get("teacherGlobalClassification").getValueAsInteger());
    }
}
