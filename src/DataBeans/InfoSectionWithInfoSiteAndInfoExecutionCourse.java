/*
 * Created on 6/Jul/2004
 *
 */
package DataBeans;

import Dominio.ISection;

/**
 * @author T�nia Pous�o
 *
 */
public class InfoSectionWithInfoSiteAndInfoExecutionCourse extends InfoSection {

    /* (non-Javadoc)
     * @see DataBeans.InfoSection#copyFromDomain(Dominio.ISection)
     */
    public void copyFromDomain(ISection section) {
        super.copyFromDomain(section);
        if(section != null) {
            setInfoSite(InfoSiteWithInfoExecutionCourse.newInfoFromDomain(section.getSite()));
        }
    }
    
    public static InfoSection newInfoFromDomain(ISection section) {
        InfoSectionWithInfoSiteAndInfoExecutionCourse infoSection = null;
        if(section != null) {
            infoSection = new InfoSectionWithInfoSiteAndInfoExecutionCourse();
            infoSection.copyFromDomain(section);
        }
        return infoSection;
    }
}
