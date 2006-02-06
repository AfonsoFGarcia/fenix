package net.sourceforge.fenixedu.applicationTier.Servico.teacher.teacherService;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.teacher.distribution.DistributionTeacherServicesByCourseDTO;
import net.sourceforge.fenixedu.dataTransferObject.teacher.distribution.DistributionTeacherServicesByCourseDTO.ExecutionCourseDistributionServiceEntryDTO;
import net.sourceforge.fenixedu.domain.CompetenceCourse;
import net.sourceforge.fenixedu.domain.CurricularCourse;
import net.sourceforge.fenixedu.domain.CurricularCourseScope;
import net.sourceforge.fenixedu.domain.Department;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.ExecutionDegree;
import net.sourceforge.fenixedu.domain.ExecutionPeriod;
import net.sourceforge.fenixedu.domain.Professorship;
import net.sourceforge.fenixedu.domain.ShiftType;
import net.sourceforge.fenixedu.domain.Teacher;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentTeacher;

/**
 * 
 * @author jpmsit, amak
 */
public class ReadTeacherServiceDistributionByCourse extends Service {

	public List run(String username, List<Integer> executionPeriodsIDs) throws FenixServiceException, ExcepcaoPersistencia {

			
		IPersistentTeacher persistenceTeacher = persistentSupport.getIPersistentTeacher();
				
		Teacher teacher = persistenceTeacher.readTeacherByUsername(username);

		Department department = teacher.getLastWorkingDepartment();

		List<CompetenceCourse> competenceCourseList = department.getCompetenceCourses();
		
		List<ExecutionPeriod> executionPeriodList = new ArrayList<ExecutionPeriod>();
		for(Integer executionPeriodID : executionPeriodsIDs){
			executionPeriodList.add((ExecutionPeriod) persistentObject.readByOID(ExecutionPeriod.class, executionPeriodID));
		}


		DistributionTeacherServicesByCourseDTO returnDTO = new DistributionTeacherServicesByCourseDTO();
		
		Map<Integer, Boolean> executionCoursesMap = new HashMap<Integer, Boolean>();

		for (CompetenceCourse cc : competenceCourseList) {
			for (CurricularCourse curricularCourseEntry : cc.getAssociatedCurricularCourses()) {

				for (ExecutionPeriod executionPeriodEntry : executionPeriodList) {

					Set<String> curricularYearsSet = buildCurricularYearsSet(curricularCourseEntry, executionPeriodEntry);

					for (ExecutionCourse executionCourseEntry : (List<ExecutionCourse>) curricularCourseEntry
							.getExecutionCoursesByExecutionPeriod(executionPeriodEntry)) {

						if (executionCoursesMap.containsKey(executionCourseEntry.getIdInternal())) {
							returnDTO.addDegreeNameToExecutionCourse(executionCourseEntry
									.getIdInternal(), curricularCourseEntry.getDegreeCurricularPlan().getDegree()
									.getSigla());
							returnDTO.addCurricularYearsToExecutionCourse(executionCourseEntry.getIdInternal(),
									curricularYearsSet);
							continue;
						}
						
						int executionCourseFirstTimeEnrollementStudentNumber = executionCourseEntry
								.getFirstTimeEnrolmentStudentNumber();
						int executionCourseSecondTimeEnrollementStudentNumber = executionCourseEntry
								.getSecondOrMoreTimeEnrolmentStudentNumber();

						String campus = getCampusForCurricularCourseAndExecutionPeriod(
								curricularCourseEntry, executionPeriodEntry);

						returnDTO
								.addExecutionCourse(
										executionCourseEntry.getIdInternal(),
										executionCourseEntry.getNome(),
										campus,
										curricularCourseEntry.getDegreeCurricularPlan().getDegree()
												.getSigla(),
										curricularYearsSet,
										executionCourseEntry.getExecutionPeriod().getSemester(),
										executionCourseFirstTimeEnrollementStudentNumber,
										executionCourseSecondTimeEnrollementStudentNumber,
										executionCourseEntry.getTotalHours(ShiftType.TEORICA),
										executionCourseEntry.getTotalHours(ShiftType.PRATICA),
										executionCourseEntry.getTotalHours(ShiftType.LABORATORIAL),
										executionCourseEntry.getTotalHours(ShiftType.TEORICO_PRATICA),
										executionCourseEntry.getStudentsNumberByShift(ShiftType.TEORICA),
										executionCourseEntry.getStudentsNumberByShift(ShiftType.PRATICA),
										executionCourseEntry
												.getStudentsNumberByShift(ShiftType.LABORATORIAL),
										executionCourseEntry
												.getStudentsNumberByShift(ShiftType.TEORICO_PRATICA));

						fillExecutionCourseDTOWithTeachers(returnDTO, executionCourseEntry, department);

						executionCoursesMap.put(executionCourseEntry.getIdInternal(), true);

					}
				}
			}
		}

		ArrayList<ExecutionCourseDistributionServiceEntryDTO> returnArraylist = new ArrayList<ExecutionCourseDistributionServiceEntryDTO>();

		for (ExecutionCourseDistributionServiceEntryDTO teacherDTO : returnDTO.getExecutionCourseMap()
				.values()) {
			returnArraylist.add(teacherDTO);
		}

		Collections.sort(returnArraylist);

		return returnArraylist;
	}
	
