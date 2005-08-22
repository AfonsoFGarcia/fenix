package net.sourceforge.fenixedu.applicationTier.Servico.manager.competenceCourseManagement;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.InvalidArgumentsServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NonExistingServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoCompetenceCourse;
import net.sourceforge.fenixedu.dataTransferObject.InfoCompetenceCourseWithCurricularCourses;
import net.sourceforge.fenixedu.domain.CompetenceCourse;
import net.sourceforge.fenixedu.domain.Department;
import net.sourceforge.fenixedu.domain.ICompetenceCourse;
import net.sourceforge.fenixedu.domain.IDepartment;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.persistenceTier.IPersistentCompetenceCourse;
import net.sourceforge.fenixedu.persistenceTier.IPersistentDepartment;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;
import pt.utl.ist.berserk.logic.serviceManager.IService;

public class CreateEditCompetenceCourse implements IService {

	public InfoCompetenceCourse run(Integer competenceCourseID, String code, String name, Integer departmentID) throws Exception{
		ISuportePersistente suportePersistente = PersistenceSupportFactory.getDefaultPersistenceSupport();
		IDepartment department = null;
		if(departmentID != null) {
			IPersistentDepartment persistentDepartment = suportePersistente.getIDepartamentoPersistente();
			department = (IDepartment) persistentDepartment.readByOID(Department.class, departmentID);
			if(department == null) {
				throw new NonExistingServiceException("error.manager.noDepartment");
			}
		}
		try {
			ICompetenceCourse competenceCourse = null;
			if(competenceCourseID == null) {
				competenceCourse = new CompetenceCourse(code, name, department);
			} else {
				IPersistentCompetenceCourse persistentCompetenceCourse = suportePersistente.getIPersistentCompetenceCourse();
				competenceCourse = (ICompetenceCourse) persistentCompetenceCourse.readByOID(CompetenceCourse.class, competenceCourseID);
				if(competenceCourse == null) {
					throw new NonExistingServiceException("error.manager.noCompetenceCourse");
				}
				competenceCourse.edit(code, name, department);
			}
			return InfoCompetenceCourseWithCurricularCourses.newInfoFromDomain(competenceCourse);
		}catch(DomainException domainException) {
			throw new InvalidArgumentsServiceException(domainException.getMessage());
		}
	}
}
