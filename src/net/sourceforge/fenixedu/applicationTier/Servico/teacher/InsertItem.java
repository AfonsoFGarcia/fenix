package net.sourceforge.fenixedu.applicationTier.Servico.teacher;

import java.util.Iterator;
import java.util.List;

import pt.utl.ist.berserk.logic.serviceManager.IService;
import net.sourceforge.fenixedu.dataTransferObject.InfoItem;
import net.sourceforge.fenixedu.dataTransferObject.util.Cloner;
import net.sourceforge.fenixedu.domain.IItem;
import net.sourceforge.fenixedu.domain.ISection;
import net.sourceforge.fenixedu.domain.Section;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.ExistingServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentItem;
import net.sourceforge.fenixedu.persistenceTier.IPersistentSection;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.OJB.SuportePersistenteOJB;
import net.sourceforge.fenixedu.persistenceTier.exceptions.ExistingPersistentException;

/**
 * @author Fernanda Quit�rio
 */
public class InsertItem implements IService {

    protected int organizeExistingItemsOrder(ISection section, int insertItemOrder)
            throws FenixServiceException {

        IPersistentItem persistentItem = null;
        try {

            ISuportePersistente persistentSuport = SuportePersistenteOJB.getInstance();

            persistentItem = persistentSuport.getIPersistentItem();

            List itemsList = persistentItem.readAllItemsBySection(section);

            if (itemsList != null) {

                if (insertItemOrder == -1) {
                    insertItemOrder = itemsList.size();
                }

                Iterator iterItems = itemsList.iterator();
                while (iterItems.hasNext()) {

                    IItem item = (IItem) iterItems.next();
                    int itemOrder = item.getItemOrder().intValue();

                    if (itemOrder >= insertItemOrder) {
                        persistentItem.simpleLockWrite(item);
                        item.setItemOrder(new Integer(itemOrder + 1));
                    }
                }
            }
        } catch (ExistingPersistentException excepcaoPersistencia) {
            throw new ExistingServiceException(excepcaoPersistencia);
        } catch (ExcepcaoPersistencia excepcaoPersistencia) {
            throw new FenixServiceException(excepcaoPersistencia);
        }
        return insertItemOrder;
    }

    public Boolean run(Integer infoExecutionCourseCode, Integer sectionCode, InfoItem infoItem)
            throws FenixServiceException, ExcepcaoPersistencia {

        final ISuportePersistente persistentSuport = SuportePersistenteOJB.getInstance();
        final IPersistentSection persistentSection = persistentSuport.getIPersistentSection();
        final IPersistentItem persistentItem = persistentSuport.getIPersistentItem();

        final ISection section = (ISection) persistentSection.readByOID(Section.class, sectionCode);
        infoItem.setInfoSection(Cloner.copyISection2InfoSection(section));

        if (persistentItem.readBySectionAndName(section, infoItem.getName()) != null) {
            throw new ExistingServiceException();
        }

        final IItem item = Cloner.copyInfoItem2IItem(infoItem);
        persistentItem.simpleLockWrite(item);
        final Integer itemOrder = new Integer(organizeExistingItemsOrder(section, infoItem
                .getItemOrder().intValue()));
        item.setItemOrder(itemOrder);

        return new Boolean(true);
    }

}