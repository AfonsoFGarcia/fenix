package ServidorAplicacao.Servico.assiduousness;

import java.util.ArrayList;
import java.util.Date;

import Dominio.Funcionario;
import ServidorAplicacao.ServicoAutorizacao;
import ServidorAplicacao.ServicoSeguro;
import ServidorAplicacao.Servico.exceptions.NotExecuteException;
import ServidorPersistenteJDBC.IFuncionarioPersistente;
import ServidorPersistenteJDBC.IJustificacaoPersistente;
import ServidorPersistenteJDBC.SuportePersistente;

/**
 *
 * @author Fernanda Quit�rio & T�nia Pous�o
 */
public class ServicoSeguroLerJustificacoesComValidade extends ServicoSeguro {
	private int _numMecanografico;
	private Date _dataInicioEscolha = null;
	private Date _dataFimEscolha = null;
	private Funcionario _funcionario = null;
	private ArrayList _listaJustificacoes = null;

	public ServicoSeguroLerJustificacoesComValidade(
		ServicoAutorizacao servicoAutorizacaoLer,
		int numMecanografico,
		Date dataInicioEscolha,
		Date dataFimEscolha) {
		super(servicoAutorizacaoLer);
		_numMecanografico = numMecanografico;
		_dataInicioEscolha = dataInicioEscolha;
		_dataFimEscolha = dataFimEscolha;
	}

	public void execute() throws NotExecuteException {
		System.out.println("--->No ServicoSeguroLerJustificacoesComValidade...");

		IFuncionarioPersistente iFuncionarioPersistente =
			SuportePersistente.getInstance().iFuncionarioPersistente();
		
		if ((_funcionario =
			iFuncionarioPersistente.lerFuncionarioPorNumMecanografico(_numMecanografico))
			== null) {
			throw new NotExecuteException("error.funcionario.naoExiste");
		}

		IJustificacaoPersistente iJustificacaoPersistente =
			SuportePersistente.getInstance().iJustificacaoPersistente();
		
		_listaJustificacoes =
			iJustificacaoPersistente.lerJustificacoesFuncionarioComValidade(
				_funcionario.getCodigoInterno(),
				_dataInicioEscolha,
				_dataFimEscolha);
	}

	public ArrayList getListaJustificacoes() {
		return _listaJustificacoes;
	}
}