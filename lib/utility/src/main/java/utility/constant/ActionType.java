package utility.constant;

public enum ActionType {
	
	CREATED ("Created"),
	UPDATED ("Updated"),
	DELETED ("Deleted"),
	DEACTIVATED("Deactivated"),
	ACTIVATED("Activated"),
	INVITED("Invited"),
	ASSIGNED("Assigned");
	
	private String values;
	
	public String getValues() {
		return this.values;
	}
	
	private ActionType( String values) {
		this.values = values;
	}
}
