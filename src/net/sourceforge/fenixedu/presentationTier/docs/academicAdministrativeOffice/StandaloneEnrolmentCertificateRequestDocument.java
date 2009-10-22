package net.sourceforge.fenixedu.presentationTier.docs.academicAdministrativeOffice;

import java.util.Collection;
import java.util.TreeSet;

import net.sourceforge.fenixedu.domain.Enrolment;
import net.sourceforge.fenixedu.domain.serviceRequests.documentRequests.DocumentRequest;
import net.sourceforge.fenixedu.domain.serviceRequests.documentRequests.StandaloneEnrolmentCertificateRequest;
import net.sourceforge.fenixedu.util.StringUtils;

public class StandaloneEnrolmentCertificateRequestDocument extends AdministrativeOfficeDocument {

    protected StandaloneEnrolmentCertificateRequestDocument(final DocumentRequest documentRequest) {
	super(documentRequest);
    }

    private static final long serialVersionUID = 1L;

    @Override
    protected StandaloneEnrolmentCertificateRequest getDocumentRequest() {
	return (StandaloneEnrolmentCertificateRequest) super.getDocumentRequest();
    }

    @Override
    protected void fillReport() {
	super.fillReport();
	addParameter("enrolmentsInfo", getEnrolmentsInfo());
    }

    @Override
    protected String getDegreeDescription() {
	return getRegistration().getDegreeDescription(null, getLocale());
    }

    final private String getEnrolmentsInfo() {
	final StringBuilder result = new StringBuilder();
	StandaloneEnrolmentCertificateRequest request = getDocumentRequest();

	final Collection<Enrolment> enrolments = new TreeSet<Enrolment>(Enrolment.COMPARATOR_BY_EXECUTION_YEAR_AND_NAME_AND_ID);
	enrolments.addAll(request.getEnrolments());

	for (final Enrolment enrolment : enrolments) {
	    result.append(
		    StringUtils.multipleLineRightPadWithSuffix(getPresentationNameFor(enrolment).toUpperCase(), LINE_LENGTH,
			    END_CHAR, getCreditsAndGradeInfo(enrolment, getExecutionYear()))).append(LINE_BREAK);
	}

	result.append(generateEndLine());

	return result.toString();
    }

}
