package net.sourceforge.fenixedu.presentationTier.Action.coordinator;

import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.Filtro.exception.FenixFilterException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.domain.Attends;
import net.sourceforge.fenixedu.domain.CurricularCourse;
import net.sourceforge.fenixedu.domain.CurricularCourseScope;
import net.sourceforge.fenixedu.domain.CurricularSemester;
import net.sourceforge.fenixedu.domain.CurricularYear;
import net.sourceforge.fenixedu.domain.DegreeCurricularPlan;
import net.sourceforge.fenixedu.domain.DomainObject;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.ExecutionDegree;
import net.sourceforge.fenixedu.domain.ExecutionPeriod;
import net.sourceforge.fenixedu.domain.StudentCurricularPlan;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.student.WeeklyWorkLoad;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;
import net.sourceforge.fenixedu.util.PeriodState;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;
import org.joda.time.DateMidnight;
import org.joda.time.DateTime;
import org.joda.time.Interval;
import org.joda.time.Period;

public class WeeklyWorkLoadDA extends FenixDispatchAction {

    public class CurricularYearWeeklyWorkLoadView {
        final Interval interval;
        final int numberOfWeeks;

        final Interval[] intervals;
        final int[] numberResponses;
        final int[] contactSum;
        final int[] autonomousStudySum;
        final int[] otherSum;
        final int[] totalSum;

        final Set<ExecutionCourse> executionCourses = new HashSet<ExecutionCourse>();

        public CurricularYearWeeklyWorkLoadView(final DegreeCurricularPlan degreeCurricularPlan,
                final ExecutionPeriod executionPeriod, final Set<ExecutionCourse> executionCourses) {
            final ExecutionDegree executionDegree = findExecutionDegree(executionPeriod, degreeCurricularPlan);
            this.interval = new Interval(
                    new DateMidnight(getBegginingOfLessonPeriod(executionPeriod, executionDegree)),
                    new DateMidnight(getEndOfExamsPeriod(executionPeriod, executionDegree)));
            final Period period = interval.toPeriod();
            int extraWeek = period.getDays() > 0 ? 1 : 0;
            numberOfWeeks = (period.getYears() * 12 + period.getMonths()) * 4 + period.getWeeks() + extraWeek;
            intervals = new Interval[numberOfWeeks];
            numberResponses = new int[numberOfWeeks];
            contactSum = new int[numberOfWeeks];
            autonomousStudySum = new int[numberOfWeeks];
            otherSum = new int[numberOfWeeks];
            totalSum = new int[numberOfWeeks];
            for (int i = 0; i < numberOfWeeks; i++) {
                final DateTime start = interval.getStart().plusWeeks(i);
                final DateTime end = start.plusWeeks(1);
                intervals[i] = new Interval(start, end);
            }

            for (final ExecutionCourse executionCourse : executionCourses) {
                for (final Attends attends : executionCourse.getAttends()) {
                    add(attends);
                }
            }
        }

        private ExecutionDegree findExecutionDegree(final ExecutionPeriod executionPeriod, final DegreeCurricularPlan degreeCurricularPlan) {
            return degreeCurricularPlan.getExecutionDegreeByYear(executionPeriod.getExecutionYear());
        }

        public Date getBegginingOfLessonPeriod(final ExecutionPeriod executionPeriod, final ExecutionDegree executionDegree) {
            if (executionPeriod.getSemester().intValue() == 1) {
                return executionDegree.getPeriodLessonsFirstSemester().getStart();
            } else if (executionPeriod.getSemester().intValue() == 2) {
                return executionDegree.getPeriodLessonsSecondSemester().getStart();
            } else {
                throw new DomainException("unsupported.execution.period.semester");
            }
        }

        public Date getEndOfExamsPeriod(final ExecutionPeriod executionPeriod, final ExecutionDegree executionDegree) {
            if (executionPeriod.getSemester().intValue() == 1) {
                return executionDegree.getPeriodExamsFirstSemester().getEnd();
            } else if (executionPeriod.getSemester().intValue() == 2) {
                return executionDegree.getPeriodExamsSecondSemester().getEnd();
            } else {
                throw new DomainException("unsupported.execution.period.semester");
            }
        }

