/*
 * IFrequentaPersistente.java
 *
 * Created on 20 de Outubro de 2002, 15:28
 */

package ServidorPersistente;

/**
 *
 * @author  tfc130
 */
import java.util.List;

import Dominio.IDisciplinaExecucao;
import Dominio.IFrequenta;
import Dominio.IStudent;

public interface IFrequentaPersistente extends IPersistentObject {
    public IFrequenta readByAlunoAndDisciplinaExecucao(IStudent aluno, IDisciplinaExecucao disciplinaExecucao) throws ExcepcaoPersistencia;
    public void lockWrite(IFrequenta frequenta) throws ExcepcaoPersistencia;
    public void delete(IFrequenta frequenta) throws ExcepcaoPersistencia;
    public void deleteAll() throws ExcepcaoPersistencia;
	public List readByStudentId(Integer id) throws ExcepcaoPersistencia;
}
