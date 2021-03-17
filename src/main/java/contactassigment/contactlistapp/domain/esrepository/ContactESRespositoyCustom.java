package contactassigment.contactlistapp.domain.esrepository;

import java.util.List;

import contactassigment.contactlistapp.domain.Contact;
import contactassigment.contactlistapp.dto.ContactSearchCriteriaDTO;

public interface ContactESRespositoyCustom {

	List<Contact> searchByNamesFetchOrganisation(ContactSearchCriteriaDTO criteria);
}
