package net.sourceforge.fenixedu.applicationTier.Servico.publico;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import net.sourceforge.fenixedu.dataTransferObject.ClassView;
import net.sourceforge.fenixedu.domain.Degree;
import net.sourceforge.fenixedu.domain.ExecutionSemester;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.SchoolClass;
import pt.ist.fenixWebFramework.services.Service;

/**
 * 
 * @author Luis Cruz
 */
public class ReadClassesForCurrentAndPreviousPeriodByDegree {

    @Service
    public static Object run(Integer degreeOID) {
        ExecutionSemester currentExecutionPeriod = ExecutionSemester.readActualExecutionSemester();
        ExecutionSemester previouseExecutionPeriod = currentExecutionPeriod.getPreviousExecutionPeriod();

        Degree degree = RootDomainObject.getInstance().readDegreeByOID(degreeOID);

        List classes = RootDomainObject.getInstance().getSchoolClasss();

        return constructViews(classes, degree, currentExecutionPeriod, previouseExecutionPeriod);
    }

    private static Object constructViews(List classes, final Degree degree, final ExecutionSemester currentExecutionPeriod,
            final ExecutionSemester previouseExecutionPeriod) {
        List classViews = new ArrayList();
        for (Iterator iterator = classes.iterator(); iterator.hasNext();) {
            SchoolClass klass = (SchoolClass) iterator.next();
            if (isInPeriodsAndForDegree(klass, degree, currentExecutionPeriod, previouseExecutionPeriod)) {
                ClassView classView = new ClassView(klass);
                classViews.add(classView);
            }
        }
        return classViews;
    }

    private static boolean isInPeriodsAndForDegree(SchoolClass klass, Degree degree, ExecutionSemester currentExecutionPeriod,
            ExecutionSemester previouseExecutionPeriod) {
        return (klass.getExecutionPeriod().getIdInternal().equals(currentExecutionPeriod.getIdInternal()) || klass
                .getExecutionPeriod().getIdInternal().equals(previouseExecutionPeriod.getIdInternal()))
                && klass.getExecutionDegree().getDegreeCurricularPlan().getDegree().getIdInternal()
                        .equals(degree.getIdInternal());
    }

}