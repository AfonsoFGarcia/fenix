/*
 * Created on Nov 3, 2005
 *	by mrsp
 */
package net.sourceforge.fenixedu.applicationTier.Servico.manager.advisoriesManagement;

import java.util.Calendar;
import java.util.Date;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.domain.Advisory;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;


public class EditAdvisory extends Service {

    public void run(Integer advisoryID, String newSender, String newSubject, String message, Date expires) throws ExcepcaoPersistencia, FenixServiceException{
        Advisory advisory = rootDomainObject.readAdvisoryByOID(advisoryID);
        
        if(advisory == null){                  
            throw new FenixServiceException("error.no.advisory");            
        }
        
        if(newSender == null && newSubject == null && message == null && expires != null){
            advisory.setExpires(Calendar.getInstance().getTime());                        
        }
        else{
            advisory.setMessage(message);
            advisory.setSender(newSender);
            advisory.setSubject(newSubject);
            advisory.setExpires(expires);
        }
    }
}
