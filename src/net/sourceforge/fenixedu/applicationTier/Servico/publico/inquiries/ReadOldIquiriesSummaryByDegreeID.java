/*
 * Created on Nov 22, 2004
 * 
 */
package net.sourceforge.fenixedu.applicationTier.Servico.publico.inquiries;

import java.util.List;

import net.sourceforge.fenixedu.applicationTier.FenixService;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.inquiries.InfoOldInquiriesSummary;
import net.sourceforge.fenixedu.domain.Degree;
import net.sourceforge.fenixedu.domain.inquiries.OldInquiriesSummary;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Transformer;

import pt.ist.fenixWebFramework.services.Service;

/**
 * @author Jo�o Fialho & Rita Ferreira
 * 
 */
public class ReadOldIquiriesSummaryByDegreeID extends FenixService {

    @Service
    public static List run(Integer degreeID) throws FenixServiceException {
	Degree degree = rootDomainObject.readDegreeByOID(degreeID);

	if (degree == null) {
	    throw new FenixServiceException("nullDegreeId");
	}

	List<OldInquiriesSummary> oldInquiriesSummaryList = degree.getAssociatedOldInquiriesSummaries();

	CollectionUtils.transform(oldInquiriesSummaryList, new Transformer() {

	    public Object transform(Object oldInquiriesSummary) {
		InfoOldInquiriesSummary iois = new InfoOldInquiriesSummary();
		try {
		    iois.copyFromDomain((OldInquiriesSummary) oldInquiriesSummary);

		} catch (Exception ex) {
		    ex.printStackTrace();
		}

		return iois;
	    }
	});

	return oldInquiriesSummaryList;
    }
}