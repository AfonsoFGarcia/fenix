package net.sourceforge.fenixedu.domain.candidacyProcess.degreeChange;

import net.sourceforge.fenixedu.domain.Degree;
import net.sourceforge.fenixedu.domain.DegreeCurricularPlan;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.StudentCurricularPlan;
import net.sourceforge.fenixedu.domain.accounting.events.candidacy.DegreeChangeIndividualCandidacyEvent;
import net.sourceforge.fenixedu.domain.candidacy.Ingression;
import net.sourceforge.fenixedu.domain.candidacyProcess.CandidacyPrecedentDegreeInformationBean;
import net.sourceforge.fenixedu.domain.candidacyProcess.ExternalPrecedentDegreeInformation;
import net.sourceforge.fenixedu.domain.candidacyProcess.IndividualCandidacyState;
import net.sourceforge.fenixedu.domain.candidacyProcess.InstitutionPrecedentDegreeInformation;
import net.sourceforge.fenixedu.domain.degreeStructure.CycleType;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.student.Registration;
import net.sourceforge.fenixedu.domain.student.registrationStates.RegistrationState;
import net.sourceforge.fenixedu.domain.student.registrationStates.RegistrationStateType;
import net.sourceforge.fenixedu.injectionCode.AccessControl;

import org.joda.time.DateTime;
import org.joda.time.LocalDate;

public class DegreeChangeIndividualCandidacy extends DegreeChangeIndividualCandidacy_Base {

    private DegreeChangeIndividualCandidacy() {
	super();
    }

    DegreeChangeIndividualCandidacy(final DegreeChangeIndividualCandidacyProcess process,
	    final DegreeChangeIndividualCandidacyProcessBean bean) {
	this();

	final Person person = bean.getOrCreatePersonFromBean();
	checkParameters(person, process, bean.getCandidacyDate(), bean.getSelectedDegree(), bean.getPrecedentDegreeInformation());

	init(person, process, bean.getCandidacyDate());
	setSelectedDegree(bean.getSelectedDegree());

	createPrecedentDegreeInformation(bean);
	createDebt(person);
    }

    private void checkParameters(final Person person, final DegreeChangeIndividualCandidacyProcess process,
	    final LocalDate candidacyDate, final Degree selectedDegree,
	    final CandidacyPrecedentDegreeInformationBean precedentDegreeInformation) {

	checkParameters(person, process, candidacyDate);

	if (selectedDegree == null) {
	    throw new DomainException("error.DegreeChangeIndividualCandidacy.invalid.degree");
	}

	if (personHasDegree(person, selectedDegree)) {
	    throw new DomainException("error.DegreeChangeIndividualCandidacy.existing.degree", selectedDegree.getNameFor(
		    getCandidacyExecutionInterval()).getContent());
	}

	if (precedentDegreeInformation == null) {
	    throw new DomainException("error.DegreeChangeIndividualCandidacy.invalid.precedentDegreeInformation");
	}
    }

    @Override
    protected void createInstitutionPrecedentDegreeInformation(final StudentCurricularPlan studentCurricularPlan) {
	final Registration registration = studentCurricularPlan.getRegistration();
	if (registration.isConcluded() || registration.isRegistrationConclusionProcessed()) {
	    throw new DomainException("error.DegreeChangeIndividualCandidacy.studentCurricularPlan.cannot.be.concluded");
	}
	super.createInstitutionPrecedentDegreeInformation(studentCurricularPlan);
    }

    @Override
    protected ExternalPrecedentDegreeInformation createExternalPrecedentDegreeInformation(
	    final CandidacyPrecedentDegreeInformationBean bean) {
	final ExternalPrecedentDegreeInformation information = super.createExternalPrecedentDegreeInformation(bean);
	information.init(bean.getNumberOfApprovedCurricularCourses(), bean.getGradeSum(), bean.getApprovedEcts(), bean
		.getEnroledEcts());
	return information;
    }

    private void createDebt(final Person person) {
	new DegreeChangeIndividualCandidacyEvent(this, person);
    }

    @Override
    public DegreeChangeIndividualCandidacyProcess getCandidacyProcess() {
	return (DegreeChangeIndividualCandidacyProcess) super.getCandidacyProcess();
    }

    @Override
    protected ExecutionYear getCandidacyExecutionInterval() {
	return (ExecutionYear) super.getCandidacyExecutionInterval();
    }

