package net.sourceforge.fenixedu.stm;

import net.sourceforge.fenixedu.domain.DomainObject;

import org.apache.ojb.broker.Identity;
import org.apache.ojb.broker.PersistenceBroker;
import org.apache.ojb.broker.PersistenceBrokerFactory;
import org.apache.ojb.broker.accesslayer.LookupException;
import org.apache.ojb.broker.metadata.ClassDescriptor;

import java.sql.Connection;
import java.sql.Statement;
import java.sql.ResultSet;
import java.sql.SQLException;

import java.net.InetAddress;
import java.net.UnknownHostException;

import java.util.List;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.ConcurrentLinkedQueue;



class TransactionChangeLogs {

    private static class OIDInfo {
	final Class realClass;
	final Class topLevelClass;
	
	OIDInfo(Class realClass, Class topLevelClass) {
	    this.realClass = realClass;
	    this.topLevelClass = topLevelClass;
	}
    }

    private static HashMap<String,OIDInfo> CLASS_OIDS = new HashMap<String,OIDInfo>();

    static Identity getObjectIdentity(PersistenceBroker pb, String className, int idInternal) {
	OIDInfo info = CLASS_OIDS.get(className);
	if (info == null) {
	    try {
		Class realClass = Class.forName(className);
		ClassDescriptor cld = pb.getClassDescriptor(realClass);
		Class topLevelClass = pb.getTopLevelClass(realClass);
		info = new OIDInfo(realClass, topLevelClass);
		CLASS_OIDS.put(className, info);
	    } catch (ClassNotFoundException cnfe) {
		throw new Error("Couldn't find class " + className + ": " + cnfe);
	    }
	}

	return new Identity(info.realClass, info.topLevelClass, new Object[] { new Integer(idInternal) });
    }



    // ------------------------------------------------------------

    private static class ChangeLog {
	final Identity oid;
	final String attr;

	ChangeLog(Identity oid, String attr) {
	    this.oid = oid;
	    this.attr = attr;
	}
    }

    private static class ChangeLogSet {
	final int txNumber;
	final List<ChangeLog> changeLogs;

	ChangeLogSet(int txNumber) {
	    this.txNumber = txNumber;
	    this.changeLogs = new ArrayList<ChangeLog>();
	}
    }

    private static ConcurrentLinkedQueue<ChangeLogSet> CHANGE_LOGS = new ConcurrentLinkedQueue<ChangeLogSet>();

    public static void cleanOldLogs(int txNumber) {
	while ((! CHANGE_LOGS.isEmpty()) && (CHANGE_LOGS.peek().txNumber <= txNumber)) {
	    CHANGE_LOGS.poll();
	}
    }

    private static boolean registerChangeLogSet(PersistenceBroker pb, ChangeLogSet clSet) {
	CHANGE_LOGS.add(clSet);

	// invalidate cache
	boolean updatedCache = false;

	for (ChangeLog log : clSet.changeLogs) {
	    DomainObject existingObject = (DomainObject)pb.serviceObjectCache().lookup(log.oid);
	    if (existingObject != null) {
		updatedCache = true;
		existingObject.invalidate(log.attr, clSet.txNumber);
	    }
	}

	return updatedCache;
    }

    public static synchronized boolean updateFromTxLogsOnDatabase(PersistenceBroker pb) throws SQLException,LookupException {
	Connection conn = pb.serviceConnectionManager().getConnection();
    conn.commit();
	Statement stmt = conn.createStatement();

	// read tx logs
	int maxTxNumber = Transaction.getCommitted();
	ResultSet rs = stmt.executeQuery("SELECT OBJ_CLASS,OBJ_ID,OBJ_ATTR,TX_NUMBER FROM TX_CHANGE_LOGS WHERE TX_NUMBER > " 
					 + maxTxNumber 
					 + " ORDER BY TX_NUMBER");

	int previousTxNum = -1;
	ChangeLogSet clSet = null;
	boolean updatedCache = false;

	while (rs.next()) {
	    int txNum = rs.getInt(4);

	    if (txNum != previousTxNum) {
		if (clSet != null) {
		    updatedCache |= registerChangeLogSet(pb, clSet);
		}
		maxTxNumber = Math.max(maxTxNumber, txNum);
		clSet = new ChangeLogSet(txNum);
		previousTxNum = txNum;
	    }

	    String className = rs.getString(1);
	    int idInternal = rs.getInt(2);
	    String attr = rs.getString(3);

	    clSet.changeLogs.add(new ChangeLog(getObjectIdentity(pb, className, idInternal), attr));
	}

	// add last built ChangeLogSet, if any
	if (clSet != null) {
	    updatedCache |= registerChangeLogSet(pb, clSet);
	}

	Transaction.setCommitted(maxTxNumber);

	return updatedCache;
    }

