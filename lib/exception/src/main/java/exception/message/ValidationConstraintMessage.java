package exception.message;

public class ValidationConstraintMessage {
	
	public static String setInvalidLengthErrorMessage( String key, int allowedLength ) {
		return String.format("Max allowed length of %s is %d.", key, allowedLength);
	}
	
	public static String setFieldMissingErrorMessage(String key) {
		return String.format("Key Missing: %s", key);
	}
	
	public static String setInvalidDataErrorMessage(String key, String message) {
		return String.format("Invalid Key: %s; %s", key, message);
	}
	
	public static String setNegativeNotAllowedErrorMessage(String key) {
		return String.format("%s does not allow negative.", key);
	}
	
	public static  String setEmptyPropertyMessage( String key ) {
		return  String.format("Please provide %s", key);
	}
	
	public static  String setInvalidRequestBody( String key ) {
		return  String.format("%s request body is invalid", key);
	}
}
