package net.sourceforge.fenixedu.presentationTier.renderers.providers.accounting.paymentPlans;

import java.util.Collections;
import java.util.SortedSet;
import java.util.TreeSet;

import net.sourceforge.fenixedu.dataTransferObject.accounting.paymentPlan.InstallmentBean;
import net.sourceforge.fenixedu.domain.ExecutionSemester;
import net.sourceforge.fenixedu.presentationTier.renderers.converters.DomainObjectKeyArrayConverter;
import pt.ist.fenixWebFramework.renderers.DataProvider;
import pt.ist.fenixWebFramework.renderers.components.converters.Converter;

public class ExecutionSemestersForInstallmentCreationProvider implements DataProvider {
    public Object provide(Object source, Object currentValue) {
	final InstallmentBean installmentBean = (InstallmentBean) source;

	if (installmentBean.getPaymentPlanBean().getExecutionYear() != null) {
	    final SortedSet<ExecutionSemester> result = new TreeSet<ExecutionSemester>(
		    ExecutionSemester.EXECUTION_PERIOD_COMPARATOR_BY_SEMESTER_AND_YEAR);
	    result.addAll(installmentBean.getPaymentPlanBean().getExecutionYear().getExecutionPeriods());
	    return result;
	}

	return Collections.EMPTY_LIST;

    }

    public Converter getConverter() {
	return new DomainObjectKeyArrayConverter();
    }
}
