/*
 * Created on Jun 26, 2004
 */
package net.sourceforge.fenixedu.dataTransferObject.grant.contract;

import net.sourceforge.fenixedu.domain.grant.contract.IGrantInsurance;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;

/**
 * @author Pica
 * @author Barbosa
 */
public class InfoGrantInsuranceWithContractAndPaymentEntity extends InfoGrantInsurance {

    public void copyFromDomain(IGrantInsurance grantInsurance) {
        super.copyFromDomain(grantInsurance);
        if (grantInsurance != null) {
            setInfoGrantContract(InfoGrantContractWithGrantOwnerAndGrantType
                    .newInfoFromDomain(grantInsurance.getGrantContract()));
            setInfoGrantPaymentEntity(InfoGrantPaymentEntity.newInfoFromDomain(grantInsurance
                    .getGrantPaymentEntity()));
        }
    }

    public static InfoGrantInsurance newInfoFromDomain(IGrantInsurance grantInsurance) {
        InfoGrantInsuranceWithContractAndPaymentEntity infoGrantInsuranceWithContract = null;
        if (grantInsurance != null) {
            infoGrantInsuranceWithContract = new InfoGrantInsuranceWithContractAndPaymentEntity();
            infoGrantInsuranceWithContract.copyFromDomain(grantInsurance);
        }
        return infoGrantInsuranceWithContract;
    }

    public void copyToDomain(InfoGrantInsurance infoGrantInsurance, IGrantInsurance grantInsurance) throws ExcepcaoPersistencia {
        super.copyToDomain(infoGrantInsurance, grantInsurance);

        grantInsurance.setGrantContract(InfoGrantContract.newDomainFromInfo(infoGrantInsurance
                .getInfoGrantContract()));
        grantInsurance.setGrantPaymentEntity(InfoGrantPaymentEntity.newDomainFromInfo(infoGrantInsurance
                .getInfoGrantPaymentEntity()));
    }

}
