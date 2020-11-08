package notification.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

@Getter
@Setter
@NoArgsConstructor
@Accessors(chain = true)
@ToString
public class GetNotificationDTO {

	private Integer id;
	
	private String message;
	
	private Integer status;
	
	private String timestamp;
	
	private String entity;
	
	private String entityKey;
	
	private Integer userId;
}
