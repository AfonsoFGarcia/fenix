package ServidorAplicacao.Servico.teacher;

import Dominio.BibliographicReference;
import Dominio.IBibliographicReference;
import Dominio.ISite;
import ServidorAplicacao.IServico;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorAplicacao.Servico.exceptions.InvalidArgumentsServiceException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentBibliographicReference;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;

/**
 * @author  Fernanda Quit�rio
 * 
 */
public class EditBibliographicReference implements IServico {
	private static EditBibliographicReference service = new EditBibliographicReference();
	/**
	 * The singleton access method of this class.
	 */
	public static EditBibliographicReference getService() {
		return service;
	}
	/**
	 * The constructor of this class.
	 */
	private EditBibliographicReference() {
	}
	/**
	 * Devolve o nome do servico
	 */
	public final String getNome() {
		return "EditBibliographicReference";
	}
	/**
	 * Executes the service.
	 */
	public boolean run(
		Integer infoExecutionCourseCode,
		Integer bibliographicReferenceCode,
		String newTitle,
		String newAuthors,
		String newReference,
		String newYear,
		Boolean optional)
		throws FenixServiceException {

		ISite site = null;
		IBibliographicReference ibibliographicReference = null;
		try {

			ISuportePersistente persistentSupport = SuportePersistenteOJB.getInstance();
			IPersistentBibliographicReference persistentBibliographicReference =
				persistentSupport.getIPersistentBibliographicReference();

			BibliographicReference bibliographicReference = new BibliographicReference(bibliographicReferenceCode);
			ibibliographicReference = (IBibliographicReference) persistentBibliographicReference.readByOId(bibliographicReference, true);


			if (ibibliographicReference == null) {
				throw new InvalidArgumentsServiceException();
			}
//			persistentBibliographicReference.lockWrite(ibibliographicReference);

			ibibliographicReference.setTitle(newTitle);
			ibibliographicReference.setAuthors(newAuthors);
			ibibliographicReference.setReference(newReference);
			ibibliographicReference.setYear(newYear);
			ibibliographicReference.setOptional(optional);
			

		} catch (ExcepcaoPersistencia excepcaoPersistencia) {
			throw new FenixServiceException(excepcaoPersistencia.getMessage());
		}

		return true;
	}
}