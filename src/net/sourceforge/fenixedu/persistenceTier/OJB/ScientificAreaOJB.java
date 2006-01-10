/*
 * Created on 18/Dez/2003
 *
 */
package net.sourceforge.fenixedu.persistenceTier.OJB;

import net.sourceforge.fenixedu.domain.ScientificArea;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentScientificArea;

import org.apache.ojb.broker.query.Criteria;

/**
 * @author Nuno Correia
 * @author Ricardo Rodrigues
 */
public class ScientificAreaOJB extends PersistentObjectOJB implements IPersistentScientificArea {
    public ScientificAreaOJB() {
    }

    public ScientificArea readByName(String name) throws ExcepcaoPersistencia {
        Criteria criteria = new Criteria();
        criteria.addEqualTo("name", name);
        return (ScientificArea) queryObject(ScientificArea.class, criteria);
    }
}