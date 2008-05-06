/*
 * Created on Jun 28, 2005
 *  by jdnf and mrsp
 */
package net.sourceforge.fenixedu.domain;

import junit.framework.TestCase;
import net.sourceforge.fenixedu._development.MetadataManager;
import pt.ist.fenixframework.pstm.Transaction;

public class DomainTestBase extends TestCase {

    protected void setUp() throws Exception {
        super.setUp();    
        
        MetadataManager.init("domain_model.dml");
        
        try {
        	Transaction.abort();
        	Transaction.commit();
        }catch (Exception e) {
			
		}               
        Transaction.begin();        
        DomainObject.turnOffLockMode();
        RootDomainObject.initTests();
    }

    protected void tearDown() throws Exception {
	Transaction.abort();
        super.tearDown();
    }


    protected void sleep(long time) {
        try {
            Thread.sleep(time);
        } catch (InterruptedException e) {
        }
    }

}
