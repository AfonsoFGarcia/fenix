/*
 * Created on 19/Ago/2003
 */
package net.sourceforge.fenixedu.util.tests;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.util.FenixUtil;

import org.apache.struts.util.LabelValueBean;

/**
 * @author Susana Fernandes
 */
public class CorrectionFormula extends FenixUtil {

    public static final int FENIX = 1;

    public static final int IMS = 2;

    public static final String FENIX_STRING = "Utilizar f�rmula do sistema.";

    public static final String LID_MULTIPLE_FORMULA = "<br/>cota��o * 2*(num de respostas certas escolhidas + num de respostas erradas escolhidas)/num de op��es -1";

    public static final String LID_SIMPLE_FORMULA = "<br/>cota��o * 1 / (num de op��es -1)";

    public static final String IMS_STRING = "Utilizar cota��es indicadas no ficheiro.";

    private Integer formula;

    public CorrectionFormula() {
    }

    public CorrectionFormula(int formula) {
	this.formula = new Integer(formula);
    }

    public CorrectionFormula(Integer formula) {
	this.formula = formula;
    }

    public Integer getFormula() {
	return formula;
    }

    public void setFormula(Integer formula) {
	this.formula = formula;
    }

    public static List<LabelValueBean> getFormulas() {
	List<LabelValueBean> result = new ArrayList<LabelValueBean>();
	result.add(new LabelValueBean(IMS_STRING, new Integer(IMS).toString()));
	// + LID_SIMPLE_FORMULA + LID_MULTIPLE_FORMULA,
	result.add(new LabelValueBean(FENIX_STRING, new Integer(FENIX).toString()));
	return result;
    }
}