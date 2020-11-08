package utility.constant;

public class  Messages {
	
	public static String setMessage( String message) { return message; }
	
	public static String setMessage( EntityType entityType, ActionType actionType, String id ) {
		return entityType.getValues() + "# " +  id  + " has been " + actionType.getValues() + " successfully";
	}
	
	public static String setMessage( EntityType entityType, WorkorderState actionType, String id ) {
		return entityType.getValues() + "# " +  id  + " has been " + actionType.getValues() + " successfully";
	}
	
	public static String setMessage( EntityType entityType, WorkorderTagState actionType, String id  ) {
		return entityType.getValues() + "# " +  id  + " has been " + actionType.getValues() + " successfully";
	}

	//TODO need to verify in future
	public static String setMessage( EntityType entityType, String actionType, String id ) {
		return entityType.getValues() + "# " +  id  + " has been " + actionType + " successfully";
	}
}
