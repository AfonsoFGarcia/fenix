/*
 * Created on 21/Mar/2003
 */
package ServidorAplicacao.Servico.masterDegree.administrativeOffice.guide;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;

import pt.utl.ist.berserk.logic.serviceManager.IService;
import DataBeans.InfoGuide;
import DataBeans.InfoGuideEntry;
import DataBeans.util.Cloner;
import Dominio.Guide;
import Dominio.GuideEntry;
import Dominio.GuideSituation;
import Dominio.IContributor;
import Dominio.ICursoExecucao;
import Dominio.IExecutionYear;
import Dominio.IGratuitySituation;
import Dominio.IGuide;
import Dominio.IGuideEntry;
import Dominio.IGuideSituation;
import Dominio.IPersonAccount;
import Dominio.IPessoa;
import Dominio.IStudent;
import Dominio.transactions.GratuityTransaction;
import Dominio.transactions.IPaymentTransaction;
import Dominio.transactions.InsuranceTransaction;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorAplicacao.Servico.exceptions.InvalidChangeServiceException;
import ServidorAplicacao.Servico.exceptions.NoChangeMadeServiceException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentGratuitySituation;
import ServidorPersistente.IPersistentPersonAccount;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;
import ServidorPersistente.transactions.IPersistentTransaction;
import Util.CalculateGuideTotal;
import Util.DocumentType;
import Util.GraduationType;
import Util.SituationOfGuide;
import Util.State;
import Util.transactions.TransactionType;

/**
 * @author Nuno Nunes (nmsn@rnl.ist.utl.pt) Joana Mota (jccm@rnl.ist.utl.pt)
 */
public class EditGuideInformation implements IService {

    /**
     * The actor of this class.
     */
    public EditGuideInformation() {
    }

