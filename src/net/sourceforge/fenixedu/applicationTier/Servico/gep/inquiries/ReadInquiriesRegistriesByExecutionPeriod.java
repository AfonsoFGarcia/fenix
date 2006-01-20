/*
 * Created on 1/Jun/2005 - 16:58:33
 * 
 */

package net.sourceforge.fenixedu.applicationTier.Servico.gep.inquiries;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.inquiries.InfoInquiriesRegistry;
import net.sourceforge.fenixedu.domain.inquiries.InquiriesRegistry;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.inquiries.IPersistentInquiriesRegistry;

/**
 * @author Jo�o Fialho & Rita Ferreira
 *
 */
public class ReadInquiriesRegistriesByExecutionPeriod extends Service {

    public List<InfoInquiriesRegistry> run(Integer executionPeriodId) throws FenixServiceException,
    ExcepcaoPersistencia, NoSuchMethodException, InvocationTargetException,
    NoSuchMethodException, IllegalAccessException {

		if (executionPeriodId == null) {
		    throw new FenixServiceException("nullExecutionPeriodId");
		}
		
		IPersistentInquiriesRegistry inquiriesRegistryDAO = persistentSupport.getIPersistentInquiriesRegistry();
		List<InquiriesRegistry> inquiriesRegistries = inquiriesRegistryDAO.readByExecutionPeriodId(executionPeriodId);
		
		List<InfoInquiriesRegistry> infoInquiriesRegistries = new ArrayList<InfoInquiriesRegistry>(
		        inquiriesRegistries.size());
		
		for (InquiriesRegistry inquiriesRegistry : inquiriesRegistries) {
		    infoInquiriesRegistries.add(InfoInquiriesRegistry.newInfoFromDomain(inquiriesRegistry));
		}
		
		return infoInquiriesRegistries;
	}

}
