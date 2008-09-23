package net.sourceforge.fenixedu.applicationTier.Servico.accounting;

import java.util.List;

import net.sourceforge.fenixedu.applicationTier.FenixService;
import net.sourceforge.fenixedu.domain.Employee;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.accounting.Entry;
import net.sourceforge.fenixedu.domain.accounting.Receipt;
import net.sourceforge.fenixedu.domain.organizationalStructure.Party;
import net.sourceforge.fenixedu.domain.organizationalStructure.Unit;

public class CreateReceipt extends FenixService {

    public CreateReceipt() {
	super();
    }

    public Receipt run(final Employee employee, final Person person, final Party contributor, final String contributorName,
	    final Integer year, final Unit creatorUnit, final Unit ownerUnit, final List<Entry> entries) {

	return Receipt.createWithContributorPartyOrContributorName(employee, person, contributor, contributorName, year,
		creatorUnit, ownerUnit, entries);
    }

}