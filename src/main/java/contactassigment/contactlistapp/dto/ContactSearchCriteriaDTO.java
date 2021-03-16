package contactassigment.contactlistapp.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ContactSearchCriteriaDTO {
	private String firstName = Constants.EMPTY_STRING;
	private String lastName = Constants.EMPTY_STRING;
	private String organisationName = Constants.EMPTY_STRING;
}
