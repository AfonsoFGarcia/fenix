package ServidorApresentacao.Action.teacher;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;

import DataBeans.gesdis.InfoAnnouncement;
import DataBeans.gesdis.InfoSite;
import ServidorAplicacao.GestorServicos;
import ServidorAplicacao.Servico.UserView;
import ServidorApresentacao.Action.base.FenixLookupDispatchAction;
import ServidorApresentacao.Action.sop.utils.SessionConstants;
import ServidorApresentacao.Action.sop.utils.SessionUtils;

/**
 * @author Ivo Brand�o
 */
public class AnnouncementManagementAction extends FenixLookupDispatchAction {

	public ActionForward createAnnouncement(
		ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		SessionUtils.validSessionVerification(request, mapping);
		HttpSession session = request.getSession(false);

		/*
		 * TODO:
		 * input .jsp
		 */
		DynaActionForm insertAnnouncementForm = (DynaActionForm) form;
		String title = (String) insertAnnouncementForm.get("title");
		String information = (String) insertAnnouncementForm.get("information");

		InfoSite infoSite = (InfoSite) session.getAttribute("Site");
		UserView userView = (UserView) session.getAttribute(SessionConstants.U_VIEW);

		Object args[] = { infoSite, title, information };
		GestorServicos manager = GestorServicos.manager();
		manager.executar(userView, "InsertAnnouncement", args);

		//return to announcementManagement
		return mapping.findForward("accessAnnouncementManagement");
	}

	public ActionForward prepareEditAnnouncement(
		ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
		throws Exception {

			SessionUtils.validSessionVerification(request, mapping);
			HttpSession session = request.getSession(false);

			return mapping.findForward("editAnnouncement");
	}

	public ActionForward editAnnouncement(
		ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
		throws Exception {

			SessionUtils.validSessionVerification(request, mapping);
			HttpSession session = request.getSession(false);

			DynaActionForm insertAnnouncementForm = (DynaActionForm) form;
			String newTitle = (String) insertAnnouncementForm.get("title");
			String information = (String) insertAnnouncementForm.get("information");

			InfoSite infoSite = (InfoSite) session.getAttribute("Site");
			InfoAnnouncement infoAnnouncement = (InfoAnnouncement) session.getAttribute("Announcement");
			UserView userView = (UserView) session.getAttribute(SessionConstants.U_VIEW);

			Object args[] = { infoSite, infoAnnouncement, newTitle, information };
			GestorServicos manager = GestorServicos.manager();
			manager.executar(userView, "EditAnnouncement", args);                        
		
			//return to announcementManagement
			return mapping.findForward("accessAnnouncementManagement");
	}

	public ActionForward deleteAnnouncement(
		ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
		throws Exception {

			/*
			 * session: Site, Announcement; 
			 * action: delete Announcement.
			 */
			SessionUtils.validSessionVerification(request, mapping);
			HttpSession session = request.getSession(false);

			InfoSite infoSite = (InfoSite) session.getAttribute("Site");
			InfoAnnouncement infoAnnouncement = (InfoAnnouncement) session.getAttribute("Announcement");

			UserView userView = (UserView) session.getAttribute(SessionConstants.U_VIEW);

			Object args[] = { infoSite, infoAnnouncement };
			GestorServicos manager = GestorServicos.manager();
			manager.executar(userView, "DeleteAnnouncement", args);
		
			//return to announcementManagement
			return mapping.findForward("accessAnnouncementManagement");
	}

	/**
	 * @see ServidorApresentacao.Action.FenixLookupDispatchAction#getKeyMethodMap()
	 */
	protected Map getKeyMethodMap() {
		Map map = new HashMap();
		map.put("button.insert", "createAnnouncement");
		map.put("button.delete", "deleteAnnouncement");
		map.put("button.edit", "prepareEditAnnouncement");

		map.put("button.save", "editAnnouncement");
		return map;
	}
}