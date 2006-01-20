/*
 * Created on 23/Jul/2003
 *
 * 
 */
package net.sourceforge.fenixedu.applicationTier.Servico.scientificCouncil;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoCurriculum;
import net.sourceforge.fenixedu.dataTransferObject.SiteView;
import net.sourceforge.fenixedu.domain.Curriculum;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentCurriculum;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;
import net.sourceforge.fenixedu.applicationTier.Service;

/**
 * @author Jo�o Mota
 * 
 * 23/Jul/2003 fenix-head ServidorAplicacao.Servico.scientificCouncil
 * 
 */
public class ReadCurriculumByOIdService extends Service {

	public SiteView run(Integer curriculumId) throws FenixServiceException, ExcepcaoPersistencia {
		ISuportePersistente persistentSupport = PersistenceSupportFactory.getDefaultPersistenceSupport();
		IPersistentCurriculum persistentCurriculum = persistentSupport.getIPersistentCurriculum();
		Curriculum curriculum = (Curriculum) persistentCurriculum.readByOID(Curriculum.class,
				curriculumId);
		InfoCurriculum infoCurriculum = InfoCurriculum.newInfoFromDomain(curriculum);
		SiteView siteView = new SiteView(infoCurriculum);

		return siteView;

	}

}