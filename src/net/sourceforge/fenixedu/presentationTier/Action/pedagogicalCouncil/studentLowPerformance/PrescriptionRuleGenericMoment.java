package net.sourceforge.fenixedu.presentationTier.Action.pedagogicalCouncil.studentLowPerformance;

import java.math.BigDecimal;
import java.util.Calendar;

import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.candidacy.Ingression;
import net.sourceforge.fenixedu.domain.student.Registration;

class PrescriptionRuleGenericMoment extends AbstractPrescriptionRule {

    private int month;

    public PrescriptionRuleGenericMoment() {
    }

    public PrescriptionRuleGenericMoment(ExecutionYear registrationStart, BigDecimal minimumEcts,
	    int numberOfEntriesStudentInSecretary, int month) {
	super(registrationStart, minimumEcts, numberOfEntriesStudentInSecretary);
	setMonth(month);

    }

    @Override
    public boolean isOccurs() {
	int month = Calendar.getInstance().get(Calendar.MONTH);
	return getMonth() == month;
    }

    @Override
    public boolean isPrescript(Registration registration, BigDecimal ects, int numberOfEntriesStudentInSecretary) {
	return ects.compareTo(getMinimumEcts()) < 0 && registration.getRegistrationYear().equals(getRegistrationStart())
		&& isForAdmission(registration.getIngression());
    }

    public int getMonth() {
	return month;
    }

    public void setMonth(int month) {
	this.month = month;
    }

    protected boolean isForAdmission(Ingression ingression) {
	return ingression != null && ingression.equals(Ingression.CNA01) && ingression.equals(Ingression.CNA02)
		&& ingression.equals(Ingression.CNA03) && ingression.equals(Ingression.CNA04)
		&& ingression.equals(Ingression.CNA05) && ingression.equals(Ingression.CNA06)
		&& ingression.equals(Ingression.CNA07);
    }

}
