package ServidorApresentacao.Action.teacher;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.DispatchAction;

import DataBeans.TeacherAdministrationSiteView;
import ServidorAplicacao.GestorServicos;
import ServidorAplicacao.Servico.UserView;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorApresentacao.Action.exceptions.FenixActionException;

/**
 * @author T�nia Pous�o 
 * @author �ngela
 *
 */
public class ReadCurricularCourseListAction extends DispatchAction {

	public ActionForward read(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
		throws FenixActionException {
		HttpSession session = request.getSession();
		
		Integer objectCode = null;
		String objectCodeString = request.getParameter("objectCode");
		if (objectCodeString == null) {
			objectCodeString = (String) request.getAttribute("objectCode");
		}
		objectCode = new Integer(objectCodeString);

		UserView userView = (UserView) session.getAttribute("UserView");

		Object args[] = { objectCode };

		GestorServicos gestor = GestorServicos.manager();
		TeacherAdministrationSiteView siteView = null;
		try {
			siteView = (TeacherAdministrationSiteView) gestor.executar(userView, "ReadCurricularCourseListByExecutionCourseCode", args);
		} catch (FenixServiceException e) {
			throw new FenixActionException(e);
		}

		request.setAttribute("siteView", siteView);
		request.setAttribute("objectCode", objectCode);
			
		return mapping.findForward("success");
	}
}
