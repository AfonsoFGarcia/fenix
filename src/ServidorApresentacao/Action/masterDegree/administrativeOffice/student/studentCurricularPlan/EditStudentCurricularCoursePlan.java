package ServidorApresentacao
	.Action
	.masterDegree
	.administrativeOffice
	.student
	.studentCurricularPlan;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;
import org.apache.struts.actions.DispatchAction;

import DataBeans.InfoEnrolmentInExtraCurricularCourse;
import DataBeans.InfoStudentCurricularPlan;
import ServidorAplicacao.GestorServicos;
import ServidorAplicacao.Servico.UserView;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorApresentacao.Action.exceptions.FenixActionException;
import ServidorApresentacao.Action.sop.utils.SessionConstants;
import Util.StudentCurricularPlanState;

/**
 * @author Angela
 * Created on 8/Out/2003
 */
public class EditStudentCurricularCoursePlan extends DispatchAction {

	public ActionForward prepare(
		ActionMapping mapping,
		ActionForm form,
		HttpServletRequest request,
		HttpServletResponse response)
		throws Exception {
		HttpSession session = request.getSession(false);
		DynaActionForm editStudentCurricularPlanForm = (DynaActionForm) form;
		Integer studentCurricularPlanId =
			new Integer(getFromRequest("studentCurricularPlanId", request));
		UserView userView =
			(UserView) session.getAttribute(SessionConstants.U_VIEW);

		Object args[] = { studentCurricularPlanId };

		GestorServicos gestor = GestorServicos.manager();

		InfoStudentCurricularPlan infoStudentCurricularPlan = null;
		try {
			infoStudentCurricularPlan =
				(InfoStudentCurricularPlan) gestor.executar(
					userView,
					"ReadPosGradStudentCurricularPlanById",
					args);
		} catch (FenixServiceException e) {
			throw new FenixActionException(e);
		}

		//put request			
		request.setAttribute(
			SessionConstants.STATE,
			StudentCurricularPlanState.toArrayList());
		request.setAttribute(
			"student",
			infoStudentCurricularPlan.getInfoStudent());
		request.setAttribute(
			"studentCurricularPlan",
			infoStudentCurricularPlan);
		editStudentCurricularPlanForm.set(
			"currentState",
			infoStudentCurricularPlan.getCurrentState().toString());
		editStudentCurricularPlanForm.set(
			"credits",
			String.valueOf(infoStudentCurricularPlan.getGivenCredits()));

		String[] formValues =
			new String[infoStudentCurricularPlan.getInfoEnrolments().size()];
		int i = 0;
		for (Iterator iter =
			infoStudentCurricularPlan.getInfoEnrolments().iterator();
			iter.hasNext();
			) {
			Object enrollment =iter.next();
			if (enrollment instanceof InfoEnrolmentInExtraCurricularCourse) {
				Integer enrollmentId =
				((InfoEnrolmentInExtraCurricularCourse)enrollment).getIdInternal();
			formValues[i] = enrollmentId.toString();
			}  
			i++;
		}
		DynaActionForm coursesForm = (DynaActionForm) form;
		coursesForm.set("extraCurricularCourses", formValues);
		return mapping.findForward("editStudentCurricularCoursePlan");
	}

	public ActionForward edit(
		ActionMapping mapping,
		ActionForm form,
		HttpServletRequest request,
		HttpServletResponse response)
		throws Exception {

		HttpSession session = request.getSession(false);
		DynaActionForm editStudentCurricularPlanForm = (DynaActionForm) form;

		String studentCurricularPlanIdString =
			request.getParameter("studentCurricularPlanId");
		String[] extraCurricularCoursesArray =
			(String[]) editStudentCurricularPlanForm.get("extraCurricularCourses");

		String currentState =
			(String) editStudentCurricularPlanForm.get("currentState");
		Double credits =
			Double.valueOf(
				(String) editStudentCurricularPlanForm.get("credits"));

		Integer studentCurricularPlanId =
			new Integer(studentCurricularPlanIdString);
		UserView userView =
			(UserView) session.getAttribute(SessionConstants.U_VIEW);

		List extraCurricularCourses = new ArrayList();

		for (int i = 0; i < extraCurricularCoursesArray.length; i++) {
			extraCurricularCourses.add(
				new Integer(extraCurricularCoursesArray[i]));
			
		}
		Object args[] =
			{
				userView,
				studentCurricularPlanId,
				currentState,
				credits,
				extraCurricularCourses };

		GestorServicos gestor = GestorServicos.manager();

		try {
			 gestor.executar(
					userView,
					"EditPosGradStudentCurricularPlanStateAndCredits",
					args);
		} catch (FenixServiceException e) {
			throw new FenixActionException(e);
		}

		
		request.setAttribute(
			"studentCurricularPlanId",
			studentCurricularPlanId);

		return mapping.findForward("ShowStudentCurricularCoursePlan");
	}

	private String getFromRequest(
		String parameter,
		HttpServletRequest request) {
		String parameterString = request.getParameter(parameter);
		if (parameterString == null) {
			parameterString = (String) request.getAttribute(parameter);
		}
		return parameterString;
	}
}
