/*
 * Created on 21/Out/2003
 *
 */
package ServidorPersistente;

import java.util.Calendar;
import java.util.List;

import Dominio.IPeriod;

/**
 * @author Ana e Ricardo
 *
 */
public interface IPersistentPeriod extends IPersistentObject{
	public List readBy(Calendar startDate) throws ExcepcaoPersistencia;
	public List readAll() throws ExcepcaoPersistencia;
	public void delete(IPeriod period) throws ExcepcaoPersistencia;
	public void deleteAll() throws ExcepcaoPersistencia;
    public Object readBy(Calendar startDate, Calendar endDate)throws ExcepcaoPersistencia;

}
