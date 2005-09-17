package net.sourceforge.fenixedu.applicationTier;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectOutputStream;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Properties;

import javax.ejb.EJBException;
import javax.ejb.SessionBean;
import javax.ejb.SessionContext;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NotAuthorizedException;
import net.sourceforge.fenixedu.applicationTier.logging.ServiceExecutionLog;
import net.sourceforge.fenixedu.applicationTier.logging.SystemInfo;
import net.sourceforge.fenixedu.applicationTier.logging.UserExecutionLog;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.stm.VersionNotAvailableException;
import net.sourceforge.fenixedu.stm.ServiceInfo;
import net.sourceforge.fenixedu.stm.Transaction;

import org.apache.commons.collections.FastHashMap;
import org.apache.log4j.Logger;

import pt.utl.ist.berserk.logic.serviceManager.IServiceManager;
import pt.utl.ist.berserk.logic.serviceManager.ServiceManager;

/**
 * This class is the entry point of the system to execute a service. It receives
 * the service to execute, its arguments and an identificator of the entity that
 * wants to run the service.
 * 
 * @author Luis Cruz
 * @author Jos� Pedro Pereira
 * @version
 */
public class ServiceManagerBean implements SessionBean, IServiceManagerWrapper {

    private static final Logger logger = Logger.getLogger(ServiceManagerBean.class);

    private static boolean serviceLoggingIsOn;

    private static FastHashMap mapServicesToWatch;

    private static boolean userLoggingIsOn;

    private static FastHashMap mapUsersToWatch;

    // just for the sake of verifying serialization of objects
    private static Properties serProps = null;

    private static Boolean verifySerializable = null;

    static {

        serviceLoggingIsOn = false;
        mapServicesToWatch = new FastHashMap();
        mapServicesToWatch.setFast(true);

        userLoggingIsOn = false;
        mapUsersToWatch = new FastHashMap();
        mapUsersToWatch.setFast(true);

    }

    public ServiceManagerBean() {
        if (verifySerializable == null) {
            verifySerializable = Boolean.FALSE;
            try {
                // load the properties file from the classpath root
                InputStream inputStream = getClass().getResourceAsStream(
                        "/serialization_verifier.properties");

                if (inputStream != null) {
                    serProps = new Properties();
                    serProps.load(inputStream);
                    String propSerVerify = serProps.getProperty("verify_serializable");
                    if (propSerVerify != null) {
                        propSerVerify = propSerVerify.trim();
                    }
                    if ("true".equalsIgnoreCase(propSerVerify) || "1".equalsIgnoreCase(propSerVerify)
                            || "on".equalsIgnoreCase(propSerVerify)
                            || "yes".equalsIgnoreCase(propSerVerify)) {
                        verifySerializable = Boolean.TRUE;
                        logger.info("Serialization verification is turned on.");
                    } else {
                        logger.info("Serialization verification is turned off.");
                    }
                }
            } catch (java.io.IOException ex) {
                logger.error("Couldn't load serialization_verifier.properties file!", ex);
            }
        }
    }

    /**
     * Executes a given service.
     * 
     * @param id
     *            represents the identification of the entity that desires to
     *            run the service
     * 
     * @param service
     *            is a string containing the name of the service to execute
     * 
     * @param argumentos
     *            is a vector with the arguments of the service to execute.
     * 
     * @throws FenixServiceException
     * @throws NotAuthorizedException
     */
    public Object execute(IUserView id, String service, Object args[]) {
        return execute(id, service, "run", args);
    }

    /*
     * (non-Javadoc)
     * 
     * @see pt.utl.ist.berserk.logic.serviceManager.IServiceManagerWrapper#execute(java.lang.Object,
     *      java.lang.String, java.lang.String, java.lang.Object[])
     */
    public Object execute(IUserView id, String service, String method, Object[] args)
            throws EJBException {
        try {
            Calendar serviceStartTime = null;
            Calendar serviceEndTime = null;

            IServiceManager manager = ServiceManager.getInstance();
            if (serviceLoggingIsOn || (userLoggingIsOn && id != null)) {
                serviceStartTime = Calendar.getInstance();
            }

	    // Replace this line with the following block if conflicting transactions should restart automatically
	    //Object serviceResult = manager.execute(id, service, method, args);

	    ServiceInfo.setCurrentServiceInfo((id == null) ? null : id.getUtilizador(), service, args);

	    Object serviceResult = null;
	    while (true) {
		try {
		    serviceResult = manager.execute(id, service, method, args);
		    break;
		} catch (VersionNotAvailableException vnae) {
		    System.out.println("Restarting TX because of VersionNotAvailableException");
		    // repeat service
		} catch (jvstm.CommitException ce) {
		    System.out.println("Restarting TX because of CommitException");
		    // repeat service
		}
	    }

            if (serviceLoggingIsOn || (userLoggingIsOn && id != null)) {
                serviceEndTime = Calendar.getInstance();
            }
            if (serviceLoggingIsOn) {
                registerServiceExecutionTime(service, method, args, serviceStartTime, serviceEndTime);
            }
            if (userLoggingIsOn && id != null) {
                registerUserExecutionOfService(id, service, method, args, serviceStartTime,
                        serviceEndTime);
            }
            if (verifySerializable.booleanValue()) {
                verifyResultIsSerializable(service, method, serviceResult);
            }
            return serviceResult;
        } catch (Exception e) {
            if (e instanceof FenixServiceException || e instanceof DomainException) {
                e.printStackTrace();
                logger.warn(e);
            } else {
                e.printStackTrace();
                logger.error(e);
            }
            throw (EJBException) new EJBException(e).initCause(e);
        } catch (Throwable t) {
            t.printStackTrace();
            logger.error(t);
            throw new EJBException(t.getMessage());
        }
    }

