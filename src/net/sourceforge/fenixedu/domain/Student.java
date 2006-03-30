package net.sourceforge.fenixedu.domain;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import net.sourceforge.fenixedu.domain.curriculum.EnrollmentCondition;
import net.sourceforge.fenixedu.domain.curriculum.EnrollmentState;
import net.sourceforge.fenixedu.domain.degree.DegreeType;
import net.sourceforge.fenixedu.domain.person.IDDocumentType;
import net.sourceforge.fenixedu.domain.space.OldRoom;
import net.sourceforge.fenixedu.domain.studentCurricularPlan.StudentCurricularPlanState;
import net.sourceforge.fenixedu.domain.teacher.Advise;
import net.sourceforge.fenixedu.domain.teacher.AdviseType;
import net.sourceforge.fenixedu.util.EntryPhase;
import net.sourceforge.fenixedu.util.PeriodState;
import net.sourceforge.fenixedu.util.StudentState;

import org.apache.commons.beanutils.BeanComparator;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;

public class Student extends Student_Base {

    private transient Double approvationRatio;

    private transient Double arithmeticMean;

    private transient Integer approvedEnrollmentsNumber = 0;

    public Student() {
        super();
        setRootDomainObject(RootDomainObject.getInstance());
        this.setSpecialSeason(Boolean.FALSE);
    }

    public Student(Person person, Integer studentNumber, StudentKind studentKind, StudentState state,
            Boolean payedTuition, Boolean enrolmentForbidden, EntryPhase entryPhase,
            DegreeType degreeType) {
        this();
        setPayedTuition(payedTuition);
        setEnrollmentForbidden(enrolmentForbidden);
        setEntryPhase(entryPhase);
        setDegreeType(degreeType);
        setPerson(person);
        setState(state);
        setNumber(studentNumber);
        setStudentKind(studentKind);

        setFlunked(Boolean.FALSE);
        setRequestedChangeDegree(Boolean.FALSE);
        setRequestedChangeBranch(Boolean.FALSE);
    }

    public void delete() {
        removeRootDomainObject();
        super.deleteDomainObject();
    }

    public StudentCurricularPlan getActiveStudentCurricularPlan() {
        for (final StudentCurricularPlan studentCurricularPlan : getStudentCurricularPlans()) {
            if (studentCurricularPlan.getCurrentState() == StudentCurricularPlanState.ACTIVE) {
                return studentCurricularPlan;
            }
        }
        return null;
    }

    public boolean attends(final ExecutionCourse executionCourse) {
        for (final Attends attends : getAssociatedAttends()) {
            if (attends.getDisciplinaExecucao() == executionCourse) {
                return true;
            }
        }
        return false;
    }

    public List<WrittenEvaluation> getWrittenEvaluations(final ExecutionPeriod executionPeriod) {
        final List<WrittenEvaluation> result = new ArrayList<WrittenEvaluation>();
        for (final Attends attend : this.getAssociatedAttends()) {
            if (attend.getDisciplinaExecucao().getExecutionPeriod() == executionPeriod) {
                for (final Evaluation evaluation : attend.getDisciplinaExecucao()
                        .getAssociatedEvaluations()) {
                    if (evaluation instanceof WrittenEvaluation && !result.contains(evaluation)) {
                        result.add((WrittenEvaluation) evaluation);
                    }
                }
            }
        }
        return result;
    }

    public List<Exam> getEnroledExams(final ExecutionPeriod executionPeriod) {
        final List<Exam> result = new ArrayList<Exam>();
        for (final WrittenEvaluationEnrolment writtenEvaluationEnrolment : this
                .getWrittenEvaluationEnrolments()) {
            if (writtenEvaluationEnrolment.getWrittenEvaluation() instanceof Exam
                    && writtenEvaluationEnrolment.isForExecutionPeriod(executionPeriod)) {
                result.add((Exam) writtenEvaluationEnrolment.getWrittenEvaluation());
            }
        }
        return result;
    }