    public static int findNewerVersionFor(Object obj, String attr, int txNumber) {
	Class objClass = obj.getClass();
	for (ChangeLogSet clSet : CHANGE_LOGS) {
	    if (clSet.txNumber > txNumber) {
		for (ChangeLog log : clSet.changeLogs) {
		    if ((log.oid.getObjectsRealClass() == objClass) && log.attr.equals(attr)) {
			return clSet.txNumber;
		    }
		}
	    }
	}
	return txNumber;
    }

    public static int initializeTransactionSystem() {
	// find the last committed transaction
	PersistenceBroker broker = null;

	try {
	    broker = PersistenceBrokerFactory.defaultPersistenceBroker();
	    broker.beginTransaction();

	    Connection conn = broker.serviceConnectionManager().getConnection();
	    Statement stmt = conn.createStatement();
	    ResultSet rs = stmt.executeQuery("SELECT MAX(TX_NUMBER) FROM TX_CHANGE_LOGS");
	    int maxTx = (rs.next() ? rs.getInt(1) : -1);

	    broker.commitTransaction();

	    new CleanThread().start();

	    return maxTx;
	} catch (Exception e) {
	    throw new Error("Couldn't initialize the transaction system");
	} finally {
	    if (broker != null) {
		broker.close();
	    }
	}
    }


    private static class CleanThread extends Thread {
	private static final long SECONDS_BETWEEN_UPDATES = 120;

	private String server;
	private int lastTxNumber = -1;
	
	CleanThread() {
	    try {
		this.server = InetAddress.getLocalHost().getHostAddress();
	    } catch (UnknownHostException uhe) {
		throw new Error("Couldn't get this host address, which is needed to register in the database");
	    }

	    setDaemon(true);
	}
	
	public void run() {
	    while (lastTxNumber == -1) {
		initializeServerRecord();
		try {
		    sleep(SECONDS_BETWEEN_UPDATES * 1000);
		} catch (InterruptedException ie) {
		    // ignore it
		}
	    }
	    
	    while (true) {
		updateServerRecord();
		try {
		    sleep(SECONDS_BETWEEN_UPDATES * 1000);
		} catch (InterruptedException ie) {
		    // ignore it
		}
	    }
	}
	
	private void initializeServerRecord() {
	    int currentTxNumber = Transaction.getCommitted();

	    PersistenceBroker broker = null;
	    
	    try {
		broker = PersistenceBrokerFactory.defaultPersistenceBroker();
		broker.beginTransaction();
		
		Connection conn = broker.serviceConnectionManager().getConnection();
		Statement stmt = conn.createStatement();

		// delete previous record for this server and insert a new one
		stmt.executeUpdate("DELETE FROM LAST_TX_PROCESSED WHERE SERVER = '" + server + "'");
		stmt.executeUpdate("INSERT INTO LAST_TX_PROCESSED VALUES ('" + server + "'," + currentTxNumber + ",null)");
		
		broker.commitTransaction();

		this.lastTxNumber = currentTxNumber;
	    } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Couldn't initialize the clean thread");
            //throw new Error("Couldn't initialize the clean thread");
	    } finally {
		if (broker != null) {
		    broker.close();
		}
	    }
	}
	
	private void updateServerRecord() {
	    int currentTxNumber = Transaction.getCommitted();

	    PersistenceBroker broker = null;
	    
	    try {
		broker = PersistenceBrokerFactory.defaultPersistenceBroker();
		broker.beginTransaction();
		
		Connection conn = broker.serviceConnectionManager().getConnection();
		Statement stmt = conn.createStatement();

		// update record for this server
		stmt.executeUpdate("UPDATE LAST_TX_PROCESSED SET LAST_TX=" 
				   + currentTxNumber 
				   + ",LAST_UPDATE=NULL WHERE SERVER = '" 
				   + server + "'");
		
		// delete obsolete values
		ResultSet rs = stmt.executeQuery("SELECT MIN(LAST_TX) FROM LAST_TX_PROCESSED WHERE LAST_UPDATE > NOW() - " 
						 + (2 * SECONDS_BETWEEN_UPDATES));
		int min = (rs.next() ? rs.getInt(1) : 0);
		if (min > 0) {
		    stmt.executeUpdate("DELETE FROM TX_CHANGE_LOGS WHERE TX_NUMBER < " + min);
		}

		broker.commitTransaction();

		this.lastTxNumber = currentTxNumber;
	    } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Couldn't update database in the clean thread");
            //throw new Error("Couldn't update database in the clean thread");
	    } finally {
		if (broker != null) {
		    broker.close();
		}
	    }
	}
    }
}