    private void verifyResultIsSerializable(Object service, String method, Object serviceResult) {
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream(baos);
            try {
                oos.writeObject(serviceResult);
                oos.flush();
            } catch (Exception e) {
                StringBuffer stringBuffer = new StringBuffer(90);
                stringBuffer.append("Problem serializing service result for service: ");
                stringBuffer.append(service);
                stringBuffer.append('.');
                stringBuffer.append(method);
                stringBuffer.append("().");
                if (serviceResult != null) {
                    stringBuffer.append(serviceResult.getClass().getName());
                    stringBuffer.append(" is not serializable.");
                }
                logger.fatal(stringBuffer.toString(), e);
            } finally {
                if (oos != null)
                    try {
                        oos.close();
                    } catch (Exception ignored) {
                        // ignore exception
                    }
                if (baos != null)
                    try {
                        baos.close();
                    } catch (Exception ignored) {
                        // ignore exception
                    }
            }
        } catch (IOException e1) {
            logger.error("IOException while verifying service result serialization.");
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.ejb.SessionBean#ejbActivate()
     */
    public void ejbCreate() {
        // nothing to do
    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.ejb.SessionBean#ejbActivate()
     */
    public void ejbActivate() throws EJBException {
        // nothing to do
    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.ejb.SessionBean#ejbPassivate()
     */
    public void ejbPassivate() throws EJBException {
        // nothing to do
    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.ejb.SessionBean#ejbRemove()
     */
    public void ejbRemove() throws EJBException {
        // nothing to do
    }

    public void setSessionContext(SessionContext arg0) throws EJBException {
    }

    public synchronized void turnServiceLoggingOn(IUserView id) {
        serviceLoggingIsOn = true;
    }

    public synchronized void turnServiceLoggingOff(IUserView id) {
        serviceLoggingIsOn = false;
    }

    public synchronized void clearServiceLogHistory(IUserView id) {
        mapServicesToWatch.clear();
    }

    public synchronized void turnUserLoggingOn(IUserView id) {
        userLoggingIsOn = true;
    }

    public synchronized void turnUserLoggingOff(IUserView id) {
        userLoggingIsOn = false;
    }

    public synchronized void clearUserLogHistory(IUserView id) {
        mapUsersToWatch.clear();
    }

    /**
     * @param service
     * @param method
     * @param args
     * @param serviceStartTime
     * @param serviceEndTime
     */
    private void registerServiceExecutionTime(String service, String method, Object[] args,
            Calendar serviceStartTime, Calendar serviceEndTime) {
        String hashKey = generateServiceHashKey(service, method, args);
        long serviceExecutionTime = calculateServiceExecutionTime(serviceStartTime, serviceEndTime);
        ServiceExecutionLog serviceExecutionLog = (ServiceExecutionLog) mapServicesToWatch.get(hashKey);
        if (serviceExecutionLog == null) {
            serviceExecutionLog = new ServiceExecutionLog(hashKey);
            mapServicesToWatch.put(hashKey, serviceExecutionLog);
        }

        serviceExecutionLog.addExecutionTime(serviceExecutionTime);
    }

    /**
     * @param id
     * @param service
     * @param method
     * @param args
     * @param serviceStartTime
     * @param serviceEndTime
     */
    private void registerUserExecutionOfService(IUserView id, String service, String method,
            Object[] args, Calendar serviceStartTime, Calendar serviceEndTime) {
        UserExecutionLog userExecutionLog = (UserExecutionLog) mapUsersToWatch.get(id.getUtilizador());
        if (userExecutionLog == null) {
            userExecutionLog = new UserExecutionLog(id);
            mapUsersToWatch.put(id.getUtilizador(), userExecutionLog);
        }

        userExecutionLog.addServiceCall(generateServiceHashKey(service, method, args), serviceStartTime);
    }

    /**
     * @param serviceStartTime
     * @param serviceEndTime
     * @return
     */
    private long calculateServiceExecutionTime(Calendar serviceStartTime, Calendar serviceEndTime) {
        return serviceEndTime.getTimeInMillis() - serviceStartTime.getTimeInMillis();
    }

    /**
     * @param service
     * @param method
     * @param args
     * @return
     */
    private String generateServiceHashKey(String service, String method, Object[] args) {
        String hashKey = service + "." + method + "(";
        if (args != null) {
            for (int i = 0; i < args.length; i++) {
                if (args[i] != null) {
                    hashKey += args[i].getClass().getName();
                } else {
                    hashKey += "null";
                }
                if (i + 1 < args.length) {
                    hashKey += ", ";
                }
            }
        }
        hashKey += ")";
        return hashKey;
    }

    /**
     * @return Returns the mapServicesToWatch.
     */
    public HashMap getMapServicesToWatch(IUserView id) {
        return mapServicesToWatch;
    }

    /*
     * (non-Javadoc)
     * 
     * @see ServidorAplicacao.IServiceManagerWrapper#loggingIsOn()
     */
    public Boolean serviceLoggingIsOn(IUserView id) {
        return new Boolean(serviceLoggingIsOn);
    }

    /**
     * @return Returns the mapUsersToWatch.
     */
    public HashMap getMapUsersToWatch(IUserView id) {
        return mapUsersToWatch;
    }

    /*
     * (non-Javadoc)
     * 
     * @see ServidorAplicacao.IServiceManagerWrapper#loggingIsOn()
     */
    public Boolean userLoggingIsOn(IUserView id) {
        return new Boolean(userLoggingIsOn);
    }

    public SystemInfo getSystemInfo(IUserView id) {
        return new SystemInfo();
    }

}
