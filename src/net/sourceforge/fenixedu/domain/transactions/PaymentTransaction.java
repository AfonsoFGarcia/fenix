package net.sourceforge.fenixedu.domain.transactions;

import java.sql.Timestamp;

import net.sourceforge.fenixedu.domain.GratuitySituation;
import net.sourceforge.fenixedu.domain.GuideEntry;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.PersonAccount;
import net.sourceforge.fenixedu.domain.RootDomainObject;

/**
 * @author <a href="mailto:sana@ist.utl.pt">Shezad Anavarali </a>
 * @author <a href="mailto:naat@ist.utl.pt">Nadir Tarmahomed </a>
 * 
 */
public abstract class PaymentTransaction extends PaymentTransaction_Base {

	public PaymentTransaction() {
	    super();
        setRootDomainObject(RootDomainObject.getInstance());
	}

	/**
	 * @param value
	 * @param transactionDate
	 * @param remarks
	 * @param paymentType
	 * @param transactionType
	 * @param wasInternalBalance
	 * @param responsiblePerson
	 * @param personAccount
	 * @param guideEntry
	 */
	public PaymentTransaction(Double value, Timestamp transactionDate,
			String remarks, PaymentType paymentType,
			TransactionType transactionType, Boolean wasInternalBalance,
			Person responsiblePerson, PersonAccount personAccount,
			GuideEntry guideEntry) {
        this();
        setOjbConcreteClass(getClass().getName());
		setGuideEntry(guideEntry);
		setValue(value);
		setTransactionDate(transactionDate);
		setRemarks(remarks);
		setPaymentType(paymentType);
		setTransactionType(transactionType);
		setWasInternalBalance(wasInternalBalance);
		setResponsiblePerson(responsiblePerson);
		setPersonAccount(personAccount);
	}

    public void delete() {
        if (this instanceof GratuityTransaction) {
            GratuityTransaction gratuityTransaction = (GratuityTransaction) this;
            gratuityTransaction.removeGratuitySituation();
            
            GratuitySituation gratuitySituation = gratuityTransaction.getGratuitySituation();
            double guideEntryValue = getGuideEntry().getPrice() * getGuideEntry().getQuantity();
            gratuitySituation.setRemainingValue(gratuitySituation.getRemainingValue() + guideEntryValue);
        }
        
        removeGuideEntry();
        removeResponsiblePerson();
        removePersonAccount();
        removeRootDomainObject();
        deleteDomainObject();
    }
    
}
