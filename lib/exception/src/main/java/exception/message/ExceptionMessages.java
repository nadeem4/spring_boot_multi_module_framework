package exception.message;

import exception.constant.ValidationExceptionType;

import javax.validation.ConstraintValidatorContext;

public class ExceptionMessages {
	
	public static void setValidationErrorMessage( ConstraintValidatorContext context, ValidationExceptionType exceptionType, String key ) {
		switch ( exceptionType) {
			case MISSING_FIELD_ERROR:
				context.buildConstraintViolationWithTemplate(
						ValidationConstraintMessage.setFieldMissingErrorMessage(key))
						.addConstraintViolation();
				break;
			case EMPTY_PROPERTY_ERROR:
				context.buildConstraintViolationWithTemplate(
						ValidationConstraintMessage.setEmptyPropertyMessage(key))
						.addConstraintViolation();
				break;
			
			case NEGATIVE_NOT_ALLOWED_ERROR:
				context.buildConstraintViolationWithTemplate(
						ValidationConstraintMessage.setNegativeNotAllowedErrorMessage(key))
						.addConstraintViolation();
				break;
		}
		
	}
	
	public static void setValidationErrorMessage( ConstraintValidatorContext context, ValidationExceptionType exceptionType, String key, int allowedLength ) {
		switch ( exceptionType) {
			case INVALID_LENGTH_ERROR:
				context.buildConstraintViolationWithTemplate(
						ValidationConstraintMessage.setInvalidLengthErrorMessage(key, allowedLength))
						.addConstraintViolation();
		}
		
	}
	
	public static void setValidationErrorMessage( ConstraintValidatorContext context, ValidationExceptionType exceptionType, String key, String additionalMsg ){
		switch ( exceptionType) {
			case INVALID_DATA_ERROR:
				context.buildConstraintViolationWithTemplate(
						ValidationConstraintMessage.setInvalidDataErrorMessage(key, additionalMsg))
						.addConstraintViolation();
				break;
		}
		
	}
	
	public static String setValidationErrorMessage(String key, ValidationExceptionType exceptionType) {
		switch ( exceptionType ) {
			case INVALID_REQUEST_BODY:
				return ValidationConstraintMessage.setInvalidRequestBody(key);
				
			default:
				return "";
				
		}
	}
}
