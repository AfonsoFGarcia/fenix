package net.sourceforge.fenixedu.dataTransferObject.accounting;

import java.io.Serializable;

import net.sourceforge.fenixedu.domain.accounting.EntryType;
import net.sourceforge.fenixedu.domain.accounting.Event;
import net.sourceforge.fenixedu.domain.accounting.Installment;
import net.sourceforge.fenixedu.util.Money;
import pt.utl.ist.fenix.tools.resources.LabelFormatter;

public class EntryWithInstallmentDTO extends EntryDTO implements Serializable {

    private Installment installment;

    public EntryWithInstallmentDTO(EntryType entryType, Event event, Money totalAmount, LabelFormatter description,
	    Installment installment) {
	super(entryType, event, totalAmount, Money.valueOf(0), totalAmount, description, totalAmount);
	setInstallment(installment);
    }

    public EntryWithInstallmentDTO(EntryType entryType, Event event, Money amountToPay, Installment installment) {
	super(entryType, event, amountToPay);
	setInstallment(installment);
    }

    public Installment getInstallment() {
	return this.installment;
    }

    public void setInstallment(Installment installment) {
	this.installment = installment;
    }

}
