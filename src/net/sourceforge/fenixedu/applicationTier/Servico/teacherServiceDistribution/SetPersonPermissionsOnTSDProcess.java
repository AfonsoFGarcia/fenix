package net.sourceforge.fenixedu.applicationTier.Servico.teacherServiceDistribution;

import net.sourceforge.fenixedu.applicationTier.FenixService;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.teacherServiceDistribution.TSDProcess;

public class SetPersonPermissionsOnTSDProcess extends FenixService {
    public void run(Integer tsdProcessId, Integer personId, Boolean phaseManagementPermission,
	    Boolean automaticValuationPermission, Boolean omissionConfigurationPermission,
	    Boolean tsdCoursesAndTeachersManagementPermission) {

	TSDProcess tsdProcess = rootDomainObject.readTSDProcessByOID(tsdProcessId);
	Person person = (Person) rootDomainObject.readPartyByOID(personId);

	if (phaseManagementPermission) {
	    tsdProcess.addPhasesManagementPermission(person);
	} else {
	    tsdProcess.removePhasesManagementPermission(person);
	}

	if (automaticValuationPermission) {
	    tsdProcess.addAutomaticValuationPermission(person);
	} else {
	    tsdProcess.removeAutomaticValuationPermission(person);
	}

	if (omissionConfigurationPermission) {
	    tsdProcess.addOmissionConfigurationPermission(person);
	} else {
	    tsdProcess.removeOmissionConfigurationPermission(person);
	}

	if (tsdCoursesAndTeachersManagementPermission) {
	    tsdProcess.addCompetenceCoursesAndTeachersManagement(person);
	} else {
	    tsdProcess.removeCompetenceCoursesAndTeachersManagement(person);
	}
    }
}
