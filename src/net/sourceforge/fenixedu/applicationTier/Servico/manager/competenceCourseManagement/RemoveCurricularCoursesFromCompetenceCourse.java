package net.sourceforge.fenixedu.applicationTier.Servico.manager.competenceCourseManagement;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NotExistingServiceException;
import net.sourceforge.fenixedu.domain.CompetenceCourse;
import net.sourceforge.fenixedu.domain.CurricularCourse;
import net.sourceforge.fenixedu.persistenceTier.IPersistentCompetenceCourse;
import net.sourceforge.fenixedu.persistenceTier.IPersistentCurricularCourse;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;
import net.sourceforge.fenixedu.applicationTier.Service;

public class RemoveCurricularCoursesFromCompetenceCourse extends Service {
	public void run(Integer competenceCourseID, Integer[] curricularCoursesIDs) throws Exception {
		ISuportePersistente suportePersistente = PersistenceSupportFactory.getDefaultPersistenceSupport();
		IPersistentCompetenceCourse persistentCompetenceCourse = suportePersistente.getIPersistentCompetenceCourse();
		CompetenceCourse competenceCourse = (CompetenceCourse) persistentCompetenceCourse.readByOID(CompetenceCourse.class, competenceCourseID);
		if(competenceCourse == null) {
			throw new NotExistingServiceException("error.manager.noCompetenceCourse");
		}
		
		IPersistentCurricularCourse persistentCurricularCourse = suportePersistente.getIPersistentCurricularCourse();
		for (Integer curricularCourseID : curricularCoursesIDs) {
			CurricularCourse curricularCourse = (CurricularCourse) persistentCurricularCourse.readByOID(CurricularCourse.class, curricularCourseID);
			if(curricularCourse != null) {
				competenceCourse.getAssociatedCurricularCourses().remove(curricularCourse);
			}
		}
	}
}
