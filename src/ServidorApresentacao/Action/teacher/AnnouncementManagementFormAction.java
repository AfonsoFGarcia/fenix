package ServidorApresentacao.Action.teacher;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;

import ServidorApresentacao.Action.FenixAction;
/**
 * @author ep15
 * @author Ivo Brand�o
 */
public class AnnouncementManagementFormAction extends FenixAction {
    public ActionForward execute(ActionMapping mapping, ActionForm form,	
    	HttpServletRequest request, HttpServletResponse response) throws Exception {
        HttpSession session = getSession(request);
		DynaActionForm announcementManagementForm = (DynaActionForm) session.getAttribute("announcementManagementForm");
		
        String option = (String) announcementManagementForm.get("option");
        int index = ((Integer) announcementManagementForm.get("index")).intValue();
        List announcements = (List) session.getAttribute("Announcements");
        if (option.equals("Editar")) {
            session.setAttribute("Announcement", announcements.get(index));
            return mapping.findForward("EditAnnouncement");
        }
        else if (option.equals("Apagar")) {
            session.setAttribute("Announcement", announcements.get(index));
            return mapping.findForward("DeleteAnnouncement");
        }
        else
            return mapping.findForward("InsertAnnouncement");
    }
}