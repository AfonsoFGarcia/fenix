/*
 * Created on 12/Mar/2003
 *
 * 
 */
package DataBeans;

import java.util.List;


/**
 * @author Jo�o Mota
 */
public class InfoTeacher extends InfoObject{

	private Integer teacherNumber;
	private InfoPerson infoPerson;
	private List professorShipsExecutionCourses;
	private List responsibleForExecutionCourses;

	/**
	 * 
	 */
	public InfoTeacher() {
		
	}
	public InfoTeacher(Integer teacherNumber,InfoPerson infoPerson) {
		setTeacherNumber(teacherNumber);
		setInfoPerson(infoPerson);
		}

	public InfoTeacher(Integer teacherNumber,List professorShipsExecutionCourses,List responsibleForExecutionCourses,InfoPerson infoPerson) {
			setTeacherNumber(teacherNumber);
			setProfessorShipsExecutionCourses(professorShipsExecutionCourses);
			setResponsibleForExecutionCourses(responsibleForExecutionCourses);
		setInfoPerson(infoPerson);
		}



	/**
	 * @return List
	 */
	public List getProfessorShipsExecutionCourses() {
		return professorShipsExecutionCourses;
	}

	/**
	 * @return List
	 */
	public List getResponsibleForExecutionCourses() {
		return responsibleForExecutionCourses;
	}

	/**
	 * @return Integer
	 */
	public Integer getTeacherNumber() {
		return teacherNumber;
	}

	/**
	 * Sets the professorShipsExecutionCourses.
	 * @param professorShipsExecutionCourses The professorShipsExecutionCourses to set
	 */
	public void setProfessorShipsExecutionCourses(List professorShipsExecutionCourses) {
		this.professorShipsExecutionCourses = professorShipsExecutionCourses;
	}

	/**
	 * Sets the responsibleForExecutionCourses.
	 * @param responsibleForExecutionCourses The responsibleForExecutionCourses to set
	 */
	public void setResponsibleForExecutionCourses(List responsibleForExecutionCourses) {
		this.responsibleForExecutionCourses = responsibleForExecutionCourses;
	}

	/**
	 * Sets the teacherNumber.
	 * @param teacherNumber The teacherNumber to set
	 */
	public void setTeacherNumber(Integer teacherNumber) {
		this.teacherNumber = teacherNumber;
	}

	/**
	 * @return InfoPerson
	 */
	public InfoPerson getInfoPerson() {
		return infoPerson;
	}

	/**
	 * Sets the person.
	 * @param person The person to set
	 */
	public void setInfoPerson(InfoPerson person) {
		this.infoPerson = person;
	}
    
	public boolean equals(Object obj) {
		 boolean result = false;
		 if (obj instanceof InfoTeacher) {
			InfoTeacher infoTeacher = (InfoTeacher) obj;
			
			 result = ((infoTeacher!=null) && 
				 (getTeacherNumber().equals(infoTeacher.getTeacherNumber()))
					 && (getInfoPerson().equals(infoTeacher.getInfoPerson())));
		 }
		 return result;
	 }
    
}
