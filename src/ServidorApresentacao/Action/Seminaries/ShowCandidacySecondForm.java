/*
 * Created on 4/Ago/2003, 18:29:26
 *
 *By Goncalo Luiz gedl [AT] rnl [DOT] ist [DOT] utl [DOT] pt
 */
package ServidorApresentacao.Action.Seminaries;
import java.util.LinkedList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import DataBeans.Seminaries.InfoEquivalency;
import ServidorAplicacao.GestorServicos;
import ServidorAplicacao.IUserView;
import ServidorApresentacao.Action.base.FenixAction;
import ServidorApresentacao.Action.exceptions.FenixActionException;
import ServidorApresentacao.Action.sop.utils.SessionConstants;
/**
 * @author Goncalo Luiz gedl [AT] rnl [DOT] ist [DOT] utl [DOT] pt
 *
 * 
 * Created at 4/Ago/2003, 18:29:26
 * 
 */
public class ShowCandidacySecondForm extends FenixAction
{
	public ActionForward execute(
		ActionMapping mapping,
		ActionForm form,
		HttpServletRequest request,
		HttpServletResponse response)
		throws FenixActionException
	{
		HttpSession session= this.getSession(request);
		IUserView userView= (IUserView) session.getAttribute(SessionConstants.U_VIEW);
		//idInternal is equivalency's IdInternal
		String equivalencyIDString= (String) request.getParameter("idInternal");
		String themeIDString= (String) request.getParameter("themeID");
		Integer equivalencyID= null;
		Integer themeID= null;
		if (equivalencyIDString == null)
			throw new FenixActionException(mapping.findForward("invalidQueryString"));
		try
		{
			if (themeIDString != null)
				themeID= new Integer(themeIDString);
			equivalencyID= new Integer(equivalencyIDString);
		}
		catch (Exception ex)
		{
			throw new FenixActionException(mapping.findForward("invalidQueryString"));
		}
		InfoEquivalency equivalency= null;
		List cases= null;
		ActionForward destiny= null;
		try
		{
			Object[] argsReadEquivalency= { equivalencyID };
			GestorServicos gestor= GestorServicos.manager();
			equivalency=
				(InfoEquivalency) gestor.executar(userView, "Seminaries.GetEquivalency", argsReadEquivalency);
			
            //
			if (themeID != null) // we want the cases of ONE theme
            {
                Object[] argsReadCases= { themeID };
				cases= (List) gestor.executar(userView, "Seminaries.GetCaseStudiesByThemeID", argsReadCases);
            }
			else // we want ALL the cases of the equivalency (its a "Completa" modality)
            {
                Object[] argsReadCases= { equivalencyID };
				cases= (List) gestor.executar(userView, "Seminaries.GetCaseStudiesByEquivalencyID", argsReadCases);
            }
		}
		catch (Exception e)
		{
            throw new FenixActionException();
		}
		destiny= mapping.findForward("showCandidacyFormNonCompleteModalitySecondInfo");
		request.setAttribute("equivalency", equivalency);
		request.setAttribute("unselectedCases", cases);
		request.setAttribute("selectedCases", new LinkedList());
		request.setAttribute("hiddenSelectedCases", new LinkedList());
		return destiny;
	}
}
