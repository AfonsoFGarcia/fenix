/*
 * Created on 13/Nov/2004
 *
 */
package net.sourceforge.fenixedu.applicationTier.Servico.student;

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
import net.sourceforge.fenixedu.domain.Grouping;
import net.sourceforge.fenixedu.domain.Shift;
import net.sourceforge.fenixedu.domain.Student;
import net.sourceforge.fenixedu.domain.StudentGroup;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;

/**
 * @author joaosa and rmalo
 * 
 */

public class EnrollGroupShift extends Service {

    public boolean run(Integer studentGroupCode, Integer groupPropertiesCode, Integer newShiftCode,
            String username) throws FenixServiceException, ExcepcaoPersistencia {
        Grouping groupProperties = (Grouping) persistentObject.readByOID(Grouping.class,
                groupPropertiesCode);
        if (groupProperties == null) {
            throw new ExistingServiceException();
        }

        StudentGroup studentGroup = (StudentGroup) persistentObject.readByOID(
                StudentGroup.class, studentGroupCode);
        if (studentGroup == null)
            throw new InvalidArgumentsServiceException();

        Shift shift = (Shift) persistentObject.readByOID(Shift.class, newShiftCode);
        if (groupProperties.getShiftType() == null || studentGroup.getShift() != null
                || (!groupProperties.getShiftType().equals(shift.getTipo()))) {
            throw new InvalidStudentNumberServiceException();
        }

        Student student = persistentSupport.getIPersistentStudent().readByUsername(username);

        IGroupEnrolmentStrategyFactory enrolmentGroupPolicyStrategyFactory = GroupEnrolmentStrategyFactory
                .getInstance();
        IGroupEnrolmentStrategy strategy = enrolmentGroupPolicyStrategyFactory
                .getGroupEnrolmentStrategyInstance(groupProperties);

        if (!strategy.checkStudentInGrouping(groupProperties, username)) {
            throw new NotAuthorizedException();
        }

        if (!checkStudentInStudentGroup(student, studentGroup)) {
            throw new InvalidSituationServiceException();
        }

        boolean result = strategy.checkNumberOfGroups(groupProperties, shift);
        if (!result) {
            throw new InvalidChangeServiceException();
        }
        studentGroup.setShift(shift);
        return true;
    }

    private boolean checkStudentInStudentGroup(Student student, StudentGroup studentGroup)
            throws FenixServiceException {

        for (final Attends attend : studentGroup.getAttends()) {
            if (attend.getAluno() == student) {
                return true;
            }
        }
        return false;
    }
}