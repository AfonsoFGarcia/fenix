/*
 * Created on Nov 13, 2003
 *  
 */
package net.sourceforge.fenixedu.applicationTier.Servico.teacher;

import net.sourceforge.fenixedu.applicationTier.Servico.framework.ReadDomainObjectService;
import net.sourceforge.fenixedu.dataTransferObject.InfoObject;
import net.sourceforge.fenixedu.dataTransferObject.teacher.InfoExternalActivity;
import net.sourceforge.fenixedu.domain.DomainObject;
import net.sourceforge.fenixedu.domain.teacher.ExternalActivity;
import net.sourceforge.fenixedu.persistenceTier.IPersistentObject;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.teacher.IPersistentExternalActivity;

/**
 * @author Leonor Almeida
 * @author Sergio Montelobo
 * 
 */
public class ReadExternalActivity extends ReadDomainObjectService {

    protected Class getDomainObjectClass() {
        return ExternalActivity.class;
    }

    protected InfoObject newInfoFromDomain(DomainObject domainObject) {
        InfoExternalActivity infoExternalActivity = InfoExternalActivity
                .newInfoFromDomain((ExternalActivity) domainObject);
        return infoExternalActivity;
    }

}
