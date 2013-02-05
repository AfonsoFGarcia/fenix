/*
 * Created on 1/Ago/2003
 */
package net.sourceforge.fenixedu.applicationTier.Servico.manager;

import net.sourceforge.fenixedu.applicationTier.FenixService;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NonExistingServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoDegreeCurricularPlan;
import net.sourceforge.fenixedu.domain.DegreeCurricularPlan;

/**
 * @author lmac1
 */

public class ReadDegreeCurricularPlan extends FenixService {

    public InfoDegreeCurricularPlan run(final Integer idInternal) throws FenixServiceException {

        final DegreeCurricularPlan degreeCurricularPlan = rootDomainObject.readDegreeCurricularPlanByOID(idInternal);

        if (degreeCurricularPlan == null) {
            throw new NonExistingServiceException();
        }

        return InfoDegreeCurricularPlan.newInfoFromDomain(degreeCurricularPlan);
    }
}