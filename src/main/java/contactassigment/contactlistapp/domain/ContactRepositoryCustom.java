package contactassigment.contactlistapp.domain;

import java.util.List;

import contactassigment.contactlistapp.dto.ContactSearchCriteriaDTO;

public interface ContactRepositoryCustom
{
  List<Contact> searchByNamesFetchOrganisation(ContactSearchCriteriaDTO criteria);
}
