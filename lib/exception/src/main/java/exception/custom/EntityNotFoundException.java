package exception.custom;

import lombok.Getter;

public class EntityNotFoundException extends RuntimeException {
	
	@Getter
	String type = "EntityNotFoundException";
	
	
	public EntityNotFoundException( String entity, Object id ) {
		super(entity + ": " + id + " is not found.");
	}

	public EntityNotFoundException(String s) {
		super(s);
	}
}
