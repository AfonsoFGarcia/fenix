package Dominio;

/**
 * @author T�nia Pous�o
 *
 */
public interface IEvalutionExecutionCourse {
	public IEvaluation getEvaluation();
	public IExecutionCourse getExecutionCourse();

	public void setEvaluation(IEvaluation evaluation);
	public void setExecutionCourse(IExecutionCourse executionCourse);
}
