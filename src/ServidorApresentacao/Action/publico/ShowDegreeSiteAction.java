package ServidorApresentacao.Action.publico;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import DataBeans.InfoDegreeInfo;
import ServidorAplicacao.GestorServicos;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorApresentacao.Action.base.FenixContextDispatchAction;
import ServidorApresentacao.Action.exceptions.FenixActionException;
import ServidorApresentacao.Action.sop.utils.SessionConstants;

/**
 * @author T�nia Pous�o Create on 11/Nov/2003
 */
public class ShowDegreeSiteAction extends FenixContextDispatchAction {

	public ActionForward showDescription(
		ActionMapping mapping,
		ActionForm actionForm,
		HttpServletRequest request,
		HttpServletResponse response)
		throws Exception {
			HttpSession session = request.getSession(true);

		Integer executionPeriodOId = getFromRequest("executionPeriodOId", request);
		Integer degreeId = getFromRequest("degreeId", request);

		GestorServicos gestorServicos = GestorServicos.manager();
		Object[] args = { executionPeriodOId, degreeId };

		InfoDegreeInfo infoDegreeInfo = null;
		try {
			infoDegreeInfo = (InfoDegreeInfo) gestorServicos.executar(null, "ReadDegreeInfoByDegreeAndExecutionPeriod", args);
		} catch (FenixServiceException e) {
			throw new FenixActionException(e);
		}

		System.out.println(infoDegreeInfo);
		
		request.setAttribute(SessionConstants.EXECUTION_PERIOD, executionPeriodOId);
		request.setAttribute("degreeId", degreeId);
		request.setAttribute("infoDegreeInfo", infoDegreeInfo);
		return mapping.findForward("showDescription");
	}

	public ActionForward showAccessRequirements(
		ActionMapping mapping,
		ActionForm actionForm,
		HttpServletRequest request,
		HttpServletResponse response)
		throws Exception {
		return mapping.findForward("showAccessRequirements");
	}

	public ActionForward showCurricularPlan(
		ActionMapping mapping,
		ActionForm actionForm,
		HttpServletRequest request,
		HttpServletResponse response)
		throws Exception {
		return mapping.findForward("showCurricularPlan");
	}

	private Integer getFromRequest(String parameter, HttpServletRequest request) {
		Integer parameterCode = null;
		String parameterCodeString = request.getParameter(parameter);
		if (parameterCodeString == null) {
			parameterCodeString = (String) request.getAttribute(parameter);
		}
		if (parameterCodeString != null) {
			parameterCode = new Integer(parameterCodeString);
		}
		return parameterCode;
	}
}
