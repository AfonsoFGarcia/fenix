package net.sourceforge.fenixedu.domain;

import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.space.OldRoom;
import net.sourceforge.fenixedu.domain.student.Registration;

public class WrittenEvaluationEnrolment extends WrittenEvaluationEnrolment_Base {

    public WrittenEvaluationEnrolment() {
    	super();
    	setRootDomainObject(RootDomainObject.getInstance());
    }

    public WrittenEvaluationEnrolment(WrittenEvaluation writtenEvaluation, Registration student) {
    	this();
        this.setWrittenEvaluation(writtenEvaluation);
        this.setStudent(student);
    }

    public WrittenEvaluationEnrolment(WrittenEvaluation writtenEvaluation, Registration student, OldRoom room) {
    	this();
        this.setWrittenEvaluation(writtenEvaluation);
        this.setStudent(student);
        this.setRoom(room);
    }

    public void delete() {
        if (this.getRoom() != null) {
            throw new DomainException("error.notAuthorizedUnEnrollment");
        }

        this.setWrittenEvaluation(null);
        this.setStudent(null);

        removeRootDomainObject();
        super.deleteDomainObject();
    }

    public boolean isForExecutionPeriod(final ExecutionPeriod executionPeriod) {
        for (final ExecutionCourse executionCourse : this.getWrittenEvaluation()
                .getAssociatedExecutionCourses()) {
            if (executionCourse.getExecutionPeriod() == executionPeriod) {
                return true;
            }
        }
        return false;
    }

}
