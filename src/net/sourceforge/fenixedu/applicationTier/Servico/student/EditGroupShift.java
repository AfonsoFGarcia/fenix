/*
 * Created on 07/Set/2003
 *
 */
package net.sourceforge.fenixedu.applicationTier.Servico.student;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.ExistingServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.InvalidArgumentsServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.InvalidChangeServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.InvalidSituationServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.InvalidStudentNumberServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NotAuthorizedException;
import net.sourceforge.fenixedu.applicationTier.strategy.groupEnrolment.strategys.GroupEnrolmentStrategyFactory;
import net.sourceforge.fenixedu.applicationTier.strategy.groupEnrolment.strategys.IGroupEnrolmentStrategy;
import net.sourceforge.fenixedu.applicationTier.strategy.groupEnrolment.strategys.IGroupEnrolmentStrategyFactory;
import net.sourceforge.fenixedu.domain.Attends;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.Grouping;
import net.sourceforge.fenixedu.domain.Shift;
import net.sourceforge.fenixedu.domain.Student;
import net.sourceforge.fenixedu.domain.StudentGroup;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;

import org.apache.struts.util.MessageResources;

import pt.utl.ist.fenix.tools.smtp.EmailSender;

/**
 * @author asnr and scpo
 * 
 */

public class EditGroupShift extends Service {

    private static final MessageResources messages = MessageResources
            .getMessageResources("resources/GlobalResources");

    public boolean run(Integer studentGroupID, Integer groupingID, Integer newShiftID, String username)
            throws FenixServiceException, ExcepcaoPersistencia {

        final Grouping grouping = rootDomainObject.readGroupingByOID(groupingID);
        if (grouping == null) {
            throw new ExistingServiceException();
        }

        final StudentGroup studentGroup = rootDomainObject.readStudentGroupByOID(studentGroupID);
        if (studentGroup == null) {
            throw new InvalidArgumentsServiceException();
        }

        final Shift shift = rootDomainObject.readShiftByOID(newShiftID);
        if (grouping.getShiftType() == null || !grouping.getShiftType().equals(shift.getTipo())) {
            throw new InvalidStudentNumberServiceException();
        }

        final Student student = Student.readByUsername(username);

        IGroupEnrolmentStrategyFactory enrolmentGroupPolicyStrategyFactory = GroupEnrolmentStrategyFactory
                .getInstance();
        IGroupEnrolmentStrategy strategy = enrolmentGroupPolicyStrategyFactory
                .getGroupEnrolmentStrategyInstance(grouping);

        if (!strategy.checkStudentInGrouping(grouping, username)) {
            throw new NotAuthorizedException();
        }

        if (!checkStudentInStudentGroup(student, studentGroup)) {
            throw new InvalidSituationServiceException();
        }

        boolean result = strategy.checkNumberOfGroups(grouping, shift);
        if (!result) {
            throw new InvalidChangeServiceException();
        }
        studentGroup.setShift(shift);

        informStudents(studentGroup, student, grouping);

        return true;
    }

    private boolean checkStudentInStudentGroup(Student student, StudentGroup studentGroup)
            throws FenixServiceException {
        boolean found = false;
        List studentGroupAttends = studentGroup.getAttends();
        Attends attend = null;
        Iterator iterStudentGroupAttends = studentGroupAttends.iterator();
        while (iterStudentGroupAttends.hasNext() && !found) {
            attend = ((Attends) iterStudentGroupAttends.next());
            if (attend.getAluno().equals(student)) {
                found = true;
            }
        }
        return found;
    }

    private void informStudents(final StudentGroup studentGroup, final Student student,
            final Grouping grouping) {
        final List<String> emails = new ArrayList<String>();
        for (final Attends attends : studentGroup.getAttends()) {
            emails.add(attends.getAluno().getPerson().getEmail());
        }

        final StringBuilder executionCourseNames = new StringBuilder();
        for (final ExecutionCourse executionCourse : grouping.getExecutionCourses()) {
            if (executionCourseNames.length() > 0) {
                executionCourseNames.append(", ");
            }
            executionCourseNames.append(executionCourse.getNome());
        }
        EmailSender.send("Fenix System", messages.getMessage("suporte.mail"), emails, null, null,
                messages.getMessage("message.subject.grouping.change"), messages.getMessage(
                        "message.body.grouping.change.shift", student.getNumber().toString(),
                        studentGroup.getGroupNumber().toString()));
    }

}