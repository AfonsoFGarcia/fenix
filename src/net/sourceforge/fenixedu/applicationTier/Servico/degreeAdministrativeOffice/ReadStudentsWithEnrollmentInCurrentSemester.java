/*
 * Created on 18/Set/2004
 *
 */
package net.sourceforge.fenixedu.applicationTier.Servico.degreeAdministrativeOffice;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.dataTransferObject.InfoStudent;
import net.sourceforge.fenixedu.domain.Enrolment;
import net.sourceforge.fenixedu.domain.ExecutionSemester;
import net.sourceforge.fenixedu.domain.student.Registration;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;

/**
 * @author Nuno Correia
 * @author Ricardo Rodrigues
 * 
 */
public class ReadStudentsWithEnrollmentInCurrentSemester extends Service {

    public ReadStudentsWithEnrollmentInCurrentSemester() {
    }

    public List run(Integer fromNumber, Integer toNumber) throws ExcepcaoPersistencia {
        List infoStudentList = new ArrayList();
        List degreeNames = new ArrayList();
        List allStudentsData = new ArrayList();

        List studentsList = Registration.readAllStudentsBetweenNumbers(fromNumber, toNumber);

        for (int iter = 0; iter < studentsList.size(); iter++) {
            Registration registration = (Registration) studentsList.get(iter);
            // TODO se ele est� inscrito no semestre actual � porque j� pagou as
            // propinas...
            if (registration.getPayedTuition().booleanValue() && studentHasEnrollments(registration)) {
                InfoStudent infoStudentWithInfoPerson = InfoStudent
                        .newInfoFromDomain(registration);
                infoStudentList.add(infoStudentWithInfoPerson);
                degreeNames.add(registration.getActiveStudentCurricularPlan().getDegreeCurricularPlan()
                        .getDegree().getNome());
            }
        }

        allStudentsData.add(infoStudentList);
        allStudentsData.add(degreeNames);
        return allStudentsData;
    }

    private boolean studentHasEnrollments(Registration registration) throws ExcepcaoPersistencia {

        ExecutionSemester executionSemester = ExecutionSemester.readActualExecutionPeriod();

        List<Enrolment> enrollments = registration.getActiveStudentCurricularPlan()
                .getEnrolmentsByExecutionPeriod(executionSemester);

        for (int iter = 0; iter < enrollments.size(); iter++) {
            Enrolment enrollment = (Enrolment) enrollments.get(iter);
            if (enrollment.isEnrolmentStateApproved()
                    || enrollment.isTemporarilyEnroled()
                    || enrollment.isEnroled())
                return true;
        }
        return false;
    }

}