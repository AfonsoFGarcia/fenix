package net.sourceforge.fenixedu.applicationTier.Servico.sop;

/**
 * Servi�o RemoverTurno
 * 
 * @author tfc130
 * @version
 */

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.dataTransferObject.InfoClass;
import net.sourceforge.fenixedu.dataTransferObject.InfoShift;
import net.sourceforge.fenixedu.domain.SchoolClass;
import net.sourceforge.fenixedu.domain.Shift;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.ITurnoPersistente;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;

public class RemoverTurno extends Service {

    public Object run(final InfoShift infoShift, final InfoClass infoClass) throws ExcepcaoPersistencia {
        final ITurnoPersistente persistentShift = persistentSupport.getITurnoPersistente();

        final Shift shift = (Shift) persistentShift.readByOID(Shift.class, infoShift.getIdInternal());
        if (shift == null) {
            return Boolean.FALSE;
        }
        final SchoolClass schoolClass = (SchoolClass) CollectionUtils.find(shift.getAssociatedClasses(), new Predicate() {
            public boolean evaluate(Object arg0) {
                final SchoolClass schoolClass = (SchoolClass) arg0;
                return schoolClass.getIdInternal().equals(infoClass.getIdInternal());
            }
        });
        if (schoolClass == null) {
            return Boolean.FALSE;
        }

        shift.getAssociatedClasses().remove(schoolClass);
        
        return Boolean.TRUE;
    }

}