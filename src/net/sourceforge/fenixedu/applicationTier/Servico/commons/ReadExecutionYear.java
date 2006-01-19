/*
 * Created on 23/Abr/2003
 *
 * 
 */
package net.sourceforge.fenixedu.applicationTier.Servico.commons;

import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionYear;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentExecutionYear;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;
import net.sourceforge.fenixedu.applicationTier.Service;

/**
 * @author Jo�o Mota
 * 
 * 
 */
public class ReadExecutionYear extends Service {

    public InfoExecutionYear run(String year) throws ExcepcaoPersistencia {

        ISuportePersistente sp = PersistenceSupportFactory.getDefaultPersistenceSupport();
        IPersistentExecutionYear executionYearDAO = sp.getIPersistentExecutionYear();
        ExecutionYear executionYear = executionYearDAO.readExecutionYearByName(year);

        if (executionYear != null) {
            return InfoExecutionYear.newInfoFromDomain(executionYear);
        }
        
        return null;
    }

}
