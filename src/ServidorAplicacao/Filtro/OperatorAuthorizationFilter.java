/*
 * Created on 19/Jul/2004 by T�nia Pous�o
 *
 */
package ServidorAplicacao.Filtro;

import Util.RoleType;

/**
 * @author T�nia Pous�o
 */
public class OperatorAuthorizationFilter extends AuthorizationByRoleFilter {

    // the singleton of this class
    public final static PersonAuthorizationFilter instance = new PersonAuthorizationFilter();

    /**
     * The singleton access method of this class.
     * 
     * @return Returns the instance of this class responsible for the
     *         authorization access to services.
     */
    public static Filtro getInstance() {
        return instance;
    }

    /*
     * (non-Javadoc)
     * 
     * @see ServidorAplicacao.Filtro.AuthorizationByRoleFilter#getRoleType()
     */
    protected RoleType getRoleType() {
        return RoleType.OPERATOR;
    }

}