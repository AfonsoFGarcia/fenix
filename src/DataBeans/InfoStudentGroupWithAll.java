/*
 * Created on 18/Jun/2004
 *
 */
package DataBeans;

import Dominio.IStudentGroup;

/**
 * @author T�nia Pous�o
 * 18/Jun/2004
 */
public class InfoStudentGroupWithAll extends InfoStudentGroup {
	public void copyFromDomain(IStudentGroup studentGroup) {
		super.copyFromDomain(studentGroup);
		if(studentGroup != null) {
			setInfoGroupProperties(InfoGroupPropertiesWithInfoExecutionCourse.newInfoFromDomain(studentGroup.getGroupProperties()));
			setInfoShift(InfoShift.newInfoFromDomain(studentGroup.getShift()));
		}
	}
	
	public static InfoStudentGroup newInfoFromDomain(IStudentGroup studentGroup) {
		InfoStudentGroupWithAll infoStudentGroup = null;
		if(studentGroup != null) {
			infoStudentGroup = new InfoStudentGroupWithAll();
			infoStudentGroup.copyFromDomain(studentGroup);
		}
		
		return infoStudentGroup;
	}
}
