package net.sourceforge.fenixedu.persistenceTier;

import java.util.Collection;

import net.sourceforge.fenixedu.domain.DomainObject;
import net.sourceforge.fenixedu.domain.RootDomainObject;

import org.apache.ojb.broker.query.Criteria;

/**
 * @author tfc130
 */
public interface IPersistentObject {

    void deleteByOID(Class classToQuery, Integer oid) throws ExcepcaoPersistencia;

    public DomainObject readByOID(Class classToQuery, Integer oid) throws ExcepcaoPersistencia;

    public int count(Class classToQuery, Criteria criteria);

    public Collection readAll(Class classToQuery)  throws ExcepcaoPersistencia;

    public RootDomainObject readRootDomainObject() throws ExcepcaoPersistencia;

}