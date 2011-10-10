package net.sourceforge.fenixedu.applicationTier.Servico.serviceRequests;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import net.sourceforge.fenixedu._development.PropertiesManager;
import net.sourceforge.fenixedu.applicationTier.FenixService;
import net.sourceforge.fenixedu.domain.CurricularCourse;
import net.sourceforge.fenixedu.domain.Enrolment;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.File;
import net.sourceforge.fenixedu.domain.administrativeOffice.AdministrativeOfficeType;
import net.sourceforge.fenixedu.domain.candidacyProcess.IndividualCandidacy;
import net.sourceforge.fenixedu.domain.candidacyProcess.IndividualCandidacyDocumentFile;
import net.sourceforge.fenixedu.domain.degreeStructure.Context;
import net.sourceforge.fenixedu.domain.degreeStructure.CourseGroup;
import net.sourceforge.fenixedu.domain.degreeStructure.DegreeModule;
import net.sourceforge.fenixedu.domain.degreeStructure.RootCourseGroup;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.serviceRequests.AcademicServiceRequest;
import net.sourceforge.fenixedu.domain.serviceRequests.EquivalencePlanRequest;
import net.sourceforge.fenixedu.domain.serviceRequests.RegistrationAcademicServiceRequest;
import net.sourceforge.fenixedu.domain.student.Registration;
import net.sourceforge.fenixedu.presentationTier.docs.academicAdministrativeOffice.ApprovementInfoForEquivalenceProcess;
import net.sourceforge.fenixedu.util.StringUtils;

import org.apache.log4j.Logger;
import org.apache.util.Base64;
import org.joda.time.YearMonthDay;
import org.restlet.Client;
import org.restlet.Request;
import org.restlet.Response;
import org.restlet.data.Method;
import org.restlet.data.Parameter;
import org.restlet.data.Protocol;
import org.restlet.data.Reference;
import org.restlet.engine.security.SslContextFactory;
import org.restlet.representation.InputRepresentation;
import org.restlet.util.Series;

import pt.ist.fenixWebFramework.security.UserView;
import pt.ist.fenixWebFramework.security.accessControl.Checked;
import pt.ist.fenixWebFramework.services.Service;
import pt.utl.ist.fenix.tools.util.excel.StyledExcelSpreadsheet;
import pt.utl.ist.fenix.tools.util.i18n.Language;

public class SendAcademicServiceRequestToExternalEntity extends FenixService {

    private static final Logger logger = Logger.getLogger(SendAcademicServiceRequestToExternalEntity.class);

    private static final String COURSE_LABEL = "Nome da disciplina do curr�culo de Bolonha";
    private static final String COURSE_ECTS = "ECTS";
    private static final String EQUIVALENT_COURSE_LABEL = "Nome da(s) disciplina(s) considerada(s) equivalente(s)";
    private static final String GRADE_LABEL = "Classifica��o";
    private static final String MEC2006 = "MEC 2006"; //remove after when the process is open to all degrees    

    @Checked("RolePredicates.ACADEMIC_ADMINISTRATIVE_OFFICE_PREDICATE")
    @Service
    public static void run(final AcademicServiceRequest academicServiceRequest, final YearMonthDay sendDate,
	    final String justification) {

	academicServiceRequest.sendToExternalEntity(sendDate, justification);

	if (academicServiceRequest instanceof EquivalencePlanRequest) {
	    try {
		sendRequestDataToExternal(academicServiceRequest);
	    } catch (KeyManagementException e) {
		throw new DomainException(e.getMessage(), e);
	    } catch (NoSuchAlgorithmException e) {
		throw new DomainException(e.getMessage(), e);
	    } catch (KeyStoreException e) {
		throw new DomainException(e.getMessage(), e);
	    }
	}
    }

