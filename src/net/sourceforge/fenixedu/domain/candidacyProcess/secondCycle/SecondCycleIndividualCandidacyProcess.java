package net.sourceforge.fenixedu.domain.candidacyProcess.secondCycle;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.caseHandling.StartActivity;
import net.sourceforge.fenixedu.domain.Degree;
import net.sourceforge.fenixedu.domain.DegreeCurricularPlan;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.candidacy.Ingression;
import net.sourceforge.fenixedu.domain.candidacyProcess.CandidacyPrecedentDegreeInformation;
import net.sourceforge.fenixedu.domain.caseHandling.Activity;
import net.sourceforge.fenixedu.domain.caseHandling.PreConditionNotValidException;
import net.sourceforge.fenixedu.domain.degreeStructure.CycleType;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.person.RoleType;
import net.sourceforge.fenixedu.presentationTier.Action.Seminaries.CancelCandidacy;

public class SecondCycleIndividualCandidacyProcess extends SecondCycleIndividualCandidacyProcess_Base {

    static private List<Activity> activities = new ArrayList<Activity>();
    static {
	activities.add(new CandidacyPayment());
	activities.add(new EditCandidacyPersonalInformation());
	activities.add(new EditCommonCandidacyInformation());
	activities.add(new EditCandidacyInformation());
	activities.add(new IntroduceCandidacyResult());
	activities.add(new CancelCandidacy());
	activities.add(new CreateRegistration());
    }

    private SecondCycleIndividualCandidacyProcess() {
	super();
    }

    private SecondCycleIndividualCandidacyProcess(final SecondCycleIndividualCandidacyProcessBean bean) {
	this();
	checkParameters(bean.getCandidacyProcess());
	setCandidacyProcess(bean.getCandidacyProcess());
	new SecondCycleIndividualCandidacy(this, bean);
	getCandidacy().editCandidacyInformation(bean.getCandidacyInformationBean());
    }

    private void checkParameters(final SecondCycleCandidacyProcess process) {
	if (process == null || !process.hasCandidacyPeriod()) {
	    throw new DomainException("error.SecondCycleIndividualCandidacyProcess.invalid.candidacy.process");
	}
    }

    @Override
    public boolean canExecuteActivity(IUserView userView) {
	return isDegreeAdministrativeOfficeEmployee(userView) || userView.hasRoleType(RoleType.SCIENTIFIC_COUNCIL)
		|| userView.hasRoleType(RoleType.COORDINATOR);
    }

    @Override
    public List<Activity> getActivities() {
	return activities;
    }

    @Override
    public SecondCycleIndividualCandidacy getCandidacy() {
	return (SecondCycleIndividualCandidacy) super.getCandidacy();
    }

    private SecondCycleIndividualCandidacyProcess editCandidacyInformation(final SecondCycleIndividualCandidacyProcessBean bean) {
	getCandidacy().editCandidacyInformation(bean.getCandidacyDate(), bean.getSelectedDegree(),
		bean.getPrecedentDegreeInformation(), bean.getProfessionalStatus(), bean.getOtherEducation());
	return this;
    }

    public Degree getCandidacySelectedDegree() {
	return getCandidacy().getSelectedDegree();
    }

    public boolean hasCandidacyForSelectedDegree(final Degree degree) {
	return getCandidacySelectedDegree() == degree;
    }

    public String getCandidacyProfessionalStatus() {
	return getCandidacy().getProfessionalStatus();
    }

    public String getCandidacyOtherEducation() {
	return getCandidacy().getOtherEducation();
    }

    public CandidacyPrecedentDegreeInformation getCandidacyPrecedentDegreeInformation() {
	return getCandidacy().getPrecedentDegreeInformation();
    }

    public Integer getCandidacyProfessionalExperience() {
	return getCandidacy().getProfessionalExperience();
    }

    public BigDecimal getCandidacyAffinity() {
	return getCandidacy().getAffinity();
    }

    public Integer getCandidacyDegreeNature() {
	return getCandidacy().getDegreeNature();
    }

