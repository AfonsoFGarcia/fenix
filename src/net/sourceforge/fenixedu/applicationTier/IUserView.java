package net.sourceforge.fenixedu.applicationTier;

import java.util.Collection;

import net.sourceforge.fenixedu.domain.person.RoleType;

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