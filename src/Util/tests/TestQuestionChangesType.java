/*
 * Created on Oct 20, 2003
 */
package Util.tests;

import java.util.ArrayList;
import java.util.List;

import org.apache.struts.util.LabelValueBean;

import Util.FenixUtil;

/**
 * @author Susana Fernandes
 */
public class TestQuestionChangesType extends FenixUtil
{
    public static final int CHANGE_VARIATION = 1;

    public static final int CHANGE_EXERCISE = 2;

    public static final String CHANGE_VARIATION_STRING = "Outra varia��o do mesmo exerc�cio";

    public static final String CHANGE_EXERCISE_STRING = "Outro exerc�cio";

    private Integer type;

    private String typeString;

    public TestQuestionChangesType() {
    }

    public TestQuestionChangesType(int type) {
        this.type = new Integer(type);
    }

    public TestQuestionChangesType(Integer type) {
        this.type = type;
    }

    public TestQuestionChangesType(String typeString) {
        this.type = getTypeCode(typeString);
    }

    public Integer getType()
    {
        return type;
    }

    public void setType(int type)
    {
        this.type = new Integer(type);
    }

    public void setType(Integer type)
    {
        this.type = type;
    }

    public List getAllTypes()
    {
        List result = new ArrayList();
        result
                .add(new LabelValueBean(CHANGE_VARIATION_STRING, new Integer(CHANGE_VARIATION)
                        .toString()));
        result.add(new LabelValueBean(CHANGE_EXERCISE_STRING, new Integer(CHANGE_EXERCISE).toString()));
        return result;
    }

    public Integer getTypeCode(String typeName)
    {
        if (typeName.equals(CHANGE_VARIATION_STRING))
            return new Integer(CHANGE_VARIATION);
        else
            if (typeName.equals(CHANGE_EXERCISE_STRING))
                return new Integer(CHANGE_EXERCISE);
        return null;
    }

    public String getTypeString()
    {
        if (type.intValue() == CHANGE_VARIATION)
            return new String(CHANGE_VARIATION_STRING);
        else
            if (type.intValue() == CHANGE_EXERCISE)
                return new String(CHANGE_EXERCISE_STRING);
        return null;
    }

}