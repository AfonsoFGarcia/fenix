package ServidorAplicacao.Servico.assiduousness;

import java.util.ArrayList;

import ServidorAplicacao.NotExecuteException;
import ServidorAplicacao.ServicoAutorizacao;
import ServidorAplicacao.ServicoSeguro;
import ServidorPersistenteJDBC.IFuncionarioPersistente;
import ServidorPersistenteJDBC.SuportePersistente;

/**
 *
 * @author  Fernanda Quit�rio & Tania Pous�o
 */
public class ServicoSeguroLerTodosFuncionarios extends ServicoSeguro {
	private ArrayList _listaFuncionarios = null;

	public ServicoSeguroLerTodosFuncionarios(ServicoAutorizacao servicoAutorizacaoLer) {
		super(servicoAutorizacaoLer);
	}

	public void execute() throws NotExecuteException {
		IFuncionarioPersistente iFuncionarioPersistente = SuportePersistente.getInstance().iFuncionarioPersistente();
		if ((_listaFuncionarios = iFuncionarioPersistente.lerTodosFuncionarios()) == null) {
			throw new NotExecuteException("error.funcionario.naoExistem");
		}
	}

	public ArrayList getListaFuncionarios() {
		return _listaFuncionarios;
	}
}