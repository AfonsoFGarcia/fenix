package Dominio;

/**
 * @author T�nia Pous�o
 *
 */
public interface IEvalutionExecutionCourse {
	public IEvaluation getEvaluation();
	public IDisciplinaExecucao getExecutionCourse();

	public void setEvaluation(IEvaluation evaluation);
	public void setExecutionCourse(IDisciplinaExecucao executionCourse);
}
