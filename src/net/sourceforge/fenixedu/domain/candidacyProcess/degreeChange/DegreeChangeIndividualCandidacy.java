package net.sourceforge.fenixedu.domain.candidacyProcess.degreeChange;

import java.math.BigDecimal;
import java.util.Formatter;
import java.util.ResourceBundle;

import net.sourceforge.fenixedu.domain.Degree;
import net.sourceforge.fenixedu.domain.DegreeCurricularPlan;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.StudentCurricularPlan;
import net.sourceforge.fenixedu.domain.accounting.events.candidacy.DegreeChangeIndividualCandidacyEvent;
import net.sourceforge.fenixedu.domain.candidacy.Ingression;
import net.sourceforge.fenixedu.domain.candidacyProcess.CandidacyPrecedentDegreeInformation;
import net.sourceforge.fenixedu.domain.candidacyProcess.CandidacyPrecedentDegreeInformationBean;
import net.sourceforge.fenixedu.domain.candidacyProcess.ExternalPrecedentDegreeInformation;
import net.sourceforge.fenixedu.domain.candidacyProcess.IndividualCandidacyProcess;
import net.sourceforge.fenixedu.domain.candidacyProcess.IndividualCandidacyProcessBean;
import net.sourceforge.fenixedu.domain.candidacyProcess.IndividualCandidacyState;
import net.sourceforge.fenixedu.domain.candidacyProcess.InstitutionPrecedentDegreeInformation;
import net.sourceforge.fenixedu.domain.degreeStructure.CycleType;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.student.Registration;
import net.sourceforge.fenixedu.domain.student.registrationStates.RegistrationState.RegistrationStateCreator;
import net.sourceforge.fenixedu.domain.student.registrationStates.RegistrationStateType;
import net.sourceforge.fenixedu.injectionCode.AccessControl;

import org.joda.time.DateTime;
import org.joda.time.LocalDate;

import pt.utl.ist.fenix.tools.util.i18n.Language;

public class DegreeChangeIndividualCandidacy extends DegreeChangeIndividualCandidacy_Base {

    private DegreeChangeIndividualCandidacy() {
	super();
    }

    DegreeChangeIndividualCandidacy(final DegreeChangeIndividualCandidacyProcess process,
	    final DegreeChangeIndividualCandidacyProcessBean bean) {
	this();

	Person person = init(bean, process);
	setSelectedDegree(bean.getSelectedDegree());

	createFormationEntries(bean.getFormationConcludedBeanList(), bean.getFormationNonConcludedBeanList());

	createPrecedentDegreeInformation(bean);

	/*
	 * 06/04/2009 - The candidacy may not be associated with a person. In this case we will not create an Event
	 */
	if (bean.getInternalPersonCandidacy()) {
	    createDebt(person);
	}
    }

    @Override
    protected void checkParameters(Person person, IndividualCandidacyProcess process, IndividualCandidacyProcessBean bean) {
	DegreeChangeIndividualCandidacyProcess degreeChangeIndividualCandidacyProcess = (DegreeChangeIndividualCandidacyProcess) process;
	DegreeChangeIndividualCandidacyProcessBean degreeChangeProcessBean = (DegreeChangeIndividualCandidacyProcessBean) bean;
	LocalDate candidacyDate = bean.getCandidacyDate();
	Degree selectedDegree = degreeChangeProcessBean.getSelectedDegree();
	CandidacyPrecedentDegreeInformationBean precedentDegreeInformation = degreeChangeProcessBean
		.getPrecedentDegreeInformation();

	checkParameters(person, degreeChangeIndividualCandidacyProcess, candidacyDate, selectedDegree, precedentDegreeInformation);
    }

    private void checkParameters(final Person person, final DegreeChangeIndividualCandidacyProcess process,
	    final LocalDate candidacyDate, final Degree selectedDegree,
	    final CandidacyPrecedentDegreeInformationBean precedentDegreeInformation) {

	checkParameters(person, process, candidacyDate);

	if (selectedDegree == null) {
	    throw new DomainException("error.DegreeChangeIndividualCandidacy.invalid.degree");
	}

	/*
	 * 31/03/2009 - The candidacy may be submited externally hence may not be associated to a person
	 * 
	 * 
	 * if (personHasDegree(person, selectedDegree)) { throw new DomainException
	 * ("error.DegreeChangeIndividualCandidacy.existing.degree", selectedDegree.getNameFor(
	 * getCandidacyExecutionInterval()).getContent()); }
	 */

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
	information.init(bean.getNumberOfEnroledCurricularCourses(), bean.getNumberOfApprovedCurricularCourses(), bean
		.getGradeSum(), bean.getApprovedEcts(), bean.getEnroledEcts());
	return information;
    }

