package net.sourceforge.fenixedu.domain.candidacyProcess.degreeTransfer;

import net.sourceforge.fenixedu.domain.Degree;
import net.sourceforge.fenixedu.domain.DegreeCurricularPlan;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.StudentCurricularPlan;
import net.sourceforge.fenixedu.domain.accounting.events.candidacy.DegreeTransferIndividualCandidacyEvent;
import net.sourceforge.fenixedu.domain.candidacy.Ingression;
import net.sourceforge.fenixedu.domain.candidacyProcess.CandidacyPrecedentDegreeInformationBean;
import net.sourceforge.fenixedu.domain.candidacyProcess.ExternalPrecedentDegreeInformation;
import net.sourceforge.fenixedu.domain.candidacyProcess.IndividualCandidacyProcess;
import net.sourceforge.fenixedu.domain.candidacyProcess.IndividualCandidacyProcessBean;
import net.sourceforge.fenixedu.domain.candidacyProcess.IndividualCandidacyState;
import net.sourceforge.fenixedu.domain.candidacyProcess.InstitutionPrecedentDegreeInformation;
import net.sourceforge.fenixedu.domain.degreeStructure.CycleType;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.student.Registration;
import net.sourceforge.fenixedu.domain.student.registrationStates.RegistrationStateType;
import net.sourceforge.fenixedu.domain.student.registrationStates.RegistrationState.RegistrationStateCreator;
import net.sourceforge.fenixedu.injectionCode.AccessControl;

import org.joda.time.DateTime;
import org.joda.time.LocalDate;

public class DegreeTransferIndividualCandidacy extends DegreeTransferIndividualCandidacy_Base {

    private DegreeTransferIndividualCandidacy() {
	super();
    }

    DegreeTransferIndividualCandidacy(final DegreeTransferIndividualCandidacyProcess process,
	    final DegreeTransferIndividualCandidacyProcessBean bean) {
	this();

	Person person = init(bean, process);
	setSelectedDegree(bean.getSelectedDegree());

	createPrecedentDegreeInformation(bean);

	/*
	 * 06/04/2009 - The candidacy may not be associated with a person. In
	 * this case we will not create an Event
	 */
	if (bean.getInternalPersonCandidacy()) {
	    createDebt(person);
	}
    }

    @Override
    protected void checkParameters(final Person person, final IndividualCandidacyProcess process,
	    final IndividualCandidacyProcessBean bean) {
	DegreeTransferIndividualCandidacyProcessBean transferProcessBean = (DegreeTransferIndividualCandidacyProcessBean) bean;
	DegreeTransferIndividualCandidacyProcess transferProcess = (DegreeTransferIndividualCandidacyProcess) process;
	LocalDate candidacyDate = bean.getCandidacyDate();
	Degree selectedDegree = transferProcessBean.getSelectedDegree();
	CandidacyPrecedentDegreeInformationBean precedentDegreeInformation = transferProcessBean.getPrecedentDegreeInformation();

	checkParameters(person, transferProcess, candidacyDate, selectedDegree, precedentDegreeInformation);
    }

    private void checkParameters(final Person person, final DegreeTransferIndividualCandidacyProcess process,
	    final LocalDate candidacyDate, final Degree selectedDegree,
	    final CandidacyPrecedentDegreeInformationBean precedentDegreeInformation) {

	checkParameters(person, process, candidacyDate);

	if (selectedDegree == null) {
	    throw new DomainException("error.DegreeTransferIndividualCandidacy.invalid.degree");
	}

	/*
	 * 31/03/2009 - The candidacy may be submited externally hence may not
	 * be associated to a person
	 * 
	 * 
	 * if (personHasDegree(person, selectedDegree)) { throw new
	 * DomainException
	 * ("error.DegreeTransferIndividualCandidacy.existing.degree",
	 * selectedDegree.getNameFor(
	 * getCandidacyExecutionInterval()).getContent()); }
	 */

	if (precedentDegreeInformation == null) {
	    throw new DomainException("error.DegreeTransferIndividualCandidacy.invalid.precedentDegreeInformation");
	}
    }

    @Override
    protected void createInstitutionPrecedentDegreeInformation(final StudentCurricularPlan studentCurricularPlan) {
	final Registration registration = studentCurricularPlan.getRegistration();
	if (registration.isConcluded() || registration.isRegistrationConclusionProcessed()) {
	    throw new DomainException("error.DegreeTransferIndividualCandidacy.studentCurricularPlan.cannot.be.concluded");
	}
	super.createInstitutionPrecedentDegreeInformation(studentCurricularPlan);
    }

    @Override
    protected ExternalPrecedentDegreeInformation createExternalPrecedentDegreeInformation(
	    final CandidacyPrecedentDegreeInformationBean bean) {
	final ExternalPrecedentDegreeInformation information = super.createExternalPrecedentDegreeInformation(bean);
	information.init(bean.getNumberOfEnroledCurricularCourses(), bean.getNumberOfApprovedCurricularCourses(), bean
		.getGradeSum(), bean.getApprovedEcts(), bean.getEnroledEcts());
	return information;
    }

    @Override
    protected void createDebt(final Person person) {
	new DegreeTransferIndividualCandidacyEvent(this, person);
    }

