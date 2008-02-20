package net.sourceforge.fenixedu.domain;

import java.io.Serializable;
import java.math.BigDecimal;
import java.text.Collator;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import net.sourceforge.fenixedu.dataTransferObject.degreeAdministrativeOffice.gradeSubmission.MarkSheetEnrolmentEvaluationBean;
import net.sourceforge.fenixedu.domain.branch.BranchType;
import net.sourceforge.fenixedu.domain.curricularPeriod.CurricularPeriod;
import net.sourceforge.fenixedu.domain.curricularPeriod.CurricularPeriodType;
import net.sourceforge.fenixedu.domain.curricularRules.CurricularRule;
import net.sourceforge.fenixedu.domain.curricularRules.RestrictionDoneDegreeModule;
import net.sourceforge.fenixedu.domain.curriculum.CurricularCourseType;
import net.sourceforge.fenixedu.domain.curriculum.EnrolmentEvaluationType;
import net.sourceforge.fenixedu.domain.degree.DegreeType;
import net.sourceforge.fenixedu.domain.degreeStructure.CompetenceCourseLoad;
import net.sourceforge.fenixedu.domain.degreeStructure.Context;
import net.sourceforge.fenixedu.domain.degreeStructure.CourseGroup;
import net.sourceforge.fenixedu.domain.degreeStructure.CurricularStage;
import net.sourceforge.fenixedu.domain.degreeStructure.DegreeModule;
import net.sourceforge.fenixedu.domain.degreeStructure.RegimeType;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.precedences.Restriction;
import net.sourceforge.fenixedu.domain.precedences.RestrictionHasEverBeenOrIsCurrentlyEnrolledInCurricularCourse;
import net.sourceforge.fenixedu.domain.student.Registration;
import net.sourceforge.fenixedu.domain.student.Student;
import net.sourceforge.fenixedu.domain.studentCurriculum.CurriculumModule;
import net.sourceforge.fenixedu.domain.util.FactoryExecutor;
import net.sourceforge.fenixedu.predicates.MarkSheetPredicates;
import net.sourceforge.fenixedu.util.DateFormatUtil;
import net.sourceforge.fenixedu.util.EnrolmentEvaluationState;
import net.sourceforge.fenixedu.util.MultiLanguageString;

import org.apache.commons.beanutils.BeanComparator;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;
import org.apache.commons.collections.comparators.ComparatorChain;
import org.apache.commons.lang.StringUtils;
import org.joda.time.DateTime;
import org.joda.time.YearMonthDay;

public class CurricularCourse extends CurricularCourse_Base {

    private static final double ECTS_CREDITS_FOR_PRE_BOLONHA = 6;

    private static final double WEIGHT_FOR_PRE_BOLONHA = 6;

    static final public Comparator<CurricularCourse> CURRICULAR_COURSE_COMPARATOR_BY_DEGREE_AND_NAME = new Comparator<CurricularCourse>() {
	public int compare(CurricularCourse o1, CurricularCourse o2) {
	    final ComparatorChain comparatorChain = new ComparatorChain();
	    comparatorChain
		    .addComparator(new BeanComparator("degreeCurricularPlan.degree.tipoCurso.name", Collator.getInstance()));
	    comparatorChain.addComparator(new BeanComparator("degreeCurricularPlan.degree.nome", Collator.getInstance()));
	    comparatorChain.addComparator(CurricularCourse.COMPARATOR_BY_NAME);

	    return comparatorChain.compare(o1, o2);
	}
    };

    public static List<CurricularCourse> readCurricularCourses() {
	List<CurricularCourse> result = new ArrayList<CurricularCourse>();
	for (DegreeModule degreeModule : RootDomainObject.getInstance().getDegreeModules()) {
	    if (degreeModule instanceof CurricularCourse) {
		result.add((CurricularCourse) degreeModule);
	    }
	}
	return result;
    }

    protected CurricularCourse() {
	super();
	final Double d = Double.valueOf(0d);
	setTheoreticalHours(d);
	setTheoPratHours(d);
	setLabHours(d);
	setPraticalHours(d);
	setCredits(d);
	setEctsCredits(d);
	setWeigth(d);
    }

    protected CurricularCourse(DegreeCurricularPlan degreeCurricularPlan, String name, String code, String acronym,
	    Boolean enrolmentAllowed, CurricularStage curricularStage) {
	this();
	checkParameters(name, code, acronym);
	checkForCurricularCourseWithSameAttributes(degreeCurricularPlan, name, code, acronym);
	setName(name);
	setCode(code);
	setAcronym(acronym);
	setEnrollmentAllowed(enrolmentAllowed);
	setCurricularStage(curricularStage);
	setDegreeCurricularPlan(degreeCurricularPlan);
	if (curricularStage == CurricularStage.OLD) {
	    setRegimeType(RegimeType.SEMESTRIAL);
	}
    }

    private void checkParameters(final String name, final String code, final String acronym) {
	if (StringUtils.isEmpty(name)) {
	    throw new DomainException("error.curricularCourse.invalid.name");
	}
	if (StringUtils.isEmpty(code)) {
	    throw new DomainException("error.curricularCourse.invalid.code");
	}
	if (StringUtils.isEmpty(acronym)) {
	    throw new DomainException("error.curricularCourse.invalid.acronym");
	}
    }

    public CurricularCourse(Double weight, String prerequisites, String prerequisitesEn, CurricularStage curricularStage,
	    CompetenceCourse competenceCourse, CourseGroup parentCourseGroup, CurricularPeriod curricularPeriod,
	    ExecutionPeriod beginExecutionPeriod, ExecutionPeriod endExecutionPeriod) {

	this();
	setWeigth(weight);
	setPrerequisites(prerequisites);
	setPrerequisitesEn(prerequisitesEn);
	setCurricularStage(curricularStage);
	setCompetenceCourse(competenceCourse);
	setType(CurricularCourseType.NORMAL_COURSE);
	new Context(parentCourseGroup, this, curricularPeriod, beginExecutionPeriod, endExecutionPeriod);
    }

    public GradeScale getGradeScaleChain() {
	return super.getGradeScale() != null ? super.getGradeScale() : getDegreeCurricularPlan().getGradeScaleChain();
    }

    @Override
    public void print(StringBuilder dcp, String tabs, Context previousContext) {
	String tab = tabs + "\t";
	dcp.append(tab);
	dcp.append("[CC ").append(this.getIdInternal()).append("][");
	dcp.append(previousContext.getCurricularPeriod().getOrderByType(CurricularPeriodType.YEAR)).append("Y,");
	dcp.append(previousContext.getCurricularPeriod().getOrderByType(CurricularPeriodType.SEMESTER)).append("S]\t");
	dcp.append("[B:").append(previousContext.getBeginExecutionPeriod().getBeginDateYearMonthDay());
	dcp.append(" E:").append(
		previousContext.hasEndExecutionPeriod() ? previousContext.getEndExecutionPeriod().getEndDateYearMonthDay()
			: "          ");
	dcp.append("]\t");
	dcp.append(this.getName()).append("\n");
    }

    @Override
    public boolean isLeaf() {
	return true;
    }

    @Override
    public boolean isRoot() {
	return false;
    }

    /**
     * Temporary method, after all degrees migration this is no longer necessary
     */
    private boolean isBoxStructure() {
	return !(getCurricularStage() == CurricularStage.OLD);
    }

    @Override
    public DegreeCurricularPlan getParentDegreeCurricularPlan() {
	if (isBoxStructure()) {
	    return hasAnyParentContexts() ? getParentContexts().get(0).getParentCourseGroup().getParentDegreeCurricularPlan()
		    : null;
	} else {
	    return super.getDegreeCurricularPlan();
	}
    }

    @Override
    final public DegreeCurricularPlan getDegreeCurricularPlan() {
	return getParentDegreeCurricularPlan();
    }

    public void edit(Double weight, String prerequisites, String prerequisitesEn, CurricularStage curricularStage,
	    CompetenceCourse competenceCourse) {

	setWeigth(weight);
	setPrerequisites(prerequisites);
	setPrerequisitesEn(prerequisitesEn);
	setCurricularStage(curricularStage);
	setCompetenceCourse(competenceCourse);
    }

    /**
     * - This method is used to edit a 'special' curricular course that will
     * represent any curricular course according to a rule
     */
    public void edit(String name, String nameEn, CurricularStage curricularStage) {
	setName(name);
	setNameEn(nameEn);
	setCurricularStage(curricularStage);
    }

    /**
     * - Edit Pre-Bolonha CurricularCourse
     */
    public void edit(String name, String nameEn, String code, String acronym, Double weigth, Double credits, Double ectsCredits) {
	checkForCurricularCourseWithSameAttributes(getDegreeCurricularPlan(), name, code, acronym);
	setName(name);
	setNameEn(nameEn);
	setCode(code);
	setAcronym(acronym);
	setWeigth(weigth);
	setCredits(credits);
	setEctsCredits(ectsCredits);
    }

    private void checkForCurricularCourseWithSameAttributes(DegreeCurricularPlan degreeCurricularPlan, String name, String code,
	    String acronym) {
	for (final CurricularCourse curricularCourse : degreeCurricularPlan.getCurricularCourses()) {
	    if (curricularCourse == this) {
		continue;
	    }
	    if (curricularCourse.getName().equals(name) && curricularCourse.getCode().equals(code)) {
		throw new DomainException("error.curricularCourseWithSameNameAndCode");
	    }
	    if (curricularCourse.getAcronym().equals(acronym)) {
		throw new DomainException("error.curricularCourseWithSameAcronym");
	    }
	}
    }

    @Override
    public void delete() {
	super.delete();
	removeUniversity();
	removeScientificArea();
	removeDegreeCurricularPlan();
	removeCompetenceCourse();
	removeRootDomainObject();
	super.deleteDomainObject();
    }

