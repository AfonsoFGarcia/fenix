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
 * @author  Fernanda Quit�rio & Tania Pous�o
 */
public class ServicoSeguroConsultarJustificacoesPorDia extends ServicoSeguro {
  
  private int _numMecanografico;
  private Date _dataEscolha = null;
  private Funcionario _funcionario = null;
  private ArrayList _listaJustificacoes = null;
  
  public ServicoSeguroConsultarJustificacoesPorDia(ServicoAutorizacao servicoAutorizacaoLer,
  int numMecanografico, Date dataEscolha) {
    super(servicoAutorizacaoLer);
    _numMecanografico = numMecanografico;
    _dataEscolha = dataEscolha;
  }
  
  public void execute() throws NotExecuteException {
    System.out.println("--->No ServicoSeguroConsultarJustificacoesPorDia...");
    
    IFuncionarioPersistente iFuncionarioPersistente = SuportePersistente.getInstance().iFuncionarioPersistente();
    if((_funcionario = iFuncionarioPersistente.lerFuncionarioPorNumMecanografico(_numMecanografico)) == null){
      throw new NotExecuteException("error.funcionario.naoExiste");
    }
    IJustificacaoPersistente iJustificacaoPersistente = SuportePersistente.getInstance().iJustificacaoPersistente();
		if ((_listaJustificacoes = iJustificacaoPersistente.lerJustificacoes(_funcionario.getCodigoInterno(), _dataEscolha)) == null) {
			throw new NotExecuteException("error.justificacao.naoExiste");
		}
  }

  public ArrayList getListaJustificacoes() {
    return _listaJustificacoes;
  }
}