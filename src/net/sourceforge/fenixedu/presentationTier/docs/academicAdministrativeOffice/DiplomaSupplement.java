package net.sourceforge.fenixedu.presentationTier.docs.academicAdministrativeOffice;

import java.math.BigDecimal;
import java.text.Collator;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;

import net.sourceforge.fenixedu.domain.CurricularCourse;
import net.sourceforge.fenixedu.domain.Degree;
import net.sourceforge.fenixedu.domain.DegreeOfficialPublication;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.IEnrolment;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.degree.DegreeType;
import net.sourceforge.fenixedu.domain.degreeStructure.Context;
import net.sourceforge.fenixedu.domain.degreeStructure.CourseGroup;
import net.sourceforge.fenixedu.domain.degreeStructure.CycleCourseGroup;
import net.sourceforge.fenixedu.domain.degreeStructure.CycleType;
import net.sourceforge.fenixedu.domain.degreeStructure.DegreeModule;
import net.sourceforge.fenixedu.domain.degreeStructure.EctsGraduationGradeConversionTable;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.organizationalStructure.AcademicalInstitutionType;
import net.sourceforge.fenixedu.domain.organizationalStructure.ScientificAreaUnit;
import net.sourceforge.fenixedu.domain.organizationalStructure.Unit;
import net.sourceforge.fenixedu.domain.organizationalStructure.UniversityUnit;
import net.sourceforge.fenixedu.domain.serviceRequests.documentRequests.DiplomaSupplementRequest;
import net.sourceforge.fenixedu.domain.serviceRequests.documentRequests.DocumentRequest;
import net.sourceforge.fenixedu.domain.student.Registration;
import net.sourceforge.fenixedu.domain.student.Student;
import net.sourceforge.fenixedu.domain.student.curriculum.ExtraCurricularActivity;
import net.sourceforge.fenixedu.domain.student.curriculum.ExtraCurricularActivityType;
import net.sourceforge.fenixedu.domain.student.curriculum.ICurriculumEntry;
import net.sourceforge.fenixedu.domain.studentCurriculum.CycleCurriculumGroup;
import net.sourceforge.fenixedu.domain.studentCurriculum.Dismissal;
import net.sourceforge.fenixedu.domain.studentCurriculum.ExternalEnrolment;
import net.sourceforge.fenixedu.util.StringFormatter;

import org.apache.commons.lang.StringUtils;
import org.joda.time.YearMonthDay;

import pt.utl.ist.fenix.tools.util.i18n.Language;

public class DiplomaSupplement extends AdministrativeOfficeDocument {

    protected DiplomaSupplement(final DocumentRequest documentRequest, final Locale locale) {
	super(documentRequest, locale);
    }

    @Override
    public String getReportTemplateKey() {
	return getClass().getName();
    }

