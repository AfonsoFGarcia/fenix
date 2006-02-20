/*
 * Created on 29/Jan/2005
 */
package net.sourceforge.fenixedu.persistenceTier.OJB.managementAssiduousness;

import java.util.Date;
import java.util.List;

import net.sourceforge.fenixedu.domain.managementAssiduousness.ExtraWorkRequests;
import net.sourceforge.fenixedu.persistenceTier.OJB.PersistentObjectOJB;
import net.sourceforge.fenixedu.persistenceTier.managementAssiduousness.IPersistentExtraWorkRequests;

import org.apache.ojb.broker.query.Criteria;

/**
 * @author T�nia Pous�o
 *
 */
public class ExtraWorkResquestsOJB extends PersistentObjectOJB implements IPersistentExtraWorkRequests {

    public ExtraWorkRequests readExtraWorkRequestByDay(Date day, Integer employeeID) throws Exception {
        Criteria criteria = new Criteria();
        criteria.addLessOrEqualThan("beginDate", day);
        criteria.addGreaterOrEqualThan("endDate", day);
        criteria.addEqualTo("keyEmployee", employeeID);        
        
        return (ExtraWorkRequests) queryObject(ExtraWorkRequests.class, criteria);
    }

    public List readExtraWorkRequestBetweenDays(Date beginDay, Date lastDay) throws Exception {
        Criteria criteria = new Criteria();
        criteria.addBetween("beginDate", beginDay, lastDay);
        criteria.addBetween("endDate", beginDay, lastDay);
        
        return queryList(ExtraWorkRequests.class, criteria);
    }   
    
    public List readExtraWorkRequestBetweenDaysAndByCC(Date beginDay, Date lastDay, Integer costCenterId, Integer costCenterMoneyId) throws Exception {
        Criteria criteria = new Criteria();
        criteria.addBetween("beginDate", beginDay, lastDay);
        criteria.addBetween("endDate", beginDay, lastDay);
        criteria.addEqualTo("keyCostCenterExtraWork", costCenterId);
        criteria.addEqualTo("keyCostCenterMoney", costCenterMoneyId);
        
        
        return queryList(ExtraWorkRequests.class, criteria);
    }
}
