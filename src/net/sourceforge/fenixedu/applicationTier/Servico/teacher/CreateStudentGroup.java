/*
 * Created on 8/Ago/2003
 */

package net.sourceforge.fenixedu.applicationTier.Servico.teacher;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.FenixService;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.domain.Attends;
import net.sourceforge.fenixedu.domain.Grouping;
import net.sourceforge.fenixedu.domain.Shift;
import net.sourceforge.fenixedu.domain.student.Registration;

/**
 * @author ansr & scpo
 */

public class CreateStudentGroup extends FenixService {

	private List buildStudentList(List<String> studentUserNames, Grouping grouping) throws FenixServiceException {

		List studentList = new ArrayList();
		for (final String studantUserName : studentUserNames) {
			Attends attend = grouping.getStudentAttend(studantUserName);
			Registration registration = attend.getRegistration();
			studentList.add(registration);
		}
		return studentList;
	}

	public Boolean run(Integer executionCourseID, Integer groupNumber, Integer groupingID, Integer shiftID, List studentUserNames)
			throws FenixServiceException {
		final Grouping grouping = rootDomainObject.readGroupingByOID(groupingID);

		if (grouping == null) {
			throw new FenixServiceException();
		}

		Shift shift = rootDomainObject.readShiftByOID(shiftID);

		List studentList = buildStudentList(studentUserNames, grouping);

		grouping.createStudentGroup(shift, groupNumber, studentList);

		return true;
	}
}