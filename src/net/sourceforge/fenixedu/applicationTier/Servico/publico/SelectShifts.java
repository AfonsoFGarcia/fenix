package net.sourceforge.fenixedu.applicationTier.Servico.publico;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.dataTransferObject.InfoShift;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.Shift;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;

/**
 * @author Jo�o Mota
 * 
 */
public class SelectShifts extends Service {

    public Object run(InfoShift infoShift) throws ExcepcaoPersistencia {
    	final Shift shift = RootDomainObject.getInstance().readShiftByOID(infoShift.getIdInternal());
    	final ExecutionCourse executionCourse = shift.getDisciplinaExecucao();
        final List<Shift> shifts = executionCourse.getAssociatedShifts();

        List<InfoShift> infoShifts = new ArrayList<InfoShift>();
        for (Shift taux : shifts) {
            infoShifts.add(InfoShift.newInfoFromDomain(taux));
        }

        return infoShifts;
    }

}
