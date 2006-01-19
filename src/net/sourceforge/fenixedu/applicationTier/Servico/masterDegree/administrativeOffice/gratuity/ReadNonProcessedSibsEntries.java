package net.sourceforge.fenixedu.applicationTier.Servico.masterDegree.administrativeOffice.gratuity;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.sibs.InfoSibsPaymentFileEntry;
import net.sourceforge.fenixedu.domain.gratuity.masterDegree.SibsPaymentFileEntry;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;
import net.sourceforge.fenixedu.persistenceTier.gratuity.masterDegree.IPersistentSibsPaymentFileEntry;
import net.sourceforge.fenixedu.applicationTier.IService;

/**
 * @author <a href="mailto:sana@ist.utl.pt">Shezad Anavarali </a>
 * @author <a href="mailto:naat@ist.utl.pt">Nadir Tarmahomed </a>
 * 
 */
public class ReadNonProcessedSibsEntries implements IService {

	public List run() throws FenixServiceException, ExcepcaoPersistencia {
		List infoSibsFileEntries = new ArrayList();

		ISuportePersistente sp = PersistenceSupportFactory.getDefaultPersistenceSupport();
		IPersistentSibsPaymentFileEntry persistentSibsPaymentFileEntry = sp
				.getIPersistentSibsPaymentFileEntry();

		List sibsFileEntries = persistentSibsPaymentFileEntry.readNonProcessed();

		for (Iterator iter = sibsFileEntries.iterator(); iter.hasNext();) {
			SibsPaymentFileEntry sibsPaymentFileEntry = (SibsPaymentFileEntry) iter.next();
			infoSibsFileEntries.add(InfoSibsPaymentFileEntry.newInfoFromDomain(sibsPaymentFileEntry));
		}

		return infoSibsFileEntries;

	}

}