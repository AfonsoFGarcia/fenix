package ServidorApresentacao.Action.sop;

import java.util.Calendar;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import ServidorAplicacao.GestorServicos;
import ServidorAplicacao.IUserView;
import ServidorApresentacao.Action.sop.utils.SessionConstants;
import ServidorApresentacao.Action.sop.utils.SessionUtils;

/**
 * @author Luis Cruz e Sara Ribeiro
 */
public class ViewEmptyRoomsInDayAndShiftAction extends Action {

	public ActionForward execute(
		ActionMapping mapping,
		ActionForm form,
		HttpServletRequest request,
		HttpServletResponse response)
		throws Exception {

		HttpSession session = request.getSession(false);
		if (session != null) {
			GestorServicos gestor = GestorServicos.manager();
			IUserView userView = SessionUtils.getUserView(request);

			Calendar dateAndTime =
				(Calendar) session.getAttribute(
					SessionConstants.EXAM_DATEANDTIME);

			Calendar date = Calendar.getInstance();
			Calendar time = Calendar.getInstance();

			date.set(
				Calendar.DAY_OF_MONTH,
				dateAndTime.get(Calendar.DAY_OF_MONTH));
			date.set(Calendar.MONTH, dateAndTime.get(Calendar.MONTH));
			date.set(Calendar.YEAR, dateAndTime.get(Calendar.YEAR));
			date.set(Calendar.HOUR_OF_DAY, 0);
			date.set(Calendar.MINUTE, 0);
			date.set(Calendar.SECOND, 0);

			time.set(Calendar.DAY_OF_MONTH, 1);
			time.set(Calendar.MONTH, 1);
			time.set(Calendar.YEAR, 1970);
			time.set(
				Calendar.HOUR_OF_DAY,
				dateAndTime.get(Calendar.HOUR_OF_DAY));
			time.set(Calendar.MINUTE, 0);
			time.set(Calendar.SECOND, 0);

			// Chamar servico que vai ler salas vazias no dia escolhido
			Object[] args = { date, time };
			List infoRoomsList =
				(List) gestor.executar(
					userView,
					"ReadRoomsWithNoExamsInDayAndBeginning",
					args);

			if (infoRoomsList != null && infoRoomsList.isEmpty()) {
				request.removeAttribute(SessionConstants.INFO_EMPTY_ROOMS_KEY);
			} else {
				request.setAttribute(
					SessionConstants.INFO_EMPTY_ROOMS_KEY,
					infoRoomsList);
				request.setAttribute(
					"dateAndTime",
					session.getAttribute(
						SessionConstants.EXAM_DATEANDTIME_STR));
			}

			return mapping.findForward("View");
		} else
			throw new Exception();
		// nao ocorre... pedido passa pelo filtro Autorizacao
	}
}
