package net.sourceforge.fenixedu.applicationTier.Servico.commons;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.util.Cloner;
import net.sourceforge.fenixedu.domain.IExecutionYear;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentExecutionYear;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;
import pt.utl.ist.berserk.logic.serviceManager.IService;

/**
 * 
 * @author Fernanda Quit�rio 14/Jan/2004
 *  
 */
public class ReadNotClosedExecutionYears implements IService {

    public ReadNotClosedExecutionYears() {
    }

    public List run() throws FenixServiceException {

        List result = new ArrayList();
        try {
            ISuportePersistente sp = PersistenceSupportFactory.getDefaultPersistenceSupport();
            IPersistentExecutionYear persistentExecutionYear = sp.getIPersistentExecutionYear();

            List executionYears = persistentExecutionYear.readNotClosedExecutionYears();

            if (executionYears != null) {
                for (int i = 0; i < executionYears.size(); i++) {
                    result.add(Cloner.get((IExecutionYear) executionYears.get(i)));
                }
            }
        } catch (ExcepcaoPersistencia ex) {
            throw new FenixServiceException(ex);
        }

        return result;
    }
}