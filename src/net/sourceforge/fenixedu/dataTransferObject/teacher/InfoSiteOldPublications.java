/*
 * Created on Nov 13, 2003
 *  
 */
package net.sourceforge.fenixedu.dataTransferObject.teacher;

import java.util.List;

import net.sourceforge.fenixedu.dataTransferObject.DataTranferObject;
import net.sourceforge.fenixedu.dataTransferObject.ISiteComponent;
import net.sourceforge.fenixedu.dataTransferObject.InfoTeacher;
import net.sourceforge.fenixedu.util.OldPublicationType;

/**
 * @author Leonor Almeida
 * @author Sergio Montelobo
 *  
 */
public class InfoSiteOldPublications extends DataTranferObject implements ISiteComponent {

    private List infoOldPublications;

    private OldPublicationType oldPublicationType;

    private InfoTeacher infoTeacher;

    public InfoSiteOldPublications() {
    }

    public Integer getNumberOldPublications() {
        return new Integer(infoOldPublications.size());
    }

    /**
     * @return Returns the infoOldPublications.
     */
    public List getInfoOldPublications() {
        return infoOldPublications;
    }

    /**
     * @param infoOldPublications
     *            The infoOldPublications to set.
     */
    public void setInfoOldPublications(List infoOldPublications) {
        this.infoOldPublications = infoOldPublications;
    }

    /**
     * @return Returns the infoTeacher.
     */
    public InfoTeacher getInfoTeacher() {
        return infoTeacher;
    }

    /**
     * @param infoTeacher
     *            The infoTeacher to set.
     */
    public void setInfoTeacher(InfoTeacher infoTeacher) {
        this.infoTeacher = infoTeacher;
    }

    /**
     * @return Returns the oldPublicationType.
     */
    public OldPublicationType getOldPublicationType() {
        return oldPublicationType;
    }

    /**
     * @param oldPublicationType
     *            The oldPublicationType to set.
     */
    public void setOldPublicationType(OldPublicationType oldPublicationType) {
        this.oldPublicationType = oldPublicationType;
    }

}