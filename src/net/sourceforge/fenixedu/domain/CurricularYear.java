package net.sourceforge.fenixedu.domain;

/**
 * @author dcs-rjao
 * 
 * 21/Mar/2003
 */

public class CurricularYear extends CurricularYear_Base {

    public String toString() {
        String result = "[" + this.getClass().getName() + ": ";
        result += "year = " + this.getYear() + "]\n";
        return result;
    }

}
