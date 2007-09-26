package net.sourceforge.fenixedu.presentationTier.docs.academicAdministrativeOffice;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Map.Entry;

import net.sourceforge.fenixedu.domain.Degree;
import net.sourceforge.fenixedu.domain.DomainReference;
import net.sourceforge.fenixedu.domain.Employee;
import net.sourceforge.fenixedu.domain.Enrolment;
import net.sourceforge.fenixedu.domain.IEnrolment;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.accounting.PostingRule;
import net.sourceforge.fenixedu.domain.accounting.postingRules.serviceRequests.CertificateRequestPR;
import net.sourceforge.fenixedu.domain.accounting.serviceAgreementTemplates.AdministrativeOfficeServiceAgreementTemplate;
import net.sourceforge.fenixedu.domain.degree.DegreeType;
import net.sourceforge.fenixedu.domain.degreeStructure.CycleType;
import net.sourceforge.fenixedu.domain.organizationalStructure.Unit;
import net.sourceforge.fenixedu.domain.organizationalStructure.UniversityUnit;
import net.sourceforge.fenixedu.domain.serviceRequests.documentRequests.CertificateRequest;
import net.sourceforge.fenixedu.domain.serviceRequests.documentRequests.DocumentRequest;
import net.sourceforge.fenixedu.domain.student.MobilityProgram;
import net.sourceforge.fenixedu.domain.studentCurriculum.Substitution;
import net.sourceforge.fenixedu.injectionCode.AccessControl;
import net.sourceforge.fenixedu.presentationTier.docs.FenixReport;
import net.sourceforge.fenixedu.util.LanguageUtils;
import net.sourceforge.fenixedu.util.Money;
import net.sourceforge.fenixedu.util.StringUtils;

import org.joda.time.DateTime;
import org.joda.time.YearMonthDay;
import org.joda.time.format.DateTimeFormat;

public class AdministrativeOfficeDocument extends FenixReport {
    
    static final protected int LINE_LENGTH = 64;
    
    static final protected int SUFFIX_LENGTH = 12;
    
    static final protected String[] identifiers = {"*", "#", "+", "**", "***"};
    
    protected DomainReference<DocumentRequest> documentRequestDomainReference;

    public static class AdministrativeOfficeDocumentCreator {
	
	public static AdministrativeOfficeDocument create(final DocumentRequest documentRequest) {
	    switch (documentRequest.getDocumentRequestType()) {
	    case ENROLMENT_CERTIFICATE:
		return new EnrolmentCertificate(documentRequest);
	    case APPROVEMENT_CERTIFICATE:
		return new ApprovementCertificate(documentRequest);
	    case DEGREE_FINALIZATION_CERTIFICATE:
		return new DegreeFinalizationCertificate(documentRequest);
	    case SCHOOL_REGISTRATION_DECLARATION:
		return new RegistrationDeclaration(documentRequest);
	    case ENROLMENT_DECLARATION:
		return new EnrolmentDeclaration(documentRequest);
	    case IRS_DECLARATION:
		return new IRSDeclaration(documentRequest);
	    case DIPLOMA_REQUEST:
		return new Diploma(documentRequest);
	    default:
		return new AdministrativeOfficeDocument(documentRequest);
	    }
	}
	
    }
    
    protected AdministrativeOfficeDocument() {
    }
    
    protected AdministrativeOfficeDocument(final DocumentRequest documentRequest) {
	this.dataSource = new ArrayList();
	this.resourceBundle = ResourceBundle.getBundle("resources.AcademicAdminOffice", LanguageUtils.getLocale());
	this.documentRequestDomainReference = new DomainReference<DocumentRequest>(documentRequest);
	
	fillReport();
    }

    protected DocumentRequest getDocumentRequest() {
	return documentRequestDomainReference.getObject();
    }
    
    @Override
    public String getReportTemplateKey() {
	return getDocumentRequest().getDocumentTemplateKey();
    }
    
    @Override
    public String getReportFileName() {
	final StringBuilder result = new StringBuilder();

	result.append(getDocumentRequest().getRegistration().getPerson().getIstUsername());
	result.append("-");
	result.append(new DateTime().toString(DateTimeFormat.forPattern("yyyyMMdd")));
	result.append("-");
	result.append(getDocumentRequest().getDescription().replace(":", "").replace(" ", ""));

	return result.toString();
    }