	private Set<String> buildCurricularYearsSet(CurricularCourse curricularCourseEntry, ExecutionPeriod executionPeriodEntry) {
		
		List<CurricularCourseScope> scopesList = curricularCourseEntry
				.getActiveScopesInExecutionPeriod(executionPeriodEntry);
		
		if(scopesList.isEmpty()){
			scopesList = curricularCourseEntry.getActiveScopesIntersectedByExecutionPeriod(executionPeriodEntry);
		}
		
		Set<String> curricularYearsSet = new LinkedHashSet<String>();
		for (CurricularCourseScope scopeEntry : scopesList) {
			curricularYearsSet.add(curricularCourseEntry
					.getCurricularYearByBranchAndSemester(scopeEntry.getBranch(),
							scopeEntry.getCurricularSemester().getSemester()).getYear()
					.toString());
		}
		return curricularYearsSet;
	}

	private void fillExecutionCourseDTOWithTeachers(DistributionTeacherServicesByCourseDTO dto,
			ExecutionCourse executionCourse, Department department) {

		for (Professorship professorShipEntry : executionCourse.getProfessorships()) {
			Teacher teacher = professorShipEntry.getTeacher();

			Integer teacherIdInternal = teacher.getIdInternal();
			String teacherName = teacher.getPerson().getNome();
			Double teacherRequiredHours = StrictMath.ceil(teacher
					.getHoursLecturedOnExecutionCourse(executionCourse));

			boolean teacherBelongsToDepartment = false;

			// TODO: See this because this is not always true. Changes when
			// teacher changes department (e.g. In different
			// execution year/periods he might work in different departments...
			// (see new model)

			if (teacher.getLastWorkingDepartment() == department) {
				teacherBelongsToDepartment = true;
			}

			dto.addTeacherToExecutionCourse(executionCourse.getIdInternal(), teacherIdInternal,
					teacherName, teacherRequiredHours.intValue(), teacherBelongsToDepartment);
		}

	}

	private String getCampusForCurricularCourseAndExecutionPeriod(CurricularCourse curricularCourse,
			ExecutionPeriod executionPeriod) {
		String campus = "";

		for (ExecutionDegree executionDegreeEntry : curricularCourse.getDegreeCurricularPlan()
				.getExecutionDegrees()) {
			if (executionDegreeEntry.getExecutionYear() == executionPeriod.getExecutionYear()) {
				campus = executionDegreeEntry.getCampus().getName();
				break;
			}
		}

		return campus;
	}

}
