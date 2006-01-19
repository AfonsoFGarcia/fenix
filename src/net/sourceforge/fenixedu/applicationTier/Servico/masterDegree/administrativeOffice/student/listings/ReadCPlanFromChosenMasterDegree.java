package net.sourceforge.fenixedu.applicationTier.Servico.masterDegree.administrativeOffice.student.listings;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoDegreeCurricularPlan;
import net.sourceforge.fenixedu.dataTransferObject.InfoDegreeCurricularPlanWithDegree;
import net.sourceforge.fenixedu.domain.Degree;
import net.sourceforge.fenixedu.domain.DegreeCurricularPlan;
import net.sourceforge.fenixedu.domain.degreeStructure.CurricularStage;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;
import net.sourceforge.fenixedu.applicationTier.IService;

/**
 * 
 * @author Nuno Nunes (nmsn@rnl.ist.utl.pt) Joana Mota (jccm@rnl.ist.utl.pt)
 */
public class ReadCPlanFromChosenMasterDegree implements IService {

    public List run(Integer idInternal) throws FenixServiceException, ExcepcaoPersistencia {
        ISuportePersistente sp = PersistenceSupportFactory.getDefaultPersistenceSupport();
        Degree degree = (Degree) sp.getICursoPersistente().readByOID(Degree.class, idInternal);

        List<InfoDegreeCurricularPlan> result = new ArrayList<InfoDegreeCurricularPlan>();
        for (DegreeCurricularPlan dcp : degree.getDegreeCurricularPlans()) {
            if (dcp.getCurricularStage().equals(CurricularStage.OLD)) {
                result.add(InfoDegreeCurricularPlanWithDegree.newInfoFromDomain(dcp));
            }
        }

        return result;
    }

}
