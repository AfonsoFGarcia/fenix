/*
 * Created on 14/Mar/2003
 *
 */
package net.sourceforge.fenixedu.applicationTier.Servico.masterDegree.administrativeOffice.candidate;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoCandidateSituation;
import net.sourceforge.fenixedu.dataTransferObject.InfoMasterDegreeCandidate;
import net.sourceforge.fenixedu.dataTransferObject.InfoMasterDegreeCandidateWithInfoPerson;
import net.sourceforge.fenixedu.dataTransferObject.util.Cloner;
import net.sourceforge.fenixedu.domain.ICandidateSituation;
import net.sourceforge.fenixedu.domain.IMasterDegreeCandidate;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;
import net.sourceforge.fenixedu.util.State;
import pt.utl.ist.berserk.logic.serviceManager.IService;

/**
 * @author Nuno Nunes (nmsn@rnl.ist.utl.pt) Joana Mota (jccm@rnl.ist.utl.pt)
 */
public class GetCandidatesByPerson implements IService {

    public List run(Integer personID) throws FenixServiceException {

        ISuportePersistente sp = null;
        List result = null;

        try {
            sp = PersistenceSupportFactory.getDefaultPersistenceSupport();

            result = sp.getIPersistentMasterDegreeCandidate().readByPersonID(personID);
        } catch (ExcepcaoPersistencia ex) {
            FenixServiceException newEx = new FenixServiceException("Persistence layer error");
            newEx.fillInStackTrace();
            throw newEx;
        }

        List candidateList = new ArrayList();
        Iterator iterator = result.iterator();
        while (iterator.hasNext()) {
            IMasterDegreeCandidate masterDegreeCandidate = (IMasterDegreeCandidate) iterator.next();
            InfoMasterDegreeCandidate infoMasterDegreeCandidate = InfoMasterDegreeCandidateWithInfoPerson
                    .newInfoFromDomain(masterDegreeCandidate);
            Iterator situationIterator = masterDegreeCandidate.getSituations().iterator();
            List situations = new ArrayList();
            while (situationIterator.hasNext()) {
                InfoCandidateSituation infoCandidateSituation = Cloner
                        .copyICandidateSituation2InfoCandidateSituation((ICandidateSituation) situationIterator
                                .next());
                situations.add(infoCandidateSituation);

                // Check if this is the Active Situation
                if (infoCandidateSituation.getValidation().equals(new State(State.ACTIVE)))
                    infoMasterDegreeCandidate.setInfoCandidateSituation(infoCandidateSituation);
            }
            infoMasterDegreeCandidate.setSituationList(situations);
            candidateList.add(infoMasterDegreeCandidate);
        }

        return candidateList;
    }

}