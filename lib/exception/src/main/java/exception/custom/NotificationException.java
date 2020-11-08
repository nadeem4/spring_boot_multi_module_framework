package exception.custom;

import lombok.Getter;
import lombok.Setter;

public class NotificationException extends RuntimeException{
	
	@Getter
	@Setter
	private String source;
	
	@Getter
	private String type = "NotificationException";
	
	public NotificationException( String src, String message) { super(message + " Unable to send notification via "+ src); }
	
	
}
