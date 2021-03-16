package contactassigment.contactlistapp.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import contactassigment.contactlistapp.domain.Organisation;
import contactassigment.contactlistapp.domain.OrganisationRepository;
import contactassigment.contactlistapp.dto.OrganisationDTO;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class OrganisationServiceImpl implements OrganisationService
{

	@Autowired
  protected final OrganisationRepository repo;


  @Override
  public List<OrganisationDTO> listAll()
  {
    List<Organisation> resultList = repo.findAll();
    return OrganisationDTO.createListBy(resultList);
  }
}
