/*
 * Created on 7/Abr/2003
 *
 * To change this generated comment go to 
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
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
import ServidorAplicacao.Servico.exceptions.ExistingServiceException;
import ServidorApresentacao.Action.base.FenixAction;
import ServidorApresentacao.Action.exceptions.ExistingActionException;
import ServidorApresentacao.Action.exceptions.FenixActionException;
import ServidorApresentacao.Action.sop.utils.SessionConstants;

/**
 * @author CIIST
 *
 * To change this generated comment go to 
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
public class InsertSectionAction extends FenixAction {

	public ActionForward execute(
		ActionMapping mapping,
		ActionForm form,
		HttpServletRequest request,
		HttpServletResponse response)
		throws FenixActionException {

		DynaActionForm dynaForm = (DynaValidatorForm) form;
		String sectionName = (String) dynaForm.get("name");
		String order = (String) dynaForm.get("sectionOrder");

		HttpSession session = request.getSession();

		InfoSite infoSite =
			(InfoSite) session.getAttribute(SessionConstants.INFO_SITE);

		UserView userView =
			(UserView) session.getAttribute(SessionConstants.U_VIEW);
						
		InfoSection parentSection = (InfoSection) session.getAttribute(SessionConstants.INFO_SECTION);
		
		Integer sectionOrder = new Integer(order);
		
		ArrayList childrenSections =
			(ArrayList) session.getAttribute(
				SessionConstants.CHILDREN_SECTIONS);		

		if (sectionOrder.equals(new Integer(-1)))
			sectionOrder = new Integer(childrenSections.size());

		InfoSection infoSection =
			new InfoSection(sectionName, sectionOrder, infoSite, parentSection);

		Object args[] = { infoSection };
		GestorServicos manager = GestorServicos.manager();
		try {
			manager.executar(userView, "InsertSection", args);
		} 
		catch (ExistingServiceException e) {
						   throw new ExistingActionException("Uma sec��o com esse nome",e);
					   }
		catch (FenixServiceException fenixServiceException) {
			throw new FenixActionException(fenixServiceException.getMessage());
		}

		Object args1[] = { infoSite };
		ArrayList sections;
		try {
			sections =
				(ArrayList) manager.executar(userView, "ReadSections", args1);
		} catch (FenixServiceException fenixServiceException) {
			throw new FenixActionException(fenixServiceException.getMessage());
		}

		Collections.sort(sections);
		session.setAttribute(SessionConstants.SECTIONS, sections);
		return mapping.findForward("viewSite");
	}
	
	
			
}
