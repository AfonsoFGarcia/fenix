package net.sourceforge.fenixedu.applicationTier.Servico.publico;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import net.sourceforge.fenixedu.applicationTier.FenixService;
import net.sourceforge.fenixedu.dataTransferObject.InfoShift;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.Shift;
import pt.ist.fenixWebFramework.services.Service;

/**
 * @author Jo�o Mota
 * 
 */
public class SelectShifts extends FenixService {

    @Service
    public static Object run(InfoShift infoShift) {
	final Shift shift = rootDomainObject.readShiftByOID(infoShift.getIdInternal());
	final ExecutionCourse executionCourse = shift.getDisciplinaExecucao();
	final Set<Shift> shifts = executionCourse.getAssociatedShifts();

	List<InfoShift> infoShifts = new ArrayList<InfoShift>();
	for (Shift taux : shifts) {
	    infoShifts.add(InfoShift.newInfoFromDomain(taux));
	}

	return infoShifts;
    }

}