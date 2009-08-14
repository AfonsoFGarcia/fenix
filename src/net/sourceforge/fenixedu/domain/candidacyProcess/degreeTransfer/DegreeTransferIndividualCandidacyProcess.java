package net.sourceforge.fenixedu.domain.candidacyProcess.degreeTransfer;

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

public class DegreeTransferIndividualCandidacyProcess extends DegreeTransferIndividualCandidacyProcess_Base {

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
	activities.add(new RevokeDocumentFile());

    }

    private DegreeTransferIndividualCandidacyProcess() {
	super();
    }

    public DegreeTransferIndividualCandidacyProcess(final DegreeTransferIndividualCandidacyProcessBean bean) {
	this();

	/*
	 * 06/04/2009 - The checkParameters, IndividualCandidacy creation and
	 * candidacy information are made in the init method
	 */
	init(bean);
    }

    @Override
    protected void createIndividualCandidacy(IndividualCandidacyProcessBean bean) {
	new DegreeTransferIndividualCandidacy(this, (DegreeTransferIndividualCandidacyProcessBean) bean);
    }

    @Override
    protected void checkParameters(CandidacyProcess candidacyProcess) {
	if (candidacyProcess == null || !candidacyProcess.hasCandidacyPeriod()) {
	    throw new DomainException("error.DegreeTransferIndividualCandidacyProcess.invalid.candidacy.process");
	}
    }

    @Override
    public DegreeTransferCandidacyProcess getCandidacyProcess() {
	return (DegreeTransferCandidacyProcess) super.getCandidacyProcess();
    }

    @Override
    public DegreeTransferIndividualCandidacy getCandidacy() {
	return (DegreeTransferIndividualCandidacy) super.getCandidacy();
    }

    @Override
    public boolean canExecuteActivity(IUserView userView) {
	return isDegreeAdministrativeOfficeEmployee(userView);
    }

    @Override
    public List<Activity> getActivities() {
	return activities;
    }

    public void editCandidacyInformation(final DegreeTransferIndividualCandidacyProcessBean bean) {
	getCandidacy().editCandidacyInformation(bean);
    }

    public Degree getCandidacySelectedDegree() {
	return getCandidacy().getSelectedDegree();
    }

    public CandidacyPrecedentDegreeInformation getCandidacyPrecedentDegreeInformation() {
	return getCandidacy().getPrecedentDegreeInformation();
    }

    public void editCandidacyCurricularCoursesInformation(final DegreeTransferIndividualCandidacyProcessBean bean) {
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
    static public class IndividualCandidacyInformation extends Activity<DegreeTransferIndividualCandidacyProcess> {

	@Override
	public void checkPreConditions(DegreeTransferIndividualCandidacyProcess process, IUserView userView) {
	    /*
	     * 06/04/2009 The candidacy may be submited by someone who's not
	     * authenticated in the system
	     * 
	     * if (!isDegreeAdministrativeOfficeEmployee(userView)) {throw new
	     * PreConditionNotValidException();}
	     */
	}

	@Override
	protected DegreeTransferIndividualCandidacyProcess executeActivity(final DegreeTransferIndividualCandidacyProcess dummy,
		final IUserView userView, final Object object) {
	    final DegreeTransferIndividualCandidacyProcessBean bean = (DegreeTransferIndividualCandidacyProcessBean) object;
	    return new DegreeTransferIndividualCandidacyProcess(bean);
	}
    }

    static public class SendEmailForApplicationSubmission extends Activity<DegreeTransferIndividualCandidacyProcess> {
	@Override
	public void checkPreConditions(DegreeTransferIndividualCandidacyProcess process, IUserView userView) {
	}

	@Override
	protected DegreeTransferIndividualCandidacyProcess executeActivity(DegreeTransferIndividualCandidacyProcess process,
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

    static private class CandidacyPayment extends Activity<DegreeTransferIndividualCandidacyProcess> {
	@Override
	public void checkPreConditions(DegreeTransferIndividualCandidacyProcess process, IUserView userView) {
	    if (!isDegreeAdministrativeOfficeEmployee(userView)) {
		throw new PreConditionNotValidException();
	    }

	    if (process.isCandidacyCancelled()) {
		throw new PreConditionNotValidException();
	    }
	}

	@Override
	protected DegreeTransferIndividualCandidacyProcess executeActivity(DegreeTransferIndividualCandidacyProcess process,
		IUserView userView, Object object) {
	    return null; // nothing to be done, for now payment is being done by
	    // existing interfaces
	}
    }

    static private class EditCandidacyPersonalInformation extends Activity<DegreeTransferIndividualCandidacyProcess> {
	@Override
	public void checkPreConditions(DegreeTransferIndividualCandidacyProcess process, IUserView userView) {
	    if (!isDegreeAdministrativeOfficeEmployee(userView)) {
		throw new PreConditionNotValidException();
	    }

	    if (process.isCandidacyCancelled()) {
		throw new PreConditionNotValidException();
	    }
	}

	@Override
	protected DegreeTransferIndividualCandidacyProcess executeActivity(DegreeTransferIndividualCandidacyProcess process,
		IUserView userView, Object object) {
	    process.editPersonalCandidacyInformation(((DegreeTransferIndividualCandidacyProcessBean) object).getPersonBean());
	    return process;
	}
    }

    static private class EditCommonCandidacyInformation extends Activity<DegreeTransferIndividualCandidacyProcess> {
	@Override
	public void checkPreConditions(DegreeTransferIndividualCandidacyProcess process, IUserView userView) {
	    if (!isDegreeAdministrativeOfficeEmployee(userView)) {
		throw new PreConditionNotValidException();
	    }

	    if (process.isCandidacyCancelled()) {
		throw new PreConditionNotValidException();
	    }
	}

	@Override
	protected DegreeTransferIndividualCandidacyProcess executeActivity(DegreeTransferIndividualCandidacyProcess process,
		IUserView userView, Object object) {
	    process.editPersonalCandidacyInformationPublic(((DegreeTransferIndividualCandidacyProcessBean) object)
		    .getPersonBean());
	    process.editCommonCandidacyInformation(((DegreeTransferIndividualCandidacyProcessBean) object)
		    .getCandidacyInformationBean());
	    return process;
	}

	@Override
	public Boolean isVisibleForAdminOffice() {
	    return Boolean.FALSE;
	}

    }

    static private class EditCandidacyInformation extends Activity<DegreeTransferIndividualCandidacyProcess> {
	@Override
	public void checkPreConditions(DegreeTransferIndividualCandidacyProcess process, IUserView userView) {
	    if (!isDegreeAdministrativeOfficeEmployee(userView)) {
		throw new PreConditionNotValidException();
	    }
	    if (process.isCandidacyCancelled() || process.isCandidacyAccepted() || process.hasRegistrationForCandidacy()) {
		throw new PreConditionNotValidException();
	    }
	}

	@Override
	protected DegreeTransferIndividualCandidacyProcess executeActivity(DegreeTransferIndividualCandidacyProcess process,
		IUserView userView, Object object) {
	    process.editCandidacyHabilitations((DegreeTransferIndividualCandidacyProcessBean) object);
	    process.getCandidacy().editObservations((DegreeTransferIndividualCandidacyProcessBean) object);
	    process.editCandidacyInformation((DegreeTransferIndividualCandidacyProcessBean) object);
	    return process;
	}
    }

    static private class EditCandidacyCurricularCoursesInformation extends Activity<DegreeTransferIndividualCandidacyProcess> {
	@Override
	public void checkPreConditions(DegreeTransferIndividualCandidacyProcess process, IUserView userView) {
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
	protected DegreeTransferIndividualCandidacyProcess executeActivity(DegreeTransferIndividualCandidacyProcess process,
		IUserView userView, Object object) {
	    process.editCandidacyCurricularCoursesInformation((DegreeTransferIndividualCandidacyProcessBean) object);
	    return process;
	}
    }

    static private class IntroduceCandidacyResult extends Activity<DegreeTransferIndividualCandidacyProcess> {
	@Override
	public void checkPreConditions(DegreeTransferIndividualCandidacyProcess process, IUserView userView) {
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
	protected DegreeTransferIndividualCandidacyProcess executeActivity(DegreeTransferIndividualCandidacyProcess process,
		IUserView userView, Object object) {
	    process.getCandidacy().editCandidacyResult((DegreeTransferIndividualCandidacyResultBean) object);
	    return process;
	}
    }

    static private class CancelCandidacy extends Activity<DegreeTransferIndividualCandidacyProcess> {
	@Override
	public void checkPreConditions(DegreeTransferIndividualCandidacyProcess process, IUserView userView) {
	    if (!isDegreeAdministrativeOfficeEmployee(userView)) {
		throw new PreConditionNotValidException();
	    }
	    if (process.isCandidacyCancelled() || process.hasAnyPaymentForCandidacy() || !process.isCandidacyInStandBy()) {
		throw new PreConditionNotValidException();
	    }
	}

	@Override
	protected DegreeTransferIndividualCandidacyProcess executeActivity(DegreeTransferIndividualCandidacyProcess process,
		IUserView userView, Object object) {
	    process.cancelCandidacy(userView.getPerson());
	    return process;
	}
    }

    static private class CreateRegistration extends Activity<DegreeTransferIndividualCandidacyProcess> {
	@Override
	public void checkPreConditions(DegreeTransferIndividualCandidacyProcess process, IUserView userView) {
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
	protected DegreeTransferIndividualCandidacyProcess executeActivity(DegreeTransferIndividualCandidacyProcess process,
		IUserView userView, Object object) {
	    process.getCandidacy().createRegistration(getDegreeCurricularPlan(process), CycleType.FIRST_CYCLE, Ingression.TRF);
	    return process;
	}

	private DegreeCurricularPlan getDegreeCurricularPlan(final DegreeTransferIndividualCandidacyProcess process) {
	    return process.getCandidacySelectedDegree().getLastActiveDegreeCurricularPlan();
	}
    }

    static private class EditPublicCandidacyPersonalInformation extends Activity<DegreeTransferIndividualCandidacyProcess> {

	@Override
	public void checkPreConditions(DegreeTransferIndividualCandidacyProcess process, IUserView userView) {
	    if (process.isCandidacyCancelled()) {
		throw new PreConditionNotValidException();
	    }
	}

	@Override
	protected DegreeTransferIndividualCandidacyProcess executeActivity(DegreeTransferIndividualCandidacyProcess process,
		IUserView userView, Object object) {
	    process.editPersonalCandidacyInformation(((DegreeTransferIndividualCandidacyProcessBean) object).getPersonBean());
	    process.editCommonCandidacyInformation(((DegreeTransferIndividualCandidacyProcessBean) object)
		    .getCandidacyInformationBean());
	    return process;
	}

	@Override
	public Boolean isVisibleForAdminOffice() {
	    return Boolean.FALSE;
	}

    }

    static private class EditPublicCandidacyDocumentFile extends Activity<DegreeTransferIndividualCandidacyProcess> {

	@Override
	public void checkPreConditions(DegreeTransferIndividualCandidacyProcess process, IUserView userView) {
	    if (process.isCandidacyCancelled()) {
		throw new PreConditionNotValidException();
	    }
	}

	@Override
	protected DegreeTransferIndividualCandidacyProcess executeActivity(DegreeTransferIndividualCandidacyProcess process,
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

    static private class EditPublicCandidacyHabilitations extends Activity<DegreeTransferIndividualCandidacyProcess> {

	@Override
	public void checkPreConditions(DegreeTransferIndividualCandidacyProcess process, IUserView userView) {
	    if (process.isCandidacyCancelled()) {
		throw new PreConditionNotValidException();
	    }
	}

	@Override
	protected DegreeTransferIndividualCandidacyProcess executeActivity(DegreeTransferIndividualCandidacyProcess process,
		IUserView userView, Object object) {
	    process.editCandidacyHabilitations((DegreeTransferIndividualCandidacyProcessBean) object);
	    process.editCommonCandidacyInformation(((DegreeTransferIndividualCandidacyProcessBean) object)
		    .getCandidacyInformationBean());
	    process.getCandidacy()
		    .editSelectedDegree(((DegreeTransferIndividualCandidacyProcessBean) object).getSelectedDegree());
	    process.getCandidacy().editObservations((DegreeTransferIndividualCandidacyProcessBean) object);
	    return process;
	}

	@Override
	public Boolean isVisibleForAdminOffice() {
	    return Boolean.FALSE;
	}

    }

    static private class EditDocuments extends Activity<DegreeTransferIndividualCandidacyProcess> {

	@Override
	public void checkPreConditions(DegreeTransferIndividualCandidacyProcess process, IUserView userView) {
	    if (process.isCandidacyCancelled()) {
		throw new PreConditionNotValidException();
	    }
	}

	@Override
	protected DegreeTransferIndividualCandidacyProcess executeActivity(DegreeTransferIndividualCandidacyProcess process,
		IUserView userView, Object object) {
	    CandidacyProcessDocumentUploadBean bean = (CandidacyProcessDocumentUploadBean) object;
	    process.bindIndividualCandidacyDocumentFile(bean);
	    return process;
	}
    }

    static private class ChangeProcessCheckedState extends Activity<DegreeTransferIndividualCandidacyProcess> {

	@Override
	public void checkPreConditions(DegreeTransferIndividualCandidacyProcess process, IUserView userView) {
	    if (!isDegreeAdministrativeOfficeEmployee(userView)) {
		throw new PreConditionNotValidException();
	    }
	}

	@Override
	protected DegreeTransferIndividualCandidacyProcess executeActivity(DegreeTransferIndividualCandidacyProcess process,
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

	if (getCandidacy().getPrecedentDegreeInformation().isExternal()
		&& getFileForType(IndividualCandidacyDocumentFileType.REGISTRATION_CERTIFICATE) == null) {
	    missingDocumentFiles.add(IndividualCandidacyDocumentFileType.REGISTRATION_CERTIFICATE);
	}

	if (getCandidacy().getPrecedentDegreeInformation().isExternal()
		&& getFileForType(IndividualCandidacyDocumentFileType.NO_PRESCRIPTION_CERTIFICATE) == null) {
	    missingDocumentFiles.add(IndividualCandidacyDocumentFileType.NO_PRESCRIPTION_CERTIFICATE);
	}

	if (getCandidacy().getPrecedentDegreeInformation().isExternal()
		&& getFileForType(IndividualCandidacyDocumentFileType.FIRST_CYCLE_ACCESS_HABILITATION_CERTIFICATE) == null) {
	    missingDocumentFiles.add(IndividualCandidacyDocumentFileType.FIRST_CYCLE_ACCESS_HABILITATION_CERTIFICATE);
	}

	if (getCandidacy().getPrecedentDegreeInformation().isInternal()
		&& getFileForType(IndividualCandidacyDocumentFileType.GRADES_DOCUMENT) == null) {
	    missingDocumentFiles.add(IndividualCandidacyDocumentFileType.GRADES_DOCUMENT);
	}

	return missingDocumentFiles;
    }

    static protected class RevokeDocumentFile extends Activity<DegreeTransferIndividualCandidacyProcess> {

	@Override
	public void checkPreConditions(DegreeTransferIndividualCandidacyProcess process, IUserView userView) {
	    if (!isDegreeAdministrativeOfficeEmployee(userView)) {
		throw new PreConditionNotValidException();
	    }
	}

	@Override
	protected DegreeTransferIndividualCandidacyProcess executeActivity(DegreeTransferIndividualCandidacyProcess process,
		IUserView userView, Object object) {
	    ((CandidacyProcessDocumentUploadBean) object).getDocumentFile().setCandidacyFileActive(Boolean.FALSE);
	    return process;
	}

	@Override
	public Boolean isVisibleForAdminOffice() {
	    return Boolean.FALSE;
	}

    }

}