    public BigDecimal getCandidacyGrade() {
	return getCandidacy().getCandidacyGrade();
    }

    public String getCandidacyInterviewGrade() {
	return getCandidacy().getInterviewGrade();
    }

    public BigDecimal getCandidacySeriesGrade() {
	return getCandidacy().getSeriesCandidacyGrade();
    }

    public String getCandidacyNotes() {
	return getCandidacy().getNotes();
    }

    @Override
    public ExecutionYear getCandidacyExecutionInterval() {
	return (ExecutionYear) super.getCandidacyExecutionInterval();
    }

    static private boolean isDegreeAdministrativeOfficeEmployee(IUserView userView) {
	return userView.hasRoleType(RoleType.ACADEMIC_ADMINISTRATIVE_OFFICE)
		&& userView.getPerson().getEmployeeAdministrativeOffice().isDegree();
    }

    @StartActivity
    static public class IndividualCandidacyInformation extends Activity<SecondCycleIndividualCandidacyProcess> {

	@Override
	public void checkPreConditions(SecondCycleIndividualCandidacyProcess process, IUserView userView) {
	    if (!isDegreeAdministrativeOfficeEmployee(userView)) {
		throw new PreConditionNotValidException();
	    }
	}

	@Override
	protected SecondCycleIndividualCandidacyProcess executeActivity(SecondCycleIndividualCandidacyProcess dummy,
		IUserView userView, Object object) {
	    return new SecondCycleIndividualCandidacyProcess((SecondCycleIndividualCandidacyProcessBean) object);
	}
    }

    static private class CandidacyPayment extends Activity<SecondCycleIndividualCandidacyProcess> {

	@Override
	public void checkPreConditions(SecondCycleIndividualCandidacyProcess process, IUserView userView) {
	    if (!isDegreeAdministrativeOfficeEmployee(userView)) {
		throw new PreConditionNotValidException();
	    }
	    if (process.isCandidacyCancelled()) {
		throw new PreConditionNotValidException();
	    }
	}

	@Override
	protected SecondCycleIndividualCandidacyProcess executeActivity(SecondCycleIndividualCandidacyProcess process,
		IUserView userView, Object object) {
	    return process; // nothing to be done, for now payment is being
	    // done by existing interface
	}
    }

    static private class EditCandidacyPersonalInformation extends Activity<SecondCycleIndividualCandidacyProcess> {

	@Override
	public void checkPreConditions(SecondCycleIndividualCandidacyProcess process, IUserView userView) {
	    if (!isDegreeAdministrativeOfficeEmployee(userView)) {
		throw new PreConditionNotValidException();
	    }
	    if (process.isCandidacyCancelled()) {
		throw new PreConditionNotValidException();
	    }
	}

	@Override
	protected SecondCycleIndividualCandidacyProcess executeActivity(SecondCycleIndividualCandidacyProcess process,
		IUserView userView, Object object) {
	    final SecondCycleIndividualCandidacyProcessBean bean = (SecondCycleIndividualCandidacyProcessBean) object;
	    process.editPersonalCandidacyInformation(bean.getPersonBean());
	    return process;
	}
    }

    static private class EditCommonCandidacyInformation extends Activity<SecondCycleIndividualCandidacyProcess> {

	@Override
	public void checkPreConditions(SecondCycleIndividualCandidacyProcess process, IUserView userView) {
	    if (!isDegreeAdministrativeOfficeEmployee(userView)) {
		throw new PreConditionNotValidException();
	    }
	    if (process.isCandidacyCancelled()) {
		throw new PreConditionNotValidException();
	    }
	}

	@Override
	protected SecondCycleIndividualCandidacyProcess executeActivity(SecondCycleIndividualCandidacyProcess process,
		IUserView userView, Object object) {
	    final SecondCycleIndividualCandidacyProcessBean bean = (SecondCycleIndividualCandidacyProcessBean) object;
	    process.editCommonCandidacyInformation(bean.getCandidacyInformationBean());
	    return process;
	}
    }