    public List<Exam> getUnenroledExams(final ExecutionPeriod executionPeriod) {
        final List<Exam> result = new ArrayList<Exam>();
        for (final Attends attend : this.getAssociatedAttends()) {
            if (attend.getDisciplinaExecucao().getExecutionPeriod() == executionPeriod) {
                for (final Evaluation evaluation : attend.getDisciplinaExecucao()
                        .getAssociatedEvaluations()) {
                    if (evaluation instanceof Exam && !this.isEnroledIn(evaluation)) {
                        result.add((Exam) evaluation);
                    }
                }
            }
        }
        return result;
    }

    public List<WrittenTest> getEnroledWrittenTests(final ExecutionPeriod executionPeriod) {
        final List<WrittenTest> result = new ArrayList<WrittenTest>();
        for (final WrittenEvaluationEnrolment writtenEvaluationEnrolment : this
                .getWrittenEvaluationEnrolments()) {
            if (writtenEvaluationEnrolment.getWrittenEvaluation() instanceof WrittenTest
                    && writtenEvaluationEnrolment.isForExecutionPeriod(executionPeriod)) {
                result.add((WrittenTest) writtenEvaluationEnrolment.getWrittenEvaluation());
            }
        }
        return result;
    }

    public List<WrittenTest> getUnenroledWrittenTests(final ExecutionPeriod executionPeriod) {
        final List<WrittenTest> result = new ArrayList<WrittenTest>();
        for (final Attends attend : this.getAssociatedAttends()) {
            if (attend.getDisciplinaExecucao().getExecutionPeriod() == executionPeriod) {
                for (final Evaluation evaluation : attend.getDisciplinaExecucao()
                        .getAssociatedEvaluations()) {
                    if (evaluation instanceof WrittenTest && !this.isEnroledIn(evaluation)) {
                        result.add((WrittenTest) evaluation);
                    }
                }
            }
        }
        return result;
    }

    public List<Project> getProjects(final ExecutionPeriod executionPeriod) {
        final List<Project> result = new ArrayList<Project>();
        for (final Attends attend : this.getAssociatedAttends()) {
            if (attend.getDisciplinaExecucao().getExecutionPeriod() == executionPeriod) {
                for (final Evaluation evaluation : attend.getDisciplinaExecucao()
                        .getAssociatedEvaluations()) {
                    if (evaluation instanceof Project) {
                        result.add((Project) evaluation);
                    }
                }
            }
        }
        return result;
    }

    public boolean isEnroledIn(final Evaluation evaluation) {
        for (final WrittenEvaluationEnrolment writtenEvaluationEnrolment : this
                .getWrittenEvaluationEnrolments()) {
            if (writtenEvaluationEnrolment.getWrittenEvaluation() == evaluation) {
                return true;
            }
        }
        return false;
    }

    public OldRoom getRoomFor(final WrittenEvaluation writtenEvaluation) {
        for (final WrittenEvaluationEnrolment writtenEvaluationEnrolment : this
                .getWrittenEvaluationEnrolments()) {
            if (writtenEvaluationEnrolment.getWrittenEvaluation() == writtenEvaluation) {
                return writtenEvaluationEnrolment.getRoom();
            }
        }
        return null;
    }

    public Double getApprovationRatio() {
        if (this.approvationRatio == null) {
            calculateApprovationRatioAndArithmeticMeanIfActive(null);
        }
        return this.approvationRatio;
    }

    public Double getArithmeticMean() {
        if (this.arithmeticMean == null) {
            calculateApprovationRatioAndArithmeticMeanIfActive(null);
        }
        return this.arithmeticMean;
    }

