/*
 * Created on 3/Abr/2003 by jpvl
 *
 */
package ServidorAplicacao.strategy.enrolment.degree;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import Dominio.ICurso;
import Dominio.IStudent;

/**
 * @author jpvl
 */
public final class EnrolmentContext {
	private ICurso degree;
	private IStudent student;
	private List curricularCoursesDoneByStudent;
	private Map acumulatedEnrolments;
	private Integer semester;
	
	private List actualEnrolment;
	
	/**
	 * defines the list that student can be enrolled
	 */
	private List finalCurricularCoursesSpanToBeEnrolled;
	
	
	/**
	 *  
	 */
	public EnrolmentContext() {
		super();
		actualEnrolment = new ArrayList();
	}

	/**
	 * @return
	 */
	public List getCurricularCoursesDoneByStudent() {
		return curricularCoursesDoneByStudent;
	}

	/**
	 * @return
	 */
	public ICurso getDegree() {
		return degree;
	}

	/**
	 * @return
	 */
	public Integer getSemester() {
		return semester;
	}

	/**
	 * @return
	 */
	public IStudent getStudent() {
		return student;
	}

	/**
	 * @param list
	 */
	public void setCurricularCoursesDoneByStudent(List list) {
		curricularCoursesDoneByStudent = list;
	}

	/**
	 * @param curso
	 */
	public void setDegree(ICurso degree) {
		this.degree = degree;
	}

	/**
	 * @param integer
	 */
	public void setSemester(Integer integer) {
		semester = integer;
	}

	/**
	 * @param student
	 */
	public void setStudent(IStudent student) {
		this.student = student;
	}
	
	/**
	 * @return
	 */
	public List getFinalCurricularCoursesSpanToBeEnrolled() {
		return finalCurricularCoursesSpanToBeEnrolled;
	}

	/**
	 * @param list
	 */
	public void setFinalCurricularCoursesSpanToBeEnrolled(List list) {
		finalCurricularCoursesSpanToBeEnrolled = list;
	}

	/**
	 * @return
	 */
	public List getActualEnrollment() {
		return actualEnrolment;
	}

	/**
	 * @return
	 */
	public Map getAcumulatedEnrolments() {
		return acumulatedEnrolments;
	}

	/**
	 * @param list
	 */
	public void setActualEnrollment(List list) {
		actualEnrolment = list;
	}

	/**
	 * @param map
	 */
	public void setAcumulatedEnrolments(Map map) {
		acumulatedEnrolments = map;
	}

}
