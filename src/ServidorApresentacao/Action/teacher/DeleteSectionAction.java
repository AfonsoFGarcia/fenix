/*
 * Created on 7/Abr/2003
 *
 * To change this generated comment go to 
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
package ServidorApresentacao.Action.teacher;

/**
 * @author lmac1
 *
 */
import java.util.Collections;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import DataBeans.gesdis.InfoSection;
import DataBeans.gesdis.InfoSite;
import ServidorAplicacao.FenixServiceException;
import ServidorAplicacao.GestorServicos;
import ServidorAplicacao.Servico.UserView;
import ServidorApresentacao.Action.base.FenixAction;
import ServidorApresentacao.Action.exceptions.FenixActionException;
import ServidorApresentacao.Action.sop.utils.SessionConstants;



public class DeleteSectionAction extends FenixAction{
	public ActionForward execute(
				ActionMapping mapping,
				ActionForm form,
				HttpServletRequest request,
				HttpServletResponse response)
				throws FenixActionException {
					
		HttpSession session = request.getSession(false);
	    UserView userView = (UserView) session.getAttribute(SessionConstants.U_VIEW);
		
		InfoSection infoSection = (InfoSection) session.getAttribute(SessionConstants.INFO_SECTION);
		InfoSection infoSuperiorSection = infoSection.getSuperiorInfoSection();
		
		
		try {
			Object deleteSectionArguments[] = { infoSection };
			GestorServicos manager = GestorServicos.manager();
			Boolean result = (Boolean) manager.executar(userView, "DeleteSection", deleteSectionArguments);

			session.removeAttribute(SessionConstants.INFO_SECTION);
			session.removeAttribute(SessionConstants.SECTIONS);
			
			InfoSite infoSite = infoSection.getInfoSite();
			Object readSectionsArguments[] = { infoSite };
			List allInfoSections = (List) manager.executar(null, "ReadSections", readSectionsArguments);
			
			Collections.sort(allInfoSections);
			session.setAttribute(SessionConstants.SECTIONS, allInfoSections);	
		
		    
        	if(infoSuperiorSection == null) { 
    
					return mapping.findForward("AccessSiteManagement");		
		   	}
		    else {
			        return mapping.findForward("AccessSectionManagement");
		          }
		   }
			catch (FenixServiceException fenixServiceException){
					   throw new FenixActionException(fenixServiceException.getMessage());
				   }
			
		}			
			
}