    public boolean curricularCourseIsMandatory() {
	return getMandatory().booleanValue();
    }

    public List<CurricularCourseScope> getInterminatedScopes() {
	List<CurricularCourseScope> result = new ArrayList<CurricularCourseScope>();
	for (CurricularCourseScope curricularCourseScope : this.getScopes()) {
	    if (curricularCourseScope.getEndDate() == null) {
		result.add(curricularCourseScope);
	    }
	}

	return result;
    }

    public List<CurricularCourseScope> getActiveScopes() {
	final List<CurricularCourseScope> activeScopes = new ArrayList<CurricularCourseScope>();
	for (CurricularCourseScope scope : this.getScopes()) {
	    if (scope.isActive()) {
		activeScopes.add(scope);
	    }
	}
	return activeScopes;
    }

    public boolean hasAnyActiveDegreModuleScope(int year, int semester) {
	for (final DegreeModuleScope degreeModuleScope : this.getDegreeModuleScopes()) {
	    if (degreeModuleScope.isActive(year, semester)) {
		return true;
	    }
	}

	return false;
    }

    public boolean hasAnyActiveDegreModuleScope() {
	for (final DegreeModuleScope degreeModuleScope : this.getDegreeModuleScopes()) {
	    if (degreeModuleScope.isActive()) {
		return true;
	    }
	}

	return false;
    }

    public boolean hasAnyActiveDegreModuleScope(final ExecutionPeriod executionPeriod) {
	for (final DegreeModuleScope degreeModuleScope : this.getDegreeModuleScopes()) {
	    if (degreeModuleScope.isActiveForExecutionPeriod(executionPeriod)) {
		return true;
	    }
	}

	return false;
    }

    public boolean hasAnyActiveDegreModuleScope(final ExecutionYear executionYear) {
	for (final ExecutionPeriod executionPeriod : executionYear.getExecutionPeriodsSet()) {
	    if (hasAnyActiveDegreModuleScope(executionPeriod)) {
		return true;
	    }
	}

	return false;
    }

    public boolean hasAnyActiveContext(final ExecutionPeriod executionPeriod) {
	for (final Context context : getParentContexts()) {
	    if (context.isValid(executionPeriod)) {
		return true;
	    }
	}
	return false;
    }

    public List<CurricularCourseScope> getActiveScopesInExecutionPeriod(final ExecutionPeriod executionPeriod) {
	final List<CurricularCourseScope> result = new ArrayList<CurricularCourseScope>();
	for (final CurricularCourseScope curricularCourseScope : getScopes()) {
	    if (curricularCourseScope.isActiveForExecutionPeriod(executionPeriod)) {
		result.add(curricularCourseScope);
	    }
	}
	return result;
    }

    public List<DegreeModuleScope> getActiveDegreeModuleScopesInExecutionPeriod(final ExecutionPeriod executionPeriod) {
	final List<DegreeModuleScope> activeScopesInExecutionPeriod = new ArrayList<DegreeModuleScope>();
	for (final DegreeModuleScope scope : getDegreeModuleScopes()) {
	    if (scope.isActiveForExecutionPeriod(executionPeriod)) {
		activeScopesInExecutionPeriod.add(scope);
	    }
	}
	return activeScopesInExecutionPeriod;
    }

    public boolean hasActiveScopesInExecutionPeriod(final ExecutionPeriod executionPeriod) {
	return !getActiveDegreeModuleScopesInExecutionPeriod(executionPeriod).isEmpty();
    }

    public Set<CurricularCourseScope> getActiveScopesInExecutionYear(final ExecutionYear executionYear) {
	final Set<CurricularCourseScope> result = new HashSet<CurricularCourseScope>();
	for (final CurricularCourseScope scope : getScopes()) {
	    if (scope.isActiveForExecutionYear(executionYear)) {
		result.add(scope);
	    }
	}
	return result;
    }

    public List<CurricularCourseScope> getActiveScopesIntersectedByExecutionPeriod(final ExecutionPeriod executionPeriod) {
	final List<CurricularCourseScope> activeScopesInExecutionPeriod = new ArrayList<CurricularCourseScope>();
	for (final CurricularCourseScope scope : getScopes()) {
	    if (scope.getBeginYearMonthDay().isBefore(executionPeriod.getBeginDateYearMonthDay())) {
		if (!scope.hasEndYearMonthDay()
			|| !scope.getEndYearMonthDay().isBefore(executionPeriod.getBeginDateYearMonthDay())) {
		    activeScopesInExecutionPeriod.add(scope);
		}
	    } else if (!scope.getBeginYearMonthDay().isAfter(executionPeriod.getEndDateYearMonthDay())) {
		activeScopesInExecutionPeriod.add(scope);
	    }
	}
	return activeScopesInExecutionPeriod;
    }

    // -------------------------------------------------------------
    // BEGIN: Only for enrollment purposes
    // -------------------------------------------------------------

    public List<RestrictionHasEverBeenOrIsCurrentlyEnrolledInCurricularCourse> getRestrictionsHasEverBeenOrIsCurrentlyEnrolledInCurricularCourse() {
	List<RestrictionHasEverBeenOrIsCurrentlyEnrolledInCurricularCourse> result = new ArrayList<RestrictionHasEverBeenOrIsCurrentlyEnrolledInCurricularCourse>();
	for (final Restriction restriction : getRestrictionsByCurricularCourse()) {
	    if (restriction instanceof RestrictionHasEverBeenOrIsCurrentlyEnrolledInCurricularCourse) {
		result.add((RestrictionHasEverBeenOrIsCurrentlyEnrolledInCurricularCourse) restriction);
	    }
	}
	return result;
    }

    public boolean hasRestrictionDone(final CurricularCourse precedence) {
	if (!isBolonhaDegree()) {
	    throw new DomainException("CurricularCourse.method.only.appliable.to.bolonha.structure");
	}

	final ExecutionPeriod currentExecutionPeriod = ExecutionPeriod.readActualExecutionPeriod();

	for (final CurricularRule curricularRule : getCurricularRulesSet()) {
	    if (curricularRule.isValid(currentExecutionPeriod) && curricularRule instanceof RestrictionDoneDegreeModule) {
		final RestrictionDoneDegreeModule restrictionDone = (RestrictionDoneDegreeModule) curricularRule;

		if (restrictionDone.getPrecedenceDegreeModule() == precedence) {
		    return true;
		}
	    }
	}

	return false;
    }

    public CurricularYear getCurricularYearByBranchAndSemester(final Branch branch, final Integer semester) {
	return getCurricularYearByBranchAndSemester(branch, semester, new Date());
    }

    public CurricularYear getCurricularYearByBranchAndSemester(final Branch branch, final Integer semester, final Date date) {
	final List<CurricularCourseScope> scopesFound = new ArrayList<CurricularCourseScope>();
	for (CurricularCourseScope scope : getScopesSet()) {
	    if (scope.getCurricularSemester().getSemester().equals(semester) && scope.isActive(date)
		    && (scope.getBranch().representsCommonBranch() || (branch != null && branch.equals(scope.getBranch())))) {
		scopesFound.add(scope);
	    }
	}
	if (scopesFound.isEmpty()) {
	    for (CurricularCourseScope scope : getScopesSet()) {
		if (scope.getCurricularSemester().getSemester().equals(semester) && scope.isActive(date)) {
		    scopesFound.add(scope);
		}
	    }
	}

	return getCurricularYearWithLowerYear(scopesFound, date);
    }

    public String getCurricularCourseUniqueKeyForEnrollment() {
	final DegreeType degreeType = (this.getDegreeCurricularPlan() != null && this.getDegreeCurricularPlan().getDegree() != null) ? this
		.getDegreeCurricularPlan().getDegree().getDegreeType()
		: null;
	return constructUniqueEnrollmentKey(this.getCode(), this.getName(), degreeType);
    }

    public boolean hasActiveScopeInGivenSemester(final Integer semester) {
	for (CurricularCourseScope curricularCourseScope : getScopesSet()) {
	    if (curricularCourseScope.getCurricularSemester().getSemester().equals(semester)
		    && curricularCourseScope.isActive().booleanValue()) {
		return true;
	    }
	}

	return false;
    }

    public boolean hasScopeInGivenSemester(final Integer semester) {
	for (CurricularCourseScope curricularCourseScope : getScopesSet()) {
	    if (curricularCourseScope.getCurricularSemester().getSemester().equals(semester)) {
		return true;
	    }
	}

	return false;
    }

    public boolean hasActiveScopeInGivenSemesterForGivenBranch(final CurricularSemester curricularSemester, final Branch branch) {
	final List<CurricularCourseScope> scopes = this.getScopes();
	for (final CurricularCourseScope curricularCourseScope : scopes) {
	    if (curricularCourseScope.getCurricularSemester().equals(curricularSemester) && curricularCourseScope.isActive()
		    && curricularCourseScope.getBranch().equals(branch)) {
		return true;
	    }
	}
	return false;
    }

    public boolean hasActiveScopeInGivenSemesterForGivenBranch(final Integer semester, final Branch branch) {
	final List<CurricularCourseScope> scopes = this.getScopes();
	for (final CurricularCourseScope curricularCourseScope : scopes) {
	    if (curricularCourseScope.getCurricularSemester().getSemester().equals(semester) && curricularCourseScope.isActive()
		    && curricularCourseScope.getBranch().equals(branch)) {
		return true;
	    }
	}
	return false;
    }

