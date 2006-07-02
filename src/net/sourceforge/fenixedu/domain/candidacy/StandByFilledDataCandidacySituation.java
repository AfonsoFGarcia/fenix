package net.sourceforge.fenixedu.domain.candidacy;

import java.util.HashSet;
import java.util.Set;

import net.sourceforge.fenixedu.domain.exceptions.DomainException;

public class StandByFilledDataCandidacySituation extends StandByFilledDataCandidacySituation_Base {
    
    public  StandByFilledDataCandidacySituation(Candidacy candidacy) {
        super();
        setCandidacy(candidacy);
    }

	@Override
	public void checkConditionsToForward() {
		
	}

	@Override
	public void nextState() {
		new StandByConfirmedDataCandidacySituation(getCandidacy());
	}
    
    @Override
    public boolean canChangePersonalData() {
        return true;
    }

    @Override
    public void checkConditionsToForward(String nextState) {
        checkConditionsToForward();
    }

    @Override
    public CandidacySituationType getCandidacySituationType() {
        return CandidacySituationType.STAND_BY_FILLED_DATA;
    }

    @Override
    public Set<String> getValidNextStates() {
        Set<String> nextStates = new HashSet<String>();
        nextStates.add(CandidacySituationType.STAND_BY_CONFIRMED_DATA.toString());
        nextStates.add(CandidacySituationType.STAND_BY.toString());        
        return nextStates;
    }

    @Override
    public void nextState(String nextState) {
        CandidacySituationType nextStateType = CandidacySituationType.valueOf(nextState);
        switch (nextStateType) {
        case STAND_BY:
            new StandByCandidacySituation(getCandidacy());
            break;
        case STAND_BY_CONFIRMED_DATA:
            new StandByConfirmedDataCandidacySituation(getCandidacy());
            break;
        default:
            throw new DomainException("");
        }
        
    }

    @Override
    public boolean canCandidacyDataBeValidated() {
        return true;
    }
    
}
