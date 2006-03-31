/*
 * Created on Jan 29, 2004
 *
 */
package net.sourceforge.fenixedu.applicationTier.Servico.grant.contract;

import net.sourceforge.fenixedu.applicationTier.Servico.framework.ReadDomainObjectService;
import net.sourceforge.fenixedu.dataTransferObject.InfoObject;
import net.sourceforge.fenixedu.dataTransferObject.grant.contract.InfoGrantPartWithSubsidyAndTeacherAndPaymentEntity;
import net.sourceforge.fenixedu.domain.DomainObject;
import net.sourceforge.fenixedu.domain.grant.contract.GrantPart;

/**
 * Jo�o Simas Nuno Barbosa
 */
public class ReadGrantPart extends ReadDomainObjectService {

    protected Class getDomainObjectClass() {
        return GrantPart.class;
    }

    protected InfoObject newInfoFromDomain(DomainObject domainObject) {
        return InfoGrantPartWithSubsidyAndTeacherAndPaymentEntity
                .newInfoFromDomain((GrantPart) domainObject);
    }

}
