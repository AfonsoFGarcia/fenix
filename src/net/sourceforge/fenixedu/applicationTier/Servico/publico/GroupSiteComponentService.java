package net.sourceforge.fenixedu.applicationTier.Servico.publico;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.applicationTier.Factory.ExecutionCourseSiteComponentBuilder;
import net.sourceforge.fenixedu.applicationTier.Factory.GroupSiteComponentBuilder;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NonExistingServiceException;
import net.sourceforge.fenixedu.dataTransferObject.ExecutionCourseSiteView;
import net.sourceforge.fenixedu.dataTransferObject.ISiteComponent;
import net.sourceforge.fenixedu.domain.Site;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;

public class GroupSiteComponentService extends Service {

    public Object run(ISiteComponent commonComponent, ISiteComponent bodyComponent,
            Integer infoSiteCode, Integer groupPropertiesCode, Integer code, Integer shiftCode,
            Integer value) throws FenixServiceException, ExcepcaoPersistencia {
        Site site = null;
        if (infoSiteCode != null) {
            site = rootDomainObject.readSiteByOID(infoSiteCode);
            if (site == null) {
                throw new NonExistingServiceException();
            }
        }
        GroupSiteComponentBuilder componentBuilder = GroupSiteComponentBuilder.getInstance();
        bodyComponent = componentBuilder.getComponent(bodyComponent, site.getExecutionCourse()
                .getIdInternal(), groupPropertiesCode, code, shiftCode, value);
        ExecutionCourseSiteComponentBuilder componentBuilder2 = ExecutionCourseSiteComponentBuilder
                .getInstance();

        commonComponent = componentBuilder2.getComponent(commonComponent, site, null, null, null);

        return new ExecutionCourseSiteView(commonComponent, bodyComponent);
    }
}