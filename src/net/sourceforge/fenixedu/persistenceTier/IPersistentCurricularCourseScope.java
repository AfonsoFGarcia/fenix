package net.sourceforge.fenixedu.persistenceTier;

import java.util.Calendar;
import java.util.List;

import net.sourceforge.fenixedu.domain.IBranch;
import net.sourceforge.fenixedu.domain.ICurricularCourse;
import net.sourceforge.fenixedu.domain.ICurricularCourseScope;
import net.sourceforge.fenixedu.domain.ICurricularSemester;
import net.sourceforge.fenixedu.domain.IDegreeCurricularPlan;
import net.sourceforge.fenixedu.domain.IExecutionPeriod;
import net.sourceforge.fenixedu.domain.IExecutionYear;

/**
 * @author dcs-rjao 24/Mar/2003
 */

public interface IPersistentCurricularCourseScope extends IPersistentObject {

    public void delete(ICurricularCourseScope curricularCourseScope) throws ExcepcaoPersistencia;

    public ICurricularCourseScope readCurricularCourseScopeByCurricularCourseAndCurricularSemesterAndBranch(
            ICurricularCourse curricularCourse, ICurricularSemester curricularSemester, IBranch branch)
            throws ExcepcaoPersistencia;

    public ICurricularCourseScope readCurricularCourseScopeByCurricularCourseAndCurricularSemesterAndBranchAndEndDate(
            ICurricularCourse curricularCourse, ICurricularSemester curricularSemester, IBranch branch,
            Calendar endDate) throws ExcepcaoPersistencia;

    public ICurricularCourseScope readCurricularCourseScopeByCurricularCourseAndCurricularSemesterAndBranchAndBeginDate(
            ICurricularCourse curricularCourse, ICurricularSemester curricularSemester, IBranch branch,
            Calendar beginDate) throws ExcepcaoPersistencia;

    public List readCurricularCourseScopesByCurricularCourse(ICurricularCourse curricularCourse)
            throws ExcepcaoPersistencia;

    public List readActiveCurricularCourseScopesByCurricularCourse(ICurricularCourse curricularCourse)
            throws ExcepcaoPersistencia;

    public List readCurricularCourseScopesByCurricularCourseInExecutionPeriod(
            ICurricularCourse curricularCourse, IExecutionPeriod executionPeriod)
            throws ExcepcaoPersistencia;

    public List readCurricularCourseScopesByDegreeCurricularPlanInExecutionYear(
            IDegreeCurricularPlan degreeCurricularPlan, IExecutionYear executionYear)
            throws ExcepcaoPersistencia;

    public List readCurricularCourseScopesByCurricularCourseInExecutionYear(
            ICurricularCourse curricularCourse, IExecutionYear executionYear)
            throws ExcepcaoPersistencia;

    public List readAll() throws ExcepcaoPersistencia;

    /**
     * @param curricularCourse
     * @param semester
     * @param branch
     * @return @throws
     *         ExcepcaoPersistencia
     */
    public List readByCurricularCourseAndSemesterAndBranch(ICurricularCourse curricularCourse,
            Integer semester, IBranch branch) throws ExcepcaoPersistencia;

    /**
     * @param curricularCourse
     * @param year
     * @param semester
     * @return @throws
     *         ExcepcaoPersistencia
     */
    public List readByCurricularCourseAndYearAndSemester(ICurricularCourse curricularCourse,
            Integer year, Integer semester) throws ExcepcaoPersistencia;

    /**
     * @param curricularCourse
     * @param year
     * @return @throws
     *         ExcepcaoPersistencia
     */
    public List readByCurricularCourseAndYear(ICurricularCourse curricularCourse, Integer year)
            throws ExcepcaoPersistencia;

    /**
     * @param curricularCourse
     * @param integer
     * @return
     */
    public List readByCurricularCourseAndSemester(ICurricularCourse curricularCourse, Integer semester)
            throws ExcepcaoPersistencia;

    /**
     * @param curricularCourse
     */
    public List readByCurricularCourse(ICurricularCourse curricularCourse) throws ExcepcaoPersistencia;

    /**
     * @param branch
     * @return @throws
     *         ExcepcaoPersistencia
     */
    public List readByBranch(IBranch branch) throws ExcepcaoPersistencia;

    public List readActiveCurricularCourseScopesByDegreeCurricularPlanAndCurricularYear(
            IDegreeCurricularPlan degreeCurricularPlan, Integer year, IExecutionYear executionYear)
            throws ExcepcaoPersistencia;

    public List readCurricularCourseScopesByDegreeCurricularPlanAndExecutionYear(
            IDegreeCurricularPlan degreeCurricularPlan, IExecutionYear executionYear)
            throws ExcepcaoPersistencia;

    /**
     * @param degreeCurricularPlanId
     * @return
     */
    public List readActiveCurricularCourseScopesByDegreeCurricularPlanId(Integer degreeCurricularPlanId)
            throws ExcepcaoPersistencia;

}