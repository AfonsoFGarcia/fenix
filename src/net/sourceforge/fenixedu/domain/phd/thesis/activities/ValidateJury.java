/**
 * 
 */
package net.sourceforge.fenixedu.domain.phd.thesis.activities;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.domain.caseHandling.PreConditionNotValidException;
import net.sourceforge.fenixedu.domain.phd.thesis.PhdThesisProcess;
import net.sourceforge.fenixedu.domain.phd.thesis.PhdThesisProcessBean;

public class ValidateJury extends PhdThesisActivity {

    @Override
    protected void activityPreConditions(PhdThesisProcess process, IUserView userView) {

	if (process.isJuryValidated()) {
	    throw new PreConditionNotValidException();
	}

	if (!process.hasPresidentJuryElement()) {
	    /*
	     * if this condition is removed then must update
	     * ScheduleThesisMeeting: scheduling is performed by president jury
	     * element
	     */
	    throw new PreConditionNotValidException();
	}

	if (!PhdThesisProcess.isMasterDegreeAdministrativeOfficeEmployee(userView)) {
	    throw new PreConditionNotValidException();
	}
    }

    @Override
    protected PhdThesisProcess executeActivity(PhdThesisProcess process, IUserView userView, Object object) {
	final PhdThesisProcessBean bean = (PhdThesisProcessBean) object;
	process.setWhenJuryValidated(bean.getWhenJuryValidated());
	process.setWhenJuryDesignated(bean.getWhenJuryDesignated());

	if (bean.isToNotify()) {
	    /*
	     * TODO: SEND ALERT after create!!!!!!!!!!!!
	     */
	}

	return process;
    }
}