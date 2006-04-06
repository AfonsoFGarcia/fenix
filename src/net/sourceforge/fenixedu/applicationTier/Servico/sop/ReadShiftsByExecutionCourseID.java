package net.sourceforge.fenixedu.applicationTier.Servico.sop;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionCourse;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionCourseOccupancy;
import net.sourceforge.fenixedu.dataTransferObject.InfoShift;
import net.sourceforge.fenixedu.dataTransferObject.InfoShiftWithInfoLessons;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.Shift;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.util.NumberUtils;

public class ReadShiftsByExecutionCourseID extends Service {

    public InfoExecutionCourseOccupancy run(Integer executionCourseID) throws ExcepcaoPersistencia {
        final InfoExecutionCourseOccupancy infoExecutionCourseOccupancy = new InfoExecutionCourseOccupancy();
        infoExecutionCourseOccupancy.setInfoShifts(new ArrayList());

        final ExecutionCourse executionCourse = rootDomainObject.readExecutionCourseByOID(executionCourseID);
        final List<Shift> shifts = executionCourse.getAssociatedShifts();

        infoExecutionCourseOccupancy.setInfoExecutionCourse(InfoExecutionCourse
                .newInfoFromDomain(executionCourse));

        for (final Shift shift : shifts) {
            Integer capacity = Integer.valueOf(1);
            if (shift.getLotacao() != null && shift.getLotacao().intValue() != 0) {
                capacity = shift.getLotacao();
            }

            final InfoShift infoShift = InfoShiftWithInfoLessons.newInfoFromDomain(shift);
            infoShift.setOcupation(new Integer(shift.getStudentsCount()));
            infoShift.setPercentage(NumberUtils.formatNumber(new Double(infoShift.getOcupation()
                    .floatValue()
                    * 100 / capacity.floatValue()), 1));

            infoExecutionCourseOccupancy.getInfoShifts().add(infoShift);
        }
        infoExecutionCourseOccupancy.getInfoExecutionCourse().setNumberOfAttendingStudents(
                executionCourse.getAttendsCount());

        return infoExecutionCourseOccupancy;
    }

}