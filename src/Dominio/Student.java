package Dominio;

import Util.StudentState;
import Util.TipoCurso;

/**
 * @author dcs-rjao
 *
 * 24/Mar/2003
 */

public class Student implements IStudent {

	protected Integer number;
	protected StudentState state;
	protected TipoCurso degreeType;
	private IStudentGroupInfo studentGroupInfo;

	private Integer internalCode;
	private Integer personKey;
	private Integer studentGroupInfoKey;
	private IPessoa person;

	public Student() {
		setNumber(null);
		setState(null);
		setPerson(null);
		setDegreeType(null);
		setInternalCode(null);
		setPersonKey(null);
		setStudentGroupInfo(null);
		setStudentGroupInfoKey(null);
	}

	public Student(Integer number, StudentState state, IPessoa person, TipoCurso degreeType) {
		this();
		setNumber(number);
		setState(state);
		setPerson(person);
		setDegreeType(degreeType);
		setInternalCode(null);
		setPersonKey(null);
	}

	public boolean equals(Object obj) {
		boolean resultado = false;
		if (obj instanceof IStudent) {
			IStudent student = (IStudent) obj;
			resultado = ( this.getNumber().equals(student.getNumber()) && this.getDegreeType().equals(student.getDegreeType()) ) ||
									( this.getDegreeType().equals(student.getDegreeType()) && this.getPerson().equals(student.getPerson())	);
		}
		return resultado;
	}

	public String toString() {
		String result = "[" + this.getClass().getName() + "; ";
		result += "internalCode = " + this.internalCode + "; ";
		result += "number = " + this.number + "; ";
		result += "state = " + this.state + "; ";
		result += "degreeType = " + this.degreeType + "; ";
		result += "studentGroupInfo = " + this.studentGroupInfo + "; ";
		result += "person = " + this.person + "]";
		return result;
	}

	/**
	 * Returns the degreeType.
	 * @return TipoCurso
	 */
	public TipoCurso getDegreeType() {
		return degreeType;
	}

	/**
	 * Returns the internalCode.
	 * @return Integer
	 */
	public Integer getInternalCode() {
		return internalCode;
	}

	/**
	 * Returns the number.
	 * @return Integer
	 */
	public Integer getNumber() {
		return number;
	}

	/**
	 * Returns the person.
	 * @return IPessoa
	 */
	public IPessoa getPerson() {
		return person;
	}

	/**
	 * Returns the personKey.
	 * @return Integer
	 */
	public Integer getPersonKey() {
		return personKey;
	}

	/**
	 * Returns the state.
	 * @return StudentState
	 */
	public StudentState getState() {
		return state;
	}

	/**
	 * Sets the degreeType.
	 * @param degreeType The degreeType to set
	 */
	public void setDegreeType(TipoCurso degreeType) {
		this.degreeType = degreeType;
	}

	/**
	 * Sets the internalCode.
	 * @param internalCode The internalCode to set
	 */
	public void setInternalCode(Integer internalCode) {
		this.internalCode = internalCode;
	}

	/**
	 * Sets the number.
	 * @param number The number to set
	 */
	public void setNumber(Integer number) {
		this.number = number;
	}

	/**
	 * Sets the person.
	 * @param person The person to set
	 */
	public void setPerson(IPessoa person) {
		this.person = person;
	}

	/**
	 * Sets the personKey.
	 * @param personKey The personKey to set
	 */
	public void setPersonKey(Integer personKey) {
		this.personKey = personKey;
	}

	/**
	 * Sets the state.
	 * @param state The state to set
	 */
	public void setState(StudentState state) {
		this.state = state;
	}
	/**
	 * @return
	 */
	public IStudentGroupInfo getStudentGroupInfo() {
		return studentGroupInfo;
	}

	/**
	 * @return
	 */
	public Integer getStudentGroupInfoKey() {
		return studentGroupInfoKey;
	}

	/**
	 * @param type
	 */
	public void setStudentGroupInfo(IStudentGroupInfo studentGroupInfo) {
		this.studentGroupInfo = studentGroupInfo;
	}

	/**
	 * @param integer
	 */
	public void setStudentGroupInfoKey(Integer studentGroupInfoKey) {
		this.studentGroupInfoKey = studentGroupInfoKey;
	}

}
