package ServidorApresentacao.Action.publico;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import DataBeans.InfoExecutionCourse;
import DataBeans.gesdis.InfoSite;
import ServidorAplicacao.FenixServiceException;
import ServidorAplicacao.GestorServicos;
import ServidorApresentacao.Action.base.FenixAction;
import ServidorApresentacao.Action.exceptions.FenixActionException;
import ServidorApresentacao.Action.sop.utils.RequestUtils;

/**
 * @author ep15
 * @author Ivo Brand�o
 */
public class AccessBibliographyAction extends FenixAction {

	public ActionForward execute(ActionMapping mapping, ActionForm form,  
		HttpServletRequest request, HttpServletResponse response) throws Exception {

			

		
			HttpSession session = request.getSession(true);
			
			InfoSite infoSite =RequestUtils.getSiteFromRequest(request);

			InfoExecutionCourse infoExecutionCourse =
				infoSite.getInfoExecutionCourse();

			
			Object args[] = { infoExecutionCourse, null };
			GestorServicos gestor = GestorServicos.manager();
			List references = null;
			try {
				references =
					(ArrayList) gestor.executar(
						null,
						"ReadBibliographicReference",
						args);		
			} catch (FenixServiceException e) {
				throw new FenixActionException(e);
			}
		
			

			
				request.setAttribute(
								"BibliographicReferences",
								references);			
					
		
			RequestUtils.setExecutionCourseToRequest(request,infoExecutionCourse);
			RequestUtils.setSiteToRequest(request,infoSite);
			RequestUtils.setSectionsToRequest(request,infoSite);
			RequestUtils.setSectionToRequest(request);	
			return mapping.findForward("Sucess");
		}
}