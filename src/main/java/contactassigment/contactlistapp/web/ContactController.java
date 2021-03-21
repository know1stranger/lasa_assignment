package contactassigment.contactlistapp.web;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import contactassigment.contactlistapp.dto.ContactDTO;
import contactassigment.contactlistapp.dto.ContactSearchCriteriaDTO;
import contactassigment.contactlistapp.dto.OrganisationDTO;
import contactassigment.contactlistapp.service.ContactService;
import contactassigment.contactlistapp.service.OrganisationService;
import contactassigment.contactlistapp.web.validators.ContactDTOValidator;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Controller
@AllArgsConstructor
@Slf4j
public class ContactController{

	private static final String SUCCESSFUL_MSG = "Contact has been updated successfully!";
	//services
	private final ContactService contactService;
	private final OrganisationService organisationService;
	private final ContactDTOValidator contactDTOValidator;
	//pages
	private static final String CONTACT_LIST_PAGE = "/contact/list";
	private static final String CONTACT_VIEW_PAGE = "/contact/view";
	private static final String CONTACT_EDIT_PAGE = "/contact/edit";
	private static final String REDIRECT_CONTACTS_ID = "redirect:/contacts/";
	
	
	@RequestMapping(value = "/contacts", method = RequestMethod.GET)
	public String listContacts(@ModelAttribute("searchCriteria") ContactSearchCriteriaDTO contactSearchCriteria,
			Model model) {
		
		List<ContactDTO> contacts = contactService.listByCriteriaFetchOrganisation(contactSearchCriteria);

		model.addAttribute("contacts", contacts);
		model.addAttribute("searchCriteria", contactSearchCriteria);
		
		if(!contacts.isEmpty() && contacts.size() == 1) {
			model.addAttribute("contact", contacts.get(0));
			return CONTACT_VIEW_PAGE;
		}
		
		log.info("inital search completed");
		return CONTACT_LIST_PAGE;
	}

	@RequestMapping(value = "/contacts", method = RequestMethod.POST)
	public String updateContact(@ModelAttribute("contact") ContactDTO contactDTO, BindingResult result, Model model,
			final RedirectAttributes redirectAttributes) {
		contactDTOValidator.validate(contactDTO, result);
		if (result.hasErrors()) {
			model.addAttribute("contact", contactDTO);
			model.addAttribute("organisations", organisationService.listAll());
			return CONTACT_EDIT_PAGE;
		} else {
			contactService.updateByDTO(contactDTO);
			redirectAttributes.addFlashAttribute("css", "success");
			redirectAttributes.addFlashAttribute("msg",SUCCESSFUL_MSG);
			return REDIRECT_CONTACTS_ID + contactDTO.getId();
		}
	}

	@RequestMapping(value = "/contacts/{id}", method = RequestMethod.GET)
	public String view(@PathVariable Integer id, Model model) {
		ContactDTO contact = contactService.findByIdFetchOrganisation(id);

		model.addAttribute("contact", contact);

		return CONTACT_VIEW_PAGE;
	}

	@RequestMapping(value = "/contacts/{id}/edit", method = RequestMethod.GET)
	public String edit(@PathVariable Integer id, Model model) {
		ContactDTO contact = contactService.findByIdFetchOrganisation(id);
		List<OrganisationDTO> organisations = organisationService.listAll();

		model.addAttribute("contact", contact);
		model.addAttribute("organisations", organisations);

		return CONTACT_EDIT_PAGE;
	}

}
