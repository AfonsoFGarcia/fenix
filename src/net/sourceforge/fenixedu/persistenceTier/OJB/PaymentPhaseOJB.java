/*
 * Created on 6/Jan/2004
 *  
 */
package net.sourceforge.fenixedu.persistenceTier.OJB;

import java.util.List;
import java.util.ListIterator;

import net.sourceforge.fenixedu.domain.IGratuityValues;
import net.sourceforge.fenixedu.domain.PaymentPhase;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentPaymentPhase;

import org.apache.ojb.broker.query.Criteria;

/**
 * @author T�nia Pous�o
 *  
 */
public class PaymentPhaseOJB extends PersistentObjectOJB implements IPersistentPaymentPhase {
    public void deletePaymentPhasesOfThisGratuity(Integer gratuityValuesID) throws ExcepcaoPersistencia {

        Criteria crit = new Criteria();
        crit.addEqualTo("gratuityValues.idInternal", gratuityValuesID);

        List result = queryList(PaymentPhase.class, crit);
        if (result != null) {

            ListIterator iterator = result.listIterator();
            while (iterator.hasNext()) {

                delete(iterator.next());
            }
        }

    }

    public List readByGratuityValues(IGratuityValues gratuityValues) throws ExcepcaoPersistencia {
        Criteria criteria = new Criteria();
        criteria.addEqualTo("gratuityValues.idInternal", gratuityValues.getIdInternal());

        return queryList(PaymentPhase.class, criteria);
    }

}