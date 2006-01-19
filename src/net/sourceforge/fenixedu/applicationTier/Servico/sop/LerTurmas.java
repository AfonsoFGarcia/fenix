package net.sourceforge.fenixedu.applicationTier.Servico.sop;

/**
 * Servi�o LerTurmas
 * 
 * @author tfc130
 * @version
 */

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import net.sourceforge.fenixedu.dataTransferObject.InfoClass;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionDegree;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionPeriod;
import net.sourceforge.fenixedu.domain.ExecutionDegree;
import net.sourceforge.fenixedu.domain.ExecutionPeriod;
import net.sourceforge.fenixedu.domain.SchoolClass;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.ITurmaPersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;
import net.sourceforge.fenixedu.applicationTier.IService;

public class LerTurmas implements IService {

    public List run(InfoExecutionDegree infoExecutionDegree, InfoExecutionPeriod infoExecutionPeriod,
            Integer curricularYear) throws ExcepcaoPersistencia {

        List classesList = null;
        List infoClassesList = null;

        ISuportePersistente sp = PersistenceSupportFactory.getDefaultPersistenceSupport();

        ITurmaPersistente classDAO = sp.getITurmaPersistente();

        ExecutionPeriod executionPeriod = (ExecutionPeriod) sp.getIPersistentExecutionPeriod()
                .readByOID(ExecutionPeriod.class, infoExecutionPeriod.getIdInternal());

        ExecutionDegree executionDegree = (ExecutionDegree) sp.getIPersistentExecutionDegree()
                .readByOID(ExecutionDegree.class, infoExecutionDegree.getIdInternal());

        if (curricularYear != null) {
            classesList = classDAO.readByExecutionPeriodAndCurricularYearAndExecutionDegree(
                    executionPeriod.getIdInternal(), curricularYear, executionDegree.getIdInternal());
        } else {
            classesList = classDAO.readByExecutionDegreeAndExecutionPeriod(executionDegree
                    .getIdInternal(), executionPeriod.getIdInternal());
        }

        Iterator iterator = classesList.iterator();
        infoClassesList = new ArrayList();
        while (iterator.hasNext()) {
            SchoolClass elem = (SchoolClass) iterator.next();

            InfoClass infoClass = InfoClass.newInfoFromDomain(elem);
            infoClass.setInfoExecutionPeriod(infoExecutionPeriod);
            infoClassesList.add(infoClass);
        }

        return infoClassesList;
    }

}