package exception.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class AuthErrorDTO extends SubErrorDTO {
	private String message;
	
}
