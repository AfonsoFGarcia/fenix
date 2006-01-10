/*
 * Created on 15/Dez/2004
 */
package net.sourceforge.fenixedu.persistenceTier.OJB.managementAssiduousness;

import java.util.Date;

import net.sourceforge.fenixedu.domain.managementAssiduousness.ExtraWork;
import net.sourceforge.fenixedu.persistenceTier.OJB.PersistentObjectOJB;
import net.sourceforge.fenixedu.persistenceTier.managementAssiduousness.IPersistentExtraWork;

import org.apache.ojb.broker.query.Criteria;

/**
 * @author T�nia Pous�o
 *
 */
public class ExtraWorkOJB extends PersistentObjectOJB implements IPersistentExtraWork{

    public ExtraWorkOJB() {
        super();
    }

    public ExtraWork readExtraWorkByDay(Date day) throws Exception {
        Criteria criteria = new Criteria(); 
        criteria.addEqualTo("day", day);
        
        return (ExtraWork) queryObject(ExtraWork.class, criteria);
    }    
}