package net.sourceforge.fenixedu.applicationTier.Servico.manager;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.InvalidArgumentsServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NonExistingServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoCurricularCourseScope;
import net.sourceforge.fenixedu.domain.CurricularCourseScope;
import net.sourceforge.fenixedu.domain.ICurricularCourseScope;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentCurricularCourseScope;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;
import pt.utl.ist.berserk.logic.serviceManager.IService;

/**
 * @author Fernanda Quit�rio 28/10/2003
 * 
 */
public class EndCurricularCourseScope implements IService {

    public void run(InfoCurricularCourseScope newInfoCurricularCourseScope) throws ExcepcaoPersistencia,
            FenixServiceException {

        if (!newInfoCurricularCourseScope.getEndDate()
                .after(newInfoCurricularCourseScope.getBeginDate())) {
            throw new InvalidArgumentsServiceException();
        }

        final ISuportePersistente ps = PersistenceSupportFactory.getDefaultPersistenceSupport();
        final IPersistentCurricularCourseScope persistentCurricularCourseScope = ps
                .getIPersistentCurricularCourseScope();
        ICurricularCourseScope oldCurricularCourseScope = (ICurricularCourseScope) persistentCurricularCourseScope
                .readByOID(CurricularCourseScope.class, newInfoCurricularCourseScope.getIdInternal(),
                        true);
        if (oldCurricularCourseScope == null) {
            throw new NonExistingServiceException("message.non.existing.curricular.course.scope", null);
        }

        oldCurricularCourseScope.setEndDate(newInfoCurricularCourseScope.getEndDate());
    }

}
