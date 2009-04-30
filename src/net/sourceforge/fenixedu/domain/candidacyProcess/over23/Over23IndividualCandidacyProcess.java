package net.sourceforge.fenixedu.domain.candidacyProcess.over23;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.caseHandling.StartActivity;
import net.sourceforge.fenixedu.domain.Degree;
import net.sourceforge.fenixedu.domain.DegreeCurricularPlan;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.candidacy.Ingression;
import net.sourceforge.fenixedu.domain.caseHandling.Activity;
import net.sourceforge.fenixedu.domain.caseHandling.PreConditionNotValidException;
import net.sourceforge.fenixedu.domain.candidacyProcess.CandidacyProcess;
import net.sourceforge.fenixedu.domain.candidacyProcess.CandidacyProcessDocumentUploadBean;
import net.sourceforge.fenixedu.domain.candidacyProcess.IndividualCandidacyProcessBean;
import net.sourceforge.fenixedu.domain.degreeStructure.CycleType;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.person.RoleType;
import net.sourceforge.fenixedu.presentationTier.Action.Seminaries.CancelCandidacy;

public class Over23IndividualCandidacyProcess extends Over23IndividualCandidacyProcess_Base {

    static private List<Activity> activities = new ArrayList<Activity>();
    static {
	activities.add(new CandidacyPayment());
	activities.add(new EditCandidacyPersonalInformation());
	activities.add(new EditCommonCandidacyInformation());
	activities.add(new EditCandidacyInformation());
	activities.add(new IntroduceCandidacyResult());
	activities.add(new CancelCandidacy());
	activities.add(new CreateRegistration());
	activities.add(new EditPublicCandidacyPersonalInformation());
	activities.add(new EditPublicCandidacyDocumentFile());
	activities.add(new EditPublicCandidacyHabilitations());
    }

    protected Over23IndividualCandidacyProcess() {
	super();
    }

    private Over23IndividualCandidacyProcess(final Over23IndividualCandidacyProcessBean bean) {
	this();

	/*
	 * 06/04/2009 - The checkParameters, IndividualCandidacy creation and
	 * candidacy information are made in the init method
	 */
	init(bean);
	
	/*
	 * 20/04/2009 - New document files specific to Over23 candidacies
	 */
	setSpecificIndividualCandidacyDocumentFiles(bean);
    }

    
    @Override
    protected void checkParameters(final CandidacyProcess process) {
	if (process == null || !process.hasCandidacyPeriod()) {
	    throw new DomainException("error.Over23IndividualCandidacyProcess.invalid.candidacy.process");
	}
    }

    @Override
    protected void createIndividualCandidacy(IndividualCandidacyProcessBean bean) {
	new Over23IndividualCandidacy(this, (Over23IndividualCandidacyProcessBean) bean);
    }

    @Override
    public Over23IndividualCandidacy getCandidacy() {
	return (Over23IndividualCandidacy) super.getCandidacy();
    }

    @Override
    public boolean canExecuteActivity(IUserView userView) {
	return isDegreeAdministrativeOfficeEmployee(userView);
    }

    @Override
    public List<Activity> getActivities() {
	return activities;
    }

    private Over23IndividualCandidacyProcess editCandidacyInformation(final Over23IndividualCandidacyProcessBean bean) {
	getCandidacy().editCandidacyInformation(bean.getCandidacyDate(), bean.getSelectedDegrees(), bean.getDisabilities(),
		bean.getEducation(), bean.getLanguages());
	return this;
    }

    public List<Degree> getSelectedDegreesSortedByOrder() {
	return getCandidacy().getSelectedDegreesSortedByOrder();
    }

    public String getDisabilities() {
	return getCandidacy().getDisabilities();
    }

    public String getEducation() {
	return getCandidacy().getEducation();
    }

    public String getLanguages() {
	return getCandidacy().getLanguages();
    }
    
    
    public String getLanguagesRead() {
	return getCandidacy().getLanguagesRead();
    }
    
