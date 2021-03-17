package contactassigment.contactlistapp.domain.esrepository;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import org.apache.lucene.util.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.mapping.IndexCoordinates;
import org.springframework.data.elasticsearch.core.query.NativeSearchQuery;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;

import contactassigment.contactlistapp.domain.Contact;
import contactassigment.contactlistapp.dto.ContactSearchCriteriaDTO;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@AllArgsConstructor
public class ContactESRespositoyCustomImpl implements ContactESRespositoyCustom {

	@Autowired
	private final ElasticsearchOperations elasticsearchRestTemplate;

	@Override
	public List<Contact> searchByNamesFetchOrganisation(ContactSearchCriteriaDTO criteria) {

		String firstNameWildCardStr = 
				".*".concat(criteria.getFirstName()).concat(".*");
				// criteria.getFirstName(); // 
		log.info("in custom es repo --> for {} ", firstNameWildCardStr);

		NativeSearchQuery searchQuery = new NativeSearchQueryBuilder()
//				.withFilter(QueryBuilders.regexpQuery("firstname", firstNameWildCardStr)).build();
				.withQuery(QueryBuilders.regexpQuery("firstName", firstNameWildCardStr)).build();

		log.info("search query -> {}", searchQuery.getQuery());

		SearchHits<Contact> esContactHits = elasticsearchRestTemplate.search(searchQuery, Contact.class,
				IndexCoordinates.of("contactstore"));

		esContactHits.forEach(x -> {
			Contact content = x.getContent();
			log.info("-> FirstName {} and LastName {}", content.getFirstName(), content.getLastName());
		});

		List<Contact> esList = esContactHits.getSearchHits().stream().map(x -> x.getContent())
				.collect(Collectors.toList());
		
		if(esList.isEmpty()) {
			return null;
		}
		return esList;
	}

}
