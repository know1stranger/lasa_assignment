package contactassigment.contactlistapp.domain.jparepository;

import lombok.AllArgsConstructor;

@AllArgsConstructor
enum QUERY_PARAM {

	CONTACT_L_NAME("contactLNamePattern"), 
	CONTACT_F_NAME("contactFNamePattern"), 
	ORG_NAME("organisationNamePattern"),
	ORG_NAME_PATTERN(":organisationNamePattern"), 
	CONTACT_L_NAME_PATTERN(":contactLNamePattern"), 
	CONTACT_F_NAME_PATTERN(":contactFNamePattern");

	private String paramTxt;

	@Override
	public String toString() {
		return paramTxt;
	}

}