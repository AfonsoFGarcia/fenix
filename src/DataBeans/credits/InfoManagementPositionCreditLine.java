/*
 * Created on 7/Mar/2004
 */
package DataBeans.credits;

/**
 * @author jpvl
 */
public class InfoManagementPositionCreditLine extends InfoDatePeriodBaseCreditLine {

    private String position;

    private Double credits;

    /**
     * @return Returns the position.
     */
    public String getPosition() {
        return position;
    }

    /**
     * @param position
     *            The position to set.
     */
    public void setPosition(String position) {
        this.position = position;
    }

    public Double getCredits() {
        return credits;
    }

    public void setCredits(Double credits) {
        this.credits = credits;
    }
}