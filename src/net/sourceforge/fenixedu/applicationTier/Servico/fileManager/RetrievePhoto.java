/*
 * Created on Aug 10, 2004
 *
 */
package net.sourceforge.fenixedu.applicationTier.Servico.fileManager;

import java.util.Collection;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.fileSuport.FileSuportObject;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.fileSupport.JdbcMysqlFileSupport;

/**
 * @author Nuno Correia
 * @author Ricardo Rodrigues
 */
public class RetrievePhoto extends Service {

    public FileSuportObject run(Integer personId) throws FenixServiceException, ExcepcaoPersistencia {
        final Person person = (Person) persistentObject.readByOID(Person.class, personId);
        final Collection<FileSuportObject> fileSupportObjects = JdbcMysqlFileSupport.listFiles(person.getSlideName());
        for (final FileSuportObject fileSupportObject : fileSupportObjects) {
            if (fileSupportObject.getFileName().indexOf("personPhoto") != -1) {
                return JdbcMysqlFileSupport.retrieveFile(person.getSlideName(), fileSupportObject.getFileName());
            }
        }
        return null;
    }

}