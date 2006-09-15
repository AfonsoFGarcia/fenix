package net.sourceforge.fenixedu.applicationTier.Servico.student.schoolRegistration;

import java.util.List;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.applicationTier.security.PasswordEncryptor;
import net.sourceforge.fenixedu.dataTransferObject.InfoPersonEditor;
import net.sourceforge.fenixedu.dataTransferObject.student.schoolRegistration.InfoResidenceCandidacy;
import net.sourceforge.fenixedu.domain.Country;
import net.sourceforge.fenixedu.domain.Enrolment;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.Role;
import net.sourceforge.fenixedu.domain.StudentCurricularPlan;
import net.sourceforge.fenixedu.domain.person.RoleType;
import net.sourceforge.fenixedu.domain.student.Registration;
import net.sourceforge.fenixedu.domain.student.ResidenceCandidacies;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;

import org.joda.time.DateTime;
import org.joda.time.YearMonthDay;

public class SchoolRegistration extends Service {

    public Boolean run(final IUserView userView, final InfoPersonEditor infoPersonEditor,
	    final InfoResidenceCandidacy infoResidenceCandidacy) throws ExcepcaoPersistencia {

	final String username = userView.getUtilizador();
	final Registration registration = Registration.readByUsername(username);
	final Person person = registration.getPerson();

	if (isStudentRegistered(person)) {
	    return Boolean.FALSE;
	}

	updatePersonalInfo(infoPersonEditor, person);
	writeResidenceCandidacy(registration, infoResidenceCandidacy);
	updateStudentInfo(registration);

	return Boolean.TRUE;
    }

    private boolean isStudentRegistered(Person pessoa) {
	return !pessoa.hasRole(RoleType.FIRST_TIME_STUDENT);
    }

    private void updatePersonalInfo(final InfoPersonEditor infoPersonEditor, final Person person) {

	final Country country;
	if (infoPersonEditor.getInfoPais() != null
		&& infoPersonEditor.getInfoPais().getNationality() != null) {
	    country = Country.readCountryByNationality(infoPersonEditor.getInfoPais().getNationality());
	} else {
	    // If the person country is undefined it is set to default
	    // "PORTUGUESA NATURAL DO CONTINENTE"
	    // In a not distance future this will not be needed since the
	    // coutry
	    // can never be null
	    country = Country.readCountryByNationality("PORTUGUESA NATURAL DO CONTINENTE");
	}

	person.edit(infoPersonEditor, country);
	person.setPassword(PasswordEncryptor.encryptPassword(infoPersonEditor.getPassword()));

	final Role studentRole = findRole(rootDomainObject.getRoles(), RoleType.STUDENT);
	final Role firstTimeStudentRole = findRole(person.getPersonRoles(), RoleType.FIRST_TIME_STUDENT);

	person.addPersonRoles(studentRole);
	person.removePersonRoles(firstTimeStudentRole);
    }

    private Role findRole(final List<Role> roles, final RoleType roleType) {
	for (final Role role : roles) {
	    if (role.getRoleType() == roleType) {
		return role;
	    }
	}
	return null;
    }

    private void writeResidenceCandidacy(final Registration registration,
	    final InfoResidenceCandidacy infoResidenceCandidacy) throws ExcepcaoPersistencia {

	if (infoResidenceCandidacy != null) {
	    registration.getStudent().setResidenceCandidacyForCurrentExecutionYear(
		    infoResidenceCandidacy.getObservations());
	}
    }

    private void updateStudentInfo(final Registration registration) throws ExcepcaoPersistencia {

	final ExecutionYear executionYear = ExecutionYear.readCurrentExecutionYear();
	registration.setRegistrationYear(executionYear);

	final StudentCurricularPlan scp = registration.getActiveStudentCurricularPlan();
	final DateTime actualDate = new DateTime();
	// update the dates, since this objects were already created and only
	// now the student is a registrated student in the campus
	scp.setStartDateYearMonthDay(new YearMonthDay());
	scp.setWhenDateTime(actualDate);

	final List<Enrolment> enrollments = scp.getEnrolments();
	for (final Enrolment enrolment : enrollments) {
	    enrolment.setCreationDateDateTime(actualDate);
	}
    }

}