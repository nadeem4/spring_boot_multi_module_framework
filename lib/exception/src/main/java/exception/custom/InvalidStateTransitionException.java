package exception.custom;

import lombok.Getter;

public class InvalidStateTransitionException extends RuntimeException {
	
	@Getter
	String type = "InvalidStateTransitionException";
	
	public InvalidStateTransitionException( String currentState, String nextState ) {
		
		super("Transition from " + currentState + " to " + nextState + " is not allowed");
	}
}
