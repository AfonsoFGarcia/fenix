package ServidorAplicacao.Servico.publico;

import java.util.Calendar;
import java.util.Collections;
import java.util.List;

import org.apache.commons.beanutils.BeanComparator;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;
import org.apache.commons.collections.Transformer;

import DataBeans.InfoWebSiteItem;
import DataBeans.InfoWebSiteSection;
import DataBeans.util.Cloner;
import Dominio.IWebSiteItem;
import Dominio.IWebSiteSection;
import Dominio.WebSiteSection;
import ServidorAplicacao.IServico;
import ServidorAplicacao.Servico.ExcepcaoInexistente;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorAplicacao.Servico.exceptions.NonExistingServiceException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentWebSiteItem;
import ServidorPersistente.IPersistentWebSiteSection;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;

/**
 * @author Fernanda Quit�rio 03/10/2003
 *  
 */
public class ReadWebSiteSectionByCode implements IServico {
    private static ReadWebSiteSectionByCode _servico = new ReadWebSiteSectionByCode();

    /**
     * The actor of this class.
     */
    private ReadWebSiteSectionByCode() {

    }

    /**
     * Returns Service Name
     */
    public String getNome() {
        return "ReadWebSiteSectionByCode";
    }

    /**
     * Returns the _servico.
     * 
     * @return ReadWebSiteSectionByCode
     */
    public static ReadWebSiteSectionByCode getService() {
        return _servico;
    }

    public Object run(Integer sectionCode) throws ExcepcaoInexistente,
            FenixServiceException {

        IWebSiteSection webSiteSection;
        InfoWebSiteSection infoWebSiteSection = new InfoWebSiteSection();
        try {
            ISuportePersistente sp = SuportePersistenteOJB.getInstance();
            IPersistentWebSiteSection persistentWebSiteSection = sp
                    .getIPersistentWebSiteSection();
            IPersistentWebSiteItem persistentWebSiteItem = sp
                    .getIPersistentWebSiteItem();

            webSiteSection = (IWebSiteSection) persistentWebSiteSection
                    .readByOID(WebSiteSection.class, sectionCode);

            if (webSiteSection == null) {
                throw new NonExistingServiceException();
            }

            List webSiteItems = persistentWebSiteItem
                    .readPublishedWebSiteItemsByWebSiteSection(webSiteSection);

            if (webSiteItems == null || webSiteItems.size() == 0) {
                throw new NonExistingServiceException();
            }

            // get items with valid dates of publishment
            CollectionUtils.filter(webSiteItems, new Predicate() {
                public boolean evaluate(Object arg0) {
                    IWebSiteItem webSiteItem = (IWebSiteItem) arg0;
                    if (!webSiteItem.getOnlineBeginDay().after(
                            Calendar.getInstance().getTime())
                            && !webSiteItem.getOnlineEndDay().before(
                                    Calendar.getInstance().getTime())) {
                        return true;
                    }

                    return false;
                }
            });
            if (webSiteItems.size() == 0) {
                throw new NonExistingServiceException();
            }

            List infoWebSiteItems = (List) CollectionUtils.collect(
                    webSiteItems, new Transformer() {
                        public Object transform(Object arg0) {
                            IWebSiteItem webSiteItem = (IWebSiteItem) arg0;
                            InfoWebSiteItem infoWebSiteItem = Cloner
                                    .copyIWebSiteItem2InfoWebSiteItem(webSiteItem);

                            return infoWebSiteItem;
                        }
                    });

            Collections.sort(infoWebSiteItems, new BeanComparator(
                    "creationDate"));
            if (webSiteSection.getSortingOrder().equals("descendent")) {
                Collections.reverse(infoWebSiteItems);
            }

            infoWebSiteSection.setInfoItemsList(infoWebSiteItems);

        } catch (ExcepcaoPersistencia e) {
            e.printStackTrace();
            throw new FenixServiceException("error.impossibleReadSection");
        }

        infoWebSiteSection.setIdInternal(webSiteSection.getIdInternal());
        infoWebSiteSection.setExcerptSize(webSiteSection.getExcerptSize());
        infoWebSiteSection.setInfoWebSite(Cloner
                .copyIWebSite2InfoWebSite(webSiteSection.getWebSite()));
        infoWebSiteSection.setName(webSiteSection.getName());
        infoWebSiteSection.setSize(webSiteSection.getSize());
        infoWebSiteSection.setSortingOrder(webSiteSection.getSortingOrder());

        return infoWebSiteSection;
    }
}