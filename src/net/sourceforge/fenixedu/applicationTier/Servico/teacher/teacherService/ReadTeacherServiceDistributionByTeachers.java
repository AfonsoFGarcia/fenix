package net.sourceforge.fenixedu.applicationTier.Servico.teacher.teacherService;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import net.sourceforge.fenixedu._development.PropertiesManager;
import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.teacher.distribution.DistributionTeacherServicesByTeachersDTO;
import net.sourceforge.fenixedu.dataTransferObject.teacher.distribution.DistributionTeacherServicesByTeachersDTO.TeacherDistributionServiceEntryDTO;
import net.sourceforge.fenixedu.domain.CurricularCourse;
import net.sourceforge.fenixedu.domain.CurricularCourseScope;
import net.sourceforge.fenixedu.domain.CurricularSemester;
import net.sourceforge.fenixedu.domain.Department;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.ExecutionPeriod;
import net.sourceforge.fenixedu.domain.Professorship;
import net.sourceforge.fenixedu.domain.Teacher;
import net.sourceforge.fenixedu.domain.organizationalStructure.PersonFunction;
import net.sourceforge.fenixedu.domain.teacher.TeacherServiceExemption;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.util.DateFormatUtil;

/**
 * 
 * @author jpmsit, amak
 */
public class ReadTeacherServiceDistributionByTeachers extends Service {
	

	public List run(String username, List<Integer> executionPeriodsIDs) throws FenixServiceException, ExcepcaoPersistencia, ParseException {		
		
		final List<ExecutionPeriod> executionPeriodList = new ArrayList<ExecutionPeriod>();
		for(Integer executionPeriodID : executionPeriodsIDs){
			executionPeriodList.add(rootDomainObject.readExecutionPeriodByOID(executionPeriodID));
		}
		
		final List<ExecutionPeriod> allExecutionPeriods = rootDomainObject.getExecutionPeriods();
		final ExecutionPeriod startPeriod = findStartPeriod(allExecutionPeriods);
		
		ExecutionPeriod endPeriod = findEndPeriod(executionPeriodList, startPeriod); 
		
		DistributionTeacherServicesByTeachersDTO returnDTO = new DistributionTeacherServicesByTeachersDTO();

		Department department = Teacher.readTeacherByUsername(username).getCurrentWorkingDepartment();
		
		for (ExecutionPeriod executionPeriodEntry : executionPeriodList) {
											
			List<Teacher> teachers = department.getTeachers(executionPeriodEntry.getBeginDate(), executionPeriodEntry.getEndDate());	
					
			for (Teacher teacher : teachers) {
				if(teacher.getCategory() == null){
					continue;
				}
				
				Double accumulatedCredits = (startPeriod == null ? 0.0 : teacher.getCreditsBetweenExecutionPeriods(startPeriod, endPeriod)); 
				
				if(returnDTO.isTeacherPresent(teacher.getIdInternal())){
					returnDTO.addHoursToTeacher(teacher.getIdInternal(), teacher.getHoursByCategory(executionPeriodEntry.getBeginDate(), executionPeriodEntry.getEndDate()));
					returnDTO.addCreditsToTeacher(teacher.getIdInternal(), teacher.getServiceExemptionCredits(executionPeriodEntry) + teacher.getManagementFunctionsCredits(executionPeriodEntry));					
				} else {
					returnDTO.addTeacher(teacher.getIdInternal(), teacher.getTeacherNumber(), teacher
							.getCategory().getCode(), teacher.getPerson().getNome(), teacher.getHoursByCategory(executionPeriodEntry.getBeginDate(), executionPeriodEntry.getEndDate()), 
							teacher.getServiceExemptionCredits(executionPeriodEntry) + teacher.getManagementFunctionsCredits(executionPeriodEntry), accumulatedCredits);
				}
					
				for (Professorship professorShip : teacher.getProfessorships()) {
					ExecutionCourse executionCourse = professorShip.getExecutionCourse();
		
					if (executionCourse.getExecutionPeriod() != executionPeriodEntry) {
						continue;
					}
		
					
					Map<Integer, String> degreeNameMap = new LinkedHashMap<Integer, String>();
					Map<Integer, Set<String>> degreeCurricularYearsMap = new LinkedHashMap<Integer, Set<String>>();
					for (CurricularCourse curricularCourse : executionCourse
							.getAssociatedCurricularCourses()) {
						String degreeName = curricularCourse.getDegreeCurricularPlan().getDegree().getSigla();
						Integer degreeIdInternal = curricularCourse.getDegreeCurricularPlan().getDegree().getIdInternal();
						if(!degreeNameMap.containsKey(degreeIdInternal)) {
							degreeNameMap.put(degreeIdInternal, degreeName);
							degreeCurricularYearsMap.put(degreeIdInternal, new LinkedHashSet<String>());
						}		
						
						Set<String> curricularYears = new LinkedHashSet<String>();
						
						for (CurricularCourseScope curricularCourseScope : curricularCourse.getScopes()) {
							
							if(curricularCourseScope.isActive(executionPeriodEntry.getEndDate())) {
								CurricularSemester curricularSemester = curricularCourseScope
										.getCurricularSemester();
								curricularYears.add(curricularSemester.getCurricularYear().getYear().toString());
							}
						}
						degreeCurricularYearsMap.get(degreeIdInternal).addAll(curricularYears);
					}
		
					Double hoursSpentByTeacher = StrictMath.ceil(teacher
							.getHoursLecturedOnExecutionCourse(executionCourse));
		
					returnDTO.addExecutionCourseToTeacher(teacher.getIdInternal(), executionCourse
							.getIdInternal(), executionCourse.getNome(), hoursSpentByTeacher.intValue(),
							degreeNameMap, degreeCurricularYearsMap, executionCourse.getExecutionPeriod().getName());
		
				}
				
				List<PersonFunction> teacherManagementFunctions = teacher.getManagementFunctions(executionPeriodEntry);
				
				
				for(PersonFunction personFunction: teacherManagementFunctions) {
					returnDTO.addManagementFunctionToTeacher(teacher.getIdInternal(), personFunction.getFunction().getName(), personFunction.getCredits());
				}
					
				List<TeacherServiceExemption> teacherServiceExemptions = teacher.getServiceExemptionSituations(executionPeriodEntry.getBeginDate(), executionPeriodEntry.getEndDate());
                
                TeacherServiceExemption teacherServiceExemption = teacher.chooseOneServiceExemption(teacherServiceExemptions, executionPeriodEntry);
				
                if(teacherServiceExemption != null) {
                    double exemptionCredits = (double) teacher.getCreditsForServiceExemption(executionPeriodEntry, teacherServiceExemption);
                    if(exemptionCredits > 0.0) {
                        returnDTO.addExemptionSituationToTeacher(teacher.getIdInternal(), teacherServiceExemption.getType().getName(), exemptionCredits);
                    }
				}
			}
		}

		ArrayList<TeacherDistributionServiceEntryDTO> returnArraylist = new ArrayList<TeacherDistributionServiceEntryDTO>();

		for (TeacherDistributionServiceEntryDTO teacher : returnDTO.getTeachersMap().values()) {
			returnArraylist.add(teacher);
		}

		Collections.sort(returnArraylist);

		return returnArraylist;
	}