    @SuppressWarnings("unchecked")
    public boolean hasActiveScopeInGivenSemesterForCommonAndGivenBranch(final Integer semester, final Branch branch) {

	List<CurricularCourseScope> scopes = this.getScopes();

	List<CurricularCourseScope> result = (List<CurricularCourseScope>) CollectionUtils.select(scopes, new Predicate() {
	    public boolean evaluate(Object obj) {
		CurricularCourseScope curricularCourseScope = (CurricularCourseScope) obj;
		return ((curricularCourseScope.getBranch().getBranchType().equals(BranchType.COMNBR) || curricularCourseScope
			.getBranch().equals(branch))
			&& curricularCourseScope.getCurricularSemester().getSemester().equals(semester) && curricularCourseScope
			.isActive().booleanValue());
	    }
	});

	return !result.isEmpty();
    }

    private CurricularYear getCurricularYearWithLowerYear(List<CurricularCourseScope> listOfScopes, Date date) {

	if (listOfScopes.isEmpty()) {
	    return null;
	}

	CurricularYear minCurricularYear = null;

	for (CurricularCourseScope curricularCourseScope : (List<CurricularCourseScope>) listOfScopes) {
	    if (curricularCourseScope.isActive(date).booleanValue()) {
		CurricularYear actualCurricularYear = curricularCourseScope.getCurricularSemester().getCurricularYear();
		if (minCurricularYear == null
			|| minCurricularYear.getYear().intValue() > actualCurricularYear.getYear().intValue()) {
		    minCurricularYear = actualCurricularYear;
		}
	    }
	}

	return minCurricularYear;
    }

    // -------------------------------------------------------------
    // END: Only for enrollment purposes
    // -------------------------------------------------------------

    private String constructUniqueEnrollmentKey(String code, String name, DegreeType tipoCurso) {
	StringBuilder stringBuffer = new StringBuilder(50);
	stringBuffer.append(code);
	stringBuffer.append(name);
	if (tipoCurso != null) {
	    stringBuffer.append(tipoCurso.toString());
	}
	return StringUtils.lowerCase(stringBuffer.toString());
    }

    public static class CurriculumFactory implements Serializable {
	private DomainReference<CurricularCourse> curricularCourseDomainReference;

	private String program;

	private String programEn;

	private String generalObjectives;

	private String generalObjectivesEn;

	private String operacionalObjectives;

	private String operacionalObjectivesEn;

	private DateTime lastModification;

	public CurriculumFactory(final CurricularCourse curricularCourse) {
	    setCurricularCourse(curricularCourse);
	}

	public String getGeneralObjectives() {
	    return generalObjectives;
	}

	public void setGeneralObjectives(String generalObjectives) {
	    this.generalObjectives = generalObjectives;
	}

	public String getGeneralObjectivesEn() {
	    return generalObjectivesEn;
	}

	public void setGeneralObjectivesEn(String generalObjectivesEn) {
	    this.generalObjectivesEn = generalObjectivesEn;
	}

	public String getOperacionalObjectives() {
	    return operacionalObjectives;
	}

	public void setOperacionalObjectives(String operacionalObjectives) {
	    this.operacionalObjectives = operacionalObjectives;
	}

	public String getOperacionalObjectivesEn() {
	    return operacionalObjectivesEn;
	}

	public void setOperacionalObjectivesEn(String operacionalObjectivesEn) {
	    this.operacionalObjectivesEn = operacionalObjectivesEn;
	}

	public String getProgram() {
	    return program;
	}

	public void setProgram(String program) {
	    this.program = program;
	}

	public String getProgramEn() {
	    return programEn;
	}

	public void setProgramEn(String programEn) {
	    this.programEn = programEn;
	}

	public DateTime getLastModification() {
	    return lastModification;
	}

	public void setLastModification(DateTime lastModification) {
	    this.lastModification = lastModification;
	}

	public CurricularCourse getCurricularCourse() {
	    return curricularCourseDomainReference == null ? null : curricularCourseDomainReference.getObject();
	}

	public void setCurricularCourse(final CurricularCourse curricularCourse) {
	    curricularCourseDomainReference = new DomainReference<CurricularCourse>(curricularCourse);
	}
    }

    public static class CurriculumFactoryInsertCurriculum extends CurriculumFactory implements FactoryExecutor {
	public CurriculumFactoryInsertCurriculum(CurricularCourse curricularCourse, ExecutionCourse executionCourse) {
	    super(curricularCourse);
	    setLastModification(executionCourse.getExecutionPeriod().getBeginDateYearMonthDay().toDateTimeAtMidnight());
	}

	public Curriculum execute() {
	    final CurricularCourse curricularCourse = getCurricularCourse();
	    return curricularCourse == null ? null : curricularCourse.insertCurriculum(getProgram(), getProgramEn(),
		    getGeneralObjectives(), getGeneralObjectivesEn(), getOperacionalObjectives(), getOperacionalObjectivesEn(),
		    getLastModification());
	}

    }

    public static class CurriculumFactoryEditCurriculum extends CurriculumFactory implements FactoryExecutor {
	private DomainReference<Curriculum> curriculum;

	public CurriculumFactoryEditCurriculum(CurricularCourse curricularCourse) {
	    super(curricularCourse);
	    setLastModification(new DateTime());
	    curriculum = null;
	}

	public CurriculumFactoryEditCurriculum(Curriculum curriculum) {
	    super(curriculum.getCurricularCourse());
	    this.curriculum = new DomainReference<Curriculum>(curriculum);
	}

	public Curriculum getCurriculum() {
	    return curriculum == null ? null : curriculum.getObject();
	}

	public void setCurriculum(Curriculum curriculum) {
	    this.curriculum = new DomainReference<Curriculum>(curriculum);
	    populateCurriculum(this, curriculum);
	}

	public Curriculum execute() {
	    final Curriculum curriculum = getCurriculum();
	    if (curriculum == null) {
		final CurricularCourse curricularCourse = getCurricularCourse();
		return curricularCourse == null ? null : curricularCourse.editCurriculum(getProgram(), getProgramEn(),
			getGeneralObjectives(), getGeneralObjectivesEn(), getOperacionalObjectives(),
			getOperacionalObjectivesEn(), getLastModification());
	    } else {
		final DateTime dt = curriculum.getLastModificationDateDateTime();
		curriculum.edit(getGeneralObjectives(), getOperacionalObjectives(), getProgram(), getGeneralObjectivesEn(),
			getOperacionalObjectivesEn(), getProgramEn());
		curriculum.setLastModificationDateDateTime(dt);
		return curriculum;
	    }
	}
    }

    public CurriculumFactoryEditCurriculum getCurriculumFactoryEditCurriculum() {
	final CurriculumFactoryEditCurriculum curriculumFactoryEditCurriculum = new CurriculumFactoryEditCurriculum(this);
	final Curriculum curriculum = findLatestCurriculum();
	populateCurriculum(curriculumFactoryEditCurriculum, curriculum);
	return curriculumFactoryEditCurriculum;
    }

    private static void populateCurriculum(final CurriculumFactoryEditCurriculum curriculumFactoryEditCurriculum,
	    final Curriculum curriculum) {
	curriculumFactoryEditCurriculum.setGeneralObjectives(curriculum.getGeneralObjectives());
	curriculumFactoryEditCurriculum.setGeneralObjectivesEn(curriculum.getGeneralObjectivesEn());
	curriculumFactoryEditCurriculum.setOperacionalObjectives(curriculum.getOperacionalObjectives());
	curriculumFactoryEditCurriculum.setOperacionalObjectivesEn(curriculum.getOperacionalObjectivesEn());
	curriculumFactoryEditCurriculum.setProgram(curriculum.getProgram());
	curriculumFactoryEditCurriculum.setProgramEn(curriculum.getProgramEn());
    }

    public Curriculum editCurriculum(String program, String programEn, String generalObjectives, String generalObjectivesEn,
	    String operacionalObjectives, String operacionalObjectivesEn, DateTime lastModification) {
	Curriculum curriculum = findLatestCurriculum();
	final ExecutionYear currentExecutionYear = ExecutionYear.readCurrentExecutionYear();
	if (!curriculum.getLastModificationDateDateTime().isBefore(
		currentExecutionYear.getBeginDateYearMonthDay().toDateMidnight())
		&& !curriculum.getLastModificationDateDateTime().isAfter(
			currentExecutionYear.getEndDateYearMonthDay().toDateMidnight())) {
	    curriculum.edit(generalObjectives, operacionalObjectives, program, generalObjectivesEn, operacionalObjectivesEn,
		    programEn);
	} else {
	    curriculum = insertCurriculum(program, programEn, operacionalObjectives, operacionalObjectivesEn, generalObjectives,
		    generalObjectivesEn, lastModification);
	}
	return curriculum;
    }

    public Curriculum insertCurriculum(String program, String programEn, String operacionalObjectives,
	    String operacionalObjectivesEn, String generalObjectives, String generalObjectivesEn, DateTime lastModification) {

	Curriculum curriculum = new Curriculum();

	curriculum.setCurricularCourse(this);
	curriculum.setProgram(program);
	curriculum.setProgramEn(programEn);
	curriculum.setOperacionalObjectives(operacionalObjectives);
	curriculum.setOperacionalObjectivesEn(operacionalObjectivesEn);
	curriculum.setGeneralObjectives(generalObjectives);
	curriculum.setGeneralObjectivesEn(generalObjectivesEn);
	curriculum.setLastModificationDateDateTime(lastModification);

	return curriculum;
    }

    public List<ExecutionCourse> getExecutionCoursesWithPublicSites() {
	List<ExecutionCourse> result = new ArrayList<ExecutionCourse>();

	for (final ExecutionCourse executionCourse : this.getAssociatedExecutionCourses()) {
	    if (executionCourse.hasSite()) {
		result.add(executionCourse);
	    }
	}

	return result;
    }

    @SuppressWarnings("unchecked")
    public List<ExecutionCourse> getExecutionCoursesByExecutionPeriod(final ExecutionPeriod executionPeriod) {
	return (List<ExecutionCourse>) CollectionUtils.select(getAssociatedExecutionCourses(), new Predicate() {
	    public boolean evaluate(Object o) {
		ExecutionCourse executionCourse = (ExecutionCourse) o;
		return executionCourse.getExecutionPeriod().equals(executionPeriod);
	    }
	});
    }

