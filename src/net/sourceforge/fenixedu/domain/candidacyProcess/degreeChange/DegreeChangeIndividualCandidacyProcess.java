package net.sourceforge.fenixedu.domain.candidacyProcess.degreeChange;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.caseHandling.StartActivity;
import net.sourceforge.fenixedu.domain.Degree;
import net.sourceforge.fenixedu.domain.DegreeCurricularPlan;
import net.sourceforge.fenixedu.domain.candidacy.Ingression;
import net.sourceforge.fenixedu.domain.candidacyProcess.CandidacyPrecedentDegreeInformation;
import net.sourceforge.fenixedu.domain.candidacyProcess.CandidacyProcess;
import net.sourceforge.fenixedu.domain.candidacyProcess.CandidacyProcessDocumentUploadBean;
import net.sourceforge.fenixedu.domain.candidacyProcess.DegreeOfficePublicCandidacyHashCode;
import net.sourceforge.fenixedu.domain.candidacyProcess.IndividualCandidacyDocumentFileType;
import net.sourceforge.fenixedu.domain.candidacyProcess.IndividualCandidacyProcessBean;
import net.sourceforge.fenixedu.domain.caseHandling.Activity;
import net.sourceforge.fenixedu.domain.caseHandling.PreConditionNotValidException;
import net.sourceforge.fenixedu.domain.degreeStructure.CycleType;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.person.RoleType;

public class DegreeChangeIndividualCandidacyProcess extends DegreeChangeIndividualCandidacyProcess_Base {

    static private List<Activity> activities = new ArrayList<Activity>();
    static {
	activities.add(new CandidacyPayment());
	activities.add(new EditCandidacyPersonalInformation());
	activities.add(new EditCommonCandidacyInformation());
	activities.add(new EditCandidacyInformation());
	activities.add(new EditCandidacyCurricularCoursesInformation());
	activities.add(new IntroduceCandidacyResult());
	activities.add(new CancelCandidacy());
	activities.add(new CreateRegistration());
	activities.add(new EditPublicCandidacyPersonalInformation());
	activities.add(new EditPublicCandidacyHabilitations());
	activities.add(new EditPublicCandidacyDocumentFile());
	activities.add(new SendEmailForApplicationSubmission());
	activities.add(new EditDocuments());
	activities.add(new ChangeProcessCheckedState());
    }

    private DegreeChangeIndividualCandidacyProcess() {
	super();
    }

    private DegreeChangeIndividualCandidacyProcess(final DegreeChangeIndividualCandidacyProcessBean bean) {
	this();

	/*
	 * 06/04/2009 - The checkParameters, IndividualCandidacy creation and
	 * candidacy information are made in the init method
	 */
	init(bean);
    }

    @Override
    protected void createIndividualCandidacy(IndividualCandidacyProcessBean bean) {
	new DegreeChangeIndividualCandidacy(this, (DegreeChangeIndividualCandidacyProcessBean) bean);
    }

    @Override
    protected void checkParameters(final CandidacyProcess candidacyProcess) {
	if (candidacyProcess == null || !candidacyProcess.hasCandidacyPeriod()) {
	    throw new DomainException("error.DegreeChangeIndividualCandidacyProcess.invalid.candidacy.process");
	}
    }

    @Override
    public DegreeChangeCandidacyProcess getCandidacyProcess() {
	return (DegreeChangeCandidacyProcess) super.getCandidacyProcess();
    }

    @Override
    public DegreeChangeIndividualCandidacy getCandidacy() {
	return (DegreeChangeIndividualCandidacy) super.getCandidacy();
    }

    @Override
    public boolean canExecuteActivity(final IUserView userView) {
	return isDegreeAdministrativeOfficeEmployee(userView);
    }

    @Override
    public List<Activity> getActivities() {
	return activities;
    }

    public Degree getCandidacySelectedDegree() {
	return getCandidacy().getSelectedDegree();
    }

    public CandidacyPrecedentDegreeInformation getCandidacyPrecedentDegreeInformation() {
	return getCandidacy().getPrecedentDegreeInformation();
    }

    private void editCandidacyInformation(final DegreeChangeIndividualCandidacyProcessBean bean) {
	getCandidacy().editCandidacyInformation(bean);
    }

