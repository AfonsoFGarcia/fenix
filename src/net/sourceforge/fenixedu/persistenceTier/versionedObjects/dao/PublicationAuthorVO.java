package net.sourceforge.fenixedu.persistenceTier.versionedObjects.dao;

import java.util.List;

import net.sourceforge.fenixedu.commons.CollectionUtils;
import net.sourceforge.fenixedu.domain.publication.PublicationAuthor;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentPublicationAuthor;
import net.sourceforge.fenixedu.persistenceTier.versionedObjects.VersionedObjectsBase;

import org.apache.commons.collections.Predicate;

public class PublicationAuthorVO extends VersionedObjectsBase implements IPersistentPublicationAuthor{

    public List readByPublicationId(final Integer publicationId) throws ExcepcaoPersistencia{
        final List<PublicationAuthor> publicationAuthors = (List<PublicationAuthor>)this.readAll(PublicationAuthor.class);
        return (List<PublicationAuthor>) CollectionUtils.select(publicationAuthors, new Predicate() {
            public boolean evaluate(Object object) {
                if (((PublicationAuthor) object).getPublication().getIdInternal().equals(publicationId)) {
                    return true;
                }
                return false;
            }
        });
    }
    
}
