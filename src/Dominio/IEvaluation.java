/*
 * Created on 23/Abr/2003
 *
 * 
 */
package Dominio;

/**
 * @author Jo�o Mota
 *
 * 
 */
public interface IEvaluation {

	public String getEvaluationElements();
	public void setEvaluationElements(String string);
	public Integer getKeyExecutionCourse();
	public void setKeyExecutionCourse(Integer integer);
	public IDisciplinaExecucao getExecutionCourse();
	public void setExecutionCourse(IDisciplinaExecucao execucao);

}
