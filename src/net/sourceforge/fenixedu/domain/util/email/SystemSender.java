package net.sourceforge.fenixedu.domain.util.email;

import net.sourceforge.fenixedu.domain.Role;
import net.sourceforge.fenixedu.domain.accessControl.RoleGroup;
import net.sourceforge.fenixedu.domain.person.RoleType;

public class SystemSender extends SystemSender_Base {

    public SystemSender() {
	super();
	setMembers(new RoleGroup(Role.getRoleByRoleType(RoleType.MANAGER)));
	setFromAddress("noreply@ist.utl.pt");
	setFromName("Sistema F�nix");
	setSystemRootDomainObject(getRootDomainObject());
    }
}
