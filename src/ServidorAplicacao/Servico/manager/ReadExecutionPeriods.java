/*
 * Created on 2003/07/16
 * 
 */
package ServidorAplicacao.Servico.manager;

import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Transformer;

import pt.utl.ist.berserk.logic.serviceManager.IService;
import DataBeans.util.Cloner;
import Dominio.IExecutionPeriod;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentExecutionPeriod;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;

/**
 * @author Luis Cruz & Sara Ribeiro
 *  
 */
public class ReadExecutionPeriods implements IService {

    /**
     * Returns info list of all execution periods.
     */
    public List run() throws FenixServiceException {

        List infoExecutionPeriods = null;
        try {
            ISuportePersistente sp = SuportePersistenteOJB.getInstance();
            IPersistentExecutionPeriod persistentExecutionPeriod = sp.getIPersistentExecutionPeriod();

            List executionPeriods = persistentExecutionPeriod.readAllExecutionPeriod();

            infoExecutionPeriods = (List) CollectionUtils.collect(executionPeriods,
                    TRANSFORM_EXECUTIONPERIOD_TO_INFOEXECUTIONPERIOD);
        } catch (ExcepcaoPersistencia e) {
            throw new FenixServiceException(e);
        }

        return infoExecutionPeriods;
    }

    private Transformer TRANSFORM_EXECUTIONPERIOD_TO_INFOEXECUTIONPERIOD = new Transformer() {
        public Object transform(Object executionPeriod) {
            return Cloner.get((IExecutionPeriod) executionPeriod);
        }
    };

}