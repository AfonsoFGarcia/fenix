package net.sourceforge.fenixedu.applicationTier.Servico.research.prizes;

import net.sourceforge.fenixedu.applicationTier.FenixService;
import net.sourceforge.fenixedu.domain.organizationalStructure.Party;
import net.sourceforge.fenixedu.domain.research.Prize;

public class RemovePartyFromPrize extends FenixService {

	public void run(Party party, Prize prize) {
		prize.getParties().remove(party);
	}
}
