package ServidorAplicacao.Servico.person;

import Dominio.Funcionario;
import Dominio.Pessoa;
import ServidorAplicacao.NotExecuteException;
import ServidorAplicacao.ServicoAutorizacao;
import ServidorAplicacao.ServicoSeguro;
import ServidorPersistenteJDBC.IFuncionarioPersistente;
import ServidorPersistenteJDBC.IPessoaPersistente;
import ServidorPersistenteJDBC.SuportePersistente;

/**
 * @author Fernanda & T�nia
 *
 */
public class ServicoSeguroLerPessoaPorNumeroMecanografico extends ServicoSeguro {
	private Pessoa pessoa = null;
	private int numeroMecanografico;

	public ServicoSeguroLerPessoaPorNumeroMecanografico(ServicoAutorizacao servicoAutorizacao, int numeroMecanografico) {
		super(servicoAutorizacao);
		this.numeroMecanografico = numeroMecanografico;
	}

	public void execute() throws NotExecuteException {
		IFuncionarioPersistente iFuncionarioPersistente = SuportePersistente.getInstance().iFuncionarioPersistente();
		Funcionario funcionario = null;
		if ((funcionario = iFuncionarioPersistente.lerFuncionarioPorNumMecanografico(numeroMecanografico)) == null)
			throw new NotExecuteException();

		IPessoaPersistente iPessoaPersistente = SuportePersistente.getInstance().iPessoaPersistente();
		if ((pessoa = iPessoaPersistente.lerPessoa(funcionario.getChavePessoa())) == null)
			throw new NotExecuteException();
	}

	public Pessoa getPessoa() {
		return pessoa;
	}
}
