/*
 * Created on Oct 14, 2003
 *
 */
package net.sourceforge.fenixedu.persistenceTier;

import java.util.Collection;
import java.util.List;

import net.sourceforge.fenixedu.domain.IExternalPerson;

/**
 * @author : - Shezad Anavarali (sana@mega.ist.utl.pt) - Nadir Tarmahomed
 *         (naat@mega.ist.utl.pt)
 *  
 */
public interface IPersistentExternalPerson extends IPersistentObject {
    public IExternalPerson readByUsername(String username) throws ExcepcaoPersistencia;

    public List readByName(String name) throws ExcepcaoPersistencia;

    public IExternalPerson readByNameAndAddressAndWorkLocationID(String name, String address,
            Integer workLocationID) throws ExcepcaoPersistencia;

    public List readByWorkLocation(Integer workLocationID) throws ExcepcaoPersistencia;

    public String readLastDocumentIdNumber() throws ExcepcaoPersistencia;
    
    public Collection<IExternalPerson> readByIDs(Collection<Integer> externalPersonsIDs) throws ExcepcaoPersistencia;
}