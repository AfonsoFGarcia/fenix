/*
 * Created on 14/Mar/2003
 */
package ServidorAplicacao.Servico.gesdis;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import DataBeans.InfoExecutionCourse;
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
 * @author lmac1
 */

public class ReadAnnouncements implements IServico {
    
	private static ReadAnnouncements _servico = new ReadAnnouncements();
    
	/**
	 * The singleton access method of this class.
	 **/
	public static ReadAnnouncements getService() {
		return _servico;
	}
    
	/**
	 * The ctor of this class.
	 **/
	private ReadAnnouncements() {
	}
    
	/**
	 * Returns the name of this service.
	 **/
	public final String getNome() {
		return "ReadAnnouncements";
	}
    
	/**
	 * Executes the service.
	 *
	 **/
	public List run(InfoSite infoSite) throws FenixServiceException {
        
        try {
        
		ISuportePersistente sp = SuportePersistenteOJB.getInstance();
		
		InfoExecutionCourse infoExecutionCourse = infoSite.getInfoExecutionCourse();
		IDisciplinaExecucao executionCourse = Cloner.copyInfoExecutionCourse2ExecutionCourse(infoExecutionCourse);
		
		ISite site = sp.getIPersistentSite().readByExecutionCourse(executionCourse);
		
		List announcementsList = site.getAnnouncements();
		List infoAnnouncementsList = new ArrayList();
		
        if ( announcementsList!= null && announcementsList.isEmpty() == false) {
					Iterator iterAnnouncements = announcementsList.iterator();
					while (iterAnnouncements.hasNext()) 
					     {
					     	IAnnouncement announcement = (IAnnouncement)iterAnnouncements.next();
						    infoAnnouncementsList.add(Cloner.copyIAnnouncement2InfoAnnouncement(announcement));
					     }
		}
		
        
		return infoAnnouncementsList;
	       } catch (ExcepcaoPersistencia e) {
			throw new FenixServiceException(e);
		}
	}
    
}

