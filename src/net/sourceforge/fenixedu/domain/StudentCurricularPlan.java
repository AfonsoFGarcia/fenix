package net.sourceforge.fenixedu.domain;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.BothAreasAreTheSameServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.InvalidArgumentsServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NotAuthorizedBranchChangeException;
import net.sourceforge.fenixedu.domain.branch.BranchType;
import net.sourceforge.fenixedu.domain.curriculum.CurricularCourseEnrollmentType;
import net.sourceforge.fenixedu.domain.curriculum.EnrollmentCondition;
import net.sourceforge.fenixedu.domain.curriculum.EnrollmentState;
import net.sourceforge.fenixedu.domain.degree.enrollment.CurricularCourse2Enroll;
import net.sourceforge.fenixedu.domain.degree.enrollment.NotNeedToEnrollInCurricularCourse;
import net.sourceforge.fenixedu.domain.degree.enrollment.rules.IEnrollmentRule;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.exceptions.FenixDomainException;
import net.sourceforge.fenixedu.domain.gratuity.GratuitySituationType;
import net.sourceforge.fenixedu.domain.studentCurricularPlan.Specialization;
import net.sourceforge.fenixedu.domain.studentCurricularPlan.StudentCurricularPlanState;
import net.sourceforge.fenixedu.tools.enrollment.AreaType;
import net.sourceforge.fenixedu.util.PeriodState;
import net.sourceforge.fenixedu.util.State;

import org.apache.commons.beanutils.BeanComparator;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;
import org.apache.commons.collections.Transformer;
import org.apache.commons.collections.comparators.ComparatorChain;
import org.apache.commons.collections.comparators.ReverseComparator;

/**
 * @author David Santos in Jun 24, 2004
 */

public class StudentCurricularPlan extends StudentCurricularPlan_Base {

	public static final Comparator STUDENT_CURRICULAR_PLAN_COMPARATOR_BY_DEGREE_TYPE_AND_DEGREE_NAME = new ComparatorChain();
	static {
		((ComparatorChain) STUDENT_CURRICULAR_PLAN_COMPARATOR_BY_DEGREE_TYPE_AND_DEGREE_NAME).addComparator(new BeanComparator("degreeCurricularPlan.degree.tipoCurso"));
		((ComparatorChain) STUDENT_CURRICULAR_PLAN_COMPARATOR_BY_DEGREE_TYPE_AND_DEGREE_NAME).addComparator(new BeanComparator("degreeCurricularPlan.degree.name"));		
	}
    
    public static final Comparator STUDENT_CURRICULAR_PLAN_COMPARATOR_BY_DEGREE_DEGREE_NAME_AND_STUDENT_NUMBER_AND_NAME = new ComparatorChain();
    static {
        ((ComparatorChain) STUDENT_CURRICULAR_PLAN_COMPARATOR_BY_DEGREE_DEGREE_NAME_AND_STUDENT_NUMBER_AND_NAME).addComparator(new BeanComparator("degreeCurricularPlan.degree.name"));
        ((ComparatorChain) STUDENT_CURRICULAR_PLAN_COMPARATOR_BY_DEGREE_DEGREE_NAME_AND_STUDENT_NUMBER_AND_NAME).addComparator(new BeanComparator("student.number"));        
        ((ComparatorChain) STUDENT_CURRICULAR_PLAN_COMPARATOR_BY_DEGREE_DEGREE_NAME_AND_STUDENT_NUMBER_AND_NAME).addComparator(new BeanComparator("student.person.name"));
    }

    protected Map acumulatedEnrollments; // For enrollment purposes only

    protected Integer creditsInSecundaryArea; // For enrollment purposes only

    protected Integer creditsInSpecializationArea; // For enrollment purposes

    // only

    public StudentCurricularPlan() {
        super();
        setRootDomainObject(RootDomainObject.getInstance());
        setOjbConcreteClass(getClass().getName());
    }

    public void delete() throws DomainException {
        removeDegreeCurricularPlan();
        removeStudent();
        removeBranch();
        removeEmployee();
        removeMasterDegreeThesis();
        getGratuitySituations().clear();

        for (Iterator iter = getEnrolmentsIterator(); iter.hasNext();) {
            Enrolment enrolment = (Enrolment) iter.next();
            iter.remove();
            enrolment.removeStudentCurricularPlan();
            enrolment.delete();
        }

        for (Iterator iter = getNotNeedToEnrollCurricularCoursesIterator(); iter.hasNext();) {
            NotNeedToEnrollInCurricularCourse notNeedToEnrollInCurricularCourse = (NotNeedToEnrollInCurricularCourse) iter
                    .next();
            iter.remove();
            notNeedToEnrollInCurricularCourse.removeStudentCurricularPlan();
            notNeedToEnrollInCurricularCourse.delete();
        }

        for (; !getCreditsInAnySecundaryAreas().isEmpty(); getCreditsInAnySecundaryAreas().get(0)
                .delete())
            ;

        for (Iterator iter = getCreditsInScientificAreasIterator(); iter.hasNext();) {
            CreditsInScientificArea creditsInScientificArea = (CreditsInScientificArea) iter.next();
            iter.remove();
            creditsInScientificArea.removeStudentCurricularPlan();
            creditsInScientificArea.delete();
        }

        removeRootDomainObject();
        deleteDomainObject();
    }
    
    public Integer getCreditsInSecundaryArea() {
        // only StudentCurricularPlanLEEC and StudentCurricularPlanLEIC should
        // return a value
        return new Integer(0);
    }

    public void setCreditsInSecundaryArea(Integer creditsInSecundaryArea) {
        // only StudentCurricularPlanLEEC and StudentCurricularPlanLEIC should
        // set a value
    }

    public Integer getCreditsInSpecializationArea() {
        // only StudentCurricularPlanLEEC and StudentCurricularPlanLEIC should
        // return a value
        return new Integer(0);
    }

    public void setCreditsInSpecializationArea(Integer creditsInSpecializationArea) {
        // only StudentCurricularPlanLEEC and StudentCurricularPlanLEIC should
        // set a value
    }

    public Branch getSecundaryBranch() {
        // only StudentCurricularPlanLEEC and StudentCurricularPlanLEIC should
        // return a value
        return null;
    }

    public void setSecundaryBranch(Branch secundaryBranch) {
        // only StudentCurricularPlanLEEC and StudentCurricularPlanLEIC should
        // set a value
    }

    public Integer getSecundaryBranchKey() {
        // only StudentCurricularPlanLEEC and StudentCurricularPlanLEIC should
        // return a value
        return null;
    }

    public void setSecundaryBranchKey(Integer secundaryBranchKey) {
        // only StudentCurricularPlanLEEC and StudentCurricularPlanLEIC should
        // set a value
    }

