package ServidorAplicacao.Servico.masterDegree.commons.candidate;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import pt.utl.ist.berserk.logic.serviceManager.IService;
import DataBeans.util.Cloner;
import Dominio.ICandidateEnrolment;
import Dominio.IMasterDegreeCandidate;
import Dominio.MasterDegreeCandidate;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorAplicacao.Servico.exceptions.NonExistingServiceException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;

/**
 * 
 * @author Nuno Nunes (nmsn@rnl.ist.utl.pt) Joana Mota (jccm@rnl.ist.utl.pt)
 */

public class ReadCandidateEnrolmentsByCandidateID implements IService {

    public List run(Integer candidateID) throws FenixServiceException {
        List result = new ArrayList();

        try {
            ISuportePersistente sp = SuportePersistenteOJB.getInstance();

            IMasterDegreeCandidate masterDegreeCandidate = (IMasterDegreeCandidate) sp
                    .getIPersistentMasterDegreeCandidate().readByOID(MasterDegreeCandidate.class,
                            candidateID);

            if (masterDegreeCandidate == null) {
                throw new NonExistingServiceException();
            }

            List candidateEnrolments = sp.getIPersistentCandidateEnrolment().readByMDCandidate(
                    masterDegreeCandidate);

            if (candidateEnrolments == null) {
                throw new NonExistingServiceException();
            }

            Iterator candidateEnrolmentIterator = candidateEnrolments.iterator();

            while (candidateEnrolmentIterator.hasNext()) {
                ICandidateEnrolment candidateEnrolmentTemp = (ICandidateEnrolment) candidateEnrolmentIterator
                        .next();
                result
                        .add(Cloner
                                .copyICandidateEnrolment2InfoCandidateEnrolment(candidateEnrolmentTemp));
            }

        } catch (ExcepcaoPersistencia ex) {
            FenixServiceException newEx = new FenixServiceException("Persistence layer error");
            newEx.fillInStackTrace();
            throw newEx;
        }

        return result;
    }
}