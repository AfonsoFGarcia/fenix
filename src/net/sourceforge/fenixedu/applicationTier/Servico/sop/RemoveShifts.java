package net.sourceforge.fenixedu.applicationTier.Servico.sop;

import java.util.List;

import net.sourceforge.fenixedu.dataTransferObject.InfoClass;
import net.sourceforge.fenixedu.domain.SchoolClass;
import net.sourceforge.fenixedu.domain.Shift;
import net.sourceforge.fenixedu.domain.SchoolClass;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;
import pt.utl.ist.berserk.logic.serviceManager.IService;

public class RemoveShifts implements IService {

    public Boolean run(final InfoClass infoClass, final List shiftOIDs) throws ExcepcaoPersistencia {
        final ISuportePersistente sp = PersistenceSupportFactory.getDefaultPersistenceSupport();

        final SchoolClass schoolClass = (SchoolClass) sp.getITurmaPersistente().readByOID(SchoolClass.class,
                infoClass.getIdInternal());
        final List<Shift> shifts = schoolClass.getAssociatedShifts();

        for (int i = 0; i < shifts.size(); i++) {
            final Shift shift = shifts.get(i);
            if (shiftOIDs.contains(shift.getIdInternal())) {
                shifts.remove(shift);
                i--;
            }
        }

        return Boolean.TRUE;
    }

}