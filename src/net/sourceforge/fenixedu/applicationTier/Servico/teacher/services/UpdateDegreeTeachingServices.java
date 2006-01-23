/**
 * Nov 17, 2005
 */
package net.sourceforge.fenixedu.applicationTier.Servico.teacher.services;

import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.domain.DomainFactory;
import net.sourceforge.fenixedu.domain.ExecutionPeriod;
import net.sourceforge.fenixedu.domain.Professorship;
import net.sourceforge.fenixedu.domain.Shift;
import net.sourceforge.fenixedu.domain.Teacher;
import net.sourceforge.fenixedu.domain.teacher.DegreeTeachingService;
import net.sourceforge.fenixedu.domain.teacher.TeacherService;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.presentationTier.Action.departmentAdmOffice.ManageDegreeTeachingServicesDispatchAction.ShiftIDTeachingPercentage;

/**
 * @author Ricardo Rodrigues
 * 
 */

public class UpdateDegreeTeachingServices extends Service {

    public void run(Integer professorshipID, List<ShiftIDTeachingPercentage> shiftsIDsTeachingPercentages)
            throws ExcepcaoPersistencia {
        Professorship professorship = (Professorship) persistentObject.readByOID(
                Professorship.class, professorshipID);        

        Teacher teacher = professorship.getTeacher();
        ExecutionPeriod executionPeriod = professorship.getExecutionCourse().getExecutionPeriod();
        TeacherService teacherService = teacher.getTeacherServiceByExecutionPeriod(executionPeriod);
        if (teacherService == null) {
            teacherService = DomainFactory.makeTeacherService(teacher, executionPeriod);
        }
        for (ShiftIDTeachingPercentage shiftIDTeachingPercentage : shiftsIDsTeachingPercentages) {
            Shift shift = (Shift) persistentObject.readByOID(Shift.class,
                    shiftIDTeachingPercentage.getShiftID());
            DegreeTeachingService degreeTeachingService = teacherService
                    .getDegreeTeachingServiceByShiftAndProfessorship(shift, professorship);
            if (degreeTeachingService != null) {
                degreeTeachingService.updatePercentage(shiftIDTeachingPercentage.getPercentage());
            } else {
                DomainFactory.makeDegreeTeachingService(teacherService, professorship, shift,
                        shiftIDTeachingPercentage.getPercentage());
            }
        }
    }
}
