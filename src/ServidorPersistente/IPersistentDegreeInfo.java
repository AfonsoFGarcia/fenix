package ServidorPersistente;

import java.util.List;

import Dominio.ICurso;

/**
 * @author T�nia Pous�o
 * Created on 30/Out/2003
 */
public interface IPersistentDegreeInfo extends IPersistentObject {
	public List readDegreeInfoByDegree(ICurso degree) throws ExcepcaoPersistencia;
}
