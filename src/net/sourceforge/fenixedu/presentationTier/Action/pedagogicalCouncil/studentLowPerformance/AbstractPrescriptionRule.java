package net.sourceforge.fenixedu.presentationTier.Action.pedagogicalCouncil.studentLowPerformance;

import java.math.BigDecimal;
import java.util.LinkedList;
import java.util.List;

import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.PrescriptionEnum;
import net.sourceforge.fenixedu.domain.student.Registration;

public abstract class AbstractPrescriptionRule {

    public boolean isOccurs() {
	return true;
    }

    public boolean contains(PrescriptionEnum prescriptionEnum) {
	return prescriptionEnum.equals(getPrescriptionEnum());
    }

    public boolean isPrescript(Registration registration, BigDecimal ects, int numberOfEntriesStudentInSecretary) {
	return ects.compareTo(getMinimumEcts()) < 0
		&& numberOfEntriesStudentInSecretary == getNumberOfEntriesStudentInSecretary();
    }

    public static List<AbstractPrescriptionRule> readPrescriptionRules(PrescriptionEnum prescriptionEnum) {
	List<AbstractPrescriptionRule> abstractPrescriptionRules = new LinkedList<AbstractPrescriptionRule>();
	for (AbstractPrescriptionRule abstractPrescriptionRule : getPrescriptionRules()) {
	    if (abstractPrescriptionRule.contains(prescriptionEnum)) {
		abstractPrescriptionRules.add(abstractPrescriptionRule);
	    }
	}
	return abstractPrescriptionRules;
    }

    private static AbstractPrescriptionRule[] getPrescriptionRules() {
	return new AbstractPrescriptionRule[] { new PrescriptionRuleMomentOne(), new PrescriptionRuleMomentTwo(),
		new PrescriptionRuleMomentTree(), new PrescriptionRuleTreeEntries(), new PrescriptionRuleFourEntries(),
		new PrescriptionRuleFiveEntries() };
    }

    public static List<AbstractPrescriptionRule> readProviderPrescriptionRules() {
	List<AbstractPrescriptionRule> abstractPrescriptionRules = new LinkedList<AbstractPrescriptionRule>();
	abstractPrescriptionRules.add(new PrescriptionRuleMomentOne());
	abstractPrescriptionRules.add(new PrescriptionRuleMomentTwo());
	abstractPrescriptionRules.add(new PrescriptionRuleMomentTree());
	abstractPrescriptionRules.add(new PrescriptionRuleGeneric());
	return abstractPrescriptionRules;
    }

    public abstract PrescriptionEnum getPrescriptionEnum();

    public abstract ExecutionYear getRegistrationStart();

    public abstract BigDecimal getMinimumEcts();

    public abstract int getNumberOfEntriesStudentInSecretary();
}