    @SuppressWarnings("unchecked")
    public List<ExecutionCourse> getExecutionCoursesByExecutionYear(final ExecutionYear executionYear) {
	return (List<ExecutionCourse>) CollectionUtils.select(getAssociatedExecutionCourses(), new Predicate() {
	    public boolean evaluate(Object o) {
		ExecutionCourse executionCourse = (ExecutionCourse) o;
		return executionCourse.getExecutionPeriod().getExecutionYear().equals(executionYear);
	    }
	});
    }

    public Curriculum findLatestCurriculum() {
	Curriculum latestCurriculum = null;
	for (final Curriculum curriculum : getAssociatedCurriculums()) {
	    if (latestCurriculum == null
		    || latestCurriculum.getLastModificationDateDateTime().isBefore(curriculum.getLastModificationDateDateTime())) {
		latestCurriculum = curriculum;
	    }
	}
	return latestCurriculum;
    }

    public Curriculum findLatestCurriculumModifiedBefore(Date date) {
	Curriculum latestCurriculum = null;

	for (Curriculum curriculum : getAssociatedCurriculums()) {
	    if (curriculum.getLastModificationDateDateTime().toDate().compareTo(date) == 1) {
		// modified after date
		continue;
	    }

	    if (latestCurriculum == null) {
		latestCurriculum = curriculum;
		continue;
	    }

	    DateTime currentLastModificationDate = latestCurriculum.getLastModificationDateDateTime();
	    if (currentLastModificationDate.isBefore(curriculum.getLastModificationDateDateTime())) {
		latestCurriculum = curriculum;
	    }
	}

	return latestCurriculum;
    }

    final public double getProblemsHours() {
	return getProblemsHours((CurricularPeriod) null, (ExecutionPeriod) null);
    }

    final public Double getProblemsHours(final CurricularPeriod curricularPeriod) {
	return getProblemsHours(curricularPeriod, (ExecutionPeriod) null);
    }

    final public double getProblemsHours(final ExecutionPeriod executionPeriod) {
	return getProblemsHours((CurricularPeriod) null, executionPeriod);
    }

    final public Double getProblemsHours(final CurricularPeriod curricularPeriod, final ExecutionPeriod executionPeriod) {
	return isBolonhaDegree() && hasCompetenceCourse() ? getCompetenceCourse().getProblemsHours(
		curricularPeriod == null ? null : curricularPeriod.getChildOrder(), executionPeriod) : 0.0d;
    }

    final public double getLaboratorialHours() {
	return getLaboratorialHours((CurricularPeriod) null, (ExecutionPeriod) null);
    }

    final public Double getLaboratorialHours(final CurricularPeriod curricularPeriod) {
	return getLaboratorialHours(curricularPeriod, (ExecutionPeriod) null);
    }

    final public double getLaboratorialHours(final ExecutionPeriod executionPeriod) {
	return getLaboratorialHours((CurricularPeriod) null, executionPeriod);
    }

    final public Double getLaboratorialHours(final CurricularPeriod curricularPeriod, final ExecutionPeriod executionPeriod) {
	return isBolonhaDegree() && hasCompetenceCourse() ? getCompetenceCourse().getLaboratorialHours(
		curricularPeriod == null ? null : curricularPeriod.getChildOrder(), executionPeriod) : 0.0d;
    }

    final public Double getSeminaryHours() {
	return getSeminaryHours((CurricularPeriod) null, (ExecutionPeriod) null);
    }

    final public Double getSeminaryHours(final CurricularPeriod curricularPeriod) {
	return getSeminaryHours(curricularPeriod, (ExecutionPeriod) null);
    }

    final public double getSeminaryHours(final ExecutionPeriod executionPeriod) {
	return getSeminaryHours((CurricularPeriod) null, executionPeriod);
    }

    final public Double getSeminaryHours(final CurricularPeriod curricularPeriod, final ExecutionPeriod executionPeriod) {
	return isBolonhaDegree() && hasCompetenceCourse() ? getCompetenceCourse().getSeminaryHours(
		curricularPeriod == null ? null : curricularPeriod.getChildOrder(), executionPeriod) : 0.0d;
    }

    final public double getFieldWorkHours() {
	return getFieldWorkHours((CurricularPeriod) null, (ExecutionPeriod) null);
    }

    final public Double getFieldWorkHours(final CurricularPeriod curricularPeriod) {
	return getFieldWorkHours(curricularPeriod, (ExecutionPeriod) null);
    }

    final public double getFieldWorkHours(final ExecutionPeriod executionPeriod) {
	return getFieldWorkHours((CurricularPeriod) null, executionPeriod);
    }

    final public Double getFieldWorkHours(final CurricularPeriod curricularPeriod, final ExecutionPeriod executionPeriod) {
	return isBolonhaDegree() && hasCompetenceCourse() ? getCompetenceCourse().getFieldWorkHours(
		curricularPeriod == null ? null : curricularPeriod.getChildOrder(), executionPeriod) : 0.0d;
    }

    final public double getTrainingPeriodHours() {
	return getTrainingPeriodHours((CurricularPeriod) null, (ExecutionPeriod) null);
    }

    final public Double getTrainingPeriodHours(final CurricularPeriod curricularPeriod) {
	return getTrainingPeriodHours(curricularPeriod, (ExecutionPeriod) null);
    }

    final public double getTrainingPeriodHours(final ExecutionPeriod executionPeriod) {
	return getTrainingPeriodHours((CurricularPeriod) null, executionPeriod);
    }

    final public Double getTrainingPeriodHours(final CurricularPeriod curricularPeriod, final ExecutionPeriod executionPeriod) {
	return isBolonhaDegree() && hasCompetenceCourse() ? getCompetenceCourse().getTrainingPeriodHours(
		curricularPeriod == null ? null : curricularPeriod.getChildOrder(), executionPeriod) : 0.0d;
    }

    final public double getTutorialOrientationHours() {
	return getTutorialOrientationHours((CurricularPeriod) null, (ExecutionPeriod) null);
    }

    final public Double getTutorialOrientationHours(final CurricularPeriod curricularPeriod) {
	return getTutorialOrientationHours(curricularPeriod, (ExecutionPeriod) null);
    }

    final public double getTutorialOrientationHours(final ExecutionPeriod executionPeriod) {
	return getTutorialOrientationHours((CurricularPeriod) null, executionPeriod);
    }

    final public Double getTutorialOrientationHours(final CurricularPeriod curricularPeriod, final ExecutionPeriod executionPeriod) {
	return isBolonhaDegree() && hasCompetenceCourse() ? getCompetenceCourse().getTutorialOrientationHours(
		curricularPeriod == null ? null : curricularPeriod.getChildOrder(), executionPeriod) : 0.0d;
    }

    final public double getAutonomousWorkHours() {
	return getAutonomousWorkHours((CurricularPeriod) null, (ExecutionPeriod) null);
    }

    final public Double getAutonomousWorkHours(final CurricularPeriod curricularPeriod) {
	return getAutonomousWorkHours(curricularPeriod, (ExecutionPeriod) null);
    }

    final public double getAutonomousWorkHours(final ExecutionPeriod executionPeriod) {
	return getAutonomousWorkHours((CurricularPeriod) null, executionPeriod);
    }

    final public Double getAutonomousWorkHours(final CurricularPeriod curricularPeriod, final ExecutionYear executionYear) {
	return getAutonomousWorkHours((CurricularPeriod) null, executionYear == null ? null : executionYear
		.getFirstExecutionPeriod());
    }

    final public Double getAutonomousWorkHours(final CurricularPeriod curricularPeriod, final ExecutionPeriod executionPeriod) {
	return isBolonhaDegree() && hasCompetenceCourse() ? getCompetenceCourse().getAutonomousWorkHours(
		curricularPeriod == null ? null : curricularPeriod.getChildOrder(), executionPeriod) : 0.0d;
    }

    final public double getContactLoad() {
	return getContactLoad((CurricularPeriod) null, (ExecutionPeriod) null);
    }

    final public Double getContactLoad(final CurricularPeriod curricularPeriod) {
	return getContactLoad(curricularPeriod, (ExecutionPeriod) null);
    }

    final public double getContactLoad(final ExecutionPeriod executionPeriod) {
	return getContactLoad((CurricularPeriod) null, executionPeriod);
    }

    final public Double getContactLoad(final CurricularPeriod curricularPeriod, final ExecutionYear executionYear) {
	return getContactLoad(curricularPeriod, executionYear == null ? null : executionYear.getFirstExecutionPeriod());
    }

    final public Double getContactLoad(final CurricularPeriod curricularPeriod, final ExecutionPeriod executionPeriod) {
	if (isBolonhaDegree()) {
	    return hasCompetenceCourse() ? getCompetenceCourse().getContactLoad(
		    curricularPeriod == null ? null : curricularPeriod.getChildOrder(), executionPeriod) : 0.0d;
	} else {
	    return getContactLoadForPreBolonha();
	}
    }

    private Double getContactLoadForPreBolonha() {
	final Double theoreticalHours = getTheoreticalHours() == null ? 0d : getTheoreticalHours();
	final Double theoPratHours = getTheoPratHours() == null ? 0d : getTheoPratHours();
	final Double praticalHours = getPraticalHours() == null ? 0d : getPraticalHours();
	final Double labHours = getLabHours() == null ? 0d : getLabHours();
	return CompetenceCourseLoad.NUMBER_OF_WEEKS
		* (theoreticalHours.doubleValue() + theoPratHours.doubleValue() + praticalHours.doubleValue() + labHours
			.doubleValue());
    }

