/*
 * Created on Apr 28, 2005
 *
 */
package net.sourceforge.fenixedu.persistenceTier.OJB.teacher.professorship;

import java.util.List;

import net.sourceforge.fenixedu.domain.Institution;
import net.sourceforge.fenixedu.domain.NonAffiliatedTeacher;
import net.sourceforge.fenixedu.domain.NonAffiliatedTeacher;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.OJB.PersistentObjectOJB;
import net.sourceforge.fenixedu.persistenceTier.teacher.professorship.IPersistentNonAffiliatedTeacher;

import org.apache.ojb.broker.query.Criteria;

/**
 * @author Ricardo Rodrigues
 *
 */

public class NonAffiliatedTeacherOJB extends PersistentObjectOJB implements IPersistentNonAffiliatedTeacher {

    public NonAffiliatedTeacherOJB() {
    }

    public List readByName(String name) throws ExcepcaoPersistencia{
        Criteria criteria = new Criteria();
        criteria.addLike("name", name);
        return queryList(NonAffiliatedTeacher.class, criteria);
    }
    
    public NonAffiliatedTeacher readByNameAndInstitution(String name, Institution institution) throws ExcepcaoPersistencia{
        Criteria criteria = new Criteria();
        criteria.addEqualTo("name", name);
        criteria.addEqualTo("institution.idInternal",institution.getIdInternal());
        return (NonAffiliatedTeacher) queryObject(NonAffiliatedTeacher.class, criteria);
    }
    
    public List readAll() throws ExcepcaoPersistencia {
        Criteria criteria = new Criteria();
        return queryList(NonAffiliatedTeacher.class, criteria);
    }
    
}
