/*
 * InfoExecutionDegree.java
 *
 * Created on 24 de Novembro de 2002, 23:05
 */

package DataBeans;

import java.io.Serializable;

/**
 *
 * @author  tfc130
 */
public class InfoExecutionDegree implements Serializable {
	private InfoExecutionYear infoExecutionYear;
	private InfoDegreeCurricularPlan infoDegreeCurricularPlan;

	public InfoExecutionDegree() {
	}
	/**
	 * 
	 * @param anoLectivo
	 * @param infoLicenciatura
	 * @deprecated
	 */
	public InfoExecutionDegree(
		String anoLectivo,
		InfoDegree infoLicenciatura) {
	}
	/**
	 * 
	 * @param infoDegreeCurricularPlan
	 * @param infoExecutionYear
	 */
	public InfoExecutionDegree(
		InfoDegreeCurricularPlan infoDegreeCurricularPlan,
		InfoExecutionYear infoExecutionYear) {
		setInfoDegreeCurricularPlan(infoDegreeCurricularPlan);
		setInfoExecutionYear(infoExecutionYear);
	}

	public boolean equals(Object obj) {
		boolean result = false;
		if (obj instanceof InfoExecutionDegree) {
			InfoExecutionDegree infoExecutionDegree = (InfoExecutionDegree) obj;
			result =
				getInfoExecutionYear().equals(infoExecutionDegree.getInfoExecutionYear())
					&& getInfoDegreeCurricularPlan().equals(
						infoExecutionDegree.getInfoDegreeCurricularPlan());
		}
		return result;
	}

	public String toString() {
		String result = "[INFOEXECUTIONDEGREE";
			result += ", infoExecutionYear=" + infoExecutionYear;
			result += ", infoDegreeCurricularPlan=" + infoDegreeCurricularPlan;
			result += "]";
		return result;
	}


	/**
	 * Returns the infoExecutionYear.
	 * @return InfoExecutionYear
	 */
	public InfoExecutionYear getInfoExecutionYear() {
		return infoExecutionYear;
	}

	/**
	 * Sets the infoExecutionYear.
	 * @param infoExecutionYear The infoExecutionYear to set
	 */
	public void setInfoExecutionYear(InfoExecutionYear infoExecutionYear) {
		this.infoExecutionYear = infoExecutionYear;
	}

	/**
	 * Returns the infoDegreeCurricularPlan.
	 * @return InfoDegreeCurricularPlan
	 */
	public InfoDegreeCurricularPlan getInfoDegreeCurricularPlan() {
		return infoDegreeCurricularPlan;
	}

	/**
	 * Sets the infoDegreeCurricularPlan.
	 * @param infoDegreeCurricularPlan The infoDegreeCurricularPlan to set
	 */
	public void setInfoDegreeCurricularPlan(InfoDegreeCurricularPlan infoDegreeCurricularPlan) {
		this.infoDegreeCurricularPlan = infoDegreeCurricularPlan;
	}

}
