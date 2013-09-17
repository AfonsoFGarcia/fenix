/*
 * ApagarTurma.java
 *
 * Created on 27 de Outubro de 2002, 18:13
 */

package net.sourceforge.fenixedu.applicationTier.Servico.resourceAllocationManager;

import net.sourceforge.fenixedu.dataTransferObject.InfoClass;
import net.sourceforge.fenixedu.domain.SchoolClass;
import pt.ist.fenixWebFramework.security.accessControl.Checked;
import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.FenixFramework;

public class ApagarTurma {

    @Checked("RolePredicates.RESOURCE_ALLOCATION_MANAGER_PREDICATE")
    @Atomic
    public static Boolean run(InfoClass infoClass) {
        FenixFramework.<SchoolClass> getDomainObject(infoClass.getExternalId()).delete();
        return Boolean.TRUE;
    }

}