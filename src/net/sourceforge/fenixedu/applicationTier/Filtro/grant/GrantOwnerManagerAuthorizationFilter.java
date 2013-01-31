/*
 * Created on 30/Oct/2003
 *  
 */
package net.sourceforge.fenixedu.applicationTier.Filtro.grant;

import net.sourceforge.fenixedu.applicationTier.Filtro.AuthorizationByRoleFilter;
import net.sourceforge.fenixedu.domain.person.RoleType;

/**
 * @author Barbosa
 * @author Pica
 */
public class GrantOwnerManagerAuthorizationFilter extends AuthorizationByRoleFilter {

	public GrantOwnerManagerAuthorizationFilter() {
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see ServidorAplicacao.Filtro.AuthorizationByRoleFilter#getRoleType()
	 */
	@Override
	protected RoleType getRoleType() {
		return RoleType.GRANT_OWNER_MANAGER;
	}

}