    private void editCandidacyCurricularCoursesInformation(final DegreeChangeIndividualCandidacyProcessBean bean) {
	getCandidacy().editCandidacyCurricularCoursesInformation(bean);
    }

    public BigDecimal getCandidacyAffinity() {
	return getCandidacy().getAffinity();
    }

    public Integer getCandidacyDegreeNature() {
	return getCandidacy().getDegreeNature();
    }

    public BigDecimal getCandidacyApprovedEctsRate() {
	return getCandidacy().getApprovedEctsRate();
    }

    public BigDecimal getCandidacyGradeRate() {
	return getCandidacy().getGradeRate();
    }

    public BigDecimal getCandidacySeriesCandidacyGrade() {
	return getCandidacy().getSeriesCandidacyGrade();
    }

    public boolean hasCandidacyForSelectedDegree(final Degree degree) {
	return getCandidacySelectedDegree() == degree;
    }

    // static information

    static private boolean isDegreeAdministrativeOfficeEmployee(IUserView userView) {
	return userView.hasRoleType(RoleType.ACADEMIC_ADMINISTRATIVE_OFFICE)
		&& userView.getPerson().getEmployeeAdministrativeOffice().isDegree();
    }

    @StartActivity
    static public class IndividualCandidacyInformation extends Activity<DegreeChangeIndividualCandidacyProcess> {

	@Override
	public void checkPreConditions(DegreeChangeIndividualCandidacyProcess process, IUserView userView) {
	    /*
	     * 06/04/2009 The candidacy may be submited by someone who's not
	     * authenticated in the system
	     * 
	     * if (!isDegreeAdministrativeOfficeEmployee(userView)) {throw new
	     * PreConditionNotValidException();}
	     */
	}

	@Override
	protected DegreeChangeIndividualCandidacyProcess executeActivity(DegreeChangeIndividualCandidacyProcess dummy,
		IUserView userView, Object object) {
	    final DegreeChangeIndividualCandidacyProcessBean bean = (DegreeChangeIndividualCandidacyProcessBean) object;
	    return new DegreeChangeIndividualCandidacyProcess(bean);
	}
    }

    static public class SendEmailForApplicationSubmission extends Activity<DegreeChangeIndividualCandidacyProcess> {
	@Override
	public void checkPreConditions(DegreeChangeIndividualCandidacyProcess process, IUserView userView) {
	}

	@Override
	protected DegreeChangeIndividualCandidacyProcess executeActivity(DegreeChangeIndividualCandidacyProcess process,
		IUserView userView, Object object) {
	    DegreeOfficePublicCandidacyHashCode hashCode = (DegreeOfficePublicCandidacyHashCode) object;
	    hashCode.sendEmailForApplicationSuccessfullySubmited();
	    return process;
	}

	@Override
	public Boolean isVisibleForAdminOffice() {
	    return Boolean.FALSE;
	}

    }

    static private class CandidacyPayment extends Activity<DegreeChangeIndividualCandidacyProcess> {

	@Override
	public void checkPreConditions(DegreeChangeIndividualCandidacyProcess process, IUserView userView) {
	    if (!isDegreeAdministrativeOfficeEmployee(userView)) {
		throw new PreConditionNotValidException();
	    }

	    if (process.isCandidacyCancelled()) {
		throw new PreConditionNotValidException();
	    }
	}

	@Override
	protected DegreeChangeIndividualCandidacyProcess executeActivity(DegreeChangeIndividualCandidacyProcess process,
		IUserView userView, Object object) {
	    return process; // nothing to be done, for now payment is being
	    // done by existing interfaces
	}
    }

    static private class EditCandidacyPersonalInformation extends Activity<DegreeChangeIndividualCandidacyProcess> {

	@Override
	public void checkPreConditions(DegreeChangeIndividualCandidacyProcess process, IUserView userView) {
	    if (!isDegreeAdministrativeOfficeEmployee(userView)) {
		throw new PreConditionNotValidException();
	    }
	    if (process.isCandidacyCancelled()) {
		throw new PreConditionNotValidException();
	    }
	}

