/*
 * Created on 13/Nov/2003
 *  
 */

package net.sourceforge.fenixedu.domain.reimbursementGuide;

import java.util.Calendar;

/**
 * @author <a href="mailto:joao.mota@ist.utl.pt">Jo�o Mota </a> 13/Nov/2003
 * 
 */
public class ReimbursementGuideSituation extends ReimbursementGuideSituation_Base {

    /**
     * @return
     */
    public Calendar getModificationDate() {
        if (this.getModification() != null) {
            Calendar result = Calendar.getInstance();
            result.setTime(this.getModification());
            return result;
        }
        return null;
    }

    /**
     * @param modificationDate
     */
    public void setModificationDate(Calendar modificationDate) {
        if (modificationDate != null) {
            this.setModification(modificationDate.getTime());
        } else {
            this.setModification(null);
        }
    }

    /**
     * @return Returns the officialDate.
     */
    public Calendar getOfficialDate() {
        if (this.getOfficial() != null) {
            Calendar result = Calendar.getInstance();
            result.setTime(this.getOfficial());
            return result;
        }
        return null;
    }

    /**
     * @param officialDate
     *            The officialDate to set.
     */
    public void setOfficialDate(Calendar officialDate) {
        if (officialDate != null) {
            this.setOfficial(officialDate.getTime());
        } else {
            this.setOfficial(null);
        }
    }

}
