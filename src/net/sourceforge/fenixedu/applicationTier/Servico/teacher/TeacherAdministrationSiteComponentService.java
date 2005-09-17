package net.sourceforge.fenixedu.applicationTier.Servico.teacher;

import net.sourceforge.fenixedu.applicationTier.Factory.TeacherAdministrationSiteComponentBuilder;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.ISiteComponent;
import net.sourceforge.fenixedu.dataTransferObject.TeacherAdministrationSiteView;
import net.sourceforge.fenixedu.domain.ISite;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentSite;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;
import pt.utl.ist.berserk.logic.serviceManager.IService;

/**
 * @author Fernanda Quit�rio
 * 
 * 
 */
public class TeacherAdministrationSiteComponentService implements IService {

    public Object run(Integer infoExecutionCourseCode, ISiteComponent commonComponent,
            ISiteComponent bodyComponent, Integer infoSiteCode, Object obj1, Object obj2)
            throws FenixServiceException, ExcepcaoPersistencia {

        final ISuportePersistente sp = PersistenceSupportFactory.getDefaultPersistenceSupport();
        final IPersistentSite persistentSite = sp.getIPersistentSite();

        final ISite site = persistentSite.readByExecutionCourse(infoExecutionCourseCode);
        final TeacherAdministrationSiteComponentBuilder componentBuilder = TeacherAdministrationSiteComponentBuilder
                .getInstance();
        commonComponent = componentBuilder.getComponent(commonComponent, site, null, null, null);
        bodyComponent = componentBuilder.getComponent(bodyComponent, site, commonComponent, obj1, obj2);

        TeacherAdministrationSiteView siteView = new TeacherAdministrationSiteView(commonComponent, bodyComponent);
	siteView.site = site;
	return siteView;
    }
}