    // -------------------------------------------------------------
    // BEGIN: Only for enrollment purposes (PUBLIC)
    // -------------------------------------------------------------

    public List getAllEnrollments() {
        List<Enrolment> allEnrollments = new ArrayList<Enrolment>();
        addNonInvisibleEnrolments(allEnrollments, getEnrolments());

        for (final StudentCurricularPlan studentCurricularPlan : getStudent()
                .getStudentCurricularPlans()) {
            if (studentCurricularPlan.getCurrentState().equals(StudentCurricularPlanState.PAST)
                    || studentCurricularPlan.getCurrentState().equals(
                            StudentCurricularPlanState.INCOMPLETE)) {
                addNonInvisibleEnrolments(allEnrollments, studentCurricularPlan.getEnrolments());
            }
        }

        return allEnrollments;
    }

    private void addNonInvisibleEnrolments(List<Enrolment> allEnrollments,
            List<Enrolment> enrollmentsToAdd) {
        for (Enrolment enrolment : enrollmentsToAdd) {
            if (enrolment.getCondition() != EnrollmentCondition.INVISIBLE) {
                allEnrollments.add(enrolment);
            }
        }
    }

    public List<CurricularCourse2Enroll> getCurricularCoursesToEnroll(ExecutionPeriod executionPeriod)
            throws FenixDomainException {

        calculateStudentAcumulatedEnrollments(executionPeriod);

        List<CurricularCourse2Enroll> setOfCurricularCoursesToEnroll = getCommonBranchAndStudentBranchesCourses(executionPeriod);

        setOfCurricularCoursesToEnroll = initAcumulatedEnrollments(setOfCurricularCoursesToEnroll);

        List enrollmentRules = getListOfEnrollmentRules(executionPeriod);
        int size = enrollmentRules.size();

        for (int i = 0; i < size; i++) {
            IEnrollmentRule enrollmentRule = (IEnrollmentRule) enrollmentRules.get(i);
            setOfCurricularCoursesToEnroll = enrollmentRule.apply(setOfCurricularCoursesToEnroll);
            if (setOfCurricularCoursesToEnroll.isEmpty()) {
                break;
            }
        }

        return setOfCurricularCoursesToEnroll;
    }

    public Integer getMinimumNumberOfCoursesToEnroll() {
        return getStudent().getStudentKind().getMinCoursesToEnrol();
    }

    public Integer getMaximumNumberOfCoursesToEnroll() {
        return getStudent().getStudentKind().getMaxCoursesToEnrol();
    }

    public Integer getMaximumNumberOfAcumulatedEnrollments() {
        return getStudent().getStudentKind().getMaxNACToEnrol();
    }

    public int getNumberOfApprovedCurricularCourses() {
        int counter = 0;

        int size = getDegreeCurricularPlan().getCurricularCourses().size();
        for (int i = 0; i < size; i++) {
            CurricularCourse curricularCourse = getDegreeCurricularPlan().getCurricularCourses().get(i);
            if (isCurricularCourseApproved(curricularCourse)) {
                counter++;
            }
        }

        return counter;
    }

    public int getNumberOfEnrolledCurricularCourses() {
        return getStudentEnrollmentsWithEnrolledState().size();
    }

    protected boolean isApproved(CurricularCourse curricularCourse,
            List<CurricularCourse> approvedCourses) {
        return hasEquivalenceIn(curricularCourse, approvedCourses)
                || hasEquivalenceInNotNeedToEnroll(curricularCourse);
        // || hasEquivalenceIn(curricularCourse,
        // getStudentNotNeedToEnrollCurricularCourses());
    }

    private boolean hasEquivalenceInNotNeedToEnroll(CurricularCourse curricularCourse) {

        // if (getNotNeedToEnrollCurricularCourses().isEmpty()) {
        // return false;
        // }

        if (notNeedToEnroll(curricularCourse)) {
            return true;
        }

        for (CurricularCourseEquivalence equiv : curricularCourse.getCurricularCourseEquivalences()) {
            if (notNeedToEnroll(equiv.getOldCurricularCourse())) {
                return true;
            }
        }

        return false;
    }

    protected boolean hasEquivalenceIn(CurricularCourse curricularCourse,
            List<CurricularCourse> otherCourses) {
        if (otherCourses.isEmpty()) {
            return false;
        }

        if (isThisCurricularCoursesInTheList(curricularCourse, otherCourses)) {
            return true;
        }

        for (CurricularCourseEquivalence equiv : curricularCourse.getCurricularCourseEquivalences()) {
            if (isThisCurricularCoursesInTheList(equiv.getOldCurricularCourse(), otherCourses)) {
                return true;
            }
        }

        return false;
    }

    public boolean isCurricularCourseApprovedInCurrentOrPreviousPeriod(final CurricularCourse course,
            final ExecutionPeriod executionPeriod) {
        final List studentApprovedEnrollments = getStudentEnrollmentsWithApprovedState();
        final List<CurricularCourse> approvedCurricularCourses = new ArrayList<CurricularCourse>();

        for (Iterator iter = studentApprovedEnrollments.iterator(); iter.hasNext();) {
            Enrolment enrolment = (Enrolment) iter.next();
            if (enrolment.getExecutionPeriod().compareTo(executionPeriod) <= 0) {
                approvedCurricularCourses.add(enrolment.getCurricularCourse());
            }
        }

        return isApproved(course, approvedCurricularCourses);
    }

    public boolean isCurricularCourseApprovedWithoutEquivalencesInCurrentOrPreviousPeriod(
            final CurricularCourse course, final ExecutionPeriod executionPeriod) {
        final List studentApprovedEnrollments = getStudentEnrollmentsWithApprovedState();

        Enrolment enrolment = (Enrolment) CollectionUtils.find(studentApprovedEnrollments,
                new Predicate() {
                    public boolean evaluate(Object arg0) {
                        Enrolment enrolment = (Enrolment) arg0;
                        if ((enrolment.getCurricularCourse().getIdInternal().equals(course
                                .getIdInternal()))
                                && (enrolment.getEnrollmentState().equals(EnrollmentState.APROVED))
                                && (enrolment.getExecutionPeriod().compareTo(executionPeriod) <= 0)) {
                            return true;
                        }
                        return false;
                    }
                });

        if (enrolment != null)
            return true;
        return false;
    }

    public boolean isCurricularCourseApproved(CurricularCourse curricularCourse) {
        List studentApprovedEnrollments = getStudentEnrollmentsWithApprovedState();

        List<CurricularCourse> result = (List) CollectionUtils.collect(studentApprovedEnrollments,
                new Transformer() {
                    public Object transform(Object obj) {
                        Enrolment enrollment = (Enrolment) obj;

                        return enrollment.getCurricularCourse();

                    }
                });

        return isApproved(curricularCourse, result);
    }

