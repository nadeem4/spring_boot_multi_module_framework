package utility.constant;

import java.util.Arrays;

public enum WorkorderState {
	CREATED ("Created"),
	APPROVED ( "Approved"),
	SENT ( "Sent"),
	ACCEPTED ( "Accepted"),
	REJECTED ( "Rejected"),
	PENDING ( "Pending"),
	DRAFT ( "Draft"),
	PARTIALLY_SHIPPED_BY_SC ( "Partially Shipped by SC"),
	FULLY_SHIPPED_BY_SC ( "Fully Shipped by SC"),
	RECEIVED_BY_OSP ( "Received by OSP"),
	PROCESSING ( "Processing"),
	PARTIALLY_REPORTED ( "Partially reported"),
	FULLY_REPORTED ( "Fully Reported"),
	FULLY_SHIPPED_BY_OSP ( "Fully Shipped by OSP"),
	PARTIALLY_SHIPPED_BY_OSP ( "Partially Shipped by OSP"),
	RECEIVED_BY_SC ( "Received by SC"),
	RECEIVED_BY_CUSTOMER ( "Received by customer"),
	CANCELLED ( "Cancelled"),
	COMPLETED ( "Completed");
	
	
	private String values;
	
	public String getValues()
	{
		return this.values;
	}
	
	private WorkorderState( String values ) {
		this.values = values;}
	
	public static boolean contains(String state){
		return Arrays.stream(WorkorderState.values()).anyMatch(( t) -> t.getValues().equals(state));
		
	}
	
}
