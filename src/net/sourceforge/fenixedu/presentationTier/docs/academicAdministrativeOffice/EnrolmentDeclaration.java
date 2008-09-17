package net.sourceforge.fenixedu.presentationTier.docs.academicAdministrativeOffice;

import java.util.List;

import net.sourceforge.fenixedu.domain.Enrolment;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.serviceRequests.documentRequests.DocumentPurposeType;
import net.sourceforge.fenixedu.domain.serviceRequests.documentRequests.DocumentRequest;
import net.sourceforge.fenixedu.domain.serviceRequests.documentRequests.EnrolmentDeclarationRequest;
import net.sourceforge.fenixedu.domain.student.Registration;

import org.apache.commons.lang.StringUtils;

public class EnrolmentDeclaration extends AdministrativeOfficeDocument {

    protected EnrolmentDeclaration(final DocumentRequest documentRequest) {
	super(documentRequest);
    }

    @Override
    protected void fillReport() {
	super.fillReport();

	addParameter("curricularYear", getCurricularYear());

	final List<Enrolment> enrolments = (List<Enrolment>) getRegistration().getEnrolments(
		getDocumentRequest().getExecutionYear());
	addParameter("numberEnrolments", Integer.valueOf(enrolments.size()));
	addParameter("approvementInfo", getApprovementInfo());
	addParameter("documentPurpose", getDocumentPurpose());
    }

    @Override
    protected String getDegreeDescription() {
	final Registration registration = getRegistration();
	return registration.getDegreeType().isComposite() ? registration.getDegreeDescription(null) : super
		.getDegreeDescription();
    }

    final private String getCurricularYear() {
	final StringBuilder result = new StringBuilder();

	if (!getDocumentRequest().getDegreeType().hasExactlyOneCurricularYear()) {
	    final Integer curricularYear = Integer.valueOf(getRegistration().getCurricularYear(
		    getDocumentRequest().getExecutionYear()));

	    result.append(getEnumerationBundle().getString(curricularYear.toString() + ".ordinal").toUpperCase());
	    result.append(" ano curricular, do ");
	}

	return result.toString();
    }

    final private String getApprovementInfo() {
	final StringBuilder result = new StringBuilder();

	final EnrolmentDeclarationRequest enrolmentDeclarationRequest = (EnrolmentDeclarationRequest) getDocumentRequest();

	if (enrolmentDeclarationRequest.getDocumentPurposeType() == DocumentPurposeType.PPRE) {
	    final Registration registration = getRegistration();
	    final ExecutionYear executionYear = enrolmentDeclarationRequest.getExecutionYear();
	    final boolean transition = registration.isTransition(executionYear);

	    if (registration.isFirstTime(executionYear) && !transition) {
		result.append(", pela 1� vez");
	    } else {
		final Registration registrationToInspect = transition ? registration.getSourceRegistration() : registration;
		if (registrationToInspect.hasApprovement(executionYear.getPreviousExecutionYear())) {
		    result.append(" e teve aproveitamento no ano lectivo " + executionYear.getPreviousExecutionYear().getYear());
		} else {
		    result.append(" e n�o teve aproveitamento no ano lectivo "
			    + executionYear.getPreviousExecutionYear().getYear());
		}
	    }
	}
	return result.toString();
    }

    final private String getDocumentPurpose() {
	final StringBuilder result = new StringBuilder();

	final EnrolmentDeclarationRequest enrolmentDeclarationRequest = (EnrolmentDeclarationRequest) getDocumentRequest();

	if (enrolmentDeclarationRequest.getDocumentPurposeType() != null) {
	    result.append(getResourceBundle().getString("documents.declaration.valid.purpose")).append(SINGLE_SPACE);
	    if (enrolmentDeclarationRequest.getDocumentPurposeType() == DocumentPurposeType.OTHER
		    && !StringUtils.isEmpty(enrolmentDeclarationRequest.getOtherDocumentPurposeTypeDescription())) {
		result.append(enrolmentDeclarationRequest.getOtherDocumentPurposeTypeDescription().toUpperCase());
	    } else {
		result.append(getEnumerationBundle().getString(enrolmentDeclarationRequest.getDocumentPurposeType().name())
			.toUpperCase());
	    }
	    result.append(".");
	}

	return result.toString();
    }

}
