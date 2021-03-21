package contactassigment.contactlistapp.service;

import java.util.List;
import java.util.Optional;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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

	// more control if we get feature-flag from other service/db config.
	@Value("${elasticsearch.toggleFlagOn:true}")
	private boolean isESToggleOn;

	@Value("${elasticsearch.index.store}")
	private String indexStore;

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
		if (isESToggleOn) {
			return Optional.empty();
		}
		return contactESRespositoyCustom
				.searchByNamesFetchOrganisation(criteria);
	}

	public Optional<Contact> findById(Integer id) {
		if (isESToggleOn) {
			return Optional.empty();
		}
		return contactESRepository.findById(id);
	}

	public Contact save(final Contact contact) {
		if (isESToggleOn) {
			return contact;
		}
		return contactESRepository.save(contact);
	}

	public void saveAll(Iterable<Contact> resultList) {
		if (isESToggleOn) {
			return;
		}
		contactESRepository.saveAll(resultList);
	}

	@PostConstruct
	void loadToESIndex() {
		log.info("Check toggle to load ES {} index.", indexStore);
		if (isESToggleOn) {
			log.info(">>>> elasticsearch toggleFlagOn {} <<<<", isESToggleOn);
			return;
		}
		log.info("Loading to ES index to support elastic searching...!");
		
		List<Contact> resultList = contactCustomRepoImpl
				.searchByNamesFetchOrganisation(
						ContactSearchCriteriaDTO.builder().build());
		
		contactESRepo.saveAll(resultList);
	}

}
