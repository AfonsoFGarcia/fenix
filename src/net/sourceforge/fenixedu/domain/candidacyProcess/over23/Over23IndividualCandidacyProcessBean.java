package net.sourceforge.fenixedu.domain.candidacyProcess.over23;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import net.sourceforge.fenixedu.dataTransferObject.person.ChoosePersonBean;
import net.sourceforge.fenixedu.dataTransferObject.person.PersonBean;
import net.sourceforge.fenixedu.domain.Degree;
import net.sourceforge.fenixedu.domain.DomainReference;

public class Over23IndividualCandidacyProcessBean implements Serializable {

    private DomainReference<Over23CandidacyProcess> candidacyProcess;

    private ChoosePersonBean choosePersonBean;

    private PersonBean personBean;

    private DomainReference<Degree> degreeToAdd;

    private List<DomainReference<Degree>> selectedDegrees;

    public Over23IndividualCandidacyProcessBean() {
	setSelectedDegrees(new ArrayList<Degree>());
    }

    public Over23IndividualCandidacyProcessBean(final List<Degree> degrees) {
	this();
	for (final Degree degree : degrees) {
	    addDegree(degree);
	}
    }

    public Over23CandidacyProcess getCandidacyProcess() {
	return (this.candidacyProcess != null) ? this.candidacyProcess.getObject() : null;
    }

    public void setCandidacyProcess(Over23CandidacyProcess candidacyProcess) {
	this.candidacyProcess = (candidacyProcess != null) ? new DomainReference<Over23CandidacyProcess>(candidacyProcess) : null;
    }

    public boolean hasCandidacyProcess() {
	return getCandidacyProcess() != null;
    }

    public ChoosePersonBean getChoosePersonBean() {
	return choosePersonBean;
    }

    public void setChoosePersonBean(ChoosePersonBean choosePersonBean) {
	this.choosePersonBean = choosePersonBean;
    }

    public PersonBean getPersonBean() {
	return personBean;
    }

    public void setPersonBean(PersonBean personBean) {
	this.personBean = personBean;
    }

    public Degree getDegreeToAdd() {
	return (this.degreeToAdd != null) ? this.degreeToAdd.getObject() : null;
    }

    public void setDegreeToAdd(Degree degreeToAdd) {
	this.degreeToAdd = (degreeToAdd != null) ? new DomainReference<Degree>(degreeToAdd) : null;
    }

    public boolean hasDegreeToAdd() {
	return getDegreeToAdd() != null;
    }

    public void removeDegreeToAdd() {
	degreeToAdd = null;
    }

    public List<Degree> getSelectedDegrees() {
	final List<Degree> result = new ArrayList<Degree>();
	for (final DomainReference<Degree> degree : selectedDegrees) {
	    result.add(degree.getObject());
	}
	return result;
    }

    public void setSelectedDegrees(final List<Degree> degrees) {
	selectedDegrees = new ArrayList<DomainReference<Degree>>();
	for (final Degree degree : degrees) {
	    selectedDegrees.add(new DomainReference<Degree>(degree));
	}
    }

    public void addDegree(final Degree degree) {
	selectedDegrees.add(new DomainReference<Degree>(degree));
    }

    public void removeDegree(final Degree degree) {
	final Iterator<DomainReference<Degree>> iter = selectedDegrees.iterator();
	while (iter.hasNext()) {
	    if (iter.next().getObject() == degree) {
		iter.remove();
		break;
	    }
	}
    }

    public boolean containsDegree(final Degree value) {
	for (final Degree degree : getSelectedDegrees()) {
	    if (degree == value) {
		return true;
	    }
	}
	return false;
    }

    public void removeSelectedDegrees() {
	selectedDegrees.clear();
    }

}
