package ServidorApresentacao.Action.manager.executionCourseManagement;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import ServidorApresentacao.Action.base.FenixDispatchAction;

/**
 * @author Fernanda Quit�rio 16/Dez/2003
 *  
 */
public class ExecutionCourseManagementAction extends FenixDispatchAction
{
	public ActionForward firstPage(
		ActionMapping mapping,
		ActionForm form,
		HttpServletRequest request,
		HttpServletResponse response)
	{
		return mapping.findForward("firstPage");
	}
	public ActionForward mainPage(
			ActionMapping mapping,
			ActionForm form,
			HttpServletRequest request,
			HttpServletResponse response)
	{
		return mapping.findForward("mainPage");
	}
}