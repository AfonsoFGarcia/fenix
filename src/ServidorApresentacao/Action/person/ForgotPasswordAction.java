package ServidorApresentacao.Action.person;

import java.io.IOException;
import java.util.ArrayList;
import java.util.ListIterator;

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

import Dominio.Pessoa;
import ServidorAplicacao.Executor;
import ServidorAplicacao.PersistenceException;
import ServidorAplicacao.Servico.assiduousness.ServicoAutorizacaoLer;
import ServidorAplicacao.Servico.assiduousness.ServicoSeguroPessoasGestaoAssiduidade;
import ServidorAplicacao.Servico.exceptions.NotExecuteException;
import ServidorAplicacao.Servico.person.ServicoAutorizacaoAlterarPessoa;
import ServidorAplicacao.Servico.person.ServicoAutorizacaoLerPessoa;
import ServidorAplicacao.Servico.person.ServicoSeguroAlterarPessoa;
import ServidorAplicacao.Servico.person.ServicoSeguroLerPessoa;
import ServidorAplicacao.security.PasswordEncryptor;
import ServidorApresentacao.formbeans.person.DadosForm;
import Util.EMail;
import Util.RandomPassword;

public final class ForgotPasswordAction extends Action {

	public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
		throws IOException, ServletException {

		HttpSession session = request.getSession();
		ActionErrors errors = new ActionErrors();

		if (isCancelled(request)) {
			if (mapping.getAttribute() != null)
				session.removeAttribute(mapping.getAttribute());
			return (mapping.findForward("Logoff"));
		}

		DadosForm passwordForm = (DadosForm) form;

		// pessoa que esta no login 
		ServicoAutorizacaoLerPessoa servicoAutorizacaoLerPessoa = new ServicoAutorizacaoLerPessoa();
		ServicoSeguroLerPessoa servicoSeguroLerPessoa = new ServicoSeguroLerPessoa(servicoAutorizacaoLerPessoa, passwordForm.getUsername());

		try {

			Executor.getInstance().doIt(servicoSeguroLerPessoa);

		} catch (NotExecuteException nee) {
			errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("error.execution"));
		} catch (PersistenceException pe) {
			errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("error.server"));
		} finally {
			if (!errors.isEmpty()) {
				saveErrors(request, errors);
				return (new ActionForward(mapping.getInput()));
			}
		}

		Pessoa pessoa = servicoSeguroLerPessoa.getPessoa();

		if (!(pessoa.getNumeroDocumentoIdentificacao()).equals(passwordForm.getNumeroDocumentoIdentificacao())) {
			errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("error.numeroIdentificacao.invalido"));
			saveErrors(request, errors);
			return (new ActionForward(mapping.getInput()));
		}
		
		//gera nova password para a pessoa
		String passwordNova = RandomPassword.getRandomPassword(8);
		pessoa.setPassword(PasswordEncryptor.encryptPassword(passwordNova));

		// obtem as pessoas que estao na gestao de assiduidade 
		ServicoAutorizacaoLer servicoAutorizacaoLer = new ServicoAutorizacaoLer();
		ServicoSeguroPessoasGestaoAssiduidade servicoSeguroPessoasGestaoAssiduidade = new ServicoSeguroPessoasGestaoAssiduidade(servicoAutorizacaoLer);

		try {

			Executor.getInstance().doIt(servicoSeguroPessoasGestaoAssiduidade);

		} catch (NotExecuteException nee) {
			errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("error.execution"));
		} catch (PersistenceException pe) {
			errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("error.server"));
		} finally {
			if (!errors.isEmpty()) {
				saveErrors(request, errors);
				return (new ActionForward(mapping.getInput()));
			}
		}
		ArrayList listaPessoas = servicoSeguroPessoasGestaoAssiduidade.getListaPessoas();
		
		
		if(pessoa.getEmail() != null && pessoa.getEmail().length() > 0){
			listaPessoas.add(pessoa);
		}
		ListIterator iterador = listaPessoas.listIterator();
		boolean enviado = false;
		
		// envia o mail de nova password para todas as pessoas de gestao de assiduidade e para a pessoa caso tenha email
		while(iterador.hasNext()){
			Pessoa pessoaDestino = (Pessoa) iterador.next();
			if (EMail
				.send(
					"mail.adm",
					"assiduidade",
					pessoaDestino.getEmail(),
					"Nova Password para acesso � Assiduidade do Funcion�rio n�mero " + passwordForm.getUsername(), 
					"A nova password do funcion�rio n�mero " + passwordForm.getUsername() + " �:      " + passwordNova)) {
						enviado = true;
					}			
		}
		
		if(enviado){
			ServicoAutorizacaoAlterarPessoa servicoAutorizacaoAlterarPessoa = new ServicoAutorizacaoAlterarPessoa();
			ServicoSeguroAlterarPessoa servicoSeguroAlterarPessoa = new ServicoSeguroAlterarPessoa(servicoAutorizacaoAlterarPessoa, pessoa);
			try {

				Executor.getInstance().doIt(servicoSeguroAlterarPessoa);

			} catch (NotExecuteException nee) {
				errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("error.server"));
			} catch (PersistenceException pe) {
				errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("error.server"));
			} finally {
				if (!errors.isEmpty()) {
					saveErrors(request, errors);
					saveToken(request);
					return (new ActionForward(mapping.getInput()));
				}
			}
		} else {
			errors.add("impossivel enviar email", new ActionError("error.enviarEmail"));

			saveErrors(request, errors);
			saveToken(request);
			return (new ActionForward(mapping.getInput()));
		}

		return (mapping.findForward("Logon"));
	}
}