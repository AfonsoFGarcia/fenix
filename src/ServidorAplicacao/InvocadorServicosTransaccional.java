package ServidorAplicacao;

/**
 * @author jorge
 **/

import ServidorAplicacao.Filtro.GestorFiltros;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;

public class InvocadorServicosTransaccional extends InvocadorServicos {

	public final static InvocadorServicosTransaccional invocador =
		new InvocadorServicosTransaccional();

	public final Object invoke(IUserView user, IServico servico, Object argumentos[], GestorFiltros filterChain)
		throws Exception {
		Object result = null;

		try {
			ISuportePersistente sp = SuportePersistenteOJB.getInstance();
			try {
				sp.iniciarTransaccao();
				filterChain.preFiltragem(user, servico, argumentos);
				result = doInvocation(servico, "run", argumentos);
				sp.confirmarTransaccao();
			} catch (Exception ex) {
					try {
						sp.cancelarTransaccao();
					} catch (ExcepcaoPersistencia newEx) {
						throw new FenixServiceException(newEx.getMessage());
					}
					throw ex;
					//throw new FenixServiceException(ex.getMessage());         	
			}
		} catch (ExcepcaoPersistencia ex) {
			throw new FenixServiceException(ex.getMessage());
		}

		return result;
	}
}