    @Override
    public DegreeTransferIndividualCandidacyProcess getCandidacyProcess() {
	return (DegreeTransferIndividualCandidacyProcess) super.getCandidacyProcess();
    }

    @Override
    protected ExecutionYear getCandidacyExecutionInterval() {
	return (ExecutionYear) super.getCandidacyExecutionInterval();
    }

    @Override
    public Registration createRegistration(final DegreeCurricularPlan degreeCurricularPlan, final CycleType cycleType,
	    final Ingression ingression) {

	if (hasRegistration()) {
	    throw new DomainException("error.IndividualCandidacy.person.with.registration", degreeCurricularPlan
		    .getPresentationName());
	}

	if (hasRegistration(degreeCurricularPlan)) {
	    final Registration registration = getMostRecentRegistration(degreeCurricularPlan);
	    setRegistration(registration);

	    if (!registration.isActive()) {
		RegistrationStateCreator.createState(registration, AccessControl.getPerson(), new DateTime(),
			RegistrationStateType.REGISTERED);
	    }

	    createInternalAbandonStateInPreviousRegistration();

	    return registration;
	}

	getPersonalDetails().ensurePersonInternalization();
	return createRegistration(getPersonalDetails().getPerson(), degreeCurricularPlan, cycleType, ingression);
    }

    private boolean hasRegistration(DegreeCurricularPlan degreeCurricularPlan) {
	return getPersonalDetails().hasPerson() && getPersonalDetails().getPerson().hasStudent()
		&& getPersonalDetails().getPerson().getStudent().hasRegistrationFor(degreeCurricularPlan);
    }

    private Registration getMostRecentRegistration(final DegreeCurricularPlan degreeCurricularPlan) {
	return getStudent().getMostRecentRegistration(degreeCurricularPlan);
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

		final DateTime now = new DateTime();
		final ExecutionYear executionYear = ExecutionYear.readByDateTime(now);

		if (information.getRegistration().hasAnyEnrolmentsIn(executionYear)) {
		    throw new DomainException("error.DegreeTransferIndividualCandidacy.cannot.create.abandon.state.due.enrolments",
			    information.getRegistration().getDegreeCurricularPlanName(), executionYear.getQualifiedName());
		}

		RegistrationStateCreator.createState(information.getRegistration(), AccessControl.getPerson(), now,
			RegistrationStateType.INTERNAL_ABANDON);
	    }
	    
	}
    }

    void editCandidacyInformation(final DegreeTransferIndividualCandidacyProcessBean bean) {
	checkParameters(bean.getCandidacyDate(), bean.getSelectedDegree(), bean.getPrecedentDegreeInformation());

	setCandidacyDate(bean.getCandidacyDate());
	setSelectedDegree(bean.getSelectedDegree());

	if (getPrecedentDegreeInformation().isExternal()) {
	    getPrecedentDegreeInformation().edit(bean.getPrecedentDegreeInformation());
	    getPrecedentDegreeInformation().editCurricularCoursesInformation(bean.getPrecedentDegreeInformation());
	}
    }

    private void checkParameters(final LocalDate candidacyDate, final Degree selectedDegree,
	    CandidacyPrecedentDegreeInformationBean precedentDegreeInformation) {

	checkParameters(getPersonalDetails().getPerson(), getCandidacyProcess(), candidacyDate);

	if (selectedDegree == null) {
	    throw new DomainException("error.DegreeTransferIndividualCandidacy.invalid.degree");
	}

	if (personHasDegree(getPersonalDetails().getPerson(), selectedDegree)) {
	    throw new DomainException("error.DegreeTransferIndividualCandidacy.existing.degree", selectedDegree.getNameFor(
		    getCandidacyExecutionInterval()).getContent());
	}

	if (precedentDegreeInformation == null) {
	    throw new DomainException("error.DegreeTransferIndividualCandidacy.invalid.precedentDegreeInformation");
	}
    }

    public void editCandidacyCurricularCoursesInformation(final DegreeTransferIndividualCandidacyProcessBean bean) {
	if (getPrecedentDegreeInformation().isExternal()) {
	    getPrecedentDegreeInformation().editCurricularCoursesInformation(bean.getPrecedentDegreeInformation());
	}
    }

    void editCandidacyResult(final DegreeTransferIndividualCandidacyResultBean bean) {

	checkParameters(bean);

	setAffinity(bean.getAffinity());
	setDegreeNature(bean.getDegreeNature());
	setApprovedEctsRate(bean.getApprovedEctsRate());
	setGradeRate(bean.getGradeRate());
	setSeriesCandidacyGrade(bean.getSeriesCandidacyGrade());

	if (isCandidacyResultStateValid(bean.getState())) {
	    setState(bean.getState());
	} else if (bean.getState() == null) {
	    setState(IndividualCandidacyState.STAND_BY);
	}
    }

    private void checkParameters(final DegreeTransferIndividualCandidacyResultBean bean) {
	if (isAccepted() && bean.getState() != IndividualCandidacyState.ACCEPTED && hasRegistration()) {
	    throw new DomainException("error.DegreeTransferIndividualCandidacy.cannot.change.state.from.accepted.candidacies");
	}
    }

    void editSelectedDegree(final Degree selectedDegree) {
	setSelectedDegree(selectedDegree);
    }

}
