/*
 * Created on 18/Ago/2003
 */
package ServidorAplicacao.Servico.manager;

import DataBeans.InfoExecutionDegree;
import DataBeans.InfoExecutionYear;
import DataBeans.InfoTeacher;
import Dominio.CursoExecucao;
import Dominio.ICursoExecucao;
import Dominio.IExecutionYear;
import Dominio.ITeacher;
import Dominio.Teacher;
import ServidorAplicacao.IServico;
import ServidorAplicacao.Servico.exceptions.ExistingServiceException;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.ICursoExecucaoPersistente;
import ServidorPersistente.IPersistentExecutionYear;
import ServidorPersistente.IPersistentTeacher;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;
import ServidorPersistente.exceptions.ExistingPersistentException;

/**
 * @author lmac1
 */

public class EditExecutionDegree implements IServico {

	private static EditExecutionDegree service = new EditExecutionDegree();

	public static EditExecutionDegree getService() {
		return service;
	}

	private EditExecutionDegree() {
	}

	public final String getNome() {
		return "EditExecutionDegree";
	}

	public Boolean run(InfoExecutionDegree infoExecutionDegree, Integer executionDegreeId) throws FenixServiceException {

		ICursoExecucaoPersistente persistentExecutionDegree = null;
		String executionYearString = "";
		
		try {
				ISuportePersistente persistentSuport = SuportePersistenteOJB.getInstance();
				persistentExecutionDegree = persistentSuport.getICursoExecucaoPersistente();
				CursoExecucao execDegree = new CursoExecucao();
				execDegree.setIdInternal(executionDegreeId);
				ICursoExecucao oldExecutionDegree = (ICursoExecucao) persistentExecutionDegree.readByOId(execDegree, false);
		
				if(oldExecutionDegree != null) {
					InfoExecutionYear infoExecutionYear = infoExecutionDegree.getInfoExecutionYear();
					executionYearString = infoExecutionYear.getYear();

					IPersistentExecutionYear persistentExecutionYear = persistentSuport.getIPersistentExecutionYear();
					IExecutionYear executionYear = persistentExecutionYear.readExecutionYearByName(executionYearString);

					oldExecutionDegree.setExecutionYear(executionYear);

					Boolean tempExamMap = infoExecutionDegree.getTemporaryExamMap();
					if (tempExamMap != null)
						oldExecutionDegree.setTemporaryExamMap(tempExamMap);
					else
						oldExecutionDegree.setTemporaryExamMap(null);

					IPersistentTeacher persistentTeacher = persistentSuport.getIPersistentTeacher();
					InfoTeacher infoTeacher = infoExecutionDegree.getInfoCoordinator();

					ITeacher teacher = (ITeacher) persistentTeacher.readByOId(new Teacher(infoExecutionDegree.getInfoCoordinator().getIdInternal()), false);
					oldExecutionDegree.setCoordinator(teacher);

					persistentExecutionDegree.lockWrite(oldExecutionDegree);
					
					return new Boolean(true);
				}
				
				return new Boolean(false);
						
		} catch (ExistingPersistentException ex) {
			throw new ExistingServiceException("O curso execu��o relativo ao ano " + executionYearString, ex);
		} catch (ExcepcaoPersistencia excepcaoPersistencia) {
			throw new FenixServiceException(excepcaoPersistencia);
		} 
	}
}