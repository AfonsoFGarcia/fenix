package ServidorApresentacao.Action.publico;

import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.util.LabelValueBean;

import Util.TipoSala;

/**
 * @author tfc130
 */
public class PrepareConsultRoomsFormAction extends Action {

  public ActionForward execute(ActionMapping mapping, ActionForm form,
                                HttpServletRequest request,
                                HttpServletResponse response)
      throws Exception {
    
    HttpSession sessao = request.getSession(false);
    if (sessao != null) {
    	/* No futuro, os edificios devem ser lidos da BD */
        ArrayList buildings = new ArrayList();
        buildings.add(new LabelValueBean("*", null));
        buildings.add(new LabelValueBean("Pavilh�o Central", "Pavilh�o Central"));
        buildings.add(new LabelValueBean("Pavilh�o Civil", "Pavilh�o Civil"));
        buildings.add(new LabelValueBean("Pavilh�o Mec�nica II", "Pavilh�o Mec�nica II"));
        buildings.add(new LabelValueBean("Pavilh�o Minas", "Pavilh�o Minas"));
        buildings.add(new LabelValueBean("Pavilh�o Novas Licenciaturas", "Pavilh�o Novas Licenciaturas"));
        buildings.add(new LabelValueBean("Torre Norte", "Torre Norte"));
        buildings.add(new LabelValueBean("Torre Sul", "Torre Sul"));
        buildings.add(new LabelValueBean("TagusPark - Bloco A - Poente", "TagusPark - Bloco A - Poente"));        
        buildings.add(new LabelValueBean("TagusPark - Bloco A - Nascente", "TagusPark - Bloco A - Nascente"));        
        buildings.add(new LabelValueBean("TagusPark - Bloco B - Poente", "TagusPark - Bloco B - Poente"));        
        buildings.add(new LabelValueBean("TagusPark - Bloco B - Nascente", "TagusPark - Bloco B - Nascente"));        
        sessao.setAttribute("publico.buildings", buildings);

        /* No futuro, os tipos de salas devem ser lidos da BD */
        ArrayList types = new ArrayList();
        types.add(new LabelValueBean("*", null));
        types.add(new LabelValueBean("Anfiteatro", (new Integer(TipoSala.ANFITEATRO)).toString()));
        types.add(new LabelValueBean("Laborat�rio", (new Integer(TipoSala.LABORATORIO)).toString()));
        types.add(new LabelValueBean("Plana", (new Integer(TipoSala.PLANA)).toString()));
        sessao.setAttribute("publico.types", types);
        
      return mapping.findForward("Sucess");
    } else
      throw new Exception(); 
  }
}
