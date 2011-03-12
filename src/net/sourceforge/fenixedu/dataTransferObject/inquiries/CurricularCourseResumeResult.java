package net.sourceforge.fenixedu.dataTransferObject.inquiries;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.ExecutionDegree;
import net.sourceforge.fenixedu.domain.Professorship;
import net.sourceforge.fenixedu.domain.ShiftType;
import net.sourceforge.fenixedu.domain.inquiries.DelegateInquiryTemplate;
import net.sourceforge.fenixedu.domain.inquiries.InquiryBlock;
import net.sourceforge.fenixedu.domain.inquiries.InquiryConnectionType;
import net.sourceforge.fenixedu.domain.inquiries.InquiryDelegateAnswer;
import net.sourceforge.fenixedu.domain.inquiries.InquiryGroupQuestion;
import net.sourceforge.fenixedu.domain.inquiries.InquiryQuestion;
import net.sourceforge.fenixedu.domain.inquiries.InquiryResponseState;
import net.sourceforge.fenixedu.domain.inquiries.InquiryResult;
import net.sourceforge.fenixedu.domain.inquiries.InquiryResultComment;
import net.sourceforge.fenixedu.domain.inquiries.ResultPersonCategory;
import net.sourceforge.fenixedu.domain.student.YearDelegate;

import org.apache.commons.beanutils.BeanComparator;
import org.apache.commons.lang.StringUtils;

public class CurricularCourseResumeResult implements Serializable {

    private static final long serialVersionUID = 1L;

    private ExecutionCourse executionCourse;
    private ExecutionDegree executionDegree;
    private YearDelegate yearDelegate;
    private Set<InquiryResult> resultBlocks;
    private List<TeacherShiftTypeResultsBean> teachersResults;

    public CurricularCourseResumeResult(ExecutionCourse executionCourse, ExecutionDegree executionDegree,
	    YearDelegate yearDelegate) {
	setExecutionCourse(executionCourse);
	setExecutionDegree(executionDegree);
	setYearDelegate(yearDelegate);
	initTeachersResults(executionCourse, yearDelegate);
    }

    private void initTeachersResults(ExecutionCourse executionCourse, YearDelegate yearDelegate) {
	setTeachersResults(new ArrayList<TeacherShiftTypeResultsBean>());
	for (Professorship professorship : executionCourse.getProfessorships()) {
	    List<InquiryResult> professorshipResults = professorship.getInquiriyResults();
	    if (!professorshipResults.isEmpty()) {
		for (ShiftType shiftType : getShiftTypes(professorshipResults)) {
		    List<InquiryResult> teacherShiftResults = professorship.getInquiriyResults(shiftType);
		    if (!teacherShiftResults.isEmpty()) {
			getTeachersResults().add(
				new TeacherShiftTypeResultsBean(professorship, shiftType, executionCourse.getExecutionPeriod(),
					teacherShiftResults, yearDelegate.getPerson()));
		    }
		}
	    }
	}

	Collections.sort(getTeachersResults(), new BeanComparator("professorship.person.name"));
	Collections.sort(getTeachersResults(), new BeanComparator("shiftType"));
    }

    private Set<ShiftType> getShiftTypes(List<InquiryResult> professorshipResults) {
	Set<ShiftType> shiftTypes = new HashSet<ShiftType>();
	for (InquiryResult inquiryResult : professorshipResults) {
	    shiftTypes.add(inquiryResult.getShiftType());
	}
	return shiftTypes;
    }

    public Set<InquiryResult> getCurricularBlocks() {
	setResultBlocks(new TreeSet<InquiryResult>(new BeanComparator("inquiryQuestion.questionOrder")));
	for (InquiryResult inquiryResult : getExecutionCourse().getInquiryResults()) {
	    if ((inquiryResult.getExecutionDegree() == getExecutionDegree() || inquiryResult.getExecutionDegree() == null)
		    && InquiryConnectionType.GROUP.equals(inquiryResult.getConnectionType())) { //change to COURSE_EVALUATION
		getResultBlocks().add(inquiryResult);
	    }
	}
	return getResultBlocks();
    }

    private Set<InquiryResult> getResultBlocks() {
	return resultBlocks;
    }

    private void setResultBlocks(TreeSet<InquiryResult> resultBlocks) {
	this.resultBlocks = resultBlocks;
    }

    public List<Integer> getMandatoryIssues() {
	List<Integer> mandatory = new ArrayList<Integer>();
	for (InquiryResult inquiryResult : getResultBlocks()) {
	    mandatory.add(mandatory.size(), getNumberOfMandatoryIssues(inquiryResult));
	}
	return mandatory;
    }

    private int getNumberOfMandatoryIssues(InquiryResult inquiryResult) {
	int count = 0;
	if (!inquiryResult.getInquiryQuestion().getAssociatedGroups().isEmpty()) {
	    InquiryResult inquiryResultQuestion = getInquiryResultByQuestion(inquiryResult.getInquiryQuestion());
	    if (inquiryResultQuestion != null && inquiryResultQuestion.getResultClassification().isMandatoryComment()) {
		count++;
	    }
	}
	List<InquiryBlock> associatedBlocks = getAssociatedBlocks(inquiryResult);
	for (InquiryBlock inquiryBlock : associatedBlocks) {
	    for (InquiryGroupQuestion inquiryGroupQuestion : inquiryBlock.getInquiryGroupsQuestions()) {
		for (InquiryQuestion inquiryQuestion : inquiryGroupQuestion.getInquiryQuestions()) {
		    InquiryResult inquiryResultQuestion = getInquiryResultByQuestion(inquiryQuestion);
		    if (inquiryResultQuestion != null && inquiryResultQuestion.getResultClassification().isMandatoryComment()) {
			count++;
		    }
		}
	    }
	}
	return count;
    }