    public void calculateApprovationRatioAndArithmeticMeanIfActive(ExecutionYear currentExecutionYear) {

        int enrollmentsNumber = 0;
        int approvedEnrollmentsNumber = 0;
        int actualApprovedEnrollmentsNumber = 0;
        int totalGrade = 0;

        for (StudentCurricularPlan studentCurricularPlan : getStudentCurricularPlans()) {
            for (Enrolment enrolment : studentCurricularPlan.getEnrolments()) {
                if (enrolment.getCondition() != EnrollmentCondition.INVISIBLE
                        && (currentExecutionYear == null || enrolment.getExecutionPeriod()
                                .getExecutionYear() != currentExecutionYear)) {
                    enrollmentsNumber++;
                    if (enrolment.getEnrollmentState() == EnrollmentState.APROVED) {
                        actualApprovedEnrollmentsNumber++;

                        Integer finalGrade = enrolment.getFinalGrade();
                        if (finalGrade != null) {
                            approvedEnrollmentsNumber++;
                            totalGrade += finalGrade;
                        } else {
                            enrollmentsNumber--;
                        }
                    }
                }
            }
        }

        setApprovedEnrollmentsNumber(Integer.valueOf(actualApprovedEnrollmentsNumber));

        setApprovationRatio((enrollmentsNumber == 0) ? 0 : (double) approvedEnrollmentsNumber
                / enrollmentsNumber);
        setArithmeticMean((approvedEnrollmentsNumber == 0) ? 0 : (double) totalGrade
                / approvedEnrollmentsNumber);

    }

    private void setApprovationRatio(Double approvationRatio) {
        this.approvationRatio = approvationRatio;
    }

    private void setArithmeticMean(Double arithmeticMean) {
        this.arithmeticMean = arithmeticMean;
    }

    public Integer getApprovedEnrollmentsNumber() {
        if (this.approvedEnrollmentsNumber == null) {
            calculateApprovationRatioAndArithmeticMeanIfActive(null);
        }
        return approvedEnrollmentsNumber;
    }

    private void setApprovedEnrollmentsNumber(Integer approvedEnrollmentsNumber) {
        this.approvedEnrollmentsNumber = approvedEnrollmentsNumber;
    }

    public List<Advise> getAdvisesByTeacher(final Teacher teacher) {
        return (List<Advise>) CollectionUtils.select(getAdvises(), new Predicate() {

            public boolean evaluate(Object arg0) {
                Advise advise = (Advise) arg0;
                return advise.getTeacher() == teacher;
            }
        });
    }

    public List<Advise> getAdvisesByType(final AdviseType adviseType) {
        return (List<Advise>) CollectionUtils.select(getAdvises(), new Predicate() {
            public boolean evaluate(Object arg0) {
                Advise advise = (Advise) arg0;
                return advise.getAdviseType().equals(adviseType);
            }
        });
    }

    public Set<Attends> getOrderedAttends() {
        final Set<Attends> result = new TreeSet<Attends>(Attends.ATTENDS_COMPARATOR);
        result.addAll(getAssociatedAttends());
        return result;
    }

    public int countCompletedCoursesForActiveUndergraduateCurricularPlan() {
        return getActiveStudentCurricularPlan().getAprovedEnrolments().size();
    }

    public List<StudentCurricularPlan> getStudentCurricularPlansByStateAndType(
            StudentCurricularPlanState state, DegreeType type) {
        List<StudentCurricularPlan> result = new ArrayList<StudentCurricularPlan>();
        for (StudentCurricularPlan studentCurricularPlan : this.getStudentCurricularPlans()) {
            if (studentCurricularPlan.getCurrentState().equals(state)
                    && studentCurricularPlan.getDegreeCurricularPlan().getDegree().getTipoCurso()
                            .equals(type)) {
                result.add(studentCurricularPlan);
            }
        }
        return result;
    }

    public List<Attends> readAttendsInCurrentExecutionPeriod() {
        List<Attends> attends = new ArrayList<Attends>();
        for (Attends attend : this.getAssociatedAttends()) {
            if (attend.getDisciplinaExecucao().getExecutionPeriod().getState().equals(
                    PeriodState.CURRENT)) {
                attends.add(attend);
            }
        }
        return attends;
    }

