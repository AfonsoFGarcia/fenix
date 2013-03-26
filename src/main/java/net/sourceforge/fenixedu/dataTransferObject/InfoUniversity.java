package net.sourceforge.fenixedu.dataTransferObject;

import net.sourceforge.fenixedu.domain.University;

/**
 * @author dcs-rjao
 * 
 *         24/Mar/2003
 */

public class InfoUniversity extends InfoObject {

    private final University university;

    public InfoUniversity(final University university) {
        this.university = university;
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof InfoUniversity && university == ((InfoUniversity) obj).university;
    }

    @Override
    public String toString() {
        return getUniversity().toString();
    }

    public String getCode() {
        return getUniversity().getCode();
    }

    public String getName() {
        return getUniversity().getName();
    }

    @Override
    public Integer getIdInternal() {
        return getUniversity().getIdInternal();
    }

    @Override
    public void setIdInternal(Integer integer) {
        throw new Error("Method should not be called!");
    }

    private University getUniversity() {
        return university;
    }

}