    @Override
    protected void fillReport() {
	addParameter("bundle", getResourceBundle());
	Registration registration = getRegistration();
	Person person = registration.getPerson();
	Degree degree = registration.getDegree();
	DegreeType degreeType = degree.getDegreeType();
	ExecutionYear conclusion = registration.getLastStudentCurricularPlan().getCycle(getRequestedCycle())
		.getConclusionProcess().getConclusionYear();
	String degreeName = degree.getFilteredName(conclusion);
	final UniversityUnit institutionsUniversityUnit = UniversityUnit.getInstitutionsUniversityUnit();

	addParameter("name", StringFormatter.prettyPrint(person.getName().trim()));

	// Group 1
	addParameter("familyName", ((DiplomaSupplementRequest) getDocumentRequest()).getFamilyNames());
	addParameter("givenName", ((DiplomaSupplementRequest) getDocumentRequest()).getGivenNames());
	addParameter("birthDay", person.getDateOfBirthYearMonthDay().toString(DD_SLASH_MM_SLASH_YYYY, getLocale()));
	addParameter("nationality", StringFormatter.prettyPrint(person.getCountry().getCountryNationality().getContent(
		getLanguage())));
	addParameter("documentIdType", applyMessageArguments(getResourceBundle().getString("diploma.supplement.one.five.one"),
		getEnumerationBundle().getString(person.getIdDocumentType().getName())));
	addParameter("documentIdNumber", person.getDocumentIdNumber());
	if (person.getExpirationDateOfDocumentIdYearMonthDay() != null) {
	    addParameter("documentIdExpiration", " / "
		    + person.getExpirationDateOfDocumentIdYearMonthDay().toString(DD_SLASH_MM_SLASH_YYYY, getLocale()));
	} else {
	    addParameter("documentIdExpiration", EMPTY_STR);
	}
	addParameter("registrationNumber", registration.getNumber());

	// Group 2
	final String graduateTitle = degreeType.getGraduateTitle(getRequestedCycle(), getLocale());
	addParameter("graduateTitle", getEnumerationBundle().getString(
		degreeType.getQualifiedName() + (degreeType.isComposite() ? "." + getRequestedCycle().name() : "")
			+ ".graduate.title")
		+ SINGLE_SPACE
		+ getResourceBundle().getString("label.in")
		+ SINGLE_SPACE
		+ degree.getFilteredName(conclusion)
		+ ", " + graduateTitle);
	addParameter("prevailingScientificArea", degreeName);
	addParameter("universityName", institutionsUniversityUnit.getName());
	addParameter("universityStatus", getEnumerationBundle()
		.getString(
			AcademicalInstitutionType.class.getSimpleName() + "."
				+ institutionsUniversityUnit.getInstitutionType().getName()));
	addParameter("institutionName", RootDomainObject.getInstance().getInstitutionUnit().getName());
	addParameter("institutionStatus", getEnumerationBundle().getString(
		RootDomainObject.getInstance().getInstitutionUnit().getType().getName())
		+ SINGLE_SPACE
		+ getResourceBundle().getString("diploma.supplement.of")
		+ SINGLE_SPACE
		+ institutionsUniversityUnit.getName());
	if (getRequestedCycle().equals(CycleType.FIRST_CYCLE)) {
	    addParameter("languages", getEnumerationBundle().getString("pt"));
	} else {
	    addParameter("languages", getEnumerationBundle().getString("pt") + SINGLE_SPACE
		    + getEnumerationBundle().getString("AND").toLowerCase() + SINGLE_SPACE
		    + getEnumerationBundle().getString("en"));
	}

	// Group 3
	addParameter("qualificationLevel", getResourceBundle().getString(
		"diploma.supplement.qualification." + getRequestedCycle()));
	addParameter("years", degreeType.getYears(getRequestedCycle()));
	addParameter("semesters", degreeType.getSemesters(getRequestedCycle()));
	addParameter("weeksOfStudyPerYear", getResourceBundle().getString(
		"diploma.supplement.weeksOfStudyPerYear." + getRequestedCycle()));
	// TODO: Confirmar com o Jo�o
	addParameter("ectsCredits", Math.round(registration.getLastStudentCurricularPlan().getCycle(getRequestedCycle())
		.getDefaultEcts(conclusion)));

	// Group 4
	addProgrammeRequirements(registration, conclusion);
	addEntriesParameters(registration);
	addParameter("classificationSystem", applyMessageArguments(getResourceBundle().getString(
		"diploma.supplement.four.four.one"), degreeType.getFilteredName()));
	final Integer finalAverage = registration.getFinalAverage(getRequestedCycle());
	addParameter("finalAverage", finalAverage);
	String qualifiedAverageGrade;
	if (finalAverage <= 13) {
	    qualifiedAverageGrade = "sufficient";
	} else if (finalAverage <= 15) {
	    qualifiedAverageGrade = "good";
	} else if (finalAverage <= 17) {
	    qualifiedAverageGrade = "verygood";
	} else {
	    qualifiedAverageGrade = "excelent";
	}
	addParameter("finalAverageQualified", getResourceBundle().getString(
		"diploma.supplement.qualifiedgrade." + qualifiedAverageGrade));
	EctsGraduationGradeConversionTable table = degree.getGraduationConversionTable(conclusion.getAcademicInterval(),
		getRequestedCycle());
	addParameter("ectsGradeConversionTable", table.getEctsTable());
	addParameter("ectsGradePercentagesTable", table.getPercentages());

	// Group 5
	final StringBuilder access = new StringBuilder();
	if (getRequestedCycle() == CycleType.THIRD_CYCLE) {
	    access.append(getResourceBundle().getString("diploma.supplement.five.one.three"));
	} else {
	    access.append(getResourceBundle().getString("diploma.supplement.five.one.one")).append(SINGLE_SPACE);
	    access.append(graduateTitle).append(SINGLE_SPACE);
	    access.append(getResourceBundle().getString("diploma.supplement.five.one.two"));
	}
	addParameter("accessToHigherLevelOfEducation", access.toString());
	addProfessionalStatus(degreeName, degree.getSigla());

	// Group 6
	addExtraCurricularActivities(registration.getStudent());

	// Group 7
	addParameter("day", new YearMonthDay().toString(DD_SLASH_MM_SLASH_YYYY, getLocale()));
	addParameter("universityPrincipalName", institutionsUniversityUnit.getInstitutionsUniversityPrincipal()
		.getValidatedName());

	// Group 8
	addParameter("langSuffix", getLanguage().name());
    }

