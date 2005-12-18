/*
 * Created on 10/Jan/2004
 *  
 */
package net.sourceforge.fenixedu.applicationTier.Servico.masterDegree.administrativeOffice.gratuity;

import java.util.Calendar;
import java.util.Iterator;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NonExistingServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoGratuitySituation;
import net.sourceforge.fenixedu.dataTransferObject.InfoGratuitySituationWithAll;
import net.sourceforge.fenixedu.domain.IEmployee;
import net.sourceforge.fenixedu.domain.IGratuitySituation;
import net.sourceforge.fenixedu.domain.IPerson;
import net.sourceforge.fenixedu.domain.gratuity.ReimbursementGuideState;
import net.sourceforge.fenixedu.domain.reimbursementGuide.IReimbursementGuideEntry;
import net.sourceforge.fenixedu.domain.transactions.IGratuityTransaction;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentEmployee;
import net.sourceforge.fenixedu.persistenceTier.IPersistentGratuitySituation;
import net.sourceforge.fenixedu.persistenceTier.IPessoaPersistente;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;
import pt.utl.ist.berserk.logic.serviceManager.IService;

/**
 * @author T�nia Pous�o
 * 
 */
public class EditGratuitySituationById implements IService {

	public Object run(InfoGratuitySituation infoGratuitySituation) throws FenixServiceException, ExcepcaoPersistencia {
		ISuportePersistente sp = null;

		if (infoGratuitySituation == null) {
			throw new FenixServiceException();
		}

		sp = PersistenceSupportFactory.getDefaultPersistenceSupport();
		IPersistentGratuitySituation persistentGratuitySituation = sp.getIPersistentGratuitySituation();

		IGratuitySituation gratuitySituation = persistentGratuitySituation
				.readGratuitySituatuionByStudentCurricularPlanAndGratuityValues(infoGratuitySituation
						.getInfoStudentCurricularPlan().getIdInternal(), infoGratuitySituation
						.getInfoGratuityValues().getIdInternal());
		if (gratuitySituation == null) {
			throw new NonExistingServiceException("Gratuity Situation not exist yet.");
		}

		// employee who made register
		IPessoaPersistente persistentPerson = sp.getIPessoaPersistente();
		IPerson person = persistentPerson.lerPessoaPorUsername(infoGratuitySituation.getInfoEmployee()
				.getPerson().getUsername());
		if (person != null) {
			IPersistentEmployee persistentEmployee = sp.getIPersistentEmployee();
			IEmployee employee = persistentEmployee.readByPerson(person.getIdInternal().intValue());
			gratuitySituation.setEmployee(employee);
		}

		persistentGratuitySituation.simpleLockWrite(gratuitySituation);

		Calendar now = Calendar.getInstance();
		gratuitySituation.setWhen(now.getTime());

		// Update remaining value
		double exemptionValue = gratuitySituation.getTotalValue().doubleValue()
				* (infoGratuitySituation.getExemptionPercentage().doubleValue() / 100.0);

		if (infoGratuitySituation.getExemptionValue() != null) {
			exemptionValue += infoGratuitySituation.getExemptionValue().doubleValue();
		}

		double newRemainingValue = gratuitySituation.getTotalValue().doubleValue() - exemptionValue;

		List transactionList = gratuitySituation.getTransactionList();
		List reimbursementGuideEntries = null;
		Iterator it = transactionList.iterator();
		IGratuityTransaction gratuityTransaction = null;
		double payedValue = 0;
		double reimbursedValue = 0;

		while (it.hasNext()) {
			gratuityTransaction = (IGratuityTransaction) it.next();
			payedValue += gratuityTransaction.getValue().doubleValue();

			if (gratuityTransaction.getGuideEntry() != null) {

				reimbursementGuideEntries = gratuityTransaction.getGuideEntry()
						.getReimbursementGuideEntries();

				if (reimbursementGuideEntries != null) {

					Iterator reimbursementIterator = reimbursementGuideEntries.iterator();
					IReimbursementGuideEntry reimbursementGuideEntry = null;

					while (reimbursementIterator.hasNext()) {
						reimbursementGuideEntry = (IReimbursementGuideEntry) reimbursementIterator
								.next();
						if (reimbursementGuideEntry.getReimbursementGuide()
								.getActiveReimbursementGuideSituation().getReimbursementGuideState()
								.equals(ReimbursementGuideState.PAYED)) {

							reimbursedValue += reimbursementGuideEntry.getValue().doubleValue();
						}
					}
				}
			}

		}

		gratuitySituation
				.setRemainingValue(new Double(newRemainingValue - payedValue + reimbursedValue));

		gratuitySituation.setExemptionDescription(infoGratuitySituation.getExemptionDescription());
		gratuitySituation.setExemptionPercentage(infoGratuitySituation.getExemptionPercentage());
		gratuitySituation.setExemptionValue(infoGratuitySituation.getExemptionValue());
		gratuitySituation.setExemptionType(infoGratuitySituation.getExemptionType());

		infoGratuitySituation = InfoGratuitySituationWithAll.newInfoFromDomain(gratuitySituation);

		return infoGratuitySituation;
	}
}