	private ExecutionPeriod findEndPeriod(final List<ExecutionPeriod> executionPeriodList, final ExecutionPeriod startPeriod) {
		ExecutionPeriod endPeriod = null; 
		
		if(!executionPeriodList.isEmpty() && startPeriod != null){
			endPeriod = executionPeriodList.get(0);
			
			for (ExecutionPeriod executionPeriodEntry : executionPeriodList) {
				if(executionPeriodEntry.compareTo(endPeriod) < 0){
					endPeriod = executionPeriodEntry;
				}
			}
			
			endPeriod = endPeriod.getPreviousExecutionPeriod() == null ? startPeriod : endPeriod.getPreviousExecutionPeriod();
			if(startPeriod.compareTo(endPeriod) > 0) {
				endPeriod = startPeriod;
			}
		}
		return endPeriod;
	}

	private ExecutionPeriod findStartPeriod(final List<ExecutionPeriod> executionPeriods) throws ParseException {
		final String firstActiveExecutionPeriodString = PropertiesManager.getProperty("teacherServiceDistributionFirstActiveExecutionPeriodBeginDate");
		final Date firstActiveExecutionPeriod = DateFormatUtil.parse("yyyy-MM-dd", firstActiveExecutionPeriodString);
		for (final ExecutionPeriod executionPeriod : executionPeriods) {
			if (DateFormatUtil.equalDates("yyyy-MM-dd", firstActiveExecutionPeriod, executionPeriod.getBeginDate())) {
				return executionPeriod;
			}
		}
		return null;
	}

}
