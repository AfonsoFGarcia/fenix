/*
 * Created on 23/Jul/2003
 *
 * 
 */
package net.sourceforge.fenixedu.domain;

import java.util.Calendar;

/**
 * @author Jo�o Mota
 * 
 * 23/Jul/2003 fenix-head Dominio
 *  
 */
public interface IDegreeObjectives extends IDomainObject {
    /**
     * @return
     */
    public abstract IDegree getDegree();

    /**
     * @param degree
     */
    public abstract void setDegree(IDegree degree);

    /**
     * @return
     */
    public abstract Integer getKeyDegree();

    /**
     * @param degreeKey
     */
    public abstract void setKeyDegree(Integer keyDegree);

    /**
     * @return
     */
    public abstract String getGeneralObjectives();

    /**
     * @param generalObjectives
     */
    public abstract void setGeneralObjectives(String generalObjectives);

    /**
     * @return
     */
    public abstract String getOperacionalObjectives();

    /**
     * @param operacionalObjectives
     */
    public abstract void setOperacionalObjectives(String operacionalObjectives);

    public Calendar getEndDate();

    /**
     * @param endDate
     */
    public void setEndDate(Calendar endDate);

    /**
     * @return
     */
    public Calendar getStartingDate();

    /**
     * @param startingDate
     */
    public void setStartingDate(Calendar startingDate);
}