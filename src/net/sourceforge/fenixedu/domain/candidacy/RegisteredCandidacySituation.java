package net.sourceforge.fenixedu.domain.candidacy;

import java.util.HashSet;
import java.util.Set;

import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.Qualification;
import net.sourceforge.fenixedu.domain.Role;
import net.sourceforge.fenixedu.domain.accounting.events.dfa.DfaRegistrationEvent;
import net.sourceforge.fenixedu.domain.accounting.events.gratuity.DfaGratuityEvent;
import net.sourceforge.fenixedu.domain.administrativeOffice.AdministrativeOffice;
import net.sourceforge.fenixedu.domain.administrativeOffice.AdministrativeOfficeType;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.person.RoleType;
import net.sourceforge.fenixedu.domain.student.Registration;
import net.sourceforge.fenixedu.domain.student.Student;
import net.sourceforge.fenixedu.injectionCode.AccessControl;

import org.joda.time.YearMonthDay;

public class RegisteredCandidacySituation extends RegisteredCandidacySituation_Base {

    public RegisteredCandidacySituation(Candidacy candidacy) {
	this(candidacy, (AccessControl.getUserView() != null) ? AccessControl.getPerson()
		: null);
    }

    public RegisteredCandidacySituation(Candidacy candidacy, Person person) {
	super();
	init(candidacy, person);

	if (getCandidacy() instanceof DFACandidacy) {
	    registerDFACandidacy(candidacy);
	}
    }

    private void registerDFACandidacy(Candidacy candidacy) {
	Person person = candidacy.getPerson();
	Student student = person.getStudent();
	if (student == null) {
	    student = new Student(person);
	}

	// create registration
	Registration registration = createNewRegistration((DFACandidacy) candidacy);

	createQualification();

	((DFACandidacy) getCandidacy()).setRegistration(registration);

	final AdministrativeOffice administrativeOffice = AdministrativeOffice
		.readByAdministrativeOfficeType(AdministrativeOfficeType.MASTER_DEGREE);

	new DfaGratuityEvent(administrativeOffice, person, registration.getActiveStudentCurricularPlan(),
		ExecutionYear.readCurrentExecutionYear());

	new DfaRegistrationEvent(administrativeOffice, person, registration);

    }

    private Registration createNewRegistration(DFACandidacy candidacy) {

	Person person = getCandidacy().getPerson();

	Registration registration = new Registration(person, ((DFACandidacy) candidacy)
		.getExecutionDegree().getDegreeCurricularPlan());

	person.addPersonRoles(Role.getRoleByRoleType(RoleType.STUDENT));

	return registration;
    }

    private void createQualification() {
	DFACandidacy dfaCandidacy = (DFACandidacy) getCandidacy();
	if (dfaCandidacy.hasPrecedentDegreeInformation()) {
	    Qualification qualification = new Qualification();
	    qualification.setPerson(dfaCandidacy.getPerson());
	    qualification.setMark(dfaCandidacy.getPrecedentDegreeInformation().getConclusionGrade());
	    qualification.setSchool(dfaCandidacy.getPrecedentDegreeInformation().getInstitutionName());
	    qualification.setDegree(dfaCandidacy.getPrecedentDegreeInformation().getDegreeDesignation());
	    if (dfaCandidacy.getPrecedentDegreeInformation().getConclusionYear() != null) {
		qualification.setDateYearMonthDay(new YearMonthDay(dfaCandidacy
			.getPrecedentDegreeInformation().getConclusionYear(), 1, 1));
	    }
	    qualification.setCountry(dfaCandidacy.getPrecedentDegreeInformation().getCountry());
	}
    }

    @Override
    public void checkConditionsToForward() {
	throw new DomainException("error.impossible.to.forward.from.registered");
    }

    @Override
    public void checkConditionsToForward(String nextState) {
	throw new DomainException("error.impossible.to.forward.from.registered");
    }

    @Override
    public CandidacySituationType getCandidacySituationType() {
	return CandidacySituationType.REGISTERED;
    }

    @Override
    public Set<String> getValidNextStates() {
	return new HashSet<String>();
    }

    @Override
    public void nextState() {
	throw new DomainException("error.impossible.to.forward.from.registered");
    }

    @Override
    public void nextState(String nextState) {
	throw new DomainException("error.impossible.to.forward.from.registered");
    }

    @Override
    public boolean canExecuteOperationAutomatically() {
	return false;
    }

}