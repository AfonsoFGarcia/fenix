package net.sourceforge.fenixedu.applicationTier.Servico.assiduousness;

import java.math.BigDecimal;

import net.sourceforge.fenixedu.applicationTier.FenixService;
import net.sourceforge.fenixedu.domain.assiduousness.ClosedMonth;
import net.sourceforge.fenixedu.domain.assiduousness.ExtraWorkRequest;
import net.sourceforge.fenixedu.domain.assiduousness.UnitExtraWorkAmount;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTierOracle.Oracle.GiafInterface;

import org.apache.struts.action.ActionMessage;
import org.joda.time.DateTimeFieldType;

public class UpdateExtraWorkClosedMonth extends FenixService {

    public ActionMessage run(ClosedMonth closedMonth) throws ExcepcaoPersistencia {
	BigDecimal totalMonthAmount = new BigDecimal(0.0);
	GiafInterface giafInterface = new GiafInterface();
	for (ExtraWorkRequest extraWorkRequest : rootDomainObject.getExtraWorkRequests()) {
	    if (extraWorkRequest.getPartialPayingDate().equals(closedMonth.getClosedYearMonth())
		    && extraWorkRequest.getApproved()) {
		try {
		    Double oldValue = extraWorkRequest.getAmount();
		    giafInterface.updateExtraWorkRequest(extraWorkRequest);
		    int year = extraWorkRequest.getPartialPayingDate().get(DateTimeFieldType.year());
		    if (extraWorkRequest.getPartialPayingDate().get(DateTimeFieldType.monthOfYear()) == 12) {
			year++;
		    }
		    UnitExtraWorkAmount unitExtraWorkAmount = extraWorkRequest.getUnit().getUnitExtraWorkAmountByYear(year);
		    unitExtraWorkAmount.updateValue(oldValue, extraWorkRequest.getAmount());
		    totalMonthAmount = totalMonthAmount.add(BigDecimal.valueOf(extraWorkRequest.getAmount()));
		} catch (ExcepcaoPersistencia e) {
		    e.printStackTrace();
		    return new ActionMessage("error.connectionError");
		}
	    }
	}
	double giafTotalMonthAmount = giafInterface.getTotalMonthAmount(closedMonth.getClosedYearMonth());
	if (totalMonthAmount.doubleValue() != giafTotalMonthAmount) {
	    return new ActionMessage("error.extraWork.totalMonthValueNotEqual", totalMonthAmount, giafTotalMonthAmount);
	}
	return null;
    }
}