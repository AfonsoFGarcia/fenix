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
public class TestType extends FenixUtil {
    public static final int EVALUATION = 1;

    public static final int STUDY = 2;

    public static final int INQUIRY = 3;

    public static final String EVALUATION_STRING = "Ficha de Avalia��o (guardar primeira resposta)";

    public static final String STUDY_STRING = "Ficha de Estudo (guardar �ltima resposta)";

    public static final String INQUIRY_STRING = "Inqu�rito";

    private final Integer type;

    public TestType(int type) {
	this.type = new Integer(type);
    }

    public TestType(Integer type) {
	this.type = type;
    }

    public TestType(String typeString) {
	this.type = getTypeCode(typeString);
    }

    public Integer getType() {
	return type;
    }

    public static List<LabelValueBean> getAllTypes() {
	List<LabelValueBean> result = new ArrayList<LabelValueBean>();
	result.add(new LabelValueBean(EVALUATION_STRING, new Integer(EVALUATION).toString()));
	result.add(new LabelValueBean(STUDY_STRING, new Integer(STUDY).toString()));
	result.add(new LabelValueBean(INQUIRY_STRING, new Integer(INQUIRY).toString()));
	return result;
    }

    public Integer getTypeCode(String typeName) {
	if (typeName.equals(EVALUATION_STRING))
	    return new Integer(EVALUATION);
	else if (typeName.equals(STUDY_STRING))
	    return new Integer(STUDY);
	else if (typeName.equals(INQUIRY_STRING))
	    return new Integer(INQUIRY);
	return null;
    }

    public String getTypeString() {
	if (type.intValue() == EVALUATION)
	    return new String(EVALUATION_STRING);
	else if (type.intValue() == STUDY)
	    return new String(STUDY_STRING);
	else if (type.intValue() == INQUIRY)
	    return new String(INQUIRY_STRING);

	return null;
    }

    @Override
    public boolean equals(Object obj) {
	boolean resultado = false;
	if (obj instanceof TestType) {
	    TestType tt = (TestType) obj;
	    resultado = this.getType().equals(tt.getType());
	}
	return resultado;
    }
}