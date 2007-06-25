package net.sourceforge.fenixedu.applicationTier.Servico.research.prizes;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.research.Prize;
import net.sourceforge.fenixedu.util.MultiLanguageString;

public class CreatePrize extends Service {

	public void run(String name, MultiLanguageString description, Integer year, Person person) {
		new Prize(name,description,year,person);
	}
}