        private void add(final Attends attends) {
            executionCourses.add(attends.getDisciplinaExecucao());

            for (final WeeklyWorkLoad weeklyWorkLoad : attends.getWeeklyWorkLoads()) {
                final int weekIndex = weeklyWorkLoad.getWeekOffset();
                numberResponses[weekIndex]++;

                final Integer contact = weeklyWorkLoad.getContact();
                contactSum[weekIndex] += contact != null ? contact.intValue() : 0;

                final Integer autounomousStudy = weeklyWorkLoad.getAutonomousStudy();
                autonomousStudySum[weekIndex] += autounomousStudy != null ? autounomousStudy.intValue() : 0;

                final Integer other = weeklyWorkLoad.getOther();
                otherSum[weekIndex] += other != null ? other.intValue() : 0;

                totalSum[weekIndex] = contactSum[weekIndex] + autonomousStudySum[weekIndex] + otherSum[weekIndex];
            }
        }

        public Interval[] getIntervals() {
            return intervals;
        }

        public Interval getInterval() {
            return interval;
        }

        public int[] getContactSum() {
            return contactSum;
        }

        public int[] getAutonomousStudySum() {
            return autonomousStudySum;
        }

        public int[] getOtherSum() {
            return otherSum;
        }

        public int[] getNumberResponses() {
            return numberResponses;
        }

        public double[] getContactAverage() {
            return average(getContactSum(), getNumberResponsesTotal());
        }

        public double[] getAutonomousStudyAverage() {
            return average(getAutonomousStudySum(), getNumberResponsesTotal());
        }

        public double[] getOtherAverage() {
            return average(getOtherSum(), getNumberResponsesTotal());
        }

        private double[] average(final int[] values, final int divisor) {
            final double[] valuesAverage = new double[numberOfWeeks];
            for (int i = 0; i < numberOfWeeks; i++) {
                valuesAverage[i] = (0.0 + values[i]) / divisor;
            }
            return valuesAverage;
        }

        private int add(final int[] values) {
            int total = 0;
            for (int i = 0; i < values.length; i++) {
                total += values[i];
            }
            return total;
        }

        public int getAutonomousStudyTotal() {
            return add(autonomousStudySum);
        }

        public int getContactTotal() {
            return add(contactSum);
        }

        public int getNumberResponsesTotal() {
            return add(numberResponses);
        }

        public int getOtherSumTotal() {
            return add(otherSum);
        }

        public double getAutonomousStudyTotalAverage() {
            return (0.0 + Math.round((0.0 + add(autonomousStudySum)) / numberOfWeeks)) / 100;
        }

        public double getContactTotalAverage() {
            return (0.0 + Math.round((0.0 + add(contactSum)) / numberOfWeeks)) / 100;
        }

        public double getNumberResponsesTotalAverage() {
            return (0.0 + Math.round((0.0 + add(numberResponses)) / numberOfWeeks)) / 100;
        }

        public double getOtherSumTotalAverage() {
            return (0.0 + Math.round((0.0 + add(otherSum)) / numberOfWeeks)) / 100;
        }

        public int[] getTotalSum() {
            return totalSum;
        }

        public int getTotalSumTotal() {
            return add(totalSum);
        }

        public double getTotalAverage() {
            System.out.println("getTotalSumTotal(): " + getTotalSumTotal());
            System.out.println("numberOfWeeks: " + numberOfWeeks);
            return (0.0 + Math.round((0.0 + getTotalSumTotal()) * 100 / numberOfWeeks)) / 100;
        }
    }