    public List<Attends> readAttendsByExecutionPeriod(ExecutionPeriod executionPeriod) {
        List<Attends> attends = new ArrayList<Attends>();
        for (Attends attend : this.getAssociatedAttends()) {
            if (attend.getDisciplinaExecucao().getExecutionPeriod().equals(executionPeriod)) {
                attends.add(attend);
            }
        }
        return attends;
    }

    public Attends readAttendByExecutionCourse(ExecutionCourse executionCourse) {
        for (Attends attend : this.getAssociatedAttends()) {
            if (attend.getDisciplinaExecucao().equals(executionCourse)) {
                return attend;
            }
        }
        return null;
    }

    public static Student readByUsername(String username) {
        for (Student student : RootDomainObject.getInstance().getStudents()) {
            if (student.getPerson() != null
                    && student.getPerson().getUsername().equalsIgnoreCase(username)) {
                return student;
            }
        }
        return null;
    }

    public static Student readStudentByNumberAndDegreeType(Integer number, DegreeType degreeType) {
        for (Student student : RootDomainObject.getInstance().getStudents()) {
            if (student.getNumber().equals(number) && student.getDegreeType().equals(degreeType)) {
                return student;
            }
        }
        return null;
    }

    public static List<Student> readMasterDegreeStudentsByNameDocIDNumberIDTypeAndStudentNumber(
            String studentName, String docIdNumber, IDDocumentType idType, Integer studentNumber) {

        List<Student> students = new ArrayList();
        for (Student student : RootDomainObject.getInstance().getStudents()) {
            Person person = student.getPerson();
            if (student.getDegreeType().equals(DegreeType.MASTER_DEGREE)
                    && ((studentName != null && person.getName().equals(studentName)) || studentName == null)
                    && ((docIdNumber != null && person.getDocumentIdNumber().equals(docIdNumber)) || docIdNumber == null)
                    && ((idType != null && person.getIdDocumentType().equals(idType)) || idType == null)
                    && ((studentNumber != null && student.getNumber().equals(studentNumber)) || studentNumber == null)) {

                students.add(student);
            }
        }
        return students;
    }

    public static List<Student> readAllStudentsBetweenNumbers(Integer fromNumber, Integer toNumber) {
        int fromNumberInt = fromNumber.intValue();
        int toNumberInt = toNumber.intValue();

        int studentNumberInt;
        List<Student> students = new ArrayList();
        for (Student student : RootDomainObject.getInstance().getStudents()) {
            studentNumberInt = student.getNumber().intValue();
            if (studentNumberInt >= fromNumberInt && studentNumberInt <= toNumberInt) {
                students.add(student);
            }
        }
        return students;
    }

    public static List<Student> readStudentsByDegreeType(DegreeType degreeType) {
        List<Student> students = new ArrayList();
        for (Student student : RootDomainObject.getInstance().getStudents()) {
            if (student.getDegreeType().equals(degreeType)) {
                students.add(student);
            }
        }
        return students;
    }

    public static Integer generateStudentNumber(DegreeType degreeType) {
        Integer number = Integer.valueOf(0);
        List<Student> students = readStudentsByDegreeType(degreeType);
        Collections.sort(students, new BeanComparator("number"));
        
        if (!students.isEmpty()) {
            number = students.get(0).getNumber();
        }
        
        // FIXME: ISTO E UMA SOLUCAO TEMPORARIA DEVIDO A EXISTIREM ALUNOS
        // NA SECRETARIA QUE
        // POR UM MOTIVO OU OUTRO NAO SE ENCONTRAM NA BASE DE DADOS
        if (degreeType.equals(DegreeType.MASTER_DEGREE) && (number.intValue() < 5411)) {
            number = Integer.valueOf(5411);
        }
        
        return Integer.valueOf(number.intValue() + 1);
    }
}
