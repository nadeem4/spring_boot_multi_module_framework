package exception.custom;

import lombok.Getter;

public class OperationNotAllowedException extends RuntimeException {
	
	@Getter
	String type = "OperationNotAllowedException";
	
	public OperationNotAllowedException( String operationName, String reason  ) {
		super("Operation failed: "+ operationName + ". " + reason);
	}
	public OperationNotAllowedException( String message  ) {
		super(message);
	}
}
