package ServidorAplicacao.Servico.assiduousness;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

import Dominio.StatusAssiduidade;
import ServidorAplicacao.ServicoAutorizacao;
import ServidorAplicacao.ServicoSeguro;
import ServidorAplicacao.Servico.exceptions.NotExecuteException;
import ServidorPersistenteJDBC.IFuncionarioPersistente;
import ServidorPersistenteJDBC.SuportePersistente;

/**
 * 
 * @author Fernanda Quit�rio & Tania Pous�o
 */
public class ServicoSeguroLerStatusAssiduidadeFuncionario extends ServicoSeguro {

    private int numMecanografico;

    private Timestamp dataInicio = null;

    private Timestamp dataFim = null;

    private List listaStatusAssiduidade = null;

    public ServicoSeguroLerStatusAssiduidadeFuncionario(
            ServicoAutorizacao servicoAutorizacaoLerFuncionario, int numMecanografico,
            Timestamp dataInicio, Timestamp dataFim) {
        super(servicoAutorizacaoLerFuncionario);
        this.numMecanografico = numMecanografico;
        this.dataInicio = dataInicio;
        this.dataFim = dataFim;
    }

    public void execute() throws NotExecuteException {
        IFuncionarioPersistente iFuncionarioPersistente = SuportePersistente.getInstance()
                .iFuncionarioPersistente();
        if ((this.listaStatusAssiduidade = iFuncionarioPersistente.lerStatusAssiduidade(
                this.numMecanografico, this.dataInicio, this.dataFim)) == null) {
            throw new NotExecuteException("error.funcionario.naoExiste");
        }
    }

    public List getListaStatusAssiduidade() {
        return this.listaStatusAssiduidade;
    }

    public List getListaEstadosStatusAssiduidade() {
        ListIterator iteradorStatus = this.listaStatusAssiduidade.listIterator();
        List listaEstados = new ArrayList();
        while (iteradorStatus.hasNext()) {
            StatusAssiduidade status = (StatusAssiduidade) iteradorStatus.next();
            listaEstados.add(status.getEstado());
        }
        return listaEstados;
    }
}