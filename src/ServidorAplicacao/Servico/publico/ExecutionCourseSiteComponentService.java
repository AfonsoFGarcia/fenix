/*
 * Created on 6/Mai/2003
 *
 *
 */
package ServidorAplicacao.Servico.publico;

import DataBeans.ExecutionCourseSiteView;
import DataBeans.ISiteComponent;
import Dominio.DisciplinaExecucao;
import Dominio.IDisciplinaExecucao;
import Dominio.ISite;
import Dominio.Site;
import ServidorAplicacao.FenixServiceException;
import ServidorAplicacao.IServico;
import ServidorAplicacao.Factory.ExecutionCourseSiteComponentBuilder;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IDisciplinaExecucaoPersistente;
import ServidorPersistente.IPersistentSite;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;

/**
 * @author Jo�o Mota
 *
 * 
 */
public class ExecutionCourseSiteComponentService implements IServico {

	private static ExecutionCourseSiteComponentService _servico =
		new ExecutionCourseSiteComponentService();

	/**
	  * The actor of this class.
	  **/

	private ExecutionCourseSiteComponentService() {

	}

	/**
	 * Returns Service Name
	 */
	public String getNome() {
		return "ExecutionCourseSiteComponentService";
	}

	/**
	 * Returns the _servico.
	 * @return ReadExecutionCourse
	 */
	public static ExecutionCourseSiteComponentService getService() {
		return _servico;
	}

	public Object run(
		ISiteComponent commonComponent,
		ISiteComponent bodyComponent,
		Integer infoSiteCode,
		Integer infoExecutionCourseCode,
		Integer sectionIndex)
		throws FenixServiceException {
		ExecutionCourseSiteView siteView = null;

		try {
			ISuportePersistente sp = SuportePersistenteOJB.getInstance();
			IDisciplinaExecucaoPersistente persistentExecutionCourse =
				sp.getIDisciplinaExecucaoPersistente();
			IPersistentSite persistentSite = sp.getIPersistentSite();
			ISite site = null;
			if (infoSiteCode != null) {

				
					site =(ISite) persistentSite.readByOId(new Site(infoSiteCode));
			} else {
				IDisciplinaExecucao executionCourse =
					(IDisciplinaExecucao) persistentExecutionCourse.readByOId(new DisciplinaExecucao(infoExecutionCourseCode));
				 site = persistentSite.readByExecutionCourse(executionCourse); 					
			}
			ExecutionCourseSiteComponentBuilder componentBuilder =
				ExecutionCourseSiteComponentBuilder.getInstance();
			commonComponent =
				componentBuilder.getComponent(
					commonComponent,
					site,
					null,
					null);
			bodyComponent =
				componentBuilder.getComponent(
					bodyComponent,
					site,
					commonComponent,
					sectionIndex);

			siteView =
				new ExecutionCourseSiteView(commonComponent, bodyComponent);
		} catch (ExcepcaoPersistencia e) {
			throw new FenixServiceException(e);
		}

		return siteView;
	}
}
