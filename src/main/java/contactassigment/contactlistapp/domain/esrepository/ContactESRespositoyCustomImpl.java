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
@AllArgsConstructor
public class ContactESRespositoyCustomImpl implements ContactESRespositoyCustom {

	private static final String field_firstName = "firstName";
	private static final String field_lastName = "lastName";
	private static final String field_orgName = "name";

	@Autowired
	private final ElasticsearchOperations elasticsearchRestTemplate;

	@Override
	public Optional<List<Contact>> searchByNamesFetchOrganisation(ContactSearchCriteriaDTO criteria) {
		log.info("in custom es repo --> for {} ", criteria);
//		// TODO: Toogle feature
//		if(true) {
//			return Optional.empty();
//		}
		NativeSearchQuery searchQuery = buildSearchQueryByCriteria(criteria);
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

	public static NativeSearchQuery buildSearchQueryByCriteria(ContactSearchCriteriaDTO criteria) {

		// matches all in index
		if (!hasAllFeildsHaveValues(criteria)) {
			log.info("search for --> all blank fields.");
			QueryBuilder matchAllQuery = QueryBuilders.matchAllQuery();
			NativeSearchQuery searchQuery = getSearchQuery(matchAllQuery);
			return searchQuery;
		}

		// for special searches
		String firstName = criteria.getFirstName();
		String lastName = criteria.getLastName();
		String orgName = criteria.getOrganisationName();

		Map<String, String> fnValues = new HashMap<String, String>();
		if (StringUtils.hasText(firstName) && StringUtils.trimAllWhitespace(firstName).length() > 0) {
			fnValues.put("firstName", firstName);
		}
		if (StringUtils.hasText(lastName) && StringUtils.trimAllWhitespace(lastName).length() > 0) {
			fnValues.put("lastName", lastName);
		}
		if (StringUtils.hasText(orgName) && StringUtils.trimAllWhitespace(orgName).length() > 0) {
			fnValues.put("orgName", orgName);
		}

		boolean wildFNSearch = hasWildChar(criteria.getFirstName());
		boolean wildLNSearch = hasWildChar(criteria.getLastName());
		boolean wildONSearch = hasWildChar(criteria.getOrganisationName());

		boolean isWildCharSearch = false;
		if (wildFNSearch || wildLNSearch || wildONSearch) {
			log.info(" initiated with wild *");
			isWildCharSearch = true;
		}

		log.info("count value is {}", fnValues.size());

		QueryBuilder queryStr1 = null;
		QueryBuilder queryStr2 = null;
		QueryBuilder queryStrNoField = null;

		if (fnValues.get("firstName") != null) {
			queryStr1 = wildFNSearch ? QueryBuilders.queryStringQuery(firstName).defaultField(field_firstName)
					.defaultOperator(Operator.AND) : QueryBuilders.matchQuery(field_firstName, firstName);
			/*
			// know and drop the unmentioned f
			if (fnValues.size() == 1) {
				queryStr1 = wildFNSearch ? QueryBuilders.queryStringQuery(firstName).defaultField(field_firstName)
						.defaultOperator(Operator.AND) : QueryBuilders.matchQuery(field_firstName, firstName);
			} else if (fnValues.size() == 2) {
				queryStr1 = wildFNSearch ? QueryBuilders.queryStringQuery(firstName).defaultField(field_firstName)
						.defaultOperator(Operator.AND) : QueryBuilders.matchQuery(field_firstName, firstName);
			} else if (fnValues.size() == 3) {
				queryStr1 = wildFNSearch ? QueryBuilders.queryStringQuery(firstName).defaultField(field_firstName)
						.defaultOperator(Operator.AND) : QueryBuilders.matchQuery(field_firstName, firstName);
			}*/
		}
		if (fnValues.get("lastName") != null) {
			queryStr2 = wildLNSearch ? QueryBuilders.queryStringQuery(lastName).defaultField(field_lastName)
					.defaultOperator(Operator.AND) : QueryBuilders.matchQuery(field_lastName, lastName);
			
			/*
			if (fnValues.size() == 1) {
				queryStr2 = wildLNSearch ? QueryBuilders.queryStringQuery(lastName).defaultField(field_lastName)
						.defaultOperator(Operator.AND) : QueryBuilders.matchQuery(field_lastName, lastName);
			}
			if (fnValues.size() == 2) {
				queryStr2 = wildLNSearch ? QueryBuilders.queryStringQuery(lastName).defaultField(field_lastName)
						.defaultOperator(Operator.AND) : QueryBuilders.matchQuery(field_lastName, lastName);
			}
			if (fnValues.size() == 3) {
				queryStr2 = wildLNSearch ? QueryBuilders.queryStringQuery(lastName).defaultField(field_lastName)
						.defaultOperator(Operator.AND) : QueryBuilders.matchQuery(field_lastName, lastName);
			}*/
			
		}
		if (fnValues.get("orgName") != null) {
			queryStrNoField = wildONSearch ? QueryBuilders.queryStringQuery(orgName).defaultOperator(Operator.AND)
					: QueryBuilders.matchQuery(field_orgName, orgName);
			/*
			if (fnValues.size() == 1) {
				queryStrNoField = wildONSearch ? QueryBuilders.queryStringQuery(orgName).defaultOperator(Operator.AND)
						: QueryBuilders.matchQuery(field_orgName, orgName);
			}
			if (fnValues.size() == 2) {
				queryStrNoField = wildONSearch ? QueryBuilders.queryStringQuery(orgName).defaultOperator(Operator.AND)
						: QueryBuilders.matchQuery(field_orgName, orgName);
			}
			if (fnValues.size() == 3) {
				queryStrNoField = wildONSearch ? QueryBuilders.queryStringQuery(orgName).defaultOperator(Operator.AND)
						: QueryBuilders.matchQuery(field_orgName, orgName);
			}*/
		}

		/*
		 * 
		 * final QueryBuilder queryStr1 = wildFNSearch ?
		 * QueryBuilders.queryStringQuery(firstName).defaultField(field_firstName).
		 * defaultOperator(Operator.AND) : QueryBuilders.matchQuery(field_firstName,
		 * firstName);
		 * 
		 * final QueryBuilder queryStr2 = wildLNSearch ?
		 * QueryBuilders.queryStringQuery(lastName).defaultField(field_lastName).
		 * defaultOperator(Operator.AND) : QueryBuilders.matchQuery(field_lastName,
		 * lastName);
		 * 
		 * final QueryBuilder queryStrNoField = wildONSearch ?
		 * QueryBuilders.queryStringQuery(orgName).defaultOperator(Operator.AND) :
		 * QueryBuilders.matchQuery(field_orgName, orgName);
		 */

		final List<QueryBuilder> queryBuilderList = new ArrayList<>();

		if (queryStr1 != null)
			queryBuilderList.add(queryStr1);
		if (queryStr2 != null)
			queryBuilderList.add(queryStr2);
		if (queryStrNoField != null)
			queryBuilderList.add(queryStrNoField);

		BoolQueryBuilder boolQueryBuilder2 = QueryBuilders.boolQuery();
		boolQueryBuilder2.must().addAll(queryBuilderList);

		BoolQueryBuilder boolQueryBuilder1 = QueryBuilders.boolQuery();
		boolQueryBuilder1.must(boolQueryBuilder2);

		NativeSearchQuery searchQuery = new NativeSearchQueryBuilder().withQuery(boolQueryBuilder1).build();

		log.info("complex : search query -> \n { \"query\": \n {}  }", searchQuery.getQuery());
		return searchQuery;
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

//		return (StringUtils.hasText(firstName) && StringUtils.hasText(lastName) && StringUtils.hasText(orgName));
	}

	private SearchHits<Contact> searchInIndex(NativeSearchQuery searchQuery) {
		SearchHits<Contact> esContactHits = elasticsearchRestTemplate.search(searchQuery, Contact.class,
				IndexCoordinates.of("contactstore"));
		return esContactHits;
	}

	private static boolean hasWildChar(String fieldValue) {
//		return true;
		return fieldValue.contains("*");
	}

}
