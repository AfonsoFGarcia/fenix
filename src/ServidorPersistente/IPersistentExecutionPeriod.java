package ServidorPersistente;

import java.util.Date;
import java.util.List;

import Dominio.IExecutionPeriod;
import Dominio.IExecutionYear;

/**
 * Created on 11/Fev/2003
 * 
 * @author Jo�o Mota 
 * Package ServidorPersistente
 *  
 */
public interface IPersistentExecutionPeriod extends IPersistentObject
{

    public List readNotClosedPublicExecutionPeriods() throws ExcepcaoPersistencia;

    /**
	 * @return ArrayList
	 * @throws ExcepcaoPersistencia
	 */
    public List readAllExecutionPeriod() throws ExcepcaoPersistencia;

   
    /**
	 * @param executionPeriod
	 * @return boolean
	 */
    public boolean delete(IExecutionPeriod executionPeriod);

    public IExecutionPeriod readActualExecutionPeriod() throws ExcepcaoPersistencia;

    /**
	 * Method readByNameAndExecutionYear.
	 * 
	 * @param string
	 * @param iExecutionYear
	 * @return IExecutionPeriod
	 */
    public IExecutionPeriod readByNameAndExecutionYear(
        String executionPeriodName,
        IExecutionYear executionYear)
        throws ExcepcaoPersistencia;

    /**
	 * @param string
	 * @param year
	 * @return
	 */
    public IExecutionPeriod readBySemesterAndExecutionYear(Integer semester, IExecutionYear year)
        throws ExcepcaoPersistencia;

    /**
	 * @param workingArea
	 * @param executionPeriodToExportDataFrom
	 */
/*    public void transferData(
        IExecutionPeriod executionPeriodToImportDataTo,
        IExecutionPeriod executionPeriodToExportDataFrom)
        throws ExcepcaoPersistencia;
*/
    public List readPublic() throws ExcepcaoPersistencia;

    /**
	 * @param executionYear
	 * @return @throws
	 *         ExcepcaoPersistencia
	 */
    public List readByExecutionYear(IExecutionYear executionYear) throws ExcepcaoPersistencia;

    /**
	 * @return
	 */
    public List readNotClosedExecutionPeriods() throws ExcepcaoPersistencia;
    
    public List readExecutionPeriodsInTimePeriod(Date start, Date end) throws ExcepcaoPersistencia;
}
