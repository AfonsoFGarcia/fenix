package net.sourceforge.fenixedu.domain.candidacy;

import java.util.Collection;

import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.util.workflow.Operation;

public class PreCandidacySituation extends PreCandidacySituation_Base {

    public PreCandidacySituation(Candidacy candidacy) {
        this(candidacy, candidacy.getPerson());
    }

    public PreCandidacySituation(Candidacy candidacy, Person person) {
        super();
        init(candidacy, person);
    }

    @Override
    public CandidacySituationType getCandidacySituationType() {
        return CandidacySituationType.PRE_CANDIDACY;
    }

    @Override
    public boolean getCanGeneratePass() {
        return false;
    }

    @Override
    public Collection<Operation> getOperationsForPerson(Person person) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public boolean canExecuteOperationAutomatically() {
        // TODO Auto-generated method stub
        return false;
    }
}