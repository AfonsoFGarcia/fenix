/*
 * Created on 17/Set/2003
 *
 */
package ServidorAplicacao.Servico.fileManager;

import org.apache.slide.common.SlideException;

import Dominio.IItem;
import Dominio.Item;
import ServidorAplicacao.IServico;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentItem;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;
import fileSuport.FileSuport;
import fileSuport.IFileSuport;

/**
 *fenix-head
 *ServidorAplicacao.Servico.fileManager
 * @author Jo�o Mota
 *17/Set/2003
 *
 */
public class DeleteItemFile implements IServico {

	private static DeleteItemFile service = new DeleteItemFile();

	public static DeleteItemFile getService() {

		return service;
	}

	private DeleteItemFile() {

	}

	public final String getNome() {

		return "DeleteItemFile";
	}

	public void run(Integer itemId, String fileName)
		throws FenixServiceException {
			
		try {
			ISuportePersistente sp = SuportePersistenteOJB.getInstance();
			IPersistentItem persistentItem = sp.getIPersistentItem();
			IItem item = new Item(itemId);
			item = (IItem) persistentItem.readByOId(item, false);
			IFileSuport fileSuport = FileSuport.getInstance();
			fileSuport.deleteFile(item.getSlideName()+"/"+fileName);
		} catch (ExcepcaoPersistencia e) {
			throw new FenixServiceException(e);
		} catch (SlideException e) {
			throw new FenixServiceException(e);
		}
	
		
	}
}
