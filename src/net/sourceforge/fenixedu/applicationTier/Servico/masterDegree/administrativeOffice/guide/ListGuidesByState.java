/*
 * Created on 21/Mar/2003
 *
 */
package net.sourceforge.fenixedu.applicationTier.Servico.masterDegree.administrativeOffice.guide;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import net.sourceforge.fenixedu.dataTransferObject.InfoGuideWithPersonAndExecutionDegreeAndContributor;
import net.sourceforge.fenixedu.domain.GuideState;
import net.sourceforge.fenixedu.domain.IGuide;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;
import pt.utl.ist.berserk.logic.serviceManager.IService;

/**
 * @author Nuno Nunes (nmsn@rnl.ist.utl.pt) Joana Mota (jccm@rnl.ist.utl.pt)
 */
public class ListGuidesByState implements IService {

	public List run(Integer guideYear, GuideState situationOfGuide) throws Exception {

		ISuportePersistente sp = null;
		List guides = new ArrayList();

		sp = PersistenceSupportFactory.getDefaultPersistenceSupport();
		guides = sp.getIPersistentGuide().readByYearAndState(guideYear, situationOfGuide);

		Iterator iterator = guides.iterator();

		List result = new ArrayList();
		while (iterator.hasNext()) {
			result.add(InfoGuideWithPersonAndExecutionDegreeAndContributor
					.newInfoFromDomain((IGuide) iterator.next()));
		}

		return result;
	}

}