    final public double getTotalLoad() {
	return getTotalLoad((CurricularPeriod) null, (ExecutionPeriod) null);
    }

    final public Double getTotalLoad(final CurricularPeriod curricularPeriod) {
	return getTotalLoad(curricularPeriod, (ExecutionPeriod) null);
    }

    final public double getTotalLoad(final ExecutionPeriod executionPeriod) {
	return getTotalLoad((CurricularPeriod) null, executionPeriod);
    }

    final public Double getTotalLoad(final CurricularPeriod curricularPeriod, final ExecutionYear executionYear) {
	return getTotalLoad(curricularPeriod, executionYear == null ? null : executionYear.getFirstExecutionPeriod());
    }

    final public Double getTotalLoad(final CurricularPeriod curricularPeriod, final ExecutionPeriod executionPeriod) {
	if (isBolonhaDegree()) {
	    return hasCompetenceCourse() ? getCompetenceCourse().getTotalLoad(
		    curricularPeriod == null ? null : curricularPeriod.getChildOrder(), executionPeriod) : 0.0d;
	} else {
	    return getAutonomousWorkHours() + getContactLoadForPreBolonha();
	}
    }

    @Override
    final public Double getLabHours() {
	return getLabHours((CurricularPeriod) null, (ExecutionPeriod) null);
    }

    final public Double getLabHours(final CurricularPeriod curricularPeriod) {
	return getLabHours(curricularPeriod, (ExecutionPeriod) null);
    }

    final public double getLabHours(final ExecutionPeriod executionPeriod) {
	return getLabHours((CurricularPeriod) null, executionPeriod);
    }

    final public Double getLabHours(final CurricularPeriod curricularPeriod, final ExecutionPeriod executionPeriod) {
	if (isBolonhaDegree()) {
	    return hasCompetenceCourse() ? getCompetenceCourse().getLaboratorialHours(
		    curricularPeriod == null ? null : curricularPeriod.getChildOrder(), executionPeriod) : 0.0d;
	} else {
	    final Double labHours = super.getLabHours();
	    return labHours == null ? 0.0d : labHours;
	}
    }

    @Override
    final public Double getTheoreticalHours() {
	return getTheoreticalHours((CurricularPeriod) null, (ExecutionPeriod) null);
    }

    final public Double getTheoreticalHours(final CurricularPeriod curricularPeriod) {
	return getTheoreticalHours(curricularPeriod, (ExecutionPeriod) null);
    }

    final public double getTheoreticalHours(final ExecutionPeriod executionPeriod) {
	return getTheoreticalHours((CurricularPeriod) null, executionPeriod);
    }

    final public Double getTheoreticalHours(final CurricularPeriod curricularPeriod, final ExecutionPeriod executionPeriod) {
	if (isBolonhaDegree()) {
	    return hasCompetenceCourse() ? getCompetenceCourse().getTheoreticalHours(
		    curricularPeriod == null ? null : curricularPeriod.getChildOrder(), executionPeriod) : 0.0d;
	} else {
	    final Double theoreticalHours = super.getTheoreticalHours();
	    return theoreticalHours == null ? 0.0d : theoreticalHours;
	}
    }

    @Override
    final public Double getPraticalHours() {
	final Double praticalHours = super.getPraticalHours();
	return praticalHours == null ? 0.0d : praticalHours;
    }

    @Override
    final public Double getTheoPratHours() {
	final Double theoPratHours = super.getTheoPratHours();
	return theoPratHours == null ? 0.0d : theoPratHours;
    }

    @Override
    final public Double getCredits() {
	return isBolonhaDegree() ? getEctsCredits() : super.getCredits();
    }

    @Override
    public Double getEctsCredits() {
	return getEctsCredits((CurricularPeriod) null, (ExecutionPeriod) null);
    }

    public Double getEctsCredits(final CurricularPeriod curricularPeriod) {
	return getEctsCredits(curricularPeriod, (ExecutionPeriod) null);
    }

    public Double getEctsCredits(final ExecutionPeriod executionPeriod) {
	return getEctsCredits((CurricularPeriod) null, executionPeriod);
    }

    public Double getEctsCredits(final CurricularPeriod curricularPeriod, final ExecutionYear executionYear) {
	return getEctsCredits(curricularPeriod, executionYear == null ? null : executionYear.getFirstExecutionPeriod());
    }

    public Double getEctsCredits(final CurricularPeriod curricularPeriod, final ExecutionPeriod executionPeriod) {
	return getEctsCredits(curricularPeriod == null ? null : curricularPeriod.getChildOrder(), executionPeriod);
    }

    public Double getEctsCredits(final Integer order, final ExecutionPeriod executionPeriod) {
	if (isBolonhaDegree()) {
	    if (hasCompetenceCourse()) {
		return getCompetenceCourse().getEctsCredits(order, executionPeriod);
	    } else if (isOptionalCurricularCourse()) {
		return Double.valueOf(0.0);
	    }
	} else {
	    if (getDegreeType().isMasterDegree()) {
		return getCredits();
	    }

	    final Double ectsCredits = super.getEctsCredits();
	    return (ectsCredits == null || ectsCredits == 0.0) ? ECTS_CREDITS_FOR_PRE_BOLONHA : ectsCredits;
	}

	throw new DomainException("CurricularCourse.with.no.ects.credits");
    }

    @Override
    public Double getMaxEctsCredits(final ExecutionPeriod executionPeriod) {
	return getEctsCredits(executionPeriod);
    }

    @Override
    public Double getMinEctsCredits(final ExecutionPeriod executionPeriod) {
	return getEctsCredits(executionPeriod);
    }

    @Override
    final public Double getWeigth() {
	if (isBolonhaDegree()) {
	    return getEctsCredits();
	}

	if (getDegreeType().isMasterDegree()) {
	    return getCredits();
	}

	final Double weigth = super.getWeigth();
	return (weigth == null || weigth == 0.0) ? WEIGHT_FOR_PRE_BOLONHA : weigth;
    }

    public CurricularSemester getCurricularSemesterWithLowerYearBySemester(Integer semester, Date date) {
	CurricularSemester result = null;
	for (CurricularCourseScope curricularCourseScope : getScopes()) {
	    if (curricularCourseScope.getCurricularSemester().getSemester().equals(semester)
		    && curricularCourseScope.isActive(date)) {
		if (result == null) {
		    result = curricularCourseScope.getCurricularSemester();
		} else {
		    if (result.getCurricularYear().getYear() > curricularCourseScope.getCurricularSemester().getCurricularYear()
			    .getYear()) {
			result = curricularCourseScope.getCurricularSemester();
		    }
		}
	    }
	}
	return result;
    }

    private List<Enrolment> getActiveEnrollments(ExecutionPeriod executionPeriod, Registration registration) {
	final List<Enrolment> results = new ArrayList<Enrolment>();
	for (final CurriculumModule curriculumModule : getCurriculumModules()) {
	    if (curriculumModule.isEnrolment()) {
		Enrolment enrollment = (Enrolment) curriculumModule;
		boolean filters = true;
		filters &= !enrollment.isAnnulled();
		filters &= executionPeriod == null || enrollment.getExecutionPeriod().equals(executionPeriod);
		filters &= registration == null || enrollment.getStudentCurricularPlan().getRegistration().equals(registration);

		if (filters) {
		    results.add(enrollment);
		}
	    }
	}
	return results;
    }

    public void addNotAnulledEnrolmentsForExecutionPeriod(final Collection<Enrolment> enrolments,
	    final ExecutionPeriod executionPeriod) {
	for (final CurriculumModule curriculumModule : getCurriculumModules()) {
	    if (curriculumModule.isEnrolment()) {
		final Enrolment enrolment = (Enrolment) curriculumModule;
		if (enrolment.isActive() && enrolment.getExecutionPeriod() == executionPeriod) {
		    enrolments.add(enrolment);
		}
	    }
	}
    }

    public void addActiveEnrollments(final Collection<Enrolment> enrolments, final ExecutionPeriod executionPeriod) {
	for (final CurriculumModule curriculumModule : getCurriculumModules()) {
	    if (curriculumModule.isEnrolment()) {
		final Enrolment enrolment = (Enrolment) curriculumModule;
		if (!enrolment.isAnnulled() && enrolment.getExecutionPeriod() == executionPeriod) {
		    enrolments.add(enrolment);
		}
	    }
	}
    }

    public List<Enrolment> getEnrolmentsByExecutionPeriod(final ExecutionPeriod executionPeriod) {
	List<Enrolment> result = new ArrayList<Enrolment>();
	addActiveEnrollments(result, executionPeriod);
	return result;
    }

    public List<Enrolment> getEnrolments() {
	final List<Enrolment> result = new ArrayList<Enrolment>();
	for (final CurriculumModule curriculumModule : getCurriculumModulesSet()) {
	    if (curriculumModule.isEnrolment()) {
		result.add((Enrolment) curriculumModule);
	    }
	}
	return result;
    }

    public int countEnrolmentsByExecutionPeriod(final ExecutionPeriod executionPeriod) {
	return getEnrolmentsByExecutionPeriod(executionPeriod).size();
    }

    public List<Enrolment> getEnrolmentsByYear(String year) {
	return getEnrolmentsByExecutionYear(ExecutionYear.readExecutionYearByName(year));
    }

