/*
 * Created on Dec 5, 2003 by jpvl
 *
 */
package net.sourceforge.fenixedu.persistenceTier.OJB.places.campus;

import java.util.List;

import net.sourceforge.fenixedu.domain.Campus;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.OJB.PersistentObjectOJB;
import net.sourceforge.fenixedu.persistenceTier.places.campus.IPersistentCampus;

import org.apache.ojb.broker.query.Criteria;

/**
 * @author jpvl
 */
public class CampusOJB extends PersistentObjectOJB implements IPersistentCampus {

    /*
     * (non-Javadoc)
     * 
     * @see ServidorPersistente.places.campus.IPersistentCampus#readAll()
     */
    public List readAll() throws ExcepcaoPersistencia {
        Criteria criteria = new Criteria();
        return queryList(Campus.class, criteria);
    }

}