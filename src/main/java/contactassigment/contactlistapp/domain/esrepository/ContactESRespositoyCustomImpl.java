package contactassigment.contactlistapp.domain.esrepository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.Operator;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.mapping.IndexCoordinates;
import org.springframework.data.elasticsearch.core.query.NativeSearchQuery;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.util.StringUtils;

import contactassigment.contactlistapp.domain.Contact;
import contactassigment.contactlistapp.dto.ContactSearchCriteriaDTO;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
//@AllArgsConstructor
public class ContactESRespositoyCustomImpl implements ContactESRespositoyCustom {

	@Value("${elasticsearch.toggleFlagOn}")
	private Boolean isESToggleOn;
	
	@AllArgsConstructor
	private static enum DocumentField {
		FIRSTNAME("firstName"), LASTNAME("lastName"), ORGNAME("name");

		private String fname;

		public String toString() {
			return fname;
		}
	}

	@Autowired
	private  ElasticsearchOperations elasticsearchRestTemplate;

	@Override
	public Optional<List<Contact>> searchByNamesFetchOrganisation(ContactSearchCriteriaDTO criteria) {
		log.info("in custom es repo --> for {} ", criteria);

		if(isESToggleOn) {
			log.info(" Elasticsearch toggleFlag is {}. ",isESToggleOn);
			 return Optional.empty();
		}

		NativeSearchQuery searchQuery = null;
		Optional<NativeSearchQuery> matchAllQuery = buildIndexMatchAllQuery(criteria);
		if (matchAllQuery.isPresent()) {
			searchQuery = matchAllQuery.get();
		} else {
			searchQuery = buildIndexSearchQuery(criteria);
		}

		SearchHits<Contact> esContactHits = searchInIndex(searchQuery);
		if (esContactHits.hasSearchHits()) {
			log.info("Got search result hits count {}", esContactHits.getTotalHits());

			esContactHits.forEach(x -> {
				Contact content = x.getContent();
				log.info("-> FirstName {}  LastName {} & OrgName {}", content.getFirstName(), content.getLastName(),
						content.getOrganisation().getName());
			});
			return Optional
					.of(esContactHits.getSearchHits().stream().map(x -> x.getContent()).collect(Collectors.toList()));
		}
		log.info("This criteria got no hits.");
		return Optional.empty();
	}
	
	
	public static Optional<NativeSearchQuery> buildIndexMatchAllQuery(ContactSearchCriteriaDTO criteria) {
		// matches all in index
		if (!hasAllFeildsHaveValues(criteria)) {
			log.info("search for --> all blank fields.");
			QueryBuilder matchAllQuery = QueryBuilders.matchAllQuery();
			return Optional.of(getSearchQuery(matchAllQuery));
		}
		return Optional.empty();
	}

	public static NativeSearchQuery buildIndexSearchQuery(ContactSearchCriteriaDTO criteria) {
		// for special searches
		final String firstName = criteria.getFirstName();
		final String lastName = criteria.getLastName();
		final String orgName = criteria.getOrganisationName();

		Map<DocumentField, String> fnValues = new HashMap<>();
		if (StringUtils.hasText(firstName) && StringUtils.trimAllWhitespace(firstName).length() > 0) {
			fnValues.put(DocumentField.FIRSTNAME, firstName);
		}
		if (StringUtils.hasText(lastName) && StringUtils.trimAllWhitespace(lastName).length() > 0) {
			fnValues.put(DocumentField.LASTNAME, lastName);
		}
		if (StringUtils.hasText(orgName) && StringUtils.trimAllWhitespace(orgName).length() > 0) {
			fnValues.put(DocumentField.ORGNAME, orgName);
		}

		boolean wildFNSearch = hasWildChar(criteria.getFirstName());
		boolean wildLNSearch = hasWildChar(criteria.getLastName());
		boolean wildONSearch = hasWildChar(criteria.getOrganisationName());

//		boolean isWildCharSearch = false;
//		if (wildFNSearch || wildLNSearch || wildONSearch) {
//			log.info(" initiated with wild *");
//			isWildCharSearch = true;
//		}
		log.info("count value is {}", fnValues.size());

		List<QueryBuilder> queryBuilderList = new ArrayList<>();
		if (fnValues.get(DocumentField.FIRSTNAME) != null) {
			QueryBuilder queryStr = buildQueryWith(wildFNSearch, DocumentField.FIRSTNAME, firstName);
			queryBuilderList.add(queryStr);
		}
		if (fnValues.get(DocumentField.LASTNAME) != null) {
			QueryBuilder queryStr = buildQueryWith(wildLNSearch, DocumentField.LASTNAME, lastName);
			queryBuilderList.add(queryStr);
		}
		if (fnValues.get(DocumentField.ORGNAME) != null) {
			QueryBuilder queryStrNoField = wildONSearch
					? QueryBuilders.queryStringQuery(orgName).defaultOperator(Operator.AND)
					: QueryBuilders.matchQuery(DocumentField.ORGNAME.toString(), orgName);
			queryBuilderList.add(queryStrNoField);
		}

		BoolQueryBuilder boolQueryBuilder2 = QueryBuilders.boolQuery();
		boolQueryBuilder2.must().addAll(queryBuilderList);

		BoolQueryBuilder boolQueryBuilder1 = QueryBuilders.boolQuery();
		boolQueryBuilder1.must(boolQueryBuilder2);

		NativeSearchQuery searchQuery = new NativeSearchQueryBuilder().withQuery(boolQueryBuilder1).build();

		log.info("complex : search query -> \n { \"query\": \n {}  }", searchQuery.getQuery());
		return searchQuery;
	}

	private static QueryBuilder buildQueryWith(boolean isWildSearch, DocumentField defaultField, String fieldValue) {
		return isWildSearch ? QueryBuilders.queryStringQuery(fieldValue).defaultField(defaultField.toString())
				.defaultOperator(Operator.AND) : QueryBuilders.matchQuery(defaultField.name(), fieldValue);
	}

	
	private static NativeSearchQuery getSearchQuery(QueryBuilder matchAllQuery) {
		NativeSearchQuery searchQuery = new NativeSearchQueryBuilder().withQuery(matchAllQuery).build();
		log.info("basic : search query -> \n { \"query\": \n {}  }", searchQuery.getQuery());
		return searchQuery;
	}

	private static boolean hasAllFeildsHaveValues(ContactSearchCriteriaDTO criteria) {
		String firstName = criteria.getFirstName();
		String lastName = criteria.getLastName();
		String orgName = criteria.getOrganisationName();
		String allStrVal = firstName.concat(lastName).concat(orgName);
		return StringUtils.hasLength(allStrVal.trim());
	}

	private SearchHits<Contact> searchInIndex(NativeSearchQuery searchQuery) {
		SearchHits<Contact> esContactHits = elasticsearchRestTemplate.search(searchQuery, Contact.class,
				IndexCoordinates.of("contactstore"));
		return esContactHits;
	}

	private static boolean hasWildChar(String fieldValue) {
		return fieldValue.contains("*");
	}

}
