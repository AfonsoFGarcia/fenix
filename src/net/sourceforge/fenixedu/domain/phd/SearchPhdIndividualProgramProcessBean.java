package net.sourceforge.fenixedu.domain.phd;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import net.sourceforge.fenixedu.commons.CollectionUtils;
import net.sourceforge.fenixedu.domain.DomainReference;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.Person;

import org.apache.commons.lang.StringUtils;

import pt.utl.ist.fenix.tools.predicates.AndPredicate;
import pt.utl.ist.fenix.tools.predicates.InlinePredicate;
import pt.utl.ist.fenix.tools.predicates.Predicate;

public class SearchPhdIndividualProgramProcessBean implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = -5653277152319382139L;

    private DomainReference<ExecutionYear> executionYear;

    private PhdIndividualProgramProcessState processState;

    private String processNumber;

    private Integer studentNumber;

    private List<DomainReference<PhdProgram>> phdPrograms;

    private Boolean filterPhdPrograms = Boolean.TRUE;

    private List<DomainReference<PhdIndividualProgramProcess>> processes;

    private Boolean filterPhdProcesses = Boolean.TRUE;

    private String name;

    private PhdProgramCandidacyProcessState candidacyProcessState;

    public PhdProgramCandidacyProcessState getCandidacyProcessState() {
	return candidacyProcessState;
    }

    public void setCandidacyProcessState(PhdProgramCandidacyProcessState candidacyProcessState) {
	this.candidacyProcessState = candidacyProcessState;
    }

    public Boolean getFilterPhdPrograms() {
	return filterPhdPrograms;
    }

    public void setFilterPhdPrograms(Boolean filterPhdPrograms) {
	this.filterPhdPrograms = filterPhdPrograms;
    }

    public Boolean getFilterPhdProcesses() {
	return filterPhdProcesses;
    }

    public void setFilterPhdProcesses(Boolean filterPhdProcesses) {
	this.filterPhdProcesses = filterPhdProcesses;
    }

    public String getName() {
	return name;
    }

    public void setName(String name) {
	this.name = name;
    }

    public List<PhdProgram> getPhdPrograms() {
	final List<PhdProgram> result = new ArrayList<PhdProgram>();
	for (final DomainReference<PhdProgram> each : this.phdPrograms) {
	    result.add(each.getObject());
	}

	return result;
    }

    public void setPhdPrograms(List<PhdProgram> phdPrograms) {
	setPhdPrograms((Collection<PhdProgram>) phdPrograms);
    }

    public void setPhdPrograms(Collection<PhdProgram> phdPrograms) {
	final List<DomainReference<PhdProgram>> result = new ArrayList<DomainReference<PhdProgram>>();
	for (final PhdProgram each : phdPrograms) {
	    result.add(new DomainReference<PhdProgram>(each));
	}

	this.phdPrograms = result;
    }

    public ExecutionYear getExecutionYear() {
	return (this.executionYear != null) ? this.executionYear.getObject() : null;
    }

    public void setExecutionYear(ExecutionYear executionYear) {
	this.executionYear = (executionYear != null) ? new DomainReference<ExecutionYear>(executionYear) : null;
    }

    public PhdIndividualProgramProcessState getProcessState() {
	return processState;
    }

    public void setProcessState(PhdIndividualProgramProcessState processState) {
	this.processState = processState;
    }

    public String getProcessNumber() {
	return processNumber;
    }

    public void setProcessNumber(String processNumber) {
	this.processNumber = processNumber;
    }

    public Integer getStudentNumber() {
	return studentNumber;
    }

    public void setStudentNumber(Integer studentNumber) {
	this.studentNumber = studentNumber;
    }

    public List<PhdIndividualProgramProcess> getProcesses() {
	final List<PhdIndividualProgramProcess> result = new ArrayList<PhdIndividualProgramProcess>();
	for (final DomainReference<PhdIndividualProgramProcess> each : this.processes) {
	    result.add(each.getObject());
	}

	return result;
    }

    public void setProcesses(List<PhdIndividualProgramProcess> processes) {
	final List<DomainReference<PhdIndividualProgramProcess>> result = new ArrayList<DomainReference<PhdIndividualProgramProcess>>();
	for (final PhdIndividualProgramProcess each : processes) {
	    result.add(new DomainReference<PhdIndividualProgramProcess>(each));
	}

	this.processes = result;
    }

    public Predicate<PhdIndividualProgramProcess> getPredicates() {
	final AndPredicate<PhdIndividualProgramProcess> result = new AndPredicate<PhdIndividualProgramProcess>();

	if (getExecutionYear() != null) {
	    result.add(new InlinePredicate<PhdIndividualProgramProcess, ExecutionYear>(getExecutionYear()) {
		@Override
		public boolean eval(PhdIndividualProgramProcess toEval) {
		    return toEval.getExecutionYear() == getValue();
		}
	    });
	}

	if (getProcessState() != null) {
	    result.add(new InlinePredicate<PhdIndividualProgramProcess, PhdIndividualProgramProcessState>(getProcessState()) {
		@Override
		public boolean eval(PhdIndividualProgramProcess toEval) {
		    return toEval.getActiveState() == getValue();
		}
	    });
	}

	if (!StringUtils.isEmpty(getProcessNumber())) {
	    result.add(new InlinePredicate<PhdIndividualProgramProcess, String>(getProcessNumber()) {
		@Override
		public boolean eval(PhdIndividualProgramProcess toEval) {
		    return toEval.getProcessNumber().equals(getValue());
		}
	    });
	}

	if (getStudentNumber() != null) {
	    result.add(new InlinePredicate<PhdIndividualProgramProcess, Integer>(getStudentNumber()) {
		@Override
		public boolean eval(PhdIndividualProgramProcess toEval) {
		    return toEval.getStudent() != null && toEval.getStudent().getNumber().compareTo(getValue()) == 0;
		}
	    });
	}

	if (getFilterPhdPrograms() != null && getFilterPhdPrograms().booleanValue()) {
	    result.add(new InlinePredicate<PhdIndividualProgramProcess, List<PhdProgram>>(getPhdPrograms()) {

		@Override
		public boolean eval(PhdIndividualProgramProcess toEval) {
		    if (toEval.hasPhdProgram()) {
			return getValue().contains(toEval.getPhdProgram());
		    } else if (toEval.hasPhdProgramFocusArea()) {
			return !CollectionUtils.intersection(getValue(), toEval.getPhdProgramFocusArea().getPhdPrograms())
				.isEmpty();
		    } else {
			return false;
		    }

		}

	    });
	}

	if (getFilterPhdProcesses() != null && getFilterPhdProcesses().booleanValue()) {
	    result.add(new InlinePredicate<PhdIndividualProgramProcess, List<PhdIndividualProgramProcess>>(getProcesses()) {

		@Override
		public boolean eval(PhdIndividualProgramProcess toEval) {
		    return getValue().contains(toEval);
		}

	    });
	}

	if (!StringUtils.isEmpty(getName())) {
	    result.add(new InlinePredicate<PhdIndividualProgramProcess, String>(getName()) {
		@Override
		public boolean eval(PhdIndividualProgramProcess toEval) {
		    return Person.findPerson(getValue()).contains(toEval.getPerson());
		}
	    });
	}

	if (getCandidacyProcessState() != null) {
	    result.add(new InlinePredicate<PhdIndividualProgramProcess, PhdProgramCandidacyProcessState>(
		    getCandidacyProcessState()) {
		@Override
		public boolean eval(PhdIndividualProgramProcess toEval) {
		    return toEval.hasCandidacyProcess() && toEval.getCandidacyProcess().getActiveState() == getValue();
		}
	    });
	}

	return result;
    }
}
