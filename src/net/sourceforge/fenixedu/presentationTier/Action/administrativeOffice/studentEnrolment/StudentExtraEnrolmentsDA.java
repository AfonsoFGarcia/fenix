package net.sourceforge.fenixedu.presentationTier.Action.administrativeOffice.studentEnrolment;

import net.sourceforge.fenixedu.dataTransferObject.administrativeOffice.studentEnrolment.StudentExtraEnrolmentBean;
import net.sourceforge.fenixedu.domain.ExecutionSemester;
import net.sourceforge.fenixedu.domain.StudentCurricularPlan;
import net.sourceforge.fenixedu.domain.studentCurriculum.NoCourseGroupCurriculumGroupType;
import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;
import pt.ist.fenixWebFramework.struts.annotations.Tile;

@Mapping(path = "/studentExtraEnrolments", module = "academicAdminOffice")
@Forwards({
	@Forward(name = "showExtraEnrolments", path = "/academicAdminOffice/showNoCourseGroupCurriculumGroupEnrolments.jsp", tileProperties = @Tile(title = "private.academicadministrativeoffice.studentoperations.viewstudents")),
	@Forward(name = "chooseExtraEnrolment", path = "/academicAdminOffice/chooseNoCourseGroupCurriculumGroupEnrolment.jsp", tileProperties = @Tile(title = "private.academicadministrativeoffice.studentoperations.viewstudents")),
	@Forward(name = "showDegreeModulesToEnrol", path = "/studentEnrolments.do?method=prepareFromExtraEnrolment")

})
public class StudentExtraEnrolmentsDA extends NoCourseGroupCurriculumGroupEnrolmentsDA {

    @Override
    protected StudentExtraEnrolmentBean createNoCourseGroupEnrolmentBean(final StudentCurricularPlan studentCurricularPlan,
	    final ExecutionSemester executionSemester) {
	return new StudentExtraEnrolmentBean(studentCurricularPlan, executionSemester);
    }

    @Override
    protected String getActionName() {
	return "studentExtraEnrolments";
    }

    @Override
    protected NoCourseGroupCurriculumGroupType getGroupType() {
	return NoCourseGroupCurriculumGroupType.EXTRA_CURRICULAR;
    }

}
