package contactassigment.contactlistapp.domain.esrepository;

import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import contactassigment.contactlistapp.domain.Contact;

public interface ContactESRepository extends ElasticsearchRepository<Contact, Integer> , ContactESRespositoyCustom{

}