    private static void sendRequestDataToExternal(final AcademicServiceRequest academicServiceRequest)
	    throws NoSuchAlgorithmException, KeyManagementException, KeyStoreException {
	final Registration registration = ((RegistrationAcademicServiceRequest) academicServiceRequest).getRegistration();
	final ExecutionYear executionYear = ((RegistrationAcademicServiceRequest) academicServiceRequest).getExecutionYear();

	if (MEC2006.equals(registration.getDegreeCurricularPlanName())) { //registration.hasIndividualCandidacy() && 
	    final IndividualCandidacy individualCandidacy = registration.getIndividualCandidacy();
	    final ResourceBundle bundle = ResourceBundle.getBundle("resources.AcademicAdminOffice", Language.getLocale());

	    final StringBuilder studentData = new StringBuilder();
	    registration.exportValues(studentData);
	    if (individualCandidacy != null) {
		individualCandidacy.exportValues(studentData);
	    }

	    final StringBuilder studentGrades = new StringBuilder();
	    for (final Registration otherRegistration : registration.getStudent().getRegistrations()) {
		if (otherRegistration.getDegreeType().getAdministrativeOfficeType() == AdministrativeOfficeType.DEGREE) {
		    String approvementsInfo = ApprovementInfoForEquivalenceProcess.getApprovementsInfo(otherRegistration);
		    if (!StringUtils.isEmpty(approvementsInfo.trim())) {
			studentGrades.append("\n\n\n");
			studentGrades.append(bundle.getString("label.degree"));
			studentGrades.append(" : ");
			studentGrades.append(otherRegistration.getDegree().getPresentationName());
			studentGrades.append("\n\n");
			studentGrades.append(approvementsInfo);
		    }
		}
	    }

	    logger.debug(studentData.toString());
	    logger.debug(studentGrades.toString());

	    List<IndividualCandidacyDocumentFile> candidacyDocuments = individualCandidacy != null ? individualCandidacy
		    .getDocuments() : Collections.EMPTY_LIST;
	    final ByteArrayOutputStream resultStream = buildDocumentsStream(candidacyDocuments, studentData.toString(),
		    studentGrades.toString(), registration, executionYear);

	    final InputRepresentation ir = new InputRepresentation(new ByteArrayInputStream(resultStream.toByteArray()));

	    MessageDigest messageDigest = MessageDigest.getInstance("MD5");
	    byte[] hashedSecret = messageDigest.digest(PropertiesManager.getProperty(
		    "external.application.workflow.equivalences.uri.secret").getBytes());

	    final Reference reference = new Reference(PropertiesManager
		    .getProperty("external.application.workflow.equivalences.uri")
		    + academicServiceRequest.getServiceRequestNumber()).addQueryParameter("creator",
		    UserView.getUser().getUsername()).addQueryParameter("requestor", registration.getPerson().getUsername())
		    .addQueryParameter("base64Secret", new String(Base64.encode(hashedSecret)));

	    TrustManager[] trustAllCerts = new TrustManager[] { new X509TrustManager() {

		@Override
		public X509Certificate[] getAcceptedIssuers() {
		    return null;
		}

		@Override
		public void checkServerTrusted(X509Certificate[] arg0, String arg1) throws CertificateException {
		}

		@Override
		public void checkClientTrusted(X509Certificate[] arg0, String arg1) throws CertificateException {
		}
	    } };

	    // Install the all-trusting trust manager
	    final SSLContext sc = SSLContext.getInstance("SSL");
	    sc.init(null, trustAllCerts, new java.security.SecureRandom());

	    Client client = new Client(Protocol.HTTPS);
	    client.setContext(new org.restlet.Context());
	    client.getContext().getAttributes().put("sslContextFactory", new SslContextFactory() {
		@Override
		public SSLContext createSslContext() throws Exception {
		    return sc;
		}

		@Override
		public void init(Series<Parameter> parameters) {
		}
	    });

	    Request request = new Request(Method.POST, reference, ir);
	    final Response response = client.handle(request);

	    if (response.getStatus().getCode() != 200) {
		throw new DomainException(response.getStatus().getThrowable() != null ? response.getStatus().getThrowable()
			.getMessage() : "error.equivalence.externalEntity");
	    }
	}

    }

    private static ByteArrayOutputStream buildDocumentsStream(final Collection<? extends File> files,
	    final String candidacyResume, final String studentGrades, Registration registration, ExecutionYear executionYear) {
	ByteArrayOutputStream resultStream = new ByteArrayOutputStream();

	Set<String> fileNames = new HashSet<String>();

	try {
	    ZipOutputStream out = new ZipOutputStream(resultStream);

	    out.putNextEntry(new ZipEntry("candidacyResume.txt"));
	    out.write(candidacyResume.getBytes());
	    out.closeEntry();

	    out.putNextEntry(new ZipEntry("studentApprovements.txt"));
	    out.write(studentGrades.getBytes());
	    out.closeEntry();

	    for (final File file : files) {
		String filename = file.getFilename();
		while (fileNames.contains(filename)) {
		    filename = filename + "_";
		}
		fileNames.add(filename);
		out.putNextEntry(new ZipEntry(filename + ".pdf"));
		//if (file.hasLocalContent()) {
		out.write(file.getContents());
		//		} else {
		//		    final byte[] content = FileUtils.readFileInBytes("/home/rcro/Documents/resultados-quc.html");
		//		    out.write(content);
		//		}
		out.closeEntry();
	    }

	    if (registration.getActiveDegreeCurricularPlan().hasRoot()) {
		StyledExcelSpreadsheet spreadsheet = new StyledExcelSpreadsheet("Disciplinas");
		RootCourseGroup rootGroup = registration.getActiveDegreeCurricularPlan().getRoot();
		buildHeaderForCurricularGroupsFile(registration, spreadsheet, executionYear);
		buildCurricularCoursesGroups(executionYear, rootGroup
			.getSortedChildContextsWithCourseGroupsByExecutionYear(executionYear), spreadsheet);

		out.putNextEntry(new ZipEntry("plano_de_estudos.xls"));
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		spreadsheet.getWorkbook().write(outputStream);
		out.write(outputStream.toByteArray());
		out.closeEntry();
	    }

	    for (Registration otherRegistration : registration.getStudent().getRegistrationsSet()) {
		if (otherRegistration != registration) {
		    Collection<Enrolment> approvedEnrolments = otherRegistration.getApprovedEnrolments();
		    if (approvedEnrolments.isEmpty()) {
			continue;
		    }
		    StyledExcelSpreadsheet spreadsheet = new StyledExcelSpreadsheet("Disciplinas");
		    buildHeaderForOtherRegistrationFile(otherRegistration, spreadsheet);
		    buildCurricularCourses(approvedEnrolments, spreadsheet);

		    out.putNextEntry(new ZipEntry("cadeiras_concluidas_"
			    + otherRegistration.getLastStudentCurricularPlan().getDegreeCurricularPlan().getName() + ".xls"));
		    ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		    spreadsheet.getWorkbook().write(outputStream);
		    out.write(outputStream.toByteArray());
		    out.closeEntry();
		}
	    }

	    out.close();

	} catch (IOException e) {
	    e.printStackTrace();
	}

	return resultStream;
    }

