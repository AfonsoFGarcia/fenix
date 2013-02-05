/*
 * Created on Jun 18, 2004
 *
 */
package net.sourceforge.fenixedu.dataTransferObject.grant.contract;

import net.sourceforge.fenixedu.dataTransferObject.grant.owner.InfoGrantOwnerWithPerson;
import net.sourceforge.fenixedu.domain.grant.contract.GrantContract;

/**
 * @author Pica
 * @author Barbosa
 */
public class InfoGrantContractWithGrantOwnerAndGrantType extends InfoGrantContract {

    @Override
    public void copyFromDomain(GrantContract grantContract) {
        super.copyFromDomain(grantContract);

        if (grantContract != null) {
            setGrantOwnerInfo(InfoGrantOwnerWithPerson.newInfoFromDomain(grantContract.getGrantOwner()));
            setGrantTypeInfo(InfoGrantType.newInfoFromDomain(grantContract.getGrantType()));
            if (grantContract.getGrantCostCenter() != null) {
                setGrantCostCenterInfo(InfoGrantCostCenter.newInfoFromDomain(grantContract.getGrantCostCenter()));
            }

        }
    }

    public static InfoGrantContract newInfoFromDomain(GrantContract grantContract) {
        InfoGrantContractWithGrantOwnerAndGrantType infoGrantContract = null;
        if (grantContract != null) {
            infoGrantContract = new InfoGrantContractWithGrantOwnerAndGrantType();
            infoGrantContract.copyFromDomain(grantContract);
        }
        return infoGrantContract;
    }

}
