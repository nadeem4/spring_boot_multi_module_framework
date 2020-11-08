package utility.dto;

import exception.dto.MultipleActionErrorDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class PartialSuccessDTO extends MultipleActionErrorDTO {
	
	public PartialSuccessDTO( String entity, String entityId, String message, String remedy ) {
		super(entity, entityId, message, remedy);
	}
}
