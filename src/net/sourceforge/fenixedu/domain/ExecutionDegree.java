/*
 * ExecutionDegree.java
 *
 * Created on 2 de Novembro de 2002, 20:53
 */

package net.sourceforge.fenixedu.domain;


/**
 * 
 * @author rpfi
 */

public class ExecutionDegree extends ExecutionDegree_Base {
    private IPeriod periodLessonsFirstSemester;

    private IPeriod periodExamsFirstSemester;

    private IPeriod periodLessonsSecondSemester;

    private IPeriod periodExamsSecondSemester;

    /** Construtor sem argumentos publico requerido pela moldura de objectos OJB */
    public ExecutionDegree() {
    }

    public ExecutionDegree(IExecutionYear executionYear, IDegreeCurricularPlan curricularPlan) {
        setExecutionYear(executionYear);
        setDegreeCurricularPlan(curricularPlan);
    }

    /**
     * @param executionDegreeId
     */
    public ExecutionDegree(Integer executionDegreeId) {
        setIdInternal(executionDegreeId);
    }

    public boolean equals(Object obj) {
        boolean resultado = false;
        if (obj instanceof IExecutionDegree) {
            IExecutionDegree cursoExecucao = (IExecutionDegree) obj;
            resultado = getIdInternal().equals(cursoExecucao.getIdInternal());
        }
        return resultado;
    }

    public String toString() {
        String result = "[CURSO_EXECUCAO";
        result += ", codInt=" + getIdInternal();
        result += ", executionYear=" + getExecutionYear();
        //result += ", keyExecutionYear=" + academicYear;
        result += ", degreeCurricularPlan=" + getDegreeCurricularPlan();
        if (getCoordinatorsList() != null) {
            result += ", coordinatorsList=" + getCoordinatorsList().size();
        } else {
            result += ", coordinatorsList is NULL";
        }
        result += ", campus=" + getCampus();
        result += "]";
        return result;
    }

   
    public IPeriod getPeriodExamsFirstSemester() {
        return periodExamsFirstSemester;
    }

    public void setPeriodExamsFirstSemester(IPeriod periodExamsFirstSemester) {
        this.periodExamsFirstSemester = periodExamsFirstSemester;
    }

    public IPeriod getPeriodExamsSecondSemester() {
        return periodExamsSecondSemester;
    }

    public void setPeriodExamsSecondSemester(IPeriod periodExamsSecondSemester) {
        this.periodExamsSecondSemester = periodExamsSecondSemester;
    }

    public IPeriod getPeriodLessonsSecondSemester() {
        return periodLessonsSecondSemester;
    }

    public void setPeriodLessonsSecondSemester(IPeriod periodLessonsSecondSemester) {
        this.periodLessonsSecondSemester = periodLessonsSecondSemester;
    }

    public IPeriod getPeriodLessonsFirstSemester() {
        return periodLessonsFirstSemester;
    }

    public void setPeriodLessonsFirstSemester(IPeriod periodLessonsFirstSemester) {
        this.periodLessonsFirstSemester = periodLessonsFirstSemester;
    }

    
}