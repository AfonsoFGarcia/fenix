/*
 * Created on 22/Nov/2003
 *
 */
package net.sourceforge.fenixedu.persistenceTier.OJB.publication;

import java.util.List;

import org.apache.ojb.broker.query.Criteria;

import net.sourceforge.fenixedu.domain.publication.PublicationType;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.OJB.PersistentObjectOJB;
import net.sourceforge.fenixedu.persistenceTier.publication.IPersistentPublicationType;

/**
 * @author TJBF & PFON
 *  
 */
public class PublicationTypeOJB extends PersistentObjectOJB implements IPersistentPublicationType {

    /**
     *  
     */
    public PublicationTypeOJB() {
        super();
    }

    /*
     * (non-Javadoc)
     * 
     * @see ServidorPersistente.teacher.IPersistentOldPublication#readAllByTeacherAndOldPublicationType(Dominio.ITeacher,
     *      Util.OldPublicationType)
     */
    public List readAll() throws ExcepcaoPersistencia {
        Criteria criteria = new Criteria();
        return queryList(PublicationType.class, criteria);
    }

    public List readByPublicationsTypeId(String publicationType) throws ExcepcaoPersistencia {
        Criteria criteria = new Criteria();
        criteria.addEqualTo("publicationType", publicationType);
        return queryList(PublicationType.class, criteria);
    }
}