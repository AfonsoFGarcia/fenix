/*
 * ITurnoPersistente.java
 *
 * Created on 17 de Outubro de 2002, 19:32
 */

package ServidorPersistente;

/**
 *
 * @author  tfc130
 */
import java.util.ArrayList;
import java.util.List;

import Dominio.ICurricularYear;
import Dominio.ICursoExecucao;
import Dominio.IDisciplinaExecucao;
import Dominio.IExecutionPeriod;
import Dominio.ITurma;
import Dominio.ITurno;
import ServidorPersistente.exceptions.ExistingPersistentException;

public interface ITurnoPersistente extends IPersistentObject {
	public ITurno readByNameAndExecutionCourse(
		String nome,
		IDisciplinaExecucao IDE)
		throws ExcepcaoPersistencia;
	public void lockWrite(ITurno turno)
		throws ExcepcaoPersistencia, ExistingPersistentException;
	public void delete(ITurno turno) throws ExcepcaoPersistencia;
	public void deleteAll() throws ExcepcaoPersistencia;
	public Integer countAllShiftsOfAllClassesAssociatedWithShift(ITurno shift)
		throws ExcepcaoPersistencia;

	// FIXME : O metodo nao seleciona bem as turmas ... mas nao da erro na query e usa o associatedCurricularCourses
	public ArrayList readByDisciplinaExecucao(
		String sigla,
		String anoLectivo,
		String siglaLicenciatura)
		throws ExcepcaoPersistencia;

	public List readByExecutionCourseAndType(
		IDisciplinaExecucao executionCourse,
		Integer type)
		throws ExcepcaoPersistencia;

	public List readByExecutionCourse(IDisciplinaExecucao executionCourse)
		throws ExcepcaoPersistencia;
	/**
	 * @param executionDegree
	 * @param curricularYear
	 * @return
	 */
	public List readByExecutionPeriodAndExecutionDegreeAndCurricularYear(
		IExecutionPeriod executionPeriod,
		ICursoExecucao executionDegree,
		ICurricularYear curricularYear)
		throws ExcepcaoPersistencia;
	/**
	 * @param shcoolClass
	 * @return
	 */
	public List readAvailableShiftsForClass(ITurma schoolClass)
		throws ExcepcaoPersistencia;
}
