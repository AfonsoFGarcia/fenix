package net.sourceforge.fenixedu.persistenceTier.OJB.transactions;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import net.sourceforge.fenixedu.domain.GuideEntry;
import net.sourceforge.fenixedu.domain.gratuity.ReimbursementGuideState;
import net.sourceforge.fenixedu.domain.reimbursementGuide.ReimbursementGuideEntry;
import net.sourceforge.fenixedu.domain.reimbursementGuide.ReimbursementGuideSituation;
import net.sourceforge.fenixedu.domain.transactions.InsuranceTransaction;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.OJB.ObjectFenixOJB;
import net.sourceforge.fenixedu.persistenceTier.transactions.IPersistentInsuranceTransaction;
import net.sourceforge.fenixedu.util.State;

import org.apache.ojb.broker.query.Criteria;

/**
 * @author - Shezad Anavarali (sana@mega.ist.utl.pt) - Nadir Tarmahomed
 *         (naat@mega.ist.utl.pt)
 */
public class InsuranceTransactionOJB extends ObjectFenixOJB implements IPersistentInsuranceTransaction {

	public InsuranceTransactionOJB() {
	}

	/*
	 * public InsuranceTransaction readByExecutionYearAndStudent(
	 * ExecutionYear executionYear, Student student) throws
	 * ExcepcaoPersistencia { Criteria crit = new Criteria();
	 * crit.addEqualTo("executionYear.idInternal", executionYear
	 * .getIdInternal()); crit.addEqualTo("student.idInternal",
	 * student.getIdInternal()); return (InsuranceTransaction)
	 * queryObject(InsuranceTransaction.class, crit); }
	 */

	public List readAllNonReimbursedByExecutionYearAndStudent(Integer executionYearID, Integer studentID)
			throws ExcepcaoPersistencia {

		List nonReimbursedInsuranceTransactions = new ArrayList();

		Criteria crit = new Criteria();
		crit.addEqualTo("executionYear.idInternal", executionYearID);
		crit.addEqualTo("student.idInternal", studentID);

		List insuranceTransactions = queryList(InsuranceTransaction.class, crit);

		for (Iterator iter = insuranceTransactions.iterator(); iter.hasNext();) {
			InsuranceTransaction insuranceTransaction = (InsuranceTransaction) iter.next();

			GuideEntry guideEntry = insuranceTransaction.getGuideEntry();
			if (guideEntry == null) {

				nonReimbursedInsuranceTransactions.add(insuranceTransaction);
			} else {
				if (guideEntry.getReimbursementGuideEntries().isEmpty()) {
					nonReimbursedInsuranceTransactions.add(insuranceTransaction);
				} else {

					// because of an OJB bug with caching, we have to read
					// the reimbursement guide entry again
					// the following code should be removed when in the next
					// release of OJB
					Criteria reimbursementGuideEntriesCriteria = new Criteria();
					reimbursementGuideEntriesCriteria.addEqualTo("guideEntry.idInternal", guideEntry
							.getIdInternal());
					List reimbursementGuideEntries = queryList(ReimbursementGuideEntry.class,
							reimbursementGuideEntriesCriteria);

					Criteria activeSituationCriteria = new Criteria();

					activeSituationCriteria.addEqualTo("reimbursementGuide.idInternal",
							((ReimbursementGuideEntry) reimbursementGuideEntries.get(0))
									.getReimbursementGuide().getIdInternal());
					activeSituationCriteria.addEqualTo("state", new State(State.ACTIVE));

					ReimbursementGuideSituation activeReimbursementGuideSituation = (ReimbursementGuideSituation) queryObject(
							ReimbursementGuideSituation.class, activeSituationCriteria);

					if (activeReimbursementGuideSituation.getReimbursementGuideState().equals(
							ReimbursementGuideState.PAYED) == false) {

						nonReimbursedInsuranceTransactions.add(insuranceTransaction);

					}

				}
			}

		}

		return nonReimbursedInsuranceTransactions;
	}

	public List readAllByExecutionYearAndStudent(Integer executionYearID, Integer studentID)
			throws ExcepcaoPersistencia {

		Criteria crit = new Criteria();
		crit.addEqualTo("executionYear.idInternal", executionYearID);
		crit.addEqualTo("student.idInternal", studentID);

		return queryList(InsuranceTransaction.class, crit);
	}

}