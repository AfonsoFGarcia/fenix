package net.sourceforge.fenixedu.applicationTier.Servico.manager.competenceCourseManagement;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NotExistingServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoCompetenceCourse;
import net.sourceforge.fenixedu.dataTransferObject.InfoCompetenceCourseWithCurricularCourses;
import net.sourceforge.fenixedu.domain.CompetenceCourse;
import net.sourceforge.fenixedu.domain.ICompetenceCourse;
import net.sourceforge.fenixedu.persistenceTier.IPersistentCompetenceCourse;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;
import pt.utl.ist.berserk.logic.serviceManager.IService;

public class ReadCompetenceCourse implements IService {
	
	public InfoCompetenceCourse run(Integer competenceCourseID) throws Exception{
		ISuportePersistente suportePersistente = PersistenceSupportFactory.getDefaultPersistenceSupport();
		IPersistentCompetenceCourse persistentCompetenceCourse = suportePersistente.getIPersistentCompetenceCourse();
		ICompetenceCourse competenceCourse = (ICompetenceCourse) persistentCompetenceCourse.readByOID(CompetenceCourse.class, competenceCourseID);
		if(competenceCourse == null) {
			throw new NotExistingServiceException("Invalid CompetenceCourse ID");
		}
		return InfoCompetenceCourseWithCurricularCourses.newInfoFromDomain(competenceCourse);
	}

}
