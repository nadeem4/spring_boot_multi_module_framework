package exception.custom;

import lombok.Getter;

public class DuplicateRecordException extends RuntimeException {
	
	@Getter
	String type = "DuplicateRecordException";
	
	public DuplicateRecordException( String entity, Object id ) {
		
		super(entity + ": "+ id + " is already present");
	}

	public DuplicateRecordException( String entity ) {

		super(entity + " is already present");
	}
}
