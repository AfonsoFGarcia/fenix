package ServidorApresentacao.Action.sop;
import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;

import DataBeans.InfoExecutionCourse;
import DataBeans.InfoShift;
import ServidorAplicacao.GestorServicos;
import ServidorAplicacao.IUserView;
import ServidorApresentacao.Action.FenixAction;
import ServidorApresentacao.Action.sop.utils.SessionConstants;
/**
 * @author tfc130
 */
public class EditarTurnoFormAction extends FenixAction {
	public ActionForward execute(
		ActionMapping mapping,
		ActionForm form,
		HttpServletRequest request,
		HttpServletResponse response)
		throws Exception {
		DynaActionForm editarTurnoForm = (DynaActionForm) form;
		HttpSession session = request.getSession(false);
		if (session != null) {
			IUserView userView =
				(IUserView) session.getAttribute(SessionConstants.U_VIEW);
			GestorServicos gestor = GestorServicos.manager();

			InfoExecutionCourse executionCourse =
				(InfoExecutionCourse) session.getAttribute(
					SessionConstants.EXECUTION_COURSE_KEY);

			ArrayList infoTurnos =
				(ArrayList) session.getAttribute(
					"infoTurnosDeDisciplinaExecucao");
			InfoShift infoTurnoAntigo =
				(InfoShift) session.getAttribute("infoTurno");
			
			InfoShift infoTurnoNovo =
				new InfoShift(
					(String) editarTurnoForm.get("nome"),
					infoTurnoAntigo.getTipo(),
					(Integer) editarTurnoForm.get("lotacao"),
					infoTurnoAntigo.getInfoDisciplinaExecucao());
			
			Object argsEditarTurno[] = { infoTurnoAntigo, infoTurnoNovo};
			
			Boolean result = Boolean.TRUE;
			ActionErrors actionErrors = null;
			try {
				 result = (Boolean) gestor.executar(userView, "EditarTurno", argsEditarTurno);
			} catch (Exception e) {
				//e.printStackTrace(System.out);
				actionErrors = new ActionErrors();
				actionErrors.add("error.shift.duplicate",new ActionError("error.shift.duplicate",infoTurnoNovo.getNome(), infoTurnoAntigo.getInfoDisciplinaExecucao().getNome()));
				editarTurnoForm.set("nome", infoTurnoAntigo.getNome());
				saveErrors(request, actionErrors);
			}
			
			if (actionErrors == null){			
				infoTurnos.set(infoTurnos.indexOf(infoTurnoAntigo), infoTurnoNovo);
				session.removeAttribute("indexTurno");
				return (mapping.findForward("Guardar"));
			}
			else {
				return mapping.getInputForward();
			}
		} else
			throw new Exception();
		// nao ocorre... pedido passa pelo filtro Autorizacao
	}
}
