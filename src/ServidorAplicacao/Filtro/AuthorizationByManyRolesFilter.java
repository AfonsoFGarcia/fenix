/*
 * Created on 6/Fev/2004
 *  
 */
package ServidorAplicacao.Filtro;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;

import pt.utl.ist.berserk.ServiceRequest;
import pt.utl.ist.berserk.ServiceResponse;
import DataBeans.InfoRole;
import ServidorAplicacao.IUserView;
import ServidorAplicacao.Filtro.exception.NotAuthorizedFilterException;

/**
 * @author T�nia Pous�o
 *  
 */
public abstract class AuthorizationByManyRolesFilter extends Filtro
{

	/**
	 * @param collection
	 * @return boolean
	 */
	private boolean containsRole(Collection roles)
	{
		CollectionUtils.intersection(roles, getNeededRoles());

		if (roles.size() != 0)
		{
			return true;
		}
		else
		{
			return false;
		}
	}
		
	/**
	 * @return The Needed Roles to Execute The Service
	 */
	abstract protected Collection getNeededRoles();

	/**
	 * @return The Needed Roles to Execute The Service
	 * but with InfoObjects
	 */
	protected List getRoleList(List roles)
	{
		List result = new ArrayList();
		Iterator iterator = roles.iterator();
		while (iterator.hasNext())
		{
			result.add(((InfoRole) iterator.next()).getRoleType());
		}
		
		return result;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see pt.utl.ist.berserk.logic.filterManager.IFilter#execute(pt.utl.ist.berserk.ServiceRequest,
	 *      pt.utl.ist.berserk.ServiceResponse)
	 */
	public void execute(ServiceRequest request, ServiceResponse response)
	throws Exception
	{
		IUserView id = (IUserView) request.getRequester();
		if ((id != null && id.getRoles() != null && !containsRole(id.getRoles()))
				|| (id != null && id.getRoles() != null && !hasProvilege(id, request.getArguments()))
				|| (id == null)
				|| (id.getRoles() == null))
		{
			throw new NotAuthorizedFilterException();
		}

	}
	
	/**
	 * @param id
	 * @param argumentos
	 * @return
	 */
	abstract protected boolean hasProvilege(IUserView id, Object[] arguments);	
}
