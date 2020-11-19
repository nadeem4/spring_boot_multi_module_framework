package exception.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.internal.engine.path.PathImpl;
import org.jboss.logging.MDC;
import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;

import javax.validation.ConstraintViolation;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
public class ErrorDTO {


	private HttpStatus status;
	
	private String message;
	
	@JsonFormat (shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy hh:mm:ss Z")
	private Date timestamp;
	
	private String requestId;
	
	private List<SubErrorDTO> detailMessage;
	
	private String debugMsg;

	/**
	 *
	 * @param status
	 * @param message
	 */
	public ErrorDTO( HttpStatus status, String message) {
		this.status = status;
		this.requestId = (String) MDC.get("request_id");
		this.debugMsg = "Please use request id: " + (String) MDC.get("request_id") + " to debug";
		this.message = message;
		this.timestamp = new Date();
	}

	/**
	 *
	 * @param fieldErrors
	 */
	public void addValidationErrors( List<FieldError> fieldErrors ) {
		fieldErrors.forEach(this::addValidationError);
	}

	/**
	 *
	 * @param actionErrorDTOS
	 */
	public void addMultiActionFailedErrors( List<MultipleActionErrorDTO> actionErrorDTOS) { actionErrorDTOS.forEach(this::addMultiError);}

	/**
	 *
	 * @param messages
	 */
	public void addAuthError( String messages) {
		addSubError(new AuthErrorDTO(message));
	}

	/**
	 *
	 * @param fieldError
	 */
	private void addValidationError( FieldError fieldError ) {
		this.addValidationError(
				fieldError.getObjectName(),
				fieldError.getField(),
				fieldError.getRejectedValue(),
				fieldError.getDefaultMessage());
	}

	/**
	 *
	 * @param actionErrorDTO
	 */
	private void addMultiError( MultipleActionErrorDTO actionErrorDTO) {
		this.addMultiError(
				actionErrorDTO.getEntity(),
				actionErrorDTO.getEntityId(),
				actionErrorDTO.getMessage(),
				actionErrorDTO.getRemedy()
		);
	}

	/**
	 *
	 * @param object
	 * @param field
	 * @param rejectedValue
	 * @param message
	 */
	private void addValidationError( String object, String field, Object rejectedValue, String message ) {
		addSubError(new ValidationErrorDTO(object, field, rejectedValue, message));
	}

	/**
	 *
	 * @param entity
	 * @param entityId
	 * @param message
	 * @param remedy
	 */
	private void addMultiError( String entity, String entityId, String message, String remedy) { addSubError(new MultipleActionErrorDTO( entity, entityId, message, remedy));}

	/**
	 *
	 * @param subError
	 */
	private void addSubError( SubErrorDTO subError ) {
		if (detailMessage == null) {
			detailMessage = new ArrayList<>();
		}
		detailMessage.add(subError);
	}

	/**
	 *
	 * @param globalErrors
	 */
	public void addValidationError( List<ObjectError> globalErrors ) {
		globalErrors.forEach(this::addValidationError);
	}

	/**
	 *
	 * @param objectError
	 */
	private void addValidationError( ObjectError objectError ) {
		this.addValidationError(
				objectError.getObjectName(),
				objectError.getDefaultMessage());
	}

	/**
	 *
	 * @param object
	 * @param message
	 */
	private void addValidationError( String object, String message ) {
		addSubError(new ValidationErrorDTO(object, message));
	}

	/**
	 *
	 * @param constraintViolations
	 */
	public void addValidationErrors( Set<ConstraintViolation<?>> constraintViolations ) {
		constraintViolations.forEach(this::addValidationError);
	}

	/**
	 *
	 * @param cv
	 */
	private void addValidationError( ConstraintViolation<?> cv ) {
		this.addValidationError(
				cv.getRootBeanClass().getSimpleName(),
				( (PathImpl) cv.getPropertyPath() ).getLeafNode().asString(),
				cv.getInvalidValue(),
				cv.getMessage());
	}
	
}