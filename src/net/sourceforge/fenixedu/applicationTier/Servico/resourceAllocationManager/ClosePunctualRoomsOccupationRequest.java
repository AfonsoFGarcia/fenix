package net.sourceforge.fenixedu.applicationTier.Servico.resourceAllocationManager;

import pt.ist.fenixWebFramework.services.Service;

import pt.ist.fenixWebFramework.security.accessControl.Checked;

import net.sourceforge.fenixedu.applicationTier.FenixService;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.PunctualRoomsOccupationRequest;

import org.joda.time.DateTime;

public class ClosePunctualRoomsOccupationRequest extends FenixService {

    @Checked("RolePredicates.RESOURCE_ALLOCATION_MANAGER_PREDICATE")
    @Service
    public static void run(PunctualRoomsOccupationRequest request, Person person) {
	if (request != null) {
	    request.closeRequestAndAssociateOwnerOnlyForEmployees(new DateTime(), person);
	}
    }
}