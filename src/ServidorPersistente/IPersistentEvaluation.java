package ServidorPersistente;

import java.util.List;

/**
 * @author Fernanda Quit�rio
 * 25/06/2003
 */
public interface IPersistentEvaluation extends IPersistentObject {
	public List readAll() throws ExcepcaoPersistencia;
 
}
