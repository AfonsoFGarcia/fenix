package net.sourceforge.fenixedu.applicationTier.Servico.gep.inquiries;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.inquiries.InfoInquiriesRegistry;
import net.sourceforge.fenixedu.domain.ExecutionPeriod;
import net.sourceforge.fenixedu.domain.inquiries.InquiriesRegistry;

public class ReadInquiriesRegistriesByExecutionPeriod extends Service {

    public List<InfoInquiriesRegistry> run(Integer executionPeriodId) throws FenixServiceException,
            IllegalAccessException, InvocationTargetException, NoSuchMethodException {

        if (executionPeriodId == null) {
            throw new FenixServiceException("nullExecutionPeriodId");
        }

        ExecutionPeriod executionPeriod = rootDomainObject.readExecutionPeriodByOID(executionPeriodId);
        List<InquiriesRegistry> inquiriesRegistries = executionPeriod.getAssociatedInquiriesRegistries();

        List<InfoInquiriesRegistry> infoInquiriesRegistries = new ArrayList<InfoInquiriesRegistry>(
                inquiriesRegistries.size());
        for (InquiriesRegistry inquiriesRegistry : inquiriesRegistries) {
            infoInquiriesRegistries.add(InfoInquiriesRegistry.newInfoFromDomain(inquiriesRegistry));
        }
        
        return infoInquiriesRegistries;
    }

}
