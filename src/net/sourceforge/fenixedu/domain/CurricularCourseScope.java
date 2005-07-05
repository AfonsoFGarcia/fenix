package net.sourceforge.fenixedu.domain;

import java.util.Calendar;

/**
 * @author dcs-rjao
 * 
 * 20/Mar/2003
 */

public class CurricularCourseScope extends CurricularCourseScope_Base {

    /**
     * @return Returns the beginDate.
     */
    public Calendar getBeginDate() {
        if (this.getBegin() != null) {
            Calendar result = Calendar.getInstance();
            result.setTime(this.getBegin());
            return result;
        }
        return null;
    }

    /**
     * @param beginDate
     *            The beginDate to set.
     */
    public void setBeginDate(Calendar beginDate) {
        if (beginDate != null) {
            this.setBegin(beginDate.getTime());
        } else {
            this.setBegin(null);
        }
    }

    /**
     * @return Returns the endDate.
     */
    public Calendar getEndDate() {
        if (this.getEnd() != null) {
            Calendar result = Calendar.getInstance();
            result.setTime(this.getEnd());
            return result;
        }
        return null;
    }

    /**
     * @param endDate
     *            The endDate to set.
     */
    public void setEndDate(Calendar endDate) {
        if (endDate != null) {
            this.setEnd(endDate.getTime());
        } else {
            this.setEnd(null);
        }
    }

    public String toString() {
        String result = "[" + this.getClass().getName() + ": ";
        result += "idInternal = " + this.getIdInternal() + "; ";
        result += "curricularCourse = " + this.getCurricularCourse() + "; ";
        result += "curricularSemester = " + this.getCurricularSemester() + "; ";
        result += "branch = " + this.getBranch() + "; ";
        result += "endDate = " + this.getEndDate() + "]\n";

        return result;
    }

    public boolean equals(Object obj) {
        if (obj instanceof ICurricularCourseScope) {
            final ICurricularCourseScope curricularCourseScope = (ICurricularCourseScope) obj;
            return getIdInternal().equals(curricularCourseScope.getIdInternal());
        }
        return false;
    }

    public Boolean isActive() {
        Boolean result = Boolean.FALSE;
        if (this.getEndDate() == null) {
            result = Boolean.TRUE;
        }
        return result;
    }

}
