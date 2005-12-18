/*
 * Created on Feb 4, 2005
 * 
 */
package net.sourceforge.fenixedu.applicationTier.Servico.teacher.inquiries;

import java.lang.reflect.InvocationTargetException;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.inquiries.InfoOldInquiriesTeachersRes;
import net.sourceforge.fenixedu.domain.inquiries.IOldInquiriesTeachersRes;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;
import net.sourceforge.fenixedu.persistenceTier.inquiries.IPersistentOldInquiriesTeachersRes;
import pt.utl.ist.berserk.logic.serviceManager.IService;

/**
 * @author Jo�o Fialho & Rita Ferreira
 * 
 */
public class ReadOldInquiryTeachersResById implements IService {

	public InfoOldInquiriesTeachersRes run(Integer internalId) throws FenixServiceException, ExcepcaoPersistencia, IllegalAccessException, InvocationTargetException, NoSuchMethodException {
		InfoOldInquiriesTeachersRes oldInquiriesTeachersRes = null;

		if (internalId == null) {
			throw new FenixServiceException("nullInternalId");
		}
		ISuportePersistente sp = PersistenceSupportFactory.getDefaultPersistenceSupport();
		IPersistentOldInquiriesTeachersRes poits = sp.getIPersistentOldInquiriesTeachersRes();

		IOldInquiriesTeachersRes oits = poits.readByInternalId(internalId);

		oldInquiriesTeachersRes = new InfoOldInquiriesTeachersRes();
		oldInquiriesTeachersRes.copyFromDomain(oits);

		return oldInquiriesTeachersRes;
	}

}