    public String getCompletionState() {
	int mandatoryIssues = 0;
	int mandatoryCommentedIssues = 0;
	for (InquiryResult inquiryResult : getResultBlocks()) {
	    mandatoryIssues += getNumberOfMandatoryIssues(inquiryResult);
	    mandatoryCommentedIssues += getCommentedfMandatoryIssues(inquiryResult);
	}

	InquiryDelegateAnswer inquiryDelegateAnswer = null;
	for (InquiryDelegateAnswer delegateAnswer : getYearDelegate().getInquiryDelegateAnswers()) {
	    if (delegateAnswer.getExecutionCourse() == getExecutionCourse()) {
		inquiryDelegateAnswer = delegateAnswer;
	    }
	}

	DelegateInquiryTemplate inquiryTemplate = DelegateInquiryTemplate.getTemplateByExecutionPeriod(getExecutionCourse()
		.getExecutionPeriod());
	int numberOfQuestions = inquiryTemplate.getNumberOfQuestions();

	if ((mandatoryIssues > 0 && mandatoryCommentedIssues == 0 && inquiryDelegateAnswer == null)
		|| (mandatoryIssues == 0 && inquiryDelegateAnswer == null)) {
	    return InquiryResponseState.EMPTY.getLocalizedName();
	} else if ((mandatoryIssues - mandatoryCommentedIssues > 0) || inquiryDelegateAnswer == null
		|| inquiryDelegateAnswer.getQuestionAnswers().size() < numberOfQuestions) {
	    return InquiryResponseState.INCOMPLETE.getLocalizedName();
	} else {
	    return InquiryResponseState.COMPLETE.getLocalizedName();
	}
    }

    private int getCommentedfMandatoryIssues(InquiryResult inquiryResult) {
	int count = 0;
	if (!inquiryResult.getInquiryQuestion().getAssociatedGroups().isEmpty()) {
	    if (isMandatoryAndCommented(inquiryResult.getInquiryQuestion())) {
		count++;
	    }
	}
	List<InquiryBlock> associatedBlocks = getAssociatedBlocks(inquiryResult);
	for (InquiryBlock inquiryBlock : associatedBlocks) {
	    for (InquiryGroupQuestion inquiryGroupQuestion : inquiryBlock.getInquiryGroupsQuestions()) {
		for (InquiryQuestion inquiryQuestion : inquiryGroupQuestion.getInquiryQuestions()) {
		    if (isMandatoryAndCommented(inquiryQuestion)) {
			count++;
		    }
		}
	    }
	}
	return count;
    }

    private boolean isMandatoryAndCommented(InquiryQuestion inquiryQuestion) {
	InquiryResult inquiryResultQuestion = getInquiryResultByQuestion(inquiryQuestion);
	InquiryResultComment inquiryResultComment = inquiryResultQuestion != null ? inquiryResultQuestion
		.getInquiryResultComment(getYearDelegate().getPerson(), ResultPersonCategory.DELEGATE) : null;
	if (inquiryResultQuestion != null && inquiryResultQuestion.getResultClassification().isMandatoryComment()
		&& inquiryResultComment != null && !StringUtils.isEmpty(inquiryResultComment.getComment())) {
	    return true;
	}
	return false;
    }

    private List<InquiryBlock> getAssociatedBlocks(InquiryResult inquiryResult) {
	List<InquiryBlock> associatedBlocks = inquiryResult.getInquiryQuestion().getAssociatedBlocks();
	if (!inquiryResult.getInquiryQuestion().getAssociatedResultBlocks().isEmpty()) {
	    associatedBlocks = new ArrayList<InquiryBlock>();
	    for (InquiryBlock inquiryBlock : inquiryResult.getInquiryQuestion().getAssociatedResultBlocks()) {
		for (InquiryGroupQuestion groupQuestion : inquiryBlock.getInquiryGroupsQuestions()) {
		    for (InquiryQuestion inquiryQuestion : groupQuestion.getInquiryQuestions()) {
			associatedBlocks.addAll(inquiryQuestion.getAssociatedBlocks());
		    }
		}
	    }
	}
	return associatedBlocks;
    }

    private InquiryResult getInquiryResultByQuestion(InquiryQuestion inquiryQuestion) {
	for (InquiryResult inquiryResult : getExecutionCourse().getInquiryResults()) {
	    if (inquiryResult.getExecutionDegree() == getExecutionDegree()
		    || (inquiryResult.getExecutionDegree() == null && inquiryResult.getShiftType() != null)) {
		if (inquiryResult.getInquiryQuestion() == inquiryQuestion && inquiryResult.getResultClassification() != null) {
		    return inquiryResult;
		}
	    }
	}
	return null;
    }

    public void setExecutionCourse(ExecutionCourse executionCourse) {
	this.executionCourse = executionCourse;
    }

    public ExecutionCourse getExecutionCourse() {
	return executionCourse;
    }

    public void setExecutionDegree(ExecutionDegree executionDegree) {
	this.executionDegree = executionDegree;
    }

    public ExecutionDegree getExecutionDegree() {
	return executionDegree;
    }

    public void setYearDelegate(YearDelegate yearDelegate) {
	this.yearDelegate = yearDelegate;
    }

    public YearDelegate getYearDelegate() {
	return yearDelegate;
    }

    public void setTeachersResults(List<TeacherShiftTypeResultsBean> teachersResults) {
	this.teachersResults = teachersResults;
    }

    public List<TeacherShiftTypeResultsBean> getTeachersResults() {
	return teachersResults;
    }
}