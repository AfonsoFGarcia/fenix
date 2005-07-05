package net.sourceforge.fenixedu.domain;

/**
 * @author T�nia Pous�o
 * 
 */
public class EvaluationExecutionCourse extends EvaluationExecutionCourse_Base {

    public boolean equals(Object obj) {
        if (obj instanceof IEvaluationExecutionCourse) {
            final IEvaluationExecutionCourse evaluationExecutionCourse = (IEvaluationExecutionCourse) obj;
            return this.getIdInternal().equals(evaluationExecutionCourse.getIdInternal());
        }
        return false;
    }

    public String toString() {
        return "[EVALUATION_EXECUTIONCOURSE:" + " evaluation= '" + this.getEvaluation() + "'"
                + " execution_course= '" + this.getExecutionCourse() + "'" + "]";
    }

}
