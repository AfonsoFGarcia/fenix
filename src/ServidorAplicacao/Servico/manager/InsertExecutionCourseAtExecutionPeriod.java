/*
 * Created on 24/Set/2003
 */
package ServidorAplicacao.Servico.manager;

import DataBeans.InfoExecutionCourse;
import Dominio.DisciplinaExecucao;
import Dominio.ExecutionPeriod;
import Dominio.IDisciplinaExecucao;
import Dominio.IExecutionPeriod;
import ServidorAplicacao.IServico;
import ServidorAplicacao.Servico.exceptions.ExistingServiceException;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorAplicacao.Servico.exceptions.NonExistingServiceException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IDisciplinaExecucaoPersistente;
import ServidorPersistente.IPersistentExecutionPeriod;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;
import ServidorPersistente.exceptions.ExistingPersistentException;

/**
 * @author lmac1
 */
public class InsertExecutionCourseAtExecutionPeriod implements IServico {

	private static InsertExecutionCourseAtExecutionPeriod service = new InsertExecutionCourseAtExecutionPeriod();

	public static InsertExecutionCourseAtExecutionPeriod getService() {
		return service;
	}

	private InsertExecutionCourseAtExecutionPeriod() {
	}

	public final String getNome() {
		return "InsertExecutionCourseAtExecutionPeriod";
	}
	

	public void run(InfoExecutionCourse infoExecutionCourse) throws FenixServiceException {
		IDisciplinaExecucao executionCourse = new DisciplinaExecucao();
		try {
				ISuportePersistente persistentSuport = SuportePersistenteOJB.getInstance();
								
				IPersistentExecutionPeriod persistentExecutionPeriod = persistentSuport.getIPersistentExecutionPeriod();
				IExecutionPeriod executionPeriod = (IExecutionPeriod) persistentExecutionPeriod.readByOId(new ExecutionPeriod(infoExecutionCourse.getInfoExecutionPeriod().getIdInternal()), false);
				
				if(executionPeriod == null)
					throw new NonExistingServiceException("message.nonExistingExecutionPeriod", null);
			
				IDisciplinaExecucaoPersistente persistentExecutionCourse = persistentSuport.getIDisciplinaExecucaoPersistente();
				
				
			
			executionCourse.setNome(infoExecutionCourse.getNome());
			
			executionCourse.setExecutionPeriod(executionPeriod);
			
			executionCourse.setSigla(infoExecutionCourse.getSigla());
			
			executionCourse.setLabHours(infoExecutionCourse.getLabHours());
			executionCourse.setPraticalHours(infoExecutionCourse.getPraticalHours());
			executionCourse.setTheoPratHours(infoExecutionCourse.getTheoPratHours());
			executionCourse.setTheoreticalHours(infoExecutionCourse.getTheoreticalHours());
			executionCourse.setComment(infoExecutionCourse.getComment());
					
				persistentExecutionCourse.lockWrite(executionCourse);
					
		} catch (ExistingPersistentException existingException) {
			throw new ExistingServiceException("A disciplina execu��o:+" + executionCourse.getNome(), existingException); 
		} catch (ExcepcaoPersistencia excepcaoPersistencia) {
			throw new FenixServiceException(excepcaoPersistencia);
		}
	}
}
