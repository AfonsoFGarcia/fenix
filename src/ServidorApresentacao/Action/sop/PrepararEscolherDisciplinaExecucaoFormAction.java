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

import DataBeans.CurricularYearAndSemesterAndInfoExecutionDegree;
import DataBeans.InfoExecutionCourse;
import DataBeans.InfoExecutionDegree;
import ServidorAplicacao.GestorServicos;
import ServidorAplicacao.IUserView;

/**
 * @author tfc130
 */
public class PrepararEscolherDisciplinaExecucaoFormAction extends Action {

  public ActionForward execute(ActionMapping mapping, ActionForm form,
                                HttpServletRequest request,
                                HttpServletResponse response)
      throws Exception {
    
    HttpSession sessao = request.getSession(false);
    if (sessao != null) {
        IUserView userView = (IUserView) sessao.getAttribute("UserView");
        GestorServicos gestor = GestorServicos.manager();

		// Ler Disciplinas em Execucao
        InfoExecutionDegree iLE = (InfoExecutionDegree) sessao.getAttribute("infoLicenciaturaExecucao");
        Integer semestre = (Integer) sessao.getAttribute("semestre");
        Integer anoCurricular = (Integer) sessao.getAttribute("anoCurricular");

        Object argsLerDisciplinasExecucaoDeLicenciaturaExecucaoEAnoCurricular[] = { new CurricularYearAndSemesterAndInfoExecutionDegree(anoCurricular, semestre, iLE) };
		ArrayList infoDisciplinasExecucao = (ArrayList) gestor.executar(userView, "LerDisciplinasExecucaoDeLicenciaturaExecucaoEAnoCurricular", argsLerDisciplinasExecucaoDeLicenciaturaExecucaoEAnoCurricular);

        //Collections.sort(infoDisciplinasExecucao);

        ArrayList disciplinasExecucao = new ArrayList();
        disciplinasExecucao.add(new LabelValueBean("escolher", ""));
		for(int i = 0; i < infoDisciplinasExecucao.size(); i++) {
            InfoExecutionCourse elem = (InfoExecutionCourse) infoDisciplinasExecucao.get(i);
            disciplinasExecucao.add(new LabelValueBean(elem.getNome(), (new Integer( infoDisciplinasExecucao.indexOf(elem) + 1 )).toString()));			
		}

        sessao.setAttribute("disciplinasExecucao", disciplinasExecucao);
        sessao.setAttribute("infoDisciplinasExecucao", infoDisciplinasExecucao);    

      return mapping.findForward("Sucesso");
    } else
      throw new Exception();  // nao ocorre... pedido passa pelo filtro Autorizacao 
  }
}
