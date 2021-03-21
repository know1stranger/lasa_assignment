package contactassigment.contactlistapp.exception;

import java.util.List;

import org.springframework.http.HttpStatus;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class ApiError {

	private String requestUrl;
	private String causeMessage;
	private List<String> errors;
	private String reason;
}