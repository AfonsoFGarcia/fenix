/*
 * Created on 23/Set/2004
 *
 */
package net.sourceforge.fenixedu.applicationTier.strategy.tests.strategys;

import net.sourceforge.fenixedu.domain.onlineTests.StudentTestQuestion;
import net.sourceforge.fenixedu.util.tests.QuestionType;
import net.sourceforge.fenixedu.util.tests.ResponseNUM;
import net.sourceforge.fenixedu.util.tests.ResponseProcessing;

/**
 * @author Susana Fernandes
 * 
 */
public class FENIX_NUMQuestionCorrectionStrategy extends QuestionCorrectionStrategy {

    public StudentTestQuestion getMark(StudentTestQuestion studentTestQuestion) {
	if (studentTestQuestion.getSubQuestionByItem().getQuestionType().getType().intValue() == QuestionType.NUM) {
	    ResponseProcessing responseProcessing = getNUMResponseProcessing(studentTestQuestion.getSubQuestionByItem()
		    .getResponseProcessingInstructions(), new Double(((ResponseNUM) studentTestQuestion.getResponse())
		    .getResponse()));
	    if (responseProcessing != null) {
		if (responseProcessing.isFenixCorrectResponse()) {
		    studentTestQuestion.setTestQuestionMark(new Double(studentTestQuestion.getTestQuestionValue().doubleValue()));
		    ResponseNUM r = (ResponseNUM) studentTestQuestion.getResponse();
		    r.setIsCorrect(new Boolean(true));
		    studentTestQuestion.setResponse(r);
		    studentTestQuestion.getSubQuestionByItem().setNextItemId(responseProcessing.getNextItem());
		    return studentTestQuestion;
		}
		studentTestQuestion.getSubQuestionByItem().setNextItemId(responseProcessing.getNextItem());
	    }
	    ResponseNUM r = (ResponseNUM) studentTestQuestion.getResponse();
	    r.setIsCorrect(new Boolean(false));
	    studentTestQuestion.setResponse(r);
	}
	studentTestQuestion.setTestQuestionMark(new Double(0));
	return studentTestQuestion;
    }
}