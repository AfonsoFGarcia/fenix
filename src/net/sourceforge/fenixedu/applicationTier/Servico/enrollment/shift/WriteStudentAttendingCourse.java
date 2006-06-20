package net.sourceforge.fenixedu.applicationTier.Servico.enrollment.shift;

import java.util.Iterator;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoStudent;
import net.sourceforge.fenixedu.domain.Attends;
import net.sourceforge.fenixedu.domain.CurricularCourse;

import net.sourceforge.fenixedu.domain.Enrolment;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.Student;
import net.sourceforge.fenixedu.domain.StudentCurricularPlan;
import net.sourceforge.fenixedu.domain.degree.DegreeType;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;

public class WriteStudentAttendingCourse extends Service {

    public class ReachedAttendsLimitServiceException extends FenixServiceException {

    }

    public Boolean run(InfoStudent infoStudent, Integer executionCourseId) throws FenixServiceException,
            ExcepcaoPersistencia {
        
        Student student = rootDomainObject.readStudentByOID(infoStudent.getIdInternal());
        infoStudent.setNumber(student.getNumber());

        if (infoStudent == null) {
            return new Boolean(false);
        }

        if (executionCourseId != null) {
            StudentCurricularPlan studentCurricularPlan = findStudentCurricularPlan(infoStudent);

            ExecutionCourse executionCourse = findExecutionCourse(executionCourseId);

            // Read every course the student attends to:
            List attends = student.readAttendsInCurrentExecutionPeriod();

            if (attends != null && attends.size() >= 8) {
                throw new ReachedAttendsLimitServiceException();
            }

            Attends attendsEntry = studentCurricularPlan.getStudent().readAttendByExecutionCourse(
                    executionCourse);

            if (attendsEntry == null) {
                attendsEntry = new Attends();
                attendsEntry.setAluno(studentCurricularPlan.getStudent());
                attendsEntry.setDisciplinaExecucao(executionCourse);

                findEnrollmentForAttend(studentCurricularPlan, executionCourse, attendsEntry);

            }
        }
        return new Boolean(true);
    }

    private void findEnrollmentForAttend(StudentCurricularPlan studentCurricularPlan,
            ExecutionCourse executionCourse, Attends attendsEntry) throws ExcepcaoPersistencia {
        // checks if there is an enrollment for this attend
        Iterator iterCurricularCourses = executionCourse.getAssociatedCurricularCourses().iterator();
        while (iterCurricularCourses.hasNext()) {
            CurricularCourse curricularCourseElem = (CurricularCourse) iterCurricularCourses.next();

            Enrolment enrollment = studentCurricularPlan
                    .getEnrolmentByCurricularCourseAndExecutionPeriod(curricularCourseElem,
                            executionCourse.getExecutionPeriod());

            if (enrollment != null) {
                attendsEntry.setEnrolment(enrollment);
                break;
            }
        }
    }

    private ExecutionCourse findExecutionCourse(Integer executionCourseId) throws FenixServiceException,
            ExcepcaoPersistencia {
        ExecutionCourse executionCourse = rootDomainObject.readExecutionCourseByOID(executionCourseId);

        if (executionCourse == null) {
            throw new FenixServiceException("noExecutionCourse");
        }
        return executionCourse;
    }

    private StudentCurricularPlan findStudentCurricularPlan(InfoStudent infoStudent) throws FenixServiceException{
    	
    	Student student = Student.readStudentByNumberAndDegreeType(infoStudent.getNumber(), DegreeType.DEGREE);
    	StudentCurricularPlan studentCurricularPlan = null;
    	if(student != null) {
    		studentCurricularPlan = student.getActiveOrConcludedStudentCurricularPlan();
    	}
        if (studentCurricularPlan == null) {
        	student = Student.readStudentByNumberAndDegreeType(infoStudent.getNumber(), DegreeType.MASTER_DEGREE);
        	if(student != null) {
        		studentCurricularPlan = student.getActiveOrConcludedStudentCurricularPlan();
        	}
        }
        
        if (studentCurricularPlan == null) {

            throw new FenixServiceException("noStudent");

        }

        return studentCurricularPlan;
    }
}