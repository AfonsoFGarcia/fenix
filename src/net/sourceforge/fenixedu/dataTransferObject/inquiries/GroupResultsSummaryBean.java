package net.sourceforge.fenixedu.dataTransferObject.inquiries;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import net.sourceforge.fenixedu.domain.inquiries.InquiryGroupQuestion;
import net.sourceforge.fenixedu.domain.inquiries.InquiryQuestion;
import net.sourceforge.fenixedu.domain.inquiries.InquiryResult;
import net.sourceforge.fenixedu.domain.inquiries.ResultClassification;

import org.apache.commons.beanutils.BeanComparator;

public class GroupResultsSummaryBean implements Serializable {

    private static final long serialVersionUID = 1L;
    private InquiryGroupQuestion inquiryGroupQuestion;
    private ResultClassification groupResultClassification;
    private List<QuestionResultsSummaryBean> questionsResults = new ArrayList<QuestionResultsSummaryBean>();
    private boolean left;

    public GroupResultsSummaryBean(InquiryGroupQuestion inquiryGroupQuestion, List<InquiryResult> inquiryResults) {
	setInquiryGroupQuestion(inquiryGroupQuestion);
	setLeft(true);
	for (InquiryQuestion inquiryQuestion : inquiryGroupQuestion.getInquiryQuestions()) {
	    if (!inquiryQuestion.getLabel().toString().startsWith("1.4")) { //acrescentar campo q diz se se apresenta ou n
		List<InquiryResult> questionResults = getResultsForQuestion(inquiryResults, inquiryQuestion);
		QuestionResultsSummaryBean resultsSummaryBean = null;
		if (inquiryQuestion.isScaleQuestion()) {
		    resultsSummaryBean = new QuestionResultsSummaryBean(inquiryQuestion, questionResults);
		} else {
		    InquiryResult inquiryResult = null;
		    if (questionResults.size() > 0) {
			inquiryResult = questionResults.get(0);
		    }
		    resultsSummaryBean = new QuestionResultsSummaryBean(inquiryQuestion, inquiryResult);
		}
		getQuestionsResults().add(resultsSummaryBean);
	    }
	}
	Collections.sort(getQuestionsResults(), new BeanComparator("inquiryQuestion.questionOrder"));
    }

    private List<InquiryResult> getResultsForQuestion(List<InquiryResult> results, InquiryQuestion inquiryQuestion) {
	List<InquiryResult> questionResults = new ArrayList<InquiryResult>();
	for (InquiryResult inquiryResult : results) {
	    if (inquiryResult.getInquiryQuestion() == inquiryQuestion) {
		questionResults.add(inquiryResult);
	    }
	}
	return questionResults;
    }

    public int getAbsoluteScaleValuesSize() {
	if (getInquiryGroupQuestion().isScaleGroup()) {
	    for (QuestionResultsSummaryBean questionResultsSummaryBean : getQuestionsResults()) {
		if (questionResultsSummaryBean.getAbsoluteScaleValues().size() > 0) {
		    return questionResultsSummaryBean.getAbsoluteScaleValues().size();
		}
	    }
	}
	return 0;
    }

    public InquiryGroupQuestion getInquiryGroupQuestion() {
	return inquiryGroupQuestion;
    }

    public void setInquiryGroupQuestion(InquiryGroupQuestion inquiryGroupQuestion) {
	this.inquiryGroupQuestion = inquiryGroupQuestion;
    }

    public List<QuestionResultsSummaryBean> getQuestionsResults() {
	return questionsResults;
    }

    public void setQuestionsResults(List<QuestionResultsSummaryBean> questionsResults) {
	this.questionsResults = questionsResults;
    }

    public void setGroupResultClassification(ResultClassification groupResultClassification) {
	this.groupResultClassification = groupResultClassification;
    }

    public ResultClassification getGroupResultClassification() {
	return groupResultClassification;
    }

    public void setLeft(boolean left) {
	this.left = left;
    }

    public boolean isLeft() {
	return left;
    }
}
