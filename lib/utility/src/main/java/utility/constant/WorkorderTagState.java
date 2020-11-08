package utility.constant;

import java.util.Arrays;

public enum WorkorderTagState {
	
	AVAILABLE ( "Available"),
	SHIPPED_TO_OSP ( "Shipped to OSP"),
	RECEIVED_BY_OSP ( "Received by OSP"),
	PROCESSING ( "Processing"),
	REPORTED ( "Reported"),
	CANCELLED ( "Cancelled"),
	NON_CONFORM ( "Non Conform");
	
	private String values;
	
	public String getValues()
	{
		return this.values;
	}
	
	private WorkorderTagState( String values ) {
		this.values = values;}
	
	public static boolean contains(String state){
		return Arrays.stream(WorkorderTagState.values()).anyMatch(( t) -> t.getValues().equals(state));
	}
	
	
}
