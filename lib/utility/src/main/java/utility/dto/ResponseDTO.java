package utility.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.jboss.logging.MDC;
import org.springframework.http.HttpStatus;
import utility.constant.AppConstant;

import java.util.Date;
import java.util.List;



@Accessors (chain = true)
@Getter
public class ResponseDTO {
	
	private HttpStatus status;
	
	@Setter
	private String message;
	
	@JsonFormat (shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy hh:mm:ss Z")
	private Date timestamp;
	
	private String requestId;
	
	@Setter
	private List<PartialSuccessDTO> detailMessage;
	
	public ResponseDTO() {
		this.requestId = (String) MDC.get(AppConstant.REQUEST_ID);
		this.status = HttpStatus.CREATED;
		this.timestamp = new Date();
	}
	
	public static ResponseDTO setResponseDTO( String message ) { return new ResponseDTO().setMessage(message); }
	
	public static ResponseDTO setResponseDTO( String message, List<PartialSuccessDTO> details ) {
		return new ResponseDTO().setMessage(message).setDetailMessage(details);
	}
}
