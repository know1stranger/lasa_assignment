package contactassigment.contactlistapp.service;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.context.ActiveProfiles;

import contactassigment.contactlistapp.domain.Contact;
import contactassigment.contactlistapp.domain.Organisation;
import contactassigment.contactlistapp.domain.esrepository.ContactESRepository;
import contactassigment.contactlistapp.domain.jparepository.ContactRepository;
import contactassigment.contactlistapp.domain.jparepository.OrganisationRepository;
import contactassigment.contactlistapp.dto.ContactDTO;
import contactassigment.contactlistapp.dto.ContactSearchCriteriaDTO;

//@Import({TestConfigs.class})
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
// @EnableAutoConfiguration(exclude =
// {FlywayAutoConfiguration.class,DataSourceAutoConfiguration.class,
// HibernateJpaAutoConfiguration.class})
public class ContactServiceTest {

	@LocalServerPort
	private int port;

	@Autowired
	private ContactService contactService;

	@MockBean
	private OrganisationService organisationService;

	@MockBean
	private OrganisationRepository organisationRepo;
	@MockBean
	private ContactRepository contactRepo;
	@MockBean
	ContactESRepository contactESRepo;

	@Test
	public void testFindByIdFetchOrgWhenESHit() {
		// mock
		when(contactESRepo.findById(1))
				.thenReturn(Optional.of(buildMockContactWithId(1)));
		// call service
		ContactDTO dto = contactService.findByIdFetchOrganisation(1);
		assertNotNull(dto);
	}

	@Test
	public void testFindByIdFetchOrgWhenNoRecordInRepos() {
		// mock
		when(contactESRepo.findById(1)).thenReturn(Optional.empty());
		when(contactRepo.findByIdFetchOrganisation(1)).thenReturn(null);
		// call service
		ContactDTO dto = contactService.findByIdFetchOrganisation(1);
		assertNull("No record to be found.", dto);
	}

	@Test
	public void testFindByIdFetchOrgWhenRecordInDB() {
		// mock
		final int contactId = 5001;
		when(contactESRepo.findById(contactId)).thenReturn(Optional.empty());
		Contact contact = buildMockContactWithId(contactId);
		when(contactRepo.findByIdFetchOrganisation(contactId))
				.thenReturn(contact);
		// call service
		ContactDTO dto = contactService.findByIdFetchOrganisation(contactId);
		assertNotNull(dto);
		assertEquals(dto.getId(), contactId);
	}

	@Test
	public void testListByCriteriaFetchOrg() {
		// mock
		final int contactId = 7001;
		ContactSearchCriteriaDTO criteria = ContactSearchCriteriaDTO.builder()
				.build();
		when(contactESRepo.searchByNamesFetchOrganisation(criteria))
				.thenReturn(Optional.empty());

		List<Contact> contacts = new ArrayList<>();
		contacts.add(buildMockContactWithId(contactId));
		contacts.add(buildMockContactWithId(21312));
		contacts.add(buildMockContactWithId(23423));

		when(contactRepo.searchByNamesFetchOrganisation(criteria))
				.thenReturn(contacts);

		// call service
		List<ContactDTO> dtoList = contactService
				.listByCriteriaFetchOrganisation(criteria);

		assertTrue(!dtoList.isEmpty());
		// ContactDTO contactDTO = dtoList.get(0);
		// assertEquals(contactDTO.getId(), contactId);
	}

	@Test
	public void testUpdateByDTO() {
		// mock
		final int contactId = 7001;

		final Contact contactESMock = buildMockContact(contactId, "abc", "opq");
		// prepare mocks
		Organisation organisationMock = buildMockOrganisation();

		Contact contactMock = buildMockContact(
				buildMockContact(contactId, "sdfas", "asdf"), organisationMock);

		when(contactRepo.findByIdFetchOrganisation(contactId))
				.thenReturn(contactMock);
		when(organisationRepo.findById(organisationMock.getId()))
				.thenReturn(Optional.of(organisationMock));

		ContactDTO contactDto = new ContactDTO(contactMock);

		when(contactRepo.findByIdFetchOrganisation(contactDto.getId()))
				.thenReturn(contactMock);

		when(contactESRepo.findById(contactId))
				.thenReturn(Optional.of(contactESMock));
		when(contactRepo.save(contactMock)).thenReturn(contactMock);

		// call service
		ContactDTO contactDtoResponse = contactService.updateByDTO(contactDto);

		assertNotNull(contactDtoResponse);
	}

	/**
	 * Mock helper methods.
	 */

	public static Contact buildMockContactWithId(int id) {
		Contact contact = new Contact();
		contact.setId(id);
		contact.setFirstName("abc");
		contact.setLastName("xyz");
		contact.setCreated(LocalDateTime.now());
		return contact;
	}

	public static Contact buildMockContact(int id, String firstName,
			String lastName) {
		Contact contact = buildMockContactWithId(id);
		contact.setId(id);
		contact.setFirstName(firstName);
		contact.setLastName(lastName);
		return contact;
	}

	public static Contact buildMockContact(Contact contact,
			Organisation organisation) {
		contact.setOrganisation(organisation);
		return contact;
	}

	public static Organisation buildMockOrganisation() {
		Organisation organisation = new Organisation();
		organisation.setId(1001);
		organisation.setAbn("12345678911");
		organisation.setName("First CorpHouse");
		return organisation;
	}

	public static Organisation buildMockOrganisation(int id, String name,
			String abn) {
		Organisation organisation = new Organisation();
		organisation.setId(id);
		organisation.setName(name);
		organisation.setAbn(abn);
		return organisation;
	}

	// @MockBean
	// private OrganisationRepository organisationRepo = null;
	// @MockBean
	// private ContactRepository contactRepo = null;
	// @MockBean
	// private ContactESRepository contactESRepo = null;
	// @Autowired
	// private ContactController contactController;

}
