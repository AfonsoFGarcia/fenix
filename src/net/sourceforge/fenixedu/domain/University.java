package net.sourceforge.fenixedu.domain;

/**
 * @author dcs-rjao
 * 
 * 24/Mar/2003
 */

public class University extends University_Base {

    public String toString() {
        String result = "[" + this.getClass().getName() + "; ";
        result += "code = " + this.getCode() + "; ";
        result += "name = " + this.getName() + "; ";
        return result;
    }

    public boolean equals(Object obj) {
        if (obj instanceof IUniversity) {
            final IUniversity university = (IUniversity) obj;
            return this.getIdInternal().equals(university.getIdInternal());
        }
        return false;
    }

}
