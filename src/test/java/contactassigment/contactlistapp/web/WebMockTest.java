package contactassigment.contactlistapp.web;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.Before;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.flyway.FlywayAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

//@WebMvcTest(HomeController.class)
// @SpringBootTest
//@ExtendWith(SpringExtension.class)
// @ActiveProfiles("test")
@EnableAutoConfiguration(exclude = {FlywayAutoConfiguration.class,
		DataSourceAutoConfiguration.class, HibernateJpaAutoConfiguration.class})
public class WebMockTest {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	WebApplicationContext webAppContext;

	private HomeController homeController;

	@Before
	private void setup() {
		mockMvc = MockMvcBuilders.standaloneSetup(homeController)
				.build();
	}

	// @MockBean
	// private GreetingService service;

//	@Test
	public void getMappingFromHomeController() throws Exception {
		// when(service.greet()).thenReturn("Hello, Mock");
		this.mockMvc.perform(get("/"))
				.andDo(print())
				.andExpect(status().isOk())
				.andExpect(content().string(containsString("Hello, Mock")));
	}
}