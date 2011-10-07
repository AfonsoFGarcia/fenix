package net.sourceforge.fenixedu.presentationTier.Action.coordinator.thesis;

import java.io.Serializable;

import net.sourceforge.fenixedu.domain.CurricularCourse;
import net.sourceforge.fenixedu.domain.Enrolment;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.finalDegreeWork.GroupStudent;
import net.sourceforge.fenixedu.domain.finalDegreeWork.Proposal;
import net.sourceforge.fenixedu.domain.student.Student;
import net.sourceforge.fenixedu.domain.thesis.Thesis;
import pt.utl.ist.fenix.tools.util.i18n.MultiLanguageString;

public class StudentThesisInfo implements Serializable {

    private Student student;
    private Enrolment enrolment;
    private Thesis thesis;
    private ThesisPresentationState state;

    public StudentThesisInfo(Enrolment enrolment) {
	setStudent(enrolment.getStudent());
	setEnrolment(enrolment);
	setThesis(enrolment.getThesis());
    }

    public Student getStudent() {
	return this.student;
    }

    protected void setStudent(Student student) {
	this.student = student;
    }

    public Enrolment getEnrolment() {
	return this.enrolment;
    }

    protected void setEnrolment(Enrolment enrolment) {
	this.enrolment = enrolment;
    }

    public Thesis getThesis() {
	return this.thesis;
    }

    protected void setThesis(Thesis thesis) {
	this.thesis = thesis;

	setState(thesis);
    }

    public String getSemester() {
	final Enrolment enrolment = getEnrolment();
	final CurricularCourse curricularCourse = enrolment.getCurricularCourse();
	return curricularCourse.isAnual() ? "" : enrolment.getExecutionPeriod().getSemester().toString();
    }

    public MultiLanguageString getTitle() {
	return getEnrolment().getPossibleDissertationTitle();
    }

    public ThesisPresentationState getState() {
	return this.state;
    }

    private void setState(Thesis thesis) {
	this.state = ThesisPresentationState.getThesisPresentationState(thesis);
    }

    public String getThesisId() {
	final Thesis thesis = getThesis();

	return thesis == null ? null : thesis.getExternalId();
    }

    public String getEnrolmentOID() {
	final Enrolment enrolment = getEnrolment();
	return enrolment == null ? null : enrolment.getExternalId();
    }

    public boolean isUnassigned() {
	return getThesis() == null;
    }

    public boolean isDraft() {
	return getThesis() != null && getThesis().isDraft();
    }

    public boolean isSubmitted() {
	return getThesis() != null && getThesis().isSubmitted();
    }

    public boolean isWaitingConfirmation() {
	return getThesis() != null && getThesis().isWaitingConfirmation();
    }

    public boolean isConfirmed() {
	return getThesis() != null && getThesis().isConfirmed();
    }

    public boolean isEvaluated() {
	return getThesis() != null && getThesis().isEvaluated();
    }

    public boolean isPreEvaluated() {
	return isEvaluated() && !getThesis().isFinalThesis();
    }

    public boolean isSubmittedAndIsCoordinatorAndNotOrientator() {
	return getThesis() != null && getThesis().isSubmittedAndIsCoordinatorAndNotOrientator();
    }

    public String getProposalYear() {
	for (GroupStudent groupStudent : getEnrolment().getRegistration().getAssociatedGroupStudents()) {
	    Proposal proposal = groupStudent.getFinalDegreeWorkProposalConfirmation();
	    if (proposal != null && proposal.getAttributionStatus().isFinalAttribution()) {
		return proposal.getScheduleing().getExecutionYearOfOneExecutionDegree().getYear();
	    }
	}
	return "-";
    }

    public boolean getHasMadeProposalPreviousYear() {
	ExecutionYear enrolmentExecutionYear = getEnrolment().getExecutionYear();
	for (GroupStudent groupStudent : getEnrolment().getRegistration().getAssociatedGroupStudents()) {
	    Proposal proposal = groupStudent.getFinalDegreeWorkProposalConfirmation();
	    if (proposal != null && proposal.isForExecutionYear(enrolmentExecutionYear.getPreviousExecutionYear())
		    && proposal.getAttributionStatus().isFinalAttribution()) {
		return true;
	    }
	}
	return false;
    }
}
