package ServidorApresentacao.Action.teacher;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.beanutils.BeanComparator;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.DispatchAction;

import DataBeans.InfoExecutionCourseOccupancy;
import DataBeans.InfoSiteProjects;
import DataBeans.InfoSiteStudents;
import DataBeans.TeacherAdministrationSiteView;
import ServidorAplicacao.GestorServicos;
import ServidorAplicacao.Servico.UserView;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorApresentacao.Action.exceptions.FenixActionException;
import ServidorApresentacao.Action.sop.utils.SessionConstants;
/**
 * @author Fernanda Quit�rio
 * @author T�nia Pous�o 
 * @author �ngela
 *
 */
public class StudentsByCurricularCourseListAction extends DispatchAction
{
    public ActionForward readStudents(
        ActionMapping mapping,
        ActionForm form,
        HttpServletRequest request,
        HttpServletResponse response)
        throws FenixActionException
    {
        HttpSession session = request.getSession(false);
        Integer objectCode = null;
        Integer shiftID = null;
        try
        {
            shiftID = new Integer(request.getParameter("shiftCode"));
        } catch (NumberFormatException ex)
        {
            //ok, we don't want to view a shift's student list
        }
        String objectCodeString = request.getParameter("objectCode");
        if (objectCodeString == null)
        {
            objectCodeString = (String) request.getAttribute("objectCode");
        }
        objectCode = new Integer(objectCodeString);
        Integer scopeCode = null;
        String scopeCodeString = request.getParameter("scopeCode");
        if (scopeCodeString == null)
        {
            scopeCodeString = (String) request.getAttribute("scopeCode");
        }
        if (scopeCodeString != null)
        {
            scopeCode = new Integer(scopeCodeString);
        }
        UserView userView = (UserView) session.getAttribute(SessionConstants.U_VIEW);
        Object args[] = { objectCode, scopeCode };
        Object argsProjects[] = { objectCode };
        Object argsInfos[] = { objectCode };
        Object argsReadShifts[] = { objectCode };
        GestorServicos gestor = GestorServicos.manager();
        TeacherAdministrationSiteView siteView = null;
        InfoSiteProjects projects = null;
        List infosGroups = null;
        List attendacies = null;
        List shiftStudents = null;
        InfoExecutionCourseOccupancy shifts = null;
        try
        {
            siteView =
                (TeacherAdministrationSiteView) gestor.executar(
                    userView,
                    "ReadStudentsByCurricularCourse",
                    args);
            projects =
                (InfoSiteProjects) gestor.executar(
                    userView,
                    "teacher.ReadExecutionCourseProjects",
                    argsProjects);
            System.out.println("Tenho estes projectos:" + projects);
            infosGroups =
                (List) gestor.executar(
                    userView,
                    "teacher.GetProjectsGroupsByExecutionCourseID",
                    argsInfos);
            InfoSiteStudents infoSiteStudents = (InfoSiteStudents) siteView.getComponent();
            shifts =
                (InfoExecutionCourseOccupancy) gestor.executar(
                    userView,
                    "ReadShiftsByExecutionCourseID",
                    argsReadShifts);
            if (shiftID != null)
            {
                //the objectCode is needed by the filter...doing this is awfull !!!
                //please read http://www.dcc.unicamp.br/~oliva/fun/prog/resign-patterns
                Object[] argsReadShiftStudents = { objectCode, shiftID };
                shiftStudents =
                    (List) gestor.executar(
                        userView,
                        "teacher.ReadStudentsByShiftID",
                        argsReadShiftStudents);
                infoSiteStudents.setStudents(shiftStudents);
            }
            Collections.sort(infoSiteStudents.getStudents(), new BeanComparator("number"));
            Object[] argsAttendacies = { objectCode, infoSiteStudents.getStudents()};
            attendacies =
                (List) gestor.executar(userView, "teacher.GetAttendaciesByStudentList", argsAttendacies);

        } catch (FenixServiceException e)
        {
            throw new FenixActionException(e);
        }
        request.setAttribute("shifts", shifts);
        request.setAttribute("siteView", siteView);
        request.setAttribute("infosGroups", infosGroups);
        request.setAttribute("objectCode", objectCode);
        if (projects == null || projects.getInfoGroupPropertiesList() == null)
        {
            request.setAttribute("projects", new ArrayList());
        } else
        {
            request.setAttribute("projects", projects.getInfoGroupPropertiesList());
        }
        request.setAttribute("attendacies", attendacies);
        return mapping.findForward("success");
    }
}
