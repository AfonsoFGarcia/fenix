package ServidorAplicacao.Servico.gesdis;

import DataBeans.InfoExecutionCourse;
import DataBeans.gesdis.InfoAnnouncement;
import DataBeans.gesdis.InfoSite;
import DataBeans.util.Cloner;
import Dominio.IAnnouncement;
import Dominio.IDisciplinaExecucao;
import Dominio.ISite;
import ServidorAplicacao.FenixServiceException;
import ServidorAplicacao.IServico;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;

/**
 * @author Ivo Brand�o
 */
public class ReadLastAnnouncement implements IServico {
    
	private static ReadLastAnnouncement service = new ReadLastAnnouncement();
    
	/**
	 * The singleton access method of this class.
	 */
	public static ReadLastAnnouncement getService() {
		return service;
	}
    
	/**
	 * Constructor.
	 */
	private ReadLastAnnouncement() {
	}
    
	/**
	 * Returns the name of this service.
	 */
	public final String getNome() {
		return "ReadLastAnnouncement";
	}
    
	/**
	 * Executes the service.
	 */
	public InfoAnnouncement run(InfoSite infoSite) throws FenixServiceException {
        
		try {
			ISuportePersistente persistentSupport = SuportePersistenteOJB.getInstance();

			InfoExecutionCourse infoExecutionCourse = infoSite.getInfoExecutionCourse();
			IDisciplinaExecucao executionCourse = Cloner.copyInfoExecutionCourse2ExecutionCourse(infoExecutionCourse);

			ISite site = persistentSupport.getIPersistentSite().readByExecutionCourse(executionCourse);

			IAnnouncement announcement = persistentSupport.getIPersistentAnnouncement().readLastAnnouncementForSite(site);
			
			InfoAnnouncement infoAnnouncement = null;
			if (announcement!=null)
				infoAnnouncement = Cloner.copyIAnnouncement2InfoAnnouncement(announcement);

			return infoAnnouncement;
		} catch (ExcepcaoPersistencia e) {
			throw new FenixServiceException(e);
		}
	}
}