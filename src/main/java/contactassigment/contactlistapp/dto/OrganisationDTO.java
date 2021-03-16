package contactassigment.contactlistapp.dto;

import java.util.ArrayList;
import java.util.List;

import contactassigment.contactlistapp.domain.Organisation;
import contactassigment.contactlistapp.service.OrganisationDataHelper;
import lombok.AccessLevel;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Data
@NoArgsConstructor
public class OrganisationDTO {

	private Integer id;
	private String name;
	private String abn;
	
	@Setter(value = AccessLevel.NONE)
	@Getter(value = AccessLevel.NONE)
	private OrganisationDataHelper organisationDataHelper;

	public OrganisationDTO(Organisation organisation) {
		setId(organisation.getId());
		setName(organisation.getName());
		setAbn(organisation.getAbn());
		organisationDataHelper = new OrganisationDataHelper(organisation);
	}

	public static OrganisationDTO createBy(Organisation organisation) {
		return new OrganisationDTO(organisation);
	}

	public static List<OrganisationDTO> createListBy(List<Organisation> organisations) {
		List<OrganisationDTO> organisationDTOs = new ArrayList<OrganisationDTO>(organisations.size());
		for (Organisation o : organisations) {
			organisationDTOs.add(OrganisationDTO.createBy(o));
		}
		return organisationDTOs;
	}

	/*
	 * Method to return orgName with abn. Better to have it here in one place than
	 * in views for giving desired format across.
	 */
	public String getNameWithABN() {
		return organisationDataHelper.getOrgNameWithABN();
	}

}
