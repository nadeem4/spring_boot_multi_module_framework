package notification.constant;

public enum NotificationMethod {
	
	SIGNALR("Signal-R"),
	EMAIL("Email"),
	PUSH("Push"),
	TEXT("Text");
	
	private String values;
	
	public String getValue() {
		return this.values;
	}
	
	private NotificationMethod( String values) {
		this.values = values;
	}
}
