package net.sourceforge.fenixedu.applicationTier.Servico.masterDegree.administrativeOffice.thesis;

import net.sourceforge.fenixedu.dataTransferObject.InfoMasterDegreeThesisDataVersion;
import net.sourceforge.fenixedu.dataTransferObject.util.Cloner;
import net.sourceforge.fenixedu.domain.IMasterDegreeThesisDataVersion;
import net.sourceforge.fenixedu.domain.MasterDegreeThesisDataVersion;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NonExistingServiceException;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.OJB.SuportePersistenteOJB;
import pt.utl.ist.berserk.logic.serviceManager.IService;

/**
 * 
 * @author - Shezad Anavarali (sana@mega.ist.utl.pt) - Nadir Tarmahomed
 *         (naat@mega.ist.utl.pt)
 *  
 */
public class ReadMasterDegreeThesisDataVersionByID implements IService {

    public Object run(Integer masterDegreeThesisDataVersionID) throws FenixServiceException {
        InfoMasterDegreeThesisDataVersion infoMasterDegreeThesisDataVersion = null;
        IMasterDegreeThesisDataVersion masterDegreeThesisDataVersion = null;

        try {
            ISuportePersistente sp = SuportePersistenteOJB.getInstance();
            masterDegreeThesisDataVersion = (IMasterDegreeThesisDataVersion) sp
                    .getIPersistentMasterDegreeThesisDataVersion().readByOID(
                            MasterDegreeThesisDataVersion.class, masterDegreeThesisDataVersionID);

            if (masterDegreeThesisDataVersion == null)
                throw new NonExistingServiceException(
                        "error.exception.masterDegree.nonExistingMasterDegreeThesisDataVersion");

            infoMasterDegreeThesisDataVersion = Cloner
                    .copyIMasterDegreeThesisDataVersion2InfoMasterDegreeThesisDataVersion(masterDegreeThesisDataVersion);

        } catch (ExcepcaoPersistencia ex) {
            FenixServiceException newEx = new FenixServiceException("Persistence layer error");
            newEx.fillInStackTrace();
            throw newEx;
        }

        return infoMasterDegreeThesisDataVersion;
    }
}