/*
 * IDisciplinaExecucao.java
 *
 * Created on 16 de Outubro de 2002, 11:12
 */

package Dominio;

import java.io.Serializable;
import java.util.List;

/**
 *
 * @author Nuno Nunes & Joana Mota
 */
public interface IDisciplinaExecucao extends Serializable, IDomainObject {
	public String getNome();
	public void setNome(java.lang.String nome);
	public String getSigla();
	public void setSigla(java.lang.String sigla);

	public Double getTheoreticalHours();
	public void setTheoreticalHours(Double theoreticalHours);
	public Double getPraticalHours();
	public void setPraticalHours(Double praticalHours);
	public Double getTheoPratHours();
	public void setTheoPratHours(Double theoPratHours);
	public Double getLabHours();
	public void setLabHours(Double labHours);

	public void setAssociatedCurricularCourses(List associatedCurricularCourses);
	public List getAssociatedCurricularCourses();
	public void setAssociatedExams(List list);
	public List getAssociatedExams();
	public void setAssociatedEvaluations(List list);
	public List getAssociatedEvaluations();

	IExecutionPeriod getExecutionPeriod();
	void setExecutionPeriod(IExecutionPeriod executionPeriod);

	public String getComment();
	public void setComment(String string);
	public List getAttendingStudents();
	public void setAttendingStudents(List attendingStudents);
}
