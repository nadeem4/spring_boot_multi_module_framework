package utility.dto;

import exception.dto.MultipleActionErrorDTO;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@Accessors(chain = true)
public class DetailDTO {
	
	private String message;
	private List<PartialSuccessDTO> partialSuccessDTOList;
	
	public void addPartialSuccessDTOs( List<MultipleActionErrorDTO> actionErrorDTOS) { actionErrorDTOS.forEach(this::addPartialSuccessDTO);}
	
	private void addPartialSuccessDTO( MultipleActionErrorDTO actionErrorDTO) {
		this.addPartialSuccessDTO(
				actionErrorDTO.getEntity(),
				actionErrorDTO.getEntityId(),
				actionErrorDTO.getMessage(),
				actionErrorDTO.getRemedy()
		);
	}
	
	
	private void addPartialSuccessDTO( String entity, String entityId, String message, String remedy) {
		if( partialSuccessDTOList == null) {
			partialSuccessDTOList = new ArrayList<>();
		}
		partialSuccessDTOList.add(new PartialSuccessDTO(entity, entityId, message, remedy));
	}
}
