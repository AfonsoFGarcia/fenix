package ServidorPersistente;

import java.util.ArrayList;

import Dominio.ICurso;
import Dominio.ICursoExecucao;
import Dominio.IDegreeCurricularPlan;
import ServidorPersistente.exceptions.ExistingPersistentException;

public interface IPersistentDegreeCurricularPlan extends IPersistentObject {

	public ArrayList readAll() throws ExcepcaoPersistencia;
	public void write(IDegreeCurricularPlan planoCurricular) throws ExcepcaoPersistencia, ExistingPersistentException;
	public void deleteDegreeCurricularPlan(IDegreeCurricularPlan planoCurricular) throws ExcepcaoPersistencia;
	public void deleteAll() throws ExcepcaoPersistencia;
	public IDegreeCurricularPlan readByNameAndDegree(String name, ICurso degree) throws ExcepcaoPersistencia;
//	public IDegreeCurricularPlan readByExecutionDegree(ICursoExecucao executionDegree) throws ExcepcaoPersistencia;
}