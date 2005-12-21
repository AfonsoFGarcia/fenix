package net.sourceforge.fenixedu.applicationTier.Servico.commons;

import java.util.ArrayList;
import java.util.Collection;

import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentObject;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;
import pt.utl.ist.berserk.logic.serviceManager.IService;

public class ReadDomainObjectsByInternalID<E> implements IService {

    public Collection<E> run(final Class<E> clazz, final Integer[] domainObjectOIDs)
            throws ExcepcaoPersistencia {
        final ISuportePersistente sp = PersistenceSupportFactory.getDefaultPersistenceSupport();
        final IPersistentObject persistentObject = sp.getIPersistentObject();

        final Collection<E> domainObjects = new ArrayList<E>(domainObjectOIDs.length);
        for (final Integer domainObjectOID : domainObjectOIDs) {
            domainObjects.add((E) persistentObject.readByOID(clazz, domainObjectOID));
        }

        return domainObjects;
    }

}
