package net.sourceforge.fenixedu.applicationTier.Servico.site;

import net.sourceforge.fenixedu.applicationTier.FenixService;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.UnitSite;

/**
 * Removes a person from the managers of a UnitSite.
 * 
 * @author cfgi
 */
public class RemoveUnitSiteManager extends FenixService {

    public void run(UnitSite site, Person person) {
        site.removeManagers(person);
    }

}
