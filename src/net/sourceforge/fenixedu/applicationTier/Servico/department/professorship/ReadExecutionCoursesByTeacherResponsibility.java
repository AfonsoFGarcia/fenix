/*
 * Created on Aug 26, 2004
 *
 */
package net.sourceforge.fenixedu.applicationTier.Servico.department.professorship;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.FenixService;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionCourse;
import net.sourceforge.fenixedu.domain.Professorship;
import net.sourceforge.fenixedu.domain.Teacher;
import pt.ist.fenixWebFramework.services.Service;

/**
 * @author Jo�o Mota
 * 
 */
public class ReadExecutionCoursesByTeacherResponsibility extends FenixService {

    @Service
    public static List run(Integer teacherNumber) throws FenixServiceException {

	final List<InfoExecutionCourse> infoExecutionCourses = new ArrayList<InfoExecutionCourse>();

	Teacher teacher = Teacher.readByNumber(teacherNumber);

	final List<Professorship> responsibilities = teacher.responsibleFors();

	if (responsibilities != null) {
	    for (final Professorship professorship : responsibilities) {
		infoExecutionCourses.add(InfoExecutionCourse.newInfoFromDomain(professorship.getExecutionCourse()));
	    }
	}
	return infoExecutionCourses;
    }
}