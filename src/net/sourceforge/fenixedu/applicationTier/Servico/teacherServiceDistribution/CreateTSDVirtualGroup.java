package net.sourceforge.fenixedu.applicationTier.Servico.teacherServiceDistribution;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.FenixService;
import net.sourceforge.fenixedu.domain.DegreeCurricularPlan;
import net.sourceforge.fenixedu.domain.ExecutionSemester;
import net.sourceforge.fenixedu.domain.ShiftType;
import net.sourceforge.fenixedu.domain.teacherServiceDistribution.TSDCourse;
import net.sourceforge.fenixedu.domain.teacherServiceDistribution.TSDVirtualCourseGroup;
import net.sourceforge.fenixedu.domain.teacherServiceDistribution.TeacherServiceDistribution;

public class CreateTSDVirtualGroup extends FenixService {
    public TSDCourse run(String courseName, Integer tsdId, Integer periodId, String[] shiftTypesArray,
	    String[] degreeCurricularPlansIdArray) {

	TeacherServiceDistribution tsd = rootDomainObject.readTeacherServiceDistributionByOID(tsdId);
	ExecutionSemester executionSemester = rootDomainObject.readExecutionSemesterByOID(periodId);

	List<DegreeCurricularPlan> degreeCurricularPlansList = new ArrayList<DegreeCurricularPlan>();
	for (String planId : degreeCurricularPlansIdArray) {
	    degreeCurricularPlansList.add(rootDomainObject.readDegreeCurricularPlanByOID(Integer.parseInt(planId)));
	}

	List<ShiftType> lecturedShiftTypes = new ArrayList<ShiftType>();
	for (String typeStr : shiftTypesArray) {
	    lecturedShiftTypes.add(ShiftType.valueOf(typeStr));
	}

	return new TSDVirtualCourseGroup(tsd, courseName, executionSemester, lecturedShiftTypes, degreeCurricularPlansList);
    }
}
