package net.sourceforge.fenixedu.applicationTier.Servico.assiduousness;

import net.sourceforge.fenixedu.domain.Funcionario;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.applicationTier.ServicoAutorizacao;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NotAuthorizeException;
import net.sourceforge.fenixedu.persistenceTierJDBC.IFuncNaoDocentePersistente;
import net.sourceforge.fenixedu.persistenceTierJDBC.IFuncionarioPersistente;
import net.sourceforge.fenixedu.persistenceTierJDBC.SuportePersistente;

/**
 * 
 * @author Fernanda Quit�rio & Tania Pous�o
 */
public class ServicoAutorizacaoPortalAssiduidade extends ServicoAutorizacao {
    private Person _pessoa;

    public ServicoAutorizacaoPortalAssiduidade(Person pessoa) {
        _pessoa = pessoa;
    }

    public void execute() throws NotAuthorizeException {
        IFuncionarioPersistente iFuncionarioPersistente = SuportePersistente.getInstance()
                .iFuncionarioPersistente();
        IFuncNaoDocentePersistente iFuncNaoDocentePersistente = SuportePersistente.getInstance()
                .iFuncNaoDocentePersistente();
        Funcionario funcionario = null;
        if ((funcionario = iFuncionarioPersistente.lerFuncionarioSemHistoricoPorPessoa(_pessoa
                .getIdInternal().intValue())) == null) {
            throw new NotAuthorizeException("error.semAutorizacao");
        }
        if (iFuncNaoDocentePersistente.lerFuncNaoDocentePorNumMecanografico(funcionario
                .getNumeroMecanografico()) == null) {
            throw new NotAuthorizeException("error.semAutorizacao");
        }

    }
}