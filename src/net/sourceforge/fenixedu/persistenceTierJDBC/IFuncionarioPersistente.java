package net.sourceforge.fenixedu.persistenceTierJDBC;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

import net.sourceforge.fenixedu.domain.Funcionario;

/**
 * 
 * @author Fernanda Quit�rio e Tania Pous�o
 */
public interface IFuncionarioPersistente {

    // leituras sem hist�rico
    public Funcionario lerFuncionarioSemHistorico(int codigoInterno);

    public Funcionario lerFuncionarioSemHistoricoPorNumMecanografico(int numMecanografico);

    public Funcionario lerFuncionarioSemHistoricoPorPessoa(int chavePessoa);

    // leituras com hist�rico
    public Funcionario lerFuncionarioPorNumMecanografico(int numMecanografico, Date dataConsulta);

    public Funcionario lerFuncionarioPorPessoa(int chavePessoa, Date dataConsulta);

    // ler hist�ricos
    public List lerStatusAssiduidade(int numMecanografico, Timestamp dataInicio, Timestamp dataFim);

    // ler listas ade funcion�rios
    public List lerTodosFuncionarios(Date dataConsulta);

    public List lerTodosFuncionariosAssiduidade(Date dataConsulta);

    /***************************************************************************
     * Assiduidade do Funcionario
     **************************************************************************/
    public Timestamp lerFimAssiduidade(int numeroMecanografico);

    public Timestamp lerInicioAssiduidade(int numMecanografico);
}
