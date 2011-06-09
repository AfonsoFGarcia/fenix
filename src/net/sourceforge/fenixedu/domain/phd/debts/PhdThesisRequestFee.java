package net.sourceforge.fenixedu.domain.phd.debts;

import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.accounting.EventType;
import net.sourceforge.fenixedu.domain.administrativeOffice.AdministrativeOffice;
import net.sourceforge.fenixedu.domain.administrativeOffice.AdministrativeOfficeType;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.phd.PhdIndividualProgramProcess;
import net.sourceforge.fenixedu.domain.phd.PhdProgram;

public class PhdThesisRequestFee extends PhdThesisRequestFee_Base {

    private PhdThesisRequestFee() {
	super();
    }

    public PhdThesisRequestFee(final PhdIndividualProgramProcess process) {
	this();
	init(AdministrativeOffice.readByAdministrativeOfficeType(AdministrativeOfficeType.MASTER_DEGREE), process.getPerson(),
		process);
    }

    private void init(AdministrativeOffice administrativeOffice, Person person, PhdIndividualProgramProcess process) {
	super.init(administrativeOffice, EventType.PHD_THESIS_REQUEST_FEE, person);
	checkProcess(process);
	super.setProcess(process);
    }

    private void checkProcess(PhdIndividualProgramProcess process) {
	check(process, "error.PhdThesisRequestFee.process.cannot.be.null");

	if (process.hasThesisRequestFee()) {
	    throw new DomainException("error.PhdThesisRequestFee.process.already.has.thesis.fee");
	}
    }

    @Override
    protected void disconnect() {
	removeProcess();
	super.disconnect();
    }

    @Override
    protected PhdProgram getPhdProgram() {
	return getProcess().getPhdProgram();
    }

    @Override
    public boolean isExemptionAppliable() {
	return true;
    }

    @Override
    public PhdIndividualProgramProcess getPhdIndividualProgramProcess() {
	return getProcess();
    }
}
