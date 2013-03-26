package net.sourceforge.fenixedu.applicationTier.Servico.research;

import java.util.Collection;
import java.util.Map;

import net.sourceforge.fenixedu.applicationTier.FenixService;
import net.sourceforge.fenixedu.applicationTier.Servico.commons.AutoCompleteSearchService;
import net.sourceforge.fenixedu.domain.person.PersonName;

public class SearchPersonsForParticipations extends FenixService implements AutoCompleteSearchService {

    @Override
    public Collection<PersonName> run(Class type, String value, int limit, Map<String, String> arguments) {
        if (type != PersonName.class) {
            return null;
        }

        String size = arguments.get("size");
        return PersonName.findInternalPerson(value, (size == null) ? 20 : Integer.parseInt(size));
    }

}
