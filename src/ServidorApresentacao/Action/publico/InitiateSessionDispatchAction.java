/*
 * Created on 6/Mar/2003
 *
 * To change this generated comment go to 
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
package ServidorApresentacao.Action.publico;

import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;
import org.apache.struts.util.LabelValueBean;

import DataBeans.InfoExecutionPeriod;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorApresentacao.Action.base.FenixDispatchAction;
import ServidorApresentacao.Action.exceptions.FenixActionException;
import ServidorApresentacao.Action.sop.utils.RequestUtils;
import ServidorApresentacao.Action.sop.utils.ServiceUtils;
import ServidorApresentacao.Action.sop.utils.SessionConstants;

/**
 * @author Nuno Nunes & David Santos
 *
 * To change this generated comment go to 
 * Window>Preferences>Java>Code Generation>Code and Comments
 */

public class InitiateSessionDispatchAction extends FenixDispatchAction {

	public ActionForward prepare(
		ActionMapping mapping,
		ActionForm form,
		HttpServletRequest request,
		HttpServletResponse response)
		throws Exception {

		HttpSession sessao = request.getSession(true);

		sessao.setAttribute(
			SessionConstants.SESSION_IS_VALID,
			new Boolean(true));

		/* Set in request ExecutionPeriods bean */
		Object argsReadExecutionPeriods[] = {
		};
		ArrayList executionPeriods;
		try {
			executionPeriods =
				(ArrayList) ServiceUtils.executeService(
					null,
					"ReadPublicExecutionPeriods",
					argsReadExecutionPeriods);
		} catch (FenixServiceException e) {
			throw new FenixActionException();
		}
		ArrayList executionPeriodsLabelValueList = new ArrayList();
		for (int i = 0; i < executionPeriods.size(); i++) {
			InfoExecutionPeriod infoExecutionPeriod =
				(InfoExecutionPeriod) executionPeriods.get(i);
			executionPeriodsLabelValueList.add(
				new LabelValueBean(
					infoExecutionPeriod.getName()
						+ " - "
						+ infoExecutionPeriod.getInfoExecutionYear().getYear(),
					"" + i));
		}
		if (executionPeriodsLabelValueList.size() > 1) {
			request.setAttribute(
				SessionConstants.LABELLIST_EXECUTIONPERIOD,
				executionPeriodsLabelValueList);
		} else {
			request.removeAttribute(SessionConstants.LABELLIST_EXECUTIONPERIOD);
		}
		/*------------------------------------*/

		// Keep (selected or current) executionPeriod in request.
		// If executionPeriod was previously selected,form has that value as default
		InfoExecutionPeriod selectedExecutionPeriod =
			RequestUtils.setExecutionContext(request);
		RequestUtils.setExecutionPeriodToRequest(
			request,
			selectedExecutionPeriod);

		if (selectedExecutionPeriod != null) {
			DynaActionForm indexForm = (DynaActionForm) form;
			indexForm.set(
				"index",
				new Integer(
					executionPeriods.indexOf((selectedExecutionPeriod))));
		}
		//----------------------------------------------------------		

		return mapping.findForward("showForm");
	}

	public ActionForward choose(
		ActionMapping mapping,
		ActionForm form,
		HttpServletRequest request,
		HttpServletResponse response)
		throws Exception {
		HttpSession session = request.getSession(false);
		DynaActionForm indexForm = (DynaActionForm) form;

		Object argsReadExecutionPeriods[] = {
		};
		ArrayList infoExecutionPeriods;
		try {
			infoExecutionPeriods =
				(ArrayList) ServiceUtils.executeService(
					null,
					"ReadPublicExecutionPeriods",
					argsReadExecutionPeriods);
		} catch (FenixServiceException e) {
			throw new FenixActionException();
		}

		Integer index = (Integer) indexForm.get("index");
		if (infoExecutionPeriods != null && index != null) {
			InfoExecutionPeriod selectedExecutionPeriod =
				(InfoExecutionPeriod) infoExecutionPeriods.get(
					index.intValue());

			// Set selected executionPeriod in request
			RequestUtils.setExecutionPeriodToRequest(
				request,
				selectedExecutionPeriod);
		}

		return mapping.findForward("choose");
	}

}
