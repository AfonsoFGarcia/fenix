package net.sourceforge.fenixedu.applicationTier.Servico.degree.finalProject;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Filtro.credits.EditTeacherDegreeFinalProjectStudentAuthorization;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NotAuthorizedException;
import net.sourceforge.fenixedu.dataTransferObject.InfoTeacher;
import net.sourceforge.fenixedu.dataTransferObject.degree.finalProject.InfoTeacherDegreeFinalProjectStudent;
import net.sourceforge.fenixedu.dataTransferObject.degree.finalProject.InfoTeacherDegreeFinalProjectStudentWithStudentAndPerson;
import net.sourceforge.fenixedu.domain.ExecutionSemester;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.Teacher;
import net.sourceforge.fenixedu.domain.degree.DegreeType;
import net.sourceforge.fenixedu.domain.degree.finalProject.TeacherDegreeFinalProjectStudent;
import net.sourceforge.fenixedu.domain.student.Registration;
import pt.ist.fenixWebFramework.services.Service;

public class EditTeacherDegreeFinalProjectStudentByOID {

    protected void run(Integer objectID, InfoTeacherDegreeFinalProjectStudent infoTeacherDegreeFinalProjectStudent)
            throws FenixServiceException {

        final Registration registration =
                Registration.readStudentByNumberAndDegreeType(infoTeacherDegreeFinalProjectStudent.getInfoStudent().getNumber(),
                        DegreeType.DEGREE);
        if (registration == null) {
            throw new FenixServiceException("message.student-not-found");
        }

        final ExecutionSemester executionSemester =
                RootDomainObject.getInstance().readExecutionSemesterByOID(infoTeacherDegreeFinalProjectStudent.getInfoExecutionPeriod()
                        .getIdInternal());
        if (executionSemester == null) {
            throw new FenixServiceException("message.execution-period-not-found");
        }

        final InfoTeacher infoTeacher = infoTeacherDegreeFinalProjectStudent.getInfoTeacher();
        final Teacher teacher = RootDomainObject.getInstance().readTeacherByOID(infoTeacher.getIdInternal());
        if (teacher == null) {
            throw new FenixServiceException("message.teacher-not-found");
        }

        checkStudentFinalDegreeProjectPercentage(registration, teacher, executionSemester,
                infoTeacherDegreeFinalProjectStudent.getPercentage());

        TeacherDegreeFinalProjectStudent teacherDegreeFinalProjectStudent =
                getTeacherDegreeFinalProjectStudentFor(teacher, registration, executionSemester);
        if (teacherDegreeFinalProjectStudent == null) {
            teacherDegreeFinalProjectStudent = new TeacherDegreeFinalProjectStudent(executionSemester, teacher, registration);
        }
        teacherDegreeFinalProjectStudent.setPercentage(infoTeacherDegreeFinalProjectStudent.getPercentage());

    }

    private void checkStudentFinalDegreeProjectPercentage(final Registration registration, final Teacher teacher,
            final ExecutionSemester executionSemester, Double percentage) throws StudentPercentageExceed {

        for (final TeacherDegreeFinalProjectStudent teacherDegreeFinalProjectStudent : registration
                .getTeacherDegreeFinalProjectStudent()) {
            if (teacherDegreeFinalProjectStudent.getExecutionPeriod() == executionSemester
                    && teacherDegreeFinalProjectStudent.getTeacher() != teacher) {
                percentage += teacherDegreeFinalProjectStudent.getPercentage();
            }
        }
        if (percentage > 100) {
            final List<InfoTeacherDegreeFinalProjectStudent> infoTeacherDegreeFinalProjectStudentList =
                    new ArrayList<InfoTeacherDegreeFinalProjectStudent>(registration.getTeacherDegreeFinalProjectStudentCount());
            for (final TeacherDegreeFinalProjectStudent teacherDegreeFinalProjectStudent : registration
                    .getTeacherDegreeFinalProjectStudent()) {
                if (teacherDegreeFinalProjectStudent.getExecutionPeriod() == executionSemester) {
                    infoTeacherDegreeFinalProjectStudentList.add(InfoTeacherDegreeFinalProjectStudentWithStudentAndPerson
                            .newInfoFromDomain(teacherDegreeFinalProjectStudent));
                }
            }
            throw new StudentPercentageExceed(infoTeacherDegreeFinalProjectStudentList);
        }
    }

    private TeacherDegreeFinalProjectStudent getTeacherDegreeFinalProjectStudentFor(final Teacher teacher,
            final Registration registration, final ExecutionSemester executionSemester) {
        for (final TeacherDegreeFinalProjectStudent teacherDegreeFinalProjectStudent : registration
                .getTeacherDegreeFinalProjectStudent()) {
            if (teacherDegreeFinalProjectStudent.getExecutionPeriod() == executionSemester
                    && teacherDegreeFinalProjectStudent.getTeacher() == teacher) {
                return teacherDegreeFinalProjectStudent;
            }
        }
        return null;
    }

    public class StudentPercentageExceed extends FenixServiceException {
        private final List infoTeacherDegreeFinalProjectStudentList;

        public StudentPercentageExceed(List infoTeacherDegreeFinalProjectStudentList) {
            this.infoTeacherDegreeFinalProjectStudentList = infoTeacherDegreeFinalProjectStudentList;
        }

        public List getInfoTeacherDegreeFinalProjectStudentList() {
            return this.infoTeacherDegreeFinalProjectStudentList;
        }
    }

    // Service Invokers migrated from Berserk

    private static final EditTeacherDegreeFinalProjectStudentByOID serviceInstance =
            new EditTeacherDegreeFinalProjectStudentByOID();

    @Service
    public static void runEditTeacherDegreeFinalProjectStudentByOID(Integer objectID,
            InfoTeacherDegreeFinalProjectStudent infoTeacherDegreeFinalProjectStudent) throws FenixServiceException,
            NotAuthorizedException {
        EditTeacherDegreeFinalProjectStudentAuthorization.instance.execute(infoTeacherDegreeFinalProjectStudent);
        serviceInstance.run(objectID, infoTeacherDegreeFinalProjectStudent);
    }

}