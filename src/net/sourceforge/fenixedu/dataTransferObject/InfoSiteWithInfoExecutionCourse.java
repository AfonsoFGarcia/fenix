/*
 * Created on 6/Jul/2004
 *
 */
package net.sourceforge.fenixedu.dataTransferObject;

import net.sourceforge.fenixedu.domain.Site;

/**
 * @author T�nia Pous�o
 *  
 */
public class InfoSiteWithInfoExecutionCourse extends InfoSite {

    /*
     * (non-Javadoc)
     * 
     * @see net.sourceforge.fenixedu.dataTransferObject.InfoSite#copyFromDomain(Dominio.Site)
     */
    public void copyFromDomain(Site site) {
        super.copyFromDomain(site);
        if (site != null) {
            setInfoExecutionCourse(InfoExecutionCourseWithExecutionPeriod.newInfoFromDomain(site
                    .getExecutionCourse()));
        }
    }

    public static InfoSite newInfoFromDomain(Site site) {
        InfoSiteWithInfoExecutionCourse infoSite = null;
        if (site != null) {
            infoSite = new InfoSiteWithInfoExecutionCourse();
            infoSite.copyFromDomain(site);
        }
        return infoSite;
    }
}