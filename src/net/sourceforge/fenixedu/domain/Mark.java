package net.sourceforge.fenixedu.domain;

/**
 * @author dcs-rjao
 * 
 * 19/Mar/2003
 */

public class Mark extends Mark_Base {

    public String toString() {
        String result = "[" + this.getClass().getName() + ": ";
        result += "idInternal = " + getIdInternal() + "; ";
        result += "mark = " + getMark() + "; ";
        result += "published mark = " + getPublishedMark() + "; ";
        result += "evaluation= " + getEvaluation().getIdInternal() + "; ";
        result += "attend = " + getAttend().getIdInternal() + "; ]";

        return result;
    }

}
