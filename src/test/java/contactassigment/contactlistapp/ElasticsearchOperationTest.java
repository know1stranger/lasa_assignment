package contactassigment.contactlistapp;

import static org.assertj.core.api.Assertions.fail;

import java.util.ArrayList;
import java.util.List;

import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.Operator;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.mapping.IndexCoordinates;
import org.springframework.data.elasticsearch.core.query.NativeSearchQuery;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;

import contactassigment.contactlistapp.domain.Contact;
import lombok.extern.slf4j.Slf4j;

/**
 * This class test the existing index. //TODO Need to update it be
 * self-sufficient for quick query testing while connecting to to ES Server.
 * 
 * @author ckarimajji
 *
 */
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@ActiveProfiles(profiles = "test")
@TestPropertySource(properties = { "spring.data.jpa.repositories.enabled=flase" })
//@EnableAutoConfiguration(exclude = { FlywayAutoConfiguration.class, DataSourceAutoConfiguration.class, HibernateJpaAutoConfiguration.class })
@Slf4j
public class ElasticsearchOperationTest {

	static String field_firstName = "firstName";
	static String field_lastName = "lastName";
	static String field_orgName = "name";

	@Autowired
	private ElasticsearchOperations elasticsearchOperations;

//	@Test
	public void testToGetAllContactsGivenFirstname() throws Exception {
		NativeSearchQuery searchQuery = new NativeSearchQueryBuilder()
				.withQuery(QueryBuilders.matchQuery(field_firstName, "Sophie")).build();
		log.info("search query ->\n  {}", searchQuery.getQuery());
		SearchHits<Contact> esContactHits = forSearchIndex(searchQuery);
		assertSearchHits(esContactHits);
	}

//	@Test
	public void testToGetAllContactsGivenLastnameWithWIldChar() throws Exception {
		// https://www.elastic.co/guide/en/elasticsearch/reference/current/query-dsl-regexp-query.html
		NativeSearchQuery searchQuery = new NativeSearchQueryBuilder()
				.withQuery(QueryBuilders.regexpQuery(field_lastName, "k.*")).build();
		log.info("search query ->\n {}", searchQuery.getQuery());
		SearchHits<Contact> esContactHits = forSearchIndex(searchQuery);
		assertSearchHits(esContactHits);
	}

	/*
	 * using regex
	 * 
	 * @Test public void testToGetAllContactsGivenFirstnameWithWIldChar_WithRegex()
	 * throws Exception {
	 * //https://www.elastic.co/guide/en/elasticsearch/reference/current/query-dsl-
	 * regexp-query.html NativeSearchQuery searchQuery = new
	 * NativeSearchQueryBuilder()
	 * .withQuery(QueryBuilders.regexpQuery(field_firsttName, "s.*i")).build();
	 * log.info("search query -> {}", searchQuery.getQuery()); SearchHits<Contact>
	 * esContactHits = forSearchIndex(searchQuery); assertSearchHits(esContactHits);
	 * }
	 */

	@Test
	public void testToGetAllContactsGivenlastNameWithWIldChar_useQueryString() throws Exception {
		// https://www.elastic.co/guide/en/elasticsearch/reference/current/query-dsl-regexp-query.html
		QueryBuilder queryStr = QueryBuilders.queryStringQuery("*k*").defaultField(field_lastName)
				.defaultOperator(Operator.AND);
		NativeSearchQuery searchQuery = new NativeSearchQueryBuilder().withQuery(queryStr).build();
		log.info("search query -> {}", searchQuery.getQuery());
		SearchHits<Contact> esContactHits = forSearchIndex(searchQuery);
		assertSearchHits(esContactHits);
	}