    void editCandidacyInformation(final DegreeChangeIndividualCandidacyProcessBean bean) {
	checkParameters(bean.getCandidacyDate(), bean.getSelectedDegree(), bean.getPrecedentDegreeInformation());

	setCandidacyDate(bean.getCandidacyDate());
	setSelectedDegree(bean.getSelectedDegree());

	if (getPrecedentDegreeInformation().isExternal()) {
	    getPrecedentDegreeInformation().edit(bean.getPrecedentDegreeInformation());
	    getPrecedentDegreeInformation().editCurricularCoursesInformation(bean.getPrecedentDegreeInformation());
	}
    }

    void editCandidacyCurricularCoursesInformation(final DegreeChangeIndividualCandidacyProcessBean bean) {
	if (getPrecedentDegreeInformation().isExternal()) {
	    getPrecedentDegreeInformation().editCurricularCoursesInformation(bean.getPrecedentDegreeInformation());
	}
    }

    private void checkParameters(final LocalDate candidacyDate, final Degree selectedDegree,
	    CandidacyPrecedentDegreeInformationBean precedentDegreeInformation) {

	checkParameters(getPerson(), getCandidacyProcess(), candidacyDate);

	if (selectedDegree == null) {
	    throw new DomainException("error.DegreeChangeIndividualCandidacy.invalid.degree");
	}

	if (personHasDegree(getPerson(), selectedDegree)) {
	    throw new DomainException("error.DegreeChangeIndividualCandidacy.existing.degree", selectedDegree.getNameFor(
		    getCandidacyExecutionInterval()).getContent());
	}

	if (precedentDegreeInformation == null) {
	    throw new DomainException("error.DegreeChangeIndividualCandidacy.invalid.precedentDegreeInformation");
	}
    }

    void editCandidacyResult(DegreeChangeIndividualCandidacyResultBean bean) {

	checkParameters(bean);

	setAffinity(bean.getAffinity());
	setDegreeNature(bean.getDegreeNature());
	setApprovedEctsRate(bean.getApprovedEctsRate());
	setGradeRate(bean.getGradeRate());
	setSeriesCandidacyGrade(bean.getSeriesCandidacyGrade());

	if (isCandidacyResultStateValid(bean.getState())) {
	    setState(bean.getState());
	}
    }

    private void checkParameters(final DegreeChangeIndividualCandidacyResultBean bean) {
	if (isAccepted() && bean.getState() != IndividualCandidacyState.ACCEPTED && hasRegistration()) {
	    throw new DomainException("error.DegreeChangeIndividualCandidacy.cannot.change.state.from.accepted.candidacies");
	}
    }

    @Override
    public Registration createRegistration(final DegreeCurricularPlan degreeCurricularPlan, final CycleType cycleType,
	    final Ingression ingression) {

	if (hasRegistration()) {
	    throw new DomainException("error.IndividualCandidacy.person.with.registration", degreeCurricularPlan
		    .getPresentationName());
	}

	if (hasRegistration(degreeCurricularPlan)) {
	    final Registration registration = getStudent().getRegistrationFor(degreeCurricularPlan);
	    setRegistration(registration);

	    if (!registration.isActive()) {
		RegistrationState.createState(registration, AccessControl.getPerson(), new DateTime(),
			RegistrationStateType.REGISTERED);
	    }
	    
	    createInternalAbandonStateInPreviousRegistration();
	    
	    return registration;
	}

	return createRegistration(getPerson(), degreeCurricularPlan, cycleType, ingression);
    }

    private boolean hasRegistration(DegreeCurricularPlan degreeCurricularPlan) {
	return getPerson().hasStudent() && getPerson().getStudent().hasRegistrationFor(degreeCurricularPlan);
    }

    @Override
    protected Registration createRegistration(Person person, DegreeCurricularPlan degreeCurricularPlan, CycleType cycleType,
	    Ingression ingression) {
	final Registration registration = super.createRegistration(person, degreeCurricularPlan, cycleType, ingression);
	registration.setRegistrationYear(getCandidacyExecutionInterval());
	createInternalAbandonStateInPreviousRegistration();
	return registration;
    }

    private void createInternalAbandonStateInPreviousRegistration() {
	if (getPrecedentDegreeInformation().isInternal()) {
	    final InstitutionPrecedentDegreeInformation information = (InstitutionPrecedentDegreeInformation) getPrecedentDegreeInformation();
	    if (!information.getRegistration().isInternalAbandon()) {
		RegistrationState.createState(information.getRegistration(), AccessControl.getPerson(), new DateTime(),
			RegistrationStateType.INTERNAL_ABANDON);
	    }
	}
    }
}
