package utility.mapper;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import exception.custom.OperationFailedException;

public class AppObjectMapper {
	public static String convertObjectToJson(Object object) {
		if (object == null) {
			return null;
		}
		ObjectMapper mapper = new ObjectMapper();
		try {
			return mapper.writeValueAsString(object);
		} catch (JsonProcessingException e) {
			throw  new OperationFailedException("UnableToConvertException","Unable to convert to JSON");
		}
	}
}
