package contactassigment.contactlistapp.web.validators;

import java.util.regex.Pattern;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import contactassigment.contactlistapp.dto.ContactDTO;

@Component
public class ContactDTOValidator implements Validator {

	/**
	 * This Validator validates *just* Person instances
	 */
	public boolean supports(Class<?> clazz) {
		return ContactDTO.class.equals(clazz);
	}

	public void validate(Object obj, Errors e) {
		ValidationUtils.rejectIfEmpty(e, "firstName", "name.empty");
		ValidationUtils.rejectIfEmpty(e, "lastName", "name.empty");

		ContactDTO contact = (ContactDTO) obj;
		validateLength(e, contact);
		validateType(e, contact);
	}

	private void validateLength(Errors e, ContactDTO contact) {
		if (contact.getFirstName()
				.length() > 30) {
			e.rejectValue("firstName", "firstName.too.long");
		}

		if (contact.getLastName()
				.length() > 30) {
			e.rejectValue("lastName", "lastName.too.long");
		}
	}
	private void validateType(Errors e, ContactDTO contact) {
		if (!hasAllWords(contact.getFirstName())) {
			e.rejectValue("firstName", "firstName.non.word");
		}

		if (!hasAllWords(contact.getLastName())) {
			e.rejectValue("lastName", "lastName.non.word");
		}
	}

	private boolean hasAllWords(String string) {
		return Pattern.matches("\\D*",string);
	}
}
