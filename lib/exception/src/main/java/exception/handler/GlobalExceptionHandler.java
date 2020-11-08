package exception.handler;


import com.microsoft.sqlserver.jdbc.SQLServerException;
import exception.custom.*;
import exception.dto.ErrorDTO;
import exception.dto.MultipleActionErrorDTO;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.beans.ConversionNotSupportedException;
import org.springframework.beans.TypeMismatchException;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.converter.HttpMessageNotWritableException;
import org.springframework.jdbc.UncategorizedSQLException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingPathVariableException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.multipart.support.MissingServletRequestPartException;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.util.*;

@ControllerAdvice
@Slf4j
@Configuration
@Order(Ordered.HIGHEST_PRECEDENCE)
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {
	
	private static final String UNEXPECTED_ERROR = "Unexpected error occurred";
	private static final String NULL_VALUE_RECEIVED = "Null Value Received";
	private static final String ACCESS_DENIED = "Access denied";
	private static final String REQUEST_ID = "request_id";
	private static  final String VALIDATION_ERROR = "Validation Errors";
	private static final String MALFORMED_REQUEST = "Malformed JSON request";
	private static final String MALFORMED_RESPONSE = "Malformed response";
	
 
	
	private ErrorDTO setErrorDTO( String message, HttpStatus status ) {
		ErrorDTO errorDTO = new ErrorDTO();
		errorDTO.setMessage(message);
		errorDTO.setStatus(status);
		errorDTO.setTimestamp(new Date());
		errorDTO.setRequestId(MDC.get(REQUEST_ID));
		errorDTO.setDebugMsg("Please use Request Id: " + MDC.get(REQUEST_ID) + " to debug.");
		
		return errorDTO;
	}
	
	private ErrorDTO setErrorDTO( String message, List<MultipleActionErrorDTO> actionErrorDTOS, HttpStatus status ) {
		ErrorDTO errorDTO = new ErrorDTO();
		errorDTO.setMessage(message);
		errorDTO.setStatus(status);
		errorDTO.addMultiActionFailedErrors(actionErrorDTOS);
		errorDTO.setTimestamp(new Date());
		errorDTO.setRequestId(MDC.get(REQUEST_ID));
		errorDTO.setDebugMsg("Please use Request Id: " + MDC.get(REQUEST_ID) + " to debug.");
		
		return errorDTO;
	}
	
	private ErrorDTO setErrorDTO( String message, HttpStatus status, List<FieldError> fieldErrors, List<ObjectError> objectErrors ) {
		ErrorDTO errorDTO = new ErrorDTO();
		errorDTO.setMessage(message);
		errorDTO.setStatus(status);
		errorDTO.setTimestamp(new Date());
		errorDTO.setRequestId(MDC.get(REQUEST_ID));
		errorDTO.addValidationErrors(fieldErrors);
		errorDTO.addValidationError(objectErrors);
		errorDTO.setDebugMsg("Please use Request Id: " + MDC.get(REQUEST_ID) + " to debug.");
		return errorDTO;
	}
	
	private ErrorDTO setErrorDTO( String message, HttpStatus status, Set<ConstraintViolation<?>> subErrors ) {
		ErrorDTO errorDTO = new ErrorDTO();
		errorDTO.setMessage(message);
		errorDTO.setStatus(status);
		errorDTO.setTimestamp(new Date());
		errorDTO.setRequestId(MDC.get(REQUEST_ID));
		errorDTO.addValidationErrors(subErrors);
		errorDTO.setDebugMsg("Please use Request Id: " + MDC.get(REQUEST_ID) + " to debug.");
		return errorDTO;
	}
	
	@ExceptionHandler ({ InvalidStateTransitionException.class })
	@ResponseStatus (value = HttpStatus.NOT_ACCEPTABLE)
	public @ResponseBody
	ErrorDTO handleInvalidStateTransitionException( InvalidStateTransitionException ex, final WebRequest request ) {
		
		final String errorMessage = ex.getType() + ": " + ex.getLocalizedMessage();
		
		MDC.put("ex", errorMessage);
		log.error(errorMessage);
		MDC.remove("ex");
		return setErrorDTO( errorMessage, HttpStatus.NOT_ACCEPTABLE);
	}
	
	@ExceptionHandler ({ DuplicateRecordException.class })
	@ResponseStatus (value = HttpStatus.BAD_REQUEST)
	public @ResponseBody
	ErrorDTO handleDuplicateRecordException( DuplicateRecordException ex, final WebRequest request ) {
		final String errorMessage = ex.getType() + ": " + ex.getLocalizedMessage();
		
		MDC.put("ex", errorMessage);
		log.error(errorMessage);
		MDC.remove("ex");
		return setErrorDTO(ex.getLocalizedMessage(), HttpStatus.BAD_REQUEST);
	}
	
	@ExceptionHandler ({ NotificationException.class })
	@ResponseStatus (value = HttpStatus.BAD_REQUEST)
	public @ResponseBody
	ErrorDTO handleNotificationException( NotificationException ex, final WebRequest request ) {
		final String errorMessage = ex.getType() + ": " + ex.getLocalizedMessage();
		
		MDC.put("ex", errorMessage);
		log.error(errorMessage);
		MDC.remove("ex");
		return setErrorDTO(ex.getLocalizedMessage(), HttpStatus.BAD_REQUEST);
	}
	
	@ExceptionHandler ({ EntityNotFoundException.class })
	@ResponseStatus (value = HttpStatus.NOT_FOUND)
	public @ResponseBody
	ErrorDTO handleEntityNotFoundException( EntityNotFoundException ex, final WebRequest request ) {
		final String errorMessage = ex.getType() + ": " + ex.getLocalizedMessage();
		
		MDC.put("ex", errorMessage);
		log.error(errorMessage);
		MDC.remove("ex");
		return setErrorDTO(ex.getLocalizedMessage(), HttpStatus.NOT_FOUND);
	}
	
	@ExceptionHandler ({ MultipleActionFailedException.class })
	@ResponseStatus (value = HttpStatus.BAD_REQUEST)
	public @ResponseBody
	ErrorDTO handleMultipleActionFailedException( MultipleActionFailedException ex, final WebRequest request ) {
		final String errorMessage = ex.getType() + ": " + ex.getLocalizedMessage();
		
		MDC.put("ex", errorMessage);
		log.error(errorMessage);
		MDC.remove("ex");
		return setErrorDTO(ex.getLocalizedMessage(),ex.getActionErrorDTOS(),  HttpStatus.BAD_REQUEST);
	}
	
	@ExceptionHandler ({ OperationNotAllowedException.class })
	@ResponseStatus (value = HttpStatus.NOT_ACCEPTABLE)
	public @ResponseBody
	ErrorDTO handleOperationNotAllowedException( OperationNotAllowedException ex, final WebRequest request ) {
		final String errorMessage = ex.getType() + ": " + ex.getLocalizedMessage();
		
		MDC.put("ex", errorMessage);
		log.error(errorMessage);
		MDC.remove("ex");
		return setErrorDTO(ex.getLocalizedMessage(), HttpStatus.NOT_ACCEPTABLE);
	}
	
	@ExceptionHandler ({ OperationFailedException.class })
	@ResponseStatus (value = HttpStatus.BAD_REQUEST)
	public @ResponseBody
	ErrorDTO handleOperationFailedException( OperationFailedException ex, final WebRequest request ) {
		final String errorMessage = ex.getType() + ": " + ex.getLocalizedMessage();
		
		MDC.put("ex", errorMessage);
		log.error(errorMessage);
		MDC.remove("ex");
		return setErrorDTO(ex.getLocalizedMessage(), HttpStatus.BAD_REQUEST);
	}
	
	@ExceptionHandler ({ FileHandlingException.class })
	@ResponseStatus (value = HttpStatus.BAD_REQUEST)
	public @ResponseBody
	ErrorDTO handleFileHandlingException( FileHandlingException ex, final WebRequest request ) {
		final String errorMessage = ex.getType() + ": " + ex.getDetailError();
		MDC.put("ex", errorMessage);
		log.error(errorMessage);
		MDC.remove("ex");
		return setErrorDTO(ex.getLocalizedMessage(), HttpStatus.BAD_REQUEST);
	}
	
	@ExceptionHandler (ArrayIndexOutOfBoundsException.class)
	@ResponseStatus (value = HttpStatus.BAD_REQUEST)
	public @ResponseBody
	ErrorDTO handleArrayOutOfBoundException( Exception ex, final WebRequest request ) {
		MDC.put("ex", ex.getLocalizedMessage());
		log.error(UNEXPECTED_ERROR);
		MDC.remove("ex");
		return setErrorDTO(ex.getLocalizedMessage(), HttpStatus.BAD_REQUEST);
	}
	
	@ExceptionHandler (IndexOutOfBoundsException.class)
	public ResponseEntity<Object> handleIndexOutOfBoundsException( IndexOutOfBoundsException ex ) {
		MDC.put("ex", ex.getLocalizedMessage());
		log.error(UNEXPECTED_ERROR);
		MDC.remove("ex");
		return new ResponseEntity<>(setErrorDTO(UNEXPECTED_ERROR, HttpStatus.BAD_REQUEST), HttpStatus.BAD_REQUEST);
	}
	
	@ExceptionHandler (NumberFormatException.class)
	public ResponseEntity<Object> handleNumberFormatException( NumberFormatException ex ) {
		MDC.put("ex", ex.getLocalizedMessage());
		log.error(UNEXPECTED_ERROR);
		MDC.remove("ex");
		return new ResponseEntity<>(setErrorDTO(UNEXPECTED_ERROR, HttpStatus.BAD_REQUEST), HttpStatus.BAD_REQUEST);
	}
	
	@ExceptionHandler (IllegalArgumentException.class)
	public ResponseEntity<Object> handleIllegalArgumentException( IllegalArgumentException ex ) {
		MDC.put("ex", ex.getLocalizedMessage());
		log.error(UNEXPECTED_ERROR);
		MDC.remove("ex");
		return new ResponseEntity<>(setErrorDTO(UNEXPECTED_ERROR, HttpStatus.BAD_REQUEST), HttpStatus.BAD_REQUEST);
	}
	
	@ExceptionHandler (AccessDeniedException.class)
	public ResponseEntity<Object> handleAccessDeniedException( AccessDeniedException ex ) {
		MDC.put("ex", ex.getLocalizedMessage());
		log.error(ACCESS_DENIED);
		MDC.remove("ex");
		return new ResponseEntity<>(setErrorDTO(ACCESS_DENIED, HttpStatus.FORBIDDEN), HttpStatus.FORBIDDEN);
	}
	
	@ExceptionHandler (NullPointerException.class)
	public ResponseEntity<Object> handleNullPointerException( NullPointerException ex ) {
		MDC.put("wx", ex.getLocalizedMessage());
		log.error(NULL_VALUE_RECEIVED, ex);
		return new ResponseEntity<>(setErrorDTO(NULL_VALUE_RECEIVED, HttpStatus.BAD_REQUEST), HttpStatus.BAD_REQUEST);
	}
	
	@ExceptionHandler (MethodArgumentTypeMismatchException.class)
	public ResponseEntity<Object> handleMethodArgumentTypeMismatch( final MethodArgumentTypeMismatchException ex,
	                                                                final WebRequest request ) {
		final String error = ex.getName() + " should be of type " + ex.getRequiredType().getName();
		MDC.put("ex", ex.getLocalizedMessage());
		log.error(error);
		MDC.remove("ex");
		ErrorDTO errorDTO = setErrorDTO(error, HttpStatus.BAD_REQUEST);
		return new ResponseEntity<>(errorDTO, HttpStatus.BAD_REQUEST);
	}
	
	@ExceptionHandler ({ DataAccessException.class })
	public ResponseEntity<Object> handleDataAccessException( final DataAccessException ex, final WebRequest request ) {
		String error;
		HttpStatus httpStatus = HttpStatus.BAD_REQUEST;
		if(ex instanceof UncategorizedSQLException)  {
			UncategorizedSQLException uncategorizedSQLException = (UncategorizedSQLException) ex;
			error =  uncategorizedSQLException.getSQLException().getLocalizedMessage() ;
			switch (uncategorizedSQLException.getSQLException().getErrorCode()) {
				case 51001:
					httpStatus = HttpStatus.UNAUTHORIZED;
					break;
				case 51002:
					httpStatus = HttpStatus.CONFLICT;
					break;
				case 51003:
					httpStatus = HttpStatus.NOT_FOUND;
					break;
					
				default:
					httpStatus = HttpStatus.BAD_REQUEST;
			}
			
		} else if ( ex.getMostSpecificCause() != null) {
			error = ex.getMostSpecificCause().getLocalizedMessage();
		} else if ( ex.getCause() != null) {
			error = ex.getCause().getLocalizedMessage();
		} else {
			error = ex.getLocalizedMessage();
		}
		MDC.put("ex", ex.getLocalizedMessage());
		log.error(error, ex);
		MDC.remove("ex");
		ErrorDTO errorDTO = setErrorDTO(error, httpStatus);
		return new ResponseEntity<>(errorDTO, httpStatus);
	}
	
	@ExceptionHandler ({ SQLServerException.class })
	public ResponseEntity<Object> handleSQLServerException( final SQLServerException ex, final WebRequest request ) {
		String error;
		if( ex.getCause() != null) {
			error = "Cause: " + ex.getCause().getLocalizedMessage() + ". SQLError: " +  ex.getSQLServerError().getErrorMessage();
		} else {
			error = ex.getLocalizedMessage();
		}
		
		MDC.put("ex", ex.getLocalizedMessage());
		MDC.put("sql_server", ex.getSQLServerError().getServerName());
		MDC.put("sql_sp", ex.getSQLServerError().getProcedureName());
		MDC.put("sql_error", ex.getSQLServerError().getErrorMessage());
		MDC.put("sql_error_line", String.valueOf(ex.getSQLServerError().getLineNumber()));
		log.error(error, ex);
		MDC.remove("ex");
		ErrorDTO errorDTO = setErrorDTO(error, HttpStatus.BAD_REQUEST);
		return new ResponseEntity<>(errorDTO, HttpStatus.BAD_REQUEST);
	}
	
	@ExceptionHandler ({ ConstraintViolationException.class })
	public ResponseEntity<Object> handleConstraintViolation( final ConstraintViolationException ex) {
		MDC.put("ex", VALIDATION_ERROR);
		log.error(VALIDATION_ERROR, ex.getConstraintViolations());
		MDC.remove("ex");
		ErrorDTO errorDTO = setErrorDTO(VALIDATION_ERROR, HttpStatus.BAD_REQUEST, ex.getConstraintViolations());
		
		return new ResponseEntity<>(errorDTO, HttpStatus.BAD_REQUEST);
	}
	
	@Override
	protected ResponseEntity<Object> handleHttpRequestMethodNotSupported(
			final HttpRequestMethodNotSupportedException ex, final HttpHeaders headers, final HttpStatus status,
			final WebRequest request ) {
		final StringBuilder builder = new StringBuilder();
		builder.append(ex.getMethod());
		builder.append(" method is not supported for this utility.request. Supported methods are ");
		ex.getSupportedHttpMethods().forEach(t -> builder.append(t + " "));
		final String error = builder.toString();
		log.error(error);
		ErrorDTO errorDTO = setErrorDTO(error, status);
		
		return new ResponseEntity<>(errorDTO, status);
	}
	
	@Override
	protected ResponseEntity<Object> handleHttpMediaTypeNotSupported( final HttpMediaTypeNotSupportedException ex,
	                                                                  final HttpHeaders headers, final HttpStatus status, final WebRequest request ) {
		final StringBuilder builder = new StringBuilder();
		builder.append(ex.getContentType());
		builder.append(" media type is not supported. Supported media types are ");
		ex.getSupportedMediaTypes().forEach(t -> builder.append(t + " "));
		final String error = builder.toString();
		log.error(error);
		ErrorDTO errorDTO = setErrorDTO(error, status);
		
		return new ResponseEntity<>(errorDTO, status);
	}
	
	@Override
	protected ResponseEntity<Object> handleMissingPathVariable( MissingPathVariableException ex, HttpHeaders headers,
	                                                            HttpStatus status, WebRequest request ) {
		final String error = "Path Variable: " + ex.getVariableName() + "is missing from utility.request";
		log.error(error);
		return new ResponseEntity<>(setErrorDTO(error,status), status);
	}
	
	@Override
	protected ResponseEntity<Object> handleHttpMessageNotReadable( HttpMessageNotReadableException ex, HttpHeaders headers,
	                                                               HttpStatus status, WebRequest request ) {
		
		final String error = MALFORMED_REQUEST + ": "+ ex.getLocalizedMessage();
		log.error(error, ex.getHttpInputMessage(), ex.getLocalizedMessage());
		return new ResponseEntity<>(setErrorDTO(error,status), status);
	}
	
	@Override
	protected ResponseEntity<Object> handleHttpMessageNotWritable( HttpMessageNotWritableException ex, HttpHeaders headers,
	                                                               HttpStatus status, WebRequest request ) {
		final StringBuilder error = new StringBuilder();
		error.append(MALFORMED_RESPONSE);
		error.append(" :");
		if( ex.getMostSpecificCause() != null) {
			error.append(ex.getMostSpecificCause().getLocalizedMessage());
		} else {
			error.append(ex.getLocalizedMessage());
		}
		log.error(error.toString(), ex.getLocalizedMessage(), ex.getLocalizedMessage());
		return new ResponseEntity<>(setErrorDTO(error.toString(),status), status);
	}
	
	@Override
	protected ResponseEntity<Object> handleMissingServletRequestParameter(
			final MissingServletRequestParameterException ex, final HttpHeaders headers, final HttpStatus status,
			final WebRequest request ) {
		final String error = ex.getParameterName() + " parameter is missing";
		log.error(error);
		ErrorDTO errorDTO = setErrorDTO(error, status);
		
		return new ResponseEntity<>(errorDTO, status);
	}
	
	@Override
	protected ResponseEntity<Object> handleConversionNotSupported( ConversionNotSupportedException ex,
	                                                               HttpHeaders headers, HttpStatus status, WebRequest request ) {
		final String error = ex.getPropertyName() + " with value: " + ex.getPropertyChangeEvent().getOldValue()
				+ " is not convertible to " + ex.getPropertyChangeEvent().getOldValue();
		
		
		log.error(error);
		return new ResponseEntity<>(setErrorDTO(error,status), status);
	}
	@Override
	protected ResponseEntity<Object> handleTypeMismatch( final TypeMismatchException ex, final HttpHeaders headers, final HttpStatus status, final WebRequest request ) {
		final String error = ex.getValue() + " value for " + ex.getPropertyName() + " should be of type "
				+ ex.getRequiredType();
		
		log.error(error, ex);
		ErrorDTO errorDTO = setErrorDTO(error, HttpStatus.BAD_REQUEST);
		
		return new ResponseEntity<>(errorDTO, HttpStatus.BAD_REQUEST);
	}
	
	@Override
	protected ResponseEntity<Object> handleMethodArgumentNotValid( final MethodArgumentNotValidException ex,
	                                                               final HttpHeaders headers, final HttpStatus status, final WebRequest request ) {
		log.error(
				VALIDATION_ERROR,
				ex.getBindingResult().getFieldErrors(),
				ex.getBindingResult().getGlobalErrors()
		);
		ErrorDTO errorDTO = setErrorDTO(
				VALIDATION_ERROR,
				status,
				ex.getBindingResult().getFieldErrors(),
				ex.getBindingResult().getGlobalErrors()
		);
		return new ResponseEntity<>(errorDTO, status);
	}
	
	@Override
	protected ResponseEntity<Object> handleMissingServletRequestPart( final MissingServletRequestPartException ex,
	                                                                  final HttpHeaders headers, final HttpStatus status, final WebRequest request ) {
		final String error = ex.getRequestPartName() + " part is missing";
		log.error(error, ex);
		ErrorDTO errorDTO = setErrorDTO(error, status);
		return new ResponseEntity<>(errorDTO, status);
	}
	
	@Override
	protected ResponseEntity<Object> handleNoHandlerFoundException( final NoHandlerFoundException ex,
	                                                                final HttpHeaders headers, final HttpStatus status, final WebRequest request ) {
		final String error = "No handler found for " + ex.getHttpMethod() + " " + ex.getRequestURL();
		log.error(error, ex);
		ErrorDTO errorDTO = setErrorDTO(error, status);
		return new ResponseEntity<>(errorDTO, status);
	}
	
	@ExceptionHandler ({ Exception.class })
	public ResponseEntity<ErrorDTO> handleAll( final Exception ex, final WebRequest request ) {
		ex.printStackTrace();
		MDC.put("ex", ex.toString());
		log.error(UNEXPECTED_ERROR);
		MDC.remove("ex");
		ErrorDTO errorDTO = setErrorDTO(UNEXPECTED_ERROR, HttpStatus.INTERNAL_SERVER_ERROR);
		
		return new ResponseEntity<>(errorDTO, HttpStatus.INTERNAL_SERVER_ERROR);
	}
	
}
