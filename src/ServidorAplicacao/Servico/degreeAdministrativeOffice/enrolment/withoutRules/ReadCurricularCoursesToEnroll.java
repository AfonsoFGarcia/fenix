/*
 * Created on 18/Fev/2004
 *  
 */
package ServidorAplicacao.Servico.degreeAdministrativeOffice.enrolment.withoutRules;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;
import org.apache.commons.collections.Transformer;

import pt.utl.ist.berserk.logic.serviceManager.IService;
import DataBeans.InfoExecutionYear;
import DataBeans.InfoStudent;
import DataBeans.InfoStudentCurricularPlan;
import DataBeans.InfoStudentCurricularPlanWithInfoStudent;
import DataBeans.enrollment.InfoCurricularCourse2Enroll;
import DataBeans.enrollment.InfoCurricularCourse2EnrollWithInfoCurricularCourse;
import Dominio.CursoExecucao;
import Dominio.ICurricularCourse;
import Dominio.ICurricularCourseScope;
import Dominio.ICursoExecucao;
import Dominio.IDegreeCurricularPlan;
import Dominio.IStudent;
import Dominio.IStudentCurricularPlan;
import Dominio.degree.enrollment.CurricularCourse2Enroll;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorAplicacao.strategy.enrolment.context.InfoStudentEnrollmentContext;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.ICursoExecucaoPersistente;
import ServidorPersistente.IPersistentStudent;
import ServidorPersistente.IStudentCurricularPlanPersistente;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;
import Util.CurricularCourseType;
import Util.TipoCurso;
import Util.enrollment.CurricularCourseEnrollmentType;

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
            InfoExecutionYear infoExecutionYear, Integer executionDegreeID, List curricularYearsList,
            List curricularSemestersList) throws FenixServiceException {
        InfoStudentEnrollmentContext infoStudentEnrolmentContext = null;

        try {
            ISuportePersistente sp = SuportePersistenteOJB.getInstance();
            IStudentCurricularPlanPersistente persistentStudentCurricularPlan = sp
                    .getIStudentCurricularPlanPersistente();

            if (infoStudent == null || infoStudent.getNumber() == null) {
                throw new FenixServiceException("error.student.curriculum.noCurricularPlans");
            }
            final IStudentCurricularPlan studentCurricularPlan = persistentStudentCurricularPlan
                    .readActiveByStudentNumberAndDegreeType(infoStudent.getNumber(), degreeType);

            if (studentCurricularPlan == null) {
                throw new FenixServiceException("error.student.curriculum.noCurricularPlans");
            }

            //Execution Degree
            ICursoExecucaoPersistente persistentExecutionDegree = sp.getICursoExecucaoPersistente();
            ICursoExecucao executionDegree = (ICursoExecucao) persistentExecutionDegree.readByOID(
                    CursoExecucao.class, executionDegreeID);
            if (executionDegree == null) {
                throw new FenixServiceException("error.degree.noData");
            }

            //Degree Curricular Plan
            IDegreeCurricularPlan degreeCurricularPlan = executionDegree.getCurricularPlan();

            if (degreeCurricularPlan == null && degreeCurricularPlan.getCurricularCourses() == null) {
                throw new FenixServiceException("error.degree.noData");
            }

            // filters a list of curricular courses by all of its scopes that
            // matters in relation to the selected semester and the selected
            // year.
            List curricularCoursesFromDegreeCurricularPlan = null;
            final List curricularYearsListFinal = verifyYears(curricularYearsList);
            final List curricularSemestersListFinal = verifySemesters(curricularSemestersList);

            curricularCoursesFromDegreeCurricularPlan = filterOptionalCourses(degreeCurricularPlan
                    .getCurricularCourses());
            curricularCoursesFromDegreeCurricularPlan = (List) CollectionUtils.select(
                    curricularCoursesFromDegreeCurricularPlan, new Predicate() {
                        public boolean evaluate(Object arg0) {
                            return !studentCurricularPlan
                                    .isCurricularCourseApproved(((CurricularCourse2Enroll) arg0)
                                            .getCurricularCourse())
                                    && !studentCurricularPlan
                                            .isCurricularCourseEnrolled(((CurricularCourse2Enroll) arg0)
                                                    .getCurricularCourse());
                        }
                    });
            if (!((curricularYearsList == null || curricularYearsList.size() <= 0) && (curricularSemestersList == null || curricularSemestersList
                    .size() <= 0))) {

                curricularCoursesFromDegreeCurricularPlan = (List) CollectionUtils.select(
                        curricularCoursesFromDegreeCurricularPlan, new Predicate() {
                            public boolean evaluate(Object arg0) {
                                boolean result = false;

                                ICurricularCourse curricularCourse = (ICurricularCourse) arg0;
                                List scopes = curricularCourse.getScopes();
                                Iterator iter = scopes.iterator();
                                while (iter.hasNext() && !result) {
                                    ICurricularCourseScope scope = (ICurricularCourseScope) iter.next();
                                    if (curricularSemestersListFinal.contains(scope
                                            .getCurricularSemester().getSemester())
                                            && curricularYearsListFinal.contains(scope
                                                    .getCurricularSemester().getCurricularYear()
                                                    .getYear())) {
                                        result = true;
                                    }
                                }
                                return result;
                            }
                        });
            }

            curricularCoursesFromDegreeCurricularPlan = (List) CollectionUtils.collect(
                    curricularCoursesFromDegreeCurricularPlan, new Transformer() {

                        public Object transform(Object arg0) {
                            CurricularCourse2Enroll curricularCourse2Enroll = null;

                            curricularCourse2Enroll = new CurricularCourse2Enroll();
                            curricularCourse2Enroll.setCurricularCourse((ICurricularCourse) arg0);

                            curricularCourse2Enroll
                                    .setEnrollmentType(CurricularCourseEnrollmentType.DEFINITIVE);
                            return curricularCourse2Enroll;
                        }
                    });

            infoStudentEnrolmentContext = buildResult(studentCurricularPlan,
                    curricularCoursesFromDegreeCurricularPlan);
            if (infoStudentEnrolmentContext == null) {
                throw new FenixServiceException("");
            }

        } catch (ExcepcaoPersistencia e) {
            throw new FenixServiceException(e);
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
        if (curricularSemestersList != null && curricularSemestersList.size() > 0) {
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

    private InfoStudentEnrollmentContext buildResult(IStudentCurricularPlan studentCurricularPlan,
            List curricularCoursesToChoose) {
        InfoStudentCurricularPlan infoStudentCurricularPlan = InfoStudentCurricularPlanWithInfoStudent
                .newInfoFromDomain(studentCurricularPlan);

        List infoCurricularCoursesToChoose = new ArrayList();
        if (curricularCoursesToChoose != null && curricularCoursesToChoose.size() > 0) {
            infoCurricularCoursesToChoose = (List) CollectionUtils.collect(curricularCoursesToChoose,
                    new Transformer() {
                        public Object transform(Object input) {
                            CurricularCourse2Enroll curricularCourse = (CurricularCourse2Enroll) input;
                            return InfoCurricularCourse2EnrollWithInfoCurricularCourse
                                    .newInfoFromDomain(curricularCourse);
                        }
                    });
            Collections.sort(infoCurricularCoursesToChoose, new Comparator() {
                public int compare(Object o1, Object o2) {
                    InfoCurricularCourse2Enroll obj1 = (InfoCurricularCourse2Enroll) o1;
                    InfoCurricularCourse2Enroll obj2 = (InfoCurricularCourse2Enroll) o2;
                    return obj1.getInfoCurricularCourse().getName().compareTo(
                            obj2.getInfoCurricularCourse().getName());
                }
            });
        }

        InfoStudentEnrollmentContext infoStudentEnrolmentContext = new InfoStudentEnrollmentContext();
        infoStudentEnrolmentContext.setInfoStudentCurricularPlan(infoStudentCurricularPlan);
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
        List result = (List) CollectionUtils.select(curricularCourses, new Predicate() {
            public boolean evaluate(Object arg0) {
                CurricularCourse2Enroll curricularCourse = (CurricularCourse2Enroll) arg0;
                return !curricularCourse.getCurricularCourse().getType().equals(
                        CurricularCourseType.OPTIONAL_COURSE_OBJ);
            }
        });

        return result;
    }

    /**
     * @param studentNumber
     * @return IStudent
     * @throws ExcepcaoPersistencia
     */
    protected IStudent getStudent(Integer studentNumber) throws ExcepcaoPersistencia {
        ISuportePersistente persistentSuport = SuportePersistenteOJB.getInstance();
        IPersistentStudent studentDAO = persistentSuport.getIPersistentStudent();

        return studentDAO.readStudentByNumberAndDegreeType(studentNumber, TipoCurso.LICENCIATURA_OBJ);
    }

    /**
     * @param student
     * @return IStudentCurricularPlan
     * @throws ExcepcaoPersistencia
     */
    protected IStudentCurricularPlan getStudentCurricularPlan(IStudent student)
            throws ExcepcaoPersistencia {
        ISuportePersistente persistentSuport = SuportePersistenteOJB.getInstance();
        IStudentCurricularPlanPersistente studentCurricularPlanDAO = persistentSuport
                .getIStudentCurricularPlanPersistente();

        return studentCurricularPlanDAO.readActiveStudentCurricularPlan(student.getNumber(), student
                .getDegreeType());
    }
}