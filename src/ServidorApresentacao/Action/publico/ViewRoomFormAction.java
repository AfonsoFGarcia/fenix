package ServidorApresentacao.Action.publico;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;

import DataBeans.InfoExecutionPeriod;
import DataBeans.InfoRoom;
import ServidorAplicacao.FenixServiceException;
import ServidorApresentacao.Action.base.FenixAction;
import ServidorApresentacao.Action.exceptions.FenixActionException;
import ServidorApresentacao.Action.sop.utils.RequestUtils;
import ServidorApresentacao.Action.sop.utils.ServiceUtils;

/**
 * @author tfc130
 */
public class ViewRoomFormAction extends FenixAction {

	public ActionForward execute(
		ActionMapping mapping,
		ActionForm form,
		HttpServletRequest request,
		HttpServletResponse response)
		throws FenixActionException {
		
		
		
		DynaActionForm indexForm = (DynaActionForm) form;
		
		HttpSession session = request.getSession(true);
		
			
			
			List infoRooms = (List) request.getAttribute("publico.infoRooms");
			String roomName = (String) indexForm.get("nome");
			
			InfoRoom argRoom = new InfoRoom();
			argRoom.setNome(roomName);
			Object[] args = {argRoom};
			
			
			InfoRoom infoRoom= null;
			List roomList;
			try {
				roomList =
					(List) ServiceUtils.executeService(
						null,
						"SelectRooms",
						args);
			} catch (FenixServiceException e1) {
				throw new FenixActionException(e1);
			}
			
			if (roomList!=null && !roomList.isEmpty()){
				infoRoom = (InfoRoom) roomList.get(0);
			}
			
			request.setAttribute("publico.infoRoom", infoRoom);


			InfoExecutionPeriod infoExecutionPeriod =RequestUtils.getExecutionPeriodFromRequest(request);
			Object argsReadLessons[] = { infoExecutionPeriod, infoRoom };

			List lessons;
			try {
				lessons =
					(List) ServiceUtils.executeService(
						null,
						"LerAulasDeSalaEmSemestre",
						argsReadLessons);
			} catch (FenixServiceException e) {
				throw new FenixActionException(e);
			}
			
			if (lessons != null) {
				request.setAttribute("lessonList", lessons);
			}
			  
			return mapping.findForward("Sucess");
		
}}