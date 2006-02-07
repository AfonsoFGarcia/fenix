package net.sourceforge.fenixedu.domain.teacher;

import net.sourceforge.fenixedu.domain.Professorship;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;

public class TeacherMasterDegreeService extends TeacherMasterDegreeService_Base {

    public TeacherMasterDegreeService(TeacherService teacherService, Professorship professorship) {
        super();
        setTeacherService(teacherService);
        setProfessorship(professorship);
    }

    public void updateValues(Double hours, Double credits) {
        if (hours != null && hours < 0) {
            throw new DomainException("error.invalid.hours");
        }
        if (credits != null && credits < 0) {
            throw new DomainException("error.invalid.credits");
        }

        if ((hours == null && credits == null)
                || ((hours == null || hours == 0) && (credits == null || credits == 0))) {
            delete();
        }
        setCredits(credits);
        setHours(hours);
    }

    public void delete() {
        removeProfessorship();
        removeTeacherService();
        deleteDomainObject();
    }
}
