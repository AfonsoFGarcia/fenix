package net.sourceforge.fenixedu.applicationTier.Servico.serviceRequests;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.Charset;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Formatter;
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
import net.sourceforge.fenixedu.domain.degree.DegreeType;
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

import org.apache.commons.beanutils.BeanComparator;
import org.apache.commons.lang.CharEncoding;
import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.util.CellRangeAddress;
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

    private static final String COURSE_LABEL = "Nome da disciplina do currículo de Bolonha";
    private static final String COURSE_ECTS = "ECTS";
    private static final String EQUIVALENT_COURSE_LABEL = "Nome da(s) disciplina(s) considerada(s) equivalente(s)";
    private static final String GRADE_LABEL = "Classificação";
    private static final String GRADE_SCALE = "Escala";
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
	//joantune: to everybody! guys, WTF, if you did a little sprinkle of comments it wouldn't hurt you
	final Registration registration = ((RegistrationAcademicServiceRequest) academicServiceRequest).getRegistration();
	final ExecutionYear executionYear = ((RegistrationAcademicServiceRequest) academicServiceRequest).getExecutionYear();

	if (MEC2006.equals(registration.getDegreeCurricularPlanName())) { //registration.hasIndividualCandidacy() && 
	    final IndividualCandidacy individualCandidacy = registration.getIndividualCandidacy();
	    final ResourceBundle bundle = ResourceBundle.getBundle("resources.AcademicAdminOffice", Language.getLocale());

	    final StringBuilder studentData = new StringBuilder();
	    registration.exportValues(studentData);
	    if (individualCandidacy != null) {
		individualCandidacy.exportValues(studentData);
	    } else {
		exportValues(registration, bundle, studentData);
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
		    studentGrades.toString(), registration, executionYear, ((EquivalencePlanRequest) academicServiceRequest)
			    .getNumberOfEquivalences());

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

	    Client client = null;
	    try {
		client = new Client(Protocol.HTTPS);
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
		    String exceptionString = response.getStatus().getThrowable() != null ? response.getStatus().getThrowable()
			    .getMessage() : "error.equivalence.externalEntity";
		    throw new DomainException(exceptionString);
		}
	    } finally {
		try {
		    org.restlet.Context.setCurrent(null);
		    Response.setCurrent(null);
		    if (client != null)
			client.stop();
		} catch (Exception e) {
		    // Cannot stop the client, this WILL cause a memory leak!
		    e.printStackTrace();
		}
	    }
	}

    }

    private static void exportValues(final Registration registration, final ResourceBundle bundle, final StringBuilder studentData) {
	final ResourceBundle applicationBundle = ResourceBundle.getBundle("resources.ApplicationResources", Language.getLocale());
	List<Registration> registrations = new ArrayList<Registration>();
	for (final Registration otherRegistration : registration.getStudent().getRegistrations()) {
	    if (otherRegistration != registration && !DegreeType.EMPTY.equals(otherRegistration.getDegreeType())) {
		registrations.add(otherRegistration);
	    }
	}
	Registration previousRegistration = registrations.isEmpty() ? null : (Registration) Collections.max(registrations,
		Registration.COMPARATOR_BY_START_DATE);

	if (previousRegistration != null) {
	    Formatter formatter = new Formatter(studentData);
	    formatter.format("%s: %s\n", bundle.getString("label.SecondCycleIndividualCandidacy.previous.degree"),
		    previousRegistration.getDegree().getPresentationName());
	    formatter.format("%s: %s\n", applicationBundle.getString("label.candidacy.numberOfApprovedCurricularCourses"),
		    previousRegistration.getCurricularCoursesApprovedByEnrolment().size());
	}
    }

    private static ByteArrayOutputStream buildDocumentsStream(final Collection<? extends File> files,
	    final String candidacyResume, final String studentGrades, Registration registration, ExecutionYear executionYear,
	    Integer numberOfEquivalencesRequested) {
	ByteArrayOutputStream resultStream = new ByteArrayOutputStream();

	Set<String> fileNames = new HashSet<String>();

	try {
	    ZipOutputStream out = new ZipOutputStream(resultStream);
	    String candidacyResumeEndLine = candidacyResume.replaceAll("\n", "\r\n");
	    out.putNextEntry(new ZipEntry("candidacyResume.txt"));
	    out.write(candidacyResumeEndLine.getBytes(Charset.forName(CharEncoding.UTF_8)));
	    out.closeEntry();

	    String studentGradesEndLine = studentGrades.replaceAll("\n", "\r\n");
	    out.putNextEntry(new ZipEntry("studentApprovements.txt"));
	    out.write(studentGradesEndLine.getBytes(Charset.forName(CharEncoding.UTF_8)));
	    out.closeEntry();

	    for (final File file : files) {
		String filename = file.getFilename();
		while (fileNames.contains(filename)) {
		    filename = filename + "_";
		}
		fileNames.add(filename);
		out.putNextEntry(new ZipEntry(filename));
		//if (file.hasLocalContent()) {
		out.write(file.getContents());
		//		} else {
		//		    final byte[] content = FileUtils.readFileInBytes("/tmp/tmp.tmp");
		//		    out.write(content);
		//		}
		out.closeEntry();
	    }

	    //joantune: here we are exporting the curricular plan of the active degree (didn't felt pain! at all!)
	    // when the origin degree registration is not active we must get the lastStudentCurricularPlan, 
	    // wich is also true for the cases where the registration is active
	    if (registration.getLastStudentCurricularPlan().hasRoot()) {

		StyledExcelSpreadsheet spreadsheet = new StyledExcelSpreadsheet("Disciplinas");

		//let's put it in the portrait position

		spreadsheet.setSheetOrientation();

		//let's create the shaded and unshaded styles to write the main content of the data
		//on even lines and odd lines
		HSSFFont normalFont = spreadsheet.getWorkbook().createFont();
		// let's make it an 10 ptr
		normalFont.setFontHeightInPoints((short) 10);

		HSSFCellStyle shadedNormalTextAndFont = spreadsheet.getWorkbook().createCellStyle();
		shadedNormalTextAndFont.setFont(normalFont);
		HSSFCellStyle unshadedNormalTextAndFont = spreadsheet.getWorkbook().createCellStyle();
		unshadedNormalTextAndFont.cloneStyleFrom(shadedNormalTextAndFont);
		//let's shade it
		shadedNormalTextAndFont.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
		shadedNormalTextAndFont.setFillForegroundColor(HSSFColor.GREY_25_PERCENT.index);
		HSSFCellStyle shadedUnlockedNormalTextAndFont = spreadsheet.getWorkbook().createCellStyle();
		shadedUnlockedNormalTextAndFont.cloneStyleFrom(shadedNormalTextAndFont);
		shadedUnlockedNormalTextAndFont.setLocked(false);
		HSSFCellStyle unshadedUnlockedNormalTextAndFont = spreadsheet.getWorkbook().createCellStyle();
		unshadedUnlockedNormalTextAndFont.cloneStyleFrom(unshadedNormalTextAndFont);
		unshadedUnlockedNormalTextAndFont.setLocked(false);

		//the style to be used in the headers
		HSSFFont biggerFont = spreadsheet.getWorkbook().createFont();
		// let's make it a 10 ptr
		biggerFont.setFontHeightInPoints((short) 10);
		biggerFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);

		//let's 'lock' the written data
		HSSFCellStyle titleStyle = spreadsheet.getExcelStyle().getTitleStyle();
		titleStyle.setFont(biggerFont);
		titleStyle.setLocked(true);

		//also, let's make sure that it has some sort of borders *no pain here as well!!*
		//		titleStyle.setBorderBottom(HSSFCellStyle.BORDER_THIN);
		//		titleStyle.setBorderTop(HSSFCellStyle.BORDER_THIN);
		//		titleStyle.setBorderRight(HSSFCellStyle.BORDER_THIN);
		//		titleStyle.setBorderLeft(HSSFCellStyle.BORDER_THIN);

		RootCourseGroup rootGroup = registration.getLastDegreeCurricularPlan().getRoot();
		buildHeaderForCurricularGroupsFile(registration, spreadsheet, executionYear);
		buildCurricularCoursesGroups(executionYear, rootGroup
			.getSortedChildContextsWithCourseGroupsByExecutionYear(executionYear), spreadsheet,
			shadedNormalTextAndFont, unshadedNormalTextAndFont, shadedUnlockedNormalTextAndFont,
			unshadedUnlockedNormalTextAndFont);

		//let's write the last lines 
		HSSFSheet hssfSheet = spreadsheet.getSheet();
		spreadsheet.newRow();
		spreadsheet.newRow();
		spreadsheet.addCell("Número de cadeiras pedidas:", unshadedUnlockedNormalTextAndFont);
		spreadsheet.addCell(numberOfEquivalencesRequested, unshadedNormalTextAndFont);

		spreadsheet.newRow();
		spreadsheet.addCell("Número de equivalências dadas:", unshadedUnlockedNormalTextAndFont);
		spreadsheet.addCell(StringUtils.EMPTY, unshadedNormalTextAndFont);
		spreadsheet.newRow();
		spreadsheet
			.addCell(
				"Homologado pelo Conselho Científico ................................................................................................................. IstId: ........................ em ....../......../...........",
				unshadedNormalTextAndFont);
		hssfSheet.addMergedRegion(new CellRangeAddress(spreadsheet.getRow().getRowNum(),
			spreadsheet.getRow().getRowNum(), 0, 4));

		//let's resize all of the four columns
		spreadsheet.getSheet().autoSizeColumn(0);
		spreadsheet.getSheet().autoSizeColumn(1);
		spreadsheet.getSheet().autoSizeColumn(2);
		spreadsheet.getSheet().autoSizeColumn(3);

		//let's protect it!!
		spreadsheet.getSheet().protectSheet("");

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
		    final List<Enrolment> aprovedEnrolmentsList = new ArrayList<Enrolment>(approvedEnrolments);
		    Collections.sort(aprovedEnrolmentsList, new BeanComparator("curricularCourse.name"));

		    StyledExcelSpreadsheet spreadsheet = new StyledExcelSpreadsheet("Disciplinas");
		    buildHeaderForOtherRegistrationFile(otherRegistration, spreadsheet);
		    buildCurricularCourses(aprovedEnrolmentsList, spreadsheet);

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
	    spreadsheet.addCell(enrolment.getGrade().getGradeScale().getDescription());
	}
    }

    private static void buildHeaderForOtherRegistrationFile(Registration registration, StyledExcelSpreadsheet spreadsheet) {
	spreadsheet.newRow();
	String degreeNameAndYear = registration.getLastDegreeCurricularPlan().getDegree().getNameI18N().toString();
	spreadsheet.addCell(degreeNameAndYear, spreadsheet.getExcelStyle().getTitleStyle());
	spreadsheet.newRow();
	String studentNameAndNumber = registration.getPerson().getName() + " - Nº" + registration.getStudent().getNumber();
	spreadsheet.addCell(studentNameAndNumber);
	spreadsheet.newRow();
	spreadsheet.addCell(COURSE_LABEL, spreadsheet.getExcelStyle().getTitleStyle());
	spreadsheet.addCell(COURSE_ECTS, spreadsheet.getExcelStyle().getTitleStyle());
	spreadsheet.addCell(GRADE_LABEL, spreadsheet.getExcelStyle().getTitleStyle());
	spreadsheet.addCell(GRADE_SCALE, spreadsheet.getExcelStyle().getTitleStyle());
    }

    private static void buildHeaderForCurricularGroupsFile(Registration registration, StyledExcelSpreadsheet spreadsheet,
	    ExecutionYear executionYear) {
	spreadsheet.newRow();
	String degreeNameAndYear = registration.getLastDegreeCurricularPlan().getDegree().getNameI18N().toString() + " - "
		+ executionYear.getName();
	spreadsheet.addCell(degreeNameAndYear, spreadsheet.getExcelStyle().getTitleStyle());
	spreadsheet.newRow();
	String studentNameAndNumber = registration.getPerson().getName() + " - Nº" + registration.getStudent().getNumber();
	spreadsheet.addCell(studentNameAndNumber);
	spreadsheet.newRow();
	spreadsheet.addCell(COURSE_LABEL, spreadsheet.getExcelStyle().getTitleStyle());
	spreadsheet.addCell(COURSE_ECTS, spreadsheet.getExcelStyle().getTitleStyle());
	spreadsheet.addCell(EQUIVALENT_COURSE_LABEL, spreadsheet.getExcelStyle().getTitleStyle());
	spreadsheet.addCell(GRADE_LABEL, spreadsheet.getExcelStyle().getTitleStyle());
    }

    private static void buildCurricularCoursesGroups(ExecutionYear executionYear, List<Context> sortedContexts,
	    StyledExcelSpreadsheet spreadsheet, HSSFCellStyle shadedNormalTextAndFont, HSSFCellStyle unshadedNormalTextAndFont,
	    HSSFCellStyle shadedUnlockedNormalTextAndFont, HSSFCellStyle unshadedUnlockedNormalTextAndFont) {
	for (Context context : sortedContexts) {
	    CourseGroup childDegreeModule = (CourseGroup) context.getChildDegreeModule();
	    spreadsheet.newRow();
	    spreadsheet.addCell(childDegreeModule.getName(), spreadsheet.getExcelStyle().getTitleStyle());
	    List<Context> sortedCurricularContexts = childDegreeModule
		    .getSortedChildContextsWithCurricularCoursesByExecutionYear(executionYear);
	    if (sortedContexts.size() > 1) {

		//let's have a counter to make sure we shade one line but not the next
		int oddLineCounter = 0;
		for (Context courseContext : sortedCurricularContexts) {
		    oddLineCounter++;
		    HSSFCellStyle cellStyleToUse;
		    HSSFCellStyle unlockedCellStyleToUse;
		    if ((oddLineCounter % 2) == 0) {
			cellStyleToUse = shadedNormalTextAndFont;
			unlockedCellStyleToUse = shadedUnlockedNormalTextAndFont;
		    } else {
			cellStyleToUse = unshadedNormalTextAndFont;
			unlockedCellStyleToUse = unshadedUnlockedNormalTextAndFont;
		    }
		    DegreeModule courseModule = courseContext.getChildDegreeModule();
		    spreadsheet.newRow();
		    spreadsheet.addCell(courseModule.getName(), cellStyleToUse);
		    spreadsheet.addCell(((CurricularCourse) courseModule).getEctsCredits(), cellStyleToUse);
		    //let's fill the next two cells so that they get shaded as well
		    spreadsheet.addCell(StringUtils.SINGLE_SPACE, unlockedCellStyleToUse);
		    spreadsheet.addCell(StringUtils.SINGLE_SPACE, unlockedCellStyleToUse);
		}
	    }
	    List<Context> sortedGroupContexts = childDegreeModule
		    .getSortedChildContextsWithCourseGroupsByExecutionYear(executionYear);
	    if (sortedGroupContexts.size() > 0) {
		buildCurricularCoursesGroups(executionYear, sortedGroupContexts, spreadsheet, shadedNormalTextAndFont,
			unshadedNormalTextAndFont, shadedUnlockedNormalTextAndFont, unshadedUnlockedNormalTextAndFont);
	    }
	}
    }
}