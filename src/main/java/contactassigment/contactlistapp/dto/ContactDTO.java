package contactassigment.contactlistapp.dto;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import contactassigment.contactlistapp.domain.Contact;
import contactassigment.contactlistapp.domain.Organisation;

public class ContactDTO {

	private Integer id;

	private String name;

	private OrganisationDTO organisation;

	public ContactDTO() {
	}

	public ContactDTO(Contact contact) {
		setId(contact.getId());
		setName(buildFullName(contact));
		Organisation org = contact.getOrganisation();
		if (org != null) {
			setOrganisation(new OrganisationDTO(contact.getOrganisation()));
		}
	}

	private String buildFullName(Contact contact) {
		return contact.getFirstName().trim().concat(" " + contact.getLastName().trim());
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getName() {
		return this.name;
	}

	public OrganisationDTO getOrganisation() {
		return organisation;
	}

	public void setOrganisation(OrganisationDTO organisation) {
		this.organisation = organisation;
	}

	public String getOrganisationInfo() {
		return Optional.ofNullable(getOrganisation()).isPresent() ? getOrganisation().getAbn()
				: Constants.EMPTY_STRING;
	}

	public String getOrganisationName() {
		return getContactOrganisation();
	}

	public static ContactDTO createBy(Contact contact) {
		return new ContactDTO(contact);
	}

	public static List<ContactDTO> createListBy(List<Contact> contacts) {
		List<ContactDTO> contactDTOs = new ArrayList<ContactDTO>(contacts.size());
		for (Contact c : contacts) {
			contactDTOs.add(ContactDTO.createBy(c));
		}
		return contactDTOs;
	}

	private String getContactOrganisation() {
		OrganisationDTO org = getOrganisation();
		if (org != null) {
			return org.getName();
		} else {
			return Constants.EMPTY_STRING;
		}
	}
	
}
