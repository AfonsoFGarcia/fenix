/*
 * Created on 31/Jul/2003, 11:53:18
 *
 *By Goncalo Luiz gedl [AT] rnl [DOT] ist [DOT] utl [DOT] pt
 */
package ServidorAplicacao.Servico.Seminaries;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import pt.utl.ist.berserk.logic.serviceManager.IService;
import DataBeans.Seminaries.InfoSeminary;
import Dominio.Seminaries.ISeminary;
import ServidorApresentacao.Action.Seminaries.Exceptions.BDException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;
import ServidorPersistente.Seminaries.IPersistentSeminary;

/**
 * @author Goncalo Luiz gedl [AT] rnl [DOT] ist [DOT] utl [DOT] pt
 * 
 * 
 * Created at 31/Jul/2003, 11:53:18
 *  
 */
public class GetAllSeminaries implements IService {

    public GetAllSeminaries() {
    }

    public List run(Boolean inEnrollmentPeriod) throws BDException {
        List seminariesInfo = new LinkedList();
        try {
            ISuportePersistente persistenceSupport = SuportePersistenteOJB.getInstance();
            IPersistentSeminary persistentSeminary = persistenceSupport.getIPersistentSeminary();
            List seminaries = persistentSeminary.readAll();
            for (Iterator iterator = seminaries.iterator(); iterator.hasNext();) {

                InfoSeminary infoSeminary = InfoSeminary.newInfoFromDomain((ISeminary) iterator.next());

                Calendar now = new GregorianCalendar();
                Calendar endDate = new GregorianCalendar();
                Calendar beginDate = new GregorianCalendar();
                endDate.setTimeInMillis(infoSeminary.getEnrollmentEndTime().getTimeInMillis()
                        + infoSeminary.getEnrollmentEndDate().getTimeInMillis());
                beginDate.setTimeInMillis(infoSeminary.getEnrollmentBeginTime().getTimeInMillis()
                        + infoSeminary.getEnrollmentBeginDate().getTimeInMillis());
                if ((!inEnrollmentPeriod.booleanValue())
                        || (endDate.after(now) && beginDate.before(now)))
                    seminariesInfo.add(infoSeminary);
            }
        } catch (ExcepcaoPersistencia ex) {
            throw new BDException(
                    "Got an error while trying to retrieve multiple seminaries from the database", ex);
        }
        return seminariesInfo;
    }
}