package ServidorApresentacao.Action.publico;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import DataBeans.ISiteComponent;
import DataBeans.InfoExecutionPeriod;
import DataBeans.InfoSiteClasses;
import DataBeans.SiteView;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorApresentacao.Action.base.FenixContextAction;
import ServidorApresentacao.Action.exceptions.FenixActionException;
import ServidorApresentacao.Action.sop.utils.ServiceUtils;
import ServidorApresentacao.Action.sop.utils.SessionConstants;

/**
 * @author Jo�o Mota
 */
public class ViewClassesAction extends FenixContextAction {

	/**
	 * @see org.apache.struts.action.Action#execute(org.apache.struts.action.ActionMapping, org.apache.struts.action.ActionForm, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	public ActionForward execute(
		ActionMapping mapping,
		ActionForm form,
		HttpServletRequest request,
		HttpServletResponse response)
		throws FenixActionException {
		try {
			super.execute(mapping, form, request, response);
		} catch (Exception e) {
			e.printStackTrace();
		}

		InfoExecutionPeriod infoExecutionPeriod =
			(InfoExecutionPeriod) request.getAttribute(
				SessionConstants.EXECUTION_PERIOD);
				
		String degreeInitials = (String) request.getAttribute("degreeInitials");
		String nameDegreeCurricularPlan =
			(String) request.getAttribute("nameDegreeCurricularPlan");
		Integer curricularYear = (Integer) request.getAttribute("curYear");

		ISiteComponent component = new InfoSiteClasses();
		SiteView siteView = null;
		Object[] args =
			{
				component,
				infoExecutionPeriod.getInfoExecutionYear().getYear(),
				infoExecutionPeriod.getName(),
				degreeInitials,
				nameDegreeCurricularPlan,
				null,
				curricularYear,null };

		try {
			siteView =
				(SiteView) ServiceUtils.executeService(
					null,
					"ClassSiteComponentService",
					args);
		} catch (FenixServiceException e1) {
			throw new FenixActionException(e1);
		}

		request.setAttribute("siteView", siteView);
		request.setAttribute("degreeInitials", degreeInitials);
		request.setAttribute(
			"nameDegreeCurricularPlan",
			nameDegreeCurricularPlan);
		return mapping.findForward("Sucess");

	}

}
