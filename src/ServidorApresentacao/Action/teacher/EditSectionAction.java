package ServidorApresentacao.Action.teacher;

import java.util.ArrayList;
import java.util.Collections;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;
import org.apache.struts.validator.DynaValidatorForm;

import DataBeans.gesdis.InfoSection;
import DataBeans.gesdis.InfoSite;
import ServidorAplicacao.FenixServiceException;
import ServidorAplicacao.GestorServicos;
import ServidorAplicacao.Servico.UserView;
import ServidorApresentacao.Action.base.FenixAction;
import ServidorApresentacao.Action.exceptions.FenixActionException;
import ServidorApresentacao.Action.sop.utils.SessionConstants;

/**
 * @author Ivo Brand�o
 */
public class EditSectionAction extends FenixAction {

	public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
		throws FenixActionException {

		DynaActionForm dynaForm = (DynaValidatorForm) form;
		String sectionName = (String) dynaForm.get("name");
		Integer order = (Integer) dynaForm.get("order");

		HttpSession session = request.getSession();

		UserView userView = (UserView) session.getAttribute(SessionConstants.U_VIEW);
		InfoSite infoSite = (InfoSite) session.getAttribute(SessionConstants.INFO_SITE);
		InfoSection oldSection = (InfoSection) session.getAttribute(SessionConstants.INFO_SECTION);

		//TODO: change section parent

		InfoSection newSection = new InfoSection(sectionName, order, infoSite, oldSection.getSuperiorInfoSection());

		//perform edition
		Object editionArgs[] = { oldSection, newSection };
		GestorServicos manager = GestorServicos.manager();
		try {
			manager.executar(userView, "EditSection", editionArgs);
		} catch (FenixServiceException fenixServiceException) {
			throw new FenixActionException(fenixServiceException.getMessage());
		}

		//read sections
		Object readArgs[] = { infoSite };
		ArrayList sections;
		try {
			sections = (ArrayList) manager.executar(userView, "ReadSections", readArgs);
		} catch (FenixServiceException fenixServiceException) {
			throw new FenixActionException(fenixServiceException.getMessage());
		}

		Collections.sort(sections);
		session.setAttribute(SessionConstants.SECTIONS, sections);
			
		return mapping.findForward("viewSite");
	} 
}