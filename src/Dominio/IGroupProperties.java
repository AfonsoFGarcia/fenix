/*
 * Created on 9/Mai/2003
 *
 * To change the template for this generated file go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
package Dominio;

import Util.EnrolmentGroupPolicyType;

/**
 * @author asnr and scpo
 *
 * To change the template for this generated type comment go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
public interface IGroupProperties extends IDomainObject{
	 
	
		public Integer getMaximumCapacity();
		public Integer getMinimumCapacity() ;
		public Integer getIdealCapacity();
		public EnrolmentGroupPolicyType getEnrolmentPolicy();
		public Integer getGroupMaximumNumber();
		public String getName();
		public IDisciplinaExecucao getExecutionCourse();

		public void setMaximumCapacity(Integer maximumCapacity);
		public void setMinimumCapacity(Integer minimumCapacity);
		public void setIdealCapacity(Integer idealCapacity);
		public void setEnrolmentPolicy(EnrolmentGroupPolicyType enrolmentPolicy);
		public void setGroupMaximumNumber(Integer groupMaximumNumber);
		public void setName(String name);
		public void setExecutionCourse(IDisciplinaExecucao executionCourse);	

}
