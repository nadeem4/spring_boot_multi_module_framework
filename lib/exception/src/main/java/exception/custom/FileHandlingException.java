package exception.custom;

import lombok.Getter;
import lombok.Setter;

public class FileHandlingException extends RuntimeException {
	
	@Getter
	@Setter
	private String fileName;
	
	@Getter
	@Setter
	private String operation;
	
	@Getter
	private String type = "FileHandlingException";
	
	@Getter
	@Setter
	private String detailError;
	
	public FileHandlingException(String fileName, String operation, String detailError) {
		super( String.format("Error occurred while % file: %", operation, fileName));
		this.fileName = fileName;
		this.operation = operation;
		this.detailError =detailError;
	}
}
