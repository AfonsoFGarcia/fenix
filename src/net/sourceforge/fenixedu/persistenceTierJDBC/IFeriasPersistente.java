package net.sourceforge.fenixedu.persistenceTierJDBC;

import java.util.List;

import net.sourceforge.fenixedu.domain.Ferias;

/**
 * 
 * @author Fernanda Quit�rio e Tania Pous�o
 */
public interface IFeriasPersistente {
    public boolean alterarFerias(Ferias ferias);

    public boolean apagarFerias(int codigoInterno);

    public boolean escreverFerias(Ferias ferias);

    public List HistoricoFeriasPorFuncionario(int numFuncionario);

    public Ferias lerFerias(int codigoInterno);

    public Ferias lerFeriasAnoPorFuncionario(int ano, int numMecanografico);

    public List lerFeriasPorAno(int ano);
}