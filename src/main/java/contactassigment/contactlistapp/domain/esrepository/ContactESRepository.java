package contactassigment.contactlistapp.domain.esrepository;

import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

import contactassigment.contactlistapp.domain.Contact;

@Repository
public interface ContactESRepository extends ElasticsearchRepository<Contact, Integer> {

}
