package net.sourceforge.fenixedu.domain;

import java.util.Date;
import java.util.Iterator;
import java.util.List;

import net.sourceforge.fenixedu.domain.transactions.PaymentType;
import net.sourceforge.fenixedu.domain.masterDegree.GuideRequester;
import net.sourceforge.fenixedu.util.State;

/**
 * @author Nuno Nunes (nmsn@rnl.ist.utl.pt) Joana Mota (jccm@rnl.ist.utl.pt)
 */

public class Guide extends Guide_Base {

	protected IPerson person;

	protected IContributor contributor;

	protected IExecutionDegree executionDegree;

	protected PaymentType paymentType;

	protected Date creationDate;

	protected Date paymentDate;

	protected GuideRequester guideRequester;

	protected List guideEntries;

	protected List guideSituations;

	protected List reimbursementGuides;

	public Guide() {
	}

	public Guide(Integer number, Integer year, Double total, String remarks,
			IPerson person, IContributor contributor,
			GuideRequester guideRequester, IExecutionDegree executionDegree,
			PaymentType paymentType, Date creationDate, Integer version) {
		this.contributor = contributor;
		setNumber(number);
		this.person = person;
		setRemarks(remarks);
		setTotal(total);
		setYear(year);
		this.guideRequester = guideRequester;
		this.executionDegree = executionDegree;
		this.paymentType = paymentType;
		this.creationDate = creationDate;
		setVersion(version);
	}

	/**
	 * @param guideId
	 */
	public Guide(Integer guideId) {
		setIdInternal(guideId);
	}

	public boolean equals(Object obj) {
		boolean resultado = false;
		if (obj instanceof IGuide) {
			IGuide guide = (IGuide) obj;

			if (((getNumber() == null && guide.getNumber() == null) || (getNumber()
					.equals(guide.getNumber())))
					&& ((getYear() == null && guide.getYear() == null) || (getYear()
							.equals(guide.getYear())))) {
				resultado = true;
			}
		}

		return resultado;
	}

	public String toString() {
		String result = "[GUIDE";
		result += ", codInt=" + getIdInternal();
		result += ", number=" + getNumber();
		result += ", year=" + getYear();
		result += ", contributor=" + contributor;
		result += ", total=" + getTotal();
		result += ", remarks=" + getRemarks();
		result += ", guide Requester=" + guideRequester;
		result += ", execution Degree=" + executionDegree;
		result += ", payment Type=" + paymentType;
		result += ", creation Date=" + creationDate;
		result += ", version=" + getVersion();
		result += ", payment Date=" + paymentDate;
		result += "]";
		return result;
	}

	public IGuideSituation getActiveSituation() {
		if (this.getGuideSituations() != null) {
			Iterator iterator = this.getGuideSituations().iterator();
			while (iterator.hasNext()) {
				IGuideSituation guideSituation = (IGuideSituation) iterator
						.next();
				if (guideSituation.getState().equals(new State(State.ACTIVE))) {
					return guideSituation;
				}
			}
		}
		return null;

	}

	/**
	 * @return
	 */
	public IContributor getContributor() {
		return contributor;
	}

	/**
	 * @return
	 */
	public IExecutionDegree getExecutionDegree() {
		return executionDegree;
	}

	/**
	 * @return
	 */
	public List getGuideEntries() {
		return guideEntries;
	}

	/**
	 * @return
	 */
	public GuideRequester getGuideRequester() {
		return guideRequester;
	}

	/**
	 * @return
	 */
	public IPerson getPerson() {
		return person;
	}

	/**
	 * @param contributor
	 */
	public void setContributor(IContributor contributor) {
		this.contributor = contributor;
	}

	/**
	 * @param execucao
	 */
	public void setExecutionDegree(IExecutionDegree execucao) {
		executionDegree = execucao;
	}

	/**
	 * @param list
	 */
	public void setGuideEntries(List list) {
		guideEntries = list;
	}

	/**
	 * @param requester
	 */
	public void setGuideRequester(GuideRequester requester) {
		guideRequester = requester;
	}

	/**
	 * @param pessoa
	 */
	public void setPerson(IPerson pessoa) {
		person = pessoa;
	}

	/**
	 * @return
	 */
	public PaymentType getPaymentType() {
		return paymentType;
	}

	/**
	 * @param type
	 */
	public void setPaymentType(PaymentType type) {
		paymentType = type;
	}

	/**
	 * @return
	 */
	public List getGuideSituations() {
		return guideSituations;
	}

	/**
	 * @param list
	 */
	public void setGuideSituations(List list) {
		guideSituations = list;
	}

	/**
	 * @return
	 */
	public Date getCreationDate() {
		return creationDate;
	}

	/**
	 * @param date
	 */
	public void setCreationDate(Date date) {
		creationDate = date;
	}

	/**
	 * @return
	 */
	public Date getPaymentDate() {
		return paymentDate;
	}

	/**
	 * @param date
	 */
	public void setPaymentDate(Date date) {
		paymentDate = date;
	}

	/**
	 * @return Returns the reimbursementGuides.
	 */
	public List getReimbursementGuides() {
		return reimbursementGuides;
	}

	/**
	 * @param reimbursementGuides
	 *            The reimbursementGuides to set.
	 */
	public void setReimbursementGuides(List reimbursementGuides) {
		this.reimbursementGuides = reimbursementGuides;
	}
}