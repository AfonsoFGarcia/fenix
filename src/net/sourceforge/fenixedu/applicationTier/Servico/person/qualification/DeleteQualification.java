/*
 * Created on 11/Ago/2005 - 19:07:51
 * 
 */

package net.sourceforge.fenixedu.applicationTier.Servico.person.qualification;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.domain.Qualification;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;

/**
 * @author Jo�o Fialho & Rita Ferreira
 *
 */
public class DeleteQualification extends Service {
	
	public void run(Integer qualificationId) throws ExcepcaoPersistencia {
		Qualification qualification = rootDomainObject.readQualificationByOID(qualificationId);
		qualification.delete();		
	}

}
