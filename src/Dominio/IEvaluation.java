package Dominio;

import java.util.List;

/**
 * @author T�nia Pous�o
 * 24 de Junho de 2003
 */
public interface IEvaluation extends IDomainObject {
	public String getPublishmentMessage();
	public void setPublishmentMessage(String revisionInformation);
	public List getAssociatedExecutionCourses();
	public void setAssociatedExecutionCourses(List list);
}
