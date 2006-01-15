package net.sourceforge.fenixedu.applicationTier.Servico.manager;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.InvalidArgumentsServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoDegreeCurricularPlan;
import net.sourceforge.fenixedu.domain.Degree;
import net.sourceforge.fenixedu.domain.DegreeCurricularPlan;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;
import pt.utl.ist.berserk.logic.serviceManager.IService;

public class EditDegreeCurricularPlan implements IService {

    public void run(InfoDegreeCurricularPlan infoDcp) throws FenixServiceException, ExcepcaoPersistencia {

        if (infoDcp.getIdInternal() == null || infoDcp.getName() == null
                || infoDcp.getInitialDate() == null || infoDcp.getDegreeDuration() == null
                || infoDcp.getMinimalYearForOptionalCourses() == null) {
            throw new InvalidArgumentsServiceException();
        }

        final ISuportePersistente persistentSupport = PersistenceSupportFactory
                .getDefaultPersistenceSupport();

        final DegreeCurricularPlan dcpToEdit = (DegreeCurricularPlan) persistentSupport
                .getIPersistentDegreeCurricularPlan().readByOID(DegreeCurricularPlan.class,
                        infoDcp.getIdInternal());
        if (dcpToEdit == null) {
            throw new FenixServiceException("message.nonExistingDegreeCurricularPlan");
        }

        final Degree degree = dcpToEdit.getDegree();
        if (degree == null) {
            throw new FenixServiceException("message.nonExistingDegree");
        }
        
        // assert unique pair name/degree
        for (final DegreeCurricularPlan degreeCurricularPlan : degree.getDegreeCurricularPlans()) {
        	if (degreeCurricularPlan != dcpToEdit && degreeCurricularPlan.getName().equalsIgnoreCase(infoDcp.getName())) {
        		throw new FenixServiceException("error.degreeCurricularPlan.existing.name.and.degree");
        	}
        }

        dcpToEdit.edit(infoDcp.getName(), infoDcp.getState(), infoDcp.getInitialDate(), infoDcp
                .getEndDate(), infoDcp.getDegreeDuration(), infoDcp.getMinimalYearForOptionalCourses(),
                infoDcp.getNeededCredits(), infoDcp.getMarkType(), infoDcp.getNumerusClausus(), infoDcp
                        .getAnotation(), infoDcp.getGradeScale());
    }

}