    @Override
    protected void fillReport() {
	parameters.put("documentRequest", getDocumentRequest());
	parameters.put("registration", getDocumentRequest().getRegistration());

        if (getDocumentRequest().isCertificate()) {
            setPriceFields();
        }
        
        final Employee employee = AccessControl.getPerson().getEmployee();
        setIntroFields(employee);

        setPersonFields();
        if (getDocumentRequest().hasExecutionYear()) {
            parameters.put("situation", getDocumentRequest().getExecutionYear().containsDate(new DateTime()) ? "EST�" : "ESTEVE");
        }
        parameters.put("degreeDescription", getDegreeDescription());
        
	parameters.put("employeeLocation", employee.getCurrentCampus().getLocation());
	parameters.put("day", new YearMonthDay().toString("dd 'de' MMMM 'de' yyyy", LanguageUtils.getLocale()));
    }

    final private void setPriceFields() {
	final CertificateRequest certificateRequest = (CertificateRequest) getDocumentRequest();
	final CertificateRequestPR certificateRequestPR = (CertificateRequestPR) getPostingRule();
	
	final Money amountPerPage = certificateRequestPR.getAmountPerPage();
	final Money baseAmountPlusAmountForUnits = certificateRequestPR.getBaseAmount().add(certificateRequestPR.getAmountPerUnit().multiply(new BigDecimal(certificateRequest.getNumberOfUnits())));
	final Money urgencyAmount = certificateRequest.getUrgentRequest() ? certificateRequestPR.getBaseAmount() : Money.ZERO;
	
	parameters.put("amountPerPage", amountPerPage);
	parameters.put("baseAmountPlusAmountForUnits", baseAmountPlusAmountForUnits);
	parameters.put("urgencyAmount", urgencyAmount);
	parameters.put("isFree", certificateRequest.isFree());
    }
    
    final private PostingRule getPostingRule() {
	final AdministrativeOfficeServiceAgreementTemplate serviceAgreementTemplate = getDocumentRequest().getAdministrativeOffice().getServiceAgreementTemplate();
	return serviceAgreementTemplate.findPostingRuleByEventType(getDocumentRequest().getEventType());
    }
    
    final private void setIntroFields(final Employee employee) {
	parameters.put("administrativeOfficeCoordinator", employee.getCurrentWorkingPlace().getActiveUnitCoordinator());
	parameters.put("administrativeOfficeName", employee.getCurrentWorkingPlace().getName());

	parameters.put("institutionName", RootDomainObject.getInstance().getInstitutionUnit().getName());
	parameters.put("universityName", UniversityUnit.getInstitutionsUniversityUnit().getName());
    }

    final private void setPersonFields() {
	final Person person = getDocumentRequest().getRegistration().getPerson();
	final String name = person.getName().toUpperCase();
	parameters.put("name", StringUtils.multipleLineRightPad(name, LINE_LENGTH, '-'));
	
	final String documentIdNumber = person.getDocumentIdNumber();
	parameters.put("documentIdNumber", StringUtils.multipleLineRightPad("portador" + (person.isMale() ? "" : "a")  + " do " + person.getIdDocumentType().getLocalizedName() + " N� " + documentIdNumber, LINE_LENGTH, '-'));
	
	final String birthLocale = person.getParishOfBirth().toUpperCase() + ", " + person.getDistrictOfBirth().toUpperCase();
	parameters.put("birthLocale", StringUtils.multipleLineRightPad("natural de " + birthLocale, LINE_LENGTH, '-'));
	
	final String nationality = person.getCountry().getFilteredNationality().toUpperCase();
	parameters.put("nationality", StringUtils.multipleLineRightPad("de nacionalidade " + nationality, LINE_LENGTH, '-'));
    }

