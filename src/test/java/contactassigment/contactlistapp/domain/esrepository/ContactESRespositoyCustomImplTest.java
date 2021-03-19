package contactassigment.contactlistapp.domain.esrepository;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import contactassigment.contactlistapp.domain.Contact;
import contactassigment.contactlistapp.dto.ContactSearchCriteriaDTO;
//@RunWith(SpringRunner.class)
//@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
//@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles(profiles = "test")
// @TestPropertySource(properties =
// "spring.autoconfigure.exclude=contactassigment.contactlistapp.domain.esrepository.ContactESRespositoyCustom")
public class ContactESRespositoyCustomImplTest {

	@Autowired
	@Qualifier("es")
	private ContactESRespositoyCustom contactESRespositoyCustom;

	@Test
	public void testCase1() {
		ContactSearchCriteriaDTO criteria = ContactSearchCriteriaDTO.builder().build();
		Optional<List<Contact>> searchResult = contactESRespositoyCustom
				.searchByNamesFetchOrganisation(criteria);
		assertTrue("Should have result", searchResult.isPresent());
	}
	
	@Test
	public void testCase2() {
		ContactSearchCriteriaDTO criteria = ContactSearchCriteriaDTO.builder().firstName("So").organisationName("for").build();
		Optional<List<Contact>> searchResult = contactESRespositoyCustom
				.searchByNamesFetchOrganisation(criteria);
		assertTrue("Should have result", searchResult.isPresent());
	}
	
	@Test
	public void testCase3() {
		ContactSearchCriteriaDTO criteria = ContactSearchCriteriaDTO.builder().firstName("So*").organisationName("for").build();
		Optional<List<Contact>> searchResult = contactESRespositoyCustom
				.searchByNamesFetchOrganisation(criteria);
		assertTrue("Should have result", searchResult.isPresent());
	}
	
	@Test
	public void testCase4() {
		ContactSearchCriteriaDTO criteria = ContactSearchCriteriaDTO.builder().firstName("So*").lastName("K*").organisationName("for").build();
		Optional<List<Contact>> searchResult = contactESRespositoyCustom
				.searchByNamesFetchOrganisation(criteria);
		assertTrue("Should have result", searchResult.isPresent());
	}
	
	@Test
	public void testCase5() {
		ContactSearchCriteriaDTO criteria = ContactSearchCriteriaDTO.builder().firstName("So*").lastName("K").organisationName("for").build();
		Optional<List<Contact>> searchResult = contactESRespositoyCustom
				.searchByNamesFetchOrganisation(criteria);
		assertTrue("Should have result", searchResult.isPresent());
	}
	
	@Test
	public void testCase6() {
		ContactSearchCriteriaDTO criteria = ContactSearchCriteriaDTO.builder().organisationName("for").build();
		Optional<List<Contact>> searchResult = contactESRespositoyCustom
				.searchByNamesFetchOrganisation(criteria);
		assertTrue("Should have result", searchResult.isPresent());
	}
	
	@Test
	public void testCase7() {
		ContactSearchCriteriaDTO criteria = ContactSearchCriteriaDTO.builder().organisationName("*for****xyz").build();
		Optional<List<Contact>> searchResult = contactESRespositoyCustom
				.searchByNamesFetchOrganisation(criteria);
		assertFalse("Shouldn't have result", searchResult.isPresent());
	}
	
	@Test
	public void testCase8() {
		ContactSearchCriteriaDTO criteria = ContactSearchCriteriaDTO.builder().firstName("*S").lastName("K*n").build();
		Optional<List<Contact>> searchResult = contactESRespositoyCustom
				.searchByNamesFetchOrganisation(criteria);
		assertTrue("Should have result", searchResult.isPresent());
	}
	
	@Test
	public void testCase9() {
		ContactSearchCriteriaDTO criteria = ContactSearchCriteriaDTO.builder().firstName("*SK").lastName("K*n").build();
		Optional<List<Contact>> searchResult = contactESRespositoyCustom
				.searchByNamesFetchOrganisation(criteria);
		assertFalse("Shouldn't have result", searchResult.isPresent());
	}
	
	@Test
	public void testCase10() {
		ContactSearchCriteriaDTO criteria = ContactSearchCriteriaDTO.builder().lastName("K*n").build();
		Optional<List<Contact>> searchResult = contactESRespositoyCustom
				.searchByNamesFetchOrganisation(criteria);
		assertTrue("Should have result", searchResult.isPresent());
	}
	
	@Test
	public void testCase11() {
		ContactSearchCriteriaDTO criteria = ContactSearchCriteriaDTO.builder().lastName("K n").build();
		Optional<List<Contact>> searchResult = contactESRespositoyCustom
				.searchByNamesFetchOrganisation(criteria);
		assertFalse("Shouldn't have result", searchResult.isPresent());
	}
	
	@Test
	public void testCase12() {
		ContactSearchCriteriaDTO criteria = ContactSearchCriteriaDTO.builder().firstName("*S*").build();
		Optional<List<Contact>> searchResult = contactESRespositoyCustom
				.searchByNamesFetchOrganisation(criteria);
		assertTrue("Should have result", searchResult.isPresent());
	}
	
	

}
