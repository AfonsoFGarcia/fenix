package net.sourceforge.fenixedu.presentationTier.docs.academicAdministrativeOffice;

import java.util.Collection;
import java.util.TreeSet;

import net.sourceforge.fenixedu.domain.Employee;
import net.sourceforge.fenixedu.domain.Enrolment;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.organizationalStructure.UniversityUnit;
import net.sourceforge.fenixedu.domain.serviceRequests.documentRequests.DocumentRequest;
import net.sourceforge.fenixedu.domain.serviceRequests.documentRequests.ExtraCurricularCertificateRequest;
import net.sourceforge.fenixedu.injectionCode.AccessControl;
import net.sourceforge.fenixedu.util.StringUtils;

import org.joda.time.LocalDate;

public class ExtraCurricularCertificateRequestDocument extends AdministrativeOfficeDocument {

    protected ExtraCurricularCertificateRequestDocument(final DocumentRequest documentRequest) {
	super(documentRequest);
    }

    private static final long serialVersionUID = 1L;

    @Override
    protected ExtraCurricularCertificateRequest getDocumentRequest() {
	return (ExtraCurricularCertificateRequest) super.getDocumentRequest();
    }

    @Override
    protected void fillReport() {
	super.fillReport();
	addParameter("enrolmentsInfo", getEnrolmentsInfo());
    }
    
    final private String getEnrolmentsInfo() {
	final StringBuilder result = new StringBuilder();
	ExtraCurricularCertificateRequest request = getDocumentRequest();

	final Collection<Enrolment> enrolments = new TreeSet<Enrolment>(Enrolment.COMPARATOR_BY_EXECUTION_YEAR_AND_NAME_AND_ID);
	enrolments.addAll(request.getEnrolments());

	for (final Enrolment enrolment : enrolments) {
	    result.append(
			StringUtils.multipleLineRightPadWithSuffix(getPresentationNameFor(enrolment).toUpperCase(), LINE_LENGTH,
				END_CHAR, getCreditsInfo(enrolment))).append(LINE_BREAK);
	}

	result.append(generateEndLine());

	return result.toString();
    }
    
    final private String getCreditsInfo(final Enrolment enrolment) {
	final StringBuilder result = new StringBuilder();

	if (getDocumentRequest().isToShowCredits()) {
	    result.append(enrolment.getCurricularCourse().getEctsCredits(enrolment.getExecutionPeriod()).toString()).append(
		    getCreditsDescription());
	}

	return result.toString();
    }
}
