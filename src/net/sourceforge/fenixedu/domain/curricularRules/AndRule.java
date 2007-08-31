package net.sourceforge.fenixedu.domain.curricularRules;

import java.util.List;

import net.sourceforge.fenixedu.dataTransferObject.GenericPair;
import net.sourceforge.fenixedu.domain.util.LogicOperator;

public class AndRule extends AndRule_Base {

    public AndRule(CurricularRule... curricularRules) {
	initCompositeRule(curricularRules);
	setCompositeRuleType(LogicOperator.AND);
    }

    @Override
    public List<GenericPair<Object, Boolean>> getLabel() {
	return getLabel("label.operator.and");
    }

}
