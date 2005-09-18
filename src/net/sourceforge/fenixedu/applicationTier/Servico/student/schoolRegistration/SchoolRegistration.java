/*
 * Created on Jul 19, 2004
 *
 */
package net.sourceforge.fenixedu.applicationTier.Servico.student.schoolRegistration;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.dataTransferObject.InfoPerson;
import net.sourceforge.fenixedu.dataTransferObject.student.schoolRegistration.InfoResidenceCandidacy;
import net.sourceforge.fenixedu.domain.Country;
import net.sourceforge.fenixedu.domain.DomainFactory;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.ICountry;
import net.sourceforge.fenixedu.domain.IEnrolment;
import net.sourceforge.fenixedu.domain.IExecutionYear;
import net.sourceforge.fenixedu.domain.IPerson;
import net.sourceforge.fenixedu.domain.IRole;
import net.sourceforge.fenixedu.domain.IStudent;
import net.sourceforge.fenixedu.domain.IStudentCurricularPlan;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.person.RoleType;
import net.sourceforge.fenixedu.domain.student.IResidenceCandidacies;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentCountry;
import net.sourceforge.fenixedu.persistenceTier.IPersistentExecutionYear;
import net.sourceforge.fenixedu.persistenceTier.IPersistentRole;
import net.sourceforge.fenixedu.persistenceTier.IPersistentStudent;
import net.sourceforge.fenixedu.persistenceTier.IPessoaPersistente;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;
import net.sourceforge.fenixedu.util.PeriodState;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;

import pt.utl.ist.berserk.logic.serviceManager.IService;

/**
 * @author Nuno Correia
 * @author Ricardo Rodrigues
 */
public class SchoolRegistration implements IService {

    public Boolean run(final IUserView userView, final InfoPerson infoPerson, final InfoResidenceCandidacy infoResidenceCandidacy) 
            throws ExcepcaoPersistencia {

        final ISuportePersistente suportePersistente = PersistenceSupportFactory.getDefaultPersistenceSupport();
        final IPersistentStudent persistentStudent = suportePersistente.getIPersistentStudent();

        final String username = userView.getUtilizador();
        final IStudent student = persistentStudent.readByUsername(username);
        final IPerson person = student.getPerson();

        if (isStudentRegistered(person)) {
            return Boolean.FALSE;
        }

        updatePersonalInfo(suportePersistente, infoPerson, person);
        writeResidenceCandidacy(student, infoResidenceCandidacy);
        updateStudentInfo(suportePersistente, student);

        return Boolean.TRUE;
    }

    private boolean isStudentRegistered(IPerson pessoa) {
        return !pessoa.hasRole(RoleType.FIRST_TIME_STUDENT);
    }

    private void updatePersonalInfo(final ISuportePersistente sp, final InfoPerson infoPerson, final IPerson person)
            throws ExcepcaoPersistencia {

        final IPersistentCountry pCountry = sp.getIPersistentCountry();
        final IPersistentRole pRole = sp.getIPersistentRole();

        final List<ICountry> countries = (List<ICountry>) pCountry.readAll(Country.class);
        final ICountry country = (ICountry) CollectionUtils.find(countries, new Predicate(){
            public boolean evaluate(Object arg0) {
                ICountry country = (ICountry) arg0;
                return country.getNationality().equals(infoPerson.getNacionalidade());
            }});         

        person.edit(infoPerson,country);

        final IRole studentRole = findRole(pRole.readAll(), RoleType.STUDENT);
        final IRole firstTimeStudentRole = findRole(person.getPersonRoles(), RoleType.FIRST_TIME_STUDENT);

        person.addPersonRoles(studentRole);
        person.removePersonRoles(firstTimeStudentRole);
    }

    private IRole findRole(final List<IRole> roles, final RoleType roleType) {
        for (final IRole role : roles) {
            if (role.getRoleType() == roleType) {
                return role;
            }
        }
        return null;
    }

    private void writeResidenceCandidacy(final IStudent student, final InfoResidenceCandidacy infoResidenceCandidacy) 
            throws ExcepcaoPersistencia {

        if (infoResidenceCandidacy != null) {
            final IResidenceCandidacies residenceCandidacy = DomainFactory.makeResidenceCandidacies();

            residenceCandidacy.setStudent(student);
            residenceCandidacy.setCreationDate(new Date());
            residenceCandidacy.setCandidate(infoResidenceCandidacy.getCandidate());
            residenceCandidacy.setObservations(infoResidenceCandidacy.getObservations());
        }
    }

    private void updateStudentInfo(final ISuportePersistente sp, final IStudent student)
            throws ExcepcaoPersistencia {

        final IPersistentExecutionYear pExecutionYear = sp.getIPersistentExecutionYear();
 
        final IExecutionYear executionYear = pExecutionYear.readCurrentExecutionYear();
        student.setRegistrationYear(executionYear);

        final IStudentCurricularPlan scp = student.getActiveStudentCurricularPlan();
        final Date actualDate = Calendar.getInstance().getTime();
        //update the dates, since this objects were already created and only now the student is a registrated student in the campus
        scp.setStartDate(actualDate);
        scp.setWhen(actualDate);

        final List<IEnrolment> enrollments = scp.getEnrolments();
        for (final IEnrolment enrolment : enrollments) {
            enrolment.setCreationDate(actualDate);
        }
    }

}