    private static void buildCurricularCourses(Collection<Enrolment> approvedEnrolments, StyledExcelSpreadsheet spreadsheet) {
	for (Enrolment enrolment : approvedEnrolments) {
	    spreadsheet.newRow();
	    spreadsheet.addCell(enrolment.getCurricularCourse().getName());
	    spreadsheet.addCell(enrolment.getEctsCredits());
	    spreadsheet.addCell(enrolment.getGrade().getNumericValue());
	}
    }

    private static void buildHeaderForOtherRegistrationFile(Registration registration, StyledExcelSpreadsheet spreadsheet) {
	spreadsheet.newRow();
	String degreeNameAndYear = registration.getLastDegreeCurricularPlan().getDegree().getNameI18N().toString();
	spreadsheet.addCell(degreeNameAndYear, spreadsheet.getExcelStyle().getTitleStyle());
	spreadsheet.newRow();
	String studentNameAndNumber = registration.getPerson().getName() + " - N�" + registration.getStudent().getNumber();
	spreadsheet.addCell(studentNameAndNumber);
	spreadsheet.newRow();
	spreadsheet.addCell(COURSE_LABEL, spreadsheet.getExcelStyle().getTitleStyle());
	spreadsheet.addCell(COURSE_ECTS, spreadsheet.getExcelStyle().getTitleStyle());
	spreadsheet.addCell(GRADE_LABEL, spreadsheet.getExcelStyle().getTitleStyle());
    }

    private static void buildHeaderForCurricularGroupsFile(Registration registration, StyledExcelSpreadsheet spreadsheet,
	    ExecutionYear executionYear) {
	spreadsheet.newRow();
	String degreeNameAndYear = registration.getActiveDegreeCurricularPlan().getDegree().getNameI18N().toString() + " - "
		+ executionYear.getName();
	spreadsheet.addCell(degreeNameAndYear, spreadsheet.getExcelStyle().getTitleStyle());
	spreadsheet.newRow();
	String studentNameAndNumber = registration.getPerson().getName() + " - N�" + registration.getStudent().getNumber();
	spreadsheet.addCell(studentNameAndNumber);
	spreadsheet.newRow();
	spreadsheet.addCell(COURSE_LABEL, spreadsheet.getExcelStyle().getTitleStyle());
	spreadsheet.addCell(COURSE_ECTS, spreadsheet.getExcelStyle().getTitleStyle());
	spreadsheet.addCell(EQUIVALENT_COURSE_LABEL, spreadsheet.getExcelStyle().getTitleStyle());
	spreadsheet.addCell(GRADE_LABEL, spreadsheet.getExcelStyle().getTitleStyle());
    }

    private static void buildCurricularCoursesGroups(ExecutionYear executionYear, List<Context> sortedContexts,
	    StyledExcelSpreadsheet spreadsheet) {
	for (Context context : sortedContexts) {
	    CourseGroup childDegreeModule = (CourseGroup) context.getChildDegreeModule();
	    spreadsheet.newRow();
	    spreadsheet.addCell(childDegreeModule.getName(), spreadsheet.getExcelStyle().getTitleStyle());
	    List<Context> sortedCurricularContexts = childDegreeModule
		    .getSortedChildContextsWithCurricularCoursesByExecutionYear(executionYear);
	    if (sortedContexts.size() > 1) {
		for (Context courseContext : sortedCurricularContexts) {
		    DegreeModule courseModule = courseContext.getChildDegreeModule();
		    spreadsheet.newRow();
		    spreadsheet.addCell(courseModule.getName());
		    spreadsheet.addCell(((CurricularCourse) courseModule).getEctsCredits());
		}
	    }
	    List<Context> sortedGroupContexts = childDegreeModule
		    .getSortedChildContextsWithCourseGroupsByExecutionYear(executionYear);
	    if (sortedGroupContexts.size() > 0) {
		buildCurricularCoursesGroups(executionYear, sortedGroupContexts, spreadsheet);
	    }
	}
    }
}