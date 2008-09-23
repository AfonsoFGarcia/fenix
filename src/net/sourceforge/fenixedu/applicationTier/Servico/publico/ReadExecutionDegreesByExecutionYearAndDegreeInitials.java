package net.sourceforge.fenixedu.applicationTier.Servico.publico;

/**
 * Servico LerSalas
 * 
 * @author tfc130
 * @version
 */

import net.sourceforge.fenixedu.applicationTier.FenixService;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionDegree;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionYear;
import net.sourceforge.fenixedu.domain.DegreeCurricularPlan;
import net.sourceforge.fenixedu.domain.ExecutionDegree;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;

public class ReadExecutionDegreesByExecutionYearAndDegreeInitials extends FenixService {

    public InfoExecutionDegree run(final InfoExecutionYear infoExecutionYear, final String degreeInitials,
	    final String nameDegreeCurricularPlan) {

	final DegreeCurricularPlan degreeCurricularPlan = DegreeCurricularPlan.readByNameAndDegreeSigla(nameDegreeCurricularPlan,
		degreeInitials);
	final ExecutionDegree executionDegree = ExecutionDegree.getByDegreeCurricularPlanAndExecutionYear(degreeCurricularPlan,
		infoExecutionYear.getYear());
	return getInfoExecutionDegree(executionDegree);
    }

    public static InfoExecutionDegree getInfoExecutionDegree(final ExecutionDegree executionDegree) {
	return InfoExecutionDegree.newInfoFromDomain(executionDegree);
    }

}