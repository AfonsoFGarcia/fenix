package net.sourceforge.fenixedu.domain.curricularRules;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import net.sourceforge.fenixedu.dataTransferObject.GenericPair;
import net.sourceforge.fenixedu.domain.DomainObject;
import net.sourceforge.fenixedu.domain.ExecutionPeriod;
import net.sourceforge.fenixedu.domain.degreeStructure.Context;
import net.sourceforge.fenixedu.domain.degreeStructure.DegreeModule;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.util.LogicOperators;

public class CompositeRule extends CompositeRule_Base {
    
    protected CompositeRule(DegreeModule degreeModuleToApplyRule, ExecutionPeriod begin,
            ExecutionPeriod end, LogicOperators compositeRuleType,
            CurricularRule... curricularRules) {

        super();

        if (degreeModuleToApplyRule == null || begin == null) {
            throw new DomainException("curricular.rule.invalid.parameters");
        }

        checkCompositeRuleType(compositeRuleType, curricularRules);
        init(degreeModuleToApplyRule, begin, end, compositeRuleType, curricularRules);
    }

    private void checkCompositeRuleType(LogicOperators compositeRuleType, CurricularRule... curricularRules) throws DomainException {
        if (compositeRuleType == LogicOperators.NOT && curricularRules.length > 1) {
            throw new DomainException("incorrect.NOT.composite.rule.use");
        } else if ((compositeRuleType == LogicOperators.AND || compositeRuleType == LogicOperators.OR) && curricularRules.length < 2) {
            throw new DomainException("incorrect.composite.rule.use");
        } else if (compositeRuleType != LogicOperators.AND
                && compositeRuleType != LogicOperators.OR
                && compositeRuleType != LogicOperators.NOT) {
            throw new DomainException("unsupported.composite.rule");
        }
    }

    private void init(DegreeModule degreeModuleToApplyRule, ExecutionPeriod begin,
            ExecutionPeriod end, LogicOperators compositeRuleType,
            CurricularRule... curricularRules) {

        setDegreeModuleToApplyRule(degreeModuleToApplyRule);
        setBegin(begin);
        setEnd(end);
        setCompositeRuleType(compositeRuleType);

        for (final CurricularRule curricularRule : curricularRules) {
            curricularRule.setParentCompositeRule(this);
        }
    }
    
    @Override
    public List<GenericPair<Object, Boolean>> getLabel() {
        if (getCompositeRuleType().equals(LogicOperators.NOT)) {
            return getCurricularRules().get(0).getLabel();
        } else {
            final String operator;
            if (getCompositeRuleType().equals(LogicOperators.AND)) {
                operator = "label.operator.and";
            } else if (getCompositeRuleType().equals(LogicOperators.OR)) {
                operator = "label.operator.or";
            } else {
                throw new DomainException("unsupported.composite.rule");
            }
            final List<GenericPair<Object, Boolean>> labelList = new ArrayList<GenericPair<Object, Boolean>>();
            labelList.add(new GenericPair<Object, Boolean>("( ", false));
            final Iterator<CurricularRule> curricularRulesIterator = getCurricularRules().listIterator();
            while (curricularRulesIterator.hasNext()) {
                labelList.addAll(curricularRulesIterator.next().getLabel());
                if (curricularRulesIterator.hasNext()) {
                    labelList.add(new GenericPair<Object, Boolean>(" ", false));
                    labelList.add(new GenericPair<Object, Boolean>(operator, true));
                    labelList.add(new GenericPair<Object, Boolean>(" ", false));
                }
            }
            labelList.add(new GenericPair<Object, Boolean>(" )", false));
            return labelList;
        }
    }

    @Override
    public boolean evaluate(Class<? extends DomainObject> object) {
        boolean result = true;
        switch (getCompositeRuleType()) {

        case NOT:
            return !getCurricularRules().get(0).evaluate(object);
        
        case AND:
            for (final CurricularRule curricularRule : getCurricularRules()) {
                result &= curricularRule.evaluate(object);
                if (!result) { break; }
            }
            return result;
        
        case OR:
            for (final CurricularRule curricularRule : getCurricularRules()) {
                result |= curricularRule.evaluate(object);
                if (result) { break; }
            }
            return result;

        default:
            throw new DomainException("unsupported.composite.rule");
        }
    }        

    @Override
    public boolean appliesToContext(Context context) {
        for (CurricularRule curricularRule : this.getCurricularRules()) {
            if (!curricularRule.appliesToContext(context)) {
               return false; 
            }
        }
        return true;
    }

    @Override
    protected void removeOwnParameters() {
        for (; !getCurricularRules().isEmpty(); getCurricularRules().get(0).delete());
    }

    @Override
    public boolean isLeaf() {
        return false;
    }
}
