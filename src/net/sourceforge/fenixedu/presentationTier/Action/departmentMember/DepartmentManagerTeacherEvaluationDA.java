package net.sourceforge.fenixedu.presentationTier.Action.departmentMember;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import pt.ist.fenixWebFramework.struts.annotations.Mapping;
import net.sourceforge.fenixedu.presentationTier.Action.teacher.evaluation.TeacherEvaluationDA;

@Mapping(module = "departmentMember", path = "/teacherEvaluation")
public class DepartmentManagerTeacherEvaluationDA extends TeacherEvaluationDA{
    
}
