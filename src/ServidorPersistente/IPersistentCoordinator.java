package ServidorPersistente;

import java.util.List;

import Dominio.ITeacher;



/**
 * Created on 2003/10/27
 * @author Jo�o Mota
 * Package ServidorPersistente
 * 
 */
public interface IPersistentCoordinator extends IPersistentObject {

	public List readDegreesByCoordinator(ITeacher teacher)
			throws ExcepcaoPersistencia;	
}