    protected CycleType getRequestedCycle() {
	return ((DiplomaSupplementRequest) getDocumentRequest()).getRequestedCycle();
    }

    private void addProgrammeRequirements(Registration registration, ExecutionYear conclusion) {
	CycleCurriculumGroup cycleGroup = registration.getLastStudentCurricularPlan().getCycle(getRequestedCycle());
	CycleCourseGroup cycle = cycleGroup.getCycleCourseGroup();
	String the = getLanguage().equals(Language.pt) ? (cycle.getDegree().getDegreeType().equals(DegreeType.BOLONHA_DEGREE) ? "A"
		: "O")
		: "The";
	String presentationName = cycle.getDegree().getPresentationName();
	long ectsCredits = Math.round(cycleGroup.getDefaultEcts(conclusion));
	Map<ScientificAreaUnit, Double> credits = new HashMap<ScientificAreaUnit, Double>();
	processDegreeModule(cycle, credits);
	List<String> areas = new ArrayList<String>();
	for (ScientificAreaUnit scientificArea : credits.keySet()) {
	    areas.add(scientificArea.getNameI18n().getContent(getLanguage()));
	}
	DegreeOfficialPublication dr = registration.getDegree().getOfficialPublication(
		conclusion.getBeginDateYearMonthDay().toDateTimeAtCurrentTime());
	if (dr == null) {
	    throw new DomainException("error.DiplomaSupplement.degreeOfficialPublicationNotFound");
	}
	String officialPublication = dr.getOfficialReference();
	String programmeRequirements;
	if (credits.keySet().size() == 0) {
	    programmeRequirements = applyMessageArguments(getResourceBundle().getString(
		    "diploma.supplement.four.two.programmerequirements.template.noareas"), the, presentationName, Long
		    .toString(ectsCredits));
	} else {
	    programmeRequirements = applyMessageArguments(getResourceBundle().getString(
		    "diploma.supplement.four.two.programmerequirements.template.withareas"), the, presentationName, Long
		    .toString(ectsCredits), Integer.toString(credits.keySet().size()), StringUtils.join(areas, "; "),
		    officialPublication);
	}
	addParameter("programmeRequirements", programmeRequirements);
    }

    private void processDegreeModule(CourseGroup group, Map<ScientificAreaUnit, Double> credits) {
	for (Context context : group.getActiveChildContexts()) {
	    DegreeModule module = context.getChildDegreeModule();
	    if (module.isCurricularCourse()) {
		CurricularCourse curricularCourse = (CurricularCourse) module;
		if (curricularCourse.hasCompetenceCourse()) {
		    ScientificAreaUnit scientificArea = curricularCourse.getCompetenceCourse().getScientificAreaUnit();
		    Double ects = curricularCourse.getEctsCredits();
		    if (!credits.containsKey(scientificArea)) {
			credits.put(scientificArea, 0d);
		    }
		    credits.put(scientificArea, credits.get(scientificArea) + ects);
		}
	    } else {
		processDegreeModule((CourseGroup) module, credits);
	    }
	}
    }

