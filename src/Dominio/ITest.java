/*
 * Created on 28/Jul/2003
 *
 */
package Dominio;

import java.util.Date;

/**
 * @author Susana Fernandes
 */
public interface ITest extends IDomainObject {
	public abstract IDisciplinaExecucao getExecutionCourse();
	public abstract Integer getKeyExecutionCourse();
	public abstract Integer getNumberOfQuestions();
	public abstract String getTitle();
	public abstract void setExecutionCourse(IDisciplinaExecucao execucao);
	public abstract void setKeyExecutionCourse(Integer integer);
	public abstract void setNumberOfQuestions(Integer integer);
	public abstract void setTitle(String string);
	public abstract Date getCreationDate();
	public abstract Date getLastModifiedDate();
	public abstract void setCreationDate(Date date);
	public abstract void setLastModifiedDate(Date date);
	public abstract String getInformation();
	public abstract void setInformation(String string);
	public abstract Boolean getVisible();
	public abstract void setVisible(Boolean visible);
}