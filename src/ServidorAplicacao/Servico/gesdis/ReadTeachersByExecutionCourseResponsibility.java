/*
 * Created on 25/Mar/2003
 *
 * To change this generated comment go to 
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
package ServidorAplicacao.Servico.gesdis;

import java.util.List;

import DataBeans.InfoExecutionCourse;
import DataBeans.util.Cloner;
import Dominio.IDisciplinaExecucao;
import ServidorAplicacao.FenixServiceException;
import ServidorAplicacao.IServico;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentTeacher;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;

/**
 * @author jmota
 *
 * To change this generated comment go to 
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
public class ReadTeachersByExecutionCourseResponsibility implements IServico {
	private static ReadTeachersByExecutionCourseResponsibility service = new ReadTeachersByExecutionCourseResponsibility();

		 /**
		  * The singleton access method of this class.
		  */
		 public static ReadTeachersByExecutionCourseResponsibility getService() {
		   return service;
		 }
		/**
		 * 
		 */
		public ReadTeachersByExecutionCourseResponsibility() {
		
		}

		/* (non-Javadoc)
		 * @see ServidorAplicacao.IServico#getNome()
		 */
		public String getNome() {
		
			return "ReadTeachersByExecutionCourseResponsibility";
		}
	
		public List run(InfoExecutionCourse infoExecutionCourse) throws FenixServiceException {
			try {
				List result= null;
				ISuportePersistente sp;
				sp = SuportePersistenteOJB.getInstance();
				IDisciplinaExecucao executionCourse =
							Cloner.copyInfoExecutionCourse2ExecutionCourse(infoExecutionCourse);
				IPersistentTeacher persistentTeacher=sp.getIPersistentTeacher();
				result=persistentTeacher.readTeacherByExecutionCourseResponsibility(executionCourse);	
				
						return result;
					} catch (ExcepcaoPersistencia e) {
						throw new FenixServiceException(e);
					} 

		}
}
