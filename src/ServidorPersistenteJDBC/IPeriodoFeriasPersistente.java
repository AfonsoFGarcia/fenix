package ServidorPersistenteJDBC;

import java.sql.Timestamp;
import java.util.List;

import Dominio.PeriodoFerias;

/**
 * 
 * @author Fernanda Quit�rio e Tania Pous�o
 */
public interface IPeriodoFeriasPersistente {
    public boolean alterarPeriodoFerias(PeriodoFerias periodoFerias);

    public boolean apagarPeriodoFerias(int codigoInterno);

    public boolean escreverPeriodoFerias(PeriodoFerias periodoFerias);

    public List historicoFeriasPorFuncionario(int numFuncionario);

    public PeriodoFerias lerPeriodoFerias(int codigoInterno);

    public List lerFuncionariosComFerias(Timestamp dataFerias);

    public List lerFeriasPorTipo(String tipoFerias);
}