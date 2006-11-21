package net.sourceforge.fenixedu.domain.util;

import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.util.workflow.IState;

public class StateMachine {
	public static void execute(IState state) {
		state.checkConditionsToForward();
		state.nextState();
	}
    
    public static void execute(IState state, String nextState) {
        if(state.getValidNextStates().contains(nextState)){
            state.checkConditionsToForward(nextState);
            state.nextState(nextState);
        } else {
            throw new DomainException("error.invalid.next.state");
        }
    }
}
