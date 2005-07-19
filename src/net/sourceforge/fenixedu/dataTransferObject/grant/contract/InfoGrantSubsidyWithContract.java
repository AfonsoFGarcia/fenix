/*
 * Created on Jun 25, 2004
 */
package net.sourceforge.fenixedu.dataTransferObject.grant.contract;

import net.sourceforge.fenixedu.domain.grant.contract.IGrantSubsidy;

/**
 * @author Pica
 * @author Barbosa
 */
public class InfoGrantSubsidyWithContract extends InfoGrantSubsidy {
    public void copyFromDomain(IGrantSubsidy grantSubsidy) {
        super.copyFromDomain(grantSubsidy);
        if (grantSubsidy != null) {
            setInfoGrantContract(InfoGrantContractWithGrantOwnerAndGrantType
                    .newInfoFromDomain(grantSubsidy.getGrantContract()));
        }
    }

    public static InfoGrantSubsidy newInfoFromDomain(IGrantSubsidy grantSubsidy) {
        InfoGrantSubsidyWithContract infoGrantSubsidy = null;
        if (grantSubsidy != null) {
            infoGrantSubsidy = new InfoGrantSubsidyWithContract();
            infoGrantSubsidy.copyFromDomain(grantSubsidy);
        }
        return infoGrantSubsidy;
    }

}
