package Util;

import java.util.ArrayList;

import org.apache.struts.util.LabelValueBean;


/**
 * 
 * @author Nuno Nunes (nmsn@rnl.ist.utl.pt)
 *         Joana Mota (jccm@rnl.ist.utl.pt)
 */

public class SituationOfGuide {

	public static final int NON_PAYED = 1;
	public static final int PAYED = 2;
	public static final int ANNULLED = 3;

	public static final SituationOfGuide NON_PAYED_TYPE = new SituationOfGuide(NON_PAYED);
	public static final SituationOfGuide PAYED_TYPE = new SituationOfGuide(PAYED);
	public static final SituationOfGuide ANNULLED_TYPE = new SituationOfGuide(ANNULLED);

	public static final String NON_PAYED_STRING = "N�o Paga";
	public static final String PAYED_STRING = "Paga";
	public static final String ANNULLED_STRING = "Anulada";
	public static final String DEFAULT = "[Escolha uma Situa��o]";


	private Integer situation;

	public SituationOfGuide() {
	}

	public SituationOfGuide(int type) {
		this.situation = new Integer(type);
	}

	public SituationOfGuide(Integer type) {
		this.situation = type;
	}

	public boolean equals(Object obj) {
		boolean resultado = false;
		if (obj instanceof SituationOfGuide) {
			SituationOfGuide ds = (SituationOfGuide) obj;
			resultado = this.getSituation().equals(ds.getSituation());
		}
		return resultado;
	}

	public static ArrayList toArrayList() {
		ArrayList result = new ArrayList();
		result.add(new LabelValueBean(SituationOfGuide.DEFAULT, null));
		result.add(new LabelValueBean(SituationOfGuide.ANNULLED_STRING, String.valueOf(SituationOfGuide.ANNULLED)));
		result.add(new LabelValueBean(SituationOfGuide.NON_PAYED_STRING, String.valueOf(SituationOfGuide.NON_PAYED)));
		result.add(new LabelValueBean(SituationOfGuide.PAYED_STRING, String.valueOf(SituationOfGuide.PAYED)));
		return result;	
	}
    
	public String toString() {
		if (situation.intValue()== SituationOfGuide.ANNULLED) return SituationOfGuide.ANNULLED_STRING;
		if (situation.intValue()== SituationOfGuide.NON_PAYED) return SituationOfGuide.NON_PAYED_STRING;
		if (situation.intValue()== SituationOfGuide.PAYED) return SituationOfGuide.PAYED_STRING;
		return "ERRO!"; // Nunca e atingido
	}      


	/**
	 * @return
	 */
	public Integer getSituation() {
		return this.situation;
	}

	/**
	 * @param integer
	 */
	public void setSituation(Integer situation) {
		this.situation = situation;
	}

}