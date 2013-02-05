package net.sourceforge.fenixedu.domain.candidacy;

import net.sourceforge.fenixedu.domain.EntryPhase;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.Qualification;
import net.sourceforge.fenixedu.domain.degreeStructure.CycleType;
import net.sourceforge.fenixedu.domain.student.Registration;
import net.sourceforge.fenixedu.domain.student.RegistrationAgreement;
import net.sourceforge.fenixedu.domain.student.Student;
import net.sourceforge.fenixedu.domain.util.workflow.IState;
import net.sourceforge.fenixedu.domain.util.workflow.StateBean;
import net.sourceforge.fenixedu.injectionCode.AccessControl;

import org.joda.time.YearMonthDay;

public class RegisteredCandidacySituation extends RegisteredCandidacySituation_Base {

    public RegisteredCandidacySituation(Candidacy candidacy) {
        this(candidacy, null);
    }

    public RegisteredCandidacySituation(Candidacy candidacy, Person person) {
        this(candidacy, person, null, null);
    }

    public RegisteredCandidacySituation(Candidacy candidacy, RegistrationAgreement registrationAgreement, CycleType cycleType,
            Ingression ingression, EntryPhase entryPhase, Integer studentNumber) {
        super();
        init(candidacy, AccessControl.getPerson());
        registerCandidacy(registrationAgreement, cycleType, studentNumber);

        ((StudentCandidacy) candidacy).setIngression(ingression);
        ((StudentCandidacy) candidacy).setEntryPhase(entryPhase);
    }

    private RegisteredCandidacySituation(Candidacy candidacy, Person person, RegistrationAgreement registrationAgreement,
            CycleType cycleType) {
        super();
        init(candidacy, person == null ? AccessControl.getPerson() : person);
        registerCandidacy(registrationAgreement, cycleType, null);
    }

    private void registerCandidacy(RegistrationAgreement registrationAgreement, CycleType cycleType, Integer studentNumber) {
        Person person = getCandidacy().getPerson();
        Registration registration = null;

        if (getCandidacy() instanceof DFACandidacy) {
            DFACandidacy dfaCandidacy = ((DFACandidacy) getCandidacy());
            registration =
                    new Registration(person, dfaCandidacy.getExecutionDegree().getDegreeCurricularPlan(), dfaCandidacy,
                            registrationAgreement, cycleType, dfaCandidacy.getExecutionDegree().getExecutionYear());

            //person.addPersonRoles(Role.getRoleByRoleType(RoleType.STUDENT));
            dfaCandidacy.setRegistration(registration);
            createQualification();

        }

        if (!person.hasStudent() && studentNumber == null) {
            new Student(person);
        } else if (!person.hasStudent() && studentNumber != null) {
            Student.createStudentWithCustomNumber(person, studentNumber);
        }
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
                qualification.setDateYearMonthDay(new YearMonthDay(dfaCandidacy.getPrecedentDegreeInformation()
                        .getConclusionYear(), 1, 1));
            }
            qualification.setCountry(dfaCandidacy.getPrecedentDegreeInformation().getCountry());
        }
    }

    @Override
    public CandidacySituationType getCandidacySituationType() {
        return CandidacySituationType.REGISTERED;
    }

    @Override
    public boolean canExecuteOperationAutomatically() {
        return false;
    }

    @Override
    public IState nextState() {
        return null;
    }

    @Override
    public IState nextState(final StateBean bean) {
        return null;
    }

}
