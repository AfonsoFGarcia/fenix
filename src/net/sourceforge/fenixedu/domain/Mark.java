package net.sourceforge.fenixedu.domain;

import net.sourceforge.fenixedu.domain.degree.DegreeType;
import net.sourceforge.fenixedu.domain.exceptions.InvalidMarkDomainException;
import net.sourceforge.fenixedu.domain.onlineTests.OnlineTest;
import net.sourceforge.fenixedu.domain.student.Registration;

public class Mark extends Mark_Base {

    public Mark() {
	super();
	setRootDomainObject(RootDomainObject.getInstance());
    }

    public Mark(final Attends attends, final Evaluation evaluation, final String mark) {
	this();
	setAttend(attends);
	setEvaluation(evaluation);
	setMark(mark);
	setPublishedMark(null);
    }

    @Override
    public void setMark(String mark) {
	if (validateMark(mark)) {
	    super.setMark(mark);
	} else {
	    throw new InvalidMarkDomainException("errors.invalidMark", mark, getAttend().getRegistration().getNumber().toString());
	}
    }

    public void delete() {
	removeAttend();
	removeEvaluation();
	removeRootDomainObject();
	deleteDomainObject();
    }

    private boolean validateMark(String mark) {
	GradeScale gradeScale;
	if (getEvaluation() instanceof AdHocEvaluation) {
	    gradeScale = ((AdHocEvaluation) getEvaluation()).getGradeScale();
	} else if (getEvaluation() instanceof WrittenEvaluation) {
	    gradeScale = ((WrittenEvaluation) getEvaluation()).getGradeScale();
	} else if (getEvaluation() instanceof OnlineTest) {
	    gradeScale = ((OnlineTest) getEvaluation()).getGradeScale();
	} else {
	    if (getAttend().getEnrolment() == null) {
		final Registration registration = getAttend().getRegistration();
		final StudentCurricularPlan studentCurricularPlan = registration.getStudentCurricularPlan(getAttend().getExecutionPeriod());
		final DegreeCurricularPlan degreeCurricularPlan = studentCurricularPlan.getDegreeCurricularPlan();
		final DegreeType degreeType = degreeCurricularPlan.getDegreeType();
		if (degreeType == DegreeType.EMPTY) {
		    gradeScale = GradeScale.TYPE20;
		} else {
		    gradeScale = degreeCurricularPlan.getGradeScaleChain();
		}
	    } else {
		gradeScale = getAttend().getEnrolment().getCurricularCourse().getGradeScaleChain();
	    }
	}
	return gradeScale.isValid(mark, getEvaluation().getEvaluationType());
    }

}