    public int getNumberOfStudentsWithFirstEnrolmentIn(ExecutionPeriod executionPeriod) {

	Map<Student, List<Enrolment>> students = new HashMap<Student, List<Enrolment>>();
	for (Enrolment enrolment : getAllEnrolmentsUntil(executionPeriod)) {
	    Student student = enrolment.getStudentCurricularPlan().getRegistration().getStudent();
	    if (!students.containsKey(student)) {
		List<Enrolment> enrolments = new ArrayList<Enrolment>();
		enrolments.add(enrolment);
		students.put(student, enrolments);
	    } else {
		students.get(student).add(enrolment);
	    }
	}

	int count = 0;
	for (Student student : students.keySet()) {
	    boolean enrolledInExecutionPeriod = false;
	    for (Enrolment enrolment : students.get(student)) {
		if (enrolment.getExecutionPeriod().equals(executionPeriod)) {
		    enrolledInExecutionPeriod = true;
		    break;
		}
	    }
	    if (enrolledInExecutionPeriod && students.get(student).size() == 1) {
		count++;
	    }
	}

	return count;
    }

    private List<Enrolment> getAllEnrolmentsUntil(ExecutionPeriod executionPeriod) {
	List<Enrolment> result = new ArrayList<Enrolment>();
	for (final CurriculumModule curriculumModule : getCurriculumModules()) {
	    if (curriculumModule.isEnrolment()) {
		final Enrolment enrolment = (Enrolment) curriculumModule;
		ExecutionPeriod enrolmentExecutionPeriod = enrolment.getExecutionPeriod();
		if (!enrolment.isAnnulled() && enrolmentExecutionPeriod.isBeforeOrEquals(executionPeriod)) {
		    result.add(enrolment);
		}
	    }
	}
	return result;
    }

    public List<Enrolment> getEnrolmentsByExecutionYear(ExecutionYear executionYear) {
	List<Enrolment> result = new ArrayList<Enrolment>();
	for (final CurriculumModule curriculumModule : getCurriculumModules()) {
	    if (curriculumModule.isEnrolment()) {
		final Enrolment enrolment = (Enrolment) curriculumModule;
		if (enrolment.getExecutionPeriod().getExecutionYear().equals(executionYear)) {
		    result.add(enrolment);
		}
	    }
	}
	return result;
    }

    public boolean hasEnrolmentsForExecutionYear(final ExecutionYear executionYear) {
	for (final CurriculumModule curriculumModule : getCurriculumModules()) {
	    if (curriculumModule.isEnrolment()) {
		final Enrolment enrolment = (Enrolment) curriculumModule;
		if (enrolment.getExecutionPeriod().getExecutionYear().equals(executionYear)) {
		    return true;
		}
	    }
	}

	return false;
    }

    public Enrolment getEnrolmentByStudentAndYear(Registration registration, String year) {
	for (final CurriculumModule curriculumModule : getCurriculumModules()) {
	    if (curriculumModule.isEnrolment()) {
		final Enrolment enrolment = (Enrolment) curriculumModule;
		if (enrolment.getStudentCurricularPlan().getRegistration().equals(registration)
			&& enrolment.getExecutionPeriod().getExecutionYear().getYear().equals(year)) {
		    return enrolment;
		}
	    }
	}
	return null;
    }

    public List<Enrolment> getActiveEnrollments(Registration registration) {
	return getActiveEnrollments(null, registration);
    }

    public List<Enrolment> getActiveEnrollments() {
	return getActiveEnrollments(null, null);
    }

    public List<Enrolment> getActiveEnrollments(ExecutionPeriod executionPeriod) {
	List<Enrolment> enrolments = new ArrayList<Enrolment>();
	addActiveEnrollments(enrolments, executionPeriod);
	return enrolments;
    }

    public List<Enrolment> getActiveEnrollments(ExecutionYear executionYear) {
	List<Enrolment> results = new ArrayList<Enrolment>();

	for (ExecutionPeriod executionPeriod : executionYear.getExecutionPeriods()) {
	    results.addAll(getActiveEnrollments(executionPeriod));
	}

	return results;
    }

    @Override
    protected void checkContextsFor(final CourseGroup parentCourseGroup, final CurricularPeriod curricularPeriod,
	    final Context ignoreContext) {
	for (final Context context : this.getParentContexts()) {
	    if (context != ignoreContext && context.getParentCourseGroup() == parentCourseGroup
		    && context.getCurricularPeriod() == curricularPeriod) {
		throw new DomainException("courseGroup.contextAlreadyExistForCourseGroup");
	    }
	}
    }

    @Override
    protected void addOwnPartipatingCurricularRules(final List<CurricularRule> result) {
	// no rules to add
    }

    @Override
    protected void checkOwnRestrictions(final CourseGroup parentCourseGroup, final CurricularPeriod curricularPeriod,
	    final ExecutionPeriod executionPeriod) {
	if (hasCompetenceCourse() && getRegime(executionPeriod) == RegimeType.ANUAL
		&& (curricularPeriod.getChildOrder() == null || curricularPeriod.getChildOrder() != 1)) {
	    throw new DomainException("competenceCourse.anual.but.trying.to.associate.curricular.course.not.to.first.period");
	}
    }

    public String getName(ExecutionPeriod period) {
	final String superName = super.getName();
	return (superName == null || superName.length() == 0) ? getCompetenceCourse().getName(period) : superName;
    }

    @Override
    public String getName() {
	final String superName = super.getName();
	return (superName == null || superName.length() == 0) && this.getCompetenceCourse() != null ? this.getCompetenceCourse()
		.getName() : superName;
    }

    public String getNameEn(ExecutionPeriod period) {
	final String superNameEn = super.getNameEn();
	return ((superNameEn == null || superNameEn.length() == 0) && getCompetenceCourse() != null) ? getCompetenceCourse()
		.getNameEn(period) : superNameEn;
    }

    @Override
    public String getNameEn() {
	final String superNameEn = super.getNameEn();
	return (superNameEn == null || superNameEn.length() == 0) && this.getCompetenceCourse() != null ? this
		.getCompetenceCourse().getNameEn() : superNameEn;
    }

    public String getAcronym(ExecutionPeriod period) {
	return (super.getAcronym() == null || super.getAcronym().length() == 0) ? getCompetenceCourse().getAcronym(period)
		: super.getAcronym();
    }

    @Override
    public String getAcronym() {
	if ((super.getAcronym() == null || super.getAcronym().length() == 0) && this.getCompetenceCourse() != null) {
	    return this.getCompetenceCourse().getAcronym();
	}
	return super.getAcronym();
    }

    public Boolean getBasic(ExecutionPeriod period) {
	return (super.getBasic() == null) ? this.getCompetenceCourse().isBasic(period) : super.getBasic();
    }

    @Override
    public Boolean getBasic() {
	if (super.getBasic() == null && this.getCompetenceCourse() != null) {
	    return this.getCompetenceCourse().isBasic();
	}
	return super.getBasic();
    }

    public String getObjectives(ExecutionPeriod period) {
	return this.getCompetenceCourse().getObjectives(period);
    }

    public String getObjectives() {
	if (this.getCompetenceCourse() != null) {
	    return this.getCompetenceCourse().getObjectives();
	}
	return null;
    }

    public String getObjectivesEn(ExecutionPeriod period) {
	return this.getCompetenceCourse().getObjectivesEn(period);
    }

    public String getObjectivesEn() {
	if (this.getCompetenceCourse() != null) {
	    return this.getCompetenceCourse().getObjectivesEn();
	}
	return null;
    }

    public String getProgram(ExecutionPeriod period) {
	return this.getCompetenceCourse().getProgram(period);
    }

    public String getProgram() {
	if (this.getCompetenceCourse() != null) {
	    return this.getCompetenceCourse().getProgram();
	}
	return null;
    }

    public String getProgramEn(ExecutionPeriod period) {
	return this.getCompetenceCourse().getProgramEn(period);
    }

    public String getProgramEn() {
	if (this.getCompetenceCourse() != null) {
	    return this.getCompetenceCourse().getProgramEn();
	}
	return null;
    }

    public String getEvaluationMethod(ExecutionPeriod period) {
	return this.getCompetenceCourse().getEvaluationMethod(period);
    }

    public String getEvaluationMethod() {
	if (this.getCompetenceCourse() != null) {
	    return this.getCompetenceCourse().getEvaluationMethod();
	}
	return null;
    }

    public String getEvaluationMethodEn(ExecutionPeriod period) {
	return this.getCompetenceCourse().getEvaluationMethodEn(period);
    }

    public String getEvaluationMethodEn() {
	if (this.getCompetenceCourse() != null) {
	    return this.getCompetenceCourse().getEvaluationMethodEn();
	}
	return null;
    }

    public RegimeType getRegime(final ExecutionPeriod period) {
	return this.getCompetenceCourse().getRegime(period);
    }

    public RegimeType getRegime(final ExecutionYear executionYear) {
	return this.getCompetenceCourse().getRegime(executionYear);
    }

    public RegimeType getRegime() {
	if (hasCompetenceCourse()) {
	    return this.getCompetenceCourse().getRegime();
	}
	return isOptionalCurricularCourse() ? RegimeType.SEMESTRIAL : null;
    }

    public boolean hasRegime() {
	return getRegime() != null;
    }

    /**
     * Maintened for legacy code compatibility purposes only. Makes no sense to
     * check an Enrolment concept in a CurricularCourse.
     * 
     * @return true if CurricularCourseType checks accordingly
     */
    @Deprecated
    final public boolean isPropaedeutic() {
	if (isBolonhaDegree()) {
	    throw new DomainException("CurricularCourse.must.check.propaedeutic.status.in.enrolment.in.bolonha.degrees");
	}

	return getType().equals(CurricularCourseType.P_TYPE_COURSE);
    }

    public boolean isOptionalCurricularCourse() {
	return false;
    }

    @Override
    final public boolean isOptional() {
	return getType() == CurricularCourseType.OPTIONAL_COURSE;
    }

    final public boolean isTFC() {
	return getType() == CurricularCourseType.TFC_COURSE;
    }

    public boolean isDissertation() {
	CompetenceCourse competenceCourse = getCompetenceCourse();
	return competenceCourse == null ? false : competenceCourse.isDissertation();
    }

