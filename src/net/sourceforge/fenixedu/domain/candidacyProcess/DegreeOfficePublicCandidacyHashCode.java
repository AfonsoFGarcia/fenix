package net.sourceforge.fenixedu.domain.candidacyProcess;

import java.util.ResourceBundle;
import java.util.UUID;

import net.sourceforge.fenixedu.domain.PublicCandidacyHashCode;
import net.sourceforge.fenixedu.domain.candidacyProcess.exceptions.HashCodeForEmailAndProcessAlreadyBounded;
import pt.ist.fenixWebFramework.services.Service;
import pt.utl.ist.fenix.tools.util.DateFormatUtil;
import pt.utl.ist.fenix.tools.util.i18n.Language;

public class DegreeOfficePublicCandidacyHashCode extends DegreeOfficePublicCandidacyHashCode_Base {

    public DegreeOfficePublicCandidacyHashCode() {
	super();
    }

    public boolean isAssociatedWithEmailAndCandidacyProcess(String email, Class<? extends IndividualCandidacyProcess> type,
	    CandidacyProcess process) {
	return email.equals(this.getEmail()) && this.hasIndividualCandidacyProcess()
		&& this.getIndividualCandidacyProcess().getClass() == type
		&& this.getIndividualCandidacyProcess().getCandidacyProcess() == process;
    }

    @Override
    public boolean hasCandidacyProcess() {
	return hasIndividualCandidacyProcess();
    }

    @Override
    public boolean isFromDegreeOffice() {
	return true;
    }

    /**
     * Get an hash code not associated with an individual candidacy process for
     * the email. Also sends an email
     * 
     * @throws HashCodeForEmailAndProcessAlreadyBounded
     */
    @Service
    static public DegreeOfficePublicCandidacyHashCode getUnusedOrCreateNewHashCodeAndSendEmailForApplicationSubmissionToCandidate(
	    Class<? extends IndividualCandidacyProcess> individualCandidadyProcessClass, CandidacyProcess parentProcess,
	    String email) throws HashCodeForEmailAndProcessAlreadyBounded {

	DegreeOfficePublicCandidacyHashCode hashCode = getUnusedOrCreateNewHashCode(individualCandidadyProcessClass,
		parentProcess, email);
	hashCode.sendEmailForApplicationSubmissionCandidacyForm(individualCandidadyProcessClass);
	return hashCode;
    }

    private static final String APPLICATION_SUBMISSION_LINK = ".const.public.application.submission.link";
    private static final String SEND_LINK_TO_ACCESS_SUBMISSION_FORM_SUBJECT = ".message.email.subject.send.link.to.submission.form";
    private static final String SEND_LINK_TO_ACCESS_SUBMISSION_FORM_BODY = ".message.email.body.send.link.to.submission.form";

    private void sendEmailForApplicationSubmissionCandidacyForm(
	    Class<? extends IndividualCandidacyProcess> individualCandidadyProcessClass) {
	ResourceBundle bundle = ResourceBundle.getBundle("resources.CandidateResources", Language.getLocale());
	String subject = bundle.getString(individualCandidadyProcessClass.getSimpleName()
		+ SEND_LINK_TO_ACCESS_SUBMISSION_FORM_SUBJECT);
	String body = bundle
		.getString(individualCandidadyProcessClass.getSimpleName() + SEND_LINK_TO_ACCESS_SUBMISSION_FORM_BODY);
	String link = bundle.getString(individualCandidadyProcessClass.getSimpleName() + APPLICATION_SUBMISSION_LINK);
	link = String.format(link, this.getValue(), Language.getLocale());
	body = String.format(body, link);
	this.sendEmail(subject, body);
    }

    private static final String APPLICATION_ACCESS_LINK = ".const.public.application.access.link";
    private static final String INFORM_APPLICATION_SUCCESS_SUBJECT = ".message.email.subject.application.submited";
    private static final String INFORM_APPLICATION_SUCCESS_BODY = ".message.email.body.application.submited";

    public void sendEmailForApplicationSuccessfullySubmited() {
	ResourceBundle bundle = ResourceBundle.getBundle("resources.CandidateResources", Language.getLocale());
	String subject = bundle.getString(this.getIndividualCandidacyProcess().getClass().getSimpleName()
		+ INFORM_APPLICATION_SUCCESS_SUBJECT);
	String body = bundle.getString(this.getIndividualCandidacyProcess().getClass().getSimpleName()
		+ INFORM_APPLICATION_SUCCESS_BODY);
	String link = getDefaultPublicLink();

	body = String.format(body, new String[] {
		this.getIndividualCandidacyProcess().getProcessCode(),
		link,
		this.getIndividualCandidacyProcess().getCandidacyProcess().getCandidacyEnd().toString(
			DateFormatUtil.DEFAULT_DATE_FORMAT) });

	sendEmail(subject, body);
    }

