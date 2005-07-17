/*
 * Created on 19/Ago/2003
 */
package net.sourceforge.fenixedu.applicationTier.Servico.manager;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;

import net.sourceforge.fenixedu.dataTransferObject.InfoCurricularCourseScopeWithBranchAndSemesterAndYear;
import net.sourceforge.fenixedu.dataTransferObject.InfoCurricularCourseScopeWithCurricularCourseAndDegreeAndSemesterAndYear;
import net.sourceforge.fenixedu.domain.CurricularCourse;
import net.sourceforge.fenixedu.domain.ICurricularCourse;
import net.sourceforge.fenixedu.domain.ICurricularCourseScope;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;
import pt.utl.ist.berserk.logic.serviceManager.IService;

/**
 * @author Fernanda Quit�rio 28/10/2003
 *  
 */
public class ReadActiveCurricularCourseScopes implements IService {

    /**
     * Executes the service. Returns the collection of active
     * infoCurricularCourseScopes.
     */
    public List run(Integer curricularCourseId) throws FenixServiceException {
        ISuportePersistente sp;
        List allCurricularCourseScopes = null;
        try {

            sp = PersistenceSupportFactory.getDefaultPersistenceSupport();
            ICurricularCourse curricularCourse = (ICurricularCourse) sp.getIPersistentCurricularCourse()
                    .readByOID(CurricularCourse.class, curricularCourseId);
            allCurricularCourseScopes = sp.getIPersistentCurricularCourseScope()
                    .readActiveCurricularCourseScopesByCurricularCourse(curricularCourse.getIdInternal());

        } catch (ExcepcaoPersistencia excepcaoPersistencia) {
            throw new FenixServiceException(excepcaoPersistencia);
        }

        if (allCurricularCourseScopes == null || allCurricularCourseScopes.isEmpty())
            return allCurricularCourseScopes;

        Iterator iterator = allCurricularCourseScopes.iterator();
        List result = new ArrayList(allCurricularCourseScopes.size());

        while (iterator.hasNext())
            result.add(InfoCurricularCourseScopeWithBranchAndSemesterAndYear.newInfoFromDomain((ICurricularCourseScope)iterator.next()));

        return result;
    }
}