    public ActionForward prepare(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws FenixFilterException,
            FenixServiceException {

        final Object[] args = { ExecutionPeriod.class };
        final List<ExecutionPeriod> executionPeriods = (List<ExecutionPeriod>) executeService(request, "ReadAllDomainObjects", args);
        final Set<ExecutionPeriod> sortedExecutionPeriods = new TreeSet<ExecutionPeriod>(executionPeriods);
        request.setAttribute("executionPeriods", sortedExecutionPeriods);

        final DynaActionForm dynaActionForm = (DynaActionForm) form;

        final Integer executionPeriodID = getExecutionPeriodID(dynaActionForm);
        final ExecutionPeriod selectedExecutionPeriod = findExecutionPeriod(executionPeriods, executionPeriodID);
        dynaActionForm.set("executionPeriodID", selectedExecutionPeriod.getIdInternal().toString());

        final DegreeCurricularPlan degreeCurricularPlan = (DegreeCurricularPlan)
            setDomainObjectInRequest(dynaActionForm, request, DegreeCurricularPlan.class, "degreeCurricularPlanID", "executionCourse");
        request.setAttribute("degreeCurricularPlanID", degreeCurricularPlan.getIdInternal());

        final Set<CurricularYear> curricularYears = new TreeSet<CurricularYear>();
        request.setAttribute("curricularYears", curricularYears);

        final Set<ExecutionCourse> executionCourses = new TreeSet<ExecutionCourse>(ExecutionCourse.EXECUTION_COURSE_NAME_COMPARATOR);
        request.setAttribute("executionCourses", executionCourses);

        final ExecutionCourse selectedExecutionCourse = (ExecutionCourse)
            setDomainObjectInRequest(dynaActionForm, request, ExecutionCourse.class, "executionCourseID", "executionCourse");
        request.setAttribute("selectedExecutionCourse", selectedExecutionCourse);

        final Integer curricularYearID = getCurricularYearID(dynaActionForm);
        final CurricularYear selecctedCurricularYear = (CurricularYear)
                setDomainObjectInRequest(dynaActionForm, request, CurricularYear.class, "curricularYearID", "selecctedCurricularYear");

        if (degreeCurricularPlan != null) {
        	for (final CurricularCourse curricularCourse : degreeCurricularPlan.getCurricularCourses()) {
        		for (final CurricularCourseScope curricularCourseScope : curricularCourse.getScopes()) {
        			final CurricularSemester curricularSemester = curricularCourseScope.getCurricularSemester();
        			final CurricularYear curricularYear = curricularSemester.getCurricularYear();
        			curricularYears.add(curricularYear);

        			if (curricularYearID == null || curricularYear.getIdInternal().equals(curricularYearID)) {
        				for (final ExecutionCourse executionCourse : curricularCourse.getExecutionCoursesByExecutionPeriod(selectedExecutionPeriod)) {
        					executionCourses.add(executionCourse);
        				}
        			}
        		}
        	}
        }

        if (selecctedCurricularYear != null) {
            request.setAttribute("curricularYearWeeklyWorkLoadView", new CurricularYearWeeklyWorkLoadView(degreeCurricularPlan, selectedExecutionPeriod, executionCourses));
        }

        return mapping.findForward("showWeeklyWorkLoad");
    }

    private DomainObject setDomainObjectInRequest(final DynaActionForm dynaActionForm, final HttpServletRequest request,
            final Class clazz, final String formAttributeName, final String requestAttributeName)
            throws FenixFilterException, FenixServiceException {
        final String domainObjectIDString = (String) dynaActionForm.get(formAttributeName);
        final Integer domainObjectID = domainObjectIDString == null || domainObjectIDString.length() == 0 ?
                null : Integer.valueOf(domainObjectIDString);
        final Object[] args = { clazz, domainObjectID };
        final DomainObject domainObject = (DomainObject) executeService(request, "ReadDomainObject", args);
        request.setAttribute(requestAttributeName, domainObject);
        return domainObject;
    }

    private Integer getCurricularYearID(final DynaActionForm dynaActionForm) {
        final String curricularYearIDString = dynaActionForm.getString("curricularYearID");
        return curricularYearIDString == null || curricularYearIDString.length() == 0 ? null : Integer.valueOf(curricularYearIDString);
	}

	private Integer getExecutionPeriodID(final DynaActionForm dynaActionForm) {
        final String exeutionPeriodIDString = dynaActionForm.getString("executionPeriodID");
        return exeutionPeriodIDString == null || exeutionPeriodIDString.length() == 0 ? null : Integer.valueOf(exeutionPeriodIDString);
    }

    private ExecutionPeriod findExecutionPeriod(final List<ExecutionPeriod> executionPeriods, final Integer executionPeriodID) {
        for (final ExecutionPeriod executionPeriod : executionPeriods) {
            if (executionPeriodID == null && executionPeriod.getState().equals(PeriodState.CURRENT)) {
                return executionPeriod;
            }
            if (executionPeriodID != null && executionPeriod.getIdInternal().equals(executionPeriodID)) {
                return executionPeriod;
            }
        }
        return null;
    }

}
