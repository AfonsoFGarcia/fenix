/*
 * Created on 23/Jul/2003
 *
 */

package ServidorAplicacao.Servico.teacher;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import DataBeans.ExecutionCourseSiteView;
import DataBeans.InfoMetadata;
import DataBeans.InfoSiteMetadatas;
import DataBeans.SiteView;
import DataBeans.util.Cloner;
import Dominio.DisciplinaExecucao;
import Dominio.IDisciplinaExecucao;
import Dominio.IMetadata;
import ServidorAplicacao.IServico;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorAplicacao.Servico.exceptions.InvalidArgumentsServiceException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IDisciplinaExecucaoPersistente;
import ServidorPersistente.IPersistentMetadata;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;
import UtilTests.ParseMetadata;

/**
 * @author Susana Fernandes
 */

public class ReadMetadatas implements IServico {

	private static ReadMetadatas service = new ReadMetadatas();

	public static ReadMetadatas getService() {
		return service;
	}

	public String getNome() {
		return "ReadMetadatas";
	}
	public SiteView run(Integer executionCourseId)
		throws FenixServiceException {

		try {
			ISuportePersistente persistentSuport =
				SuportePersistenteOJB.getInstance();
			IDisciplinaExecucaoPersistente persistentExecutionCourse =
				persistentSuport.getIDisciplinaExecucaoPersistente();
			IDisciplinaExecucao executionCourse =
				new DisciplinaExecucao(executionCourseId);
			executionCourse =
				(IDisciplinaExecucao) persistentExecutionCourse.readByOId(
					executionCourse,
					false);
			if (executionCourse == null) {
				throw new InvalidArgumentsServiceException();
			}

			IPersistentMetadata persistentMetadata =
				(IPersistentMetadata) persistentSuport.getIPersistentMetadata();

			List metadatas =
				persistentMetadata.readByExecutionCourse(executionCourse);
			List result = new ArrayList();
			Iterator iter = metadatas.iterator();
			while (iter.hasNext()) {
				IMetadata metadata = (IMetadata) iter.next();
				InfoMetadata infoMetadata =
					Cloner.copyIMetadata2InfoMetadata(metadata);
				ParseMetadata p = new ParseMetadata();
				try {
					infoMetadata =
						p.parseMetadata(
							metadata.getMetadataFile(),
							infoMetadata);
				} catch (Exception e) {
					throw new FenixServiceException(e);
				}
				result.add(infoMetadata);
			}
			InfoSiteMetadatas bodyComponent = new InfoSiteMetadatas();
			bodyComponent.setInfoMetadatas(result);
			bodyComponent.setExecutionCourse(
				Cloner.copyIExecutionCourse2InfoExecutionCourse(
					executionCourse));
			SiteView siteView =
				new ExecutionCourseSiteView(bodyComponent, bodyComponent);

			return siteView;
		} catch (ExcepcaoPersistencia e) {
			throw new FenixServiceException(e);
		}
	}
}
