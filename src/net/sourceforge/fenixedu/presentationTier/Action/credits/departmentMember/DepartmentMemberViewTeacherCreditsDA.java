package net.sourceforge.fenixedu.presentationTier.Action.credits.departmentMember;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.domain.credits.util.TeacherCreditsBean;
import net.sourceforge.fenixedu.domain.person.RoleType;
import net.sourceforge.fenixedu.presentationTier.Action.credits.ViewTeacherCreditsDA;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import pt.ist.fenixWebFramework.security.UserView;
import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;

@Mapping(module = "departmentMember", path = "/credits", scope = "request", parameter = "method")
@Forwards(value = { @Forward(name = "showTeacherCredits", path = "/credits/showTeacherCredits.jsp"),
	@Forward(name = "showPastTeacherCredits", path = "/credits/showPastTeacherCredits.jsp"),
	@Forward(name = "showAnnualTeacherCredits", path = "/credits/showAnnualTeacherCredits.jsp") })
public class DepartmentMemberViewTeacherCreditsDA extends ViewTeacherCreditsDA {

    @Override
    public ActionForward showTeacherCredits(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws NumberFormatException, FenixServiceException, Exception {
	final IUserView userView = UserView.getUser();
	if (userView.getPerson().getTeacher() != null) {
	    TeacherCreditsBean teacherBean = new TeacherCreditsBean(userView.getPerson().getTeacher());
	    teacherBean.prepareAnnualTeachingCredits();
	    request.setAttribute("teacherBean", teacherBean);
	}
	return mapping.findForward("showTeacherCredits");
    }

    @Override
    public ActionForward viewAnnualTeachingCredits(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws NumberFormatException, FenixServiceException, Exception {
	return viewAnnualTeachingCredits(mapping, form, request, response, RoleType.DEPARTMENT_MEMBER);
    }

}