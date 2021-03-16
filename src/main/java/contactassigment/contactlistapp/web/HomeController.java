package contactassigment.contactlistapp.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class HomeController {
	
	@Autowired
	Environment env;

	@RequestMapping("/")
	public String home(Model model) {
		model.addAttribute("username", env.getProperty("user.name"));
		return "/home";
	}

}
