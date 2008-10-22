package net.sourceforge.fenixedu.applicationTier.Servico.Seminaries;

import java.util.LinkedList;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.FenixService;
import net.sourceforge.fenixedu.dataTransferObject.Seminaries.InfoSeminary;
import net.sourceforge.fenixedu.dataTransferObject.Seminaries.InfoSeminaryWithEquivalencies;
import net.sourceforge.fenixedu.domain.Seminaries.Seminary;
import net.sourceforge.fenixedu.presentationTier.Action.Seminaries.Exceptions.BDException;

import org.joda.time.DateTime;

public class GetAllSeminaries extends FenixService {

    public List run(Boolean inEnrollmentPeriod) throws BDException {
	List<InfoSeminary> result = new LinkedList<InfoSeminary>();

	List<Seminary> seminaries = rootDomainObject.getSeminarys();
	for (Seminary seminary : seminaries) {

	    if (!inEnrollmentPeriod) {
		result.add(InfoSeminaryWithEquivalencies.newInfoFromDomain(seminary));
	    } else {
		final DateTime now = new DateTime();
		if (!now.isBefore(seminary.getEnrollmentBeginYearMonthDay().toDateMidnight())
			&& !now.isAfter(seminary.getEnrollmentEndYearMonthDay().toDateMidnight())) {
		    result.add(InfoSeminaryWithEquivalencies.newInfoFromDomain(seminary));
		}
	    }
	}

	return result;
    }

}
