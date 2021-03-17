package contactassigment.contactlistapp.service;

import java.util.List;
import java.util.Optional;

import javax.persistence.EntityNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import contactassigment.contactlistapp.domain.Contact;
import contactassigment.contactlistapp.domain.Organisation;
import contactassigment.contactlistapp.domain.esrepository.ContactESRepository;
import contactassigment.contactlistapp.domain.jparepository.ContactRepository;
import contactassigment.contactlistapp.domain.jparepository.OrganisationRepository;
import contactassigment.contactlistapp.dto.ContactDTO;
import contactassigment.contactlistapp.dto.ContactSearchCriteriaDTO;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Transactional
@Slf4j
@AllArgsConstructor
public class ContactServiceImpl implements ContactService {

	@Autowired
	private final OrganisationRepository organisationRepo;
	@Autowired
	private final ContactRepository contactRepo;
	@Autowired
	private final ContactESRepository contactESRepo;

	@Override
	public ContactDTO findByIdFetchOrganisation(Integer id) {

		Optional<Contact> cachedDoc = contactESRepo.findById(id);
		if (cachedDoc.isPresent()) {
			log.info("cache hit...!");
			return ContactDTO.createBy(cachedDoc.get());
		}
		log.info("only when not  cached");
		Contact contact = contactRepo.findByIdFetchOrganisation(id);
		contactESRepo.save(contact);
		return (contact != null) ? ContactDTO.createBy(contact) : null;
	}

	@Override
	public List<ContactDTO> listByCriteriaFetchOrganisation(ContactSearchCriteriaDTO criteria) {
		log.debug("Query Criteria: " + criteria);
		List<Contact> resultList = contactRepo.searchByNamesFetchOrganisation(criteria);
		// TODO index here..?
		return ContactDTO.createListBy(resultList);
	}

	@Override
	public ContactDTO updateByDTO(ContactDTO contactDTO) throws EntityNotFoundException {
		log.debug("Update contact with dto: " + contactDTO);
		Contact persistedContact = contactRepo.findByIdFetchOrganisation(contactDTO.getId());
		if (persistedContact == null) {
			throw new EntityNotFoundException(String.format("Unable to find Entity: %s with id: %d",
					Contact.class.getCanonicalName(), contactDTO.getId()));
		}

		persistedContact.setFirstName(contactDTO.getFirstName());
		persistedContact.setLastName(contactDTO.getLastName());

		if (Integer.valueOf("-1").equals(contactDTO.getOrganisation().getId())) {
			persistedContact.setOrganisation(null);
		} else {
			Optional<Organisation> persistedOrg = organisationRepo.findById(contactDTO.getOrganisation().getId());
			if (!persistedOrg.isPresent()) {
				throw new EntityNotFoundException(String.format("Unable to find Entity: %s with id: %d",
						Organisation.class.getCanonicalName(), contactDTO.getOrganisation().getId()));
			}
			persistedContact.setOrganisation(persistedOrg.get());
		}

		persistedContact = contactRepo.save(persistedContact);
		return ContactDTO.createBy(persistedContact);
	}
}