	@Override
	protected DegreeChangeIndividualCandidacyProcess executeActivity(DegreeChangeIndividualCandidacyProcess process,
		IUserView userView, Object object) {
	    process.editPersonalCandidacyInformation(((DegreeChangeIndividualCandidacyProcessBean) object).getPersonBean());
	    return process;
	}
    }

    static private class EditPublicCandidacyPersonalInformation extends Activity<DegreeChangeIndividualCandidacyProcess> {

	@Override
	public void checkPreConditions(DegreeChangeIndividualCandidacyProcess process, IUserView userView) {
	    if (process.isCandidacyCancelled()) {
		throw new PreConditionNotValidException();
	    }
	}

	@Override
	protected DegreeChangeIndividualCandidacyProcess executeActivity(DegreeChangeIndividualCandidacyProcess process,
		IUserView userView, Object object) {
	    process.editPersonalCandidacyInformationPublic(((DegreeChangeIndividualCandidacyProcessBean) object).getPersonBean());
	    process.editCommonCandidacyInformation(((DegreeChangeIndividualCandidacyProcessBean) object)
		    .getCandidacyInformationBean());
	    return process;
	}

	@Override
	public Boolean isVisibleForAdminOffice() {
	    return Boolean.FALSE;
	}

    }

    static private class EditPublicCandidacyDocumentFile extends Activity<DegreeChangeIndividualCandidacyProcess> {

	@Override
	public void checkPreConditions(DegreeChangeIndividualCandidacyProcess process, IUserView userView) {
	    if (process.isCandidacyCancelled()) {
		throw new PreConditionNotValidException();
	    }
	}

	@Override
	protected DegreeChangeIndividualCandidacyProcess executeActivity(DegreeChangeIndividualCandidacyProcess process,
		IUserView userView, Object object) {
	    CandidacyProcessDocumentUploadBean bean = (CandidacyProcessDocumentUploadBean) object;
	    process.bindIndividualCandidacyDocumentFile(bean);
	    return process;
	}

	@Override
	public Boolean isVisibleForAdminOffice() {
	    return Boolean.FALSE;
	}

    }

    static private class EditPublicCandidacyHabilitations extends Activity<DegreeChangeIndividualCandidacyProcess> {

	@Override
	public void checkPreConditions(DegreeChangeIndividualCandidacyProcess process, IUserView userView) {
	    if (process.isCandidacyCancelled()) {
		throw new PreConditionNotValidException();
	    }
	}

	@Override
	protected DegreeChangeIndividualCandidacyProcess executeActivity(DegreeChangeIndividualCandidacyProcess process,
		IUserView userView, Object object) {
	    process.editCandidacyHabilitations((DegreeChangeIndividualCandidacyProcessBean) object);
	    process.editCommonCandidacyInformation(((DegreeChangeIndividualCandidacyProcessBean) object)
		    .getCandidacyInformationBean());
	    process.getCandidacy().editSelectedDegree(((DegreeChangeIndividualCandidacyProcessBean) object).getSelectedDegree());
	    process.getCandidacy().editObservations((DegreeChangeIndividualCandidacyProcessBean) object);
	    return process;
	}

	@Override
	public Boolean isVisibleForAdminOffice() {
	    return Boolean.FALSE;
	}

    }

    static private class EditCommonCandidacyInformation extends Activity<DegreeChangeIndividualCandidacyProcess> {

	@Override
	public void checkPreConditions(DegreeChangeIndividualCandidacyProcess process, IUserView userView) {
	    if (!isDegreeAdministrativeOfficeEmployee(userView)) {
		throw new PreConditionNotValidException();
	    }
	    if (process.isCandidacyCancelled()) {
		throw new PreConditionNotValidException();
	    }
	}

	@Override
	protected DegreeChangeIndividualCandidacyProcess executeActivity(DegreeChangeIndividualCandidacyProcess process,
		IUserView userView, Object object) {
	    process.editCommonCandidacyInformation(((DegreeChangeIndividualCandidacyProcessBean) object)
		    .getCandidacyInformationBean());
	    return process;
	}
    }

    static private class EditCandidacyInformation extends Activity<DegreeChangeIndividualCandidacyProcess> {

