package utility.constant;

public enum EntityType {

	TEST_ENTITY("Test Entity"),
	USER("User");
	
	private String values;
	
	public String getValue() {
		return this.values;
	}
	
	private EntityType( String values) {
		this.values = values;
	}
}
