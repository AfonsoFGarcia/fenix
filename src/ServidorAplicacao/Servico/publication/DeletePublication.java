/*
 * Created on Jun 11, 2004
 * 
 */
package ServidorAplicacao.Servico.publication;

import pt.utl.ist.berserk.logic.serviceManager.IService;
import Dominio.publication.Publication;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;
import ServidorPersistente.publication.IPersistentPublication;

/**
 * @author Carlos Pereira & Francisco Passos
 *  
 */
public class DeletePublication implements IService {

	public DeletePublication() {
	}

	public void run(final Integer publicationId) throws ExcepcaoPersistencia {
		ISuportePersistente sp = SuportePersistenteOJB.getInstance();
		
		IPersistentPublication persistentPublication = sp.getIPersistentPublication();

		persistentPublication.deleteByOID(Publication.class,publicationId);
		
	}
}