    final private String getDegreeDescription() {
	final StringBuilder result = new StringBuilder();
	
	final Degree degree = getDocumentRequest().getDegree();
	final DegreeType degreeType = degree.getDegreeType();
	switch (degreeType) {
	case BOLONHA_PHD_PROGRAM:
	    break;
	case BOLONHA_ADVANCED_FORMATION_DIPLOMA:
	    result.append("curso conducente ao ");
	    break;
	default:
	    result.append("curso de ");
	    break;
	}
	
	result.append(degreeType.getFilteredName().toUpperCase());
	result.append(" em ").append(degree.getFilteredName().toUpperCase());
	
	if (degreeType.isComposite()) {
	    final CycleType lastConcludedCycleType = getDocumentRequest().getRegistration().getLastConcludedCycleType();
	    if(lastConcludedCycleType != null){
		result.append(" (").append(lastConcludedCycleType.getDescription()).append(")");
	    }
	} else if (degreeType.hasExactlyOneCycleType()) {
	    result.append(" (").append(degreeType.getCycleType().getDescription()).append(")");
	}
	
	return result.toString();
    }
    
    final protected String getCreditsDescription() {
	return getDocumentRequest().getDegreeType() == DegreeType.MASTER_DEGREE ? " Cr�d." : " ECTS";
    }
    
    final protected String generateEndLine() {
	return StringUtils.rightPad(StringUtils.EMPTY, LINE_LENGTH, '-'); 
    }
    
    final protected String getEnrolmentName(final Map<Unit, String> academicUnitIdentifiers, final IEnrolment approvedIEnrolment) {
	StringBuilder result = new StringBuilder();
	
	if (approvedIEnrolment.isExternalEnrolment()) {
	    result.append(getAcademicUnitIdentifier(academicUnitIdentifiers, approvedIEnrolment.getAcademicUnit()));
	}
	
	result.append(approvedIEnrolment.getName().toUpperCase());
	
	return result.toString();
    }

    @SuppressWarnings("static-access")
    final protected String getAcademicUnitIdentifier(final Map<Unit, String> academicUnitIdentifiers, final Unit academicUnit) {
	if (!academicUnitIdentifiers.containsKey(academicUnit)) {
	    academicUnitIdentifiers.put(academicUnit, this.identifiers[academicUnitIdentifiers.size()]);
	} 
	
	return academicUnitIdentifiers.get(academicUnit);
    }

    final protected void getCreditsInfo(final StringBuilder result, final IEnrolment approvedIEnrolment) {
        final Substitution substitution = getDocumentRequest().getRegistration().getSubstitution(approvedIEnrolment);
    
        Double ectsCredits = null;
        if (substitution == null && approvedIEnrolment.isEnrolment()) {
            final Enrolment enrolment = (Enrolment) approvedIEnrolment;
            ectsCredits = enrolment.getCurricularCourse().getEctsCredits(enrolment.getExecutionPeriod());
        } else {
            ectsCredits = substitution.getGivenCredits();
        }
    
        if (ectsCredits != null) {
            result.append(ectsCredits).append(getCreditsDescription()).append(", ");
        }
    }
    
    final protected String getDismissalsEctsCreditsInfo() {
	final Double dismissalsEctsCreditsExceptApproved = getDocumentRequest().getRegistration().getDismissalsEctsCreditsExceptApproved();
	
	final StringBuilder result = new StringBuilder();
	if (dismissalsEctsCreditsExceptApproved != 0d) {
	    result.append("\n");
	    result.append(StringUtils.multipleLineRightPadWithSuffix("Cr�ditos por Dispensas:", LINE_LENGTH, '-', dismissalsEctsCreditsExceptApproved + getCreditsDescription()));
	    result.append("\n");
	}
	
	return result.toString();
    }

    final protected String getAcademicUnitInfo(final Map<Unit, String> academicUnitIdentifiers, final MobilityProgram mobilityProgram) {
	final StringBuilder result = new StringBuilder();
	
	for (final Entry<Unit,String> academicUnitIdentifier : academicUnitIdentifiers.entrySet()) {
	    final StringBuilder academicUnit = new StringBuilder();
	    
	    academicUnit.append(academicUnitIdentifier.getValue());
	    academicUnit.append(" ").append(resourceBundle.getString("documents.external.curricular.courses.program"));
	    academicUnit.append(" ").append(enumerationBundle.getString(mobilityProgram.getQualifiedName()).toUpperCase());
	    academicUnit.append(" ").append(resourceBundle.getString("in.feminine"));
	    academicUnit.append(" ").append(academicUnitIdentifier.getKey().getName().toUpperCase());
	    
	    result.append(StringUtils.multipleLineRightPad(academicUnit.toString(), LINE_LENGTH, '-') + "\n");
	}
	
	return result.toString();
    }

}
