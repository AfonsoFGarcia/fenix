/*
 * Created on 15/Nov/2003
 *
 */
package net.sourceforge.fenixedu.domain.teacher;

import net.sourceforge.fenixedu.dataTransferObject.teacher.InfoExternalActivity;
import net.sourceforge.fenixedu.domain.ITeacher;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;

/**
 * @author Leonor Almeida
 * @author Sergio Montelobo
 * 
 */
public class ExternalActivity extends ExternalActivity_Base {
	
	public ExternalActivity() {
		super();
	}
	
	public ExternalActivity(ITeacher teacher, InfoExternalActivity infoExternalActivity) {
		if(teacher == null)
			throw new DomainException("The teacher should not be null!");

		setTeacher(teacher);
		this.setActivity(infoExternalActivity.getActivity());
	}

	public void delete() {
		removeTeacher();
		super.deleteDomainObject();
	}

	public void edit(InfoExternalActivity infoExternalActivity) {
		
		this.setActivity(infoExternalActivity.getActivity());

	}
}
