/*
 * Created on Oct 7, 2004
 */
package net.sourceforge.fenixedu.applicationTier.Servico.administrativeOffice.lists;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.domain.DegreeCurricularPlan;
import net.sourceforge.fenixedu.domain.DegreeModuleScope;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;

import org.apache.commons.beanutils.BeanComparator;
import org.apache.commons.collections.comparators.ComparatorChain;

/**
 * @author nmgo
 * @author lmre
 */
public class ReadCurricularCourseScopeByDegreeCurricularPlanAndExecutionYear extends Service {

    public List<DegreeModuleScope> run(Integer degreeCurricularPlanID, Integer executioYearID)
	    throws FenixServiceException, ExcepcaoPersistencia {
	final DegreeCurricularPlan degreeCurricularPlan = rootDomainObject
		.readDegreeCurricularPlanByOID(degreeCurricularPlanID);
	final ExecutionYear executionYear = rootDomainObject.readExecutionYearByOID(executioYearID);

	final ComparatorChain comparator = new ComparatorChain();
	comparator.addComparator(new BeanComparator("curricularYear"));
	comparator.addComparator(new BeanComparator("curricularSemester"));
	comparator.addComparator(new BeanComparator("curricularCourse.idInternal"));
	comparator.addComparator(new BeanComparator("branch"));

	final List<DegreeModuleScope> scopes = new ArrayList<DegreeModuleScope>();

	for (DegreeModuleScope degreeModuleScope : degreeCurricularPlan.getDegreeModuleScopes()) {
	    if (degreeModuleScope.isActiveForExecutionYear(executionYear)) {
		scopes.add(degreeModuleScope);
	    }
	}
	Collections.sort(scopes,comparator);
	return scopes;
    }

}
