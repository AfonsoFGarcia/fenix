package net.sourceforge.fenixedu.applicationTier.Servico.commons.searchers;

import java.util.Collection;
import java.util.Map;

import net.sourceforge.fenixedu.applicationTier.FenixService;
import net.sourceforge.fenixedu.applicationTier.Servico.commons.AutoCompleteSearchService;

public abstract class SearchParties extends FenixService implements AutoCompleteSearchService {

    private static int DEFAULT_SIZE = 50;

    @Override
    public Collection run(Class type, String value, int limit, Map<String, String> arguments) {
        int size = getSize(arguments);
        return search(value, size);
    }

    protected abstract Collection search(String value, int size);

    private int getSize(Map<String, String> arguments) {
        String size = arguments.get("size");

        if (size == null) {
            return DEFAULT_SIZE;
        } else {
            return Integer.parseInt(size);
        }
    }

}