	@Test
	public void testgetContactWithBoolQueryWithFirstnameMatchAndQueryStrForOtherInputs() {
		QueryBuilder matchQueryStr = QueryBuilders.matchQuery(field_firstName, "Sophie");
		QueryBuilder queryStr = QueryBuilders.queryStringQuery("*k*").defaultField(field_lastName)
				.defaultOperator(Operator.AND);
		QueryBuilder queryStrNoField = QueryBuilders.queryStringQuery("*Australian").defaultOperator(Operator.AND);

		List<QueryBuilder> queryBuilderList = new ArrayList<>();
		queryBuilderList.add(matchQueryStr);
		queryBuilderList.add(queryStr);
		queryBuilderList.add(queryStrNoField);

		BoolQueryBuilder boolQueryBuilder2 = QueryBuilders.boolQuery();
		boolQueryBuilder2.must().addAll(queryBuilderList);

		BoolQueryBuilder boolQueryBuilder1 = QueryBuilders.boolQuery();
		boolQueryBuilder1.must(boolQueryBuilder2);

		NativeSearchQuery searchQuery = new NativeSearchQueryBuilder().withQuery(boolQueryBuilder1).build();
		log.info("search query -> {}", searchQuery.getQuery());
		SearchHits<Contact> esContactHits = forSearchIndex(searchQuery);
		assertSearchHits(esContactHits);
	}

	@Test
	public void testBoolQueryWithAllFieldsWithWildChar() {
		QueryBuilder queryStr1 = QueryBuilders.queryStringQuery("*phie*").defaultField(field_firstName)
				.defaultOperator(Operator.AND);
		QueryBuilder queryStr2 = QueryBuilders.queryStringQuery("*k*").defaultField(field_lastName)
				.defaultOperator(Operator.AND);
		QueryBuilder queryStrNoField = QueryBuilders.queryStringQuery("*Australian").defaultOperator(Operator.AND);

		List<QueryBuilder> queryBuilderList = new ArrayList<>();
		queryBuilderList.add(queryStr1);
		queryBuilderList.add(queryStr2);
		queryBuilderList.add(queryStrNoField);

		BoolQueryBuilder boolQueryBuilder2 = QueryBuilders.boolQuery();
		boolQueryBuilder2.must().addAll(queryBuilderList);

		BoolQueryBuilder boolQueryBuilder1 = QueryBuilders.boolQuery();
		boolQueryBuilder1.must(boolQueryBuilder2);

		NativeSearchQuery searchQuery = new NativeSearchQueryBuilder().withQuery(boolQueryBuilder1).build();
		log.info("search query -> {}", searchQuery.getQuery());
		SearchHits<Contact> esContactHits = forSearchIndex(searchQuery);
		assertSearchHits(esContactHits);
	}

	@Test
	public void testToGetAllContacts_useMatchAllQuery() throws Exception {
		// https://www.elastic.co/guide/en/elasticsearch/reference/current/query-dsl-regexp-query.html
		NativeSearchQuery searchQuery = new NativeSearchQueryBuilder().withQuery(QueryBuilders.matchAllQuery()).build();
		log.info("search query -> {}", searchQuery.getQuery());
		SearchHits<Contact> esContactHits = forSearchIndex(searchQuery);
		assertSearchHits(esContactHits);
	}

	/**
	 * Helper methods
	 * 
	 * @param esContactHits
	 */

	private void assertSearchHits(SearchHits<Contact> esContactHits) {
		if (esContactHits.hasSearchHits()) {
			log.info("Got search result hits count {}", esContactHits.getTotalHits());

			esContactHits.forEach(x -> {
				Contact content = x.getContent();
				log.info("-> FirstName {} and LastName {}", content.getFirstName(), content.getLastName());
			});

		} else {
			log.warn("No luck...");
			fail("No search result");
		}
	}

	private SearchHits<Contact> forSearchIndex(NativeSearchQuery searchQuery) {
		SearchHits<Contact> esContactHits = elasticsearchOperations.search(searchQuery, Contact.class,
				IndexCoordinates.of("contactstore"));
		return esContactHits;
	}

}