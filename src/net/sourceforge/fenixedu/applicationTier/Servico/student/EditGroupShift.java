/*
 * Created on 07/Set/2003
 *
 */
package net.sourceforge.fenixedu.applicationTier.Servico.student;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.ResourceBundle;

import org.apache.struts.util.MessageResources;

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
import net.sourceforge.fenixedu.util.EMail;

/**
 * @author asnr and scpo
 * 
 */

public class EditGroupShift extends Service {

    public String mailServer() {
        final String server = ResourceBundle.getBundle("SMTPConfiguration").getString("mailSender.server.url");
        return (server != null) ? server : "mail.adm";
    }

    private static final MessageResources messages = MessageResources.getMessageResources("resources/GlobalResources");

    public boolean run(Integer studentGroupID, Integer groupingID, Integer newShiftID, String username)
            throws FenixServiceException, ExcepcaoPersistencia {
        final Grouping grouping = (Grouping) persistentObject.readByOID(Grouping.class,
                groupingID);
        if (grouping == null) {
            throw new ExistingServiceException();
        }

        final StudentGroup studentGroup = (StudentGroup) persistentObject.readByOID(
                StudentGroup.class, studentGroupID);
        if (studentGroup == null) {
            throw new InvalidArgumentsServiceException();
        }

        final Shift shift = (Shift) persistentObject.readByOID(Shift.class, newShiftID);
        if (grouping.getShiftType() == null || !grouping.getShiftType().equals(shift.getTipo())) {
            throw new InvalidStudentNumberServiceException();
        }

        final Student student = persistentSupport.getIPersistentStudent().readByUsername(username);

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

    private void informStudents(final StudentGroup studentGroup, final Student student, final Grouping grouping) {
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
        EMail.send(mailServer(), "Fenix System", messages.getMessage("suporte.mail"),
                messages.getMessage("message.subject.grouping.change"), emails, new ArrayList(), new ArrayList(),
                messages.getMessage("message.body.grouping.change.shift", student.getNumber().toString(),
                        studentGroup.getGroupNumber().toString()));
    }

}