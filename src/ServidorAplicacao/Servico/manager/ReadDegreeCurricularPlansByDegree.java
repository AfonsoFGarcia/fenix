/*
 * Created on 29/Jul/2003
 */
package ServidorAplicacao.Servico.manager;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import pt.utl.ist.berserk.logic.serviceManager.IService;
import DataBeans.util.Cloner;
import Dominio.IDegree;
import Dominio.IDegreeCurricularPlan;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;

/**
 * @author lmac1
 */

public class ReadDegreeCurricularPlansByDegree implements IService {

    /**
     * Executes the service. Returns the current collection of
     * infoDegreeCurricularPlans.
     */
    public List run(Integer idDegree) throws FenixServiceException {
        ISuportePersistente sp;
        List allDegreeCurricularPlans = null;
        try {
            sp = SuportePersistenteOJB.getInstance();
            IDegree degree = sp.getICursoPersistente().readByIdInternal(idDegree);
            allDegreeCurricularPlans = sp.getIPersistentDegreeCurricularPlan().readByDegree(degree);
        } catch (ExcepcaoPersistencia excepcaoPersistencia) {
            throw new FenixServiceException(excepcaoPersistencia);
        }

        if (allDegreeCurricularPlans == null || allDegreeCurricularPlans.isEmpty())
            return allDegreeCurricularPlans;

        // build the result of this service
        Iterator iterator = allDegreeCurricularPlans.iterator();
        List result = new ArrayList(allDegreeCurricularPlans.size());

        while (iterator.hasNext()) {
            IDegreeCurricularPlan degreeCurricularPlan = (IDegreeCurricularPlan) iterator.next();
            result.add(Cloner.copyIDegreeCurricularPlan2InfoDegreeCurricularPlan(degreeCurricularPlan));
        }
        return result;
    }
}