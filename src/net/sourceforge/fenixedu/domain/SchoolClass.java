/*
 * SchoolClass.java
 *
 * Created on 17 de Outubro de 2002, 19:07
 */

package net.sourceforge.fenixedu.domain;

import java.util.List;

/**
 * 
 * @author Luis Cruz & Sara Ribeiro
 */

public class SchoolClass extends SchoolClass_Base {
    private IExecutionDegree executionDegree;

    private IExecutionPeriod executionPeriod;

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
    public SchoolClass(String name, Integer semester, Integer curricularYear, IDegree degree) {
        setName(name);
        //	setSemestre(semestre);
        setCurricularYear(curricularYear);
        //	setLicenciatura(licenciatura);
    }

    public SchoolClass(String name, Integer curricularYear, IExecutionDegree executionDegree,
            IExecutionPeriod executionPeriod) {
        setName(name);
        setCurricularYear(curricularYear);
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
        result += ", nome=" + getName();
        result += ", executionPeriod=" + executionPeriod;
        result += ", executionDegree=" + executionDegree;
        result += "]";
        return result;
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
     * Sets the executionDegree.
     * 
     * @param executionDegree
     *            The executionDegree to set
     */
    public void setExecutionDegree(IExecutionDegree executionDegree) {
        this.executionDegree = executionDegree;
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
     * Sets the executionPeriod.
     * 
     * @param executionPeriod
     *            The executionPeriod to set
     */
    public void setExecutionPeriod(IExecutionPeriod executionPeriod) {
        this.executionPeriod = executionPeriod;
    }

}