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

package ServidorAplicacao.Servico.masterDegree.coordinator;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import pt.utl.ist.berserk.logic.serviceManager.IService;
import DataBeans.InfoCandidateSituation;
import DataBeans.InfoExecutionDegree;
import DataBeans.InfoMasterDegreeCandidate;
import DataBeans.InfoMasterDegreeCandidateWithInfoPerson;
import DataBeans.util.Cloner;
import Dominio.ICandidateSituation;
import Dominio.IExecutionDegree;
import Dominio.IMasterDegreeCandidate;
import ServidorAplicacao.Servico.ExcepcaoInexistente;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;
import Util.State;

public class ReadDegreeCandidates implements IService {

    public List run(InfoExecutionDegree infoExecutionDegree) throws ExcepcaoInexistente,
            FenixServiceException {

        ISuportePersistente sp = null;

        List candidates = null;

        try {
            sp = SuportePersistenteOJB.getInstance();

            // Read the Candidates

            IExecutionDegree executionDegree = Cloner
                    .copyInfoExecutionDegree2ExecutionDegree(infoExecutionDegree);

            candidates = sp.getIPersistentMasterDegreeCandidate().readByExecutionDegree(executionDegree);

        } catch (ExcepcaoPersistencia ex) {
            FenixServiceException newEx = new FenixServiceException("Persistence layer error");
            newEx.fillInStackTrace();
            throw newEx;
        }

        if (candidates == null)
            return new ArrayList();

        Iterator iterator = candidates.iterator();
        List result = new ArrayList();
        while (iterator.hasNext()) {
            // For all candidates ...
            IMasterDegreeCandidate masterDegreeCandidate = (IMasterDegreeCandidate) iterator.next();

            //CLONER
            //InfoMasterDegreeCandidate infoMasterDegreeCandidate =
            // Cloner.copyIMasterDegreeCandidate2InfoMasterDegreCandidate(masterDegreeCandidate);
            InfoMasterDegreeCandidate infoMasterDegreeCandidate = InfoMasterDegreeCandidateWithInfoPerson
                    .newInfoFromDomain(masterDegreeCandidate);
            // Copy all Situations
            List situations = new ArrayList();
            Iterator situationIter = masterDegreeCandidate.getSituations().iterator();
            while (situationIter.hasNext()) {
                //CLONER
                //InfoCandidateSituation infoCandidateSituation =
                // Cloner.copyICandidateSituation2InfoCandidateSituation((ICandidateSituation)
                // situationIter.next());
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

    public List run(Integer degreeCurricularPlanId) throws ExcepcaoInexistente, FenixServiceException,
            ExcepcaoPersistencia {

        ISuportePersistente sp = null;

        List candidates = null;

        sp = SuportePersistenteOJB.getInstance();

        // Read the Candidates
        candidates = sp.getIPersistentMasterDegreeCandidate().readByDegreeCurricularPlanId(
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