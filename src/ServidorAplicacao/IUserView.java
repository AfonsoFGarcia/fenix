package ServidorAplicacao;

import java.util.Collection;

import Util.RoleType;

/**
 * @author jorge
 */
public interface IUserView {
    /* Acrecentado por Fernanda Quit�rio & Tania Pous�o */
    String getUtilizador();

    public String getFullName();

    Collection getRoles();

    ICandidateView getCandidateView();

    boolean hasRoleType(RoleType roleType);
}