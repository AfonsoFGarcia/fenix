package net.sourceforge.fenixedu.applicationTier.Servico.publico;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.dataTransferObject.InfoClass;
import net.sourceforge.fenixedu.domain.SchoolClass;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.ITurmaPersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;
import net.sourceforge.fenixedu.applicationTier.Service;

/**
 * @author Jo�o Mota
 */
public class SelectClasses extends Service {

    public Object run(InfoClass infoClass) throws ExcepcaoPersistencia {
        final ITurmaPersistente classDAO = persistentSupport.getITurmaPersistente();

        List<SchoolClass> classes = classDAO.readByExecutionPeriodAndCurricularYearAndExecutionDegree(
                infoClass.getInfoExecutionPeriod().getIdInternal(), infoClass.getAnoCurricular(),
                infoClass.getInfoExecutionDegree().getIdInternal());

        List<InfoClass> infoClasses = new ArrayList<InfoClass>();
        for (SchoolClass taux : classes) {
            infoClasses.add(InfoClass.newInfoFromDomain(taux));
        }

        return infoClasses;
    }

}
