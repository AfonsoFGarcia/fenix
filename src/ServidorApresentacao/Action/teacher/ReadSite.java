/*
 * Created on 21/Mar/2003
 *
 * 
 */
package ServidorApresentacao.Action.teacher;

import java.util.Collections;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import DataBeans.InfoAnnouncement;
import DataBeans.InfoExecutionCourse;
import DataBeans.InfoSite;
import ServidorAplicacao.GestorServicos;
import ServidorAplicacao.Servico.UserView;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorApresentacao.Action.base.FenixAction;
import ServidorApresentacao.Action.exceptions.FenixActionException;
import ServidorApresentacao.Action.sop.utils.ServiceUtils;
import ServidorApresentacao.Action.sop.utils.SessionConstants;

/**
 * @author Jo�o Mota
 *
 * 
 */
public class ReadSite extends FenixAction {

	public ActionForward execute(
		ActionMapping mapping,
		ActionForm form,
		HttpServletRequest request,
		HttpServletResponse response)
		throws FenixActionException {

		HttpSession session = request.getSession(false);
		session.removeAttribute(SessionConstants.INFO_SECTION);
		UserView userView =
			(UserView) session.getAttribute(SessionConstants.U_VIEW);

//		InfoTeacher infoTeacher =
//			(InfoTeacher) session.getAttribute(SessionConstants.INFO_TEACHER);
//		List infoSites =
//			(List) session.getAttribute(SessionConstants.INFO_SITES_LIST);

		InfoSite site = null;


//		String index = (String) request.getParameter("index");

		
		try {
			
			InfoExecutionCourse infoExecutionCourse = new InfoExecutionCourse();
			infoExecutionCourse.setIdInternal(
				new Integer(request.getParameter("objectCode")));
			
			Object[] args = { infoExecutionCourse };
			try {
				site =
					(InfoSite) ServiceUtils.executeService(userView, "ReadExecutionCourseSite",args);
			} catch (FenixServiceException e) {
				throw new FenixActionException();
			}
		} catch (NumberFormatException e) {
			site = (InfoSite) session.getAttribute(SessionConstants.INFO_SITE);
			
		}
		
//		if (index != null) {
//			site = (InfoSite) infoSites.get((new Integer(index)).intValue());
//		} else {
//			site = (InfoSite) session.getAttribute(SessionConstants.INFO_SITE);
//		}

		session.setAttribute(SessionConstants.INFO_SITE, site);
		session.setAttribute(
			SessionConstants.ALTERNATIVE_SITE,
			site.getAlternativeSite());
		session.setAttribute(SessionConstants.MAIL, site.getMail());

		//Read last Anouncement
		Object []args = new Object[1];
		args[0] = site;
//TODO: the last announcemnet is to be removed
		InfoAnnouncement lastAnnouncement = null;
		GestorServicos manager = GestorServicos.manager();
		try {
			lastAnnouncement =
				(InfoAnnouncement) manager.executar(
					userView,
					"ReadLastAnnouncement",
					args);
			session.setAttribute(
				SessionConstants.LAST_ANNOUNCEMENT,
				lastAnnouncement);
		} catch (FenixServiceException fenixServiceException) {
			throw new FenixActionException(fenixServiceException.getMessage());
		}

		// read sections		
		List sections = null;
		try {
			sections = (List) manager.executar(userView, "ReadSections", args);
			Collections.sort(sections);
			session.setAttribute(SessionConstants.SECTIONS, sections);
		} catch (FenixServiceException fenixServiceException) {
			throw new FenixActionException(fenixServiceException.getMessage());
		}
		return mapping.findForward("viewSite");
	}

}
