/*
 * SchoolClass.java
 *
 * Created on 17 de Outubro de 2002, 19:07
 */

package Dominio;

import java.util.List;

/**
 * 
 * @author Luis Cruz & Sara Ribeiro
 */

public class SchoolClass extends DomainObject implements ISchoolClass {
    protected String _nome;

    protected Integer _anoCurricular;

    private IExecutionDegree executionDegree;

    private Integer keyExecutionDegree;

    private IExecutionPeriod executionPeriod;

    private Integer keyExecutionPeriod;

    private List associatedShifts;

    /**
     * Construtor sem argumentos p�blico requerido pela moldura de objectos
     * OJB
     */
    public SchoolClass() {
    }

    public SchoolClass(Integer idInternal) {
        setIdInternal(idInternal);
    }

    /**
     * @deprecated @param
     *             nome
     * @param semestre
     * @param anoCurricular
     * @param licenciatura
     */
    public SchoolClass(String nome, Integer semestre, Integer anoCurricular, IDegree licenciatura) {
        setNome(nome);
        //	setSemestre(semestre);
        setAnoCurricular(anoCurricular);
        //	setLicenciatura(licenciatura);
    }

    public SchoolClass(String nome, Integer anoCurricular, IExecutionDegree executionDegree,
            IExecutionPeriod executionPeriod) {
        setNome(nome);
        setAnoCurricular(anoCurricular);
        setExecutionDegree(executionDegree);
        setExecutionPeriod(executionPeriod);
    }

    public boolean equals(Object obj) {
        boolean resultado = false;
        if (obj instanceof ISchoolClass) {
            ISchoolClass turma = (ISchoolClass) obj;
            resultado = getIdInternal().equals(turma.getIdInternal());
        }
        return resultado;
    }

    public String toString() {
        String result = "[TURMA";
        result += ", codigoInterno=" + getIdInternal();
        result += ", nome=" + _nome;
        result += ", executionPeriod=" + executionPeriod;
        result += ", executionDegree=" + executionDegree;
        result += "]";
        return result;
    }

    /**
     * Returns the anoCurricular.
     * 
     * @return Integer
     */
    public Integer getAnoCurricular() {
        return _anoCurricular;
    }

    /**
     * Returns the nome.
     * 
     * @return String
     */
    public String getNome() {
        return _nome;
    }

    /**
     * Returns the executionDegree.
     * 
     * @return IExecutionDegree
     */
    public IExecutionDegree getExecutionDegree() {
        return executionDegree;
    }

    /**
     * Returns the keyExecutionDegree.
     * 
     * @return Integer
     */
    public Integer getKeyExecutionDegree() {
        return keyExecutionDegree;
    }

    /**
     * Sets the anoCurricular.
     * 
     * @param anoCurricular
     *            The anoCurricular to set
     */
    public void setAnoCurricular(Integer anoCurricular) {
        _anoCurricular = anoCurricular;
    }

    /**
     * Sets the nome.
     * 
     * @param nome
     *            The nome to set
     */
    public void setNome(String nome) {
        _nome = nome;
    }

    /**
     * Sets the executionDegree.
     * 
     * @param executionDegree
     *            The executionDegree to set
     */
    public void setExecutionDegree(IExecutionDegree executionDegree) {
        this.executionDegree = executionDegree;
    }

    /**
     * Sets the keyExecutionDegree.
     * 
     * @param keyExecutionDegree
     *            The keyExecutionDegree to set
     */
    public void setKeyExecutionDegree(Integer keyExecutionDegree) {
        this.keyExecutionDegree = keyExecutionDegree;
    }

    /**
     * Returns the executionPeriod.
     * 
     * @return IExecutionPeriod
     */
    public IExecutionPeriod getExecutionPeriod() {
        return executionPeriod;
    }

    /**
     * Returns the keyExecutionPeriod.
     * 
     * @return Integer
     */
    public Integer getKeyExecutionPeriod() {
        return keyExecutionPeriod;
    }

    /**
     * Sets the executionPeriod.
     * 
     * @param executionPeriod
     *            The executionPeriod to set
     */
    public void setExecutionPeriod(IExecutionPeriod executionPeriod) {
        this.executionPeriod = executionPeriod;
    }

    /**
     * Sets the keyExecutionPeriod.
     * 
     * @param keyExecutionPeriod
     *            The keyExecutionPeriod to set
     */
    public void setKeyExecutionPeriod(Integer keyExecutionPeriod) {
        this.keyExecutionPeriod = keyExecutionPeriod;
    }

    /**
     * @return
     */
    public List getAssociatedShifts() {
        return associatedShifts;
    }

    /**
     * @param list
     */
    public void setAssociatedShifts(List list) {
        associatedShifts = list;
    }

}