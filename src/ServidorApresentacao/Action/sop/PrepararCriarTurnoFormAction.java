package ServidorApresentacao.Action.sop;

import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.util.LabelValueBean;

import ServidorApresentacao.Action.sop.utils.SessionConstants;
import ServidorApresentacao.Action.sop.utils.SessionUtils;
import Util.TipoAula;

/**
 * @author tfc130
*/
public class PrepararCriarTurnoFormAction extends Action {
  public ActionForward execute(ActionMapping mapping, ActionForm form,
                                HttpServletRequest request,
                                HttpServletResponse response)
      throws Exception {
		
    HttpSession sessao = request.getSession(false);
    if (sessao != null) {

		/** to be sure that execution course list is in session */
		SessionUtils.getExecutionCourses(request);
        ArrayList tiposAula = new ArrayList();
        tiposAula.add(new LabelValueBean("escolher", ""));
        tiposAula.add(new LabelValueBean("Teorica", (new Integer(TipoAula.TEORICA)).toString() ));
        tiposAula.add(new LabelValueBean("Pratica", (new Integer(TipoAula.PRATICA)).toString() ));
        tiposAula.add(new LabelValueBean("Teorico-Pratica", (new Integer(TipoAula.TEORICO_PRATICA)).toString() ));
        tiposAula.add(new LabelValueBean("Laboratorial", (new Integer(TipoAula.LABORATORIAL)).toString() ));
        tiposAula.add(new LabelValueBean("Duvidas", (new Integer(TipoAula.DUVIDAS)).toString() ));
        tiposAula.add(new LabelValueBean("Reserva", (new Integer(TipoAula.RESERVA)).toString() ));
        request.setAttribute("tiposAula", tiposAula);
        
	  request.removeAttribute(SessionConstants.EXECUTION_COURSE_KEY);
	  request.removeAttribute(SessionConstants.CLASS_VIEW);
      return mapping.findForward("Sucesso");
    } else
      throw new Exception();  // nao ocorre... pedido passa pelo filtro Autorizacao 
  }
}
