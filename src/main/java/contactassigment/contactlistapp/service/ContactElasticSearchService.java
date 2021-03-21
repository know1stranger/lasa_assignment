package contactassigment.contactlistapp.service;

import java.util.List;
import java.util.Optional;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import contactassigment.contactlistapp.domain.Contact;
import contactassigment.contactlistapp.domain.esrepository.ContactESRepository;
import contactassigment.contactlistapp.domain.esrepository.ContactESRespositoyCustom;
import contactassigment.contactlistapp.domain.jparepository.ContactRepositoryCustom;
import contactassigment.contactlistapp.dto.ContactSearchCriteriaDTO;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class ContactElasticSearchService {

	@Autowired
	private ContactESRepository contactESRepository;
	
	@Autowired
	private ContactESRespositoyCustom contactESRespositoyCustom;
	
	@Autowired
	private ContactESRepository contactESRepo;
	@Autowired
	private ContactRepositoryCustom contactCustomRepoImpl;
	

	public Optional<List<Contact>> searchByNamesFetchOrganisation(
			ContactSearchCriteriaDTO criteria) {
		return contactESRespositoyCustom
				.searchByNamesFetchOrganisation(criteria);
	}

	public Optional<Contact> findById(Integer id) {
		return contactESRepository.findById(id);
	}

	public Contact save(Contact contact) {
		return contactESRepository.save(contact);
	}

	public void saveAll(Iterable<Contact> resultList) {
		contactESRepository.saveAll(resultList);
	}

	@PostConstruct
	void loadToESIndex() {
		log.info("Loading to ES index to support elastic searching...!");
		List<Contact> resultList = contactCustomRepoImpl.searchByNamesFetchOrganisation(
				ContactSearchCriteriaDTO.builder()
						.build());
		contactESRepo.saveAll(resultList);
	}
}
