package net.sourceforge.fenixedu.domain;

import net.sourceforge.fenixedu.fileSuport.INode;

/**
 * Created on 11/Fev/2003
 * 
 * @author Jo�o Mota ciapl Dominio
 * 
 */
public class ExecutionYear extends ExecutionYear_Base {

    /**
     * Constructor for ExecutionYear.
     */
    public ExecutionYear() {
    }

    /**
     * @param year
     */
    public ExecutionYear(String year) {
        this.setYear(year);
    }

    /**
     * @see java.lang.Object#equals(java.lang.Object)
     */
    public boolean equals(Object obj) {
        if (obj instanceof IExecutionYear) {
            final IExecutionYear executionYear = (IExecutionYear) obj;
            return getYear().equals(executionYear.getYear());
        }
        return super.equals(obj);
    }

    public String toString() {
        String result = "[EXECUTION_YEAR";
        result += ", internalCode=" + getIdInternal();
        result += ", year=" + getYear();
        result += ", begin=" + getBeginDate();
        result += ", end=" + getEndDate();
        result += "]";
        return result;
    }

    public String getSlideName() {
        String result = "/EY" + getIdInternal();
        return result;
    }

    public INode getParentNode() {
        return null;
    }

}
