/*
 * Created on 23/Abr/2003
 *
 * 
 */
package net.sourceforge.fenixedu.domain;

/**
 * @author Jo�o Mota
 * 
 * 
 */
public class EvaluationMethod extends EvaluationMethod_Base {

    public String toString() {
        String result = "[EvaluationMethod";
        result += ", codInt=" + getIdInternal();
        result += ", evaluationElements =" + getEvaluationElements();
        result += ", executionCourse =" + getExecutionCourse();
        result += "]";
        return result;
    }
    
    public void edit(String evaluationElements, String evaluationElementsEng) {
        if (evaluationElements == null || evaluationElementsEng == null)
            throw new NullPointerException();
        
        setEvaluationElements(evaluationElements);
        setEvaluationElementsEn(evaluationElementsEng);
    }
    
    public void delete() {
        setExecutionCourse(null);        
    }

}
