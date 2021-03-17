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

	@Override
	public List<Contact> searchByNamesFetchOrganisation(ContactSearchCriteriaDTO criteria) {
		StringBuilder sbuilder = new StringBuilder("SELECT c FROM Contact c LEFT JOIN FETCH c.organisation o ");

		 String firstName = criteria.getFirstName().trim();
		if (StringUtils.hasText(firstName)) {
			sbuilder.append("AND c.firstName like :contactFNamePattern ");
		}
		
		String lastName = criteria.getLastName().trim();
		if (StringUtils.hasText(lastName)) {
			sbuilder.append("AND c.lastName like :contactLNamePattern ");
		}

		String organisationName = criteria.getOrganisationName();
		if (StringUtils.hasText(organisationName)) {
			sbuilder.append("AND o.name like :organisationNamePattern ");
		}

		String queryHQL = sbuilder.toString().replaceFirst("AND", "WHERE").trim();
		log.debug("Query HQL: " + queryHQL);

		javax.persistence.Query q = em.createQuery(queryHQL);

		if (StringUtils.hasText(firstName)) {
			q.setParameter("contactFNamePattern", firstName);
		}
		if (StringUtils.hasText(lastName)) {
			q.setParameter("contactLNamePattern", lastName);
		}

		if (StringUtils.hasText(organisationName)) {
			q.setParameter("organisationNamePattern", organisationName);
		}

		return q.getResultList();
	}
}
