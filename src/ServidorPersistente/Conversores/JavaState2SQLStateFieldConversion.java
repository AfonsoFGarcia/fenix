/*
 * JavaSituationValidation2SQLSituationValidationFieldConversion.java
 *
 * Created on 08 of February 2003, 11:17
 */

package ServidorPersistente.Conversores;

import org.apache.ojb.broker.accesslayer.conversions.FieldConversion;

import Util.State;
/**
 * 
 * @author Nuno & Joana
 *
 * To change the template for this generated type comment go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */

public class JavaState2SQLStateFieldConversion implements FieldConversion {
    
	/*
	 * @see FieldConversion#javaToSql(Object)
	 */
	public Object javaToSql(Object source)
	{
		if (source instanceof State)
		{
			State s = (State) source;
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
			return new State(src);
		}
		else
		{
			return source;
		}
	}    
}
