package ServidorAplicacao.Servico.assiduousness;

import Dominio.Funcionario;
import Dominio.Person;
import ServidorAplicacao.ServicoAutorizacao;
import ServidorAplicacao.Servico.exceptions.NotAuthorizeException;
import ServidorPersistenteJDBC.IFuncNaoDocentePersistente;
import ServidorPersistenteJDBC.IFuncionarioPersistente;
import ServidorPersistenteJDBC.SuportePersistente;

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