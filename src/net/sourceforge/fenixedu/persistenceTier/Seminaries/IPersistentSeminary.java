/*
 * Created on 28/Jul/2003, 10:58:39
 *
 *By Goncalo Luiz gedl [AT] rnl [DOT] ist [DOT] utl [DOT] pt
 */
package net.sourceforge.fenixedu.persistenceTier.Seminaries;

import java.util.List;

import net.sourceforge.fenixedu.domain.Seminaries.ISeminary;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentObject;

/**
 * @author Goncalo Luiz gedl [AT] rnl [DOT] ist [DOT] utl [DOT] pt
 * 
 * 
 * Created at 28/Jul/2003, 10:58:39
 *  
 */
public interface IPersistentSeminary extends IPersistentObject {
    ISeminary readByName(String name) throws ExcepcaoPersistencia;

    List readAll() throws ExcepcaoPersistencia;

    void delete(ISeminary seminary) throws ExcepcaoPersistencia;
}