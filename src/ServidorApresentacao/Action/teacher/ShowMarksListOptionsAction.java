package ServidorApresentacao.Action.teacher;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.DispatchAction;

import DataBeans.ISiteComponent;
import DataBeans.InfoSiteCommon;
import DataBeans.TeacherAdministrationSiteView;
import ServidorAplicacao.Servico.UserView;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorApresentacao.Action.exceptions.FenixActionException;
import ServidorApresentacao.Action.sop.utils.ServiceUtils;
import ServidorApresentacao.Action.sop.utils.SessionConstants;

/**
 * @author T�nia Pous�o
 *
 */
public class ShowMarksListOptionsAction extends DispatchAction {

	public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
		throws FenixActionException {

		HttpSession session = request.getSession(false);

		UserView userView = (UserView) session.getAttribute(SessionConstants.U_VIEW);

		Integer executionCourseCode = null;
		String executionCourseCodeString = request.getParameter("objectCode");
		if (executionCourseCodeString == null) {
			executionCourseCodeString = (String) request.getAttribute("objectCode");
		}
		executionCourseCode = new Integer(executionCourseCodeString);

		Integer examCode = null;
		String examCodeString = request.getParameter("examCode");
		if (examCodeString == null) {
			examCodeString = (String) request.getAttribute("examCode");
		}
		examCode = new Integer(examCodeString);

		ISiteComponent commonComponent = new InfoSiteCommon();
		Object[] args = { executionCourseCode, commonComponent, null, null, null, null };
		TeacherAdministrationSiteView siteView = null;
		try {
			siteView =
				(TeacherAdministrationSiteView) ServiceUtils.executeService(userView, "TeacherAdministrationSiteComponentService", args);

		} catch (FenixServiceException e) {
			throw new FenixActionException(e);
		}
		
		request.setAttribute("siteView", siteView);
		request.setAttribute("objectCode", ((InfoSiteCommon) siteView.getCommonComponent()).getExecutionCourse().getIdInternal());
		request.setAttribute("examCode", examCode);
		
		return mapping.findForward("showMarksListOptions");
	}
}