    private static final String RECOVERY_APPLICATION_SUBJECT = ".message.email.subject.recovery.access";
    private static final String RECOVERY_APPLICATION_BODY = ".message.email.body.recovery.access";

    public void sendEmailFoAccessLinkRecovery() {
	ResourceBundle bundle = ResourceBundle.getBundle("resources.CandidateResources", Language.getLocale());
	String subject = bundle.getString(this.getIndividualCandidacyProcess().getClass().getSimpleName()
		+ RECOVERY_APPLICATION_SUBJECT);
	String body = bundle.getString(this.getIndividualCandidacyProcess().getClass().getSimpleName()
		+ RECOVERY_APPLICATION_BODY);
	String link = getDefaultPublicLink();

	body = String.format(body, new String[] { link, this.getIndividualCandidacyProcess().getProcessCode() });

	sendEmail(subject, body);
    }

    public String getDefaultPublicLink() {
	ResourceBundle bundle = ResourceBundle.getBundle("resources.CandidateResources", Language.getLocale());
	return String.format(bundle.getString(this.getIndividualCandidacyProcess().getClass().getSimpleName()
		+ APPLICATION_ACCESS_LINK), this.getValue(), Language.getLocale().getLanguage());
    }

    /**
     * Get an hash code not associated with an individual candidacy process for
     * the email. If the hash
     * 
     * @throws HashCodeForEmailAndProcessAlreadyBounded
     */
    @Service
    static public DegreeOfficePublicCandidacyHashCode getUnusedOrCreateNewHashCode(
	    Class<? extends IndividualCandidacyProcess> individualCandidadyProcessClass, CandidacyProcess parentProcess,
	    String email) throws HashCodeForEmailAndProcessAlreadyBounded {

	DegreeOfficePublicCandidacyHashCode publicCandidacyHashCode = getPublicCandidacyHashCodeByEmailAndCandidacyProcessTypeOrNotAssociated(
		email, individualCandidadyProcessClass, parentProcess);

	if (publicCandidacyHashCode == null) {
	    return createNewHashCode(email);
	} else if (!publicCandidacyHashCode.hasCandidacyProcess()) {
	    return publicCandidacyHashCode;
	} else {
	    throw new HashCodeForEmailAndProcessAlreadyBounded();
	}
    }

    private static DegreeOfficePublicCandidacyHashCode createNewHashCode(String email) {
	DegreeOfficePublicCandidacyHashCode publicCandidacyHashCode = new DegreeOfficePublicCandidacyHashCode();
	publicCandidacyHashCode.setEmail(email);
	publicCandidacyHashCode.setValue(UUID.randomUUID().toString());
	return publicCandidacyHashCode;
    }

    public static DegreeOfficePublicCandidacyHashCode getPublicCandidacyHashCodeByEmailAndCandidacyProcessType(
	    final String email, Class<? extends IndividualCandidacyProcess> processType, CandidacyProcess process) {

	for (final PublicCandidacyHashCode hashCode : getHashCodesAssociatedWithEmail(email)) {
	    if (!hashCode.isFromDegreeOffice()) {
		continue;
	    }

	    final DegreeOfficePublicCandidacyHashCode hash = (DegreeOfficePublicCandidacyHashCode) hashCode;
	    if (hash.isAssociatedWithEmailAndCandidacyProcess(email, processType, process)) {
		return hash;
	    }
	}

	return null;
    }

    static public DegreeOfficePublicCandidacyHashCode getPublicCandidacyHashCodeByEmailAndCandidacyProcessTypeOrNotAssociated(
	    final String email, Class<? extends IndividualCandidacyProcess> processType, CandidacyProcess process) {

	DegreeOfficePublicCandidacyHashCode associatedHashCode = getPublicCandidacyHashCodeByEmailAndCandidacyProcessType(email,
		processType, process);

	if (associatedHashCode != null) {
	    return associatedHashCode;
	}

	for (final PublicCandidacyHashCode hashCode : getHashCodesAssociatedWithEmail(email)) {
	    if (hashCode.isFromDegreeOffice() && !hashCode.hasCandidacyProcess())
		return (DegreeOfficePublicCandidacyHashCode) hashCode;
	}

	return null;
    }

}