    public boolean isCurricularCourseEnrolled(CurricularCourse curricularCourse) {
        List studentEnrolledEnrollments = getStudentEnrollmentsWithEnrolledState();

        List result = (List) CollectionUtils.collect(studentEnrolledEnrollments, new Transformer() {
            public Object transform(Object obj) {
                Enrolment enrollment = (Enrolment) obj;
                return enrollment.getCurricularCourse();
            }
        });

        return result.contains(curricularCourse);
    }

    public boolean isCurricularCourseEnrolledInExecutionPeriod(CurricularCourse curricularCourse,
            ExecutionPeriod executionPeriod) {

        List studentEnrolledEnrollments = getAllStudentEnrolledEnrollmentsInExecutionPeriod(executionPeriod);

        List result = (List) CollectionUtils.collect(studentEnrolledEnrollments, new Transformer() {
            public Object transform(Object obj) {
                Enrolment enrollment = (Enrolment) obj;
                return enrollment.getCurricularCourse();
            }
        });

        return result.contains(curricularCourse);
    }

    public Integer getCurricularCourseAcumulatedEnrollments(CurricularCourse curricularCourse) {

        String key = curricularCourse.getCurricularCourseUniqueKeyForEnrollment();

        Integer curricularCourseAcumulatedEnrolments = (Integer) getAcumulatedEnrollmentsMap().get(key);

        if (curricularCourseAcumulatedEnrolments == null) {
            curricularCourseAcumulatedEnrolments = new Integer(0);
        }

        if (curricularCourseAcumulatedEnrolments.intValue() >= curricularCourse
                .getMinimumValueForAcumulatedEnrollments().intValue()) {
            curricularCourseAcumulatedEnrolments = curricularCourse
                    .getMaximumValueForAcumulatedEnrollments();
        }

        if (curricularCourseAcumulatedEnrolments.intValue() == 0) {
            curricularCourseAcumulatedEnrolments = curricularCourse
                    .getMinimumValueForAcumulatedEnrollments();
        }

        return curricularCourseAcumulatedEnrolments;
    }

    public List getAllStudentEnrolledEnrollmentsInExecutionPeriod(final ExecutionPeriod executionPeriod) {

        calculateStudentAcumulatedEnrollments(executionPeriod);
        return initAcumulatedEnrollments((List) CollectionUtils.select(
                getStudentEnrollmentsWithEnrolledState(), new Predicate() {

                    public boolean evaluate(Object arg0) {

                        return ((Enrolment) arg0).getExecutionPeriod().equals(executionPeriod);
                    }
                }));
    }

    public List<Enrolment> getAllStudentEnrollmentsInExecutionPeriod(
            final ExecutionPeriod executionPeriod) {
        calculateStudentAcumulatedEnrollments(executionPeriod);
        return initAcumulatedEnrollments((List) CollectionUtils.select(getEnrolments(), new Predicate() {
            public boolean evaluate(Object arg0) {

                return ((Enrolment) arg0).getExecutionPeriod().equals(executionPeriod);
            }
        }));
    }

    public List getStudentTemporarilyEnrolledEnrollments() {

        return initAcumulatedEnrollments((List) CollectionUtils.select(getEnrolments(), new Predicate() {
            public boolean evaluate(Object obj) {
                Enrolment enrollment = (Enrolment) obj;
                return (enrollment.getEnrollmentState().equals(EnrollmentState.ENROLLED) && enrollment
                        .getCondition().equals(EnrollmentCondition.TEMPORARY));
            }
        }));
    }

    public CurricularCourseEnrollmentType getCurricularCourseEnrollmentType(
            CurricularCourse curricularCourse, ExecutionPeriod currentExecutionPeriod) {

        if (getBranch() == null) {
            if (!curricularCourse.hasActiveScopeInGivenSemester(currentExecutionPeriod.getSemester())) {
                return CurricularCourseEnrollmentType.NOT_ALLOWED;
            }
        } else {
            if (!curricularCourse.hasActiveScopeInGivenSemesterForCommonAndGivenBranch(
                    currentExecutionPeriod.getSemester(), getBranch())) {
                return CurricularCourseEnrollmentType.NOT_ALLOWED;
            }
        }

        if (isCurricularCourseApproved(curricularCourse)) {
            return CurricularCourseEnrollmentType.NOT_ALLOWED;
        }

        List enrollmentsWithEnrolledStateInCurrentExecutionPeriod = getAllStudentEnrolledEnrollmentsInExecutionPeriod(currentExecutionPeriod);

        for (int i = 0; i < enrollmentsWithEnrolledStateInCurrentExecutionPeriod.size(); i++) {
            Enrolment enrollment = (Enrolment) enrollmentsWithEnrolledStateInCurrentExecutionPeriod
                    .get(i);
            if (curricularCourse.equals(enrollment.getCurricularCourse())) {
                return CurricularCourseEnrollmentType.NOT_ALLOWED;
            }
        }

        List enrollmentsWithEnrolledStateInPreviousExecutionPeriod = getAllStudentEnrolledEnrollmentsInExecutionPeriod(currentExecutionPeriod
                .getPreviousExecutionPeriod());

        for (int i = 0; i < enrollmentsWithEnrolledStateInPreviousExecutionPeriod.size(); i++) {
            Enrolment enrollment = (Enrolment) enrollmentsWithEnrolledStateInPreviousExecutionPeriod
                    .get(i);
            if (curricularCourse.equals(enrollment.getCurricularCourse())) {
                return CurricularCourseEnrollmentType.TEMPORARY;
            }
        }

        if (isMathematicalCourse(curricularCourse)) {
            if (hasCurricularCourseEquivalenceIn(curricularCourse,
                    enrollmentsWithEnrolledStateInPreviousExecutionPeriod))
                return CurricularCourseEnrollmentType.TEMPORARY;
        }

        return CurricularCourseEnrollmentType.DEFINITIVE;
    }

