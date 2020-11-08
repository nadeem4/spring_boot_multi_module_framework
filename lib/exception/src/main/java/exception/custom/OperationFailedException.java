package exception.custom;

import lombok.Getter;

public class OperationFailedException extends RuntimeException {
	
	@Getter
	String type = "OperationFailedException";
	
	public OperationFailedException( String operationName, String reason  ) {
		
		super("Unable to perform "+ operationName + ". " + reason);
	}

	public OperationFailedException(String message) {
		super(message);
	}
}
