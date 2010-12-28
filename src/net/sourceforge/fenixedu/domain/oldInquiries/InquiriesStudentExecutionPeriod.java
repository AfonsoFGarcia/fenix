package net.sourceforge.fenixedu.domain.oldInquiries;

import net.sourceforge.fenixedu.domain.ExecutionSemester;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.oldInquiries.InquiriesStudentExecutionPeriod_Base;
import net.sourceforge.fenixedu.domain.student.Student;

public class InquiriesStudentExecutionPeriod extends InquiriesStudentExecutionPeriod_Base {

    public InquiriesStudentExecutionPeriod() {
	super();
	setRootDomainObject(RootDomainObject.getInstance());
    }

    public InquiriesStudentExecutionPeriod(final Student student) {
	this();
	setStudent(student);
	setExecutionPeriod(ExecutionSemester.readActualExecutionSemester());
    }

    public InquiriesStudentExecutionPeriod(final Student student, final ExecutionSemester executionSemester) {
	this();
	setStudent(student);
	setExecutionPeriod(executionSemester);
    }

}
