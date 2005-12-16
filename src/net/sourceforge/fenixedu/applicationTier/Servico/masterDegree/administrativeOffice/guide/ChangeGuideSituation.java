/**
 * Autores : - Nuno Nunes (nmsn@rnl.ist.utl.pt) - Joana Mota
 * (jccm@rnl.ist.utl.pt)
 */

package net.sourceforge.fenixedu.applicationTier.Servico.masterDegree.administrativeOffice.guide;

import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.applicationTier.Servico.ExcepcaoInexistente;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.ExistingServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NonValidChangeServiceException;
import net.sourceforge.fenixedu.domain.DocumentType;
import net.sourceforge.fenixedu.domain.DomainFactory;
import net.sourceforge.fenixedu.domain.GuideState;
import net.sourceforge.fenixedu.domain.IExecutionDegree;
import net.sourceforge.fenixedu.domain.IGratuitySituation;
import net.sourceforge.fenixedu.domain.IGuide;
import net.sourceforge.fenixedu.domain.IGuideEntry;
import net.sourceforge.fenixedu.domain.IGuideSituation;
import net.sourceforge.fenixedu.domain.IPerson;
import net.sourceforge.fenixedu.domain.IPersonAccount;
import net.sourceforge.fenixedu.domain.IStudent;
import net.sourceforge.fenixedu.domain.degree.DegreeType;
import net.sourceforge.fenixedu.domain.transactions.IPaymentTransaction;
import net.sourceforge.fenixedu.domain.transactions.PaymentType;
import net.sourceforge.fenixedu.domain.transactions.TransactionType;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentGratuitySituation;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;
import net.sourceforge.fenixedu.persistenceTier.exceptions.ExistingPersistentException;
import net.sourceforge.fenixedu.util.State;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;

import pt.utl.ist.berserk.logic.serviceManager.IService;

public class ChangeGuideSituation implements IService {

    public void run(Integer guideNumber, Integer guideYear, Integer guideVersion, Date paymentDate,
            String remarks, String situationOfGuideString, String paymentType, IUserView userView)
            throws ExcepcaoInexistente, FenixServiceException, ExistingPersistentException,
            ExcepcaoPersistencia {

        ISuportePersistente sp = PersistenceSupportFactory.getDefaultPersistenceSupport();
        IGuide guide = sp.getIPersistentGuide().readByNumberAndYearAndVersion(guideNumber, guideYear,
                guideVersion);

        if (guide == null) {
            throw new ExcepcaoInexistente("Unknown Guide !!");
        }

        IGuideSituation activeGuideSituation = (IGuideSituation) CollectionUtils.find(guide
                .getGuideSituations(), new Predicate() {

            public boolean evaluate(Object arg0) {
                IGuideSituation guideSituation = (IGuideSituation) arg0;
                return guideSituation.getState().equals(new State(State.ACTIVE));
            }

        });

        GuideState newSituationOfGuide = GuideState.valueOf(situationOfGuideString);

        // check if the change is valid
        if (!verifyChangeValidation(activeGuideSituation, newSituationOfGuide)) {
            throw new NonValidChangeServiceException();
        }

        if (newSituationOfGuide.equals(activeGuideSituation.getSituation())) {
            activeGuideSituation.setRemarks(remarks);
            if (activeGuideSituation.getSituation().equals(GuideState.PAYED)) {
                guide.setPaymentDate(paymentDate);
                guide.setPaymentType(PaymentType.valueOf(paymentType));
            }
            // guide.addGuideSituations(activeGuideSituation);
        } else {

            // Create The New Situation
            activeGuideSituation.setState(new State(State.INACTIVE));

            IGuideSituation newGuideSituation = DomainFactory.makeGuideSituation();

            Calendar date = Calendar.getInstance();
            newGuideSituation.setDate(date.getTime());
            newGuideSituation.setRemarks(remarks);
            newGuideSituation.setSituation(newSituationOfGuide);
            newGuideSituation.setState(new State(State.ACTIVE));

            if (newSituationOfGuide.equals(GuideState.PAYED)) {
                guide.setPaymentDate(paymentDate);
                guide.setPaymentType(PaymentType.valueOf(paymentType));

                // For Transactions Creation
                IPersonAccount personAccount = sp.getIPersistentPersonAccount().readByPerson(
                        guide.getPerson().getIdInternal());
                if (personAccount == null) {
                    personAccount = DomainFactory.makePersonAccount(guide.getPerson());
                }

                IPersistentGratuitySituation persistentGratuitySituation = sp
                        .getIPersistentGratuitySituation();

                IPerson employeePerson = sp.getIPessoaPersistente().lerPessoaPorUsername(
                        userView.getUtilizador());
                IPerson studentPerson = guide.getPerson();
                IStudent student = sp.getIPersistentStudent().readByPersonAndDegreeType(
                        studentPerson.getIdInternal(), DegreeType.MASTER_DEGREE);
                IExecutionDegree executionDegree = guide.getExecutionDegree();

                // Iterate Guide Entries to create Transactions
                for (IGuideEntry guideEntry : guide.getGuideEntries()) {

                    // Write Gratuity Transaction
                    if (guideEntry.getDocumentType().equals(DocumentType.GRATUITY)) {

                        IGratuitySituation gratuitySituation = persistentGratuitySituation
                                .readGratuitySituationByExecutionDegreeAndStudent(executionDegree
                                        .getIdInternal(), student.getIdInternal());
                        Double value = new Double(guideEntry.getPrice().doubleValue()
                                * guideEntry.getQuantity().intValue());

                        IPaymentTransaction paymentTransaction = DomainFactory.makeGratuityTransaction(
                                value, new Timestamp(Calendar.getInstance().getTimeInMillis()),
                                guideEntry.getDescription(), guide.getPaymentType(),
                                TransactionType.GRATUITY_ADHOC_PAYMENT, Boolean.FALSE, employeePerson,
                                personAccount, guideEntry, gratuitySituation);

                        // Update GratuitySituation
                        Double remainingValue = gratuitySituation.getRemainingValue();

                        gratuitySituation.setRemainingValue(new Double(remainingValue.doubleValue()
                                - paymentTransaction.getValue().doubleValue()));

                    }

                    // Write Insurance Transaction
                    if (guideEntry.getDocumentType().equals(DocumentType.INSURANCE)) {

                        List insuranceTransactionList = sp.getIPersistentInsuranceTransaction()
                                .readAllNonReimbursedByExecutionYearAndStudent(
                                        executionDegree.getExecutionYear().getIdInternal(),
                                        student.getIdInternal());

                        if (!insuranceTransactionList.isEmpty()) {
                            throw new ExistingServiceException(
                                    "error.message.transaction.insuranceTransactionAlreadyExists");
                        }

                        DomainFactory.makeInsuranceTransaction(
                                guideEntry.getPrice(), new Timestamp(Calendar.getInstance()
                                        .getTimeInMillis()), guideEntry.getDescription(), guide
                                        .getPaymentType(), TransactionType.INSURANCE_PAYMENT,
                                Boolean.FALSE, guide.getPerson(), personAccount, guideEntry,
                                executionDegree.getExecutionYear(), student);

                    }
                }
            }

            // Write the new Situation
            guide.addGuideSituations(newGuideSituation);

        }

    }

    private boolean verifyChangeValidation(IGuideSituation activeGuideSituation,
            GuideState situationOfGuide) {
        if (activeGuideSituation.equals(GuideState.ANNULLED))
            return false;

        if ((activeGuideSituation.getSituation().equals(GuideState.PAYED))
                && (situationOfGuide.equals(GuideState.NON_PAYED)))
            return false;

        return true;
    }

}