    protected boolean hasActiveScopeInGivenSemester(CurricularCourse curricularCourse,
            ExecutionPeriod currentExecutionPeriod) {

        boolean result = true;
        List<CurricularCourse> curricularCoursesFromCommonBranches = new ArrayList<CurricularCourse>();
        List<Branch> commonAreas = getDegreeCurricularPlan().getCommonAreas();
        int commonAreasSize = commonAreas.size();

        for (int i = 0; i < commonAreasSize; i++) {
            Branch area = (Branch) commonAreas.get(i);
            curricularCoursesFromCommonBranches.addAll(getDegreeCurricularPlan()
                    .getCurricularCoursesFromArea(area, AreaType.BASE));
        }
        if (!curricularCoursesFromCommonBranches.contains(curricularCourse)) {

            if (this.getBranch() != null && this.getSecundaryBranch() != null) {
                if (!curricularCourse.hasActiveScopeInGivenSemesterForGivenBranch(currentExecutionPeriod
                        .getSemester(), this.getBranch())
                        && !curricularCourse.hasActiveScopeInGivenSemesterForGivenBranch(
                                currentExecutionPeriod.getSemester(), this.getSecundaryBranch())) {
                    result = false;
                }
            } else if (getBranch() != null && getSecundaryBranch() == null) {
                if (!curricularCourse.hasActiveScopeInGivenSemesterForGivenBranch(currentExecutionPeriod
                        .getSemester(), this.getBranch())) {
                    result = false;
                }
            } else if (getBranch() == null && getSecundaryBranch() != null) {
                if (!curricularCourse.hasActiveScopeInGivenSemesterForGivenBranch(currentExecutionPeriod
                        .getSemester(), this.getSecundaryBranch())) {
                    result = false;
                }
            } else if (getBranch() == null) {
                if (!curricularCourse
                        .hasActiveScopeInGivenSemester(currentExecutionPeriod.getSemester())) {
                    result = false;
                }
            }
        } else {
            result = curricularCourse
                    .hasActiveScopeInGivenSemester(currentExecutionPeriod.getSemester());
        }
        return result;
    }

    /*
     * (non-Javadoc)
     * 
     * @see Dominio.IStudentCurricularPlan#areNewAreasCompatible(Dominio.Branch,
     *      Dominio.Branch)
     */
    public boolean areNewAreasCompatible(Branch specializationArea, Branch secundaryArea)
            throws BothAreasAreTheSameServiceException, InvalidArgumentsServiceException {

        return true;
    }

    /*
     * (non-Javadoc)
     * 
     * @see Dominio.IStudentCurricularPlan#getCanChangeSpecializationArea()
     */
    public boolean getCanChangeSpecializationArea() {
        if (getBranch() != null) {
            return false;
        }
        return true;
    }

    public List getAprovedEnrolmentsInExecutionPeriod(final ExecutionPeriod executionPeriod) {
        return (List) CollectionUtils.select(getEnrolments(), new Predicate() {
            public boolean evaluate(Object obj) {
                Enrolment enrollment = (Enrolment) obj;
                if (enrollment.getEnrollmentState().equals(EnrollmentState.APROVED)
                        && enrollment.getExecutionPeriod().equals(executionPeriod))
                    return true;
                return false;
            }
        });
    }

    protected List getAprovedEnrolments() {
        return (List) CollectionUtils.select(getEnrolments(), new Predicate() {
            public boolean evaluate(Object obj) {
                Enrolment enrollment = (Enrolment) obj;
                return enrollment.getEnrollmentState().equals(EnrollmentState.APROVED);
            }
        });
    }

    // -------------------------------------------------------------

    // END: Only for enrollment purposes (PUBLIC)
    // -------------------------------------------------------------

    // -------------------------------------------------------------
    // BEGIN: Only for enrollment purposes (PROTECTED)
    // -------------------------------------------------------------

    protected void calculateStudentAcumulatedEnrollments(ExecutionPeriod executionPeriod) {
        if (getAcumulatedEnrollmentsMap() == null) {
            List enrollments = getAllEnrollmentsExceptTheOnesWithEnrolledState(executionPeriod);

            List curricularCourses = (List) CollectionUtils.collect(enrollments, new Transformer() {
                public Object transform(Object obj) {
                    CurricularCourse curricularCourse = ((Enrolment) obj).getCurricularCourse();
                    return curricularCourse.getCurricularCourseUniqueKeyForEnrollment();
                }
            });
            setAcumulatedEnrollmentsMap(CollectionUtils.getCardinalityMap(curricularCourses));
        }
    }

    protected CurricularCourse2Enroll transformToCurricularCourse2Enroll(
            CurricularCourse curricularCourse, ExecutionPeriod currentExecutionPeriod) {
        return new CurricularCourse2Enroll(curricularCourse, getCurricularCourseEnrollmentType(
                curricularCourse, currentExecutionPeriod), new Boolean(false));
    }

    public List initAcumulatedEnrollments(List elements) {
        if (getAcumulatedEnrollmentsMap() != null) {
            List result = new ArrayList();
            int size = elements.size();

            for (int i = 0; i < size; i++) {
                try {
                    Enrolment enrollment = (Enrolment) elements.get(i);
                    enrollment.setAccumulatedWeight(getCurricularCourseAcumulatedEnrollments(enrollment
                            .getCurricularCourse()));
                    result.add(enrollment);
                } catch (ClassCastException e) {
                    // FIXME shouldn't this be done in a clearer way?...

                    CurricularCourse2Enroll curricularCourse2Enroll = (CurricularCourse2Enroll) elements
                            .get(i);
                    curricularCourse2Enroll
                            .setAccumulatedWeight(getCurricularCourseAcumulatedEnrollments(curricularCourse2Enroll
                                    .getCurricularCourse()));
                    result.add(curricularCourse2Enroll);
                    // FIXME is this correct? adding a CurricularCourse2Enroll
                    // to a list of Enrolments?
                }
            }

            return result;
        }
        return elements;
    }

    protected Set getCurricularCoursesInCurricularCourseEquivalences(
            final CurricularCourse curricularCourse) {
        Set<CurricularCourse> curricularCoursesEquivalent = new HashSet<CurricularCourse>();
        List<CurricularCourse> sameCompetenceCurricularCourses;

        if (curricularCourse.getCompetenceCourse() == null) {
            sameCompetenceCurricularCourses = new ArrayList<CurricularCourse>();
            sameCompetenceCurricularCourses.add(curricularCourse);
        } else {
            sameCompetenceCurricularCourses = new ArrayList<CurricularCourse>();
            for (CurricularCourse course : curricularCourse.getCompetenceCourse()
                    .getAssociatedCurricularCourses()) {
                if (!course.isBolonha()) {
                    sameCompetenceCurricularCourses.add(course);
                }
            }
        }

        for (CurricularCourse course : sameCompetenceCurricularCourses) {
            for (CurricularCourseEquivalence curricularCourseEquivalence : course
                    .getOldCurricularCourseEquivalences()) {
                curricularCoursesEquivalent.add(curricularCourseEquivalence
                        .getEquivalentCurricularCourse());
            }
        }

        /*
         * final List curricularCourseEquivalences = getDegreeCurricularPlan()
         * .getCurricularCourseEquivalences(); List resultCurricularCourses =
         * new ArrayList(); for (int i = 0; i <
         * curricularCourseEquivalences.size(); i++) { final
         * CurricularCourseEquivalence curricularCourseEquivalence =
         * (CurricularCourseEquivalence) curricularCourseEquivalences .get(i);
         * if (areTheseCurricularCoursesTheSame(curricularCourseEquivalence
         * .getOldCurricularCourse(), curricularCourse)) {
         * resultCurricularCourses.add(curricularCourseEquivalence
         * .getEquivalentCurricularCourse()); } }
         */

        return curricularCoursesEquivalent;
    }