    static private class EditCandidacyInformation extends Activity<SecondCycleIndividualCandidacyProcess> {

	@Override
	public void checkPreConditions(SecondCycleIndividualCandidacyProcess process, IUserView userView) {
	    if (!isDegreeAdministrativeOfficeEmployee(userView)) {
		throw new PreConditionNotValidException();
	    }
	    if (process.isCandidacyCancelled() || process.isCandidacyAccepted() || process.hasRegistrationForCandidacy()) {
		throw new PreConditionNotValidException();
	    }
	}

	@Override
	protected SecondCycleIndividualCandidacyProcess executeActivity(SecondCycleIndividualCandidacyProcess process,
		IUserView userView, Object object) {
	    return process.editCandidacyInformation((SecondCycleIndividualCandidacyProcessBean) object);
	}
    }

    static private class IntroduceCandidacyResult extends Activity<SecondCycleIndividualCandidacyProcess> {

	@Override
	public void checkPreConditions(SecondCycleIndividualCandidacyProcess process, IUserView userView) {
	    if (!isDegreeAdministrativeOfficeEmployee(userView)) {
		throw new PreConditionNotValidException();
	    }

	    if (process.isCandidacyCancelled()) {
		throw new PreConditionNotValidException();
	    }

	    if (!process.hasAnyPaymentForCandidacy()) {
		throw new PreConditionNotValidException();
	    }

	    if (!process.isSentToCoordinator() && !process.isSentToScientificCouncil()) {
		throw new PreConditionNotValidException();
	    }
	}

	@Override
	protected SecondCycleIndividualCandidacyProcess executeActivity(SecondCycleIndividualCandidacyProcess process,
		IUserView userView, Object object) {
	    process.getCandidacy().editCandidacyResult((SecondCycleIndividualCandidacyResultBean) object);
	    return process;
	}
    }

    static private class CancelCandidacy extends Activity<SecondCycleIndividualCandidacyProcess> {

	@Override
	public void checkPreConditions(SecondCycleIndividualCandidacyProcess process, IUserView userView) {
	    if (!isDegreeAdministrativeOfficeEmployee(userView)) {
		throw new PreConditionNotValidException();
	    }
	    if (process.isCandidacyCancelled() || !process.isCandidacyInStandBy() || process.hasAnyPaymentForCandidacy()) {
		throw new PreConditionNotValidException();
	    }
	}

	@Override
	protected SecondCycleIndividualCandidacyProcess executeActivity(SecondCycleIndividualCandidacyProcess process,
		IUserView userView, Object object) {
	    process.cancelCandidacy(userView.getPerson());
	    return process;
	}
    }

    static private class CreateRegistration extends Activity<SecondCycleIndividualCandidacyProcess> {

	@Override
	public void checkPreConditions(SecondCycleIndividualCandidacyProcess process, IUserView userView) {
	    if (!isDegreeAdministrativeOfficeEmployee(userView)) {
		throw new PreConditionNotValidException();
	    }

	    if (!process.isCandidacyAccepted()) {
		throw new PreConditionNotValidException();
	    }

	    if (process.hasRegistrationForCandidacy()) {
		throw new PreConditionNotValidException();
	    }

	    // TODO: check if can create registration when first cycle is
	    // not concluded
	}

	@Override
	protected SecondCycleIndividualCandidacyProcess executeActivity(SecondCycleIndividualCandidacyProcess process,
		IUserView userView, Object object) {
	    createRegistration(process);
	    return process;
	}

	private void createRegistration(final SecondCycleIndividualCandidacyProcess candidacyProcess) {
	    candidacyProcess.getCandidacy().createRegistration(getDegreeCurricularPlan(candidacyProcess), CycleType.SECOND_CYCLE,
		    Ingression.CIA2C);
	}

	private DegreeCurricularPlan getDegreeCurricularPlan(final SecondCycleIndividualCandidacyProcess candidacyProcess) {
	    return candidacyProcess.getCandidacySelectedDegree().getLastActiveDegreeCurricularPlan();
	}
    }
}
