package ServidorApresentacao.Action.sop;

import java.util.ArrayList;
import java.util.Calendar;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;

import DataBeans.InfoExecutionCourse;
import DataBeans.InfoLesson;
import ServidorAplicacao.GestorServicos;
import ServidorAplicacao.IUserView;
import ServidorApresentacao.Action.sop.base.FenixExecutionCourseAndExecutionDegreeAndCurricularYearContextAction;
import ServidorApresentacao.Action.sop.utils.RequestUtils;
import ServidorApresentacao.Action.sop.utils.SessionConstants;

/**
 * @author tfc130
 */
public class PrepararEditarAulaFormAction
	extends FenixExecutionCourseAndExecutionDegreeAndCurricularYearContextAction {

	public ActionForward execute(
		ActionMapping mapping,
		ActionForm form,
		HttpServletRequest request,
		HttpServletResponse response)
		throws Exception {

		super.execute(mapping, form, request, response);

		DynaActionForm editarAulaForm = (DynaActionForm) form;

		HttpSession sessao = request.getSession(false);
		if (sessao != null) {

			DynaActionForm manipularAulasForm =
				(DynaActionForm) request.getAttribute("manipularAulasForm");

			Integer indexAula = (Integer) manipularAulasForm.get("indexAula");

			IUserView userView = (IUserView) sessao.getAttribute("UserView");
			GestorServicos gestor = GestorServicos.manager();

			InfoExecutionCourse iDE =
				(InfoExecutionCourse) request.getAttribute(
					SessionConstants.EXECUTION_COURSE);

			Object argsLerAulas[] = new Object[1];
			argsLerAulas[0] = iDE;
			ArrayList infoAulas =
				(ArrayList) gestor.executar(
					userView,
					"LerAulasDeDisciplinaExecucao",
					argsLerAulas);

//			ArrayList infoAulas =
//				(ArrayList) request.getAttribute("listaAulas");
			InfoLesson infoAula =
				(InfoLesson) infoAulas.get(indexAula.intValue());

			//sessao.removeAttribute("indexAula");
			request.setAttribute("infoAula", infoAula);

			editarAulaForm.set(
				"diaSemana",
				String.valueOf(infoAula.getWeekDay()));
			editarAulaForm.set(
				"horaInicio",
				String.valueOf(infoAula.getInicio().get(Calendar.HOUR_OF_DAY)));
			editarAulaForm.set(
				"minutosInicio",
				String.valueOf(infoAula.getInicio().get(Calendar.MINUTE)));
			editarAulaForm.set(
				"horaFim",
				String.valueOf(infoAula.getFim().get(Calendar.HOUR_OF_DAY)));
			editarAulaForm.set(
				"minutosFim",
				String.valueOf(infoAula.getFim().get(Calendar.MINUTE)));
			editarAulaForm.set(
				"tipoAula",
				String.valueOf(infoAula.getTipo().getTipo().intValue()));
			editarAulaForm.set("nomeSala", infoAula.getInfoSala().getNome());

			RequestUtils.setLessonTypes(request);

			return mapping.findForward("Sucesso");
		} else
			throw new Exception();
		// nao ocorre... pedido passa pelo filtro Autorizacao
	}
}
