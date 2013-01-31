package net.sourceforge.fenixedu.applicationTier.utils;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.SortedSet;
import java.util.TreeSet;

import net.sourceforge.fenixedu.applicationTier.ICandidateView;
import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.Role;
import net.sourceforge.fenixedu.domain.person.RoleType;

import org.joda.time.DateTime;

public class MockUserView implements IUserView {

	final private Person person;

	final private String username;

	final private Collection<RoleType> roleTypes = new HashSet<RoleType>();

	private transient Collection<Role> roles;

	private final DateTime userCreationDateTime = new DateTime();

	public MockUserView(final String username, final Collection<Role> roles, final Person person) {
		this.person = person;
		this.username = username;
		this.roles = roles;
		for (final Role role : roles) {
			roleTypes.add(role.getRoleType());
		}
	}

	@Override
	public String getUtilizador() {
		return username;
	}

	@Override
	public String getFullName() {
		return null;
	}

	public ICandidateView getCandidateView() {
		return null;
	}

	@Override
	public boolean hasRoleType(RoleType roleType) {
		return false;
	}

	public void setCandidateView(ICandidateView candidateView) {
	}

	@Override
	public Person getPerson() {
		return person;
	}

	@Override
	public Collection<RoleType> getRoleTypes() {
		return roleTypes;
	}

	public Collection<Role> getRoles() {
		if (roles != null) {
			final SortedSet<Role> roles = new TreeSet<Role>();
			for (final RoleType roleType : roleTypes) {
				roles.add(Role.getRoleByRoleType(roleType));
			}
			this.roles = Collections.unmodifiableSortedSet(roles);
		}
		return roles;
	}

	@Override
	public DateTime getExpirationDate() {
		return null;
	}

	@Override
	public String getPrivateConstantForDigestCalculation() {
		return null;
	}

	@Override
	public String getUsername() {
		return getUtilizador();
	}

	@Override
	public boolean hasRole(String role) {
		return false;
	}

	@Override
	public DateTime getLastLogoutDateTime() {
		return null;
	}

	@Override
	public DateTime getUserCreationDateTime() {
		return userCreationDateTime;
	}

}
