/**
 * 
 */
package net.sourceforge.fenixedu.dataTransferObject.inquiries;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import net.sourceforge.fenixedu.domain.DomainReference;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.student.YearDelegate;

import org.apache.commons.lang.StringUtils;
import org.joda.time.DateTime;

/**
 * @author - Shezad Anavarali (shezad@ist.utl.pt)
 * 
 */
public class YearDelegateCourseInquiryDTO implements Serializable {

    private DomainReference<ExecutionCourse> executionCourse;

    private DomainReference<YearDelegate> delegate;

    private InquiriesBlock firstBlock;

    private InquiriesBlock secondBlock;

    private InquiriesBlock thirdBlock;

    private InquiriesBlock fourthBlock;

    private InquiriesBlock fifthBlock;

    private InquiriesBlock sixthBlock;

    private InquiriesBlock seventhBlock;

    private DateTime startDateTime;

    public ExecutionCourse getExecutionCourse() {
	return executionCourse.getObject();
    }

    public YearDelegate getDelegate() {
	return delegate.getObject();
    }

    public long getAnswerDuration() {
	return this.startDateTime == null ? 0 : new DateTime().getMillis() - this.startDateTime.getMillis();
    }

    public YearDelegateCourseInquiryDTO(final ExecutionCourse executionCourse, final YearDelegate delegate) {
	super();
	this.executionCourse = new DomainReference<ExecutionCourse>(executionCourse);
	this.delegate = new DomainReference<YearDelegate>(delegate);
	this.startDateTime = new DateTime();
	buildQuestionBlocks();
    }

    public Map<String, InquiriesQuestion> buildAnswersMap(boolean fullLabels) {
	final Map<String, InquiriesQuestion> answers = new HashMap<String, InquiriesQuestion>();
	retrieveAnswersFromBlock(answers, firstBlock, fullLabels);
	retrieveAnswersFromBlock(answers, secondBlock, fullLabels);
	retrieveAnswersFromBlock(answers, thirdBlock, fullLabels);
	retrieveAnswersFromBlock(answers, fourthBlock, fullLabels);
	retrieveAnswersFromBlock(answers, fifthBlock, fullLabels);
	retrieveAnswersFromBlock(answers, sixthBlock, fullLabels);
	retrieveAnswersFromBlock(answers, seventhBlock, fullLabels);
	return answers;
    }

    static private void retrieveAnswersFromBlock(final Map<String, InquiriesQuestion> answers, InquiriesBlock inquiriesBlock,
	    boolean fullLabels) {
	for (final InquiriesQuestion inquiriesQuestion : inquiriesBlock.getQuestions()) {
	    if (fullLabels) {
		answers.put(inquiriesQuestion.getLabel(), inquiriesQuestion);
	    } else {
		final String label = inquiriesQuestion.getLabel();
		answers.put(label.substring(label.lastIndexOf('.') + 1), inquiriesQuestion);
	    }
	}
    }

