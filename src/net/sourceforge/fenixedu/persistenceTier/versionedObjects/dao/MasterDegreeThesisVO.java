/*
 * Created on Oct 10, 2003
 *
 *
 */
package net.sourceforge.fenixedu.persistenceTier.versionedObjects.dao;

import net.sourceforge.fenixedu.domain.MasterDegreeThesis;
import net.sourceforge.fenixedu.domain.StudentCurricularPlan;
import net.sourceforge.fenixedu.domain.StudentCurricularPlan;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentMasterDegreeThesis;
import net.sourceforge.fenixedu.persistenceTier.versionedObjects.VersionedObjectsBase;

/**
 * @author - Shezad Anavarali (sana@mega.ist.utl.pt) - Nadir Tarmahomed
 *         (naat@mega.ist.utl.pt)
 * 
 */
public class MasterDegreeThesisVO extends VersionedObjectsBase implements IPersistentMasterDegreeThesis {

    public MasterDegreeThesis readByStudentCurricularPlan(Integer studentCurricularPlanId)
            throws ExcepcaoPersistencia {

        return ((StudentCurricularPlan) readByOID(StudentCurricularPlan.class, studentCurricularPlanId))
                .getMasterDegreeThesis();

    }
}