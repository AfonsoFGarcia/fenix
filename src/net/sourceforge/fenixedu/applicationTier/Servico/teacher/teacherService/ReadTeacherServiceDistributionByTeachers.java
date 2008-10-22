package net.sourceforge.fenixedu.applicationTier.Servico.teacher.teacherService;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import net.sourceforge.fenixedu.applicationTier.FenixService;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.teacher.distribution.DistributionTeacherServicesByTeachersDTO;
import net.sourceforge.fenixedu.dataTransferObject.teacher.distribution.DistributionTeacherServicesByTeachersDTO.TeacherDistributionServiceEntryDTO;
import net.sourceforge.fenixedu.domain.CurricularCourse;
import net.sourceforge.fenixedu.domain.CurricularCourseScope;
import net.sourceforge.fenixedu.domain.CurricularSemester;
import net.sourceforge.fenixedu.domain.Degree;
import net.sourceforge.fenixedu.domain.Department;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.ExecutionSemester;
import net.sourceforge.fenixedu.domain.ProfessionalSituationType;
import net.sourceforge.fenixedu.domain.Professorship;
import net.sourceforge.fenixedu.domain.Teacher;
import net.sourceforge.fenixedu.domain.organizationalStructure.PersonFunction;
import net.sourceforge.fenixedu.domain.teacher.TeacherServiceExemption;

/**
 * 
 * @author jpmsit, amak
 */
public class ReadTeacherServiceDistributionByTeachers extends FenixService {

    public List run(Integer departmentId, List<Integer> executionPeriodsIDs) throws FenixServiceException, ParseException {

	final List<ExecutionSemester> executionPeriodList = new ArrayList<ExecutionSemester>();
	for (Integer executionPeriodID : executionPeriodsIDs) {
	    executionPeriodList.add(rootDomainObject.readExecutionSemesterByOID(executionPeriodID));
	}

	final ExecutionSemester startPeriod = ExecutionSemester.readStartExecutionSemesterForCredits();

	ExecutionSemester endPeriod = findEndPeriod(executionPeriodList, startPeriod);

	DistributionTeacherServicesByTeachersDTO returnDTO = new DistributionTeacherServicesByTeachersDTO();

	Department department = rootDomainObject.readDepartmentByOID(departmentId);

	for (ExecutionSemester executionPeriodEntry : executionPeriodList) {

	    List<Teacher> teachers = department.getAllTeachers(executionPeriodEntry.getBeginDateYearMonthDay(),
		    executionPeriodEntry.getEndDateYearMonthDay());

	    for (Teacher teacher : teachers) {
		if (teacher.getCategory() == null) {
		    continue;
		}

		if (returnDTO.isTeacherPresent(teacher.getIdInternal())) {
		    returnDTO.addHoursToTeacher(teacher.getIdInternal(), teacher.getLessonHours(executionPeriodEntry));
		} else {
		    Double accumulatedCredits = (startPeriod == null ? 0.0 : teacher.getBalanceOfCreditsUntil(endPeriod));
		    returnDTO.addTeacher(teacher.getIdInternal(), teacher.getTeacherNumber(), teacher.getCategory().getCode(),
			    teacher.getPerson().getName(), teacher.getLessonHours(executionPeriodEntry), accumulatedCredits);
		}

		for (Professorship professorShip : teacher.getProfessorships()) {
		    ExecutionCourse executionCourse = professorShip.getExecutionCourse();

		    if (executionCourse.getExecutionPeriod() != executionPeriodEntry) {
			continue;
		    }

		    Map<Integer, String> degreeNameMap = new LinkedHashMap<Integer, String>();
		    Map<Integer, Set<String>> degreeCurricularYearsMap = new LinkedHashMap<Integer, Set<String>>();
		    for (CurricularCourse curricularCourse : executionCourse.getAssociatedCurricularCourses()) {
			Degree degree = curricularCourse.getDegreeCurricularPlan().getDegree();
			Integer degreeIdInternal = degree.getIdInternal();
			if (!degreeNameMap.containsKey(degreeIdInternal)) {
			    degreeNameMap.put(degreeIdInternal, degree.getSigla());
			    degreeCurricularYearsMap.put(degreeIdInternal, new LinkedHashSet<String>());
			}

			Set<String> curricularYears = new LinkedHashSet<String>();

			for (CurricularCourseScope curricularCourseScope : curricularCourse.getScopes()) {

			    if (curricularCourseScope.isActive(executionPeriodEntry.getEndDate())) {
				CurricularSemester curricularSemester = curricularCourseScope.getCurricularSemester();
				curricularYears.add(curricularSemester.getCurricularYear().getYear().toString());
			    }
			}
			degreeCurricularYearsMap.get(degreeIdInternal).addAll(curricularYears);
		    }

		    Double hoursSpentByTeacher = StrictMath.ceil(teacher.getHoursLecturedOnExecutionCourse(executionCourse));

		    returnDTO.addExecutionCourseToTeacher(teacher.getIdInternal(), executionCourse.getIdInternal(),
			    executionCourse.getNome(), hoursSpentByTeacher.intValue(), degreeNameMap, degreeCurricularYearsMap,
			    executionCourse.getExecutionPeriod().getName());

		}

		for (PersonFunction personFunction : teacher.getManagementFunctions(executionPeriodEntry)) {
		    returnDTO.addManagementFunctionToTeacher(teacher.getIdInternal(), personFunction.getFunction().getName(),
			    personFunction.getCredits());
		}

		double exemptionCredits = teacher.getServiceExemptionCredits(executionPeriodEntry);
		if (exemptionCredits > 0.0) {
		    List<TeacherServiceExemption> serviceExemptions = teacher
			    .getValidTeacherServiceExemptionsToCountInCredits(executionPeriodEntry);

		    List<ProfessionalSituationType> exemptionTypes = new ArrayList<ProfessionalSituationType>();
		    for (TeacherServiceExemption exemption : serviceExemptions) {
			exemptionTypes.add(exemption.getType());
		    }
		    returnDTO.addExemptionSituationToTeacher(teacher.getIdInternal(), exemptionTypes, exemptionCredits);
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

    private ExecutionSemester findEndPeriod(final List<ExecutionSemester> executionPeriodList, final ExecutionSemester startPeriod) {
	ExecutionSemester endPeriod = null;

	if (!executionPeriodList.isEmpty() && startPeriod != null) {
	    endPeriod = executionPeriodList.get(0);

	    for (ExecutionSemester executionPeriodEntry : executionPeriodList) {
		if (executionPeriodEntry.compareTo(endPeriod) < 0) {
		    endPeriod = executionPeriodEntry;
		}
	    }

	    endPeriod = endPeriod.getPreviousExecutionPeriod() == null ? startPeriod : endPeriod.getPreviousExecutionPeriod();
	    if (startPeriod.compareTo(endPeriod) > 0) {
		endPeriod = startPeriod;
	    }
	}
	return endPeriod;
    }
}
