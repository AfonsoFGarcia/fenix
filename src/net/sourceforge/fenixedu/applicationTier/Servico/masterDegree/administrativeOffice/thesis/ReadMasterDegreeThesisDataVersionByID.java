package net.sourceforge.fenixedu.applicationTier.Servico.masterDegree.administrativeOffice.thesis;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NonExistingServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoMasterDegreeThesisDataVersion;
import net.sourceforge.fenixedu.dataTransferObject.InfoMasterDegreeThesisDataVersionWithGuidersAndRespAndThesis;
import net.sourceforge.fenixedu.domain.MasterDegreeThesisDataVersion;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;

/**
 * 
 * @author - Shezad Anavarali (sana@mega.ist.utl.pt) - Nadir Tarmahomed
 *         (naat@mega.ist.utl.pt)
 * 
 */
public class ReadMasterDegreeThesisDataVersionByID extends Service {

	public Object run(Integer masterDegreeThesisDataVersionID) throws FenixServiceException, ExcepcaoPersistencia {
		InfoMasterDegreeThesisDataVersion infoMasterDegreeThesisDataVersion = null;
		MasterDegreeThesisDataVersion masterDegreeThesisDataVersion = null;

		masterDegreeThesisDataVersion = (MasterDegreeThesisDataVersion) persistentObject.readByOID(
						MasterDegreeThesisDataVersion.class, masterDegreeThesisDataVersionID);

		if (masterDegreeThesisDataVersion == null)
			throw new NonExistingServiceException(
					"error.exception.masterDegree.nonExistingMasterDegreeThesisDataVersion");

		infoMasterDegreeThesisDataVersion = InfoMasterDegreeThesisDataVersionWithGuidersAndRespAndThesis
				.newInfoFromDomain(masterDegreeThesisDataVersion);

		return infoMasterDegreeThesisDataVersion;
	}
}