    public String getLanguagesWrite() {
	return getCandidacy().getLanguagesWrite();
    }
    
    public String getLanguagesSpeak() {
	return getCandidacy().getLanguagesSpeak();
    }
    
    public Degree getAcceptedDegree() {
	return getCandidacy().getAcceptedDegree();
    }

    public boolean hasAcceptedDegree() {
	return getAcceptedDegree() != null;
    }

    @Override
    public ExecutionYear getCandidacyExecutionInterval() {
	return (ExecutionYear) super.getCandidacyExecutionInterval();
    }

    static private boolean isDegreeAdministrativeOfficeEmployee(IUserView userView) {
	return userView.hasRoleType(RoleType.ACADEMIC_ADMINISTRATIVE_OFFICE)
		&& userView.getPerson().getEmployeeAdministrativeOffice().isDegree();
    }
    
    private void setSpecificIndividualCandidacyDocumentFiles(Over23IndividualCandidacyProcessBean bean) {
	bindIndividualCandidacyDocumentFile(bean.getCurriculumVitaeDocument());
	bindIndividualCandidacyDocumentFile(bean.getHandicapProofDocument());
	
	for(CandidacyProcessDocumentUploadBean documentBean : bean.getHabilitationCertificateList()) {
	    bindIndividualCandidacyDocumentFile(documentBean);
	}
	
	for(CandidacyProcessDocumentUploadBean documentBean : bean.getReportOrWorkDocumentList()) {
	    bindIndividualCandidacyDocumentFile(documentBean);
	}
    }

    private void editCandidacyHabilitations(Over23IndividualCandidacyProcessBean bean) {
	this.getCandidacy().editFormationEntries(bean.getFormationConcludedBeanList(), bean.getFormationNonConcludedBeanList());
    }
    
    private void saveChoosedDegrees(final List<Degree> degrees) {
	this.getCandidacy().saveChoosedDegrees(degrees);	
    }
    
    @StartActivity
    static public class IndividualCandidacyInformation extends Activity<Over23IndividualCandidacyProcess> {

	@Override
	public void checkPreConditions(Over23IndividualCandidacyProcess process, IUserView userView) {
		/* 
		 * 06/04/2009
		 * The candidacy may be submited by someone who's not authenticated in the system
		 *
	     *if (!isDegreeAdministrativeOfficeEmployee(userView)) {
		 *throw new PreConditionNotValidException();
	     *}
		 */
	}

	@Override
	protected Over23IndividualCandidacyProcess executeActivity(Over23IndividualCandidacyProcess dummy, IUserView userView,
		Object object) {
	    return new Over23IndividualCandidacyProcess((Over23IndividualCandidacyProcessBean) object);
	}
    }

    static private class CandidacyPayment extends Activity<Over23IndividualCandidacyProcess> {

	@Override
	public void checkPreConditions(Over23IndividualCandidacyProcess process, IUserView userView) {
	    if (!isDegreeAdministrativeOfficeEmployee(userView)) {
		throw new PreConditionNotValidException();
	    }
	    if (process.isCandidacyCancelled()) {
		throw new PreConditionNotValidException();
	    }
	}

	@Override
	protected Over23IndividualCandidacyProcess executeActivity(Over23IndividualCandidacyProcess process, IUserView userView,
		Object object) {
	    return process; // nothing to be done, for now payment is being
	    // done by existing interface
	}
    }

    static private class EditCandidacyPersonalInformation extends Activity<Over23IndividualCandidacyProcess> {

	@Override
	public void checkPreConditions(Over23IndividualCandidacyProcess process, IUserView userView) {
	    if (!isDegreeAdministrativeOfficeEmployee(userView)) {
		throw new PreConditionNotValidException();
	    }
	    if (process.isCandidacyCancelled()) {
		throw new PreConditionNotValidException();
	    }
	}

