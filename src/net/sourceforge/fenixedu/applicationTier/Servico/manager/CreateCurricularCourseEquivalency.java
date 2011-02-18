package net.sourceforge.fenixedu.applicationTier.Servico.manager;

import java.util.Collections;

import net.sourceforge.fenixedu.applicationTier.FenixService;
import net.sourceforge.fenixedu.domain.CurricularCourse;
import net.sourceforge.fenixedu.domain.CurricularCourseEquivalence;
import net.sourceforge.fenixedu.domain.DegreeCurricularPlan;
import pt.ist.fenixWebFramework.security.accessControl.Checked;
import pt.ist.fenixWebFramework.services.Service;

public class CreateCurricularCourseEquivalency extends FenixService {

    @Checked("RolePredicates.MANAGER_OR_OPERATOR_PREDICATE")
    @Service
    public static void run(final Integer degreeCurricularPlanID, final Integer curricularCourseID,
	    final Integer oldCurricularCourseID) {
	final DegreeCurricularPlan degreeCurricularPlan = rootDomainObject.readDegreeCurricularPlanByOID(degreeCurricularPlanID);
	final CurricularCourse curricularCourse = (CurricularCourse) rootDomainObject.readDegreeModuleByOID(curricularCourseID);
	final CurricularCourse oldCurricularCourse = (CurricularCourse) rootDomainObject
		.readDegreeModuleByOID(oldCurricularCourseID);

	new CurricularCourseEquivalence(degreeCurricularPlan, curricularCourse, Collections.singleton(oldCurricularCourse));
    }

}