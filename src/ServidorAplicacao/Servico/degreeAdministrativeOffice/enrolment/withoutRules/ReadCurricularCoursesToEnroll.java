/*
 * Created on 18/Fev/2004
 *  
 */
package ServidorAplicacao.Servico.degreeAdministrativeOffice.enrolment.withoutRules;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.beanutils.BeanComparator;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;
import org.apache.commons.collections.Transformer;

import pt.utl.ist.berserk.logic.serviceManager.IService;
import DataBeans.InfoExecutionYear;
import DataBeans.InfoStudent;
import DataBeans.InfoStudentCurricularPlan;
import DataBeans.util.Cloner;
import Dominio.CursoExecucao;
import Dominio.ICurricularCourse;
import Dominio.ICurricularCourseScope;
import Dominio.ICursoExecucao;
import Dominio.IDegreeCurricularPlan;
import Dominio.IEnrollment;
import Dominio.IStudentCurricularPlan;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorAplicacao.strategy.enrolment.context.InfoStudentEnrollmentContext;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.ICursoExecucaoPersistente;
import ServidorPersistente.IPersistentEnrolment;
import ServidorPersistente.IStudentCurricularPlanPersistente;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;
import Util.CurricularCourseType;
import Util.EnrolmentState;
import Util.TipoCurso;

/**
 * @author T�nia Pous�o
 *  
 */
public class ReadCurricularCoursesToEnroll implements IService {
    private static final int MAX_CURRICULAR_YEARS = 5;

    private static final int MAX_CURRICULAR_SEMESTERS = 2;

    public ReadCurricularCoursesToEnroll() {
    }

    public Object run(InfoStudent infoStudent, TipoCurso degreeType,
            InfoExecutionYear infoExecutionYear, Integer executionDegreeID,
            List curricularYearsList, List curricularSemestersList)
            throws FenixServiceException {
        InfoStudentEnrollmentContext infoStudentEnrolmentContext = null;

        try {
            ISuportePersistente sp = SuportePersistenteOJB.getInstance();

            //Execution Degree
            ICursoExecucaoPersistente persistentExecutionDegree = sp
                    .getICursoExecucaoPersistente();
            ICursoExecucao executionDegree = (ICursoExecucao) persistentExecutionDegree
                    .readByOID(CursoExecucao.class, executionDegreeID);
            if (executionDegree == null) {
                throw new FenixServiceException("error.degree.noData");
            }

            //Degree Curricular Plan
            IDegreeCurricularPlan degreeCurricularPlan = executionDegree
                    .getCurricularPlan();

            if (degreeCurricularPlan == null
                    && degreeCurricularPlan.getCurricularCourses() == null) {
                throw new FenixServiceException("error.degree.noData");
            }

            // filters a list of curricular courses by all of its scopes that
            // matters in relation to the selected semester and the selected
            // year.
            List curricularCoursesFromDegreeCurricularPlan = null;
            if ((curricularYearsList == null || curricularYearsList.size() <= 0)
                    && (curricularSemestersList == null || curricularSemestersList
                            .size() <= 0)) {
                curricularCoursesFromDegreeCurricularPlan = filterOptionalCourses(degreeCurricularPlan
                        .getCurricularCourses());
            } else {

                final List curricularYearsListFinal = verifyYears(curricularYearsList);
                final List curricularSemestersListFinal = verifySemesters(curricularSemestersList);

                List result = (List) CollectionUtils.select(
                        degreeCurricularPlan.getCurricularCourses(),
                        new Predicate() {

                            public boolean evaluate(Object arg0) {
                                boolean result = false;
                                if (arg0 instanceof ICurricularCourse) {
                                    ICurricularCourse curricularCourse = (ICurricularCourse) arg0;
                                    List scopes = curricularCourse.getScopes();
                                    Iterator iter = scopes.iterator();
                                    while (iter.hasNext() && !result) {
                                        ICurricularCourseScope scope = (ICurricularCourseScope) iter
                                                .next();
                                        if (curricularSemestersListFinal
                                                .contains(scope
                                                        .getCurricularSemester()
                                                        .getSemester())
                                                && curricularYearsListFinal
                                                        .contains(scope
                                                                .getCurricularSemester()
                                                                .getCurricularYear()
                                                                .getYear())) {
                                            result = true;
                                        }
                                    }
                                }
                                return result;
                            }

                        });

                curricularCoursesFromDegreeCurricularPlan = filterOptionalCourses(result);
            }

            //Student Curricular Plan
            IStudentCurricularPlanPersistente persistentStudentCurricularPlan = sp
                    .getIStudentCurricularPlanPersistente();
            IStudentCurricularPlan studentCurricularPlan = null;
            if (infoStudent != null && infoStudent.getNumber() != null) {
                studentCurricularPlan = persistentStudentCurricularPlan
                        .readActiveByStudentNumberAndDegreeType(infoStudent
                                .getNumber(), degreeType);
            }
            if (studentCurricularPlan == null) {
                throw new FenixServiceException(
                        "error.student.curriculum.noCurricularPlans");
            }

            IPersistentEnrolment persistentEnrolment = sp
                    .getIPersistentEnrolment();
            // Enrolments that have state APROVED and ENROLLED are to be
            // subtracted from list of
            // possible choices.
            List enrollmentsEnrolled = persistentEnrolment
                    .readEnrolmentsByStudentCurricularPlanAndEnrolmentState(
                            studentCurricularPlan, EnrolmentState.ENROLED);
            List enrollmentsAproved = persistentEnrolment
                    .readEnrolmentsByStudentCurricularPlanAndEnrolmentState(
                            studentCurricularPlan, EnrolmentState.APROVED);

            List enrollmentsEnrolledAndAproved = new ArrayList();
            enrollmentsEnrolledAndAproved.addAll(enrollmentsEnrolled);
            enrollmentsEnrolledAndAproved.addAll(enrollmentsAproved);

            List curricularCoursesFromEnrolmentsWithStateEnroledAndAproved = (List) CollectionUtils
                    .collect(enrollmentsEnrolledAndAproved, new Transformer() {
                        public Object transform(Object obj) {
                            IEnrollment enrolment = (IEnrollment) obj;

                            return enrolment.getCurricularCourse();
                        }
                    });

            List possibleCurricularCoursesToChoose = (List) CollectionUtils
                    .subtract(curricularCoursesFromDegreeCurricularPlan,
                            curricularCoursesFromEnrolmentsWithStateEnroledAndAproved);

            infoStudentEnrolmentContext = buildResult(studentCurricularPlan,
                    possibleCurricularCoursesToChoose);
            if (infoStudentEnrolmentContext == null) {
                throw new FenixServiceException("");
            }

        } catch (ExcepcaoPersistencia e) {
            e.printStackTrace();
            throw new FenixServiceException("");
        }

        return infoStudentEnrolmentContext;
    }

