/*
 * Created on 4/Ago/2003
 */
package net.sourceforge.fenixedu.applicationTier.Servico.manager;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.FenixService;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoCurricularCourse;
import net.sourceforge.fenixedu.domain.CurricularCourse;
import net.sourceforge.fenixedu.domain.DegreeCurricularPlan;
import pt.ist.fenixWebFramework.services.Service;

/**
 * 
 * @author Luis Cruz
 */
public class ReadCurricularCoursesByDegreeCurricularPlan extends FenixService {

    @Service
    public static List run(final Integer idDegreeCurricularPlan) throws FenixServiceException {
        final DegreeCurricularPlan degreeCurricularPlan = rootDomainObject.readDegreeCurricularPlanByOID(idDegreeCurricularPlan);

        final List<CurricularCourse> curricularCourses = degreeCurricularPlan.getCurricularCourses();
        final List<InfoCurricularCourse> infoCurricularCourses = new ArrayList<InfoCurricularCourse>(curricularCourses.size());
        for (final CurricularCourse curricularCourse : curricularCourses) {
            infoCurricularCourses.add(InfoCurricularCourse.newInfoFromDomain(curricularCourse));
        }
        return infoCurricularCourses;
    }

}