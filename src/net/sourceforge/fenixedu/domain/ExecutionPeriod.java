package net.sourceforge.fenixedu.domain;

import net.sourceforge.fenixedu.fileSuport.INode;

/**
 * Created on 11/Fev/2003
 * 
 * @author Jo�o Mota
 * @author jpvl
 *  
 */
public class ExecutionPeriod extends ExecutionPeriod_Base {

    /**
     * Constructor for ExecutionPeriod.
     */
    public ExecutionPeriod() {
    }

    public ExecutionPeriod(Integer idInternal) {
        setIdInternal(idInternal);
    }

    public ExecutionPeriod(String name, IExecutionYear executionYear) {
        this.setName(name);
        this.setExecutionYear(executionYear);
    }

    public String toString() {
        String result = "[EXECUTION_PERIOD";
        result += ", internalCode=" + getIdInternal();
        result += ", name=" + getName();
        result += ", executionYear=" + getExecutionYear();
        result += ", begin Date=" + getBeginDate();
        result += ", end Date=" + getEndDate();
        result += "]\n";
        return result;
    }

    public boolean equals(Object obj) {
        if (obj instanceof IExecutionPeriod) {
            final IExecutionPeriod executionPeriod = (IExecutionPeriod) obj;
            return getIdInternal().equals(executionPeriod.getIdInternal());
        }
        return super.equals(obj);
    }

    public String getSlideName() {
        String result = getParentNode().getSlideName() + "/EP" + getIdInternal();
        return result;
    }

    public INode getParentNode() {
        IExecutionYear executionYear = getExecutionYear();
        return executionYear;
    }

    public int compareTo(Object o) {
        IExecutionPeriod executionPeriod = (IExecutionPeriod) o;
        String yearThis = this.getExecutionYear().getYear() + getSemester();
        String year = executionPeriod.getExecutionYear().getYear() + getSemester();
        return yearThis.compareTo(year);
    }

}