    public InfoGuide run(InfoGuide infoGuide, String[] quantityList, Integer contributorNumber,
            String othersRemarks, Integer othersQuantity, Double othersPrice) throws Exception {

        ISuportePersistente sp = null;

        // This will be the flag that indicates if a change has been made to the
        // Guide
        // No need to anything if there's no change ...
        boolean change = false;

        IContributor contributor = null;
        IGuide guide = new Guide();
        IGuideEntry othersGuideEntry = null;

        // Safety check to see if the Guide can be changed
        this.chekIfChangeable(infoGuide);

        // Read The Guide
        try {
            sp = SuportePersistenteOJB.getInstance();
            guide = sp.getIPersistentGuide().readByNumberAndYearAndVersion(infoGuide.getNumber(),
                    infoGuide.getYear(), infoGuide.getVersion());
        } catch (ExcepcaoPersistencia ex) {
            FenixServiceException newEx = new FenixServiceException("Persistence layer error");
            newEx.fillInStackTrace();
            throw newEx;
        }

        // check if it's needed to change the Contributor

        if ((contributorNumber != null)
                && (!infoGuide.getInfoContributor().getContributorNumber().equals(contributorNumber))) {
            change = true;
        } else {
            contributorNumber = infoGuide.getInfoContributor().getContributorNumber();
        }

        // Read the Contributor
        try {
            sp = SuportePersistenteOJB.getInstance();

            contributor = sp.getIPersistentContributor().readByContributorNumber(contributorNumber);
        } catch (ExcepcaoPersistencia ex) {
            FenixServiceException newEx = new FenixServiceException("Persistence layer error");
            newEx.fillInStackTrace();
            throw newEx;
        }

        infoGuide.setInfoContributor(Cloner.copyIContributor2InfoContributor(contributor));

        // Check the quantities of the Guide Entries
        // The items without a quantity or with a 0 quantity will be deleted if
        // the guide is NON PAYED or
        // they won't appear in the new guide version if the guide has been
        // payed

        Iterator iterator = infoGuide.getInfoGuideEntries().iterator();
        List newInfoGuideEntries = new ArrayList();
        List guideEntriesToRemove = new ArrayList();

        int quantityListIndex = 0;
        while (iterator.hasNext()) {
            InfoGuideEntry infoGuideEntry = (InfoGuideEntry) iterator.next();
            if ((quantityList[quantityListIndex] == null)
                    || (quantityList[quantityListIndex].length() == 0)
                    || (quantityList[quantityListIndex].equals("0"))) {
                // Add to items to remove
                guideEntriesToRemove.add(infoGuideEntry);
                change = true;
            } else {
                if (!infoGuideEntry.getQuantity().equals(new Integer(quantityList[quantityListIndex])))
                    change = true;
                infoGuideEntry.setQuantity(new Integer(quantityList[quantityListIndex]));
                newInfoGuideEntries.add(infoGuideEntry);
            }
            quantityListIndex++;
        }

        // Check if a Others Guide Entry will be Added
        if ((othersPrice != null) && (othersQuantity != null) && (!othersPrice.equals(new Double(0)))
                && (!othersQuantity.equals(new Integer(0)))) {
            change = true;
            othersGuideEntry = new GuideEntry();
            othersGuideEntry.setDescription(othersRemarks);
            othersGuideEntry.setDocumentType(DocumentType.OTHERS_TYPE);
            // TODO : In the future it's possible to be a Major Degree
            othersGuideEntry.setGraduationType(GraduationType.MASTER_DEGREE_TYPE);
            othersGuideEntry.setPrice(othersPrice);
            othersGuideEntry.setQuantity(othersQuantity);
        }

        if (infoGuide.getInfoGuideSituation().getSituation().equals(SituationOfGuide.NON_PAYED_TYPE)) {
            // If there's a change ...
            if (change) {

                // fill in the last field in the Others Guide Entry if necessary
                if (othersGuideEntry != null) {
                    othersGuideEntry.setGuide(guide);
                }
                // Remove the Guide entries wich have been deleted
                Iterator entryIterator = guideEntriesToRemove.iterator();
                while (entryIterator.hasNext()) {
                    InfoGuideEntry infoGuideEntry = (InfoGuideEntry) entryIterator.next();
                    try {
                        sp = SuportePersistenteOJB.getInstance();
                        IGuideEntry guideEntry = sp.getIPersistentGuideEntry()
                                .readByGuideAndGraduationTypeAndDocumentTypeAndDescription(guide,
                                        infoGuideEntry.getGraduationType(),
                                        infoGuideEntry.getDocumentType(),
                                        infoGuideEntry.getDescription());
                        sp.getIPersistentGuideEntry().delete(guideEntry);
                    } catch (ExcepcaoPersistencia ex) {
                        FenixServiceException newEx = new FenixServiceException(
                                "Persistence layer error");
                        newEx.fillInStackTrace();
                        throw newEx;
                    }
                }

                // Update the remaing guide entries
                entryIterator = newInfoGuideEntries.iterator();
                while (entryIterator.hasNext()) {
                    InfoGuideEntry infoGuideEntry = (InfoGuideEntry) entryIterator.next();
                    try {
                        sp = SuportePersistenteOJB.getInstance();
                        IGuideEntry guideEntry = sp.getIPersistentGuideEntry()
                                .readByGuideAndGraduationTypeAndDocumentTypeAndDescription(guide,
                                        infoGuideEntry.getGraduationType(),
                                        infoGuideEntry.getDocumentType(),
                                        infoGuideEntry.getDescription());
                        guideEntry.setQuantity(infoGuideEntry.getQuantity());
                    } catch (ExcepcaoPersistencia ex) {
                        FenixServiceException newEx = new FenixServiceException(
                                "Persistence layer error");
                        newEx.fillInStackTrace();
                        throw newEx;
                    }
                }
                sp.getIPersistentGuide().simpleLockWrite(guide);
                guide.setContributor(contributor);

            }

        } else if (infoGuide.getInfoGuideSituation().getSituation().equals(SituationOfGuide.PAYED_TYPE)) {
            // If there's a change ...
            if (change) {

                // Create a new Guide Version
                IGuide newGuideVersion = null;
                try {
                    sp = SuportePersistenteOJB.getInstance();
                    sp.getIPersistentGuide().simpleLockWrite(newGuideVersion);
                    newGuideVersion = this.createNewGuideVersion(infoGuide);

                    // fill in the last field in the Others Guide Entry if
                    // necessary
                    if (othersGuideEntry != null) {
                        othersGuideEntry.setGuide(newGuideVersion);
                    }

                    // Create The new Situation
                    IGuideSituation guideSituation = new GuideSituation();

                    sp.getIPersistentGuideSituation().simpleLockWrite(guideSituation);
                    guideSituation.setDate(infoGuide.getInfoGuideSituation().getDate());
                    guideSituation.setGuide(newGuideVersion);
                    guideSituation.setRemarks(infoGuide.getRemarks());
                    guideSituation.setSituation(infoGuide.getInfoGuideSituation().getSituation());
                    guideSituation.setState(new State(State.ACTIVE));

                    // Write the new Guide Version

                    //					// Make sure that everything is written before reading
                    // ...
                    //					sp.confirmarTransaccao();
                    //					sp.iniciarTransaccao();

                    //For Transactions Creation
                    IPersistentTransaction persistentTransaction = sp.getIPersistentTransaction();
                    IPaymentTransaction paymentTransaction = null;
                    IGratuitySituation gratuitySituation = null;
                    IPersistentPersonAccount persistentPersonAccount = sp.getIPersistentPersonAccount();
                    IPersonAccount personAccount = persistentPersonAccount.readByPerson(guide
                            .getPerson());
                    IPersistentGratuitySituation persistentGratuitySituation = sp
                            .getIPersistentGratuitySituation();

                    // Write the Guide Entries
                    Iterator guideEntryIterator = newInfoGuideEntries.iterator();
                    while (guideEntryIterator.hasNext()) {

                        IGuideEntry guideEntry = Cloner
                                .copyInfoGuideEntry2IGuideEntry((InfoGuideEntry) guideEntryIterator
                                        .next());

                        // Reset id internal to allow persistence to write a new
                        // version
                        sp.getIPersistentGuideEntry().simpleLockWrite(guideEntry);
                        guideEntry.setIdInternal(null);
                        guideEntry.setGuide(newGuideVersion);

                        IPessoa studentPerson = guide.getPerson();
                        IStudent student = sp.getIPersistentStudent().readByUsername(
                                studentPerson.getUsername());
                        ICursoExecucao executionDegree = guide.getExecutionDegree();

                        //Write Gratuity Transaction
                        if (guideEntry.getDocumentType().equals(DocumentType.GRATUITY_TYPE)) {

                            executionDegree = guide.getExecutionDegree();
                            gratuitySituation = persistentGratuitySituation
                                    .readGratuitySituationByExecutionDegreeAndStudent(executionDegree,
                                            student);

                            paymentTransaction = new GratuityTransaction(guideEntry.getPrice(),
                                    new Timestamp(Calendar.getInstance().getTimeInMillis()), guideEntry
                                            .getDescription(), infoGuide.getPaymentType(),
                                    TransactionType.GRATUITY_ADHOC_PAYMENT, Boolean.FALSE, guide
                                            .getPerson(), personAccount, guideEntry, gratuitySituation);

                            persistentTransaction.lockWrite(paymentTransaction);

                            //Update GratuitySituation
                            persistentGratuitySituation.lockWrite(gratuitySituation);

                            Double remainingValue = gratuitySituation.getRemainingValue();

                            gratuitySituation.setRemainingValue(new Double(remainingValue.doubleValue()
                                    + paymentTransaction.getValue().doubleValue()));

                        }

                        //Write Insurance Transaction
                        if (guideEntry.getDocumentType().equals(DocumentType.INSURANCE_TYPE)) {
                            paymentTransaction = new InsuranceTransaction(guideEntry.getPrice(),
                                    new Timestamp(Calendar.getInstance().getTimeInMillis()), guideEntry
                                            .getDescription(), infoGuide.getPaymentType(),
                                    TransactionType.INSURANCE_PAYMENT, Boolean.FALSE, guide.getPerson(),
                                    personAccount, guideEntry, executionDegree.getExecutionYear(),
                                    student);

                            persistentTransaction.lockWrite(paymentTransaction);
                        }

                    }

                    // Update the version number for the next Database Access
                    infoGuide.setVersion(newGuideVersion.getVersion());

                    // CREATE TRANSACTIONS!!!!!!!!!!!!!!!!!!!!!(Gratuity or
                    // Insurance)

                    //UPDATE remainigValue ou GRATUITY_SITUATION!!!!!

                } catch (ExcepcaoPersistencia ex) {
                    FenixServiceException newEx = new FenixServiceException("Persistence layer error");
                    newEx.fillInStackTrace();
                    throw newEx;
                }
            }

        }
        // If there's no change

        if (!change) {
            throw new NoChangeMadeServiceException();
        }
        IGuide newGuide = null;
        InfoGuide result = null;
        try {
            sp = SuportePersistenteOJB.getInstance();

            // write the Others Guide Entry if necessary
            if (othersGuideEntry != null) {
                sp.getIPersistentGuideEntry().simpleLockWrite(othersGuideEntry);
            }
            // Make sure that everything is written before reading ...
            sp.confirmarTransaccao();
            sp.iniciarTransaccao();

            newGuide = sp.getIPersistentGuide().readByNumberAndYearAndVersion(infoGuide.getNumber(),
                    infoGuide.getYear(), infoGuide.getVersion());
            // Update the Guide Total
            InfoGuide infoGuideTemp = new InfoGuide();
            infoGuideTemp.setInfoGuideEntries(newInfoGuideEntries);

            result = Cloner.copyIGuide2InfoGuide(newGuide);

            result.setTotal(CalculateGuideTotal.calculate(result));

            sp.getIPersistentGuide().simpleLockWrite(newGuide);
            newGuide.setTotal(result.getTotal());

        } catch (ExcepcaoPersistencia ex) {
            FenixServiceException newEx = new FenixServiceException("Persistence layer error", ex);
            throw newEx;
        }

        return result;

    }