	@Override
	protected Over23IndividualCandidacyProcess executeActivity(Over23IndividualCandidacyProcess process, IUserView userView,
		Object object) {
	    final Over23IndividualCandidacyProcessBean bean = (Over23IndividualCandidacyProcessBean) object;
	    process.editPersonalCandidacyInformation(bean.getPersonBean());
	    return process;
	}
    }

    static private class EditCommonCandidacyInformation extends Activity<Over23IndividualCandidacyProcess> {

	@Override
	public void checkPreConditions(Over23IndividualCandidacyProcess process, IUserView userView) {
	    if (!isDegreeAdministrativeOfficeEmployee(userView)) {
		throw new PreConditionNotValidException();
	    }
	    if (process.isCandidacyCancelled()) {
		throw new PreConditionNotValidException();
	    }
	}

	@Override
	protected Over23IndividualCandidacyProcess executeActivity(Over23IndividualCandidacyProcess process, IUserView userView,
		Object object) {
	    final Over23IndividualCandidacyProcessBean bean = (Over23IndividualCandidacyProcessBean) object;
	    process.editCommonCandidacyInformation(bean.getCandidacyInformationBean());
	    return process;
	}
    }

    static private class EditCandidacyInformation extends Activity<Over23IndividualCandidacyProcess> {

	@Override
	public void checkPreConditions(Over23IndividualCandidacyProcess process, IUserView userView) {
	    if (!isDegreeAdministrativeOfficeEmployee(userView)) {
		throw new PreConditionNotValidException();
	    }
	    if (!process.isInStandBy() || process.isCandidacyCancelled() || process.isCandidacyAccepted()) {
		throw new PreConditionNotValidException();
	    }
	}

	@Override
	protected Over23IndividualCandidacyProcess executeActivity(Over23IndividualCandidacyProcess process, IUserView userView,
		Object object) {
	    return process.editCandidacyInformation((Over23IndividualCandidacyProcessBean) object);
	}
    }

    static private class IntroduceCandidacyResult extends Activity<Over23IndividualCandidacyProcess> {

	@Override
	public void checkPreConditions(Over23IndividualCandidacyProcess process, IUserView userView) {
	    if (!isDegreeAdministrativeOfficeEmployee(userView)) {
		throw new PreConditionNotValidException();
	    }

	    if (process.isCandidacyCancelled()) {
		throw new PreConditionNotValidException();
	    }

	    if (!process.hasAnyPaymentForCandidacy()) {
		throw new PreConditionNotValidException();
	    }

	    if (!process.isSentToJury()) {
		throw new PreConditionNotValidException();
	    }
	}

	@Override
	protected Over23IndividualCandidacyProcess executeActivity(Over23IndividualCandidacyProcess process, IUserView userView,
		Object object) {
	    final Over23IndividualCandidacyResultBean bean = (Over23IndividualCandidacyResultBean) object;
	    process.getCandidacy().editCandidacyResult(bean.getState(), bean.getAcceptedDegree());
	    return process;
	}
    }

    static private class CancelCandidacy extends Activity<Over23IndividualCandidacyProcess> {

	@Override
	public void checkPreConditions(Over23IndividualCandidacyProcess process, IUserView userView) {
	    if (!isDegreeAdministrativeOfficeEmployee(userView)) {
		throw new PreConditionNotValidException();
	    }

	    if (process.isCandidacyCancelled() || process.hasAnyPaymentForCandidacy() || !process.isCandidacyInStandBy()) {
		throw new PreConditionNotValidException();
	    }
	}

	@Override
	protected Over23IndividualCandidacyProcess executeActivity(Over23IndividualCandidacyProcess process, IUserView userView,
		Object object) {
	    process.cancelCandidacy(userView.getPerson());
	    return process;
	}
    }

    static private class CreateRegistration extends Activity<Over23IndividualCandidacyProcess> {

