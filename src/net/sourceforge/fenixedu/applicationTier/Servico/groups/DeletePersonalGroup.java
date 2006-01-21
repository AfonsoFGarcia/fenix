package net.sourceforge.fenixedu.applicationTier.Servico.groups;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.domain.PersonalGroup;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;

public class DeletePersonalGroup extends Service {
    
    public void run(Integer groupId) throws FenixServiceException, ExcepcaoPersistencia {
        PersonalGroup personalGroup = (PersonalGroup) persistentObject.readByOID(PersonalGroup.class, groupId);
        
        if (personalGroup == null) {
            throw new FenixServiceException("error.noPersonalGroup");
        }
        
        personalGroup.delete();
    }

}