    private void chekIfChangeable(InfoGuide infoGuide) throws FenixServiceException {
        SuportePersistenteOJB sp = null;
        List guides = null;

        // Annuled Guides cannot be changed
        if (infoGuide.getInfoGuideSituation().getSituation().equals(SituationOfGuide.ANNULLED_TYPE))
            throw new InvalidChangeServiceException("Situation of Guide Is Annulled");

        try {
            sp = SuportePersistenteOJB.getInstance();
            guides = sp.getIPersistentGuide().readByNumberAndYear(infoGuide.getNumber(),
                    infoGuide.getYear());

        } catch (ExcepcaoPersistencia e) {
            FenixServiceException newEx = new FenixServiceException("Persistence layer error", e);
            throw newEx;
        }

        // If it's not the latest version ...
        if (guides.size() != infoGuide.getVersion().intValue())
            throw new InvalidChangeServiceException("Not the Latest Version");
    }

    private IGuide createNewGuideVersion(InfoGuide infoGuide) throws FenixServiceException {
        IGuide guide = new Guide();
        SuportePersistenteOJB sp = null;
        IContributor contributor = null;
        IPessoa person = null;
        ICursoExecucao executionDegree = null;

        // Read the needed information from the DataBase
        try {
            sp = SuportePersistenteOJB.getInstance();
            person = sp.getIPessoaPersistente().lerPessoaPorUsername(
                    infoGuide.getInfoPerson().getUsername());
            contributor = sp.getIPersistentContributor().readByContributorNumber(
                    infoGuide.getInfoContributor().getContributorNumber());
            IExecutionYear executionYear = sp.getIPersistentExecutionYear().readExecutionYearByName(
                    infoGuide.getInfoExecutionDegree().getInfoExecutionYear().getYear());

            executionDegree = sp.getIPersistentExecutionDegree()
                    .readByDegreeInitialsAndNameDegreeCurricularPlanAndExecutionYear(
                            infoGuide.getInfoExecutionDegree().getInfoDegreeCurricularPlan()
                                    .getInfoDegree().getSigla(),
                            infoGuide.getInfoExecutionDegree().getInfoDegreeCurricularPlan().getName(),
                            executionYear);

        } catch (ExcepcaoPersistencia e) {
            FenixServiceException newEx = new FenixServiceException("Persistence layer error");
            newEx.fillInStackTrace();
            throw newEx;
        }

        // Set the fields
        guide.setContributor(contributor);
        guide.setPerson(person);
        guide.setExecutionDegree(executionDegree);

        guide.setCreationDate(Calendar.getInstance().getTime());
        guide.setGuideRequester(infoGuide.getGuideRequester());
        guide.setNumber(infoGuide.getNumber());
        guide.setYear(infoGuide.getYear());
        guide.setVersion(new Integer(infoGuide.getVersion().intValue() + 1));
        guide.setPaymentDate(infoGuide.getPaymentDate());
        guide.setPaymentType(infoGuide.getPaymentType());
        guide.setRemarks(null);

        // The total will be calculated afterwards
        guide.setTotal(new Double(0));

        return guide;
    }

}

