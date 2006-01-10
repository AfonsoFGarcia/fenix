package net.sourceforge.fenixedu.persistenceTier.versionedObjects.dao;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import net.sourceforge.fenixedu.commons.CollectionUtils;
import net.sourceforge.fenixedu.domain.CurricularCourse;
import net.sourceforge.fenixedu.domain.CurricularCourseScope;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentCurricularCourseScope;
import net.sourceforge.fenixedu.persistenceTier.versionedObjects.VersionedObjectsBase;

import org.apache.commons.collections.Predicate;

public class CurricularCourseScopeVO extends VersionedObjectsBase implements
		IPersistentCurricularCourseScope {

	public CurricularCourseScope readCurricularCourseScopeByCurricularCourseAndCurricularSemesterAndBranch(
			Integer curricularCourseId, final Integer curricularSemesterId,
			final Integer branchId) throws ExcepcaoPersistencia {

		CurricularCourse curricularCourse = (CurricularCourse) readByOID(CurricularCourse.class,curricularCourseId);
		if (curricularCourse != null) {
			List<CurricularCourseScope> scopes = curricularCourse.getScopes();
			return (CurricularCourseScope)CollectionUtils.find(scopes,new Predicate(){
				public boolean evaluate(Object o) {
					CurricularCourseScope ccs = (CurricularCourseScope) o;
					return ((ccs.getCurricularSemester().getIdInternal().equals(curricularSemesterId)) &&
							(ccs.getBranch().getIdInternal().equals(branchId)));
				}
			});
		}
		return null;
	}
	
	public CurricularCourseScope readCurricularCourseScopeByCurricularCourseAndCurricularSemesterAndBranchAndEndDate(
			Integer curricularCourseId, final Integer curricularSemesterId,
			final Integer branchId, final Calendar endDate) throws ExcepcaoPersistencia {
		CurricularCourse curricularCourse = (CurricularCourse) readByOID(CurricularCourse.class,curricularCourseId);
		if (curricularCourse != null) {
			List<CurricularCourseScope> scopes = curricularCourse.getScopes();
			return (CurricularCourseScope)CollectionUtils.find(scopes,new Predicate(){
				public boolean evaluate(Object o) {
					CurricularCourseScope ccs = (CurricularCourseScope) o;
					boolean compliesToDateRestriction =	(endDate == null && ccs.getEndDate() == null) || 
												(endDate != null && endDate.equals(ccs.getEndDate()));
					
					return ((ccs.getCurricularSemester().getIdInternal().equals(curricularSemesterId)) &&
							(ccs.getBranch().getIdInternal().equals(branchId)) && 
							compliesToDateRestriction);
				}
			});
		}
		return null;
	}

	public List readActiveCurricularCourseScopesByCurricularCourse(
			Integer curricularCourseId) throws ExcepcaoPersistencia {
		CurricularCourse curricularCourse = (CurricularCourse) readByOID(CurricularCourse.class,curricularCourseId);
		if (curricularCourse != null) {
			List<CurricularCourseScope> scopes = curricularCourse.getScopes();
			return (List)CollectionUtils.select(scopes,new Predicate(){
				public boolean evaluate(Object o) {
					CurricularCourseScope ccs = (CurricularCourseScope) o;
					return (ccs.getEndDate() == null);
				}
			});
		}
		return new ArrayList(0);
	}

	public List readCurricularCourseScopesByCurricularCourseInExecutionPeriod(
			Integer curricularCourseId, final Date beginDate, final Date endDate)
			throws ExcepcaoPersistencia {
		CurricularCourse curricularCourse = (CurricularCourse) readByOID(CurricularCourse.class,curricularCourseId);
		if (curricularCourse != null) {
			List<CurricularCourseScope> scopes = curricularCourse.getScopes();
			List result = (List)CollectionUtils.select(scopes,new Predicate(){
				public boolean evaluate(Object o) {
					CurricularCourseScope ccs = (CurricularCourseScope) o;
					
					return isCurricularCourseScopeInTimeSpan(ccs,beginDate,endDate);
				}
			});
			return result;
		}
		return new ArrayList(0);
	}

	public List readCurricularCourseScopesByDegreeCurricularPlanInExecutionYear(
			final Integer degreeCurricularPlanId, final Date beginDate, final Date endDate)
			throws ExcepcaoPersistencia {

		List<CurricularCourseScope> scopes = (List<CurricularCourseScope>) readAll(CurricularCourseScope.class);
		return (List)CollectionUtils.select(scopes,new Predicate(){
				public boolean evaluate(Object o) {
					CurricularCourseScope ccs = (CurricularCourseScope) o;
					
					return ccs.getCurricularCourse().getDegreeCurricularPlan().getIdInternal().
								equals(degreeCurricularPlanId) &&
							isCurricularCourseScopeInTimeSpan(ccs,beginDate,endDate);
				}
			});
	}

	public List readCurricularCourseScopesByCurricularCourseInExecutionYear(
			Integer curricularCourseId, final Date beginDate, final Date endDate)
			throws ExcepcaoPersistencia {
		CurricularCourse curricularCourse = (CurricularCourse) readByOID(CurricularCourse.class,curricularCourseId);
		if (curricularCourse != null) {
			List<CurricularCourseScope> scopes = curricularCourse.getScopes();
			return (List)CollectionUtils.select(scopes,new Predicate(){
				public boolean evaluate(Object o) {
					CurricularCourseScope ccs = (CurricularCourseScope) o;
					
					return isCurricularCourseScopeInTimeSpan(ccs,beginDate,endDate);
				}
			});
		}
		return new ArrayList(0);
	}

	public List readActiveCurricularCourseScopesByDegreeCurricularPlanAndCurricularYear(
			final Integer degreeCurricularPlanId, final Integer curricularYear,
			final Date beginDate, final Date endDate) throws ExcepcaoPersistencia {

		List<CurricularCourseScope> scopes = (List<CurricularCourseScope>) readAll(CurricularCourseScope.class);
		return (List)CollectionUtils.select(scopes,new Predicate(){
				public boolean evaluate(Object o) {
					CurricularCourseScope ccs = (CurricularCourseScope) o;
					
					return	ccs.getCurricularCourse().getDegreeCurricularPlan().getIdInternal().
								equals(degreeCurricularPlanId) &&
							ccs.getCurricularSemester().getCurricularYear().getYear().
								equals(curricularYear) &&
							isCurricularCourseScopeInTimeSpan(ccs,beginDate,endDate);
				}
			});

	}

	public List readActiveCurricularCourseScopesByDegreeCurricularPlanId(
			final Integer degreeCurricularPlanId) throws ExcepcaoPersistencia {
		List<CurricularCourseScope> scopes = (List<CurricularCourseScope>) readAll(CurricularCourseScope.class);
		return (List)CollectionUtils.select(scopes,new Predicate(){
				public boolean evaluate(Object o) {
					CurricularCourseScope ccs = (CurricularCourseScope) o;
					
					return ccs.getCurricularCourse().getDegreeCurricularPlan().getIdInternal().
								equals(degreeCurricularPlanId) &&
							(ccs.getEndDate() == null);
				}
			});
	}
	
	private boolean isCurricularCourseScopeInTimeSpan(CurricularCourseScope ccs, Date beginDate, Date endDate) {
		return ccs.getBegin().before(endDate) && (ccs.getEndDate() == null || (ccs.getEnd().after(beginDate)));
	}
}
