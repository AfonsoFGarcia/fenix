/*
 * Created on 1/Ago/2003
 */
package ServidorApresentacao.Action.manager;

import java.util.Collections;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.beanutils.BeanComparator;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import DataBeans.InfoDegreeCurricularPlan;
import ServidorAplicacao.GestorServicos;
import ServidorAplicacao.Servico.UserView;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorAplicacao.Servico.exceptions.NonExistingServiceException;
import ServidorApresentacao.Action.base.FenixAction;
import ServidorApresentacao.Action.exceptions.FenixActionException;
import ServidorApresentacao.Action.exceptions.NonExistingActionException;
import ServidorApresentacao.Action.sop.utils.SessionConstants;

/**
 * @author lmac1
 */

public class ReadDegreeCurricularPlanAction extends FenixAction {

	public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws FenixActionException {

		HttpSession session = request.getSession(false);

		UserView userView = (UserView) session.getAttribute(SessionConstants.U_VIEW);

		Integer degreeCurricularPlanId = new Integer(request.getParameter("degreeCurricularPlanId"));

		Object args[] = { degreeCurricularPlanId };

		GestorServicos manager = GestorServicos.manager();
		InfoDegreeCurricularPlan infoDegreeCurricularPlan = null;

		try {
				infoDegreeCurricularPlan = (InfoDegreeCurricularPlan) manager.executar(userView, "ReadDegreeCurricularPlan", args);
				
		} catch (NonExistingServiceException e) {
			throw new NonExistingActionException("O plano curricular", e);
		} catch (FenixServiceException fenixServiceException) {
			throw new FenixActionException(fenixServiceException.getMessage());
		}
		// in case the degreeCurricularPlan really exists
		List curricularCourses = null;
		try {
				curricularCourses = (List) manager.executar(userView, "ReadCurricularCoursesByDegreeCurricularPlan", args);
				
		} catch (FenixServiceException e) {
			throw new FenixActionException(e);
		}
		Collections.sort(curricularCourses, new BeanComparator("name"));

		List executionDegrees = null;
		try {
				executionDegrees = (List) manager.executar(userView, "ReadExecutionDegreesByDegreeCurricularPlan", args);
				
		} catch (FenixServiceException e) {
			throw new FenixActionException(e);
		}
		Collections.sort(executionDegrees, new BeanComparator("infoExecutionYear.year"));

		request.setAttribute("curricularCoursesList", curricularCourses);
		request.setAttribute("executionDegreesList", executionDegrees);
		request.setAttribute("infoDegreeCurricularPlan", infoDegreeCurricularPlan);
		return mapping.findForward("viewDegreeCurricularPlan");
	}
}
