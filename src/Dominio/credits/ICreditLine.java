/*
 * Created on 29/Fev/2004
 */
package Dominio.credits;

import Dominio.IDomainObject;
import Dominio.ITeacher;

/**
 * @author jpvl
 */
public interface ICreditLine extends IDomainObject {
    /**
     * @return Returns the teacher.
     */
    public abstract ITeacher getTeacher();

    /**
     * @param teacher
     *            The teacher to set.
     */
    public abstract void setTeacher(ITeacher teacher);
}