package net.sourceforge.fenixedu.applicationTier.Servico.resourceAllocationManager;

import net.sourceforge.fenixedu.applicationTier.FenixService;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.InvalidArgumentsServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionPeriod;
import net.sourceforge.fenixedu.domain.ExecutionSemester;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.util.PeriodState;

public class AlterExecutionPeriodState extends FenixService {

	public void run(InfoExecutionPeriod infoExecutionPeriod, PeriodState periodState) throws FenixServiceException {

		final ExecutionSemester executionSemester =
				ExecutionYear.readExecutionYearByName(infoExecutionPeriod.getInfoExecutionYear().getYear())
						.getExecutionSemesterFor(infoExecutionPeriod.getSemester());
		if (executionSemester == null) {
			throw new InvalidArgumentsServiceException();
		}
		if (periodState.getStateCode().equals(PeriodState.CURRENT)) {
			// Deactivate the current
			ExecutionSemester.readActualExecutionSemester().setState(new PeriodState(PeriodState.OPEN));
		}
		executionSemester.setState(periodState);
	}

}
