/*
 * Created on Jun 26, 2004
 */
package DataBeans.grant.contract;

import DataBeans.util.Cloner;
import Dominio.grant.contract.IGrantInsurance;


/**
 * @author Pica
 * @author Barbosa
 */
public class InfoGrantInsuranceWithContractAndPaymentEntity extends InfoGrantInsurance {

    public void copyFromDomain(IGrantInsurance grantInsurance)
	{
		super.copyFromDomain(grantInsurance);
		if(grantInsurance != null){
		    setInfoGrantContract(InfoGrantContractWithGrantOwnerAndGrantType.newInfoFromDomain(grantInsurance.getGrantContract()));
		    setInfoGrantPaymentEntity(Cloner.copyIGrantPaymentEntity2InfoGrantPaymentEntity(grantInsurance.getGrantPaymentEntity()));
		}
	}
	
	public static InfoGrantInsurance newInfoFromDomain(IGrantInsurance grantInsurance)
	{
		InfoGrantInsuranceWithContractAndPaymentEntity infoGrantInsuranceWithContract = null;
		if(grantInsurance != null){
			infoGrantInsuranceWithContract = new InfoGrantInsuranceWithContractAndPaymentEntity();
			infoGrantInsuranceWithContract.copyFromDomain(grantInsurance);
		}
		return infoGrantInsuranceWithContract;
	}

}
