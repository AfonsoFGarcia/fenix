package ServidorApresentacao.Action.sop;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;
import org.apache.struts.actions.DispatchAction;

import DataBeans.InfoExecutionPeriod;
import ServidorAplicacao.GestorServicos;
import ServidorAplicacao.IUserView;
import ServidorApresentacao.Action.sop.utils.ServiceUtils;
import ServidorApresentacao.Action.sop.utils.SessionConstants;
import ServidorApresentacao.Action.sop.utils.SessionUtils;

/**
 * @author Luis Cruz e Sara Ribeiro
 */
public class ViewAllRoomsSchedulesDA  extends DispatchAction {
	
	public ActionForward choose(
		ActionMapping mapping,
		ActionForm form,
		HttpServletRequest request,
		HttpServletResponse response)
		throws Exception {

		HttpSession session = request.getSession(false);
		if (session != null) {
			GestorServicos gestor = GestorServicos.manager();
			IUserView userView =
				(IUserView) session.getAttribute(SessionConstants.U_VIEW);

			InfoExecutionPeriod infoExecutionPeriod =
				setExecutionContext(request);

			/* Criar o bean de pavilhoes */				
			List pavillionsNamesList = new ArrayList();
			pavillionsNamesList.add("Pavilh�o Central");
			pavillionsNamesList.add("Pavilh�o Civil");
			pavillionsNamesList.add("Pavilh�o Electricidade");
			pavillionsNamesList.add("Pavilh�o P�s-Gradua��o");
			pavillionsNamesList.add("Pavilh�o Qu�mica");
			pavillionsNamesList.add("Pavilh�o Mec�nica I");
			pavillionsNamesList.add("Pavilh�o Mec�nica II");
			pavillionsNamesList.add("Pavilh�o Mec�nica III");
			pavillionsNamesList.add("Pavilh�o Minas");
			pavillionsNamesList.add("Pavilh�o Novas Licenciaturas");
			pavillionsNamesList.add("TagusPark - Bloco A - Poente");
			pavillionsNamesList.add("TagusPark - Bloco A - Nascente");
			pavillionsNamesList.add("TagusPark - Bloco B - Poente");
			pavillionsNamesList.add("TagusPark - Bloco B - Nascente");			
			pavillionsNamesList.add("Torre Norte");
			pavillionsNamesList.add("Torre Sul");
			
			request.setAttribute(
				SessionConstants.PAVILLIONS_NAMES_LIST,
				pavillionsNamesList);

			return mapping.findForward("choose");
		} else
			throw new Exception();
		// nao ocorre... pedido passa pelo filtro Autorizacao
	}
	
	
	public ActionForward list(
		ActionMapping mapping,
		ActionForm form,
		HttpServletRequest request,
		HttpServletResponse response)
		throws Exception {

		HttpSession session = request.getSession(false);
		if (session != null) {
			GestorServicos gestor = GestorServicos.manager();
			IUserView userView =
				(IUserView) session.getAttribute(SessionConstants.U_VIEW);
			DynaActionForm chooseViewAllRoomsSchedulesContextForm =
				(DynaActionForm) form;
				
			InfoExecutionPeriod infoExecutionPeriod = setExecutionContext(request);

			List pavillions = new ArrayList();
			pavillions.add("Pavilh�o Central");
			pavillions.add("Pavilh�o Civil");
			pavillions.add("Pavilh�o Electricidade");
			pavillions.add("Pavilh�o P�s-Gradua��o");
			pavillions.add("Pavilh�o Qu�mica");
			pavillions.add("Pavilh�o Mec�nica I");
			pavillions.add("Pavilh�o Mec�nica II");
			pavillions.add("Pavilh�o Mec�nica III");
			pavillions.add("Pavilh�o Minas");
			pavillions.add("Pavilh�o Novas Licenciaturas");
			pavillions.add("TagusPark - Bloco A - Poente");
			pavillions.add("TagusPark - Bloco A - Nascente");
			pavillions.add("TagusPark - Bloco B - Poente");
			pavillions.add("TagusPark - Bloco B - Nascente");			
			pavillions.add("Torre Norte");
			pavillions.add("Torre Sul");

			Boolean selectAllPavillions =
				(Boolean) chooseViewAllRoomsSchedulesContextForm.get(
					"selectAllPavillions");
			List selectedPavillions = null;
			if (selectAllPavillions != null && selectAllPavillions.booleanValue()) {
				selectedPavillions = pavillions;
			} else {
				String [] selectedPavillionsNames =
					(String[]) chooseViewAllRoomsSchedulesContextForm.get(
						"selectedPavillions");
				selectedPavillions = new ArrayList();
				for (int i = 0; i < selectedPavillionsNames.length; i++) {
					selectedPavillions.add(selectedPavillionsNames[i]);
				}
			}


			Object[] args =	{ selectedPavillions, infoExecutionPeriod };
			List infoViewClassScheduleList =
				(List) gestor.executar(
					userView,"ReadPavillionsRoomsLessons",	args);

			if (infoViewClassScheduleList != null
				&& infoViewClassScheduleList.isEmpty()) {
				request.removeAttribute(SessionConstants.ALL_INFO_VIEW_ROOM_SCHEDULE);
			} else {
				request.setAttribute(
					SessionConstants.ALL_INFO_VIEW_ROOM_SCHEDULE,
					infoViewClassScheduleList);
				request.setAttribute(
					SessionConstants.INFO_EXECUTION_PERIOD,
					infoExecutionPeriod);
			}

			return mapping.findForward("list");
		} else
			throw new Exception();
		// nao ocorre... pedido passa pelo filtro Autorizacao
	}
	
	/**
	 * Method setExecutionContext.
	 * @param request
	 */
	private InfoExecutionPeriod setExecutionContext(HttpServletRequest request)
		throws Exception {

		HttpSession session = request.getSession(false);
		InfoExecutionPeriod infoExecutionPeriod =
			(InfoExecutionPeriod) request.getAttribute(
				SessionConstants.INFO_EXECUTION_PERIOD_KEY);
		if (infoExecutionPeriod == null) {
			IUserView userView = SessionUtils.getUserView(request);
			infoExecutionPeriod =
				(InfoExecutionPeriod) ServiceUtils.executeService(
					userView,
					"ReadCurrentExecutionPeriod",
					new Object[0]);

			request.setAttribute(
				SessionConstants.INFO_EXECUTION_PERIOD_KEY,
				infoExecutionPeriod);
		}
		return infoExecutionPeriod;
	}
	
	
}