    private void addProfessionalStatus(String degreeName, String degreeSigla) {
	String professionalStatus;
	if (!getRequestedCycle().equals(CycleType.SECOND_CYCLE)) {
	    professionalStatus = getResourceBundle().getString("diploma.supplement.professionalstatus.notapplicable");
	} else {
	    if (degreeSigla.equals("MEFT")) {
		professionalStatus = applyMessageArguments(getResourceBundle().getString(
			"diploma.supplement.professionalstatus.uncredited.engineer"), degreeName);
	    } else if (degreeSigla.equals("MERC") || degreeSigla.equals("MEE")) {
		professionalStatus = applyMessageArguments(getResourceBundle().getString(
			"diploma.supplement.professionalstatus.uncredited.underappreciation.engineer"), degreeName);
	    } else if (degreeSigla.equals("MA")) {
		professionalStatus = applyMessageArguments(getResourceBundle().getString(
			"diploma.supplement.professionalstatus.credited.arquitect.withintership"), degreeName);
	    } else if (degreeSigla.equals("MMA") || degreeSigla.equals("MQ")) {
		professionalStatus = getResourceBundle().getString("diploma.supplement.professionalstatus.notapplicable");
	    } else {
		professionalStatus = applyMessageArguments(getResourceBundle().getString(
			"diploma.supplement.professionalstatus.credited.engineer"), degreeName);
	    }
	}
	addParameter("professionalStatus", professionalStatus);
    }

    private void addExtraCurricularActivities(final Student student) {
	if (student.hasAnyExtraCurricularActivity()) {
	    List<String> activities = new ArrayList<String>();
	    Map<ExtraCurricularActivityType, List<ExtraCurricularActivity>> activityMap = new HashMap<ExtraCurricularActivityType, List<ExtraCurricularActivity>>();
	    for (ExtraCurricularActivity activity : student.getExtraCurricularActivitySet()) {
		if (!activityMap.containsKey(activity.getType())) {
		    activityMap.put(activity.getType(), new ArrayList<ExtraCurricularActivity>());
		}
		activityMap.get(activity.getType()).add(activity);
	    }
	    for (Entry<ExtraCurricularActivityType, List<ExtraCurricularActivity>> entry : activityMap.entrySet()) {
		StringBuilder activityText = new StringBuilder();
		activityText.append(getResourceBundle().getString("diploma.supplement.six.one.extracurricularactivity.heading"));
		activityText.append(SINGLE_SPACE);
		activityText.append(entry.getKey().getName().getContent(getLanguage()));
		activityText.append(SINGLE_SPACE);
		List<String> activityTimings = new ArrayList<String>();
		for (ExtraCurricularActivity activity : entry.getValue()) {
		    activityTimings.add(getResourceBundle().getString(
			    "diploma.supplement.six.one.extracurricularactivity.time.heading")
			    + SINGLE_SPACE
			    + activity.getStart().toString("MM-yyyy")
			    + SINGLE_SPACE
			    + getResourceBundle().getString("diploma.supplement.six.one.extracurricularactivity.time.separator")
			    + SINGLE_SPACE + activity.getEnd().toString("MM-yyyy"));
		}
		activityText.append(StringUtils.join(activityTimings, ", "));
		activities.add(activityText.toString());
	    }
	    addParameter("extraCurricularActivities", StringUtils.join(activities, '\n') + ".");
	} else {
	    addParameter("extraCurricularActivities", getResourceBundle().getString(
		    "diploma.supplement.six.one.extracurricularactivity.none"));
	}
    }

    private String applyMessageArguments(String message, String... args) {
	for (String arg : args) {
	    message = message.replaceFirst("\\{.*?\\}", arg);
	}
	return message;
    }

    private void addEntriesParameters(final Registration registration) {
	final List<DiplomaSupplementEntry> entries = new ArrayList<DiplomaSupplementEntry>();
	final Map<Unit, String> academicUnitIdentifiers = new HashMap<Unit, String>();

	// for (ICurriculumEntry entry :
	// registration.getCurriculum(cycleType).getCurriculumEntries()) {
	// System.out.println("old format: " + entry.getName() + " grade: " +
	// entry.getGradeValue());
	// }
	// System.out.println();
	// System.out.println();
	// System.out.println();
	// System.out.println();

	for (ICurriculumEntry entry : registration.getCurriculum(getRequestedCycle()).getCurriculumEntries()) {
	    entries.add(new DiplomaSupplementEntry(entry, academicUnitIdentifiers));
	}

	Collections.sort(entries);
	addParameter("entries", entries);

	final List<AcademicUnitEntry> identifiers = new ArrayList<AcademicUnitEntry>();
	for (final Entry<Unit, String> entry2 : academicUnitIdentifiers.entrySet()) {
	    identifiers.add(new AcademicUnitEntry(entry2));
	}
	addParameter("academicUnitIdentifiers", identifiers);
    }

