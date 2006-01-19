/*
 * Created on 1/Ago/2003
 */
package net.sourceforge.fenixedu.applicationTier.Servico.manager;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NonExistingServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoDegreeCurricularPlan;
import net.sourceforge.fenixedu.dataTransferObject.InfoDegreeCurricularPlanWithDegree;
import net.sourceforge.fenixedu.domain.DegreeCurricularPlan;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;
import net.sourceforge.fenixedu.applicationTier.IService;

/**
 * @author lmac1
 */

public class ReadDegreeCurricularPlan implements IService {

    /**
     * The constructor of this class.
     */
    public ReadDegreeCurricularPlan() {
    }

    /**
     * Executes the service. Returns the current InfoDegreeCurricularPlan.
     * 
     * @throws ExcepcaoPersistencia
     */
    public InfoDegreeCurricularPlan run(final Integer idInternal) throws FenixServiceException,
            ExcepcaoPersistencia {

        final ISuportePersistente sp = PersistenceSupportFactory.getDefaultPersistenceSupport();
		final DegreeCurricularPlan degreeCurricularPlan = (DegreeCurricularPlan) sp.getIPersistentDegreeCurricularPlan()
                .readByOID(DegreeCurricularPlan.class, idInternal);

        if (degreeCurricularPlan == null) {
            throw new NonExistingServiceException();
        }

        return InfoDegreeCurricularPlanWithDegree.newInfoFromDomain(degreeCurricularPlan);
    }
}