    public boolean isAnual() {
	if (!isBolonhaDegree()) {
	    return getRegimeType() == RegimeType.ANUAL;
	}
	return hasCompetenceCourse() && getCompetenceCourse().isAnual();
    }

    public boolean isAnual(final ExecutionYear executionYear) {
	if (!isBolonhaDegree()) {
	    return getRegimeType() == RegimeType.ANUAL;
	}
	return hasCompetenceCourse() && getCompetenceCourse().isAnual(executionYear);
    }

    public boolean isEquivalent(CurricularCourse oldCurricularCourse) {
	return this.equals(oldCurricularCourse)
		|| (hasCompetenceCourse() && getCompetenceCourse().hasAssociatedCurricularCourses(oldCurricularCourse));
    }

    public boolean hasScopeForCurricularYear(final Integer curricularYear, final ExecutionPeriod executionPeriod) {
	for (final DegreeModuleScope degreeModuleScope : getDegreeModuleScopes()) {
	    if (degreeModuleScope.isActiveForExecutionPeriod(executionPeriod)
		    && degreeModuleScope.getCurricularYear().equals(curricularYear)) {
		return true;
	    }
	}
	return false;
    }

    static public List<CurricularCourse> readByCurricularStage(final CurricularStage curricularStage) {
	final List<CurricularCourse> result = new ArrayList<CurricularCourse>();
	for (CurricularCourse curricularCourse : CurricularCourse.readCurricularCourses()) {
	    if (curricularCourse.getCurricularStage() != null && curricularCourse.getCurricularStage().equals(curricularStage)) {
		result.add(curricularCourse);
	    }
	}
	return result;
    }

    public Set<CurricularCourseScope> findCurricularCourseScopesIntersectingPeriod(final Date beginDate, final Date endDate) {
	final Set<CurricularCourseScope> curricularCourseScopes = new HashSet<CurricularCourseScope>();
	for (final CurricularCourseScope curricularCourseScope : getScopesSet()) {
	    if (curricularCourseScope.intersects(beginDate, endDate)) {
		curricularCourseScopes.add(curricularCourseScope);
	    }
	}
	return curricularCourseScopes;
    }

    public Set<Enrolment> getEnrolmentsNotInAnyMarkSheet(MarkSheetType markSheetType, ExecutionPeriod executionPeriod) {

	final Set<Enrolment> result = new HashSet<Enrolment>();
	for (final CurriculumModule curriculumModule : this.getCurriculumModulesSet()) {

	    if (curriculumModule.isEnrolment()) {
		final Enrolment enrolment = (Enrolment) curriculumModule;

		if (isInSamePeriod(enrolment, executionPeriod)
			&& markSheetType.getEnrolmentEvaluationType() == enrolment.getEnrolmentEvaluationType()) {
		    if (!enrolment.hasAssociatedMarkSheetOrFinalGrade(markSheetType)) {
			result.add(enrolment);
		    }
		} else if (markSheetType == MarkSheetType.IMPROVEMENT) {
		    if (enrolment.hasImprovementFor(executionPeriod) && !enrolment.hasAssociatedMarkSheet(markSheetType)) {
			result.add(enrolment);
		    }
		}
	    }
	}
	return result;
    }

    private boolean isInSamePeriod(Enrolment enrolment, ExecutionPeriod executionPeriod) {
	if (isAnual()) {
	    return enrolment.getExecutionYear() == executionPeriod.getExecutionYear();
	} else {
	    return enrolment.getExecutionPeriod() == executionPeriod;
	}
    }

    private boolean hasEnrolmentsNotInAnyMarkSheet(ExecutionPeriod executionPeriod) {
	for (final CurriculumModule curriculumModule : this.getCurriculumModulesSet()) {

	    if (curriculumModule.isEnrolment()) {
		final Enrolment enrolment = (Enrolment) curriculumModule;

		if (enrolment.getExecutionPeriod() == executionPeriod
			&& enrolment.getEnrolmentEvaluationType() == EnrolmentEvaluationType.NORMAL) {
		    if (!enrolment.hasAssociatedMarkSheetOrFinalGrade(MarkSheetType.NORMAL)) {
			return true;
		    }
		}
		if (enrolment.hasImprovement() && !enrolment.hasAssociatedMarkSheet(MarkSheetType.IMPROVEMENT)
			&& enrolment.hasImprovementFor(executionPeriod)) {
		    return true;
		}
	    }
	}
	return false;
    }

    public MarkSheet createNormalMarkSheet(ExecutionPeriod executionPeriod, Teacher responsibleTeacher, Date evaluationDate,
	    MarkSheetType markSheetType, Boolean submittedByTeacher,
	    Collection<MarkSheetEnrolmentEvaluationBean> evaluationBeans, Employee employee) {

	return MarkSheet.createNormal(this, executionPeriod, responsibleTeacher, evaluationDate, markSheetType,
		submittedByTeacher, evaluationBeans, employee);
    }

    public MarkSheet rectifyEnrolmentEvaluation(MarkSheet markSheet, EnrolmentEvaluation enrolmentEvaluation,
	    Date evaluationDate, Grade grade, String reason, Employee employee) {

	if (markSheet == null || evaluationDate == null || grade.isEmpty()) {
	    throw new DomainException("error.markSheet.invalid.arguments");
	}

	if (!markSheet.hasEnrolmentEvaluations(enrolmentEvaluation)) {
	    throw new DomainException("error.no.student.in.markSheet");
	}

	if (markSheet.isNotConfirmed()) {
	    throw new DomainException("error.markSheet.must.be.confirmed");
	}

	if (enrolmentEvaluation.hasRectification()) {
	    throw new DomainException("error.markSheet.student.alreadyRectified", enrolmentEvaluation.getEnrolment()
		    .getStudentCurricularPlan().getRegistration().getNumber().toString());
	}

	enrolmentEvaluation.setEnrolmentEvaluationState(EnrolmentEvaluationState.TEMPORARY_OBJ);
	// enrolmentEvaluation.setWhenDateTime(new DateTime());

	MarkSheet rectificationMarkSheet = createRectificationMarkSheet(markSheet.getExecutionPeriod(), evaluationDate, markSheet
		.getResponsibleTeacher(), markSheet.getMarkSheetType(), reason, new MarkSheetEnrolmentEvaluationBean(
		enrolmentEvaluation.getEnrolment(), evaluationDate, grade), employee);

	// Rectification MarkSheet MUST have only ONE EnrolmentEvaluation
	rectificationMarkSheet.getEnrolmentEvaluations().get(0).setRectified(enrolmentEvaluation);
	return rectificationMarkSheet;
    }

    public MarkSheet createRectificationMarkSheet(ExecutionPeriod executionPeriod, Date evaluationDate,
	    Teacher responsibleTeacher, MarkSheetType markSheetType, String reason,
	    MarkSheetEnrolmentEvaluationBean evaluationBean, Employee employee) {

	return MarkSheet.createRectification(this, executionPeriod, responsibleTeacher, evaluationDate, markSheetType, reason,
		evaluationBean, employee);
    }

    public Collection<MarkSheet> searchMarkSheets(ExecutionPeriod executionPeriod, Teacher teacher, Date evaluationDate,
	    MarkSheetState markSheetState, MarkSheetType markSheetType) {

	final String dateFormat = "dd/MM/yyyy";
	final Collection<MarkSheet> result = new HashSet<MarkSheet>();

	for (final MarkSheet markSheet : this.getMarkSheetsSet()) {
	    if (executionPeriod != null && markSheet.getExecutionPeriod() != executionPeriod) {
		continue;
	    }
	    if (teacher != null && markSheet.getResponsibleTeacher() != teacher) {
		continue;
	    }
	    if (evaluationDate != null
		    && DateFormatUtil.compareDates(dateFormat, evaluationDate, markSheet.getEvaluationDateDateTime().toDate()) != 0) {
		continue;
	    }
	    if (markSheetState != null && markSheet.getMarkSheetState() != markSheetState) {
		continue;
	    }
	    if (markSheetType != null && markSheet.getMarkSheetType() != markSheetType) {
		continue;
	    }
	    result.add(markSheet);
	}
	return result;
    }

    public boolean hasScopeInGivenSemesterAndCurricularYearInDCP(CurricularYear curricularYear,
	    DegreeCurricularPlan degreeCurricularPlan, final ExecutionPeriod executionPeriod) {

	if (degreeCurricularPlan == null || getDegreeCurricularPlan().equals(degreeCurricularPlan)) {
	    for (DegreeModuleScope degreeModuleScope : getDegreeModuleScopes()) {
		if (degreeModuleScope.isActiveForExecutionPeriod(executionPeriod)
			&& (curricularYear == null || degreeModuleScope.getCurricularYear().equals(curricularYear.getYear()))) {
		    return true;
		}
	    }
	}
	return false;
    }

    public boolean isGradeSubmissionAvailableFor(ExecutionPeriod executionPeriod) {
	return isGradeSubmissionAvailableFor(executionPeriod, MarkSheetType.NORMAL)
		|| isGradeSubmissionAvailableFor(executionPeriod, MarkSheetType.IMPROVEMENT)
		|| isGradeSubmissionAvailableFor(executionPeriod, MarkSheetType.SPECIAL_SEASON);
    }

    public boolean isGradeSubmissionAvailableFor(ExecutionPeriod executionPeriod, MarkSheetType type) {
	final ExecutionDegree executionDegree = getExecutionDegreeFor(executionPeriod.getExecutionYear());
	switch (type) {
	case NORMAL:
	case IMPROVEMENT:
	    if (executionPeriod.getSemester().equals(Integer.valueOf(1))) {
		return executionDegree.isDateInFirstSemesterNormalSeasonOfGradeSubmission(new YearMonthDay());
	    } else {
		return executionDegree.isDateInSecondSemesterNormalSeasonOfGradeSubmission(new YearMonthDay());
	    }

	case SPECIAL_SEASON:
	    return executionDegree.isDateInSpecialSeasonOfGradeSubmission(new YearMonthDay());

	default:
	    return false;
	}
    }

