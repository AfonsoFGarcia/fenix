/*
 * JavaStudentState2SqlStudentStateFieldConversion.java
 *
 * Created on 21 de Novembro de 2002, 15:57
 */

package ServidorPersistente.Conversores;

import org.apache.ojb.broker.accesslayer.conversions.FieldConversion;

import Util.EnrolmentEvaluationState;

public class JavaEnrolmentEvaluationState2SqlEnrolmentEvaluationStateFieldConversion implements FieldConversion {
    
    /*
     * @see FieldConversion#javaToSql(Object)
     */
    public Object javaToSql(Object source)
    {
        if (source instanceof EnrolmentEvaluationState)
        {
            EnrolmentEvaluationState s = (EnrolmentEvaluationState) source;
            return s.getState();
        }
        else {
            return source;
        }
    }    

   /*
     * @see FieldConversion#sqlToJava(Object)
     */
    public Object sqlToJava(Object source)
    {
        if (source instanceof Integer)
        {
            Integer src = (Integer) source;
            return new EnrolmentEvaluationState(src);
        }
        else
        {
            return source;
        }
    }    
}
