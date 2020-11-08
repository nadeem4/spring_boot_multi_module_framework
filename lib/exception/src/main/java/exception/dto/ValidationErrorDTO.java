package exception.dto;

import lombok.*;

@Data
@AllArgsConstructor
@Getter
@Setter
@EqualsAndHashCode (callSuper = false)
public class ValidationErrorDTO extends SubErrorDTO {
	private String object;
	private String field;
	private Object rejectedValue;
	private String message;
	
	ValidationErrorDTO(String object, String message) {
		this.object = object;
		this.message = message;
	}
	
}