	@Override
	public void checkPreConditions(DegreeChangeIndividualCandidacyProcess process, IUserView userView) {
	    if (!isDegreeAdministrativeOfficeEmployee(userView)) {
		throw new PreConditionNotValidException();
	    }
	    if (process.isCandidacyCancelled() || process.isCandidacyAccepted() || process.hasRegistrationForCandidacy()) {
		throw new PreConditionNotValidException();
	    }
	}

	@Override
	protected DegreeChangeIndividualCandidacyProcess executeActivity(DegreeChangeIndividualCandidacyProcess process,
		IUserView userView, Object object) {
	    process.editCandidacyHabilitations((DegreeChangeIndividualCandidacyProcessBean) object);
	    process.getCandidacy().editObservations((DegreeChangeIndividualCandidacyProcessBean) object);
	    process.editCandidacyInformation((DegreeChangeIndividualCandidacyProcessBean) object);
	    return process;
	}
    }

    static private class EditCandidacyCurricularCoursesInformation extends Activity<DegreeChangeIndividualCandidacyProcess> {

	@Override
	public void checkPreConditions(DegreeChangeIndividualCandidacyProcess process, IUserView userView) {
	    if (!isDegreeAdministrativeOfficeEmployee(userView)) {
		throw new PreConditionNotValidException();
	    }
	    if (process.isCandidacyCancelled()) {
		throw new PreConditionNotValidException();
	    }
	    if (process.isInStandBy()) {
		throw new PreConditionNotValidException();
	    }
	}

	@Override
	protected DegreeChangeIndividualCandidacyProcess executeActivity(DegreeChangeIndividualCandidacyProcess process,
		IUserView userView, Object object) {
	    process.editCandidacyCurricularCoursesInformation((DegreeChangeIndividualCandidacyProcessBean) object);
	    return process;
	}
    }

    static private class IntroduceCandidacyResult extends Activity<DegreeChangeIndividualCandidacyProcess> {

	@Override
	public void checkPreConditions(DegreeChangeIndividualCandidacyProcess process, IUserView userView) {
	    if (!isDegreeAdministrativeOfficeEmployee(userView)) {
		throw new PreConditionNotValidException();
	    }

	    if (process.isCandidacyCancelled() || !process.hasAnyPaymentForCandidacy()) {
		throw new PreConditionNotValidException();
	    }

	    if (!process.isSentToCoordinator() && !process.isSentToScientificCouncil()) {
		throw new PreConditionNotValidException();
	    }
	}

	@Override
	protected DegreeChangeIndividualCandidacyProcess executeActivity(DegreeChangeIndividualCandidacyProcess process,
		IUserView userView, Object object) {
	    process.getCandidacy().editCandidacyResult((DegreeChangeIndividualCandidacyResultBean) object);
	    return process;
	}
    }

    static private class CancelCandidacy extends Activity<DegreeChangeIndividualCandidacyProcess> {

	@Override
	public void checkPreConditions(DegreeChangeIndividualCandidacyProcess process, IUserView userView) {
	    if (!isDegreeAdministrativeOfficeEmployee(userView)) {
		throw new PreConditionNotValidException();
	    }
	    if (process.isCandidacyCancelled() || process.hasAnyPaymentForCandidacy() || !process.isCandidacyInStandBy()) {
		throw new PreConditionNotValidException();
	    }
	}

	@Override
	protected DegreeChangeIndividualCandidacyProcess executeActivity(DegreeChangeIndividualCandidacyProcess process,
		IUserView userView, Object object) {
	    process.cancelCandidacy(userView.getPerson());
	    return process;
	}
    }

    static private class CreateRegistration extends Activity<DegreeChangeIndividualCandidacyProcess> {

	@Override
	public void checkPreConditions(DegreeChangeIndividualCandidacyProcess process, IUserView userView) {
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
	protected DegreeChangeIndividualCandidacyProcess executeActivity(DegreeChangeIndividualCandidacyProcess process,
		IUserView userView, Object object) {
	    process.getCandidacy().createRegistration(getDegreeCurricularPlan(process), CycleType.FIRST_CYCLE,
		    getIngression(process));
	    return process;
	}

	private Ingression getIngression(final DegreeChangeIndividualCandidacyProcess process) {
	    return process.getCandidacyPrecedentDegreeInformation().isExternal() ? Ingression.MCE : Ingression.MCI;
	}

	private DegreeCurricularPlan getDegreeCurricularPlan(final DegreeChangeIndividualCandidacyProcess process) {
	    return process.getCandidacySelectedDegree().getLastActiveDegreeCurricularPlan();
	}
    }

