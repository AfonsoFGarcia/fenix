package net.sourceforge.fenixedu.applicationTier.Servico.masterDegree.administrativeOffice.marksManagement;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.domain.CurricularCourse;
import net.sourceforge.fenixedu.domain.ICurricularCourse;
import net.sourceforge.fenixedu.domain.IEmployee;
import net.sourceforge.fenixedu.domain.IEnrollment;
import net.sourceforge.fenixedu.domain.IEnrolmentEvaluation;
import net.sourceforge.fenixedu.domain.IPerson;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentCurricularCourse;
import net.sourceforge.fenixedu.persistenceTier.IPersistentEmployee;
import net.sourceforge.fenixedu.persistenceTier.IPersistentEnrollment;
import net.sourceforge.fenixedu.persistenceTier.IPersistentEnrolmentEvaluation;
import net.sourceforge.fenixedu.persistenceTier.IPessoaPersistente;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.OJB.PersistenceSupportFactory;
import net.sourceforge.fenixedu.util.EnrollmentState;
import net.sourceforge.fenixedu.util.EnrolmentEvaluationState;
import net.sourceforge.fenixedu.util.MarkType;
import pt.utl.ist.berserk.logic.serviceManager.IService;

/**
 * @author Fernanda Quit�rio 10/07/2003
 *  
 */
public class ConfirmStudentsFinalEvaluation implements IService {

    public Boolean run(Integer curricularCourseCode, String yearString, IUserView userView)
            throws FenixServiceException {

        try {
            ISuportePersistente sp = PersistenceSupportFactory.getDefaultPersistenceSupport();
            IPersistentCurricularCourse persistentCurricularCourse = sp.getIPersistentCurricularCourse();
            IPersistentEnrolmentEvaluation persistentEnrolmentEvaluation = sp
                    .getIPersistentEnrolmentEvaluation();
            IPersistentEnrollment persistentEnrolment = sp.getIPersistentEnrolment();
            IPessoaPersistente persistentPerson = sp.getIPessoaPersistente();

            //			employee
            IPerson person = persistentPerson.lerPessoaPorUsername(userView.getUtilizador());
            IEmployee employee = readEmployee(person);

            ICurricularCourse curricularCourse = (ICurricularCourse) persistentCurricularCourse
                    .readByOID(CurricularCourse.class, curricularCourseCode, false);

            List enrolments = null;
            if (yearString != null) {
                enrolments = persistentEnrolment.readByCurricularCourseAndYear(curricularCourse,
                        yearString);
            } else {
                enrolments = persistentEnrolment.readByCurricularCourse(curricularCourse);
            }
            List enrolmentEvaluations = new ArrayList();
            Iterator iterEnrolment = enrolments.listIterator();
            while (iterEnrolment.hasNext()) {
                IEnrollment enrolment = (IEnrollment) iterEnrolment.next();
                List allEnrolmentEvaluations = persistentEnrolmentEvaluation
                        .readEnrolmentEvaluationByEnrolment(enrolment);
                IEnrolmentEvaluation enrolmentEvaluation = (IEnrolmentEvaluation) allEnrolmentEvaluations
                        .get(allEnrolmentEvaluations.size() - 1);
                enrolmentEvaluations.add(enrolmentEvaluation);
            }

            if (enrolmentEvaluations != null && enrolmentEvaluations.size() > 0) {
                ListIterator iterEnrolmentEvaluations = enrolmentEvaluations.listIterator();
                while (iterEnrolmentEvaluations.hasNext()) {
                    IEnrolmentEvaluation enrolmentEvaluationElem = (IEnrolmentEvaluation) iterEnrolmentEvaluations
                            .next();
                    if (enrolmentEvaluationElem.getGrade() != null
                            && enrolmentEvaluationElem.getGrade().length() > 0
                            && enrolmentEvaluationElem.getEnrolmentEvaluationState().equals(
                                    EnrolmentEvaluationState.TEMPORARY_OBJ)) {

                        updateEnrolmentEvaluation(persistentEnrolmentEvaluation, persistentEnrolment,
                                employee, enrolmentEvaluationElem);
                    }
                }
            }
        } catch (ExcepcaoPersistencia ex) {
            ex.printStackTrace();
            FenixServiceException newEx = new FenixServiceException("");
            newEx.fillInStackTrace();
            throw newEx;
        }

        return Boolean.TRUE;
    }

    private void updateEnrolmentEvaluation(IPersistentEnrolmentEvaluation persistentEnrolmentEvaluation,
            IPersistentEnrollment persistentEnrolment, IEmployee employee,
            IEnrolmentEvaluation enrolmentEvaluationElem) throws ExcepcaoPersistencia {
        persistentEnrolmentEvaluation.simpleLockWrite(enrolmentEvaluationElem);

        enrolmentEvaluationElem.setEnrolmentEvaluationState(EnrolmentEvaluationState.FINAL_OBJ);
        Calendar calendar = Calendar.getInstance();
        enrolmentEvaluationElem.setWhen(calendar.getTime());
        enrolmentEvaluationElem.setEmployee(employee);
        enrolmentEvaluationElem.setObservation("Lan�amento de Notas na Secretaria");
        //TODO: checksum
        enrolmentEvaluationElem.setCheckSum("");

        // update state of enrolment: aproved, notAproved or notEvaluated
        updateEnrolmentState(persistentEnrolment, enrolmentEvaluationElem);
    }

    private void updateEnrolmentState(IPersistentEnrollment persistentEnrolment,
            IEnrolmentEvaluation enrolmentEvaluationElem) throws ExcepcaoPersistencia {
        IEnrollment enrolmentToEdit = enrolmentEvaluationElem.getEnrolment();
        persistentEnrolment.simpleLockWrite(enrolmentToEdit);

        EnrollmentState newEnrolmentState = EnrollmentState.APROVED;

        if (MarkType.getRepMarks().contains(enrolmentEvaluationElem.getGrade())) {
            newEnrolmentState = EnrollmentState.NOT_APROVED;
        } else if (MarkType.getNaMarks().contains(enrolmentEvaluationElem.getGrade())) {
            newEnrolmentState = EnrollmentState.NOT_EVALUATED;
        }
        enrolmentToEdit.setEnrollmentState(newEnrolmentState);
    }

    private IEmployee readEmployee(IPerson person) {
        IEmployee employee = null;
        IPersistentEmployee persistentEmployee;
        try {
            persistentEmployee = PersistenceSupportFactory.getDefaultPersistenceSupport().getIPersistentEmployee();
            employee = persistentEmployee.readByPerson(person.getIdInternal().intValue());
        } catch (ExcepcaoPersistencia e) {
            e.printStackTrace();
        }
        return employee;
    }
}