    private List verifyYears(List curricularYearsList) {
        if (curricularYearsList != null && curricularYearsList.size() > 0) {
            return curricularYearsList;
        }

        return getListOfChosenCurricularYears();
    }

    private List getListOfChosenCurricularYears() {
        List result = new ArrayList();

        for (int i = 1; i <= MAX_CURRICULAR_YEARS; i++) {
            result.add(new Integer(i));
        }
        return result;
    }

    private List verifySemesters(List curricularSemestersList) {
        if (curricularSemestersList != null
                && curricularSemestersList.size() > 0) {
            return curricularSemestersList;
        }

        return getListOfChosenCurricularSemesters();
    }

    private List getListOfChosenCurricularSemesters() {
        List result = new ArrayList();

        for (int i = 1; i <= MAX_CURRICULAR_SEMESTERS; i++) {
            result.add(new Integer(i));
        }
        return result;
    }

    private InfoStudentEnrollmentContext buildResult(
            IStudentCurricularPlan studentCurricularPlan,
            List curricularCoursesToChoose) {
        InfoStudentCurricularPlan infoStudentCurricularPlan = Cloner
                .copyIStudentCurricularPlan2InfoStudentCurricularPlan(studentCurricularPlan);

        List infoCurricularCoursesToChoose = new ArrayList();
        if (curricularCoursesToChoose != null
                && curricularCoursesToChoose.size() > 0) {
            infoCurricularCoursesToChoose = (List) CollectionUtils.collect(
                    curricularCoursesToChoose, new Transformer() {
                        public Object transform(Object input) {
                            ICurricularCourse curricularCourse = (ICurricularCourse) input;
                            return Cloner
                                    .copyCurricularCourse2InfoCurricularCourse(curricularCourse);
                        }
                    });
            Collections.sort(infoCurricularCoursesToChoose, new BeanComparator(
                    ("name")));
        }

        InfoStudentEnrollmentContext infoStudentEnrolmentContext = new InfoStudentEnrollmentContext();
        infoStudentEnrolmentContext
                .setInfoStudentCurricularPlan(infoStudentCurricularPlan);
        infoStudentEnrolmentContext
                .setFinalInfoCurricularCoursesWhereStudentCanBeEnrolled(infoCurricularCoursesToChoose);

        return infoStudentEnrolmentContext;
    }

    /**
     * @param curricularCourses
     * @return List
     * @author David Santos
     */
    private List filterOptionalCourses(List curricularCourses) {
        List result = (List) CollectionUtils.select(curricularCourses,
                new Predicate() {
                    public boolean evaluate(Object arg0) {
                        ICurricularCourse curricularCourse = (ICurricularCourse) arg0;
                        return !curricularCourse.getType().equals(
                                CurricularCourseType.OPTIONAL_COURSE_OBJ);
                    }
                });

        return result;
    }
}