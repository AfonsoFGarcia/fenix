/*
 * Created on 21/Apr/2004
 */

package Util.tests;

import java.util.Iterator;
import java.util.List;

import Util.FenixUtil;

/**
 * @author Susana Fernandes
 */

public class ResponseProcessing extends FenixUtil {

    public static final int SET = 1;

    public static final int ADD = 2;

    public static final int SUBTRACT = 3;

    public static final int MULTIPLY = 4;

    public static final int DIVIDE = 5;

    public static final String SET_STRING = "Set";

    public static final String ADD_STRING = "Add";

    public static final String SUBTRACT_STRING = "Subtract";

    public static final String MULTIPLY_STRING = "Multiply";

    public static final String DIVIDE_STRING = "Divide";

    private List responseConditions;

    private Double responseValue;

    private Integer action;

    private List feedback;

    private int responseProcessingId;

    private boolean fenixCorrectResponse;

    public ResponseProcessing(int id) {
        responseProcessingId = id;
    }

    public ResponseProcessing(List responseConditions, Double responseValue,
            Integer action, List feedback, boolean fenixCorrectResponse) {
        this.responseConditions = responseConditions;
        this.responseValue = responseValue;
        this.action = action;
        this.feedback = feedback;
        this.fenixCorrectResponse = fenixCorrectResponse;
    }

    public int getResponseProcessingId() {
        return responseProcessingId;
    }

    public void setResponseProcessingId(int responseProcessingId) {
        this.responseProcessingId = responseProcessingId;
    }

    public List getResponseConditions() {
        return responseConditions;
    }

    public void setResponseConditions(List responses) {
        this.responseConditions = responses;
    }

    public Double getResponseValue() {
        return responseValue;
    }

    public void setResponseValue(Double responseValue) {
        this.responseValue = responseValue;
    }

    public Integer getAction() {
        return action;
    }

    public void setAction(String actionString) {
        this.action = getActionCode(actionString);
    }

    public List getFeedback() {
        return feedback;
    }

    public void setFeedback(List feedback) {
        this.feedback = feedback;
    }

    public boolean isFenixCorrectResponse() {
        return fenixCorrectResponse;
    }

    public void setFenixCorrectResponse(boolean fenixCorrectResponse) {
        this.fenixCorrectResponse = fenixCorrectResponse;
    }

    private Integer getActionCode(String actionString) {
        if (actionString.equals(SET_STRING))
            return new Integer(SET);
        else if (actionString.equals(ADD_STRING))
            return new Integer(ADD);
        else if (actionString.equals(SUBTRACT_STRING))
            return new Integer(SUBTRACT);
        else if (actionString.equals(MULTIPLY_STRING))
            return new Integer(MULTIPLY);
        else if (actionString.equals(DIVIDE_STRING))
                return new Integer(DIVIDE);
        return null;
    }

    public boolean isAllCorrect(String[] studentResponse) {
        Iterator it = getResponseConditions().iterator();
        boolean match = false;
        while (it.hasNext()) {
            ResponseCondition responseCondition = (ResponseCondition) it.next();
            match = false;
            for (int i = 0; i < studentResponse.length; i++) {
                if (responseCondition.isCorrect(studentResponse[i])) {
                    match = true;
                }
            }
            if (!match) return false;
        }
        return true;
    }

    public boolean isAllCorrect(String studentResponse) {
        Iterator it = getResponseConditions().iterator();
        boolean match = false;
        while (it.hasNext()) {
            ResponseCondition responseCondition = (ResponseCondition) it.next();
            if (responseCondition.isCorrect(studentResponse))
                match = true;
            else
                return false;
        }
        if (!match) return false;
        return true;
    }

    public boolean isAllCorrect(Double studentResponse) {
        Iterator it = getResponseConditions().iterator();
        boolean match = false;
        while (it.hasNext()) {
            ResponseCondition responseCondition = (ResponseCondition) it.next();
            if (responseCondition.isCorrect(studentResponse))
                match = true;
            else
                return false;
        }
        if (!match) return false;
        return true;
    }

}