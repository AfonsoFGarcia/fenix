package net.sourceforge.fenixedu.applicationTier.Servico.sop;

/**
 * Servi�o RemoverTurno
 * 
 * @author tfc130
 * @version
 */

import net.sourceforge.fenixedu.dataTransferObject.InfoClass;
import net.sourceforge.fenixedu.dataTransferObject.InfoShift;
import net.sourceforge.fenixedu.dataTransferObject.util.Cloner;
import net.sourceforge.fenixedu.domain.ISchoolClass;
import net.sourceforge.fenixedu.domain.ISchoolClassShift;
import net.sourceforge.fenixedu.domain.IShift;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.OJB.PersistenceSupportFactory;
import pt.utl.ist.berserk.logic.serviceManager.IService;

public class RemoverTurno implements IService {

    public RemoverTurno() {
    }

    public Object run(InfoShift infoShift, InfoClass infoClass) {

        try {
            ISuportePersistente sp = PersistenceSupportFactory.getDefaultPersistenceSupport();

            IShift shift = Cloner.copyInfoShift2IShift(infoShift);
            ISchoolClass classTemp = Cloner.copyInfoClass2Class(infoClass);

            // Read From Database

            IShift shiftToDelete = sp.getITurnoPersistente().readByNameAndExecutionCourse(
                    shift.getNome(), shift.getDisciplinaExecucao());
            ISchoolClass classToDelete = sp.getITurmaPersistente()
                    .readByNameAndExecutionDegreeAndExecutionPeriod(classTemp.getNome(),
                            classTemp.getExecutionDegree(), classTemp.getExecutionPeriod());
            ISchoolClassShift turmaTurnoToDelete = null;
            if ((shiftToDelete != null) && (classToDelete != null)) {
                turmaTurnoToDelete = sp.getITurmaTurnoPersistente().readByTurmaAndTurno(classToDelete,
                        shiftToDelete);
            } else
                return Boolean.FALSE;

            // Check if exists
            if (turmaTurnoToDelete != null)
                sp.getITurmaTurnoPersistente().delete(turmaTurnoToDelete);
            else
                return Boolean.FALSE;

        } catch (ExcepcaoPersistencia ex) {
            ex.printStackTrace();
        }
        return Boolean.TRUE;
    }

}