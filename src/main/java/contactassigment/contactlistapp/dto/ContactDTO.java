package contactassigment.contactlistapp.dto;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import contactassigment.contactlistapp.domain.Contact;
import contactassigment.contactlistapp.domain.Organisation;
import contactassigment.contactlistapp.service.OrganisationDataHelper;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ContactDTO {

	private Integer id;
	private String name;
	private String firstName;
	private String lastName;
	private LocalDateTime created;
	private OrganisationDTO organisation;

	public ContactDTO(Contact contact) {
		setId(contact.getId());
		buildFullName(contact);
		setCreated(contact.getCreated());
		
		Organisation org = contact.getOrganisation();
		if (org != null) {
			setOrganisation(new OrganisationDTO(contact.getOrganisation()));
		}
	}

	private String buildFullName(Contact contact) {
		this.firstName = contact.getFirstName().trim();
		this.lastName = contact.getLastName().trim();
		return name = this.firstName + (" " + this.lastName);
	}

	public String getOrganisationInfo() {
		return Optional.ofNullable(getOrganisation()).isPresent()
				? OrganisationDataHelper.formatABN(getOrganisation().getAbn())
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
