/**
 * 
 */
package net.sourceforge.fenixedu.presentationTier.Action.administrativeOffice.lists;

import java.util.TreeSet;

import net.sourceforge.fenixedu.domain.Degree;
import net.sourceforge.fenixedu.injectionCode.AccessControl;
import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;
import pt.ist.fenixWebFramework.struts.annotations.ExceptionHandling;
import pt.ist.fenixWebFramework.struts.annotations.Exceptions;
import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;
import pt.ist.fenixWebFramework.struts.annotations.Tile;

/**
 * @author - �ngela Almeida (argelina@ist.utl.pt)
 * @author - Shezad Anavarali (shezad@ist.utl.pt)
 * 
 */

@Mapping(path = "/studentsListByCurricularCourse", module = "academicAdminOffice")
@Forwards( { @Forward(name = "chooseCurricularCourse", path = "/academicAdminOffice/lists/chooseCurricularCourses.jsp"),
	@Forward(name = "studentByCurricularCourse", path = "/academicAdminOffice/lists/studentsByCurricularCourses.jsp") })
public class StudentsListByCurricularCourseDA extends
	net.sourceforge.fenixedu.presentationTier.Action.commons.administrativeOffice.lists.StudentsListByCurricularCourseDA {

    @Override
    protected TreeSet<Degree> getAdministratedDegrees() {
	return new TreeSet<Degree>(AccessControl.getPerson().getEmployee().getAdministrativeOffice().getAdministratedDegrees());
    }
}
