package net.sourceforge.fenixedu.applicationTier.Servico.space;

import net.sourceforge.fenixedu.applicationTier.FenixService;
import net.sourceforge.fenixedu.domain.space.SpaceInformation;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;

public class CreateSpaceInformation extends FenixService {

    public void run(final Integer spaceInformationID) {
	final SpaceInformation spaceInformation = rootDomainObject.readSpaceInformationByOID(spaceInformationID);
	// spaceInformation.createNewSpaceInformation();
    }

}
