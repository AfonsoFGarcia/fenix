package net.sourceforge.fenixedu.applicationTier.Servico;

import net.sourceforge.fenixedu.domain.IExecutionYear;
import net.sourceforge.fenixedu.fileSuport.FileSuport;
import net.sourceforge.fenixedu.fileSuport.FileSuportObject;
import net.sourceforge.fenixedu.fileSuport.IFileSuport;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentExecutionYear;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;
import pt.utl.ist.berserk.logic.serviceManager.IService;

public class CheckIsAliveService implements IService {

    private static final String filename = "checkIsAlive";
    private static final String filepath = "/" + filename;

    public Boolean run() throws ExcepcaoPersistencia {
        checkFenixDatabaseOps();
        //checkSlideDatabaseOps();
        return new Boolean(true);
    }

    private void checkFenixDatabaseOps() throws ExcepcaoPersistencia {
        final ISuportePersistente persistenceSupport = PersistenceSupportFactory
                .getDefaultPersistenceSupport();
        final IPersistentExecutionYear persistentExecutionYear = persistenceSupport
                .getIPersistentExecutionYear();

        final IExecutionYear executionYear = persistentExecutionYear.readCurrentExecutionYear();

        if (executionYear.getIdInternal() == null) {
            throw new RuntimeException("Problems accesing fenix database!");
        }
    }

    synchronized private void checkSlideDatabaseOps() {
        final IFileSuport fileSuport = FileSuport.getInstance();

        boolean resultRead = false;
        try {
            fileSuport.beginTransaction();
            final FileSuportObject fileSuportObject = fileSuport.retrieveFile(filepath);
            resultRead = fileSuportObject != null;
            fileSuport.commitTransaction();
        } catch (Exception ex) {
            resultRead = false;
            try {
                fileSuport.abortTransaction();
            } catch (Exception ex2) {
                // nothing else to do.
            }
        }

        if (!resultRead) {
            throw new RuntimeException("Problems accesing slide database!");
        }

    }

}
