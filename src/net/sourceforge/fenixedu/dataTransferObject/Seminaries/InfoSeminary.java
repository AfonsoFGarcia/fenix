/**
 * @author Goncalo Luiz gedl [AT] rnl [DOT] ist [DOT] utl [DOT] pt
 * 
 * 
 * Created at 29/8/2005, 16:05:55
 *  
 */

package net.sourceforge.fenixedu.dataTransferObject.Seminaries;

import java.util.Calendar;

import net.sourceforge.fenixedu.dataTransferObject.InfoObject;
import net.sourceforge.fenixedu.domain.Seminaries.Seminary;

public class InfoSeminary extends InfoObject{
    private String name;

    private String description;

    private Integer allowedCandidaciesPerStudent;

    private Calendar enrollmentBeginDate;

    private Calendar enrollmentBeginTime;

    private Calendar enrollmentEndDate;

    private Calendar enrollmentEndTime;

    private Boolean hasThemes;

    private Boolean hasCaseStudy;

    /**
     * @return
     */
    public String getDescription() {
        return description;
    }



    /**
     * @return
     */
    public String getName() {
        return name;
    }

    /**
     * @param string
     */
    public void setDescription(String string) {
        description = string;
    }


    /**
     * @param string
     */
    public void setName(String string) {
        name = string;
    }

    /**
     * @return
     */
    public Integer getAllowedCandidaciesPerStudent() {
        return allowedCandidaciesPerStudent;
    }

    /**
     * @param integer
     */
    public void setAllowedCandidaciesPerStudent(Integer integer) {
        allowedCandidaciesPerStudent = integer;
    }

    /**
     * @return
     */
    public Calendar getEnrollmentBeginDate() {
        return enrollmentBeginDate;
    }

    /**
     * @return
     */
    public Calendar getEnrollmentBeginTime() {
        return enrollmentBeginTime;
    }

    /**
     * @return
     */
    public Calendar getEnrollmentEndDate() {
        return enrollmentEndDate;
    }

    /**
     * @return
     */
    public Calendar getEnrollmentEndTime() {
        return enrollmentEndTime;
    }

    /**
     * @param calendar
     */
    public void setEnrollmentBeginDate(Calendar calendar) {
        enrollmentBeginDate = calendar;
    }

    /**
     * @param calendar
     */
    public void setEnrollmentBeginTime(Calendar calendar) {
        enrollmentBeginTime = calendar;
    }

    /**
     * @param calendar
     */
    public void setEnrollmentEndDate(Calendar calendar) {
        enrollmentEndDate = calendar;
    }

    /**
     * @param calendar
     */
    public void setEnrollmentEndTime(Calendar calendar) {
        enrollmentEndTime = calendar;
    }

    public String printDeadline() {
        StringBuffer result = new StringBuffer();
        result.append(this.getEnrollmentEndDate().get(Calendar.DAY_OF_MONTH));
        result.append("-").append(this.getEnrollmentEndDate().get(Calendar.MONTH + 1));
        result.append("-").append(this.getEnrollmentEndDate().get(Calendar.YEAR));
        result.append(", ");
        int minute = this.getEnrollmentEndTime().get(Calendar.MINUTE);
        int hour = this.getEnrollmentEndTime().get(Calendar.HOUR_OF_DAY);
        if (hour < 10)
            result.append("0"); /*
                                 * lets stuff an with an extra 0 for readability
                                 * improvement
                                 */
        result.append(hour).append(":");
        if (minute < 10)
            result.append("0"); /*
                                 * lets stuff an with an extra 0 for readability
                                 * improvement
                                 */
        result.append(minute);

        return result.toString();
    }

    /**
     * @return
     */
    public Boolean getHasCaseStudy() {
        return hasCaseStudy;
    }

    /**
     * @return
     */
    public Boolean getHasThemes() {
        return hasThemes;
    }

    /**
     * @param boolean1
     */
    public void setHasCaseStudy(Boolean boolean1) {
        hasCaseStudy = boolean1;
    }

    /**
     * @param boolean1
     */
    public void setHasThemes(Boolean boolean1) {
        hasThemes = boolean1;
    }

    public void copyFromDomain(Seminary seminary) {
        super.copyFromDomain(seminary);
        if (seminary != null) {
            setAllowedCandidaciesPerStudent(seminary.getAllowedCandidaciesPerStudent());
            setDescription(seminary.getDescription());
            setEnrollmentBeginDate(seminary.getEnrollmentBeginDate());
            setEnrollmentBeginTime(seminary.getEnrollmentBeginTime());
            setEnrollmentEndDate(seminary.getEnrollmentEndDate());
            setEnrollmentEndTime(seminary.getEnrollmentEndTime());
            setHasCaseStudy(seminary.getHasCaseStudy());
            setHasThemes(seminary.getHasTheme());
            setName(seminary.getName());
        }
    }

    public static InfoSeminary newInfoFromDomain(Seminary seminary) {
        InfoSeminary infoSeminary = null;
        if (seminary != null) {
            infoSeminary = new InfoSeminary();
            infoSeminary.copyFromDomain(seminary);
        }
        return infoSeminary;
    }


}
