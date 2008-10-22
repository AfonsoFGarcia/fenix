package net.sourceforge.fenixedu.applicationTier.Servico.gep.inquiries;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.FenixService;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.inquiries.InfoInquiriesRegistry;
import net.sourceforge.fenixedu.domain.ExecutionSemester;
import net.sourceforge.fenixedu.domain.inquiries.InquiriesRegistry;
import pt.ist.fenixWebFramework.services.Service;

public class ReadInquiriesRegistriesByExecutionPeriod extends FenixService {

    @Service
    public static List<InfoInquiriesRegistry> run(Integer executionPeriodId) throws FenixServiceException,
	    IllegalAccessException, InvocationTargetException, NoSuchMethodException {

	if (executionPeriodId == null) {
	    throw new FenixServiceException("nullExecutionPeriodId");
	}

	ExecutionSemester executionSemester = rootDomainObject.readExecutionSemesterByOID(executionPeriodId);
	List<InquiriesRegistry> inquiriesRegistries = executionSemester.getAssociatedInquiriesRegistries();

	List<InfoInquiriesRegistry> infoInquiriesRegistries = new ArrayList<InfoInquiriesRegistry>(inquiriesRegistries.size());
	for (InquiriesRegistry inquiriesRegistry : inquiriesRegistries) {
	    infoInquiriesRegistries.add(InfoInquiriesRegistry.newInfoFromDomain(inquiriesRegistry));
	}

	return infoInquiriesRegistries;
    }

}