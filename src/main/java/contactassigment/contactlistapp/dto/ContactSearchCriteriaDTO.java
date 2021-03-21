package contactassigment.contactlistapp.dto;

import lombok.Builder;
import lombok.Data;

@Data
public class ContactSearchCriteriaDTO {
	private String firstName = Constants.EMPTY_STRING;
	private String lastName = Constants.EMPTY_STRING;
	private String organisationName = Constants.EMPTY_STRING;

	@Builder
	public ContactSearchCriteriaDTO() {
		firstName = Constants.EMPTY_STRING;
		lastName = Constants.EMPTY_STRING;
		 organisationName = Constants.EMPTY_STRING;

	}

}
