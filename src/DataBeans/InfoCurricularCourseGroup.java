/*
 * Created on 26/Nov/2003
 *
 * To change the template for this generated file go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
package DataBeans;

import java.io.Serializable;
import java.util.List;

import Util.AreaType;

/**
 * @author Nuno Correia
 * @author Ricardo Rodrigues
 */

public class InfoCurricularCourseGroup extends InfoObject implements Serializable
{
    private Integer minimumCredits;
    private Integer maximumCredits;
    private AreaType areaType;
    private InfoBranch infoBranch;
    private List curricularCourseScopes;

    public InfoCurricularCourseGroup()
    {
    }

    public boolean equals(Object obj)
    {
        boolean resultado = false;
        if (obj instanceof InfoCurricularCourseGroup)
        {
            InfoCurricularCourseGroup infoCurricularCourseGroup = (InfoCurricularCourseGroup) obj;
            resultado =
                (((getInfoBranch() == null && infoCurricularCourseGroup.getInfoBranch() == null)
                    || (getInfoBranch() != null
                        && infoCurricularCourseGroup.getInfoBranch() != null
                        && getInfoBranch().equals(infoCurricularCourseGroup.getInfoBranch())))
                    && ((getAreaType() == null && infoCurricularCourseGroup.getAreaType() == null)
                        || (getAreaType() != null
                            && infoCurricularCourseGroup.getAreaType() != null
                            && getAreaType().equals(infoCurricularCourseGroup.getAreaType())))
                    && ((getCurricularCourseScopes() == null
                        && infoCurricularCourseGroup.getCurricularCourseScopes() == null)
                        || (getCurricularCourseScopes() != null
                            && infoCurricularCourseGroup.getCurricularCourseScopes() != null
                            && getCurricularCourseScopes().equals(
                                infoCurricularCourseGroup.getCurricularCourseScopes()))));
        }

        return resultado;
    }
    
    public String toString()
    {
    	String result = "[" + this.getClass().getName() + "; ";
    	result += "Branch = " + this.infoBranch + "; ";
    	result += "MimimumCredits = " + this.minimumCredits + "; ";
    	result += "MaximumCredits = " + this.maximumCredits + "; ";
    	result += "AreaType = " + this.areaType + "; ";
    	return result;
    }


    /**
     * @return
     */
    public InfoBranch getInfoBranch()
    {
        return infoBranch;
    }

    /**
     * @param branch
     */
    public void setInfoBranch(InfoBranch infoBranch)
    {
        this.infoBranch = infoBranch;
    }

    /**
     * @return
     */
    public List getCurricularCourseScopes()
    {
        return curricularCourseScopes;
    }

    /**
     * @param curricularCourseScopes
     */
    public void setCurricularCourseScopes(List curricularCourseScopes)
    {
        this.curricularCourseScopes = curricularCourseScopes;
    }

    /**
     * @return
     */
    public AreaType getAreaType()
    {
        return areaType;
    }

    /**
     * @param areaType
     */
    public void setAreaType(AreaType areaType)
    {
        this.areaType = areaType;
    }

    /**
     * @return
     */
    public Integer getMaximumCredits()
    {
        return maximumCredits;
    }

    /**
     * @param maximumCredits
     */
    public void setMaximumCredits(Integer maximumCredits)
    {
        this.maximumCredits = maximumCredits;
    }

    /**
     * @return
     */
    public Integer getMinimumCredits()
    {
        return minimumCredits;
    }

    /**
     * @param minimumCredits
     */
    public void setMinimumCredits(Integer minimumCredits)
    {
        this.minimumCredits = minimumCredits;
    }

}
