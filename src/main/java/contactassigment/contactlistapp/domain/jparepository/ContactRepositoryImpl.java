package contactassigment.contactlistapp.domain.jparepository;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.util.StringUtils;

import contactassigment.contactlistapp.domain.Contact;
import contactassigment.contactlistapp.dto.ContactSearchCriteriaDTO;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@AllArgsConstructor
public class ContactRepositoryImpl implements ContactRepositoryCustom {

	@PersistenceContext
	final private EntityManager em;

	@AllArgsConstructor
	static enum QUERY_PARAM {
		CONTACT_L_NAME("contactLNamePattern"), CONTACT_F_NAME("contactFNamePattern"),
		ORG_NAME("organisationNamePattern"), ORG_NAME_PATTERN(":organisationNamePattern"),
		CONTACT_L_NAME_PATTERN(":contactLNamePattern"), CONTACT_F_NAME_PATTERN(":contactFNamePattern");

		private String paramTxt;

		@Override
		public String toString() {
			return paramTxt;
		}
	}

	@Override
	public List<Contact> searchByNamesFetchOrganisation(ContactSearchCriteriaDTO criteria) {
		log.info("Start --> Search from DB......" + criteria);
		StringBuilder sbuilder = new StringBuilder("SELECT c FROM Contact c LEFT JOIN FETCH c.organisation o ");

		String firstName = criteria.getFirstName().trim();
		if (StringUtils.hasText(firstName)) {
			sbuilder.append(" AND c.firstName like" + QUERY_PARAM.CONTACT_F_NAME_PATTERN);
		}

		String lastName = criteria.getLastName().trim();
		if (StringUtils.hasText(lastName)) {
			sbuilder.append(" AND c.lastName like" + QUERY_PARAM.CONTACT_L_NAME_PATTERN);
		}

		String organisationName = criteria.getOrganisationName();
		if (StringUtils.hasText(organisationName)) {
			sbuilder.append(" AND o.name like" + QUERY_PARAM.ORG_NAME_PATTERN);
		}

		String queryHQL = sbuilder.toString().replaceFirst("AND", "WHERE");
		log.info("Query HQL: " + queryHQL);

		javax.persistence.Query q = em.createQuery(queryHQL);

		if (StringUtils.hasText(firstName)) {
			q.setParameter(QUERY_PARAM.CONTACT_F_NAME.name(), firstName);
		}
		if (StringUtils.hasText(lastName)) {
			q.setParameter(QUERY_PARAM.CONTACT_L_NAME.name(), lastName);
		}

		if (StringUtils.hasText(organisationName)) {
			q.setParameter(QUERY_PARAM.ORG_NAME.name(), organisationName);
		}
		log.info("End --> Search from DB......");
		return q.getResultList();
	}
}
