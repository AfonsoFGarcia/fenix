/*
 * Created on May 5, 2004
 */
package net.sourceforge.fenixedu.domain.grant.contract;

import java.util.Calendar;
import java.util.Date;

/**
 * @author Pica
 * @author Barbosa
 */
public class GrantContractRegime extends GrantContractRegime_Base {

    public Boolean getContractRegimeActive() {
        if (getDateEndContract().after(Calendar.getInstance().getTime())) {
            return Boolean.TRUE;
        }
        return Boolean.FALSE;
    }
    public boolean belongsToPeriod(Date beginDate, Date endDate) {
        if (!this.getDateBeginContract().after(endDate)
                && !this.getDateEndContract().before(beginDate)) {
            return true;
        }
        return false;
    }

}
