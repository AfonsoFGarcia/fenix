package net.sourceforge.fenixedu.domain.phd.thesis;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.domain.phd.PhdParticipant;
import net.sourceforge.fenixedu.domain.phd.PhdParticipantBean;

public class PhdThesisJuryElementBean extends PhdParticipantBean {

    static private final long serialVersionUID = -5365333247731361583L;

    private PhdThesisProcess thesisProcess;
    private boolean reporter;

    private ThesisJuryElement juryElement;

    public PhdThesisJuryElementBean(final PhdThesisProcess process) {
	super(process.getIndividualProgramProcess());
	setThesisProcess(process);
    }

    public PhdThesisJuryElementBean(final PhdThesisProcess process, final ThesisJuryElement element) {

	this(process);

	setParticipant(element.getParticipant());
	setParticipantType(element.getParticipant().isInternal() ? PhdParticipantType.INTERNAL : PhdParticipantType.EXTERNAL);
	setJuryElement(element);

	setName(element.getName());
	setTitle(element.getTitle());
	setCategory(element.getCategory());
	setWorkLocation(element.getWorkLocation());
	setInstitution(element.getInstitution());

	if (!element.getParticipant().isInternal()) {
	    setQualification(element.getQualification());
	    setAddress(element.getAddress());
	    setEmail(element.getEmail());
	    setPhone(element.getPhone());
	}

	setReporter(element.getReporter().booleanValue());
    }

    public PhdThesisProcess getThesisProcess() {
	return thesisProcess;
    }

    public void setThesisProcess(PhdThesisProcess thesisProcess) {
	this.thesisProcess = thesisProcess;
    }

    public boolean isReporter() {
	return reporter;
    }

    public void setReporter(boolean reporter) {
	this.reporter = reporter;
    }

    public ThesisJuryElement getJuryElement() {
	return juryElement;
    }

    public void setJuryElement(ThesisJuryElement juryElement) {
	this.juryElement = juryElement;
    }

    public List<PhdParticipant> getExistingParticipants() {
	final List<PhdParticipant> result = new ArrayList<PhdParticipant>();
	for (final PhdParticipant participant : getIndividualProgramProcess().getParticipantsSet()) {
	    if (!participant.hasAnyThesisJuryElements()) {
		result.add(participant);
	    }
	}
	return result;
    }

    public List<PhdParticipant> getExistingParticipantsEvenWithJuryElements() {
	final List<PhdParticipant> result = getExistingParticipants();
	for (final PhdParticipant participant : getIndividualProgramProcess().getParticipantsSet()) {
	    if (participant.hasAnyThesisJuryElements() && participant.getThesisJuryElementsCount() == 1
		    && participant.getThesisJuryElements().get(0).getProcessForPresidentJuryElement() == null) {
		result.add(participant);
	    }
	}
	return result;
    }

}
