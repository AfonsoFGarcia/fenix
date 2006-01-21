/*
 * Created on 23/Jul/2003
 *
 * 
 */
package net.sourceforge.fenixedu.applicationTier.Servico.scientificCouncil;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoCurricularCourse;
import net.sourceforge.fenixedu.dataTransferObject.InfoCurricularCourseWithInfoDegree;
import net.sourceforge.fenixedu.dataTransferObject.SiteView;
import net.sourceforge.fenixedu.domain.CurricularCourse;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentObject;

/**
 * @author Jo�o Mota
 * 
 * 23/Jul/2003 fenix-head ServidorAplicacao.Servico.scientificCouncil
 * 
 */
public class ReadCurricularCourseByOIdService extends Service {

	public SiteView run(Integer curricularCourseId) throws FenixServiceException, ExcepcaoPersistencia {
		CurricularCourse curricularCourse = (CurricularCourse) persistentObject.readByOID(
				CurricularCourse.class, curricularCourseId);

		InfoCurricularCourse infoCurricularCourse = InfoCurricularCourseWithInfoDegree
				.newInfoFromDomain(curricularCourse);

		SiteView siteView = new SiteView(infoCurricularCourse);

		return siteView;

	}

}