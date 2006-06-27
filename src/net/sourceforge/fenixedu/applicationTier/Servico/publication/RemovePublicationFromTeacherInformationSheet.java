package net.sourceforge.fenixedu.applicationTier.Servico.publication;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.domain.Teacher;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.publication.Publication;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;

public class RemovePublicationFromTeacherInformationSheet extends Service {
    
    public void run(Integer teacherId, final Integer publicationId) throws ExcepcaoPersistencia, DomainException {
        Teacher teacher = rootDomainObject.readTeacherByOID(teacherId);
        Publication publication = (Publication) rootDomainObject.readPublicationByOID(publicationId);        
        teacher.removeFromTeacherInformationSheet(publication);
    }
    
}