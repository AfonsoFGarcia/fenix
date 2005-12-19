/*
 * Created on 23/Jul/2003
 *
 * 
 */
package net.sourceforge.fenixedu.applicationTier.Servico.scientificCouncil;

import net.sourceforge.fenixedu.applicationTier.Factory.ScientificCouncilComponentBuilder;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.ISiteComponent;
import net.sourceforge.fenixedu.dataTransferObject.SiteView;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import pt.utl.ist.berserk.logic.serviceManager.IService;

/**
 * @author Jo�o Mota
 * 
 * 23/Jul/2003 fenix-head ServidorAplicacao.Servico.scientificCouncil
 *  
 */
public class ScientificCouncilComponentService implements IService {

    public SiteView run(ISiteComponent bodyComponent, Integer degreeId, Integer curricularYear,
            Integer degreeCurricularPlanId) throws FenixServiceException, ExcepcaoPersistencia {

        ScientificCouncilComponentBuilder componentBuilder = ScientificCouncilComponentBuilder
                .getInstance();
        bodyComponent = componentBuilder.getComponent(bodyComponent, degreeId, curricularYear,
                degreeCurricularPlanId);
        SiteView siteView = new SiteView();
        siteView.setComponent(bodyComponent);

        return siteView;
    }

}