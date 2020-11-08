package notification.SignalR;

public class SignalRMessage {
	
	public String target;
	public Object[] arguments;
	
	public SignalRMessage(String target, Object[] arguments) {
		this.target = target;
		this.arguments = arguments;
	}
}