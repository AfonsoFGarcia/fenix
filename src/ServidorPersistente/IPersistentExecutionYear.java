package ServidorPersistente;

import java.util.ArrayList;

import Dominio.IExecutionYear;
import ServidorPersistente.exceptions.ExistingPersistentException;

/**
 * Created on 11/Fev/2003
 * @author Jo�o Mota
 * Package ServidorPersistente
 * 
 */
public interface IPersistentExecutionYear extends IPersistentObject {
	/**
	 * 
	 * @param year
	 * @return IExecutionYear
	 */
	public IExecutionYear readExecutionYearByName(String year) throws ExcepcaoPersistencia;
	/**
	 * 
	 * @return ArrayList
	 */
	public ArrayList readAllExecutionYear()throws ExcepcaoPersistencia;
	/**
	 * 
	 * @param executionYear
	 * @return boolean
	 */
	public void writeExecutionYear(IExecutionYear executionYear) throws ExcepcaoPersistencia, ExistingPersistentException;
	/**
	 * 
	 * @return boolean
	 */
	public boolean deleteAll();
	
	/**
	 * 
	 * @param executionYear
	 * @return boolean
	 */
	public boolean delete(IExecutionYear executionYear);
	
	
	public IExecutionYear readCurrentExecutionYear() throws ExcepcaoPersistencia ;
}
