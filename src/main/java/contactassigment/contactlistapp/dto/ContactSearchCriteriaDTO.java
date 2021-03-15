package contactassigment.contactlistapp.dto;

public class ContactSearchCriteriaDTO
{

  private String name = Constants.EMPTY_STRING;
  private String organisationName = Constants.EMPTY_STRING;
  private String firstName = Constants.EMPTY_STRING;

  public String getName()
  {
    return name;
  }

  public void setName(String name)
  {
    this.name = name;
  }

  public String getOrganisationName()
  {
    return organisationName;
  }

  public void setOrganisationName(String organisationName)
  {
    this.organisationName = organisationName;
  }

public String getFirstName() {
	return firstName;
}

public void setFirstName(String firstName) {
	this.firstName = firstName;
}


}
