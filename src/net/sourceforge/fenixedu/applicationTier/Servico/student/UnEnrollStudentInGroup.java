package net.sourceforge.fenixedu.applicationTier.Servico.student;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.InvalidSituationServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NotAuthorizedException;
import net.sourceforge.fenixedu.applicationTier.strategy.groupEnrolment.strategys.GroupEnrolmentStrategyFactory;
import net.sourceforge.fenixedu.applicationTier.strategy.groupEnrolment.strategys.IGroupEnrolmentStrategy;
import net.sourceforge.fenixedu.applicationTier.strategy.groupEnrolment.strategys.IGroupEnrolmentStrategyFactory;
import net.sourceforge.fenixedu.domain.Attends;
import net.sourceforge.fenixedu.domain.Grouping;
import net.sourceforge.fenixedu.domain.Student;
import net.sourceforge.fenixedu.domain.StudentGroup;
import net.sourceforge.fenixedu.domain.StudentGroup;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentStudent;
import net.sourceforge.fenixedu.persistenceTier.IPersistentStudentGroup;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;
import pt.utl.ist.berserk.logic.serviceManager.IService;

/**
 * @author asnr and scpo
 * 
 */

public class UnEnrollStudentInGroup implements IService {

    public Boolean run(String userName, Integer studentGroupCode) throws FenixServiceException,
            ExcepcaoPersistencia {

        ISuportePersistente persistentSuport = PersistenceSupportFactory.getDefaultPersistenceSupport();

        IPersistentStudentGroup persistentStudentGroup = persistentSuport.getIPersistentStudentGroup();
        IPersistentStudent persistentStudent = persistentSuport.getIPersistentStudent();

        StudentGroup studentGroup = (StudentGroup) persistentStudentGroup.readByOID(
                StudentGroup.class, studentGroupCode);

        if (studentGroup == null) {
            throw new InvalidSituationServiceException();
        }

        Student student = persistentStudent.readByUsername(userName);

        Grouping groupProperties = studentGroup.getGrouping();

        Attends attend = groupProperties.getStudentAttend(student);

        if (attend == null) {
            throw new NotAuthorizedException();
        }
        
        IGroupEnrolmentStrategyFactory enrolmentGroupPolicyStrategyFactory = GroupEnrolmentStrategyFactory
                .getInstance();
        
        IGroupEnrolmentStrategy strategy = enrolmentGroupPolicyStrategyFactory
                .getGroupEnrolmentStrategyInstance(groupProperties);

        boolean resultEmpty = strategy.checkIfStudentGroupIsEmpty(attend,
                studentGroup);
        
        studentGroup.removeAttends(attend);
                               
        if (resultEmpty) {
            groupProperties.removeStudentGroups(studentGroup);
            studentGroup.setShift(null);
            persistentStudentGroup.deleteByOID(StudentGroup.class, studentGroup.getIdInternal());
            return Boolean.FALSE;
        }

        return Boolean.TRUE;
    }

}