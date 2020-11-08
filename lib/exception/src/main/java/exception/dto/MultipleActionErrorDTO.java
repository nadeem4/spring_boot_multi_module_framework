package exception.dto;

import lombok.*;

@Data
@Getter
@Setter
@AllArgsConstructor
@EqualsAndHashCode (callSuper = false)
public class MultipleActionErrorDTO extends  SubErrorDTO {
	private String entity;
	
	private String entityId;
	
	private String message;
	
	private String remedy;
}
