package net.sourceforge.fenixedu.applicationTier;

import javax.ejb.CreateException;
import javax.ejb.EJBLocalHome;

/**
 * The local home interface for the service manager seassion bean. *
 * 
 * @author Jos� Pedro Pereira
 * @version
 */
public interface ServiceManagerLocalHome extends EJBLocalHome {
    public ServiceManagerLocal create() throws CreateException;
}