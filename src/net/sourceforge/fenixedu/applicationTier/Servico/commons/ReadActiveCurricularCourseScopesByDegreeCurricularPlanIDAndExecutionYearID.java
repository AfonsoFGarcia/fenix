package net.sourceforge.fenixedu.applicationTier.Servico.commons;

import java.util.Set;
import java.util.TreeSet;

import net.sourceforge.fenixedu.applicationTier.FenixService;
import net.sourceforge.fenixedu.domain.DegreeCurricularPlan;
import net.sourceforge.fenixedu.domain.DegreeModuleScope;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import pt.ist.fenixWebFramework.services.Service;

/**
 * @author - Shezad Anavarali (shezad@ist.utl.pt)
 * 
 */
public class ReadActiveCurricularCourseScopesByDegreeCurricularPlanIDAndExecutionYearID extends FenixService {

	@Service
	public static Set<DegreeModuleScope> run(Integer degreeCurricularPlanId, Integer executionYearID) {
		final ExecutionYear executionYear = rootDomainObject.readExecutionYearByOID(executionYearID);
		final DegreeCurricularPlan degreeCurricularPlan = rootDomainObject.readDegreeCurricularPlanByOID(degreeCurricularPlanId);
		final Set<DegreeModuleScope> degreeModuleScopes = degreeCurricularPlan.getDegreeModuleScopes();
		final Set<DegreeModuleScope> result =
				new TreeSet<DegreeModuleScope>(
						DegreeModuleScope.COMPARATOR_BY_CURRICULAR_YEAR_AND_SEMESTER_AND_CURRICULAR_COURSE_NAME);
		for (final DegreeModuleScope degreeModuleScope : degreeModuleScopes) {
			if (degreeModuleScope.isActiveForExecutionYear(executionYear)) {
				result.add(degreeModuleScope);
			}
		}
		return result;
	}

}