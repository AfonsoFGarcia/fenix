/*
 * Created on 24/Jul/2003
 */
 
package ServidorAplicacao.strategy.groupEnrolment.strategys;

import java.util.Calendar;

import Dominio.IGroupProperties;
import Dominio.IStudentGroup;

/**
 * @author asnr and scpo
 *
 */

public class AtomicGroupEnrolmentStrategy extends GroupEnrolmentStrategy implements IGroupEnrolmentStrategy {
	
	
	public AtomicGroupEnrolmentStrategy(){
	}

	public boolean enrolmentPolicy(IGroupProperties groupProperties,int numberOfStudentsToEnrole,IStudentGroup studentGroup)
	{
		boolean result = false;
		if(checkNumberOfGroups(groupProperties) && checkEnrolmentDate(groupProperties,Calendar.getInstance()))
		{
			if(numberOfStudentsToEnrole >= groupProperties.getMinimumCapacity().intValue()&& numberOfStudentsToEnrole <= groupProperties.getMaximumCapacity().intValue())
				result = true;
		}			
		return result;			
				
	}
	
}
