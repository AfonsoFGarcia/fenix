/*
 * IPersistentExam.java
 *
 * Created on 2003/03/19
 */

package ServidorPersistente;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import Dominio.IExam;

/**
 *
 * @author  Luis Cruz & Sara Ribeiro
 */
public interface IPersistentExam extends IPersistentObject {
	public List readBy(Date day, Calendar beginning) throws ExcepcaoPersistencia;	
	public List readAll() throws ExcepcaoPersistencia;
    public void delete(IExam exam) throws ExcepcaoPersistencia;
    public void deleteAll() throws ExcepcaoPersistencia;
}
