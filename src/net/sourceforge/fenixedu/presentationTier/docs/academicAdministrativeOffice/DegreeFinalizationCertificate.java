package net.sourceforge.fenixedu.presentationTier.docs.academicAdministrativeOffice;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.ResourceBundle;

import net.sourceforge.fenixedu.domain.Enrolment;
import net.sourceforge.fenixedu.domain.EnrolmentEvaluation;
import net.sourceforge.fenixedu.domain.serviceRequests.documentRequests.DegreeFinalizationCertificateRequest;
import net.sourceforge.fenixedu.domain.serviceRequests.documentRequests.DocumentRequest;
import net.sourceforge.fenixedu.domain.student.Registration;
import net.sourceforge.fenixedu.util.LanguageUtils;
import net.sourceforge.fenixedu.util.StringUtils;

public class DegreeFinalizationCertificate extends AdministrativeOfficeDocument {

    private static final ResourceBundle enumerationBundle = ResourceBundle.getBundle(
	    "resources.EnumerationResources", LanguageUtils.getLocale());

    protected DegreeFinalizationCertificate(final DocumentRequest documentRequest) {
	super(documentRequest);
    }

    @Override
    protected void fillReport() {
	super.fillReport();

	final DegreeFinalizationCertificateRequest degreeFinalizationCertificateRequest = (DegreeFinalizationCertificateRequest) getDocumentRequest();
	final Registration registration = degreeFinalizationCertificateRequest.getRegistration();

	parameters.put("degreeFinalizationDate", registration.getConclusionDate().toString(
		"dd 'de' MMMM 'de' yyyy", LanguageUtils.getLocale()));

	final StringBuilder degreeFinalizationGrade = new StringBuilder();
	if (degreeFinalizationCertificateRequest.getAverage()) {
	    final Integer finalAverage = registration.getFinalAverage();
	    degreeFinalizationGrade.append(" ").append(resourceBundle.getString("documents.registration.final.arithmetic.mean"));
	    degreeFinalizationGrade.append(" ").append(finalAverage);
	    degreeFinalizationGrade.append(" (").append(enumerationBundle.getString(finalAverage.toString()));
	    degreeFinalizationGrade.append(") ").append(resourceBundle.getString("values"));
	} 
	parameters.put("degreeFinalizationGrade", degreeFinalizationGrade.toString());

	final StringBuilder degreeFinalizationInfo = new StringBuilder();
	if (degreeFinalizationCertificateRequest.getDetailed()) {
	    degreeFinalizationInfo.append(resourceBundle.getString("documents.registration.approved.enrolments.info")).append(":\n");
	    
	    final List<Enrolment> approvedEnrolments = new ArrayList<Enrolment>(registration.getApprovedEnrolments());
	    Collections.sort(approvedEnrolments, Enrolment.COMPARATOR_BY_EXECUTION_PERIOD_AND_NAME_AND_ID);

	    for (final Enrolment approvedEnrolment : approvedEnrolments) {
		final EnrolmentEvaluation latestEnrolmentEvaluation = approvedEnrolment.getLatestEnrolmentEvaluation();
		
		final StringBuilder gradeInfo = new StringBuilder();
		gradeInfo.append(" ").append(resourceBundle.getString("label.with"));
		gradeInfo.append(" ").append(latestEnrolmentEvaluation.getGrade());
		gradeInfo.append(
			StringUtils.rightPad(
				"("
				+ enumerationBundle.getString(latestEnrolmentEvaluation.getGrade()) 
				+ ")", 
				11, 
				' '));
		gradeInfo.append(resourceBundle.getString("values"));

		degreeFinalizationInfo.append(
			StringUtils.multipleLineRightPadWithSuffix(
				approvedEnrolment.getName().getContent().toUpperCase(), 
				LINE_LENGTH,
				'-', 
				gradeInfo.toString())).append("\n");
	    }
	    
	    degreeFinalizationInfo.append(StringUtils.multipleLineRightPad(
		    org.apache.commons.lang.StringUtils.EMPTY,
		    LINE_LENGTH, 
		    '-')).append("\n");
	}
	parameters.put("degreeFinalizationInfo", degreeFinalizationInfo.toString());

    }

}

