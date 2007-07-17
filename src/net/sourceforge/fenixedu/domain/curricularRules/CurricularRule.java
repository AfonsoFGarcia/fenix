package net.sourceforge.fenixedu.domain.curricularRules;

import java.util.List;

import net.sourceforge.fenixedu.dataTransferObject.GenericPair;
import net.sourceforge.fenixedu.domain.ExecutionPeriod;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.curricularRules.ruleExecutors.CurricularRuleExecutorFactory;
import net.sourceforge.fenixedu.domain.curricularRules.ruleExecutors.RuleResult;
import net.sourceforge.fenixedu.domain.degreeStructure.Context;
import net.sourceforge.fenixedu.domain.degreeStructure.CourseGroup;
import net.sourceforge.fenixedu.domain.degreeStructure.DegreeModule;
import net.sourceforge.fenixedu.domain.enrolment.EnrolmentContext;
import net.sourceforge.fenixedu.domain.enrolment.IDegreeModuleToEvaluate;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.util.LogicOperator;

public abstract class CurricularRule extends CurricularRule_Base implements ICurricularRule {

    protected CurricularRule() {
	super();
	setRootDomainObject(RootDomainObject.getInstance());
    }

    protected void init(final DegreeModule degreeModuleToApplyRule,
	    final CourseGroup contextCourseGroup, final ExecutionPeriod begin,
	    final ExecutionPeriod end, final CurricularRuleType type) {

	init(degreeModuleToApplyRule, contextCourseGroup, begin, end);
	checkCurricularRuleType(type);
	setCurricularRuleType(type);
    }

    private void checkCurricularRuleType(final CurricularRuleType type) {
	if (type == null) {
	    throw new DomainException("curricular.rule.invalid.parameters");
	}
    }

    protected void init(final DegreeModule degreeModuleToApplyRule,
	    final CourseGroup contextCourseGroup, final ExecutionPeriod begin, final ExecutionPeriod end) {

	checkParameters(degreeModuleToApplyRule, begin);
	checkExecutionPeriods(begin, end);
	setDegreeModuleToApplyRule(degreeModuleToApplyRule);
	setContextCourseGroup(contextCourseGroup);
	setBegin(begin);
	setEnd(end);
    }

    private void checkParameters(final DegreeModule degreeModuleToApplyRule, final ExecutionPeriod begin) {
	if (degreeModuleToApplyRule == null || begin == null) {
	    throw new DomainException("curricular.rule.invalid.parameters");
	}
    }

    protected void edit(ExecutionPeriod beginExecutionPeriod, ExecutionPeriod endExecutionPeriod) {
	checkExecutionPeriods(beginExecutionPeriod, endExecutionPeriod);
	setBegin(beginExecutionPeriod);
	setEnd(endExecutionPeriod);
    }

    public void delete() {
	removeOwnParameters();
	removeCommonParameters();
	removeRootDomainObject();
	super.deleteDomainObject();
    }

    protected void removeCommonParameters() {
	removeDegreeModuleToApplyRule();
	removeBegin();
	removeEnd();
	removeParentCompositeRule();
	removeContextCourseGroup();
	removeRootDomainObject();
    }

    public boolean appliesToContext(final Context context) {
	return context == null || this.appliesToCourseGroup(context.getParentCourseGroup());
    }

    public boolean appliesToCourseGroup(final CourseGroup courseGroup) {
	return (this.getContextCourseGroup() == null || this.getContextCourseGroup() == courseGroup);
    }

    public boolean isCompositeRule() {
	return getCurricularRuleType() == null;
    }

    protected boolean belongsToCompositeRule() {
	return (getParentCompositeRule() != null);
    }

    @Override
    public ExecutionPeriod getBegin() {
	return belongsToCompositeRule() ? getParentCompositeRule().getBegin() : super.getBegin();
    }

    @Override
    public ExecutionPeriod getEnd() {
	return belongsToCompositeRule() ? getParentCompositeRule().getEnd() : super.getEnd();
    }

    @Override
    public DegreeModule getDegreeModuleToApplyRule() {
	return belongsToCompositeRule() ? getParentCompositeRule().getDegreeModuleToApplyRule() : super
		.getDegreeModuleToApplyRule();
    }

    @Override
    public CourseGroup getContextCourseGroup() {
	return belongsToCompositeRule() ? getParentCompositeRule().getContextCourseGroup() : super
		.getContextCourseGroup();
    }

    public boolean isValid(ExecutionPeriod executionPeriod) {
	return (getBegin().isBeforeOrEquals(executionPeriod) && (getEnd() == null || getEnd()
		.isAfterOrEquals(executionPeriod)));
    }

    public boolean isValid(ExecutionYear executionYear) {
	for (ExecutionPeriod executionPeriod : executionYear.getExecutionPeriods()) {
	    if (isValid(executionPeriod)) {
		return true;
	    }
	}
	return false;
    }

    protected void checkExecutionPeriods(ExecutionPeriod beginExecutionPeriod,
	    ExecutionPeriod endExecutionPeriod) {
	if (endExecutionPeriod != null && beginExecutionPeriod.isAfter(endExecutionPeriod)) {
	    throw new DomainException("curricular.rule.begin.is.after.end.execution.period");
	}
    }
    
    public boolean isVisible() {
        return true;
    }

    public RuleResult evaluate(final IDegreeModuleToEvaluate sourceDegreeModuleToEvaluate, final EnrolmentContext enrolmentContext) {
	return CurricularRuleExecutorFactory.findExecutor(this).execute(this, sourceDegreeModuleToEvaluate, enrolmentContext);
    }

    abstract protected void removeOwnParameters();

    abstract public boolean isLeaf();

    abstract public List<GenericPair<Object, Boolean>> getLabel();

    static public CurricularRule createCurricularRule(final LogicOperator logicOperator,
	    final CurricularRule... curricularRules) {
	switch (logicOperator) {
	case AND:
	    return new AndRule(curricularRules);
	case OR:
	    return new OrRule(curricularRules);
	case NOT:
	    if (curricularRules.length != 1) {
		throw new DomainException("error.invalid.notRule.parameters");
	    }
	    return new NotRule(curricularRules[0]);
	default:
	    throw new DomainException("error.unsupported.logic.operator");
	}
    }
}
