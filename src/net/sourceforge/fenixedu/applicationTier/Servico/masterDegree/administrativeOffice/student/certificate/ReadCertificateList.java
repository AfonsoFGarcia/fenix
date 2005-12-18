package net.sourceforge.fenixedu.applicationTier.Servico.masterDegree.administrativeOffice.student.certificate;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Servico.ExcepcaoInexistente;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoPrice;
import net.sourceforge.fenixedu.domain.GraduationType;
import net.sourceforge.fenixedu.domain.IPrice;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;
import pt.utl.ist.berserk.logic.serviceManager.IService;

/**
 * @author Nuno Nunes (nmsn@rnl.ist.utl.pt) Joana Mota (jccm@rnl.ist.utl.pt)
 */
public class ReadCertificateList implements IService {

	public List run(GraduationType graduationType, List types) throws FenixServiceException, ExcepcaoPersistencia {

		ISuportePersistente sp = null;
		List certificates = null;

		sp = PersistenceSupportFactory.getDefaultPersistenceSupport();

		// Read the certificates

		certificates = sp.getIPersistentPrice().readByGraduationTypeAndDocumentType(graduationType,
				types);

		if (certificates == null)
			throw new ExcepcaoInexistente("No Certificates Found !!");

		List result = new ArrayList();
		Iterator iterator = certificates.iterator();

		while (iterator.hasNext()) {
			IPrice price = (IPrice) iterator.next();
			result.add(InfoPrice.newInfoFromDoaim(price));
		}
		return result;
	}

}