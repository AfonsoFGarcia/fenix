/*
 * Created on 29/Mai/2003 by jpvl
 *
 */
package ServidorApresentacao.Action.teacher.credits;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;
import org.apache.struts.actions.DispatchAction;

import DataBeans.teacher.credits.InfoCredits;
import ServidorAplicacao.IUserView;
import ServidorApresentacao.Action.sop.utils.ServiceUtils;
import ServidorApresentacao.Action.sop.utils.SessionUtils;

/**
 * @author jpvl
 */
public class TeacherCreditsDispatchAction extends DispatchAction {

	public ActionForward prepare(
		ActionMapping mapping,
		ActionForm form,
		HttpServletRequest request,
		HttpServletResponse response)
		throws Exception {
		IUserView userView = SessionUtils.getUserView(request);
		DynaActionForm creditsForm = (DynaActionForm) form;
		Integer teacherOID =
			new Integer((String) creditsForm.get("teacherOID"));
		Object[] args = { teacherOID };

		InfoCredits infoCredits =
			(InfoCredits) ServiceUtils.executeService(
				userView,
				"ReadCreditsTeacher",
				args);

		DynaActionForm creditsTeacherForm = (DynaActionForm) form;
		Integer tfcStudentsNumber = infoCredits.getTfcStudentsNumber();
		if (tfcStudentsNumber != null && tfcStudentsNumber.intValue() != 0) {
			creditsTeacherForm.set(
				"tfcStudentsNumber",
				infoCredits.getTfcStudentsNumber());
		}
		request.setAttribute("infoCreditsTeacher", infoCredits);
		
		return mapping.findForward("showForm");
	}

	public ActionForward processForm(
		ActionMapping mapping,
		ActionForm form,
		HttpServletRequest request,
		HttpServletResponse response)
		throws Exception {
		IUserView userView = SessionUtils.getUserView(request);

		DynaActionForm creditsTeacherForm = (DynaActionForm) form;
		Integer tfcTfcStudentsNumber =
			(Integer) creditsTeacherForm.get("tfcStudentsNumber");
		Integer teacherOID =
			new Integer((String) creditsTeacherForm.get("teacherOID"));

		Object[] args = { teacherOID, tfcTfcStudentsNumber };

		ServiceUtils.executeService(userView, "WriteCreditsTeacher", args);

		return mapping.getInputForward();
	}

}