    protected boolean areTheseCurricularCoursesTheSame(CurricularCourse curricularCourse1,
            CurricularCourse curricularCourse2) {

        return curricularCourse1.getCurricularCourseUniqueKeyForEnrollment().equals(
                curricularCourse2.getCurricularCourseUniqueKeyForEnrollment());
    }

    protected boolean isThisCurricularCoursesInTheList(final CurricularCourse curricularCourse,
            List<CurricularCourse> curricularCourses) {
        /*
         * CurricularCourse curricularCourseFound = (CurricularCourse)
         * CollectionUtils.find( curricularCourses, new Predicate() { public
         * boolean evaluate(Object obj) { ICurricularCourse
         * curricularCourseToCompare = (CurricularCourse) obj; return
         * curricularCourseToCompare.getCurricularCourseUniqueKeyForEnrollment()
         * .equals(curricularCourse.getCurricularCourseUniqueKeyForEnrollment()); }
         * });
         */
        for (CurricularCourse otherCourse : curricularCourses) {
            if ((curricularCourse == otherCourse) || haveSameCompetence(curricularCourse, otherCourse)) {
                return true;
            }
        }
        return false;
    }

    protected boolean notNeedToEnroll(final CurricularCourse curricularCourse) {

        for (NotNeedToEnrollInCurricularCourse needToEnrollInCurricularCourse : getNotNeedToEnrollCurricularCourses()) {
            CurricularCourse otherCourse = needToEnrollInCurricularCourse.getCurricularCourse();
            if ((curricularCourse == otherCourse) || haveSameCompetence(curricularCourse, otherCourse)) {
                return true;
            }
        }
        return false;
    }

    private boolean haveSameCompetence(CurricularCourse course1, CurricularCourse course2) {
        CompetenceCourse comp1 = course1.getCompetenceCourse();
        CompetenceCourse comp2 = course2.getCompetenceCourse();
        return (comp1 != null) && (comp1 == comp2);
    }

    public List getStudentEnrollmentsWithApprovedState() {

        return (List) CollectionUtils.select(getAllEnrollments(), new Predicate() {
            public boolean evaluate(Object obj) {
                Enrolment enrollment = (Enrolment) obj;
                return enrollment.getEnrollmentState().equals(EnrollmentState.APROVED);
            }
        });
    }

    public int getNumberOfStudentEnrollmentsWithApprovedState() {

        return CollectionUtils.countMatches(getAllEnrollments(), new Predicate() {
            public boolean evaluate(Object obj) {
                Enrolment enrollment = (Enrolment) obj;
                return enrollment.getEnrollmentState().equals(EnrollmentState.APROVED);
            }
        });
    }

    public int getNumberOfStudentEnrollments() {
        return getAllEnrollments().size();
    }

    protected List getStudentEnrollmentsWithEnrolledState() {

        return (List) CollectionUtils.select(getEnrolments(), new Predicate() {
            public boolean evaluate(Object obj) {
                Enrolment enrollment = (Enrolment) obj;
                return enrollment.getEnrollmentState().equals(EnrollmentState.ENROLLED)
                        && !enrollment.getCondition().equals(EnrollmentCondition.INVISIBLE);
            }
        });
    }

    protected List getAllEnrollmentsExceptTheOnesWithEnrolledState(final ExecutionPeriod executionPeriod) {
        final ExecutionPeriod actualExecutionCourse = getActualExecutionPeriod(executionPeriod);
        return (List) CollectionUtils.select(getAllEnrollments(), new Predicate() {
            public boolean evaluate(Object obj) {
                Enrolment enrollment = (Enrolment) obj;
                return !enrollment.getEnrollmentState().equals(EnrollmentState.ANNULED)
                        && !(enrollment.getEnrollmentState().equals(EnrollmentState.ENROLLED) && enrollment
                                .getExecutionPeriod().equals(actualExecutionCourse));
            }
        });
    }

    private ExecutionPeriod getActualExecutionPeriod(ExecutionPeriod executionPeriod) {
        ExecutionPeriod executionPeriodAux = executionPeriod;
        while (executionPeriodAux != null) {
            if (executionPeriodAux.getState().equals(PeriodState.CURRENT)) {
                return executionPeriodAux;
            }
            executionPeriodAux = executionPeriodAux.getNextExecutionPeriod();
        }
        executionPeriodAux = executionPeriod;
        while (executionPeriodAux != null) {
            if (executionPeriodAux.getState().equals(PeriodState.CURRENT)) {
                return executionPeriodAux;
            }
            executionPeriodAux = executionPeriodAux.getPreviousExecutionPeriod();
        }
        return null;
    }

    protected Map getAcumulatedEnrollmentsMap() {
        return acumulatedEnrollments;
    }

    protected void setAcumulatedEnrollmentsMap(Map acumulatedEnrollments) {
        this.acumulatedEnrollments = acumulatedEnrollments;
    }

    protected List getListOfEnrollmentRules(ExecutionPeriod executionPeriod) {
        return getDegreeCurricularPlan().getListOfEnrollmentRules(this, executionPeriod);
    }

    protected List<CurricularCourse> getStudentNotNeedToEnrollCurricularCourses() {
        return (List<CurricularCourse>) CollectionUtils.collect(getNotNeedToEnrollCurricularCourses(),
                new Transformer() {
                    public Object transform(Object obj) {
                        NotNeedToEnrollInCurricularCourse notNeedToEnrollInCurricularCourse = (NotNeedToEnrollInCurricularCourse) obj;
                        return notNeedToEnrollInCurricularCourse.getCurricularCourse();
                    }
                });
    }

