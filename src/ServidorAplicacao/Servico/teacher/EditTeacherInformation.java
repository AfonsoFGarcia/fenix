/*
 * Created on 17/Nov/2003
 *  
 */
package ServidorAplicacao.Servico.teacher;

import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.beanutils.PropertyUtils;

import pt.utl.ist.berserk.logic.serviceManager.IService;
import DataBeans.teacher.InfoOrientation;
import DataBeans.teacher.InfoPublicationsNumber;
import DataBeans.teacher.InfoServiceProviderRegime;
import DataBeans.teacher.InfoWeeklyOcupation;
import DataBeans.util.Cloner;
import Dominio.teacher.IOrientation;
import Dominio.teacher.IPublicationsNumber;
import Dominio.teacher.IServiceProviderRegime;
import Dominio.teacher.IWeeklyOcupation;
import Dominio.teacher.Orientation;
import Dominio.teacher.PublicationsNumber;
import Dominio.teacher.ServiceProviderRegime;
import Dominio.teacher.WeeklyOcupation;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;
import ServidorPersistente.teacher.IPersistentOrientation;
import ServidorPersistente.teacher.IPersistentPublicationsNumber;
import ServidorPersistente.teacher.IPersistentServiceProviderRegime;
import ServidorPersistente.teacher.IPersistentWeeklyOcupation;

/**
 * @author Leonor Almeida
 * @author Sergio Montelobo
 *  
 */
public class EditTeacherInformation implements IService {
    /**
     *  
     */
    public EditTeacherInformation() {
    }

    /*
     * (non-Javadoc)
     * 
     * @see ServidorAplicacao.IServico#getNome()
     */
    public String getNome() {
        return "EditTeacherInformation";
    }

    /**
     * Executes the service.
     */
    public Boolean run(InfoServiceProviderRegime infoServiceProviderRegime,
            InfoWeeklyOcupation infoWeeklyOcupation, List infoOrientations,
            List infoPublicationsNumbers) throws FenixServiceException {
        try {
            Date date = Calendar.getInstance().getTime();
            ISuportePersistente sp = SuportePersistenteOJB.getInstance();

            IPersistentServiceProviderRegime persistentServiceProviderRegime = sp
                    .getIPersistentServiceProviderRegime();
            Integer serviceProviderRegimeId = infoServiceProviderRegime
                    .getIdInternal();
            IServiceProviderRegime serviceProviderRegimeToLock = (IServiceProviderRegime) persistentServiceProviderRegime
                    .readByOID(ServiceProviderRegime.class,
                            serviceProviderRegimeId, true);
            IServiceProviderRegime newServiceProviderRegime = Cloner
                    .copyInfoServiceProviderRegime2IServiceProviderRegime(infoServiceProviderRegime);

            if (serviceProviderRegimeToLock == null) {
                serviceProviderRegimeToLock = new ServiceProviderRegime();
                persistentServiceProviderRegime
                        .simpleLockWrite(serviceProviderRegimeToLock);
            }
            Integer ackOptLock = serviceProviderRegimeToLock.getAckOptLock();
            PropertyUtils.copyProperties(serviceProviderRegimeToLock,
                    newServiceProviderRegime);
            serviceProviderRegimeToLock.setAckOptLock(ackOptLock);
            serviceProviderRegimeToLock.setLastModificationDate(date);

            IPersistentWeeklyOcupation persistentWeeklyOcupation = sp
                    .getIPersistentWeeklyOcupation();
            Integer weeklyOcupationId = infoWeeklyOcupation.getIdInternal();
            IWeeklyOcupation weeklyOcupationToLock = (IWeeklyOcupation) persistentWeeklyOcupation
                    .readByOID(WeeklyOcupation.class, weeklyOcupationId, true);
            IWeeklyOcupation newWeeklyOcupation = Cloner
                    .copyInfoWeeklyOcupation2IWeeklyOcupation(infoWeeklyOcupation);

            if (weeklyOcupationToLock == null) {
                weeklyOcupationToLock = new WeeklyOcupation();
                persistentWeeklyOcupation
                        .simpleLockWrite(weeklyOcupationToLock);
            }
            ackOptLock = weeklyOcupationToLock.getAckOptLock();
            PropertyUtils.copyProperties(weeklyOcupationToLock,
                    newWeeklyOcupation);
            weeklyOcupationToLock.setAckOptLock(ackOptLock);
            weeklyOcupationToLock.setLastModificationDate(date);

            IPersistentOrientation persistentOrientation = sp
                    .getIPersistentOrientation();
            Iterator iter = infoOrientations.iterator();
            while (iter.hasNext()) {
                InfoOrientation infoOrientation = (InfoOrientation) iter.next();
                Integer orientationId = infoOrientation.getIdInternal();
                IOrientation orientationToLock = (IOrientation) persistentOrientation
                        .readByOID(Orientation.class, orientationId, true);

                IOrientation newOrientation = Cloner
                        .copyInfoOrientation2IOrientation(infoOrientation);

                if (orientationToLock == null) {
                    orientationToLock = new Orientation();
                    persistentOrientation.simpleLockWrite(orientationToLock);
                }
                ackOptLock = orientationToLock.getAckOptLock();
                PropertyUtils.copyProperties(orientationToLock, newOrientation);
                orientationToLock.setAckOptLock(ackOptLock);
                orientationToLock.setLastModificationDate(date);
            }

            IPersistentPublicationsNumber persistentPublicationsNumber = sp
                    .getIPersistentPublicationsNumber();
            iter = infoPublicationsNumbers.iterator();
            while (iter.hasNext()) {
                InfoPublicationsNumber infoPublicationsNumber = (InfoPublicationsNumber) iter
                        .next();
                Integer publicationsNumberId = infoPublicationsNumber
                        .getIdInternal();
                IPublicationsNumber publicationsNumberToLock = (IPublicationsNumber) persistentPublicationsNumber
                        .readByOID(PublicationsNumber.class,
                                publicationsNumberId, true);
                IPublicationsNumber newPublicationsNumber = Cloner
                        .copyInfoPublicationsNumber2IPublicationsNumber(infoPublicationsNumber);

                if (publicationsNumberToLock == null) {
                    publicationsNumberToLock = new PublicationsNumber();
                    persistentPublicationsNumber
                            .simpleLockWrite(publicationsNumberToLock);
                }
                ackOptLock = publicationsNumberToLock.getAckOptLock();
                PropertyUtils.copyProperties(publicationsNumberToLock,
                        newPublicationsNumber);
                publicationsNumberToLock.setAckOptLock(ackOptLock);
                publicationsNumberToLock.setLastModificationDate(date);
            }
            // TODO: faltam os cargos de gest�o

            return new Boolean(true);
        } catch (Exception e) {
            throw new FenixServiceException(e);
        }
    }
}