    @Override
    protected void createDebt(final Person person) {
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

	checkParameters(getPersonalDetails().getPerson(), getCandidacyProcess(), candidacyDate);

	if (selectedDegree == null) {
	    throw new DomainException("error.DegreeChangeIndividualCandidacy.invalid.degree");
	}

	if (personHasDegree(getPersonalDetails().getPerson(), selectedDegree)) {
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
	} else if (bean.getState() == null) {
	    setState(IndividualCandidacyState.STAND_BY);
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
	return getPersonalDetails().hasStudent() && getPersonalDetails().getStudent().hasRegistrationFor(degreeCurricularPlan);
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
		    throw new DomainException("error.DegreeChangeIndividualCandidacy.cannot.create.abandon.state.due.enrolments",
			    information.getRegistration().getDegreeCurricularPlanName(), executionYear.getQualifiedName());
		}

		RegistrationStateCreator.createState(information.getRegistration(), AccessControl.getPerson(), now,
			RegistrationStateType.INTERNAL_ABANDON);
	    }
	}
    }

    void editSelectedDegree(final Degree selectedDegree) {
	setSelectedDegree(selectedDegree);
    }

    @Override
    public void exportValues(StringBuilder result) {
	super.exportValues(result);

	final ResourceBundle bundle = ResourceBundle.getBundle("resources.AcademicAdminOffice", Language.getLocale());
	final ResourceBundle candidateBundle = ResourceBundle.getBundle("resources.CandidateResources", Language.getLocale());
	final ResourceBundle applicationBundle = ResourceBundle.getBundle("resources.ApplicationResources", Language.getLocale());

	Formatter formatter = new Formatter(result);

	formatter.format("%s: %s\n", candidateBundle.getString("label.process.id"), getCandidacyProcess().getProcessCode());
	CandidacyPrecedentDegreeInformation precedentDegreeInformation = getCandidacyProcess()
		.getCandidacyPrecedentDegreeInformation();
	formatter.format("%s: %s\n", bundle.getString("label.SecondCycleIndividualCandidacy.previous.degree"),
		precedentDegreeInformation.getDegreeDesignation());
	formatter.format("%s: %s\n", bundle.getString("label.SecondCycleIndividualCandidacy.institution"),
		precedentDegreeInformation.getInstitution().getName());
	formatter.format("%s: %s\n", applicationBundle.getString("label.candidacy.numberOfEnroledCurricularCourses"),
		precedentDegreeInformation.getNumberOfEnroledCurricularCourses());
	formatter.format("%s: %s\n", applicationBundle.getString("label.candidacy.numberOfApprovedCurricularCourses"),
		precedentDegreeInformation.getNumberOfApprovedCurricularCourses());
	formatter.format("%s: %s\n", applicationBundle.getString("label.candidacy.gradeSum"), precedentDegreeInformation
		.getGradeSum());
	formatter.format("%s: %s\n", applicationBundle.getString("label.candidacy.approvedEcts"), precedentDegreeInformation
		.getApprovedEcts());
	formatter.format("%s: %s\n", applicationBundle.getString("label.candidacy.enroledEcts"), precedentDegreeInformation
		.getEnroledEcts());

	formatter.format("\n");
	formatter.format("%s: %f\n", bundle.getString("label.SecondCycleIndividualCandidacy.affinity"),
		getAffinity() != null ? getAffinity() : BigDecimal.ZERO);
	formatter.format("%s: %d\n", bundle.getString("label.SecondCycleIndividualCandidacy.degreeNature"),
		getDegreeNature() != null ? getDegreeNature() : 0);
	formatter.format("%s: %f\n", bundle.getString("label.DegreeChangeIndividualCandidacy.approvedEctsRate"),
		getApprovedEctsRate() != null ? getApprovedEctsRate() : BigDecimal.ZERO);
	formatter.format("%s: %f\n", bundle.getString("label.DegreeChangeIndividualCandidacy.gradeRate"),
		getGradeRate() != null ? getGradeRate() : BigDecimal.ZERO);
	formatter.format("%s: %f\n", bundle.getString("label.SecondCycleIndividualCandidacy.seriesCandidacyGrade"),
		getSeriesCandidacyGrade() != null ? getSeriesCandidacyGrade() : BigDecimal.ZERO);

    }

    @Override
    public String getDescription() {
	return getCandidacyProcess().getDisplayName() + (hasSelectedDegree() ? ": " + getSelectedDegree().getNameI18N() : "");
    }

    public boolean isDegreeChange() {
	return true;
    }

}
