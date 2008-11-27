package net.sourceforge.fenixedu.applicationTier.Servico.masterDegree.administrativeOffice.candidate;

import pt.ist.fenixWebFramework.services.Service;

import pt.ist.fenixWebFramework.security.accessControl.Checked;

import java.util.Calendar;

import net.sourceforge.fenixedu.applicationTier.FenixService;
import net.sourceforge.fenixedu.domain.CandidateSituation;
import net.sourceforge.fenixedu.domain.ExecutionDegree;
import net.sourceforge.fenixedu.domain.MasterDegreeCandidate;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.util.SituationName;
import net.sourceforge.fenixedu.util.State;

public class CreateCandidateSituation extends FenixService {

    @Checked("RolePredicates.MASTER_DEGREE_ADMINISTRATIVE_OFFICE_PREDICATE")
    @Service
    public static void run(Integer executionDegreeID, Integer personID, SituationName newSituation) {

	final Person person = (Person) rootDomainObject.readPartyByOID(personID);
	final ExecutionDegree executionDegree = rootDomainObject.readExecutionDegreeByOID(executionDegreeID);

	final MasterDegreeCandidate masterDegreeCandidate = person.getMasterDegreeCandidateByExecutionDegree(executionDegree);
	for (final CandidateSituation candidateSituation : masterDegreeCandidate.getSituations()) {
	    if (candidateSituation.getValidation().equals(new State(State.ACTIVE))) {
		candidateSituation.setValidation(new State(State.INACTIVE));
	    }
	}
	// Create the New Candidate Situation
	final Calendar calendar = Calendar.getInstance();
	new CandidateSituation(calendar.getTime(), null, new State(State.ACTIVE), masterDegreeCandidate, newSituation);
    }
}