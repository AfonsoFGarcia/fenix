package Dominio;

/**
 * @author T�nia Pous�o
 *  
 */
public interface IEvalutionExecutionCourse extends IDomainObject {
    public IEvaluation getEvaluation();

    public IExecutionCourse getExecutionCourse();

    public void setEvaluation(IEvaluation evaluation);

    public void setExecutionCourse(IExecutionCourse executionCourse);
}