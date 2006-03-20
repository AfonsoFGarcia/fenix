/*
 * Created on Jul 19, 2004
 *
 */
package net.sourceforge.fenixedu.applicationTier.Servico.student.schoolRegistration;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.applicationTier.security.PasswordEncryptor;
import net.sourceforge.fenixedu.dataTransferObject.InfoPerson;
import net.sourceforge.fenixedu.dataTransferObject.student.schoolRegistration.InfoResidenceCandidacy;
import net.sourceforge.fenixedu.domain.Country;
import net.sourceforge.fenixedu.domain.DomainFactory;
import net.sourceforge.fenixedu.domain.Enrolment;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.Role;
import net.sourceforge.fenixedu.domain.Student;
import net.sourceforge.fenixedu.domain.StudentCurricularPlan;
import net.sourceforge.fenixedu.domain.person.RoleType;
import net.sourceforge.fenixedu.domain.student.ResidenceCandidacies;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentExecutionYear;
import net.sourceforge.fenixedu.persistenceTier.IPersistentStudent;

/**
 * @author Nuno Correia
 * @author Ricardo Rodrigues
 */
public class SchoolRegistration extends Service {

    public Boolean run(final IUserView userView, final InfoPerson infoPerson, final InfoResidenceCandidacy infoResidenceCandidacy) 
            throws ExcepcaoPersistencia {
        final IPersistentStudent persistentStudent = persistentSupport.getIPersistentStudent();

        final String username = userView.getUtilizador();
        final Student student = persistentStudent.readByUsername(username);

        
        final Person person = student.getPerson();

        if (isStudentRegistered(person)) {
            return Boolean.FALSE;
        }

        updatePersonalInfo(infoPerson, person);
        writeResidenceCandidacy(student, infoResidenceCandidacy);
        updateStudentInfo(student);

        return Boolean.TRUE;
    }

    private boolean isStudentRegistered(Person pessoa) {
        return !pessoa.hasRole(RoleType.FIRST_TIME_STUDENT);
    }

    private void updatePersonalInfo(final InfoPerson infoPerson, final Person person)
            throws ExcepcaoPersistencia {

        final Country country;
        if (infoPerson.getInfoPais() != null && infoPerson.getInfoPais().getNationality() != null){
            country = Country.readCountryByNationality(infoPerson.getInfoPais().getNationality());
        } else {
            //If the person country is undefined it is set to default "PORTUGUESA NATURAL DO CONTINENTE" 
            //In a not distance future this will not be needed since the coutry can never be null
            country = Country.readCountryByNationality("PORTUGUESA NATURAL DO CONTINENTE");
        }
        
        person.edit(infoPerson,country);
        person.setPassword(PasswordEncryptor.encryptPassword(infoPerson.getPassword()));

        final Role studentRole = findRole((List<Role>) persistentObject.readAll(Role.class), RoleType.STUDENT);
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

    private void writeResidenceCandidacy(final Student student, final InfoResidenceCandidacy infoResidenceCandidacy) 
            throws ExcepcaoPersistencia {

        if (infoResidenceCandidacy != null) {
            final ResidenceCandidacies residenceCandidacy = DomainFactory.makeResidenceCandidacies();

            residenceCandidacy.setStudent(student);
            residenceCandidacy.setCreationDate(new Date());
            residenceCandidacy.setCandidate(infoResidenceCandidacy.getCandidate());
            residenceCandidacy.setObservations(infoResidenceCandidacy.getObservations());
        }
    }

    private void updateStudentInfo(final Student student)
            throws ExcepcaoPersistencia {

        final IPersistentExecutionYear pExecutionYear = persistentSupport.getIPersistentExecutionYear();
 
        final ExecutionYear executionYear = pExecutionYear.readCurrentExecutionYear();
        student.setRegistrationYear(executionYear);

        final StudentCurricularPlan scp = student.getActiveStudentCurricularPlan();
        final Date actualDate = Calendar.getInstance().getTime();
        //update the dates, since this objects were already created and only now the student is a registrated student in the campus
        scp.setStartDate(actualDate);
        scp.setWhen(actualDate);

        final List<Enrolment> enrollments = scp.getEnrolments();
        for (final Enrolment enrolment : enrollments) {
            enrolment.setCreationDate(actualDate);
        }
    }

}