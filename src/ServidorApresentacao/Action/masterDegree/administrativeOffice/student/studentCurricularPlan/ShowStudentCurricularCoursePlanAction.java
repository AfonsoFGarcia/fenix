package ServidorApresentacao.Action.masterDegree.administrativeOffice.student.studentCurricularPlan;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.DispatchAction;

import DataBeans.InfoStudentCurricularPlan;
import ServidorAplicacao.GestorServicos;
import ServidorAplicacao.Servico.UserView;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorApresentacao.Action.exceptions.FenixActionException;

/**
 * @author T�nia Pous�o
 * Created on 6/Out/2003
 */
public class ShowStudentCurricularCoursePlanAction extends DispatchAction {

	public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		HttpSession session = request.getSession();

		Integer studentCurricularPlanId = null;
		String studentCurricularPlanIdString = request.getParameter("studentCurricularPlanId");
		if (studentCurricularPlanIdString == null) {
			studentCurricularPlanIdString = (String) request.getAttribute("studentCurricularPlanId");
		}
		studentCurricularPlanId = new Integer(studentCurricularPlanIdString);

		UserView userView = (UserView) session.getAttribute("UserView");

		Object args[] = { studentCurricularPlanId };

		GestorServicos gestor = GestorServicos.manager();

		InfoStudentCurricularPlan infoStudentCurricularPlan = null;
		try {
			infoStudentCurricularPlan = (InfoStudentCurricularPlan) gestor.executar(userView, "ReadPosGradStudentCurricularPlanById", args);
		} catch (FenixServiceException e) {
			throw new FenixActionException(e);
		}

		request.setAttribute("student", infoStudentCurricularPlan.getInfoStudent());
		request.setAttribute("studentCurricularPlan", infoStudentCurricularPlan);

		return mapping.findForward("ShowStudentCurricularCoursePlan");
	}
}
