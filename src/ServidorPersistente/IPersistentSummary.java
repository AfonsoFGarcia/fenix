/*
 * Created on 21/Jul/2003
 *
 * 
 */
package ServidorPersistente;

import java.util.List;

import Dominio.IDisciplinaExecucao;
import Dominio.ISummary;
import Util.TipoAula;

/**
 * @author Jo�o Mota
 *
 * 21/Jul/2003
 * fenix-head
 * ServidorPersistente.OJB
 * 
 */
public interface IPersistentSummary extends IPersistentObject{
	public  List readByExecutionCourse(IDisciplinaExecucao executionCourse)
		throws ExcepcaoPersistencia;		
	public  List readByExecutionCourseAndType(
		IDisciplinaExecucao executionCourse,
		TipoAula summaryType)
		throws ExcepcaoPersistencia;
	public abstract void delete (ISummary summary) throws ExcepcaoPersistencia;	
}