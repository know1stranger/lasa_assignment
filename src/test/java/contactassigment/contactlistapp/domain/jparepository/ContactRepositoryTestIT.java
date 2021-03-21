package contactassigment.contactlistapp.domain.jparepository;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.test.context.ActiveProfiles;

import contactassigment.contactlistapp.domain.Contact;
import contactassigment.contactlistapp.dto.ContactSearchCriteriaDTO;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
class ContactRepositoryTestIT {

	@Autowired
	@Qualifier("db")
	private ContactRepositoryCustom contactRepositoryCustom;

	@Test
	void findWithEmptyCriteria() {
		ContactSearchCriteriaDTO criteria = ContactSearchCriteriaDTO.builder()
				.build();
		criteria.setFirstName("");
		criteria.setLastName("");
		criteria.setOrganisationName("");

		List<Contact> contacts = contactRepositoryCustom
				.searchByNamesFetchOrganisation(criteria);
		assertNotNull(contacts);
	}

	@Test
	void findWithAllParmas() {
		ContactSearchCriteriaDTO criteria = ContactSearchCriteriaDTO.builder()
				.build();
		criteria.setFirstName("S");
		criteria.setLastName("K");
		criteria.setOrganisationName("A");

		List<Contact> contacts = contactRepositoryCustom
				.searchByNamesFetchOrganisation(criteria);
		assertTrue(contacts.isEmpty());
	}

	@Test
	void findWithContactNames() {
		ContactSearchCriteriaDTO criteria = ContactSearchCriteriaDTO.builder()
				.build();
		criteria.setFirstName("S");
		criteria.setLastName("K");

		List<Contact> contacts = contactRepositoryCustom
				.searchByNamesFetchOrganisation(criteria);
		assertTrue(contacts.isEmpty());
	}

	@Test
	void findWithOrgName() {
		ContactSearchCriteriaDTO criteria = ContactSearchCriteriaDTO.builder()
				.build();
		criteria.setOrganisationName("A");

		List<Contact> contacts = contactRepositoryCustom
				.searchByNamesFetchOrganisation(criteria);
		assertFalse(contacts.isEmpty());
	}

	@Test
	void findWithContactFName() {
		ContactSearchCriteriaDTO criteria = ContactSearchCriteriaDTO.builder()
				.build();
		criteria.setFirstName("S");
		List<Contact> contacts = contactRepositoryCustom
				.searchByNamesFetchOrganisation(criteria);
		assertFalse(contacts.isEmpty());
	}

	@Test
	void findWithContactLName() {
		ContactSearchCriteriaDTO criteria = ContactSearchCriteriaDTO.builder()
				.build();
		criteria.setLastName("K");
		List<Contact> contacts = contactRepositoryCustom
				.searchByNamesFetchOrganisation(criteria);
		assertFalse(contacts.isEmpty());
	}
	
	@Test
	void findWithFNameAndOrgName() {
		ContactSearchCriteriaDTO criteria = ContactSearchCriteriaDTO.builder()
				.build();
		criteria.setFirstName("S");
		criteria.setOrganisationName("A");

		List<Contact> contacts = contactRepositoryCustom
				.searchByNamesFetchOrganisation(criteria);
		assertFalse(contacts.isEmpty());
	}
	
	
	@Test
	void findWithLNameAndOrgName() {
		ContactSearchCriteriaDTO criteria = ContactSearchCriteriaDTO.builder()
				.build();
		criteria.setLastName("K");
		criteria.setOrganisationName("A");

		List<Contact> contacts = contactRepositoryCustom
				.searchByNamesFetchOrganisation(criteria);
		assertFalse(contacts.isEmpty());
	}

}