    static private class EditDocuments extends Activity<DegreeChangeIndividualCandidacyProcess> {

	@Override
	public void checkPreConditions(DegreeChangeIndividualCandidacyProcess process, IUserView userView) {
	    if (process.isCandidacyCancelled()) {
		throw new PreConditionNotValidException();
	    }
	}

	@Override
	protected DegreeChangeIndividualCandidacyProcess executeActivity(DegreeChangeIndividualCandidacyProcess process,
		IUserView userView, Object object) {
	    CandidacyProcessDocumentUploadBean bean = (CandidacyProcessDocumentUploadBean) object;
	    process.bindIndividualCandidacyDocumentFile(bean);
	    return process;
	}
    }

    static private class ChangeProcessCheckedState extends Activity<DegreeChangeIndividualCandidacyProcess> {

	@Override
	public void checkPreConditions(DegreeChangeIndividualCandidacyProcess process, IUserView userView) {
	    if (!isDegreeAdministrativeOfficeEmployee(userView)) {
		throw new PreConditionNotValidException();
	    }
	}

	@Override
	protected DegreeChangeIndividualCandidacyProcess executeActivity(DegreeChangeIndividualCandidacyProcess process,
		IUserView userView, Object object) {
	    process.setProcessChecked(((IndividualCandidacyProcessBean) object).getProcessChecked());
	    return process;
	}
    }

    @Override
    public Boolean isCandidacyProcessComplete() {
	// TODO Auto-generated method stub
	return null;
    }

    @Override
    public List<IndividualCandidacyDocumentFileType> getMissingRequiredDocumentFiles() {
	List<IndividualCandidacyDocumentFileType> missingDocumentFiles = new ArrayList<IndividualCandidacyDocumentFileType>();

	if (getFileForType(IndividualCandidacyDocumentFileType.PHOTO) == null) {
	    missingDocumentFiles.add(IndividualCandidacyDocumentFileType.PHOTO);
	}

	if (getFileForType(IndividualCandidacyDocumentFileType.CV_DOCUMENT) == null) {
	    missingDocumentFiles.add(IndividualCandidacyDocumentFileType.CV_DOCUMENT);
	}

	if (!getCandidacy().hasStudent()
		&& getFileForType(IndividualCandidacyDocumentFileType.HABILITATION_CERTIFICATE_DOCUMENT) == null) {
	    missingDocumentFiles.add(IndividualCandidacyDocumentFileType.HABILITATION_CERTIFICATE_DOCUMENT);
	}

	if (getFileForType(IndividualCandidacyDocumentFileType.DOCUMENT_IDENTIFICATION) == null) {
	    missingDocumentFiles.add(IndividualCandidacyDocumentFileType.DOCUMENT_IDENTIFICATION);
	}

	if (getFileForType(IndividualCandidacyDocumentFileType.PAYMENT_DOCUMENT) == null) {
	    missingDocumentFiles.add(IndividualCandidacyDocumentFileType.PAYMENT_DOCUMENT);
	}

	if (!getCandidacy().hasStudent() && getFileForType(IndividualCandidacyDocumentFileType.REGISTRATION_CERTIFICATE) == null) {
	    missingDocumentFiles.add(IndividualCandidacyDocumentFileType.REGISTRATION_CERTIFICATE);
	}

	if (!getCandidacy().hasStudent()
		&& getFileForType(IndividualCandidacyDocumentFileType.NO_PRESCRIPTION_CERTIFICATE) == null) {
	    missingDocumentFiles.add(IndividualCandidacyDocumentFileType.NO_PRESCRIPTION_CERTIFICATE);
	}

	if (!getCandidacy().hasStudent()
		&& getFileForType(IndividualCandidacyDocumentFileType.FIRST_CYCLE_ACCESS_HABILITATION_CERTIFICATE) == null) {
	    missingDocumentFiles.add(IndividualCandidacyDocumentFileType.FIRST_CYCLE_ACCESS_HABILITATION_CERTIFICATE);
	}

	return missingDocumentFiles;
    }

}