    static final public Comparator<DiplomaSupplementEntry> COMPARATOR = new Comparator<DiplomaSupplementEntry>() {

	@Override
	public int compare(DiplomaSupplementEntry o1, DiplomaSupplementEntry o2) {
	    final int c = o1.getExecutionYear().compareTo(o2.getExecutionYear());
	    return c == 0 ? Collator.getInstance().compare(o1.getName(), o2.getName()) : c;
	}

    };

    public class DiplomaSupplementEntry implements Comparable<DiplomaSupplementEntry> {

	private final ICurriculumEntry entry;

	private final String executionYear;

	private final String name;

	private final String type;

	private final String duration;

	private final BigDecimal ectsCreditsForCurriculum;

	private final String gradeValue;

	private final String ectsScale;

	private final String academicUnitId;

	public DiplomaSupplementEntry(final ICurriculumEntry entry, final Map<Unit, String> academicUnitIdentifiers) {
	    this.entry = entry;
	    this.executionYear = entry.getExecutionYear().getYear();
	    this.name = getMLSTextContent(entry.getName());
	    if (entry instanceof IEnrolment) {
		IEnrolment enrolment = (IEnrolment) entry;
		this.type = getEnumerationBundle().getString(enrolment.getEnrolmentTypeName());
		this.duration = getResourceBundle().getString(
			enrolment.isAnual() ? "diploma.supplement.annual" : "diploma.supplement.semestral");
		this.ectsScale = enrolment.getEctsGrade(getRegistration().getLastStudentCurricularPlan()).getValue();
	    } else if (entry instanceof Dismissal && ((Dismissal) entry).getCredits().isEquivalence()) {
		Dismissal dismissal = (Dismissal) entry;
		this.type = getEnumerationBundle().getString(dismissal.getEnrolmentTypeName());
		this.duration = getResourceBundle().getString(
			dismissal.isAnual() ? "diploma.supplement.annual" : "diploma.supplement.semestral");
		this.ectsScale = dismissal.getEctsGrade().getValue();
	    } else {
		throw new Error("The roof is on fire");
	    }
	    this.ectsCreditsForCurriculum = entry.getEctsCreditsForCurriculum();
	    this.academicUnitId = obtainAcademicUnitIdentifier(academicUnitIdentifiers);
	    this.gradeValue = entry.getGrade().getValue();
	}

	public ICurriculumEntry getEntry() {
	    return entry;
	}

	public String getExecutionYear() {
	    return executionYear;
	}

	public String getName() {
	    return name;
	}

	public String getType() {
	    return type;
	}

	public String getDuration() {
	    return duration;
	}

	public BigDecimal getEctsCreditsForCurriculum() {
	    return ectsCreditsForCurriculum;
	}

	public String getGradeValue() {
	    return gradeValue;
	}

	public String getEctsScale() {
	    return ectsScale;
	}

	public String getAcademicUnitId() {
	    return academicUnitId;
	}

	private String obtainAcademicUnitIdentifier(final Map<Unit, String> academicUnitIdentifiers) {
	    final Unit unit = entry instanceof ExternalEnrolment ? ((ExternalEnrolment) entry).getAcademicUnit()
		    : RootDomainObject.getInstance().getInstitutionUnit();
	    return getAcademicUnitIdentifier(academicUnitIdentifiers, unit);
	}

	@Override
	public int compareTo(final DiplomaSupplementEntry o) {
	    return COMPARATOR.compare(this, o);
	}

    }

    public class AcademicUnitEntry {
	private final String identifier;

	private final String name;

	public AcademicUnitEntry(final Entry<Unit, String> entry) {
	    this.identifier = entry.getValue();
	    this.name = getMLSTextContent(entry.getKey().getNameI18n());
	}

	public String getIdentifier() {
	    return identifier;
	}

	public String getName() {
	    return name;
	}
    }

}
