package net.sourceforge.fenixedu.applicationTier.Servico.scientificCouncil.curricularPlans;

import net.sourceforge.fenixedu.applicationTier.FenixService;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.InvalidArgumentsServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NonExistingServiceException;
import net.sourceforge.fenixedu.domain.Degree;
import pt.ist.fenixWebFramework.security.accessControl.Checked;
import pt.ist.fenixWebFramework.services.Service;

public class DeleteDegree extends FenixService {

    @Checked("RolePredicates.SCIENTIFIC_COUNCIL_PREDICATE")
    @Service
    public static void run(Integer idInternal) throws FenixServiceException {
        if (idInternal == null) {
            throw new InvalidArgumentsServiceException();
        }

        final Degree degreeToDelete = rootDomainObject.readDegreeByOID(idInternal);

        if (degreeToDelete == null) {
            throw new NonExistingServiceException();
        } else {
            degreeToDelete.delete();
        }
    }

}