    private void buildQuestionBlocks() {

	this.firstBlock = new InquiriesBlock(StringUtils.EMPTY, true, "header.yearDelegateInquiries.belowExpected",
		"header.yearDelegateInquiries.expected", "header.yearDelegateInquiries.aboveExpected");
	this.firstBlock.addQuestion(new RadioGroupQuestion("label.yearDelegateInquiries.workLoadClassification", 1, 3, false));
	this.firstBlock.addQuestion(new TextBoxQuestion("label.yearDelegateInquiries.workLoadClassificationReasons", false));

	this.secondBlock = new InquiriesBlock(StringUtils.EMPTY, false, "header.yearDelegateInquiries.totallyDisagree",
		"header.yearDelegateInquiries.two", "header.yearDelegateInquiries.disagree", "header.yearDelegateInquiries.four",
		"header.yearDelegateInquiries.neitherAgreeOrDisagree", "header.yearDelegateInquiries.six",
		"header.yearDelegateInquiries.agree", "header.yearDelegateInquiries.eight",
		"header.yearDelegateInquiries.totallyAgree");
	this.secondBlock
		.addQuestion(new RadioGroupQuestion("label.yearDelegateInquiries.enoughOnlineCUInformation", 1, 9, false));
	this.secondBlock.addQuestion(new TextBoxQuestion("label.yearDelegateInquiries.enoughOnlineCUInformationReasons", false));
	this.secondBlock.addQuestion(new RadioGroupQuestion("label.yearDelegateInquiries.clearOnlineCUInformation", 1, 9, false));
	this.secondBlock.addQuestion(new TextBoxQuestion("label.yearDelegateInquiries.clearOnlineCUInformationReasons", false));
	this.secondBlock
		.addQuestion(new RadioGroupQuestion("label.yearDelegateInquiries.explicitEvaluationMethods", 1, 9, false));
	this.secondBlock.addQuestion(new TextBoxQuestion("label.yearDelegateInquiries.explicitEvaluationMethodsReasons", false));
	this.secondBlock.addQuestion(new RadioGroupQuestion("label.yearDelegateInquiries.evaluationMethodsWellApplied", 1, 9,
		false));
	this.secondBlock
		.addQuestion(new TextBoxQuestion("label.yearDelegateInquiries.evaluationMethodsWellAppliedReasons", false));

	this.thirdBlock = new InquiriesBlock("title.yearDelegateInquiries.evaluationMethodsDisclosed", false,
		"header.yearDelegateInquiries.yes", "header.yearDelegateInquiries.no");
	this.thirdBlock.addQuestion(new RadioGroupQuestion(
		"label.yearDelegateInquiries.evaluationMethodsDisclosedToWorkingStudents", false).addChoice("YES",
		StringUtils.EMPTY).addChoice("NO", StringUtils.EMPTY));
	this.thirdBlock.addQuestion(new RadioGroupQuestion(
		"label.yearDelegateInquiries.evaluationMethodsDisclosedToSpecialSeasonStudents", false).addChoice("YES",
		StringUtils.EMPTY).addChoice("NO", StringUtils.EMPTY));

	this.fourthBlock = new InquiriesBlock(StringUtils.EMPTY, false, "header.yearDelegateInquiries.totallyDisagree",
		"header.yearDelegateInquiries.two", "header.yearDelegateInquiries.disagree", "header.yearDelegateInquiries.four",
		"header.yearDelegateInquiries.neitherAgreeOrDisagree", "header.yearDelegateInquiries.six",
		"header.yearDelegateInquiries.agree", "header.yearDelegateInquiries.eight",
		"header.yearDelegateInquiries.totallyAgree");
	this.fourthBlock.addQuestion(new RadioGroupQuestion(
		"label.yearDelegateInquiries.evaluationDatesScheduleActiveParticipation", 1, 9, false));
	this.fourthBlock.addQuestion(new TextBoxQuestion(
		"label.yearDelegateInquiries.evaluationDatesScheduleActiveParticipationReasons", false));
	this.fourthBlock.addQuestion(new RadioGroupQuestion("label.yearDelegateInquiries.supportMaterialAvailableOnTime", 1, 9,
		false));
	this.fourthBlock.addQuestion(new TextBoxQuestion("label.yearDelegateInquiries.supportMaterialAvailableOnTimeReasons",
		false));
	this.fourthBlock.addQuestion(new RadioGroupQuestion("label.yearDelegateInquiries.previousKnowlegdeArticulation", 1, 9,
		false));
	this.fourthBlock.addQuestion(new TextBoxQuestion("label.yearDelegateInquiries.previousKnowlegdeArticulationReasons",
		false));

	this.fifthBlock = new InquiriesBlock(false);
	this.fifthBlock.addQuestion(new TextBoxQuestion("label.yearDelegateInquiries.suggestedBestPractices", true));

	this.sixthBlock = new InquiriesBlock(false);
	this.sixthBlock.addQuestion(new TextBoxQuestion("label.yearDelegateInquiries.strongAndWeakPointsOfCUTeachingProcess",
		true));

	this.seventhBlock = new InquiriesBlock(false);
	this.seventhBlock.addQuestion(new TextBoxQuestion("label.yearDelegateInquiries.finalCommentsAndImproovements", true));

    }

    public InquiriesBlock getFirstBlock() {
	return firstBlock;
    }

    public void setFirstBlock(InquiriesBlock firstBlock) {
	this.firstBlock = firstBlock;
    }

    public InquiriesBlock getSecondBlock() {
	return secondBlock;
    }

    public void setSecondBlock(InquiriesBlock secondBlock) {
	this.secondBlock = secondBlock;
    }

    public InquiriesBlock getThirdBlock() {
	return thirdBlock;
    }

    public void setThirdBlock(InquiriesBlock thirdBlock) {
	this.thirdBlock = thirdBlock;
    }

    public InquiriesBlock getFourthBlock() {
	return fourthBlock;
    }

    public void setFourthBlock(InquiriesBlock fourthBlock) {
	this.fourthBlock = fourthBlock;
    }

    public InquiriesBlock getFifthBlock() {
	return fifthBlock;
    }

    public void setFifthBlock(InquiriesBlock fifthBlock) {
	this.fifthBlock = fifthBlock;
    }

    public InquiriesBlock getSixthBlock() {
	return sixthBlock;
    }

    public void setSixthBlock(InquiriesBlock sixthBlock) {
	this.sixthBlock = sixthBlock;
    }

    public InquiriesBlock getSeventhBlock() {
	return seventhBlock;
    }

    public void setSeventhBlock(InquiriesBlock seventhBlock) {
	this.seventhBlock = seventhBlock;
    }

}
