package contactassigment.contactlistapp.domain.jparepository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import contactassigment.contactlistapp.domain.Organisation;

@Repository
public interface OrganisationRepository extends CrudRepository<Organisation, Integer>
{
}
