package net.sourceforge.fenixedu.domain;

import java.util.Iterator;
import java.util.List;

import net.sourceforge.fenixedu.domain.branch.BranchType;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.tools.enrollment.AreaType;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;

/**
 * @author dcs-rjao
 * 
 * 19/Mar/2003
 */

public class Branch extends Branch_Base {

	public Branch() {
		super();
	}
	
	public Branch (String name, String nameEn, String code, DegreeCurricularPlan degreeCurricularPlan) {
		super();
		setName(name);
		setNameEn(nameEn);
		setCode(code);
		setDegreeCurricularPlan(degreeCurricularPlan);
	}
	
    public String toString() {
        String result = "[" + this.getClass().getName() + ": ";
        result += "idInternal = " + getIdInternal() + "; ";
        result += "name = " + this.getName() + "; ";
        result += "code = " + this.getCode() + "; ";
        result += "acronym = " + this.getAcronym() + "]\n";
        return result;
    }

    public Boolean representsCommonBranch() {
        if (this.getBranchType().equals(BranchType.COMNBR)) {
            return Boolean.TRUE;
        }
        return Boolean.FALSE;
    }
	
	public void edit(String name, String nameEn, String code) {
		setName(name);
		setNameEn(nameEn);
		setCode(code);
	}

    public List getAreaCurricularCourseGroups(final AreaType areaType) {

        return (List) CollectionUtils.select(getCurricularCourseGroups(), new Predicate() {

            public boolean evaluate(Object arg0) {
                CurricularCourseGroup curricularCourseGroup = (CurricularCourseGroup) arg0;
                return curricularCourseGroup.getAreaType().equals(areaType);
            }
        });
    }
	
	private Boolean canDeleteAllEligibleCurricularCourseScopes (final Branch commonBranch) {
		Iterator branchCurricularCourseScopesIterator = getScopesIterator();
		while (branchCurricularCourseScopesIterator.hasNext()) {
			CurricularCourseScope scope = (CurricularCourseScope)branchCurricularCourseScopesIterator.next();
			CurricularCourse curricularCourse = scope.getCurricularCourse();

			// if CurricularCourse already has a common Branch
			if (hasCurricularCourseCommonBranchInAnyCurricularCourseScope(curricularCourse,commonBranch)) {
				// we want to delete this CurricularCourseScope
				
				if (!scope.canBeDeleted())
					return false;
			}
		}
		return true;
	}	
	
	public Boolean canBeDeleted() {
		if (this.hasAnyAssociatedFinalDegreeWorkProposals())
			return false;
		
		if (this.representsCommonBranch() && 
				(this.hasAnyCurricularCourseGroups() || this.hasAnyScopes()))
			return false;
		
		Branch commonBranch = findCommonBranchForSameDegreeCurricularPlan();
		if (commonBranch == null)
			return false;
		
		if (!canDeleteAllEligibleCurricularCourseScopes(commonBranch))
			return false;
		
		return true;
	}
	
	public void delete() throws DomainException {
		
		if (!this.canBeDeleted())
			throw new DomainException("error.branch.cant.delete");
		
		Branch commonBranch = findCommonBranchForSameDegreeCurricularPlan();

		this.getStudentCurricularPlans().clear();
		this.getSecundaryStudentCurricularPlansLEIC().clear();
		this.getSecundaryStudentCurricularPlansLEEC().clear();

		Iterator curricularCourseGroupsIterator = getCurricularCourseGroupsIterator();
		while (curricularCourseGroupsIterator.hasNext()) {
			CurricularCourseGroup curricularCourseGroup = (CurricularCourseGroup) curricularCourseGroupsIterator.next();
			curricularCourseGroupsIterator.remove();
			curricularCourseGroup.setBranch(commonBranch);
		}

		removeCurricularCourseScopes(commonBranch);
		
		removeDegreeCurricularPlan();
		
		deleteDomainObject();
	}
	
	private void removeCurricularCourseScopes(final Branch commonBranch) throws DomainException {
		Iterator branchCurricularCourseScopesIterator = getScopesIterator();
		while (branchCurricularCourseScopesIterator.hasNext()) {
			CurricularCourseScope scope = (CurricularCourseScope)branchCurricularCourseScopesIterator.next();
			CurricularCourse curricularCourse = scope.getCurricularCourse();
	
			// if CurricularCourse already has a common Branch
			if (hasCurricularCourseCommonBranchInAnyCurricularCourseScope(curricularCourse,commonBranch)) {
				// delete the CurricularCourseScope
				branchCurricularCourseScopesIterator.remove();
				scope.removeBranch();
				scope.delete();
				
			} else {
				// set the Branch in the CurricularCourseScope to commonBranch
				branchCurricularCourseScopesIterator.remove();
				scope.setBranch(commonBranch);
			}
		}		
	}

	private Branch findCommonBranchForSameDegreeCurricularPlan() {
		for (Branch branch : getDegreeCurricularPlan().getAreas()) {
			if (branch.representsCommonBranch() && branch.getName().equals("")) {
				return branch;
			}
		}
		return null;
	}
	
	private Boolean hasCurricularCourseCommonBranchInAnyCurricularCourseScope (CurricularCourse curricularCourse, final Branch commonBranch) {
		return ((CurricularCourseScope) CollectionUtils.find(curricularCourse.getScopes(),new Predicate() {
			public boolean evaluate(Object o) {
				CurricularCourseScope ccs = (CurricularCourseScope) o;
				return ccs.getBranch().equals(commonBranch);
			}}) != null);
	}
}
