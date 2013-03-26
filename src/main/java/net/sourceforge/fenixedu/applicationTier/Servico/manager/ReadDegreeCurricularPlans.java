/*
 * Created on 4/Set/2003, 13:55:41
 *
 *By Goncalo Luiz gedl [AT] rnl [DOT] ist [DOT] utl [DOT] pt
 */
package net.sourceforge.fenixedu.applicationTier.Servico.manager;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.FenixService;
import net.sourceforge.fenixedu.dataTransferObject.InfoDegreeCurricularPlan;
import net.sourceforge.fenixedu.domain.DegreeCurricularPlan;
import net.sourceforge.fenixedu.domain.degreeStructure.CurricularStage;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import pt.ist.fenixWebFramework.services.Service;

/**
 * @author Goncalo Luiz gedl [AT] rnl [DOT] ist [DOT] utl [DOT] pt
 * 
 * 
 *         Created at 4/Set/2003, 13:55:41
 * 
 */
public class ReadDegreeCurricularPlans extends FenixService {

    /**
     * Executes the service. Returns the current InfoDegreeCurricularPlan.
     * 
     * @throws ExcepcaoPersistencia
     */
    @Service
    public static List run() {
        final List curricularPlans = DegreeCurricularPlan.readByCurricularStage(CurricularStage.OLD);
        final List infoCurricularPlans = new ArrayList(curricularPlans.size());

        for (final Iterator iter = curricularPlans.iterator(); iter.hasNext();) {
            final DegreeCurricularPlan curricularPlan = (DegreeCurricularPlan) iter.next();
            infoCurricularPlans.add(InfoDegreeCurricularPlan.newInfoFromDomain(curricularPlan));
        }
        return infoCurricularPlans;
    }
}