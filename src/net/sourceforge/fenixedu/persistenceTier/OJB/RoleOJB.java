package net.sourceforge.fenixedu.persistenceTier.OJB;

import java.util.List;

import net.sourceforge.fenixedu.domain.Role;
import net.sourceforge.fenixedu.domain.person.RoleType;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentRole;

import org.apache.ojb.broker.query.Criteria;

/**
 * 
 * @author Nuno Nunes (nmsn@rnl.ist.utl.pt) Joana Mota (jccm@rnl.ist.utl.pt)
 */
public class RoleOJB extends PersistentObjectOJB implements IPersistentRole {

    public RoleOJB() {
        super();
    }

    public Role readByRoleType(RoleType roleType) throws ExcepcaoPersistencia {
        Criteria crit = new Criteria();
        crit.addEqualTo("roleType", roleType);
        return (Role) queryObject(Role.class, crit);

    }

    public List readAll() throws ExcepcaoPersistencia {
        return queryList(Role.class, null);
    }

}