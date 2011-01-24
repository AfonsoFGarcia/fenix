package net.sourceforge.fenixedu.domain.inquiries;

import net.sourceforge.fenixedu.domain.ExecutionSemester;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;

import org.joda.time.DateTime;

public abstract class InquiryTemplate extends InquiryTemplate_Base {

    public void validateInquiryPeriod(DateTime begin, DateTime end) {
	if (begin == null || begin.isAfter(end)) {
	    throw new DomainException("error.invalid.period.defined");
	}
    }

    public boolean isOpen() {
	return !getResponsePeriodBegin().isAfterNow() && !getResponsePeriodEnd().isBeforeNow();
    }

    public static InquiryTemplate getInquiryTemplateByTypAndExecutionSemester(ExecutionSemester executionSemester,
	    InquiryResponsePeriodType type) {

	switch (type) {
	case STUDENT:
	    return CurricularCourseInquiryTemplate.getTemplateByExecutionPeriod(executionSemester);
	case DELEGATE:
	    return DelegateInquiryTemplate.getTemplateByExecutionPeriod(executionSemester);
	case TEACHING:
	    return TeacherInquiryTemplate.getTemplateByExecutionPeriod(executionSemester);
	default:
	    return null;
	}
    }
}
