package Util;

import java.util.ArrayList;

import org.apache.struts.util.LabelValueBean;

/**
 * @author dcs-rjao
 *
 * 2/Abr/2003
 */
public class EnrolmentEvaluationType {

	public static final int NORMAL = 1; // primeira avaliacao (engloba 1� e 2� �poca)
	public static final int IMPROVEMENT = 2; // avaliacao por melhoria
	public static final int SPECIAL_SEASON = 3; // avaliacao na epoca especial
	public static final int EXTERNAL = 4; // avaliacao feita fora do IST
	public static final int EQUIVALENCE = 5; // avaliacao feita por equivalencia
	public static final int CLOSED = 6; // inscricao com avaliacao fechada
	public static final int FIRST_SEASON = 7; // s� para a classe Enrolment Evaluation, usada apenas na migracao dos Enrolments para guarda hist�rico, n�o usar para mais nada!
	public static final int SECOND_SEASON = 8; // s� para a classe Enrolment Evaluation, usada apenas na migracao dos Enrolments para guarda hist�rico, n�o usar para mais nada!

	public static final EnrolmentEvaluationType NORMAL_OBJ = new EnrolmentEvaluationType(EnrolmentEvaluationType.NORMAL);
	public static final EnrolmentEvaluationType IMPROVEMENT_OBJ = new EnrolmentEvaluationType(EnrolmentEvaluationType.IMPROVEMENT);
	public static final EnrolmentEvaluationType SPECIAL_SEASON_OBJ = new EnrolmentEvaluationType(EnrolmentEvaluationType.SPECIAL_SEASON);
	public static final EnrolmentEvaluationType EXTERNAL_OBJ = new EnrolmentEvaluationType(EnrolmentEvaluationType.EXTERNAL);
	public static final EnrolmentEvaluationType EQUIVALENCE_OBJ = new EnrolmentEvaluationType(EnrolmentEvaluationType.EQUIVALENCE);
	public static final EnrolmentEvaluationType CLOSED_OBJ = new EnrolmentEvaluationType(EnrolmentEvaluationType.CLOSED);
	public static final EnrolmentEvaluationType FIRST_SEASON_OBJ = new EnrolmentEvaluationType(EnrolmentEvaluationType.FIRST_SEASON);
	public static final EnrolmentEvaluationType SECOND_SEASON_OBJ = new EnrolmentEvaluationType(EnrolmentEvaluationType.SECOND_SEASON);

	public static final String NORMAL_STRING = "Epoca Normal";
	public static final String IMPROVEMENT_STRING = "Melhoria de Nota";
	public static final String SPECIAL_STRING = "Epoca Especial";
	public static final String EQUIVALENCE_STRING = "Por Equival�ncia";

	private Integer type;

	public EnrolmentEvaluationType() {
	}

	public EnrolmentEvaluationType(int state) {
		this.type = new Integer(state);
	}

	public EnrolmentEvaluationType(Integer state) {
		this.type = state;
	}

	/** Getter for property type.
	 * @return Value of property type.
	 *
	 */
	public java.lang.Integer getType() {
		return type;
	}

	/** Setter for property type.
	 * @param type New value of property type.
	 *
	 */
	public void setType(Integer state) {
		this.type = state;
	}

	public boolean equals(Object o) {
		if (o instanceof EnrolmentEvaluationType) {
			EnrolmentEvaluationType aux = (EnrolmentEvaluationType) o;
			return this.type.equals(aux.getType());
		} else {
			return false;
		}
	}

	public String toString() {

		int value = this.type.intValue();
		String valueS = null;

		switch (value) {
			case IMPROVEMENT :
				valueS = "IMPROVEMENT";
				break;
			default :
				break;
		}

		return "[" + this.getClass().getName() + ": " + valueS + "]\n";
	}

	public EnrolmentEvaluationType(String type) {
			if (type.equals(EnrolmentEvaluationType.NORMAL_STRING)) this.type = new Integer(EnrolmentEvaluationType.NORMAL);
			if (type.equals(EnrolmentEvaluationType.IMPROVEMENT_STRING)) this.type = new Integer(EnrolmentEvaluationType.IMPROVEMENT);
		if (type.equals(EnrolmentEvaluationType.SPECIAL_STRING)) this.type = new Integer(EnrolmentEvaluationType.SPECIAL_SEASON);
		if (type.equals(EnrolmentEvaluationType.EQUIVALENCE_STRING)) this.type = new Integer(EnrolmentEvaluationType.EQUIVALENCE);
		}

	public ArrayList toArrayList() {
		ArrayList result = new ArrayList();
		//		   result.add(new LabelValueBean(EnrolmentEvaluationType.DEFAULT, null));
		result.add(new LabelValueBean(EnrolmentEvaluationType.IMPROVEMENT_STRING, String.valueOf(EnrolmentEvaluationType.IMPROVEMENT)));
		result.add(new LabelValueBean(EnrolmentEvaluationType.NORMAL_STRING, String.valueOf(EnrolmentEvaluationType.NORMAL)));
		result.add(new LabelValueBean(EnrolmentEvaluationType.SPECIAL_STRING, String.valueOf(EnrolmentEvaluationType.SPECIAL_SEASON)));
		result.add(new LabelValueBean(EnrolmentEvaluationType.EQUIVALENCE_STRING, String.valueOf(EnrolmentEvaluationType.EQUIVALENCE)));
		return result;
	}

}
