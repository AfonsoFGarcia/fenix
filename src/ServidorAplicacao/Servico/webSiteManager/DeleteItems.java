package ServidorAplicacao.Servico.webSiteManager;

import java.util.Iterator;
import java.util.List;

import Dominio.IWebSiteItem;
import Dominio.IWebSiteSection;
import Dominio.WebSiteItem;
import Dominio.WebSiteSection;
import ServidorAplicacao.IServico;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentWebSiteItem;
import ServidorPersistente.IPersistentWebSiteSection;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;

/**
 * @author Fernanda Quit�rio 26/09/2003
 *  
 */
public class DeleteItems implements IServico {

    private static DeleteItems service = new DeleteItems();

    public static DeleteItems getService() {

        return service;
    }

    private DeleteItems() {

    }

    public final String getNome() {

        return "DeleteItems";
    }

    //infoItem with an infoSection

    public boolean run(Integer sectionCode, List itemsToDelete)
            throws FenixServiceException {

        try {
            ISuportePersistente persistentSuport = SuportePersistenteOJB
                    .getInstance();
            IPersistentWebSiteSection persistentWebSiteSection = persistentSuport
                    .getIPersistentWebSiteSection();
            IPersistentWebSiteItem persistentWebSiteItem = persistentSuport
                    .getIPersistentWebSiteItem();

            IWebSiteSection webSiteSection = (IWebSiteSection) persistentWebSiteSection
                    .readByOID(WebSiteSection.class, sectionCode);

            Iterator iterItemsCode = itemsToDelete.iterator();
            while (iterItemsCode.hasNext()) {
                Integer itemCode = (Integer) iterItemsCode.next();

                IWebSiteItem webSiteItemAux = (IWebSiteItem) persistentWebSiteItem
                        .readByOID(WebSiteItem.class, itemCode, true);
                if (webSiteItemAux != null) {
                    persistentWebSiteItem.delete(webSiteItemAux);
                }
            }
        } catch (ExcepcaoPersistencia excepcaoPersistencia) {
            throw new FenixServiceException(excepcaoPersistencia);
        }
        return true;
    }
}