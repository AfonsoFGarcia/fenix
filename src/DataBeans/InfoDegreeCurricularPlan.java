package DataBeans;

import java.io.Serializable;
import java.util.Date;

import Util.DegreeState;

/**
 * @author David Santos
 *
 * 19/Mar/2003
 */

public class InfoDegreeCurricularPlan implements Serializable {

	private InfoDegree infoDegree;
	private String name;
	private DegreeState state;
	private Date initialDate;
	private Date endDate;

	public InfoDegreeCurricularPlan() {
		setName(null);
		setInfoDegree(null);
		setState(null);
		setInitialDate(null);
		setEndDate(null);
	}

	public InfoDegreeCurricularPlan(String name, InfoDegree infoDegree) {
		setName(name);
		setInfoDegree(infoDegree);
		setState(null);
		setInitialDate(null);
		setEndDate(null);
	}

	public InfoDegreeCurricularPlan(String nome, InfoDegree infoDegree, DegreeState state, Date initialDate, Date endDate) {
		setName(nome);
		setInfoDegree(infoDegree);
		setState(state);
		setInitialDate(initialDate);
		setEndDate(endDate);
	}

	public boolean equals(Object obj) {
		boolean result = false;
		if (obj instanceof InfoDegreeCurricularPlan) {
			InfoDegreeCurricularPlan infoCurricularPlan = (InfoDegreeCurricularPlan) obj;
			result = (this.getName().equals(infoCurricularPlan.getName()) && this.getInfoDegree().equals(infoCurricularPlan.getInfoDegree()));
		}
		return result;
	}

	public String toString() {
		String result = "[" + this.getClass().getName() + ": ";
		result += "name = " + this.name + "; ";
		result += "initialDate = " + this.initialDate + "; ";
		result += "endDate = " + this.endDate + "; ";
		result += "state = " + this.state + "; ";
		result += "degree = " + this.infoDegree + "]";
		return result;
	}

	/**
	 * @return Date
	 */
	public Date getEndDate() {
		return endDate;
	}

	/**
	 * @return InfoDegree
	 */
	public InfoDegree getInfoDegree() {
		return infoDegree;
	}

	/**
	 * @return Date
	 */
	public Date getInitialDate() {
		return initialDate;
	}

	/**
	 * @return String
	 */
	public String getName() {
		return name;
	}

	/**
	 * @return DegreeState
	 */
	public DegreeState getState() {
		return state;
	}

	/**
	 * Sets the endDate.
	 * @param endDate The endDate to set
	 */
	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	/**
	 * Sets the infoDegree.
	 * @param infoDegree The infoDegree to set
	 */
	public void setInfoDegree(InfoDegree infoDegree) {
		this.infoDegree = infoDegree;
	}

	/**
	 * Sets the initialDate.
	 * @param initialDate The initialDate to set
	 */
	public void setInitialDate(Date initialDate) {
		this.initialDate = initialDate;
	}

	/**
	 * Sets the name.
	 * @param name The name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * Sets the state.
	 * @param state The state to set
	 */
	public void setState(DegreeState state) {
		this.state = state;
	}

}