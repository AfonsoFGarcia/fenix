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
import net.sourceforge.fenixedu.domain.ExecutionSemester;
import net.sourceforge.fenixedu.domain.inquiries.OldInquiriesSummary;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Transformer;

/**
 * @author Jo�o Fialho & Rita Ferreira
 * 
 */
public class ReadOldIquiriesSummaryByDegreeIDAndExecutionPeriodID extends FenixService {

    public List run(Integer degreeID, Integer executionPeriodID) throws FenixServiceException {
	Degree degree = rootDomainObject.readDegreeByOID(degreeID);
	ExecutionSemester executionSemester = rootDomainObject.readExecutionSemesterByOID(executionPeriodID);

	if (degree == null) {
	    throw new FenixServiceException("nullDegreeId");
	}
	if (executionSemester == null) {
	    throw new FenixServiceException("nullExecutionPeriodId");
	}

	List<OldInquiriesSummary> oldInquiriesSummaryList = degree.getOldInquiriesSummariesByExecutionPeriod(executionSemester);

	CollectionUtils.transform(oldInquiriesSummaryList, new Transformer() {

	    public Object transform(Object oldInquiriesSummary) {
		InfoOldInquiriesSummary iois = new InfoOldInquiriesSummary();
		try {
		    iois.copyFromDomain((OldInquiriesSummary) oldInquiriesSummary);

		} catch (Exception ex) {
		}

		return iois;
	    }
	});

	return oldInquiriesSummaryList;
    }
}
