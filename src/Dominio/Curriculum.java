/*
 * Curriculum.java
 *
 * Created on 6 de Janeiro de 2003, 20:29
 */
package Dominio;
/**
 *
 * @author  EP 15 - fjgc
 * @author Jo�o Mota
 */
public class Curriculum extends DomainObject implements ICurriculum {
	protected String generalObjectives;
	protected String operacionalObjectives;
	protected String program;
	protected String generalObjectivesEn;
	protected String operacionalObjectivesEn;
	protected String programEn;
	protected ICurricularCourse curricularCourse;
	private Integer keyCurricularCourse;
	/** Creates a new instance of Curriculum */
	public Curriculum() {
	}
	
	public Curriculum(Integer idInternal) {
		setIdInternal(idInternal);
		}
	public Curriculum(ICurricularCourse curricularCourse, String generalObjectives, String operacionalObjectives, String program) {
		setGeneralObjectives(generalObjectives);
		setOperacionalObjectives(operacionalObjectives);
		setProgram(program);
		setCurricularCourse(curricularCourse);
	}
	public Curriculum(
		ICurricularCourse curricularCourse,
		String generalObjectives,
		String operacionalObjectives,
		String generalObjectivesEn,
		String operacionalObjectivesEn) {
		setGeneralObjectives(generalObjectives);
		setOperacionalObjectives(operacionalObjectives);
		setGeneralObjectivesEn(generalObjectivesEn);
		setOperacionalObjectivesEn(operacionalObjectivesEn);
		setCurricularCourse(curricularCourse);
	}
	public Curriculum(ICurricularCourse curricularCourse, String program, String programEn) {
		setProgram(program);
		setProgramEn(programEn);
		setCurricularCourse(curricularCourse);
	}
	
	public Integer getKeyCurricularCourse() {
		return keyCurricularCourse;
	}
	public void setKeyCurricularCourse(Integer keyExecutionCourse) {
		this.keyCurricularCourse = keyExecutionCourse;
	}
	public String getGeneralObjectives() {
		return generalObjectives;
	}
	public String getOperacionalObjectives() {
		return operacionalObjectives;
	}
	public String getProgram() {
		return program;
	}
	public ICurricularCourse getCurricularCourse() {
		return curricularCourse;
	}
	public void setGeneralObjectives(String generalObjectives) {
		this.generalObjectives = generalObjectives;
	}
	public void setOperacionalObjectives(String operacionalObjectives) {
		this.operacionalObjectives = operacionalObjectives;
	}
	public void setProgram(String program) {
		this.program = program;
	}
	public void setCurricularCourse(ICurricularCourse curricularCourse) {
		this.curricularCourse = curricularCourse;
	}
	public boolean equals(Object obj) {
		boolean result = false;
		if (obj instanceof ICurriculum) {
			ICurriculum curriculum = (ICurriculum) obj;
			result = getCurricularCourse().equals(curriculum.getCurricularCourse());
		}
		return result;
	}
	public String toString() {
		String result = "[CURRICULUM";
		result += "codigo interno" + getIdInternal();
		result += "Objectivos Operacionais" + getOperacionalObjectives();
		result += "Objectivos gerais" + getGeneralObjectives();
		result += "programa" + getProgram();
		result += "Objectivos Operacionais em Ingl�s" + getOperacionalObjectivesEn();
		result += "Objectivos gerais em Ingl�s" + getGeneralObjectivesEn();
		result += "programa em Ingl�s" + getProgramEn();
		result += "curricular Course" + getCurricularCourse();
		result += "]";
		return result;
	}
	/**
	 * @return
	 */
	public String getGeneralObjectivesEn() {
		return generalObjectivesEn;
	}
	/**
	 * @return
	 */
	public String getOperacionalObjectivesEn() {
		return operacionalObjectivesEn;
	}
	/**
	 * @return
	 */
	public String getProgramEn() {
		return programEn;
	}
	/**
	 * @param string
	 */
	public void setGeneralObjectivesEn(String string) {
		generalObjectivesEn = string;
	}
	/**
	 * @param string
	 */
	public void setOperacionalObjectivesEn(String string) {
		operacionalObjectivesEn = string;
	}
	/**
	 * @param string
	 */
	public void setProgramEn(String string) {
		programEn = string;
	}
}
