/*
 * Created on 6/Fev/2005
 */
package net.sourceforge.fenixedu.applicationTier.Filtro;

import net.sourceforge.fenixedu.domain.person.RoleType;

/**
 * @author T�nia Pous�o
 *
 */
public class ManagementAssiduousnessAuthorizationFilter extends AuthorizationByRoleFilter {
    /*
     * (non-Javadoc)
     * 
     * @see ServidorAplicacao.Filtro.AuthorizationByRoleFilter#getRoleType()
     */
    protected RoleType getRoleType() {
        return RoleType.MANAGEMENT_ASSIDUOUSNESS;
    }
}