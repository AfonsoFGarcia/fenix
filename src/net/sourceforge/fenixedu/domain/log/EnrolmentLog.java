/*
 * Created on Nov 4, 2004
 */
package net.sourceforge.fenixedu.domain.log;

import net.sourceforge.fenixedu.domain.CurricularCourse;
import net.sourceforge.fenixedu.domain.ExecutionSemester;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.student.Registration;
import net.sourceforge.fenixedu.util.EnrolmentAction;

import org.joda.time.DateTime;

/**
 * @author nmgo
 * @author lmre
 */
public class EnrolmentLog extends EnrolmentLog_Base {

    public EnrolmentLog(EnrolmentAction action, Registration registration, CurricularCourse curricularCourse,
	    ExecutionSemester executionSemester, String who) {
	super();
	setRootDomainObject(RootDomainObject.getInstance());
	this.setDateDateTime(new DateTime());
	this.setAction(action);
	this.setStudent(registration);
	this.setCurricularCourse(curricularCourse);
	this.setExecutionPeriod(executionSemester);
	this.setWho(who);
    }

    public void delete() {
	removeRootDomainObject();
	removeStudent();
	removeCurricularCourse();
	removeExecutionPeriod();
	super.deleteDomainObject();
    }

}