	@Override
	public void checkPreConditions(Over23IndividualCandidacyProcess process, IUserView userView) {
	    if (!isDegreeAdministrativeOfficeEmployee(userView)) {
		throw new PreConditionNotValidException();
	    }

	    if (!process.isCandidacyAccepted()) {
		throw new PreConditionNotValidException();
	    }

	    if (process.hasRegistrationForCandidacy()) {
		throw new PreConditionNotValidException();
	    }

	}

	@Override
	protected Over23IndividualCandidacyProcess executeActivity(Over23IndividualCandidacyProcess process, IUserView userView,
		Object object) {
	    createRegistration(process);
	    return process;
	}

	private void createRegistration(final Over23IndividualCandidacyProcess candidacyProcess) {
	    candidacyProcess.getCandidacy().createRegistration(getDegreeCurricularPlan(candidacyProcess), CycleType.FIRST_CYCLE,
		    Ingression.CM23);
	}

	private DegreeCurricularPlan getDegreeCurricularPlan(final Over23IndividualCandidacyProcess candidacyProcess) {
	    return candidacyProcess.getAcceptedDegree().getLastActiveDegreeCurricularPlan();
	}
    }
    
    static private class EditPublicCandidacyPersonalInformation extends Activity<Over23IndividualCandidacyProcess> {

	@Override
	public void checkPreConditions(Over23IndividualCandidacyProcess process, IUserView userView) {
	    if (process.isCandidacyCancelled()) {
		throw new PreConditionNotValidException();
	    }	    
	}

	@Override
	protected Over23IndividualCandidacyProcess executeActivity(Over23IndividualCandidacyProcess process,
		IUserView userView, Object object) {
	    process.editPersonalCandidacyInformation(((Over23IndividualCandidacyProcessBean) object).getPersonBean());
	    process.editCommonCandidacyInformation(((Over23IndividualCandidacyProcessBean) object).getCandidacyInformationBean());
	    return process;
	}
	
    }
    
    static private class EditPublicCandidacyDocumentFile extends Activity<Over23IndividualCandidacyProcess> {

	@Override
	public void checkPreConditions(Over23IndividualCandidacyProcess process, IUserView userView) {
	    if (process.isCandidacyCancelled()) {
		throw new PreConditionNotValidException();
	    }	    
	}

	@Override
	protected Over23IndividualCandidacyProcess executeActivity(Over23IndividualCandidacyProcess process,
		IUserView userView, Object object) {
	    CandidacyProcessDocumentUploadBean bean = (CandidacyProcessDocumentUploadBean) object; 
	    process.bindIndividualCandidacyDocumentFile(bean);
	    return process;
	}
    }
    
    static private class EditPublicCandidacyHabilitations extends Activity<Over23IndividualCandidacyProcess> {

	@Override
	public void checkPreConditions(Over23IndividualCandidacyProcess process, IUserView userView) {
	    if (process.isCandidacyCancelled()) {
		throw new PreConditionNotValidException();
	    }	    
	}

	@Override
	protected Over23IndividualCandidacyProcess executeActivity(Over23IndividualCandidacyProcess process, IUserView userView,
		Object object) {
	    process.editCandidacyHabilitations((Over23IndividualCandidacyProcessBean) object);
	    process.saveChoosedDegrees(((Over23IndividualCandidacyProcessBean) object).getSelectedDegrees());
	    process.saveLanguagesReadWriteSpeak((Over23IndividualCandidacyProcessBean) object);
	    process.getCandidacy().setDisabilities(((Over23IndividualCandidacyProcessBean) object).getDisabilities());
	    return process;
	}
    }

    private void saveLanguagesReadWriteSpeak(Over23IndividualCandidacyProcessBean bean) {
	this.getCandidacy().setLanguagesRead(bean.getLanguagesRead());
	this.getCandidacy().setLanguagesSpeak(bean.getLanguagesSpeak());
	this.getCandidacy().setLanguagesWrite(bean.getLanguagesWrite());
    }
}
