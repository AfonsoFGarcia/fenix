/*
 * Created on 13/Nov/2003
 *  
 */
package net.sourceforge.fenixedu.persistenceTier.publication;

import java.util.List;

import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentObject;

/**
 * @author TJBF & PFON
 *  
 */
public interface IPersistentPublicationFormat extends IPersistentObject {
    List readAll() throws ExcepcaoPersistencia;
}