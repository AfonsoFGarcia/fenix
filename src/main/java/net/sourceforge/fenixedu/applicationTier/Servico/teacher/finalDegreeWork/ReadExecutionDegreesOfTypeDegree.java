package net.sourceforge.fenixedu.applicationTier.Servico.teacher.finalDegreeWork;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.FenixService;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionDegree;
import net.sourceforge.fenixedu.domain.ExecutionDegree;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.degree.DegreeType;
import pt.ist.fenixWebFramework.security.accessControl.Checked;
import pt.ist.fenixWebFramework.services.Service;

/*
 * Created on Mar 8, 2004
 *
 */

/**
 * @author Nuno Correia
 * @author Ricardo Rodrigues
 */
public class ReadExecutionDegreesOfTypeDegree extends FenixService {

    @Checked("RolePredicates.TEACHER_PREDICATE")
    @Service
    public static List run() {
        String year = ExecutionYear.readCurrentExecutionYear().getYear();
        List executionDegrees = ExecutionDegree.getAllByExecutionYearAndDegreeType(year, DegreeType.DEGREE);

        List infoExecutionDegrees = new ArrayList();

        if (executionDegrees != null) {
            Iterator iterator = executionDegrees.iterator();
            while (iterator.hasNext()) {
                ExecutionDegree executionDegree = (ExecutionDegree) iterator.next();
                InfoExecutionDegree infoExecutionDegree = InfoExecutionDegree.newInfoFromDomain(executionDegree);

                infoExecutionDegrees.add(infoExecutionDegree);
            }
        }

        return infoExecutionDegrees;
    }
}