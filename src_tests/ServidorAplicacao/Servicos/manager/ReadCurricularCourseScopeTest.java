/*
 * Created on 1/Set/2003
 */
package net.sourceforge.fenixedu.applicationTier.Servicos.manager;

import net.sourceforge.fenixedu.dataTransferObject.InfoBranch;
import net.sourceforge.fenixedu.dataTransferObject.InfoCurricularCourse;
import net.sourceforge.fenixedu.dataTransferObject.InfoCurricularCourseScope;
import net.sourceforge.fenixedu.dataTransferObject.InfoCurricularSemester;
import net.sourceforge.fenixedu.dataTransferObject.InfoCurricularYear;
import net.sourceforge.fenixedu.dataTransferObject.InfoDegree;
import net.sourceforge.fenixedu.dataTransferObject.InfoDegreeCurricularPlan;

/**
 * @author lmac1
 */

public class ReadCurricularCourseScopeTest extends TestCaseManagerReadServices {

    /**
     * @param testName
     */
    public ReadCurricularCourseScopeTest(String testName) {
        super(testName);
    }

    protected String getNameOfServiceToBeTested() {
        return "ReadCurricularCourseScope";
    }

    protected Object[] getArgumentsOfServiceToBeTestedUnsuccessfuly() {
        Object[] args = { new Integer(100) };
        return args;
    }

    protected Object[] getArgumentsOfServiceToBeTestedSuccessfuly() {
        Object[] args = { new Integer(12) };
        return args;
    }

    protected Object getObjectToCompare() {

        InfoDegree infoDegree = new InfoDegree("LEIC",
                "Licenciatura de Engenharia Informatica e de Computadores");

        InfoDegreeCurricularPlan infoDegreeCurricularPlan = new InfoDegreeCurricularPlan("plano1",
                infoDegree);

        InfoCurricularCourse infoCurricularCourse = new InfoCurricularCourse();
        infoCurricularCourse.setInfoDegreeCurricularPlan(infoDegreeCurricularPlan);
        infoCurricularCourse.setName("Analise Matematica I");
        infoCurricularCourse.setCode("AMI");

        InfoBranch infoBranch = new InfoBranch();
        infoBranch.setCode("C");
        infoBranch.setInfoDegreeCurricularPlan(infoDegreeCurricularPlan);

        InfoCurricularYear infoCurricularYear = new InfoCurricularYear(new Integer(1));

        InfoCurricularSemester infoCurricularSemester = new InfoCurricularSemester(new Integer(1),
                infoCurricularYear);

        InfoCurricularCourseScope infoCurricularCourseScope = new InfoCurricularCourseScope();
        infoCurricularCourseScope.setInfoBranch(infoBranch);
        infoCurricularCourseScope.setInfoCurricularCourse(infoCurricularCourse);
        infoCurricularCourseScope.setInfoCurricularSemester(infoCurricularSemester);

        return infoCurricularCourseScope;
    }
}