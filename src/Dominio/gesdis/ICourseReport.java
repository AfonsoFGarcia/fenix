/*
 * Created on 7/Nov/2003
 *
 * To change the template for this generated file go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
package Dominio.gesdis;

import java.util.Date;

import Dominio.IDisciplinaExecucao;
import Dominio.IDomainObject;

/**
 * @author Leonor Almeida
 * @author Sergio Montelobo
 *
 */
public interface ICourseReport extends IDomainObject {

	public String getReport();
	public IDisciplinaExecucao getExecutionCourse();
	public Date getLastModificationDate();
	public void setReport(String report);
	public void setExecutionCourse(IDisciplinaExecucao executionCourse);
    public void setLastModificationDate(Date lastModificationDate);
}
