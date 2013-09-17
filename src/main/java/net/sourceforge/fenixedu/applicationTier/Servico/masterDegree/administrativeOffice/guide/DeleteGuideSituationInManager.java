package net.sourceforge.fenixedu.applicationTier.Servico.masterDegree.administrativeOffice.guide;

import net.sourceforge.fenixedu.domain.GuideSituation;
import pt.ist.fenixWebFramework.security.accessControl.Checked;
import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.FenixFramework;

/**
 * 
 * @author <a href="mailto:shezad@ist.utl.pt">Shezad Anavarali </a>
 * 
 */
public class DeleteGuideSituationInManager {

    @Checked("RolePredicates.MANAGER_PREDICATE")
    @Atomic
    public static void run(String guideSituationID) {
        GuideSituation guideSituation = FenixFramework.getDomainObject(guideSituationID);
        guideSituation.delete();
    }

}