package net.sourceforge.fenixedu.presentationTier.Action.assiduousness;

import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import net.sourceforge.fenixedu.applicationTier.Executor;
import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.applicationTier.PersistenceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NotExecuteException;
import net.sourceforge.fenixedu.applicationTier.Servico.person.ServicoAutorizacaoLerPessoa;
import net.sourceforge.fenixedu.applicationTier.Servico.person.ServicoSeguroLerPessoa;
import net.sourceforge.fenixedu.constants.assiduousness.Constants;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.presentationTier.Action.sop.utils.SessionConstants;

import org.apache.struts.Globals;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

/*
 * @author Fernanda Quit�rio & T�nia Pous�o (ANTES: era o nosso LogonAction)
 */
public final class IndexAction extends Action {

    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {

        Locale locale = request.getLocale();

        ActionErrors errors = new ActionErrors();
        HttpSession session = request.getSession();

        IUserView userView = (IUserView) session.getAttribute(SessionConstants.U_VIEW);

        String username = userView.getUtilizador();//((LogonForm)

        ServicoAutorizacaoLerPessoa servicoAutorizacaoLerPessoa = new ServicoAutorizacaoLerPessoa();
        ServicoSeguroLerPessoa servicoSeguroLerPessoa = new ServicoSeguroLerPessoa(
                servicoAutorizacaoLerPessoa, username);

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

        Person pessoa = servicoSeguroLerPessoa.getPessoa();

        session.setMaxInactiveInterval(-1); // sessao nunca expira

        session.setAttribute(Constants.USERNAME, username);
        session.setAttribute(Globals.LOCALE_KEY, locale);
        session.setAttribute(Constants.USER_KEY, pessoa);

        // datas que v�o ser apresentadas enquanto o funcionario estiver ligado
        Calendar calendario = Calendar.getInstance();
        calendario.setLenient(false);
        Date data = new Date(calendario.getTimeInMillis());
        session.setAttribute(Constants.FIM_CONSULTA, data);

        int mes = calendario.get(Calendar.MONTH);
        calendario.add(Calendar.DAY_OF_MONTH, -15);
        calendario.setTimeInMillis(calendario.getTimeInMillis());
        if (mes == calendario.get(Calendar.MONTH)) {
            calendario.set(Calendar.DAY_OF_MONTH, 1);
        }
        data = new Date(calendario.getTimeInMillis());

        //se data inicio de consulta for antes de 1 de Maio, prevalece o 1 de
        // Maio
        Calendar calendarioInicioAplicacao = Calendar.getInstance();
        calendarioInicioAplicacao.setLenient(false);
        calendarioInicioAplicacao.clear();
        calendarioInicioAplicacao.set(2003, Calendar.MAY, 1, 00, 00, 00);
        if (calendario.before(calendarioInicioAplicacao)) {
            data = new Date(calendarioInicioAplicacao.getTimeInMillis());
        }

        session.setAttribute(Constants.INICIO_CONSULTA, data);

        return (mapping.findForward("ConsultarFuncionarioMostrarAction"));
    }
}