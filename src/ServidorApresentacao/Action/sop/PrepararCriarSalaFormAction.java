package ServidorApresentacao.Action.sop;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import ServidorApresentacao.Action.base.FenixDispatchAction;
import ServidorApresentacao.Action.sop.utils.SessionUtils;
import ServidorApresentacao.Action.sop.utils.Util;

/**
 * @author tfc130
 */
public class PrepararCriarSalaFormAction extends FenixDispatchAction {

	/**
	 * Prepares the information for the form used to search salas.
	 **/
	public ActionForward prepareSearch(ActionMapping mapping, ActionForm form,
					 HttpServletRequest request, HttpServletResponse response)
		throws Exception {
			SessionUtils.validSessionVerification(request, mapping);
	  HttpSession sessao = getSession(request);

	  List edificios = Util.readExistingBuldings("*", null);
	  List tipos = Util.readTypesOfRooms("*", null);

	  sessao.setAttribute("publico.buildings", edificios);
	  sessao.setAttribute("publico.types", tipos);
  
	  return mapping.findForward("PesquisarSalas");
	}


	/**
	 * Prepares the information for the form used to create a room.
	 **/
	public ActionForward prepareCreate(ActionMapping mapping, ActionForm form,
					 HttpServletRequest request, HttpServletResponse response)
		throws Exception
	{
	  HttpSession sessao = getSession(request);
    
	  List edificios = Util.readExistingBuldings("escolher", "");
	  List tipos = Util.readTypesOfRooms("escolher", "");

	  sessao.setAttribute("publico.buildings", edificios);
	  sessao.setAttribute("publico.types", tipos);

	  return mapping.findForward("CriarSala");
	}
}
