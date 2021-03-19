package contactassigment.contactlistapp.dto;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Data
@Builder
public class ContactSearchCriteriaDTO {
	@Builder.Default
	private String firstName = Constants.EMPTY_STRING;
	@Builder.Default
	private String lastName = Constants.EMPTY_STRING;
	@Builder.Default
	private String organisationName = Constants.EMPTY_STRING;
}
