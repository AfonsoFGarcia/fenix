/*
 * Created on Aug 10, 2004
 *
 */
package net.sourceforge.fenixedu.applicationTier.Servico.fileManager;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FileAlreadyExistsServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FileNameTooLongServiceException;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.fileSuport.FileSuport;
import net.sourceforge.fenixedu.fileSuport.FileSuportObject;
import net.sourceforge.fenixedu.fileSuport.IFileSuport;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;

/**
 * @author Nuno Correia
 * @author Ricardo Rodrigues
 */
public class StorePhoto extends Service {

	public Boolean run(FileSuportObject file, Integer personId) throws FenixServiceException,
			ExcepcaoPersistencia {
		Person person = (Person) persistentObject.readByOID(Person.class, personId);

		file.setUri(person.getSlideName());
		IFileSuport fileSuport = FileSuport.getInstance();
		try {
			fileSuport.beginTransaction();
		} catch (Exception e) {
			e.printStackTrace();
			throw new FenixServiceException(e);
		}
		try {
			if (!fileSuport.isFileNameValid(file)) {
				try {
					fileSuport.abortTransaction();
				} catch (Exception e1) {
					throw new FenixServiceException(e1);
				}
				throw new FileNameTooLongServiceException();
			}
			if (fileSuport.isStorageAllowed(file)) {
				boolean result = fileSuport.updateFile(file);// storeFile(file);
				if (!result) {
					try {
						fileSuport.abortTransaction();
					} catch (Exception e1) {
						throw new FenixServiceException(e1);
					}
					throw new FileAlreadyExistsServiceException();
				}
				fileSuport.commitTransaction();
				return new Boolean(true);
			}
			fileSuport.commitTransaction();
			return new Boolean(false);

		} catch (FileNameTooLongServiceException e1) {
			throw e1;
		} catch (FileAlreadyExistsServiceException e1) {
			throw e1;
		} catch (FenixServiceException e1) {
			throw e1;
		} catch (Exception e1) {
			e1.printStackTrace();
			throw new FenixServiceException(e1);
		}
	}
}