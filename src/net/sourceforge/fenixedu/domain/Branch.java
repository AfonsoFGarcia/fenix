package net.sourceforge.fenixedu.domain;

import java.util.List;

import net.sourceforge.fenixedu.util.AreaType;
import net.sourceforge.fenixedu.util.BranchType;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;

/**
 * @author dcs-rjao
 * 
 * 19/Mar/2003
 */

public class Branch extends Branch_Base {
    private List scopes;

    private IDegreeCurricularPlan degreeCurricularPlan;

    /**
     * @author Nuno Correia & Ricardo Rodrigues
     */
    private BranchType branchType;

    private List areaCurricularCourseGroups;

    private List optionalCurricularCourseGroups;

    public Branch() {
    }

    public boolean equals(Object obj) {
        boolean result = false;
        if (obj instanceof IBranch) {
            IBranch branch = (IBranch) obj;
            result = this.getCode().equals(branch.getCode())
                    && this.getDegreeCurricularPlan().equals(branch.getDegreeCurricularPlan());
        }
        return result;
    }

    public String toString() {
        String result = "[" + this.getClass().getName() + ": ";
        result += "idInternal = " + getIdInternal() + "; ";
        result += "name = " + this.getName() + "; ";
        result += "code = " + this.getCode() + "; ";
        result += "acronym = " + this.getAcronym() + "]\n";
        return result;
    }

    /**
     * @author Fernanda Quit�rio
     */
    public Boolean representsCommonBranch() {

        if (this.getBranchType().equals(BranchType.COMMON_BRANCH)) {
            return Boolean.TRUE;
        }
        return Boolean.FALSE;
    }

    /**
     * @return List
     */
    public List getScopes() {
        return scopes;
    }

    /**
     * @param scopes
     */
    public void setScopes(List scopes) {
        this.scopes = scopes;
    }

    /**
     * @return
     */
    public IDegreeCurricularPlan getDegreeCurricularPlan() {
        return degreeCurricularPlan;
    }

    /**
     * @param plan
     */
    public void setDegreeCurricularPlan(IDegreeCurricularPlan plan) {
        degreeCurricularPlan = plan;
    }

    /**
     * @return Returns the branchType.
     */
    public BranchType getBranchType() {
        return branchType;
    }

    /**
     * @param branchType
     *            The branchType to set.
     */
    public void setBranchType(BranchType branchType) {
        this.branchType = branchType;
    }

    /**
     * @return Returns the curricularCourseGroups.
     */
    public List getAreaCurricularCourseGroups() {
        return areaCurricularCourseGroups;
    }

    /**
     * @param curricularCourseGroups
     *            The curricularCourseGroups to set.
     */
    public void setAreaCurricularCourseGroups(List curricularCourseGroups) {
        this.areaCurricularCourseGroups = curricularCourseGroups;
    }

    /*
     * (non-Javadoc)
     * 
     * @see Dominio.IBranch#getCurricularCourseGroups(Util.AreaType)
     */
    public List getAreaCurricularCourseGroups(final AreaType areaType) {

        return (List) CollectionUtils.select(getAreaCurricularCourseGroups(), new Predicate() {

            public boolean evaluate(Object arg0) {
                ICurricularCourseGroup curricularCourseGroup = (ICurricularCourseGroup) arg0;
                return curricularCourseGroup.getAreaType().equals(areaType);
            }
        });
    }

    /**
     * @return Returns the optionCurricularCourseGroups.
     */
    public List getOptionalCurricularCourseGroups() {
        return optionalCurricularCourseGroups;
    }

    /**
     * @param optionCurricularCourseGroups
     *            The optionCurricularCourseGroups to set.
     */
    public void setOptionalCurricularCourseGroups(List optionCurricularCourseGroups) {
        this.optionalCurricularCourseGroups = optionCurricularCourseGroups;
    }
}