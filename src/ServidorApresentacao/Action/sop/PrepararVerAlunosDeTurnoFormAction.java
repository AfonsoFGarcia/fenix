package ServidorApresentacao.Action.sop;
import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;

import DataBeans.InfoShift;
import DataBeans.ShiftKey;
import ServidorAplicacao.GestorServicos;
import ServidorAplicacao.IUserView;
/**
@author tfc130
*/
public class PrepararVerAlunosDeTurnoFormAction extends Action {
  public ActionForward execute(ActionMapping mapping, ActionForm form,
                                HttpServletRequest request,
                                HttpServletResponse response)
      throws Exception {
		
    HttpSession sessao = request.getSession(false);
    if (sessao != null) {
		DynaActionForm manipularTurnosForm = (DynaActionForm) sessao.getAttribute("manipularTurnosForm");
	    IUserView userView = (IUserView) sessao.getAttribute("UserView");
        GestorServicos gestor = GestorServicos.manager();
       
		Integer indexTurno = (Integer) manipularTurnosForm.get("indexTurno");
        ArrayList infoTurnos = (ArrayList) sessao.getAttribute("infoTurnosDeDisciplinaExecucao");
       
        InfoShift infoTurno = (InfoShift) infoTurnos.get(indexTurno.intValue());
       
        sessao.removeAttribute("infoTurno");
        sessao.setAttribute("infoTurno", infoTurno);
	    Object argsLerAlunosDeTurno[] = { new ShiftKey(infoTurno.getNome(), infoTurno.getInfoDisciplinaExecucao())};
        ArrayList infoAlunosDeTurno = (ArrayList) gestor.executar(userView, "LerAlunosDeTurno", argsLerAlunosDeTurno);
		sessao.removeAttribute("infoAlunosDeTurno");
		if (!infoAlunosDeTurno.isEmpty())
	        sessao.setAttribute("infoAlunosDeTurno", infoAlunosDeTurno);
      return mapping.findForward("Sucesso");
    } else
      throw new Exception();  // nao ocorre... pedido passa pelo filtro Autorizacao 
  }
}