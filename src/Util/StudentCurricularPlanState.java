/*
 * StudentCurricularState.java
 *
 * Created on 20 de Dezembro de 2002, 14:12
 */

package Util;

/**
 *
 * @author  Nuno Nunes & Joana Mota
 */
public class StudentCurricularPlanState {

	public static final int ACTIVE = 1;
	public static final int CONCLUDED = 2;
	public static final int INCOMPLETE = 3;

	private Integer state;

	public StudentCurricularPlanState() {
	}

	public StudentCurricularPlanState(int state) {
		this.state = new Integer(state);
	}

	public StudentCurricularPlanState(Integer state) {
		this.state = state;
	}

	public Integer getState() {
		return this.state;
	}

	public void setState(int state) {
		this.state = new Integer(state);
	}

	public void setState(Integer state) {
		this.state = state;
	}

	public boolean equals(Object obj) {
		boolean resultado = false;
		if (obj instanceof StudentCurricularPlanState) {
			StudentCurricularPlanState state = (StudentCurricularPlanState) obj;
			resultado = getState().equals(state.getState());
		}
		return resultado;
	}

	public String toString() {
		int value = this.state.intValue();
		String valueS = null;
		switch (value) {
			case ACTIVE :
				valueS = "ACTIVE";
				break;
			case CONCLUDED :
				valueS = "CONCLUDED";
				break;
			case INCOMPLETE :
				valueS = "INCOMPLETE";
				break;
			default:
				break;
		}
		return valueS;
	}
}