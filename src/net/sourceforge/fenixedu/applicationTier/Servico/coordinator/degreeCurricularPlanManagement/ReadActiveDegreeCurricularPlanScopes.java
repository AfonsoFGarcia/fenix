package net.sourceforge.fenixedu.applicationTier.Servico.coordinator.degreeCurricularPlanManagement;

import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Servico.coordinator.ReadDegreeCurricularPlanBaseService;

/**
 * @author Fernanda Quit�rio 5/Nov/2003
 * 
 * @modified <a href="mailto:amam@mega.ist.utl.pt">Amin Amirali </a> 23/11/2004
 * @modified <a href="mailto:frnp@mega.ist.utl.pt">Francisco Paulo </a>
 *           23/11/2004
 * 
 */

public class ReadActiveDegreeCurricularPlanScopes extends ReadDegreeCurricularPlanBaseService {

    public List run(final Integer degreeCurricularPlanId) {

	return super.readActiveCurricularCourseScopes(degreeCurricularPlanId);

    }
}