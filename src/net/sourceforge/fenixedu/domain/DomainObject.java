/*
 * Created on 11/Mar/2003 by jpvl
 *  
 */
package net.sourceforge.fenixedu.domain;

import java.lang.reflect.Field;
import java.util.Comparator;

import net.sourceforge.fenixedu._development.LogLevel;
import net.sourceforge.fenixedu._development.PropertiesManager;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.injectionCode.AccessControl;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;
import net.sourceforge.fenixedu.persistenceTier.OJB.SequenceUtil;
import net.sourceforge.fenixedu.stm.Transaction;
import net.sourceforge.fenixedu.stm.VersionedSubject;

import org.apache.ojb.broker.PersistenceBroker;
import org.apache.ojb.broker.metadata.ClassDescriptor;

import pt.utl.ist.fenix.tools.util.StringAppender;

/**
 * @author jpvl
 */
public abstract class DomainObject extends DomainObject_Base implements dml.runtime.FenixDomainObject {

    static final protected Comparator<DomainObject> COMPARATOR_BY_ID = new Comparator<DomainObject>() {
        public int compare(DomainObject o1, DomainObject o2) {
	    return o1.getIdInternal().compareTo(o2.getIdInternal());
        }
    };

    private static final boolean ERROR_IF_DELETED_OBJECT_NOT_DISCONNECTED = 
		PropertiesManager.getBooleanProperty("error.if.deleted.object.not.disconnected");

    public class UnableToDetermineIdException extends DomainException {
        public UnableToDetermineIdException(Throwable cause) {
            super("unable.to.determine.idException", cause);
        }
    }

    // This variable was created so that locking of domain objects can be
    // disabled for writting test cases. Testing domain code can be done
    // without persisting anything.
    private static boolean lockMode = true;

    // This variable was created so that for a predetermined set of data
    // the next generated ID's will always be the same. This is usefull 
    // for example for acceptance tests where the same id's must always
    // be generated.
    private static boolean autoDetermineId = false;

    private static int nextIdInternal =  1; 

    public static void turnOffLockMode() {
        lockMode = false;
    }

    public static void turnOnLockMode() {
        lockMode = true;
    }

    public static int autoDetermineId() {
        autoDetermineId = true;
        return nextIdInternal = SequenceUtil.findMaxID() + 1;
    }

    public static void stopAutoDetermineId() {
        autoDetermineId = false;
    }

    private Integer idInternal;

    public DomainObject() {
        super();
        // All domain objects become persistent upon there creation.
        // Ensure that this object gets an idInternal
        // The idInternal will be used by both the hashcode and the equals methods
        // jcachopo: This should be changed in the future...
        ensureIdInternal();
        Transaction.storeNewObject(this);
    }

    public Integer getIdInternal() {
        return idInternal;
    }
    
    public Integer get$idInternal() {
        return idInternal;
    }
    
    public void setIdInternal(Integer idInternal) {
        if ((this.idInternal != null) && (! this.idInternal.equals(idInternal))) {
            System.out.println("!!!!!! WARNING !!!!!! Changing the idInternal of an object: " + this);
            //throw new Error("Cannot change the idInternal of an object");
        }
        this.idInternal = idInternal;
    }
    
    public void set$idInternal(Integer idInternal) {
        setIdInternal(idInternal);
    }


    private void ensureIdInternal() {
        if (!lockMode || autoDetermineId) {
            setIdInternal(Integer.valueOf(nextIdInternal++));
	} else {
	    try {
		PersistenceBroker broker = Transaction.getOJBBroker();
		ClassDescriptor cld = broker.getClassDescriptor(this.getClass());
		Integer id = (Integer)broker.serviceSequenceManager().getUniqueValue(cld.getFieldDescriptorByName("idInternal"));
		setIdInternal(id);
	    } catch (Exception e) {
	        if (LogLevel.WARN) {
	            System.out.println("Something went wrong when initializing the idInternal.  Not setting it...");
	        }
                throw new UnableToDetermineIdException(e);
	    }
	}
    }

    public final int hashCode() {
        return super.hashCode();
    }

    public final boolean equals(Object obj) {
        return super.equals(obj);
    }

    public long getOID() {
        return Transaction.getOIDFor(this);
    }

    public static DomainObject fromOID(long oid) {
        return Transaction.getObjectForOID(oid);
    }

    protected final void deleteDomainObject() {
        if (lockMode) {
            if (! checkDisconnected()) {
                if (ERROR_IF_DELETED_OBJECT_NOT_DISCONNECTED) {
                    throw new Error("Trying to delete a DomainObject that is still connected to other objects: " + this);
                } else {
                    System.err.println("WARNING: Deleting a DomainObject that is still connected to other objects: " + this);
                }
            }

            PersistenceSupportFactory.getDefaultPersistenceSupport()
                .getIPersistentObject().deleteByOID(this.getClass(), getIdInternal());
        }
    }

    public jvstm.VBoxBody addNewVersion(String attrName, int txNumber) {
	Class myClass = this.getClass();
	while (myClass != Object.class) {
	    try {
		Field f = myClass.getDeclaredField(attrName);
		f.setAccessible(true);
		return ((VersionedSubject)f.get(this)).addNewVersion(attrName, txNumber);
	    } catch (NoSuchFieldException nsfe) {
		myClass = myClass.getSuperclass();
	    } catch (IllegalAccessException iae) {
		throw new Error("Couldn't addNewVersion to attribute " + attrName + ": " + iae);
	    } catch (SecurityException se) {
		throw new Error("Couldn't addNewVersion to attribute " + attrName + ": " + se);
	    }
	}
        
	if (LogLevel.WARN) {
            System.out.println("!!! WARNING !!!: addNewVersion couldn't find the appropriate slot");
	}
        return null;
    }

    protected String getCurrentUser() {
    	if(AccessControl.getUserView() != null) {
    	    return AccessControl.getUserView().getUtilizador();
    	} else {
    	    return System.getProperty("user.name", "FENIX");
    	}
    }

    @Override
    public final String toString() {
	return StringAppender.append(getClass().getName(), "(", getIdInternal().toString(), ")");
    }

    public final void readFromResultSet(java.sql.ResultSet rs) throws java.sql.SQLException {
        int txNumber = Transaction.current().getNumber();
        readSlotsFromResultSet(rs, txNumber);
    }
}
