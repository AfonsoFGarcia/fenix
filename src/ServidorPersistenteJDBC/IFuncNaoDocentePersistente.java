package ServidorPersistenteJDBC;

import Dominio.NonTeacherEmployee;

/**
 * 
 * @author Fernanda Quit�rio e Tania Pous�o
 */
public interface IFuncNaoDocentePersistente {
    public boolean alterarFuncNaoDocente(NonTeacherEmployee funcionario);

    public boolean apagarFuncNaoDocente(int chaveFuncionario);

    public boolean escreverFuncNaoDocente(NonTeacherEmployee funcionario);

    public NonTeacherEmployee lerFuncNaoDocente(int codigoInterno);

    public NonTeacherEmployee lerFuncNaoDocentePorFuncionario(int chaveFuncionario);

    public NonTeacherEmployee lerFuncNaoDocentePorNumMecanografico(int numMecanografico);

    public NonTeacherEmployee lerFuncNaoDocentePorPessoa(int chavePessoa);

    public int ultimoCodigoInterno();
}