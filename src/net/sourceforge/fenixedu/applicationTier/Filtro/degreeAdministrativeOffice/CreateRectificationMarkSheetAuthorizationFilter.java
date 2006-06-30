package net.sourceforge.fenixedu.applicationTier.Filtro.degreeAdministrativeOffice;

import java.util.HashSet;
import java.util.Set;

public class CreateRectificationMarkSheetAuthorizationFilter extends MarkSheetAuthorizationFilter {

    @Override
    public Set<String> getAuthorizedEmployees() {
        Set<String> authorizedEmployees = new HashSet<String>();
        authorizedEmployees.add("1272");
        authorizedEmployees.add("1268");
        authorizedEmployees.add("2675");
        authorizedEmployees.add("3978");
        return authorizedEmployees;
    }

}