    protected List<CurricularCourse2Enroll> getCommonBranchAndStudentBranchesCourses(
            ExecutionPeriod executionPeriod) {
        Set<CurricularCourse> curricularCourses = new HashSet<CurricularCourse>();
        DegreeCurricularPlan degreeCurricularPlan = getDegreeCurricularPlan();
        List commonAreas = degreeCurricularPlan.getCommonAreas();
        int commonAreasSize = commonAreas.size();

        for (int i = 0; i < commonAreasSize; i++) {
            Branch area = (Branch) commonAreas.get(i);
            curricularCourses.addAll(degreeCurricularPlan.getCurricularCoursesFromArea(area,
                    AreaType.BASE));
        }

        if (getBranch() != null) {
            curricularCourses.addAll(degreeCurricularPlan.getCurricularCoursesFromArea(getBranch(),
                    AreaType.SPECIALIZATION));
        }

        if (getSecundaryBranch() != null) {
            curricularCourses.addAll(degreeCurricularPlan.getCurricularCoursesFromArea(
                    getSecundaryBranch(), AreaType.SECONDARY));
        }

        curricularCourses.addAll(degreeCurricularPlan.getTFCs());

        List<CurricularCourse> allCurricularCourses = new ArrayList<CurricularCourse>(curricularCourses
                .size());
        allCurricularCourses.addAll(curricularCourses);

        List<CurricularCourse2Enroll> result = new ArrayList<CurricularCourse2Enroll>();
        int curricularCoursesSize = curricularCourses.size();

        for (int i = 0; i < curricularCoursesSize; i++) {
            CurricularCourse curricularCourse = (CurricularCourse) allCurricularCourses.get(i);
            result.add(transformToCurricularCourse2Enroll(curricularCourse, executionPeriod));
        }

        markOptionalCurricularCourses(allCurricularCourses, result, executionPeriod);

        List elementsToRemove = (List) CollectionUtils.select(result, new Predicate() {
            public boolean evaluate(Object obj) {
                CurricularCourse2Enroll curricularCourse2Enroll = (CurricularCourse2Enroll) obj;
                return curricularCourse2Enroll.getEnrollmentType().equals(
                        CurricularCourseEnrollmentType.NOT_ALLOWED)
                        || !curricularCourse2Enroll.getCurricularCourse().getEnrollmentAllowed()
                                .booleanValue();
            }
        });

        result.removeAll(elementsToRemove);

        return result;
    }

    protected void markOptionalCurricularCourses(List curricularCourses, List result,
            ExecutionPeriod executionPeriod) {

        List allOptionalCurricularCourseGroups = getDegreeCurricularPlan()
                .getAllOptionalCurricularCourseGroups();

        List<CurricularCourse> curricularCoursesToRemove = new ArrayList<CurricularCourse>();
        List<CurricularCourse> curricularCoursesToKeep = new ArrayList<CurricularCourse>();

        int size = allOptionalCurricularCourseGroups.size();
        for (int i = 0; i < size; i++) {

            CurricularCourseGroup optionalCurricularCourseGroup = (CurricularCourseGroup) allOptionalCurricularCourseGroups
                    .get(i);

            if (getBranch() != null && getSecundaryBranch() != null) {
                if (optionalCurricularCourseGroup.getBranch().equals(getBranch())
                        || optionalCurricularCourseGroup.getBranch().equals(getSecundaryBranch())) {
                    selectOptionalCoursesToBeRemoved(curricularCoursesToRemove, curricularCoursesToKeep,
                            optionalCurricularCourseGroup, executionPeriod);
                }
            } else if (getBranch() != null && getSecundaryBranch() == null) {
                if (optionalCurricularCourseGroup.getBranch().equals(getBranch())) {
                    selectOptionalCoursesToBeRemoved(curricularCoursesToRemove, curricularCoursesToKeep,
                            optionalCurricularCourseGroup, executionPeriod);
                }
            } else if (getBranch() == null && getSecundaryBranch() != null) {
                if (optionalCurricularCourseGroup.getBranch().equals(getSecundaryBranch())) {
                    selectOptionalCoursesToBeRemoved(curricularCoursesToRemove, curricularCoursesToKeep,
                            optionalCurricularCourseGroup, executionPeriod);
                }
            } else if (getBranch() == null) {
                if (optionalCurricularCourseGroup.getBranch().getBranchType().equals(BranchType.COMNBR)) {
                    selectOptionalCoursesToBeRemoved(curricularCoursesToRemove, curricularCoursesToKeep,
                            optionalCurricularCourseGroup, executionPeriod);
                }
            }

        }

        size = result.size();
        for (int i = 0; i < size; i++) {
            CurricularCourse2Enroll curricularCourse2Enroll = (CurricularCourse2Enroll) result.get(i);

            if (curricularCoursesToRemove.contains(curricularCourse2Enroll.getCurricularCourse())
                    && !curricularCoursesToKeep.contains(curricularCourse2Enroll.getCurricularCourse())) {
                curricularCourse2Enroll.setEnrollmentType(CurricularCourseEnrollmentType.NOT_ALLOWED);
            } else if (curricularCoursesToKeep.contains(curricularCourse2Enroll.getCurricularCourse())) {
                curricularCourse2Enroll.setOptionalCurricularCourse(new Boolean(true));
            }
        }
    }

    protected void selectOptionalCoursesToBeRemoved(List<CurricularCourse> curricularCoursesToRemove,
            List<CurricularCourse> curricularCoursesToKeep,
            CurricularCourseGroup optionalCurricularCourseGroup, ExecutionPeriod executionPeriod) {
        int count = 0;

        int size2 = optionalCurricularCourseGroup.getCurricularCourses().size();
        for (int j = 0; j < size2; j++) {
            CurricularCourse curricularCourse = optionalCurricularCourseGroup.getCurricularCourses()
                    .get(j);
            if (isCurricularCourseEnrolledInExecutionPeriod(curricularCourse, executionPeriod)
                    || isCurricularCourseApproved(curricularCourse)) {
                count++;
            }
        }

        if (count >= optionalCurricularCourseGroup.getMaximumNumberOfOptionalCourses().intValue()) {
            curricularCoursesToRemove.addAll(optionalCurricularCourseGroup.getCurricularCourses());
        } else {
            curricularCoursesToKeep.addAll(optionalCurricularCourseGroup.getCurricularCourses());
        }
    }

    protected boolean hasCurricularCourseEquivalenceIn(CurricularCourse curricularCourse,
            List curricularCoursesEnrollments) {

        int size = curricularCoursesEnrollments.size();
        for (int i = 0; i < size; i++) {
            CurricularCourse tempCurricularCourse = ((Enrolment) curricularCoursesEnrollments.get(i))
                    .getCurricularCourse();
            Set curricularCourseEquivalences = getCurricularCoursesInCurricularCourseEquivalences(tempCurricularCourse);
            if (curricularCourseEquivalences.contains(curricularCourse)) {
                return true;
            }
        }

        List<CurricularCourse> studentNotNeedToEnrollCourses = getStudentNotNeedToEnrollCurricularCourses();

        if (isThisCurricularCoursesInTheList(curricularCourse, studentNotNeedToEnrollCourses)) {
            return true;
        }

        size = studentNotNeedToEnrollCourses.size();
        for (int i = 0; i < size; i++) {
            CurricularCourse ccNotNeedToDo = (CurricularCourse) studentNotNeedToEnrollCourses.get(i);
            Set curricularCourseEquivalences = getCurricularCoursesInCurricularCourseEquivalences(ccNotNeedToDo);
            if (curricularCourseEquivalences.contains(curricularCourse)) {
                return true;
            }
        }

        return false;
    }

    /**
     * 
     * @param curricularCourse
     * @return
     */
    protected boolean isMathematicalCourse(CurricularCourse curricularCourse) {
        String code = curricularCourse.getCode();

        return code.equals("QN") || code.equals("PY") || code.equals("P5") || code.equals("UN")
                || code.equals("U8") || code.equals("AZ2") || code.equals("AZ3") || code.equals("AZ4")
                || code.equals("AZ5") || code.equals("AZ6");
    }

