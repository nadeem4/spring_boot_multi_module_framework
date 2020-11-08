package utility.constant;

public enum EntityType {

	ADDRESS_MASTER("Address Master"),
	WORK_ORDER ("Work Order"),
	TAG("Tag"),
	ORGANIZATION("Organization"),
	USER("User"),
	ITEM("Item"),
	DELIVERY_TERM("Deliver Term"),
	DELIVERY_MODE("Delivery Mode"),
	CHARGE_CODE("Charge Code"),
	CURRENCY("Currency"),
	SPEC_CODE("Specification Code"),
	PRICING_UNIT("Pricing Unit"),
	SERVICE("Service"),
	TOLERANCE_CODE("Tolerance Code"),
	MACHINE("Machine");

	
	
	
	private String values;
	
	public String getValues() {
		return this.values;
	}
	
	private EntityType( String values) {
		this.values = values;
	}
}
