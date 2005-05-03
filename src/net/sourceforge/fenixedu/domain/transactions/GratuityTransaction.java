package net.sourceforge.fenixedu.domain.transactions;

import java.sql.Timestamp;

import net.sourceforge.fenixedu.domain.IGratuitySituation;
import net.sourceforge.fenixedu.domain.IGuideEntry;
import net.sourceforge.fenixedu.domain.IPerson;
import net.sourceforge.fenixedu.domain.IPersonAccount;
import net.sourceforge.fenixedu.util.PaymentType;
import net.sourceforge.fenixedu.util.transactions.TransactionType;

/**
 * @author <a href="mailto:sana@ist.utl.pt">Shezad Anavarali </a>
 * @author <a href="mailto:naat@ist.utl.pt">Nadir Tarmahomed </a>
 * 
 */
public class GratuityTransaction extends GratuityTransaction_Base {

	private IGratuitySituation gratuitySituation;

	public GratuityTransaction() {
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
	 * @param gratuitySituation
	 */
	public GratuityTransaction(Double value, Timestamp transactionDate,
			String remarks, PaymentType paymentType,
			TransactionType transactionType, Boolean wasInternalBalance,
			IPerson responsiblePerson, IPersonAccount personAccount,
			IGuideEntry guideEntry, IGratuitySituation gratuitySituation) {
		setValue(value);
		setTransactionDate(transactionDate);
		setRemarks(remarks);
		setPaymentType(paymentType);
		setTransactionType(transactionType);
		setWasInternalBalance(wasInternalBalance);
		setResponsiblePerson(responsiblePerson);
		setPersonAccount(personAccount);
		this.gratuitySituation = gratuitySituation;
	}

	/**
	 * @return Returns the gratuitySituation.
	 */
	public IGratuitySituation getGratuitySituation() {
		return gratuitySituation;
	}

	/**
	 * @param gratuitySituation
	 *            The gratuitySituation to set.
	 */
	public void setGratuitySituation(IGratuitySituation gratuitySituation) {
		this.gratuitySituation = gratuitySituation;
	}

}