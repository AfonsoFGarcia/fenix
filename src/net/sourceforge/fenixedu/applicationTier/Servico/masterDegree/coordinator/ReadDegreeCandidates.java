/*
 * ReadMasterDegreeCandidateByUsername.java
 *
 * The Service ReadMasterDegreeCandidateByUsername reads the information of a
 * Candidate and returns it
 * 
 * Created on 02 de Dezembro de 2002, 16:25
 */

/**
 * 
 * Autores : - Nuno Nunes (nmsn@rnl.ist.utl.pt) - Joana Mota
 * (jccm@rnl.ist.utl.pt)
 *  
 */

package net.sourceforge.fenixedu.applicationTier.Servico.masterDegree.coordinator;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.dataTransferObject.InfoCandidateSituation;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionDegree;
import net.sourceforge.fenixedu.dataTransferObject.InfoMasterDegreeCandidate;
import net.sourceforge.fenixedu.dataTransferObject.InfoMasterDegreeCandidateWithInfoPerson;
import net.sourceforge.fenixedu.domain.CandidateSituation;
import net.sourceforge.fenixedu.domain.MasterDegreeCandidate;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.util.State;

public class ReadDegreeCandidates extends Service {

    public List run(InfoExecutionDegree infoExecutionDegree) throws ExcepcaoPersistencia {
        // Read the Candidates
        List candidates = persistentSupport.getIPersistentMasterDegreeCandidate().readByExecutionDegree(
                infoExecutionDegree.getIdInternal());

        if (candidates == null)
            return new ArrayList();

        Iterator iterator = candidates.iterator();
        List result = new ArrayList();
        while (iterator.hasNext()) {
            // For all candidates ...
            MasterDegreeCandidate masterDegreeCandidate = (MasterDegreeCandidate) iterator.next();

            InfoMasterDegreeCandidate infoMasterDegreeCandidate = InfoMasterDegreeCandidateWithInfoPerson
                    .newInfoFromDomain(masterDegreeCandidate);
            // Copy all Situations
            List situations = new ArrayList();
            Iterator situationIter = masterDegreeCandidate.getSituations().iterator();
            while (situationIter.hasNext()) {
                InfoCandidateSituation infoCandidateSituation = InfoCandidateSituation
                        .newInfoFromDomain((CandidateSituation) situationIter.next());
                situations.add(infoCandidateSituation);

                // Check if this is the Active Situation
                if (infoCandidateSituation.getValidation().equals(new State(State.ACTIVE)))
                    infoMasterDegreeCandidate.setInfoCandidateSituation(infoCandidateSituation);
            }
            infoMasterDegreeCandidate.setSituationList(situations);
            result.add(infoMasterDegreeCandidate);
        }

        return result;
    }

    public List run(Integer degreeCurricularPlanId) throws ExcepcaoPersistencia {
        // Read the Candidates
        List candidates = persistentSupport.getIPersistentMasterDegreeCandidate().readByDegreeCurricularPlanId(
                degreeCurricularPlanId);

        if (candidates == null)
            return new ArrayList();

        Iterator iterator = candidates.iterator();
        List result = new ArrayList();
        while (iterator.hasNext()) {
            // For all candidates ...
            MasterDegreeCandidate masterDegreeCandidate = (MasterDegreeCandidate) iterator.next();
            InfoMasterDegreeCandidate infoMasterDegreeCandidate = InfoMasterDegreeCandidateWithInfoPerson
                    .newInfoFromDomain(masterDegreeCandidate);
            // Copy all Situations
            List situations = new ArrayList();
            Iterator situationIter = masterDegreeCandidate.getSituations().iterator();
            while (situationIter.hasNext()) {

                InfoCandidateSituation infoCandidateSituation = InfoCandidateSituation
                        .newInfoFromDomain((CandidateSituation) situationIter.next());
                situations.add(infoCandidateSituation);

                // Check if this is the Active Situation
                if (infoCandidateSituation.getValidation().equals(new State(State.ACTIVE)))
                    infoMasterDegreeCandidate.setInfoCandidateSituation(infoCandidateSituation);
            }
            infoMasterDegreeCandidate.setSituationList(situations);
            result.add(infoMasterDegreeCandidate);
        }

        return result;
    }

}
