package ServidorAplicacao;

public abstract class ServicoSeguro implements IServicoAssiduidade {
    private ServicoAutorizacao _servicoAutorizacao;
    
    public ServicoSeguro(ServicoAutorizacao servicoAutorizacao) {
        _servicoAutorizacao = servicoAutorizacao;
    }
    
    public void execute() throws NotExecuteException {
    }
    
    public final void authorize() throws NotAuthorizeException {
      if(_servicoAutorizacao != null)
            _servicoAutorizacao.execute();
    }
}