package Util;

/**
 * @author dcs-rjao
 *
 * 25/Mar/2003
 */
public class CurricularCourseExecutionScope {

	public static final int SEMESTRIAL = 1;
	public static final int ANUAL = 2;
	
	private Integer type;

	public CurricularCourseExecutionScope() {
	}

	public CurricularCourseExecutionScope(int state) {
		this.type = new Integer(state);
	}

	public CurricularCourseExecutionScope(Integer state) {
		this.type = state;
	}

	public java.lang.Integer getType() {
		return type;
	}

	public void setType(Integer state) {
		this.type = state;
	}

	public boolean equals(Object o) {
		if (o instanceof CurricularCourseExecutionScope) {
			CurricularCourseExecutionScope aux = (CurricularCourseExecutionScope) o;
			return this.type.equals(aux.getType());
		} else {
			return false;
		}
	}

	public String toString() {

		int value = this.type.intValue();
		String valueS = null;

		switch (value) {
			case SEMESTRIAL :
				valueS = "SEMESTRIAL";
				break;
			case ANUAL :
				valueS = "ANUAL";
				break;
			default:
				break;
		}

		return "[" + this.getClass().getName() + ": " + valueS + "]";
	}
}