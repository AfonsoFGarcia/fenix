/*
 * Created on 28/Jul/2003
 *
 */
package Dominio;

import java.util.Date;

/**
 * @author Susana Fernandes
 */
public class Test extends DomainObject implements ITest {
	private String title;
	private String information;
	private Integer numberOfQuestions;
	private Date creationDate;
	private Date lastModifiedDate;
	private IExecutionCourse executionCourse;
	private Integer keyExecutionCourse;

	public Test() {
	}

	public Test(Integer testId) {
		setIdInternal(testId);
	}

	public IExecutionCourse getExecutionCourse() {
		return executionCourse;
	}

	public Integer getKeyExecutionCourse() {
		return keyExecutionCourse;
	}

	public Integer getNumberOfQuestions() {
		return numberOfQuestions;
	}

	public String getTitle() {
		return title;
	}

	public void setExecutionCourse(IExecutionCourse execucao) {
		executionCourse = execucao;
	}

	public void setKeyExecutionCourse(Integer integer) {
		keyExecutionCourse = integer;
	}

	public void setNumberOfQuestions(Integer integer) {
		numberOfQuestions = integer;
	}

	public void setTitle(String string) {
		title = string;
	}

	public Date getCreationDate() {
		return creationDate;
	}

	public Date getLastModifiedDate() {
		return lastModifiedDate;
	}

	public void setCreationDate(Date date) {
		creationDate = date;
	}

	public void setLastModifiedDate(Date date) {
		lastModifiedDate = date;
	}

	public String getInformation() {
		return information;
	}

	public void setInformation(String string) {
		information = string;
	}
}
