/*
 * DeleteAnnouncement.java
 *
 */

package ServidorAplicacao.Servico.gesdis.teacher;

/**
 *
 * @author  EP15
 * @author Jo�o Mota
 */

import DataBeans.InfoAnnouncement;
import DataBeans.InfoExecutionCourse;
import DataBeans.InfoSite;
import DataBeans.util.Cloner;
import Dominio.IAnnouncement;
import Dominio.ISite;
import Dominio.Site;
import ServidorAplicacao.FenixServiceException;
import ServidorAplicacao.IServico;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentAnnouncement;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;

public class DeleteAnnouncement implements IServico {

	private static DeleteAnnouncement service = new DeleteAnnouncement();

	public static DeleteAnnouncement getService() {
		return service;
	}

	private DeleteAnnouncement() {
	}

	public final String getNome() {
		return "DeleteAnnouncement";
	}

	public Boolean run(InfoExecutionCourse infoExecutionCourse,InfoSite infoSite, InfoAnnouncement infoAnnouncement)
		throws FenixServiceException {

		try {
			String announcementTitle = infoAnnouncement.getTitle();
			
			ISuportePersistente sp = SuportePersistenteOJB.getInstance();

			ISite site = new Site();

			site = Cloner.copyInfoSite2ISite(infoSite);
			
			IPersistentAnnouncement persistentAnnouncement = sp.getIPersistentAnnouncement();
			
			
			IAnnouncement announcement =
				persistentAnnouncement.readAnnouncementByTitleAndCreationDateAndSite(
					announcementTitle,
					infoAnnouncement.getCreationDate(),
					site);
			
			if (announcement != null)
				persistentAnnouncement.delete(announcement);
				
			return new Boolean(true);	
		} catch (ExcepcaoPersistencia e) {
			throw new FenixServiceException(e);
//		} catch (IllegalAccessException e) {
//			throw new FenixServiceException(e);
//		} catch (InvocationTargetException e) {
//			throw new FenixServiceException(e);
		}

	}

}