    // -------------------------------------------------------------
    // END: Only for enrollment purposes (PROTECTED)
    // -------------------------------------------------------------

    public StudentCurricularPlan(Student student, DegreeCurricularPlan degreeCurricularPlan,
            Branch branch, Date startDate, StudentCurricularPlanState currentState, Double givenCredits,
            Specialization specialization) {

        this(student, degreeCurricularPlan, currentState, startDate);

        setBranch(branch);
        setGivenCredits(givenCredits);
        setSpecialization(specialization);
    }

    public StudentCurricularPlan(Student student, DegreeCurricularPlan degreeCurricularPlan,
            StudentCurricularPlanState studentCurricularPlanState, Date startDate) {

        this.setRootDomainObject(RootDomainObject.getInstance());
        this.setOjbConcreteClass(getClass().getName());

        setCurrentState(studentCurricularPlanState);
        setDegreeCurricularPlan(degreeCurricularPlan);
        setStartDate(startDate);
        setStudent(student);
        setWhen(new Date());

        if (!canSetStateToActive()
                && studentCurricularPlanState.equals(StudentCurricularPlanState.ACTIVE)) {
            inactivateTheActiveStudentCurricularPlan();
            setCurrentState(studentCurricularPlanState);
        }
    }

    public void changeState(StudentCurricularPlanState studentCurricularPlanState)
            throws DomainException {

        if (!canSetStateToActive()
                && studentCurricularPlanState.equals(StudentCurricularPlanState.ACTIVE))
            throw new DomainException("error.student.curricular.plan.state.conflict");
        else
            setCurrentState(studentCurricularPlanState);
    }

    private boolean canSetStateToActive() {

        final Student student = getStudent();
        final DegreeCurricularPlan degreeCurricularPlan = getDegreeCurricularPlan();

        for (StudentCurricularPlan otherStudentCurricularPlan : student.getStudentCurricularPlans()) {
            final DegreeCurricularPlan associatedDegreeCurricularPlan = otherStudentCurricularPlan
                    .getDegreeCurricularPlan();

            if (associatedDegreeCurricularPlan.getIdInternal().equals(
                    degreeCurricularPlan.getIdInternal())
                    && otherStudentCurricularPlan.getCurrentState().equals(
                            StudentCurricularPlanState.ACTIVE)) {

                // there can be only one active student curricular plan for the
                // same student and degree curricular plan
                return false;
            }
        }

        return true;
    }

    private void inactivateTheActiveStudentCurricularPlan() {

        final Student student = getStudent();
        final DegreeCurricularPlan degreeCurricularPlan = getDegreeCurricularPlan();

        for (StudentCurricularPlan otherStudentCurricularPlan : student.getStudentCurricularPlans()) {
            final DegreeCurricularPlan associatedDegreeCurricularPlan = otherStudentCurricularPlan
                    .getDegreeCurricularPlan();

            if (associatedDegreeCurricularPlan.getIdInternal().equals(
                    degreeCurricularPlan.getIdInternal())
                    && otherStudentCurricularPlan.getCurrentState().equals(
                            StudentCurricularPlanState.ACTIVE)) {

                otherStudentCurricularPlan.setCurrentState(StudentCurricularPlanState.INACTIVE);
            }
        }
    }

    public Enrolment getEnrolmentByCurricularCourseAndExecutionPeriod(
            final CurricularCourse curricularCourse, final ExecutionPeriod executionPeriod) {

        return (Enrolment) CollectionUtils.find(getEnrolments(), new Predicate() {
            public boolean evaluate(Object o) {
                Enrolment enrolment = (Enrolment) o;
                return (enrolment.getCurricularCourse().equals(curricularCourse))
                        && (enrolment.getExecutionPeriod().equals(executionPeriod));
            }
        });
    }

    public void setStudentAreasWithoutRestrictions(Branch specializationArea, Branch secundaryArea)
            throws DomainException {

        if (specializationArea != null && secundaryArea != null
                && specializationArea.equals(secundaryArea))
            throw new DomainException("error.student.curricular.plan.areas.conflict");

        setBranch(specializationArea);
        setSecundaryBranch(secundaryArea);
    }

    public void setStudentAreas(Branch specializationArea, Branch secundaryArea)
            throws NotAuthorizedBranchChangeException, BothAreasAreTheSameServiceException,
            InvalidArgumentsServiceException, DomainException {

        if (!getCanChangeSpecializationArea())
            throw new NotAuthorizedBranchChangeException();

        if (areNewAreasCompatible(specializationArea, secundaryArea))
            setStudentAreasWithoutRestrictions(specializationArea, secundaryArea);

        else
            throw new DomainException("error.student.curricular.plan.areas.conflict");
    }

    public GratuitySituation getGratuitySituationByGratuityValues(final GratuityValues gratuityValues) {

        return (GratuitySituation) CollectionUtils.find(getGratuitySituations(), new Predicate() {
            public boolean evaluate(Object arg0) {
                GratuitySituation gratuitySituation = (GratuitySituation) arg0;
                return gratuitySituation.getGratuityValues().equals(gratuityValues);
            }
        });

    }

    public GratuitySituation getGratuitySituationByGratuityValuesAndGratuitySituationType(
            final GratuitySituationType gratuitySituationType, final GratuityValues gratuityValues) {

        GratuitySituation gratuitySituation = this.getGratuitySituationByGratuityValues(gratuityValues);
        if (gratuitySituation != null
                && (gratuitySituationType == null || ((gratuitySituationType
                        .equals(GratuitySituationType.CREDITOR) && gratuitySituation.getRemainingValue() < 0.0)
                        || (gratuitySituationType.equals(GratuitySituationType.DEBTOR) && gratuitySituation
                                .getRemainingValue() > 0.0) || (gratuitySituationType
                        .equals(GratuitySituationType.REGULARIZED) && gratuitySituation
                        .getRemainingValue() == 0.0)))) {
            return gratuitySituation;
        }
        return null;
    }

    public boolean isEnroledInSpecialSeason(ExecutionPeriod executionPeriod) {
        List<Enrolment> enrolments = getAllStudentEnrollmentsInExecutionPeriod(executionPeriod);
        for (Enrolment enrolment : enrolments) {
            if (enrolment.hasSpecialSeason()) {
                return true;
            }
        }
        return false;
    }

    public List<Enrolment> getEnrolments(final CurricularCourse curricularCourse) {
        List<Enrolment> results = new ArrayList<Enrolment>();

        for (Enrolment enrolment : this.getEnrolments()) {
            if (enrolment.getCurricularCourse().equals(curricularCourse)) {
                results.add(enrolment);
            }
        }

        return results;
    }

