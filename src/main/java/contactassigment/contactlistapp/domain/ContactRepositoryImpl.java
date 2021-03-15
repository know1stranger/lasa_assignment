package contactassigment.contactlistapp.domain;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

public class ContactRepositoryImpl implements ContactRepositoryCustom {
	Logger logger = LoggerFactory.getLogger(ContactRepositoryImpl.class);

	@PersistenceContext
	EntityManager em;

	@Override
	public List<Contact> searchByNamesFetchOrganisation(String contactName, String organisationName) {
		StringBuilder sbuilder = new StringBuilder("SELECT c FROM Contact c LEFT JOIN FETCH c.organisation o ");
		if (StringUtils.hasText(contactName)) {
			sbuilder.append("AND c.firstName like :contactFNamePattern ");
			sbuilder.append("AND c.lastName like :contactLNamePattern ");
		}
		if (StringUtils.hasText(organisationName)) {
			sbuilder.append("AND o.name like :organisationNamePattern ");
		}

		String queryHQL = sbuilder.toString().replaceFirst("AND", "WHERE").trim();
		logger.debug("Query HQL: " + queryHQL);
		javax.persistence.Query q = em.createQuery(queryHQL);

		if (StringUtils.hasText(contactName)) {

			String[] fullName = contactName.split(" ");
			
			if (fullName.length > 1) {
				q.setParameter("contactFNamePattern", fullName[0]);
				q.setParameter("contactLNamePattern", fullName[1]);
			}
			
		}
		if (StringUtils.hasText(organisationName)) {
			q.setParameter("organisationNamePattern", organisationName);
		}

		return q.getResultList();
	}

}
