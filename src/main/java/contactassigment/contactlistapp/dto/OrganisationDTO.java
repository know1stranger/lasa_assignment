package contactassigment.contactlistapp.dto;

import java.util.ArrayList;
import java.util.List;

import org.springframework.util.StringUtils;

import contactassigment.contactlistapp.domain.Organisation;

public class OrganisationDTO {

	private Integer id;
	private String name;
	private String abn;
	
	private static final String ABN_FORMAT = " %1$s%2$s %3$1s%4$s%5$s %6$1s%7$s%8$s %9$1s%10$s%11$s";

	private static final String REGEX_DIGIT = "\\d*?";

	public OrganisationDTO() {
	}

	public OrganisationDTO(Organisation organisation) {
		setId(organisation.getId());
		setName(organisation.getName());
		setAbn(organisation.getAbn());
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getAbn() {
		return abn;
	}

	public void setAbn(String abn) {
		this.abn = abn;
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
		return String.format("%s (%s)", name, formatABN(abn));
	}

	public static String formatABN(String abn) {
		if (abn == null || !StringUtils.hasText(abn)) {
			return null;
		}
		String[] split = abn.split(REGEX_DIGIT);
		return String.format(ABN_FORMAT, split[0], split[1], split[2], split[3], split[4], split[5], split[6], split[7],split[8], split[9], split[10]);
	}
}