    public int countEnrolmentsByCurricularCourse(final CurricularCourse curricularCourse) {
        int count = 0;
        for (Enrolment enrolment : this.getEnrolments()) {
            if (enrolment.getCurricularCourse() == curricularCourse) {
                count++;
            }
        }
        return count;
    }

    public List<Enrolment> getEnrolmentsByState(EnrollmentState state) {
        List<Enrolment> results = new ArrayList<Enrolment>();
        for (Enrolment enrolment : this.getEnrolments()) {
            if (enrolment.getEnrollmentState().equals(state)) {
                results.add(enrolment);
            }
        }
        return results;
    }

    public List<Enrolment> getEnrolmentsByExecutionPeriod(final ExecutionPeriod executionPeriod) {
        List<Enrolment> results = new ArrayList<Enrolment>();
        for (Enrolment enrolment : this.getEnrolments()) {
            if (enrolment.getExecutionPeriod().equals(executionPeriod)) {
                results.add(enrolment);
            }
        }
        return results;
    }

    public MasterDegreeProofVersion readActiveMasterDegreeProofVersion() {
        MasterDegreeThesis masterDegreeThesis = this.getMasterDegreeThesis();
        if (masterDegreeThesis != null) {
            for (MasterDegreeProofVersion masterDegreeProofVersion : masterDegreeThesis
                    .getMasterDegreeProofVersions()) {
                if (masterDegreeProofVersion.getCurrentState().getState().equals(State.ACTIVE)) {
                    return masterDegreeProofVersion;
                }
            }
        }
        return null;
    }

    public List<MasterDegreeProofVersion> readNotActiveMasterDegreeProofVersions() {
        MasterDegreeThesis masterDegreeThesis = this.getMasterDegreeThesis();
        List<MasterDegreeProofVersion> masterDegreeProofVersions = new ArrayList<MasterDegreeProofVersion>();
        if (masterDegreeThesis != null) {
            for (MasterDegreeProofVersion masterDegreeProofVersion : masterDegreeThesis
                    .getMasterDegreeProofVersions()) {
                if (!masterDegreeProofVersion.getCurrentState().getState().equals(State.ACTIVE)) {
                    masterDegreeProofVersions.add(masterDegreeProofVersion);
                }
            }
        }
        Collections.sort(masterDegreeProofVersions, new ReverseComparator(
                MasterDegreeProofVersion.LAST_MODIFICATION_COMPARATOR));
        return masterDegreeProofVersions;
    }

    public MasterDegreeThesisDataVersion readActiveMasterDegreeThesisDataVersion() {
        MasterDegreeThesis masterDegreeThesis = this.getMasterDegreeThesis();
        if (masterDegreeThesis != null) {
            for (MasterDegreeThesisDataVersion masterDegreeThesisDataVersion : masterDegreeThesis
                    .getMasterDegreeThesisDataVersions()) {
                if (masterDegreeThesisDataVersion.getCurrentState().getState().equals(State.ACTIVE)) {
                    return masterDegreeThesisDataVersion;
                }
            }
        }
        return null;
    }

    public List<MasterDegreeThesisDataVersion> readNotActiveMasterDegreeThesisDataVersions() {
        MasterDegreeThesis masterDegreeThesis = this.getMasterDegreeThesis();
        List<MasterDegreeThesisDataVersion> masterDegreeThesisDataVersions = new ArrayList<MasterDegreeThesisDataVersion>();
        if (masterDegreeThesis != null) {
            for (MasterDegreeThesisDataVersion masterDegreeThesisDataVersion : masterDegreeThesis
                    .getMasterDegreeThesisDataVersions()) {
                if (!masterDegreeThesisDataVersion.getCurrentState().getState().equals(State.ACTIVE)) {
                    masterDegreeThesisDataVersions.add(masterDegreeThesisDataVersion);
                }
            }
        }
        Collections.sort(masterDegreeThesisDataVersions, new ReverseComparator(
                MasterDegreeThesisDataVersion.LAST_MODIFICATION_COMPARATOR));
        return masterDegreeThesisDataVersions;
    }

    public Enrolment findEnrolmentByEnrolmentID(final Integer enrolmentID) {
        for (final Enrolment enrolment : getEnrolmentsSet()) {
            if (enrolment.getIdInternal().equals(enrolmentID)) {
                return enrolment;
            }
        }
        return null;
    }

    public boolean approvedInAllCurricularCoursesUntilInclusiveCurricularYear(final Integer curricularYearInteger) {
    	final DegreeCurricularPlan degreeCurricularPlan = getDegreeCurricularPlan();
    	for (final CurricularCourse curricularCourse : degreeCurricularPlan.getCurricularCoursesSet()) {
    		final Collection<CurricularCourseScope> activeCurricularCourseScopes = curricularCourse.getActiveScopes();
    		for (final CurricularCourseScope curricularCourseScope : activeCurricularCourseScopes) {
    			final CurricularSemester curricularSemester = curricularCourseScope.getCurricularSemester();
    			final CurricularYear curricularYear = curricularSemester.getCurricularYear();
    			if (curricularYearInteger == null || curricularYear.getYear().intValue() <= curricularYearInteger.intValue()) {
    				if (!isCurricularCourseApproved(curricularCourse)) {
    					System.out.println("curricular course failed: " + curricularCourse.getName() + " " + curricularCourse.getCode());
    					return false;
    				}
    			}
    		}
    	}
    	return true;
    }

    public int numberCompletedCoursesForSpecifiedDegrees(final Set<Degree> degrees) {
    	int numberCompletedCourses = 0;
    	for (final StudentCurricularPlan studentCurricularPlan : getStudent().getStudentCurricularPlansSet()) {
    		for (Enrolment enrolment : studentCurricularPlan.getEnrolments()) {
                if (enrolment.getCondition() != EnrollmentCondition.INVISIBLE
                		&& enrolment.getEnrollmentState() == EnrollmentState.APROVED) {
                	final ExecutionPeriod executionPeriod = enrolment.getExecutionPeriod();
                	final ExecutionYear executionYear = executionPeriod.getExecutionYear();
                	if (!PeriodState.CURRENT.equals(executionYear.getState())) {
                		final CurricularCourse curricularCourse = enrolment.getCurricularCourse();
                		final DegreeCurricularPlan degreeCurricularPlan = curricularCourse.getDegreeCurricularPlan();
                		final Degree degree = degreeCurricularPlan.getDegree();
                		final CompetenceCourse competenceCourse = curricularCourse.getCompetenceCourse();
                		if (degrees.contains(degree) || (competenceCourse != null && competenceCourse.isAssociatedToAnyDegree(degrees))) {
                			numberCompletedCourses++;
                		}
                	}
                }
    		}
    	}
    	return numberCompletedCourses;
    }

}
