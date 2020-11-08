package exception.custom;

import exception.dto.MultipleActionErrorDTO;
import lombok.Getter;

import java.util.List;

public class MultipleActionFailedException extends RuntimeException {
	
	@Getter
	private String type = "MultipleActionFailedException";
	
	@Getter
	private List<MultipleActionErrorDTO> actionErrorDTOS;
	
	public MultipleActionFailedException( String message, List<MultipleActionErrorDTO> actionErrorDTOS ) {
		super(message );
		this.actionErrorDTOS = actionErrorDTOS;
	}
	
}
