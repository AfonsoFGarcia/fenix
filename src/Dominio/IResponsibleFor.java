/*
 * Created on 26/Mar/2003
 *
 *
 */
package Dominio;

/**
 * @author Jo�o Mota
 *
 * 
 */
public interface IResponsibleFor extends IDomainObject {
	public ITeacher getTeacher();
	public IDisciplinaExecucao getExecutionCourse();

	public void setTeacher(ITeacher teacher);
	public void setExecutionCourse(IDisciplinaExecucao executionCourse);
}
