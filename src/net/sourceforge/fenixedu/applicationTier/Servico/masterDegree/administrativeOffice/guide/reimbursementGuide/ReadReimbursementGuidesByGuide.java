/*
 * Created on 24/Mar/2004
 *  
 */
package net.sourceforge.fenixedu.applicationTier.Servico.masterDegree.administrativeOffice.guide.reimbursementGuide;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.guide.reimbursementGuide.InfoReimbursementGuide;
import net.sourceforge.fenixedu.domain.Guide;
import net.sourceforge.fenixedu.domain.reimbursementGuide.ReimbursementGuide;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;
import net.sourceforge.fenixedu.applicationTier.IService;

/**
 * @author <a href="mailto:sana@ist.utl.pt">Shezad Anavarali </a>
 * @author <a href="mailto:naat@ist.utl.pt">Nadir Tarmahomed </a>
 * 
 */

public class ReadReimbursementGuidesByGuide implements IService {

    /**
     * @throws FenixServiceException
     * @throws ExcepcaoPersistencia
     */

    public List run(Integer guideId) throws ExcepcaoPersistencia {

        ISuportePersistente ps = PersistenceSupportFactory.getDefaultPersistenceSupport();

        // guide
        Guide guide = (Guide) ps.getIPersistentGuide().readByOID(Guide.class, guideId);

        // reimbursement Guides
        List reimbursementGuides = guide.getReimbursementGuides();

        List infoReimbursementGuides = new ArrayList();

        Iterator it = reimbursementGuides.iterator();
        while (it.hasNext()) {

            infoReimbursementGuides.add(InfoReimbursementGuide
                    .newInfoFromDomain((ReimbursementGuide) it.next()));

        }

        return infoReimbursementGuides;

    }

}