package net.sourceforge.fenixedu.persistenceTier;

import java.util.Date;
import java.util.List;

import net.sourceforge.fenixedu.domain.ExecutionYear;

/**
 * Created on 11/Fev/2003
 * 
 * @author Jo�o Mota Package ServidorPersistente
 *  
 */
public interface IPersistentExecutionYear extends IPersistentObject {

    public ExecutionYear readExecutionYearByName(String year) throws ExcepcaoPersistencia;
    
    public List readExecutionYearsInPeriod(Date start, Date end) throws ExcepcaoPersistencia;
}