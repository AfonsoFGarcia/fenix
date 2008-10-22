/**
 * Dec 7, 2005
 */
package net.sourceforge.fenixedu.applicationTier.Servico.credits;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.FenixService;
import net.sourceforge.fenixedu.dataTransferObject.credits.CreditLineDTO;
import net.sourceforge.fenixedu.domain.ExecutionSemester;
import net.sourceforge.fenixedu.domain.Teacher;
import net.sourceforge.fenixedu.domain.teacher.TeacherService;
import pt.ist.fenixWebFramework.services.Service;

/**
 * @author Ricardo Rodrigues
 * 
 */

public class ReadAllTeacherCredits extends FenixService {

    @Service
    public static List<CreditLineDTO> run(Integer teacherID) throws ParseException {

	List<CreditLineDTO> creditLines = new ArrayList<CreditLineDTO>();
	final Teacher teacher = rootDomainObject.readTeacherByOID(teacherID);

	ExecutionSemester executionSemester = ExecutionSemester.readStartExecutionSemesterForCredits();

	while (executionSemester != null) {

	    double managementCredits = teacher.getManagementFunctionsCredits(executionSemester);
	    double serviceExemptionsCredits = teacher.getServiceExemptionCredits(executionSemester);
	    double thesesCredits = teacher.getThesesCredits(executionSemester);
	    int mandatoryLessonHours = teacher.getMandatoryLessonHours(executionSemester);
	    TeacherService teacherService = teacher.getTeacherServiceByExecutionPeriod(executionSemester);

	    CreditLineDTO creditLineDTO = new CreditLineDTO(executionSemester, teacherService, managementCredits,
		    serviceExemptionsCredits, mandatoryLessonHours, teacher, thesesCredits);
	    creditLines.add(creditLineDTO);

	    if (executionSemester.isCurrent()) {
		break;
	    }

	    executionSemester = executionSemester.getNextExecutionPeriod();
	}

	return creditLines;
    }
}