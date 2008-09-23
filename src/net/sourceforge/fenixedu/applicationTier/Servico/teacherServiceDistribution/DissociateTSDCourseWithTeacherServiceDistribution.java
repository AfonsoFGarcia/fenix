package net.sourceforge.fenixedu.applicationTier.Servico.teacherServiceDistribution;

import net.sourceforge.fenixedu.applicationTier.FenixService;
import net.sourceforge.fenixedu.domain.teacherServiceDistribution.TSDCourse;
import net.sourceforge.fenixedu.domain.teacherServiceDistribution.TeacherServiceDistribution;

public class DissociateTSDCourseWithTeacherServiceDistribution extends FenixService {
    public void run(Integer tsdId, Integer tsdCourseId) {
	TeacherServiceDistribution tsd = rootDomainObject.readTeacherServiceDistributionByOID(tsdId);
	TSDCourse tsdCourse = rootDomainObject.readTSDCourseByOID(tsdCourseId);

	for (TSDCourse course : tsd.getTSDCoursesByCompetenceCourse(tsdCourse.getCompetenceCourse())) {
	    tsd.removeTSDCourseFromAllChilds(course);
	}

	tsd.removeTSDCourseFromAllChilds(tsdCourse);
    }
}
