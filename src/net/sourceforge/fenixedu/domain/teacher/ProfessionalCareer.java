/*
 * Created on 13/Nov/2003
 *
 */
package net.sourceforge.fenixedu.domain.teacher;

import net.sourceforge.fenixedu.dataTransferObject.teacher.InfoProfessionalCareer;
import net.sourceforge.fenixedu.domain.ITeacher;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;

/**
 * @author Leonor Almeida
 * @author Sergio Montelobo
 * 
 */
public class ProfessionalCareer extends ProfessionalCareer_Base {

    public ProfessionalCareer() {
        super();
        setOjbConcreteClass(ProfessionalCareer.class.getName());
    }
	
    public ProfessionalCareer(ITeacher teacher, InfoProfessionalCareer infoProfessionalCareer) {
		if(teacher == null)
			throw new DomainException("The teacher should not be null!");

		setOjbConcreteClass(ProfessionalCareer.class.getName());
		setTeacher(teacher);
		setBasicProperties(infoProfessionalCareer);
    }
	
    
    public String toString() {
        String result = "[" + ProfessionalCareer.class.getName();
        result += ", beginYear=" + getBeginYear();
        result += ", endYear=" + getEndYear();
        result += ", entity=" + getEntity();
        result += ", function=" + getFunction();
        result += ", teacher=" + getTeacher();
        result += "]";
        return result;
    }
	
	public void edit(InfoProfessionalCareer infoProfessionalCareer) {
		
		setBasicProperties(infoProfessionalCareer);
	}
	
	private void setBasicProperties(InfoProfessionalCareer infoProfessionalCareer) {
		this.setBeginYear(infoProfessionalCareer.getBeginYear());
		this.setEndYear(infoProfessionalCareer.getEndYear());
    	this.setEntity(infoProfessionalCareer.getEntity());
    	this.setFunction(infoProfessionalCareer.getFunction());
		
	}

}
