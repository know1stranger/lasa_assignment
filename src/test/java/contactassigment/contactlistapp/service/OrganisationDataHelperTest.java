package contactassigment.contactlistapp.service;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import contactassigment.contactlistapp.domain.Organisation;

class OrganisationDataHelperTest {

	private static final String ORG_WITH_ABN = "First CorpHouse (12 345 678 911)";
	private static final String formattedABM = "12 345 678 917";

	@Test
	void testFormatABN() {
		String rawAbn = "12345678917";
		assertEquals(formattedABM, OrganisationDataHelper.formatABN(rawAbn));
	}

	@Test
	void testFormatABNWhenNull() {
		assertNull(OrganisationDataHelper.formatABN(null));
	}
	
	@Test
	void testFormatABNWhenEmpty() {
		assertNull(OrganisationDataHelper.formatABN(""));
	}

	@Test
	void testFormatABNWithParanthesis() {
		OrganisationDataHelper dataHelper = new OrganisationDataHelper(
				ContactServiceTest.buildMockOrganisation());
		dataHelper.getOrgNameWithABN();
		assertEquals(ORG_WITH_ABN, dataHelper.getOrgNameWithABN());
	}

}
