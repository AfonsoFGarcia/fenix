package net.sourceforge.fenixedu.persistenceTier.OJB.transactions;

import net.sourceforge.fenixedu.domain.reimbursementGuide.IReimbursementGuideEntry;
import net.sourceforge.fenixedu.domain.transactions.IReimbursementTransaction;
import net.sourceforge.fenixedu.domain.transactions.ReimbursementTransaction;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.OJB.ObjectFenixOJB;
import net.sourceforge.fenixedu.persistenceTier.transactions.IPersistentReimbursementTransaction;

import org.apache.ojb.broker.query.Criteria;

/**
 * 
 * @author <a href="mailto:sana@ist.utl.pt">Shezad Anavarali </a>
 * @author <a href="mailto:naat@ist.utl.pt">Nadir Tarmahomed </a>
 *  
 */
public class ReimbursementTransactionOJB extends ObjectFenixOJB implements
        IPersistentReimbursementTransaction {

    public ReimbursementTransactionOJB() {
    }

    public IReimbursementTransaction readByReimbursementGuideEntry(
            IReimbursementGuideEntry reimbursementGuideEntry) throws ExcepcaoPersistencia {

        Criteria criteria = new Criteria();
        criteria.addEqualTo("reimbursementGuideEntry.idInternal", reimbursementGuideEntry
                .getIdInternal());

        return (IReimbursementTransaction) queryObject(ReimbursementTransaction.class, criteria);

    }

}