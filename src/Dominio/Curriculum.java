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
public class Curriculum implements ICurriculum {
	protected String generalObjectives;
	protected String operacionalObjectives;
	protected String program;
	protected String generalObjectivesEn;
	protected String operacionalObjectivesEn;
	protected String programEn;
	protected IDisciplinaExecucao executionCourse;
	private Integer internalCode;
	private Integer keyExecutionCourse;
	/** Creates a new instance of Curriculum */
	public Curriculum() {
	}
	public Curriculum(IDisciplinaExecucao executionCourse, String generalObjectives, String operacionalObjectives, String program) {
		setGeneralObjectives(generalObjectives);
		setOperacionalObjectives(operacionalObjectives);
		setProgram(program);
		setExecutionCourse(executionCourse);
	}
	public Curriculum(
		IDisciplinaExecucao executionCourse,
		String generalObjectives,
		String operacionalObjectives,
		String generalObjectivesEn,
		String operacionalObjectivesEn) {
		setGeneralObjectives(generalObjectives);
		setOperacionalObjectives(operacionalObjectives);
		setGeneralObjectivesEn(generalObjectivesEn);
		setOperacionalObjectivesEn(operacionalObjectivesEn);
		setExecutionCourse(executionCourse);
	}
	public Curriculum(IDisciplinaExecucao executionCourse, String program, String programEn) {
		setProgram(program);
		setProgramEn(programEn);
		setExecutionCourse(executionCourse);
	}
	public Integer getInternalCode() {
		return internalCode;
	}
	public void setInternalCode(Integer internalCode) {
		this.internalCode = internalCode;
	}
	public Integer getKeyExecutionCourse() {
		return keyExecutionCourse;
	}
	public void setKeyExecutionCourse(Integer keyExecutionCourse) {
		this.keyExecutionCourse = keyExecutionCourse;
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
	public IDisciplinaExecucao getExecutionCourse() {
		return executionCourse;
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
	public void setExecutionCourse(IDisciplinaExecucao executionCourse) {
		this.executionCourse = executionCourse;
	}
	public boolean equals(Object obj) {
		boolean result = false;
		if (obj instanceof ICurriculum) {
			ICurriculum curriculum = (ICurriculum) obj;
			result = getExecutionCourse().equals(curriculum.getExecutionCourse());
		}
		return result;
	}
	public String toString() {
		String result = "[CURRICULUM";
		result += "codigo interno" + getInternalCode();
		result += "Objectivos Operacionais" + getOperacionalObjectives();
		result += "Objectivos gerais" + getGeneralObjectives();
		result += "programa" + getProgram();
		result += "Objectivos Operacionais em Ingl�s" + getOperacionalObjectivesEn();
		result += "Objectivos gerais em Ingl�s" + getGeneralObjectivesEn();
		result += "programa em Ingl�s" + getProgramEn();
		result += "execution course" + getExecutionCourse();
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
