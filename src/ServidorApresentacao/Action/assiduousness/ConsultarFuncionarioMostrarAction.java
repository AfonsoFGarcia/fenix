package ServidorApresentacao.Action.assiduousness;

import java.io.IOException;
import java.util.Date;
import java.util.Locale;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.util.MessageResources;

import Dominio.Funcionario;
import Dominio.Pessoa;
import ServidorAplicacao.Executor;
import ServidorAplicacao.PersistenceException;
import ServidorAplicacao.Servico.assiduousness.ServicoAutorizacaoLer;
import ServidorAplicacao.Servico.assiduousness.ServicoSeguroConsultarFuncionario;
import ServidorAplicacao.Servico.assiduousness.ServicoSeguroLerFuncionario;
import ServidorAplicacao.Servico.exceptions.NotAuthorizeException;
import ServidorAplicacao.Servico.exceptions.NotExecuteException;
import ServidorApresentacao.formbeans.assiduousness.ConsultarFuncionarioMostrarForm;
import constants.assiduousness.Constants;

/**
 *
 * @author  Fernanda Quit�rio & Tania Pous�o
 */
public final class ConsultarFuncionarioMostrarAction extends Action {
  
  public ActionForward execute(ActionMapping mapping,
  ActionForm form,
  HttpServletRequest request,
  HttpServletResponse response)
  throws IOException, ServletException {
    System.out.println("--->No ConsultarFuncionarioMostrarAction...");
    
    Locale locale = getLocale(request);
    MessageResources messages = getResources(request);
    ActionErrors errors = new ActionErrors();
    HttpSession session = request.getSession();
        
    Pessoa pessoa = (Pessoa)session.getAttribute(Constants.USER_KEY);
    
    ServicoAutorizacaoLer servicoAutorizacaoLer = new ServicoAutorizacaoLer();
    ServicoSeguroLerFuncionario servicoSeguroLerFuncionario =
    new ServicoSeguroLerFuncionario(servicoAutorizacaoLer, pessoa.getCodigoInterno().intValue());
    
    try {
      Executor.getInstance().doIt(servicoSeguroLerFuncionario);
      
    } catch (NotAuthorizeException nae) {
      errors.add(ActionErrors.GLOBAL_ERROR,
      new ActionError(nae.getMessage()));
    } catch (NotExecuteException nee) {
      errors.add(ActionErrors.GLOBAL_ERROR,
      new ActionError(nee.getMessage()));
    } catch (PersistenceException pe) {
      errors.add(ActionErrors.GLOBAL_ERROR,
      new ActionError("error.server"));
    } finally {
      if (!errors.isEmpty()) {
        saveErrors(request, errors);
        return (new ActionForward(mapping.getInput()));
      }
    }
    
    Funcionario funcionario = servicoSeguroLerFuncionario.getFuncionario();
    
    ServicoSeguroConsultarFuncionario servicoSeguroConsultarFuncionario =
    new ServicoSeguroConsultarFuncionario(servicoAutorizacaoLer, funcionario.getNumeroMecanografico());
    
    try {
      
      /* funcionario a consultar */
      Executor.getInstance().doIt(servicoSeguroConsultarFuncionario);
      
    } catch (NotAuthorizeException nae) {
      errors.add(ActionErrors.GLOBAL_ERROR,
      new ActionError(nae.getMessage()));
    } catch (NotExecuteException nee) {
      errors.add(ActionErrors.GLOBAL_ERROR,
      new ActionError(nee.getMessage()));
    } catch (PersistenceException pe) {
      errors.add(ActionErrors.GLOBAL_ERROR,
      new ActionError("error.server"));
    } finally {
      if (!errors.isEmpty()) {
        saveErrors(request, errors);
        return (new ActionForward(mapping.getInput()));
      }
    }

    session.setAttribute("numMecanografico", new Integer(funcionario.getNumeroMecanografico()));
    session.setAttribute("pessoa", servicoSeguroConsultarFuncionario.getPessoa());
    session.setAttribute("centroCusto", servicoSeguroConsultarFuncionario.getCentroCusto());

    /*    
    if (mapping.getAttribute() != null) {
      if ("request".equals(mapping.getScope()))
        request.removeAttribute(mapping.getAttribute());
      else
        session.removeAttribute(mapping.getAttribute());
    }
    */
    
    ConsultarFuncionarioMostrarForm funcNaoDocenteForm = (ConsultarFuncionarioMostrarForm) form;
    funcNaoDocenteForm.setForm((Date) session.getAttribute(Constants.INICIO_CONSULTA),
		(Date) session.getAttribute(Constants.FIM_CONSULTA), servicoSeguroConsultarFuncionario.getPessoa(), servicoSeguroConsultarFuncionario.getFuncionario(),
    servicoSeguroConsultarFuncionario.getStatusAssiduidade(), servicoSeguroConsultarFuncionario.getCentroCusto(), servicoSeguroConsultarFuncionario.getFuncNaoDocente(),
    servicoSeguroConsultarFuncionario.getRotacaoHorario(), servicoSeguroConsultarFuncionario.getListaRegimesRotacao());    
    return (mapping.findForward("ConsultarFuncionarioMostrar"));
  }
}