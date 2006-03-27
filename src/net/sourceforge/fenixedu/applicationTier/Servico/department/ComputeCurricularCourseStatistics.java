/**
 * 
 */
package net.sourceforge.fenixedu.applicationTier.Servico.department;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Formatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.domain.CurricularCourse;
import net.sourceforge.fenixedu.domain.CurricularCourseScope;
import net.sourceforge.fenixedu.domain.DegreeCurricularPlan;
import net.sourceforge.fenixedu.domain.Enrolment;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.ExecutionPeriod;
import net.sourceforge.fenixedu.domain.ExecutionYear;

/**
 * @author - Shezad Anavarali (shezad@ist.utl.pt)
 * 
 */
public class ComputeCurricularCourseStatistics extends Service {

    public String run(Integer degreeCurricularPlanID, Integer executionYearID) {

        ExecutionYear executionYear = rootDomainObject.readExecutionYearByOID(executionYearID);
        DegreeCurricularPlan degreeCurricularPlan = rootDomainObject
                .readDegreeCurricularPlanByOID(degreeCurricularPlanID);
        List<CurricularCourse> curricularCourses = degreeCurricularPlan.getCurricularCourses();

        Formatter result = new Formatter();

        for (CurricularCourse curricularCourse : curricularCourses) {

            List<ExecutionPeriod> executionPeriods = executionYear.getExecutionPeriods();
            for (ExecutionPeriod executionPeriod : executionPeriods) {

                // Get Scopes
                List<CurricularCourseScope> scopes = curricularCourse
                        .getActiveScopesInExecutionPeriod(executionPeriod);
                if (scopes.isEmpty()) {
                    continue;
                }

                // Get max Year and its Semester
                int year = 0;
                int semester = 0;
                for (CurricularCourseScope scope : scopes) {
                    Integer scopeYear = scope.getCurricularSemester().getCurricularYear().getYear();
                    if (scopeYear > year) {
                        year = scopeYear;
                        semester = scope.getCurricularSemester().getSemester();
                    }
                }

                // Get Execution Course
                List<ExecutionCourse> executionCourses = curricularCourse
                        .getExecutionCoursesByExecutionPeriod(executionPeriod);
                if (executionCourses.isEmpty() || executionCourses.size() > 1) {
                    // Houston, we have a problem...!!
                    continue;
                }
                ExecutionCourse executionCourse = executionCourses.get(0);

                // Organize enrolments by DegreeCurricularPlans
                Map<DegreeCurricularPlan, Collection<Enrolment>> enrolmentsMap = organizeEnrolmentsByDCP(
                        curricularCourse, executionPeriod);

                // Calculate enrolments for each DegreeCurricularPlan
                for (DegreeCurricularPlan enrolmentDCP : enrolmentsMap.keySet()) {

                    int firstEnrolledCount = 0;
                    int secondEnrolledCount = 0;

                    Collection<Enrolment> dcpEnrolments = enrolmentsMap.get(enrolmentDCP);
                    for (Enrolment enrolment : dcpEnrolments) {
                        switch (enrolment.getNumberOfTotalEnrolmentsInThisCourse()) {

                        case 1:
                            firstEnrolledCount++;
                            break;
                        case 2:
                            secondEnrolledCount++;
                            break;
                        default:
                            break;
                        }
                    }

                    // Add to result
                    result.format("%s\t%s\t%d\t%s\t%d\t%s\t%d\t%d\t%d\t%d\t%d\n", curricularCourse
                            .getCode(), curricularCourse.getName(), executionCourse.getIdInternal(),
                            executionCourse.getSigla(), enrolmentDCP.getIdInternal(), enrolmentDCP
                                    .getName(), semester, year, firstEnrolledCount, secondEnrolledCount,
                            dcpEnrolments.size());
                }
            }
        }

        return result.toString();
    }

    private Map<DegreeCurricularPlan, Collection<Enrolment>> organizeEnrolmentsByDCP(
            CurricularCourse curricularCourse, ExecutionPeriod executionPeriod) {
        Map<DegreeCurricularPlan, Collection<Enrolment>> enrolmentsMap = new HashMap<DegreeCurricularPlan, Collection<Enrolment>>();

        List<Enrolment> enrolments = curricularCourse.getEnrolmentsByExecutionPeriod(executionPeriod);
        for (Enrolment enrolment : enrolments) {

            DegreeCurricularPlan studentDCP = enrolment.getStudentCurricularPlan()
                    .getDegreeCurricularPlan();
            Collection<Enrolment> dcpEnrolments = enrolmentsMap.get(studentDCP);
            if (dcpEnrolments == null) {
                dcpEnrolments = new ArrayList<Enrolment>();
                enrolmentsMap.put(studentDCP, dcpEnrolments);
            }
            dcpEnrolments.add(enrolment);

        }
        return enrolmentsMap;
    }

}
