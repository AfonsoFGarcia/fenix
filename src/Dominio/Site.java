/*
 * Site.java
 * Mar 10, 2003
 */
package Dominio;

import java.util.List;

/**
 * @author Ivo Brand�o
 */
public class Site implements ISite {

	private Integer internalCode;
	private IDisciplinaExecucao executionCourse;
	private Integer keyExecutionCourse;
	private List sections;
	private ISection initialSection;
	private Integer keyInitialSection;
	private List announcements;
	
	/** 
	 * Construtor
	 */
	public Site() {
	}

	/** 
	 * Construtor
	 */
	public Site(IDisciplinaExecucao executionCourse) {

		setExecutionCourse(executionCourse);
	}

	/** 
	* Construtor
	*/
	
	public Site(ISection initialSection, List sections, IDisciplinaExecucao executionCourse, List announcements){
		setInitialSection(initialSection);
		setSections(sections);
		setExecutionCourse(executionCourse);
		setAnnouncements(announcements);
	}
	
	
	/**
	 * @return IDisciplinaExecucao
	 */
	public IDisciplinaExecucao getExecutionCourse() {
		return executionCourse;
	}

	/**
	 * @return Integer
	 */
	public Integer getInternalCode() {
		return internalCode;
	}

	/**
	 * Sets the executionCourse.
	 * @param executionCourse The executionCourse to set
	 */
	public void setExecutionCourse(IDisciplinaExecucao executionCourse) {
		this.executionCourse = executionCourse;
	}

	/**
	 * Sets the internalCode.
	 * @param internalCode The internalCode to set
	 */
	public void setInternalCode(Integer internalCode) {
		this.internalCode = internalCode;
	}

	/**
	 * @return Integer
	 */
	public Integer getKeyExecutionCourse() {
		return keyExecutionCourse;
	}

	/**
	 * Sets the keyExecutionCourse.
	 * @param keyExecutionCourse The keyExecutionCourse to set
	 */
	public void setKeyExecutionCourse(Integer keyExecutionCourse) {
		this.keyExecutionCourse = keyExecutionCourse;
	}

	/**
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	public boolean equals(Object arg0) {
		boolean result = false;
		if (arg0 instanceof ISite) {
			result =
				(getExecutionCourse()
					.equals(((ISite) arg0).getExecutionCourse()));
		}
		return result;
	}

	/**
	 * @return List
	 */
	public List getAnnouncements() {
		return announcements;
	}

	/**
	 * @return ISection
	 */
	public ISection getInitialSection() {
		return initialSection;
	}

	

	/**
	 * @return List
	 */
	public List getSections() {
		return sections;
	}

	/**
	 * Sets the announcements.
	 * @param announcements The announcements to set
	 */
	public void setAnnouncements(List announcements) {
		this.announcements = announcements;
	}

	/**
	 * Sets the initialSection.
	 * @param initialSection The initialSection to set
	 */
	public void setInitialSection(ISection initialSection) {
		this.initialSection = initialSection;
	}

	

	/**
	 * Sets the sections.
	 * @param sections The sections to set
	 */
	public void setSections(List sections) {
		this.sections = sections;
	}

	/**
	 * @return Integer
	 */
	public Integer getKeyInitialSection() {
		return keyInitialSection;
	}

	/**
	 * Sets the keyInitialSection.
	 * @param keyInitialSection The keyInitialSection to set
	 */
	public void setKeyInitialSection(Integer keyInitialSection) {
		this.keyInitialSection = keyInitialSection;
	}

	/**
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		String result = "[SITE";
		result += ", codInt=" + getInternalCode();
		result += ", executionCourse=" + getExecutionCourse();
		result += ", sections=" + getSections();
		result += ", initialSection=" + getInitialSection();
//		result += ", announcements=" + getAnnouncements();
		result += "]";
		return result;
	}

}
