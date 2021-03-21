package contactassigment.contactlistapp.domain.jparepository;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.springframework.util.StringUtils;

import contactassigment.contactlistapp.domain.Contact;
import contactassigment.contactlistapp.dto.ContactSearchCriteriaDTO;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@AllArgsConstructor
public class ContactRepositoryImpl implements ContactRepositoryCustom {

	private static final String WILD_CHAR = "%WILD_CHAR%";
	@PersistenceContext
	final private EntityManager entityManager;

	@SuppressWarnings("unchecked")
	@Override
	public List<Contact> searchByNamesFetchOrganisation(
			ContactSearchCriteriaDTO criteria) {
		log.info("Start --> Search from DB......" + criteria);
		StringBuilder sbuilder = new StringBuilder(
				"SELECT c FROM Contact c LEFT JOIN FETCH c.organisation o ");

		String firstName = criteria.getFirstName()
				.trim();
		buildQueryByCriteria(sbuilder, firstName,
				QUERY_PARAM.CONTACT_F_NAME_PATTERN, " AND c.firstName like");

		String lastName = criteria.getLastName()
				.trim();
		buildQueryByCriteria(sbuilder, lastName,
				QUERY_PARAM.CONTACT_L_NAME_PATTERN, " AND c.lastName like");

		String orgName = criteria.getOrganisationName();
		buildQueryByCriteria(sbuilder, orgName, QUERY_PARAM.ORG_NAME_PATTERN,
				" AND o.name like");

		String queryHQL = sbuilder.toString()
				.replaceFirst("AND", "WHERE");

		log.info("Query HQL: " + queryHQL);

		Query q = setQueryParams(queryHQL, firstName, lastName, orgName);

		log.info("End --> Search from DB......");
		return q.getResultList();
	}

	private void buildQueryByCriteria(StringBuilder sbuilder, String param,
			QUERY_PARAM paramName, String clauseStr) {
		if (StringUtils.hasText(param)) {
			sbuilder.append(clauseStr + paramName.toString());
		}
	}

	private Query setQueryParams(String queryHQL, String firstName,
			String lastName, String orgName) {
		javax.persistence.Query q = entityManager.createQuery(queryHQL);

		if (StringUtils.hasText(firstName)) {
			q.setParameter(QUERY_PARAM.CONTACT_F_NAME.toString(),
					buildToSupportWildSearch(firstName));
		}
		if (StringUtils.hasText(lastName)) {
			q.setParameter(QUERY_PARAM.CONTACT_L_NAME.toString(),
					buildToSupportWildSearch(firstName));
		}

		if (StringUtils.hasText(orgName)) {
			q.setParameter(QUERY_PARAM.ORG_NAME.toString(),
					buildToSupportWildSearch(firstName));
		}
		return q;
	}

	private static String buildToSupportWildSearch(String paramValue) {
		return WILD_CHAR.replace("WILD_CHAR", paramValue);
	}

}
