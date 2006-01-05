/*
 * Created on Dec 9, 2005
 */
package net.sourceforge.fenixedu.applicationTier.Servico.bolonhaManager;

import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.bolonhaManager.CourseLoad;
import net.sourceforge.fenixedu.domain.DomainFactory;
import net.sourceforge.fenixedu.domain.ICompetenceCourse;
import net.sourceforge.fenixedu.domain.degreeStructure.CurricularStage;
import net.sourceforge.fenixedu.domain.degreeStructure.RegimeType;
import net.sourceforge.fenixedu.domain.organizationalStructure.IUnit;
import net.sourceforge.fenixedu.domain.organizationalStructure.Unit;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;
import pt.utl.ist.berserk.logic.serviceManager.IService;

public class CreateCompetenceCourse implements IService {

    public ICompetenceCourse run(String name, String nameEn, String acronym, Boolean basic,
            RegimeType regimeType, Integer unitID) throws ExcepcaoPersistencia, FenixServiceException {

        final ISuportePersistente persistentSupport = PersistenceSupportFactory.getDefaultPersistenceSupport();
        final IUnit unit = (IUnit) persistentSupport.getIPersistentObject().readByOID(Unit.class, unitID);
        if (unit == null) {
            throw new FenixServiceException("error.invalidUnit");
        }           
        final ICompetenceCourse competenceCourse = 
            DomainFactory.makeCompetenceCourse(name, nameEn, acronym, basic, regimeType, CurricularStage.DRAFT, unit);

        
        return competenceCourse;
    }
}
