package ServidorAplicacao.Servico.teacher;
import java.util.Iterator;
import java.util.List;

import org.apache.slide.common.SlideException;

import fileSuport.FileSuport;
import fileSuport.IFileSuport;

import Dominio.IItem;
import Dominio.Item;
import ServidorAplicacao.IServico;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentItem;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;

/**
 * @author  Fernanda Quit�rio
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
			ISuportePersistente persistentSuport = SuportePersistenteOJB.getInstance();

			IPersistentItem persistentItem = persistentSuport.getIPersistentItem();

			IItem deletedItem = (IItem) persistentItem.readByOId(new Item(itemCode), false);
			if (deletedItem == null) {
				return new Boolean(true);
			}

			Integer orderOfDeletedItem = deletedItem.getItemOrder();
			persistentItem.delete(deletedItem);
			IFileSuport fileSuport = FileSuport.getInstance();
			try {
				fileSuport.deleteFolder(deletedItem.getSlideName());
			} catch (SlideException e1) {
				System.out.println("n�o consegui apagar os ficheiros do item");
			}
			
			persistentSuport.confirmarTransaccao();
			persistentSuport.iniciarTransaccao();

			List itemsList = null;
			itemsList = persistentItem.readAllItemsBySection(deletedItem.getSection());
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
