/*
 * nao e utilizado
 */

package ServidorAplicacao.Servico.assiduousness;

import Dominio.FuncNaoDocente;
import ServidorAplicacao.ServicoAutorizacao;
import ServidorAplicacao.ServicoSeguro;
import ServidorAplicacao.Servico.exceptions.NotExecuteException;
import ServidorPersistenteJDBC.IFuncNaoDocentePersistente;
import ServidorPersistenteJDBC.SuportePersistente;

/**
 *
 * @author  Fernanda Quit�rio & Tania Pous�o
 */
public class ServicoSeguroLerFuncNaoDocente extends ServicoSeguro {
  
  private FuncNaoDocente _funcNaoDocente = null;
  private int _numMecanografico;
  
  public ServicoSeguroLerFuncNaoDocente(ServicoAutorizacao servicoAutorizacaoLerFuncNaoDocente,
  int numMecanografico) {
    super(servicoAutorizacaoLerFuncNaoDocente);
    _numMecanografico = numMecanografico;
  }
  
  public void execute() throws NotExecuteException {
    IFuncNaoDocentePersistente iFuncNaoDocentePersistente = SuportePersistente.getInstance().iFuncNaoDocentePersistente();
    if((_funcNaoDocente = iFuncNaoDocentePersistente.lerFuncNaoDocentePorNumMecanografico(_numMecanografico)) == null){
      throw new NotExecuteException("error.funcionario.naoExiste");
    }
  }
  
  public FuncNaoDocente getFuncNaoDocente() {
    return _funcNaoDocente;
  }
}