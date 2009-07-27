package net.sourceforge.fenixedu.domain.accounting;

public enum PaymentCodeType {

    TOTAL_GRATUITY(0), GRATUITY_FIRST_INSTALLMENT(1), GRATUITY_SECOND_INSTALLMENT(2), ADMINISTRATIVE_OFFICE_FEE_AND_INSURANCE(3), INSURANCE(
	    4), PRE_BOLONHA_MASTER_DEGREE_TOTAL_GRATUITY(5), PRE_BOLONHA_MASTER_DEGREE_INSURANCE(6), RESIDENCE_FEE(7), INTERNAL_DEGREE_CHANGE_INDIVIDUAL_CANDIDACY_PROCESS(
	    15), EXTERNAL_DEGREE_CHANGE_INDIVIDUAL_CANDIDACY_PROCESS(16), DEGREE_TRANSFER_INDIVIDUAL_CANDIDACY_PROCESS(17), SECOND_CYCLE_INDIVIDUAL_CANDIDACY_PROCESS(
	    18), DEGREE_CANDIDACY_FOR_GRADUATED_PERSON_INDIVIDUAL_PROCESS(19);

    private int typeDigit;

    private PaymentCodeType(int typeDigit) {
	this.typeDigit = typeDigit;
    }

    public String getName() {
	return name();
    }

    public String getQualifiedName() {
	return PaymentCodeType.class.getSimpleName() + "." + name();
    }

    public String getFullyQualifiedName() {
	return PaymentCodeType.class.getName() + "." + name();
    }

    public int getTypeDigit() {
	return typeDigit;
    }

}
