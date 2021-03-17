package contactassigment.contactlistapp.domain.jparepository;

import java.util.List;

import contactassigment.contactlistapp.domain.Contact;
import contactassigment.contactlistapp.dto.ContactSearchCriteriaDTO;

public interface ContactRepositoryCustom
{
  List<Contact> searchByNamesFetchOrganisation(ContactSearchCriteriaDTO criteria);
}
