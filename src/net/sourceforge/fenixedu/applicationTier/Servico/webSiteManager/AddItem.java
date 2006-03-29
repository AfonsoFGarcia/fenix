package net.sourceforge.fenixedu.applicationTier.Servico.webSiteManager;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoWebSite;
import net.sourceforge.fenixedu.dataTransferObject.InfoWebSiteItem;
import net.sourceforge.fenixedu.dataTransferObject.InfoWebSiteSection;
import net.sourceforge.fenixedu.domain.DomainFactory;
import net.sourceforge.fenixedu.domain.WebSiteItem;
import net.sourceforge.fenixedu.domain.WebSiteSection;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentWebSiteSection;

/**
 * @author Fernanda Quit�rio 25/09/2003
 * 
 */
public class AddItem extends ManageWebSiteItem {

    public InfoWebSite run(Integer sectionCode, InfoWebSiteItem infoWebSiteItem, String user)
            throws FenixServiceException, ExcepcaoPersistencia {

        List infoWebSiteSections = new ArrayList();

        IPersistentWebSiteSection persistentWebSiteSection = persistentSupport
                .getIPersistentWebSiteSection();

        WebSiteSection webSiteSection = rootDomainObject.readWebSiteSectionByOID(sectionCode);
        
        InfoWebSiteSection infoWebSiteSection = InfoWebSiteSection.newInfoFromDomain(webSiteSection);
        InfoWebSite infoWebSite = InfoWebSite.newInfoFromDomain(webSiteSection.getWebSite());
        infoWebSiteSection.setInfoWebSite(infoWebSite);

        checkData(infoWebSiteItem, webSiteSection);

        WebSiteItem webSiteItem = DomainFactory.makeWebSiteItem();

        fillWebSiteItemForDB(infoWebSiteItem, user, persistentWebSiteSection, webSiteSection,
                webSiteItem);

        List webSiteSections = persistentWebSiteSection.readByWebSite(webSiteSection.getWebSite());
        Iterator iterSections = webSiteSections.iterator();
        while (iterSections.hasNext()) {
            WebSiteSection section = (WebSiteSection) iterSections.next();

            InfoWebSiteSection infoWebSiteSection2 = InfoWebSiteSection.newInfoFromDomain(section);

            infoWebSiteSections.add(infoWebSiteSection2);
        }

        infoWebSite.setSections(infoWebSiteSections);

        return infoWebSite;
    }
}