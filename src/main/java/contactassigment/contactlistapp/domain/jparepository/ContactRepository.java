package contactassigment.contactlistapp.domain.jparepository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import contactassigment.contactlistapp.domain.Contact;

@Repository
public interface ContactRepository extends CrudRepository<Contact, Integer>, ContactRepositoryCustom
{
  @Query("SELECT c FROM Contact c LEFT JOIN FETCH c.organisation WHERE c.id = ?1")
  Contact findByIdFetchOrganisation(Integer id);

  @Query("FROM Contact c LEFT JOIN FETCH c.organisation")
  List<Contact> findAllFetchOrganisation();
}
