package net.sourceforge.fenixedu.domain.curricularRules;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;

import net.sourceforge.fenixedu.dataTransferObject.GenericPair;
import net.sourceforge.fenixedu.domain.ExecutionSemester;
import net.sourceforge.fenixedu.domain.curricularRules.executors.verifyExecutors.VerifyRuleExecutor;
import net.sourceforge.fenixedu.domain.degreeStructure.CourseGroup;
import net.sourceforge.fenixedu.domain.degreeStructure.DegreeModule;

public class MaximumNumberOfECTSInSpecialSeasonEvaluation extends CurricularRuleNotPersistent {
    
    static private final BigDecimal DEFAULT_VALUE = new BigDecimal(15);

    public List<GenericPair<Object, Boolean>> getLabel() {
	return Collections.singletonList(new GenericPair<Object, Boolean>(
		"label.maximumNumberOfEnrolmentsInSpecialSeasonEvaluation", true));
    }

    public DegreeModule getDegreeModuleToApplyRule() {
	return null;
    }

    public CourseGroup getContextCourseGroup() {
	return null;
    }

    public CompositeRule getParentCompositeRule() {
	return null;
    }

    public CurricularRuleType getCurricularRuleType() {
	return CurricularRuleType.MAXIMUM_NUMBER_OF_ECTS_IN_SPECIAL_SEASON_EVALUATION;
    }

    public ExecutionSemester getBegin() {
	return ExecutionSemester.readActualExecutionSemester();
    }

    public ExecutionSemester getEnd() {
	return null;
    }

    @Override
    public boolean equals(Object obj) {
	return obj instanceof MaximumNumberOfECTSInSpecialSeasonEvaluation;
    }

    public VerifyRuleExecutor createVerifyRuleExecutor() {
	return VerifyRuleExecutor.NULL_VERIFY_EXECUTOR;
    }

    public BigDecimal getMaxEcts() {
	return DEFAULT_VALUE;
    }

    public boolean allowEcts(final BigDecimal value) {
	return value.compareTo(getMaxEcts()) <= 0;
    }
    
}
