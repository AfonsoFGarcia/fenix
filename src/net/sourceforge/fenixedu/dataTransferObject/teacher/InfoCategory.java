/*
 * Created on 7/Nov/2003
 * 
 */
package net.sourceforge.fenixedu.dataTransferObject.teacher;

import net.sourceforge.fenixedu.dataTransferObject.InfoObject;
import net.sourceforge.fenixedu.domain.teacher.Category;

/**
 * @author Leonor Almeida
 * @author Sergio Montelobo
 * 
 */
public class InfoCategory extends InfoObject {
    private Boolean canBeExecutionCourseResponsible;

    private String code;

    private String longName;

    private String shortName;

    public InfoCategory() {
    }

    public boolean equals(Object obj) {
	boolean resultado = false;
	if (obj instanceof InfoCategory) {
	    resultado = getCode().equals(((InfoCategory) obj).getCode());
	}
	return resultado;
    }

    /**
     * @return Returns the canBeExecutionCourseResponsible.
     */
    public Boolean getCanBeExecutionCourseResponsible() {
	return this.canBeExecutionCourseResponsible;
    }

    /**
     * @return Returns the code.
     */
    public String getCode() {
	return code;
    }

    /**
     * @return Returns the longName.
     */
    public String getLongName() {
	return longName;
    }

    /**
     * @return Returns the shortName.
     */
    public String getShortName() {
	return shortName;
    }

    /**
     * @param canBeExecutionCourseResponsible
     *            The canBeExecutionCourseResponsible to set.
     */
    public void setCanBeExecutionCourseResponsible(Boolean canBeExecutionCourseResponsible) {
	this.canBeExecutionCourseResponsible = canBeExecutionCourseResponsible;
    }

    /**
     * @param code
     *            The code to set.
     */
    public void setCode(String code) {
	this.code = code;
    }

    /**
     * @param longName
     *            The longName to set.
     */
    public void setLongName(String longName) {
	this.longName = longName;
    }

    /**
     * @param shortName
     *            The shortName to set.
     */
    public void setShortName(String shortName) {
	this.shortName = shortName;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * net.sourceforge.fenixedu.dataTransferObject.InfoObject#copyFromDomain
     * (Dominio.DomainObject)
     */
    public void copyFromDomain(Category category) {
	super.copyFromDomain(category);
	if (category != null) {
	    setCanBeExecutionCourseResponsible(category.getCanBeExecutionCourseResponsible());
	    setCode(category.getCode());
	    setLongName(category.getLongName());
	    setShortName(category.getShortName());
	}
    }

    public static InfoCategory newInfoFromDomain(Category category) {
	InfoCategory infoCategory = null;
	if (category != null) {
	    infoCategory = new InfoCategory();
	    infoCategory.copyFromDomain(category);
	}
	return infoCategory;
    }
}