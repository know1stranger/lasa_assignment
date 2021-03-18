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
	
	private static final String CONTACT_L_NAME_QPARAM = "contactLNamePattern";
	private static final String CONTACT_F_NAME_QPARAM = "contactFNamePattern";
	private static final  String ORG_NAME_QPARAM = "organisationNamePattern";
	
	private static final String ORG_NAME_PATTERN = " :organisationNamePattern";
	private static final String CONTACT_L_NAME_PATTERN = " :contactLNamePattern";
	private static final String CONTACT_F_NAME_PATTERN = " :contactFNamePattern";
	
	@Override
	public List<Contact> searchByNamesFetchOrganisation(ContactSearchCriteriaDTO criteria) {
		log.info("Start --> Search from DB......" + criteria);
		StringBuilder sbuilder = new StringBuilder("SELECT c FROM Contact c LEFT JOIN FETCH c.organisation o ");

		 String firstName = criteria.getFirstName().trim();
		if (StringUtils.hasText(firstName)) {
			sbuilder.append(" AND c.firstName like"+CONTACT_F_NAME_PATTERN);
		}
		
		String lastName = criteria.getLastName().trim();
		if (StringUtils.hasText(lastName)) {
			sbuilder.append(" AND c.lastName like"+CONTACT_L_NAME_PATTERN);
		}

		String organisationName = criteria.getOrganisationName();
		if (StringUtils.hasText(organisationName)) {
			sbuilder.append(" AND o.name like"+ORG_NAME_PATTERN);
		}

		String queryHQL = sbuilder.toString().replaceFirst("AND", "WHERE");
		log.info("Query HQL: " + queryHQL);

		javax.persistence.Query q = em.createQuery(queryHQL);

		if (StringUtils.hasText(firstName)) {
			q.setParameter(CONTACT_F_NAME_QPARAM, firstName);
		}
		if (StringUtils.hasText(lastName)) {
			q.setParameter(CONTACT_L_NAME_QPARAM, lastName);
		}

		if (StringUtils.hasText(organisationName)) {
			q.setParameter(ORG_NAME_QPARAM, organisationName);
		}
		log.info("End --> Search from DB......");
		return q.getResultList();
	}
}