    public ExecutionDegree getExecutionDegreeFor(ExecutionYear executionYear) {
	return getDegreeCurricularPlan().getExecutionDegreeByYear(executionYear);
    }

    public boolean hasAnyDegreeGradeToSubmit(ExecutionPeriod period) {
	return hasEnrolmentsNotInAnyMarkSheet(period);
    }

    public boolean hasAnyDegreeMarkSheetToConfirm(ExecutionPeriod period) {
	for (MarkSheet markSheet : this.getMarkSheetsSet()) {
	    if (markSheet.getExecutionPeriod().equals(period) && markSheet.isNotConfirmed()) {
		return true;
	    }
	}
	return false;
    }

    public List<DegreeModuleScope> getDegreeModuleScopes() {
	return DegreeModuleScope.getDegreeModuleScopes(this);
    }

    public MultiLanguageString getNameI18N() {
	final MultiLanguageString multiLanguageString = new MultiLanguageString();
	if (getName() != null && getName().length() > 0) {
	    multiLanguageString.setContent(Language.pt, getName());
	}
	if (getNameEn() != null && getNameEn().length() > 0) {
	    multiLanguageString.setContent(Language.en, getNameEn());
	}
	return multiLanguageString;
    }

    private int countAssociatedStudentsByExecutionPeriodAndEnrolmentNumber(ExecutionPeriod executionPeriod, int enrolmentNumber) {
	int curricularCourseAndExecutionPeriodAssociatedStudents = 0;

	for (CurriculumModule curriculumModule : getCurriculumModules()) {
	    if (curriculumModule.isEnrolment()) {
		Enrolment enrolmentsEntry = (Enrolment) curriculumModule;
		if (enrolmentsEntry.getExecutionPeriod() == executionPeriod) {

		    StudentCurricularPlan studentCurricularPlanEntry = enrolmentsEntry.getStudentCurricularPlan();
		    int numberOfEnrolmentsForThatCurricularCourseAndExecutionPeriod = 0;

		    for (Enrolment enrolmentsFromStudentCPEntry : studentCurricularPlanEntry.getEnrolments()) {
			if (enrolmentsFromStudentCPEntry.getCurricularCourse() == this
				&& (enrolmentsFromStudentCPEntry.getExecutionPeriod().compareTo(executionPeriod) <= 0)) {
			    ++numberOfEnrolmentsForThatCurricularCourseAndExecutionPeriod;
			}
		    }

		    if (numberOfEnrolmentsForThatCurricularCourseAndExecutionPeriod == enrolmentNumber) {
			curricularCourseAndExecutionPeriodAssociatedStudents++;
		    }
		}
	    }
	}

	return curricularCourseAndExecutionPeriodAssociatedStudents;
    }

    public Integer getTotalEnrolmentStudentNumber(ExecutionPeriod executionPeriod) {
	int curricularCourseAndExecutionPeriodStudentNumber = 0;

	for (CurriculumModule curriculumModule : getCurriculumModules()) {
	    if (curriculumModule.isEnrolment()) {
		Enrolment enrolmentsEntry = (Enrolment) curriculumModule;
		if (enrolmentsEntry.getExecutionPeriod() == executionPeriod) {
		    curricularCourseAndExecutionPeriodStudentNumber++;
		}
	    }
	}

	return curricularCourseAndExecutionPeriodStudentNumber;
    }

    public Integer getFirstTimeEnrolmentStudentNumber(ExecutionPeriod executionPeriod) {
	return countAssociatedStudentsByExecutionPeriodAndEnrolmentNumber(executionPeriod, 1);
    }

    public Integer getSecondTimeEnrolmentStudentNumber(ExecutionPeriod executionPeriod) {
	return getTotalEnrolmentStudentNumber(executionPeriod) - getFirstTimeEnrolmentStudentNumber(executionPeriod);
    }

    public List<ExecutionCourse> getMostRecentExecutionCourses() {
	ExecutionPeriod period = ExecutionPeriod.readActualExecutionPeriod();

	while (period != null) {
	    List<ExecutionCourse> executionCourses = getExecutionCoursesByExecutionPeriod(period);
	    if (executionCourses != null && !executionCourses.isEmpty()) {
		return executionCourses;
	    }

	    period = period.getPreviousExecutionPeriod();
	}

	return new ArrayList<ExecutionCourse>();
    }

    public boolean isActive(final ExecutionYear executionYear) {
	final ExecutionYear executionYearToCheck = executionYear == null ? ExecutionYear.readCurrentExecutionYear()
		: executionYear;
	for (final ExecutionPeriod executionPeriod : executionYearToCheck.getExecutionPeriodsSet()) {
	    if (isActive(executionPeriod)) {
		return true;
	    }
	}
	return false;
    }

    public boolean isActive(final ExecutionPeriod executionPeriod) {
	return getActiveScopesInExecutionPeriod(executionPeriod).size() > 0
		|| getActiveDegreeModuleScopesInExecutionPeriod(executionPeriod).size() > 0;
    }

    public boolean hasEnrolmentForPeriod(final ExecutionPeriod executionPeriod) {
	for (final CurriculumModule curriculumModule : getCurriculumModules()) {
	    if (curriculumModule.isEnrolment()) {
		final Enrolment enrolment = (Enrolment) curriculumModule;
		if (!enrolment.isAnnulled() && enrolment.getExecutionPeriod() == executionPeriod) {
		    return true;
		}
	    }
	}
	return false;
    }

    @Override
    public void getAllDegreeModules(final Collection<DegreeModule> degreeModules) {
	degreeModules.add(this);
    }

    public List<CurricularCourseEquivalence> getOldCurricularCourseEquivalences(final DegreeCurricularPlan degreeCurricularPlan) {
	final List<CurricularCourseEquivalence> result = new ArrayList<CurricularCourseEquivalence>();
	for (final CurricularCourseEquivalence curricularCourseEquivalence : getOldCurricularCourseEquivalences()) {
	    if (curricularCourseEquivalence.isFrom(degreeCurricularPlan)) {
		result.add(curricularCourseEquivalence);
	    }
	}
	return result;
    }

    public List<CurricularCourseEquivalence> getCurricularCourseEquivalencesFor(final CurricularCourse equivalentCurricularCourse) {
	final List<CurricularCourseEquivalence> result = new ArrayList<CurricularCourseEquivalence>();
	for (final CurricularCourseEquivalence curricularCourseEquivalence : getOldCurricularCourseEquivalences()) {
	    if (curricularCourseEquivalence.getEquivalentCurricularCourse() == equivalentCurricularCourse) {
		result.add(curricularCourseEquivalence);
	    }
	}
	return result;
    }

    @Override
    public boolean isCurricularCourse() {
	return true;
    }

    public DegreeModuleScope getOldestDegreeModuleScope() {
	List<DegreeModuleScope> scopes = new ArrayList<DegreeModuleScope>(this.getDegreeModuleScopes());
	Collections.sort(scopes, DegreeModuleScope.COMPARATOR_BY_CURRICULAR_YEAR_AND_SEMESTER_AND_CURRICULAR_COURSE_NAME);
	return scopes.get(0);
    }

    @Override
    public Integer getMinimumValueForAcumulatedEnrollments() {
	return super.getMinimumValueForAcumulatedEnrollments() == null ? Integer.valueOf(0) : super
		.getMinimumValueForAcumulatedEnrollments();
    }

    @Override
    public Integer getMaximumValueForAcumulatedEnrollments() {
	return super.getMaximumValueForAcumulatedEnrollments() == null ? Integer.valueOf(0) : super
		.getMaximumValueForAcumulatedEnrollments();
    }

    public BigDecimal getTotalHoursByShiftType(ShiftType type, ExecutionPeriod executionPeriod) {
	if (type != null) {
	    Double hours = null;
	    switch (type) {
	    case TEORICA:
		hours = getTheoreticalHours(executionPeriod);
		break;
	    case TEORICO_PRATICA:
		hours = getTheoPratHours();
		break;
	    case PRATICA:
		hours = getPraticalHours();
		break;
	    case PROBLEMS:
		hours = getProblemsHours(executionPeriod);
		break;
	    case LABORATORIAL:
		hours = getLabHours(executionPeriod);
		break;
	    case TRAINING_PERIOD:
		hours = getTrainingPeriodHours(executionPeriod);
		break;
	    case SEMINARY:
		hours = getSeminaryHours(executionPeriod);
		break;
	    case TUTORIAL_ORIENTATION:
		hours = getTutorialOrientationHours(executionPeriod);
		break;
	    case FIELD_WORK:
		hours = getFieldWorkHours(executionPeriod);
		break;
	    default:
		break;
	    }
	    return hours != null ? BigDecimal.valueOf(hours).multiply(BigDecimal.valueOf(CompetenceCourseLoad.NUMBER_OF_WEEKS))
		    : null;
	}
	return null;
    }

    public boolean hasAnyExecutionCourseIn(ExecutionPeriod executionPeriod) {
	for (ExecutionCourse executionCourse : getAssociatedExecutionCoursesSet()) {
	    if (executionCourse.getExecutionPeriod().equals(executionPeriod)) {
		return true;
	    }
	}
	return false;
    }

    @Override
    public Set<CurricularCourse> getAllCurricularCourses() {
	return Collections.singleton(this);
    }

    @Override
    public Set<CurricularCourse> getAllCurricularCourses(ExecutionPeriod executionPeriod) {
	return getAllCurricularCourses();
    }

    public boolean getCanCreateMarkSheet() {
	return !isDissertation() || (isDissertation() && MarkSheetPredicates.checkDissertation());
    }

}
