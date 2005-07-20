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

import net.sourceforge.fenixedu.dataTransferObject.InfoCandidateSituation;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionDegree;
import net.sourceforge.fenixedu.dataTransferObject.InfoMasterDegreeCandidate;
import net.sourceforge.fenixedu.dataTransferObject.InfoMasterDegreeCandidateWithInfoPerson;
import net.sourceforge.fenixedu.domain.ICandidateSituation;
import net.sourceforge.fenixedu.domain.IMasterDegreeCandidate;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;
import net.sourceforge.fenixedu.util.State;
import pt.utl.ist.berserk.logic.serviceManager.IService;

public class ReadDegreeCandidates implements IService {

    public List run(InfoExecutionDegree infoExecutionDegree) throws ExcepcaoPersistencia {

        final ISuportePersistente sp = PersistenceSupportFactory.getDefaultPersistenceSupport();

        // Read the Candidates
        List candidates = sp.getIPersistentMasterDegreeCandidate().readByExecutionDegree(
                infoExecutionDegree.getIdInternal());

        if (candidates == null)
            return new ArrayList();

        Iterator iterator = candidates.iterator();
        List result = new ArrayList();
        while (iterator.hasNext()) {
            // For all candidates ...
            IMasterDegreeCandidate masterDegreeCandidate = (IMasterDegreeCandidate) iterator.next();

            InfoMasterDegreeCandidate infoMasterDegreeCandidate = InfoMasterDegreeCandidateWithInfoPerson
                    .newInfoFromDomain(masterDegreeCandidate);
            // Copy all Situations
            List situations = new ArrayList();
            Iterator situationIter = masterDegreeCandidate.getSituations().iterator();
            while (situationIter.hasNext()) {
                InfoCandidateSituation infoCandidateSituation = InfoCandidateSituation
                        .newInfoFromDomain((ICandidateSituation) situationIter.next());
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
        final ISuportePersistente sp = PersistenceSupportFactory.getDefaultPersistenceSupport();

        // Read the Candidates
        List candidates = sp.getIPersistentMasterDegreeCandidate().readByDegreeCurricularPlanId(
                degreeCurricularPlanId);

        if (candidates == null)
            return new ArrayList();

        Iterator iterator = candidates.iterator();
        List result = new ArrayList();
        while (iterator.hasNext()) {
            // For all candidates ...
            IMasterDegreeCandidate masterDegreeCandidate = (IMasterDegreeCandidate) iterator.next();
            InfoMasterDegreeCandidate infoMasterDegreeCandidate = InfoMasterDegreeCandidateWithInfoPerson
                    .newInfoFromDomain(masterDegreeCandidate);
            // Copy all Situations
            List situations = new ArrayList();
            Iterator situationIter = masterDegreeCandidate.getSituations().iterator();
            while (situationIter.hasNext()) {

                InfoCandidateSituation infoCandidateSituation = InfoCandidateSituation
                        .newInfoFromDomain((ICandidateSituation) situationIter.next());
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
