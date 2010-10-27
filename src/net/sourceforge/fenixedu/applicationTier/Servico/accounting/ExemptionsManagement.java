package net.sourceforge.fenixedu.applicationTier.Servico.accounting;

import net.sourceforge.fenixedu.dataTransferObject.accounting.AdministrativeOfficeFeeAndInsuranceExemptionBean;
import net.sourceforge.fenixedu.dataTransferObject.accounting.InsuranceExemptionBean;
import net.sourceforge.fenixedu.domain.Employee;
import net.sourceforge.fenixedu.domain.accounting.events.AdministrativeOfficeFeeAndInsuranceExemption;
import net.sourceforge.fenixedu.domain.accounting.events.InsuranceExemption;
import pt.ist.fenixWebFramework.services.Service;

public class ExemptionsManagement {

    @Service
    public static void createAdministrativeOfficeFeeAndInsuranceExemption(final Employee employee,
	    final AdministrativeOfficeFeeAndInsuranceExemptionBean exemptionBean) {
	new AdministrativeOfficeFeeAndInsuranceExemption(employee, exemptionBean.getAdministrativeOfficeFeeAndInsuranceEvent(),
		exemptionBean.getJustificationType(), exemptionBean.getReason(), exemptionBean.getDispatchDate());
    }

    @Service
    public static void createInsuranceExemption(final Employee employee, final InsuranceExemptionBean exemptionBean) {
	new InsuranceExemption(employee, exemptionBean.getInsuranceEvent(), exemptionBean.getJustificationType(), exemptionBean
		.getReason(), exemptionBean.getDispatchDate());
    }

}
