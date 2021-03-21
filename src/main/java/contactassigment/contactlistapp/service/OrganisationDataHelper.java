package contactassigment.contactlistapp.service;

import org.springframework.util.StringUtils;

import contactassigment.contactlistapp.domain.Organisation;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class OrganisationDataHelper {
	
	private final static String ABN_FORMAT = "%1$s%2$s %3$1s%4$s%5$s %6$1s%7$s%8$s %9$1s%10$s%11$s";
	private final static String REGEX_DIGIT = "\\d*?";
	
	@Getter(AccessLevel.NONE)
	@Setter(AccessLevel.NONE)
	private String name;
	@Getter(AccessLevel.NONE)
	@Setter(AccessLevel.NONE)
	private String abn;
	@Getter(AccessLevel.NONE)
	@Setter(AccessLevel.NONE)
	Organisation organisation;

	public OrganisationDataHelper(Organisation organisation) {
		this.name = organisation.getName();
		this.abn = organisation.getAbn();
	}

	public String getOrgNameWithABN() {
		return String.format("%1$s%2$s(%3$s)", name," ",formatABN(abn));
	}

	public static String formatABN(String abn) {
		if (abn == null || !StringUtils.hasText(abn)) {
			return null;
		}
		String[] split = abn.split(REGEX_DIGIT);
		return String.format(ABN_FORMAT, split[0], split[1], split[2], split[3], split[4], split[5], split[6], split[7],
				split[8], split[9], split[10]);
	}
}