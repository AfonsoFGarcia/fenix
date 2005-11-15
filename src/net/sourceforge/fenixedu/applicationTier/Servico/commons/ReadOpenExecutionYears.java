/*
 * Created on Oct 7, 2004
 */
package net.sourceforge.fenixedu.applicationTier.Servico.commons;

import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentExecutionYear;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;
import pt.utl.ist.berserk.logic.serviceManager.IService;

/**
 * @author nmgo
 * @author lmre
 */
public class ReadOpenExecutionYears implements IService {

    public List run() throws FenixServiceException, ExcepcaoPersistencia {
        
        IPersistentExecutionYear persistentExecutionYear = PersistenceSupportFactory
                .getDefaultPersistenceSupport().getIPersistentExecutionYear();
        
        return persistentExecutionYear.readOpenExecutionYears();

    }
}