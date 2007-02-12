package net.sourceforge.fenixedu.stm;

import net.sourceforge.fenixedu.domain.DomainObject;
import net.sourceforge.fenixedu.persistenceTier.cache.FenixCache;

import org.apache.ojb.broker.PersistenceBroker;

public abstract class Transaction extends jvstm.Transaction {

    public final static TransactionStatistics STATISTICS = new TransactionStatistics();

    static {
        new StatisticsThread().start();
    }

    private static final FenixCache cache = new FenixCache();
    private static boolean initialized = false;

    static {
	jvstm.Transaction.setTransactionFactory(new jvstm.TransactionFactory() {
		public jvstm.Transaction makeTopLevelTransaction(int txNumber) {
		    return new TopLevelTransaction(txNumber);
		}

                public jvstm.Transaction makeReadOnlyTopLevelTransaction(int txNumber) {
		    return new ReadOnlyTopLevelTransaction(txNumber);
                }
	    });
    }


    private Transaction() {
 	// this is never to be used!!!
 	super(0);
    }

    public static void setScriptMode() {
        // empty now...
        // but it should be changed later to disable the reporting in the JVSTM
    }

    static synchronized void initializeIfNeeded() {
	if (! initialized) {
	    int maxTx = TransactionChangeLogs.initializeTransactionSystem();
	    if (maxTx >= 0) {
		System.out.println("Setting the last committed TX number to " + maxTx);
		setCommitted(maxTx);
	    } else {
		throw new Error("Couldn't determine the last transaction number");
	    }
	    initialized = true;
	}
    }


    private static final ThreadLocal<Boolean> DEFAULT_READ_ONLY = new ThreadLocal<Boolean>() {
         protected Boolean initialValue() {
             return Boolean.FALSE;
         }
    };

    public static boolean getDefaultReadOnly() {
	return DEFAULT_READ_ONLY.get().booleanValue();
    }

    public static void setDefaultReadOnly(boolean readOnly) {
	DEFAULT_READ_ONLY.set(readOnly ? Boolean.TRUE : Boolean.FALSE);
    }


    public static jvstm.Transaction begin() {
        return Transaction.begin(getDefaultReadOnly());
    }

    public static jvstm.Transaction begin(boolean readOnly) {
	initializeIfNeeded();	
	return jvstm.Transaction.begin(readOnly);
    }

    public static void forceFinish() {
	if (current() != null) {
	    try {
		commit();
	    } catch (Throwable t) {
		System.out.println("Aborting from Transaction.forceFinish(). If being called from CloseTransactionFilter it will leave an open transaction.");
		abort();
	    }
	}
    }

    public static void abort() {
        STATISTICS.incAborts();

        jvstm.Transaction.abort();
        Transaction.begin();
        Transaction.currentFenixTransaction().setReadOnly();
    }
    
    public static FenixTransaction currentFenixTransaction() {
	return (FenixTransaction)current();
    }

    protected static DBChanges currentDBChanges() {
	return currentFenixTransaction().getDBChanges();
    }

    public static void logAttrChange(DomainObject obj, String attrName) {
	currentDBChanges().logAttrChange(obj, attrName);
    }

    public static void storeNewObject(DomainObject obj) {
	currentDBChanges().storeNewObject(obj);
    }

    public static void storeObject(DomainObject obj, String attrName) {
	currentDBChanges().storeObject(obj, attrName);
    }

    public static void deleteObject(Object obj) {
	currentDBChanges().deleteObject(obj);
    }

    public static void addRelationTuple(String relation, Object obj1, String colNameOnObj1, Object obj2, String colNameOnObj2) {
	currentDBChanges().addRelationTuple(relation, obj1, colNameOnObj1, obj2, colNameOnObj2);
    }

    public static void removeRelationTuple(String relation, Object obj1, String colNameOnObj1, Object obj2, String colNameOnObj2) {
	currentDBChanges().removeRelationTuple(relation, obj1, colNameOnObj1, obj2, colNameOnObj2);
    }

    public static PersistenceBroker getOJBBroker() {
	return currentFenixTransaction().getOJBBroker();
    }

    public static FenixCache getCache() {
	return cache;
    }

    public static void withTransaction(jvstm.TransactionalCommand command) {
        withTransaction(false, command);
    }

    public static void withTransaction(boolean readOnly, jvstm.TransactionalCommand command) {
        while (true) {
            Transaction.begin(readOnly);
            boolean txFinished = false;
            try {
                command.doIt();
                Transaction.commit();
                txFinished = true;
                return;
            } catch (jvstm.CommitException ce) {
                System.out.println("Restarting TX because of a conflict");
                Transaction.abort();
                txFinished = true;
            } finally {
                if (! txFinished) {
                    STATISTICS.incAborts();
                    jvstm.Transaction.abort();
                }
            }
        }
    }
}
