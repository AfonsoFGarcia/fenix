package ServidorApresentacao.Action.teacher;

import java.util.List;

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
import ServidorApresentacao.Action.base.FenixDispatchAction;
import ServidorApresentacao.Action.sop.utils.SessionConstants;
import ServidorApresentacao.Action.sop.utils.SessionUtils;

/**
 * @author Ivo Brand�o
 */
public class AnnouncementManagementDispatchAction extends FenixDispatchAction {

	public ActionForward prepareCreateAnnouncement(
		ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
		throws Exception {

			SessionUtils.validSessionVerification(request, mapping);
			HttpSession session = request.getSession(false);
			
			session.removeAttribute("insertAnnouncementForm");
			return mapping.findForward("insertAnnouncement");
	}

	public ActionForward createAnnouncement(
		ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		SessionUtils.validSessionVerification(request, mapping);
		HttpSession session = request.getSession(false);

		DynaActionForm insertAnnouncementForm = (DynaActionForm) form;
		String title = (String) insertAnnouncementForm.get("title");
		String information = (String) insertAnnouncementForm.get("information");

		InfoSite infoSite = (InfoSite) session.getAttribute(SessionConstants.INFO_SITE);
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

			//retrieve announcement
			List announcements = (List) session.getAttribute(SessionConstants.INFO_SITE_ANNOUNCEMENT_LIST);
			String announcementIndex = (String) request.getParameter("index");
			Integer index = new Integer(announcementIndex);
			InfoAnnouncement infoAnnouncement = (InfoAnnouncement) announcements.get(index.intValue());
			
			session.setAttribute("Announcement", infoAnnouncement);

			return mapping.findForward("editAnnouncement");
	}

	public ActionForward editAnnouncement(
		ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
		throws Exception {

			SessionUtils.validSessionVerification(request, mapping);
			HttpSession session = request.getSession(false);

			DynaActionForm insertAnnouncementForm = (DynaActionForm) form;
			String newTitle = (String) insertAnnouncementForm.get("title");
			String newInformation = (String) insertAnnouncementForm.get("information");

			InfoSite infoSite = (InfoSite) session.getAttribute(SessionConstants.INFO_SITE);
			InfoAnnouncement infoAnnouncement = (InfoAnnouncement) session.getAttribute("Announcement");
			UserView userView = (UserView) session.getAttribute(SessionConstants.U_VIEW);

			Object args[] = { infoSite, infoAnnouncement, newTitle, newInformation };
			GestorServicos manager = GestorServicos.manager();
			manager.executar(userView, "EditAnnouncement", args);                        

			//remove index from session
			session.removeAttribute("index");

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

			InfoSite infoSite = (InfoSite) session.getAttribute(SessionConstants.INFO_SITE);
			
			List announcements = (List) session.getAttribute(SessionConstants.INFO_SITE_ANNOUNCEMENT_LIST);
			String announcementIndex = (String) request.getParameter("index");
			Integer index = new Integer(announcementIndex);
			InfoAnnouncement infoAnnouncement = (InfoAnnouncement) announcements.get(index.intValue());

			UserView userView = (UserView) session.getAttribute(SessionConstants.U_VIEW);

			Object args[] = { infoSite, infoAnnouncement };
			GestorServicos manager = GestorServicos.manager();
			manager.executar(userView, "DeleteAnnouncement", args);
		
			//remove index from session
			session.removeAttribute("index");
		
			//return to announcementManagement
			return mapping.findForward("accessAnnouncementManagement");
	}

	public ActionForward showAnnouncements(
		ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
		throws Exception {

			List announcements = (List) request.getSession().getAttribute(SessionConstants.INFO_SITE_ANNOUNCEMENT_LIST);

			//return to announcementManagement
			return mapping.findForward("showAnnouncements");
	}

}