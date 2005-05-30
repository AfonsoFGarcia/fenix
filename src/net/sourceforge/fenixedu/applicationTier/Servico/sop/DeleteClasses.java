/*
 *
 * Created on 2003/08/15
 */

package net.sourceforge.fenixedu.applicationTier.Servico.sop;

/**
 * 
 * @author Luis Cruz & Sara Ribeiro
 */
import java.util.List;

import net.sourceforge.fenixedu.domain.ISchoolClass;
import net.sourceforge.fenixedu.domain.ISchoolClassShift;
import net.sourceforge.fenixedu.domain.IShift;
import net.sourceforge.fenixedu.domain.SchoolClass;
import net.sourceforge.fenixedu.domain.SchoolClassShift;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;
import pt.utl.ist.berserk.logic.serviceManager.IService;

public class DeleteClasses implements IService {

    public Object run(List classOIDs) throws ExcepcaoPersistencia {

        final ISuportePersistente sp = PersistenceSupportFactory.getDefaultPersistenceSupport();

        for (int i = 0; i < classOIDs.size(); i++) {
            Integer classId = (Integer) classOIDs.get(i);
            final ISchoolClass schoolClass = (ISchoolClass) sp.getITurmaPersistente().readByOID(
                    SchoolClass.class, classId);

            // sp.getITurmaPersistente().simpleLockWrite(schoolClass);

            // Shift
            for (IShift shift : (List<IShift>) schoolClass.getAssociatedShifts()) {
                shift.getAssociatedClasses().remove(schoolClass);
            }
            schoolClass.getAssociatedShifts().clear();

            // ExecutionDegree
            schoolClass.getExecutionDegree().getSchoolClasses().remove(schoolClass);
            schoolClass.setExecutionDegree(null);

            // ExecutionPeriod
            schoolClass.getExecutionPeriod().getSchoolClasses().remove(schoolClass);
            schoolClass.setExecutionPeriod(null);

            // SchoolClassShift
            List<ISchoolClassShift> schoolClassShifts = schoolClass.getSchoolClassShifts();
            for (ISchoolClassShift schoolClassShift : schoolClassShifts) {
                schoolClassShift.setTurma(null);
                schoolClassShift.getTurno().getSchoolClassShifts().remove(schoolClassShift);
                schoolClassShift.setTurno(null);
                sp.getITurmaTurnoPersistente().deleteByOID(SchoolClassShift.class,
                        schoolClassShift.getIdInternal());
            }
            schoolClass.getSchoolClassShifts().clear();

            sp.getITurmaPersistente().deleteByOID(SchoolClass.class, schoolClass.getIdInternal());
            // sp.getITurmaPersistente().delete(schoolClass);
        }

        return new Boolean(true);

    }

}