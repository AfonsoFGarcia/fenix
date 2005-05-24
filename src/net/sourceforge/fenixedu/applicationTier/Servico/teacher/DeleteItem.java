package net.sourceforge.fenixedu.applicationTier.Servico.teacher;

import java.util.Iterator;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.IServico;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.notAuthorizedServiceDeleteException;
import net.sourceforge.fenixedu.domain.IItem;
import net.sourceforge.fenixedu.domain.Item;
import net.sourceforge.fenixedu.fileSuport.FileSuport;
import net.sourceforge.fenixedu.fileSuport.IFileSuport;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentItem;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;

/**
 * @author Fernanda Quit�rio
 *  
 */
public class DeleteItem implements IServico {
    private static DeleteItem service = new DeleteItem();

    public static DeleteItem getService() {
        return service;
    }

    private DeleteItem() {
    }

    public final String getNome() {
        return "DeleteItem";
    }

    public Boolean run(Integer infoExecutionCourseCode, Integer itemCode) throws FenixServiceException {
        try {

            ISuportePersistente persistentSuport = PersistenceSupportFactory.getDefaultPersistenceSupport();
            IPersistentItem persistentItem = persistentSuport.getIPersistentItem();
            IItem deletedItem = (IItem) persistentItem.readByOID(Item.class, itemCode);
            if (deletedItem == null) {
                return new Boolean(true);
            }
            IFileSuport fileSuport = FileSuport.getInstance();
            long size = 1;

            size = fileSuport.getDirectorySize(deletedItem.getSlideName());

            if (size > 0) {
                throw new notAuthorizedServiceDeleteException();
            }
            Integer orderOfDeletedItem = deletedItem.getItemOrder();
            
            if((deletedItem.getSection() != null) && (deletedItem.getSection().getAssociatedItems() != null))
                deletedItem.getSection().getAssociatedItems().remove(deletedItem);
            
            deletedItem.setSection(null);
           
            persistentItem.deleteByOID(Item.class, deletedItem.getIdInternal());

            persistentSuport.confirmarTransaccao();
            persistentSuport.iniciarTransaccao();
            List itemsList = null;
            itemsList = persistentItem.readAllItemsBySection(deletedItem.getSection().getIdInternal(),
                    deletedItem.getSection().getSite().getExecutionCourse().getSigla(),
                    deletedItem.getSection().getSite().getExecutionCourse().getExecutionPeriod().getExecutionYear().getYear(),
                    deletedItem.getSection().getSite().getExecutionCourse().getExecutionPeriod().getName());
            Iterator iterItems = itemsList.iterator();
            while (iterItems.hasNext()) {
                IItem item = (IItem) iterItems.next();
                Integer itemOrder = item.getItemOrder();
                if (itemOrder.intValue() > orderOfDeletedItem.intValue()) {
                    persistentItem.simpleLockWrite(item);
                    item.setItemOrder(new Integer(itemOrder.intValue() - 1));
                }
            }
            return new Boolean(true);
        } catch (ExcepcaoPersistencia e) {
            throw new FenixServiceException(e);
        }
    }
}