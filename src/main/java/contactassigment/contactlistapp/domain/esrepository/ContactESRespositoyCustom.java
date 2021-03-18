package contactassigment.contactlistapp.domain.esrepository;

import java.util.List;
import java.util.Optional;

import contactassigment.contactlistapp.domain.Contact;
import contactassigment.contactlistapp.dto.ContactSearchCriteriaDTO;

public interface ContactESRespositoyCustom {

	Optional<List<Contact>> searchByNamesFetchOrganisation(ContactSearchCriteriaDTO criteria);
}
