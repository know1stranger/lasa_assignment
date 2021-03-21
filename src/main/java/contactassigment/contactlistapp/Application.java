package contactassigment.contactlistapp;

import java.util.List;

import javax.annotation.PostConstruct;

import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.web.servlet.error.ErrorMvcAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.data.elasticsearch.client.ClientConfiguration;
import org.springframework.data.elasticsearch.client.RestClients;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;
import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import contactassigment.contactlistapp.domain.Contact;
import contactassigment.contactlistapp.domain.esrepository.ContactESRepository;
import contactassigment.contactlistapp.domain.jparepository.ContactRepository;
import contactassigment.contactlistapp.dto.ContactSearchCriteriaDTO;
import lombok.extern.slf4j.Slf4j;

@SpringBootApplication
@EnableJpaRepositories(basePackages = "contactassigment.contactlistapp.domain.jparepository", considerNestedRepositories = false)
@EnableElasticsearchRepositories(basePackages = "contactassigment.contactlistapp.domain.esrepository", considerNestedRepositories = false)
@Slf4j
//@EnableAutoConfiguration(exclude = {ErrorMvcAutoConfiguration.class})
public class Application {

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}

	@Value("${elasticsearch.server}")
	private String server;
	@Value("${elasticsearch.server.port}")
	private String port;
	@Autowired
	private ContactRepository contactRepo;
	@Autowired
	private ContactESRepository contactESRepo;

	@Bean
	public RestHighLevelClient client() {
		ClientConfiguration clientConfiguration = ClientConfiguration.builder()
				.connectedTo(server.concat(":" + port))
				.build();
		return RestClients.create(clientConfiguration)
				.rest();
	}

	@Bean
	public ElasticsearchOperations elasticsearchTemplate() {
		return new ElasticsearchRestTemplate(client());
	}

	@PostConstruct
	void loadToESIndex() {
		log.info("Loading to ES index to support elastic searching...!");
		List<Contact> resultList = contactRepo.searchByNamesFetchOrganisation(
				ContactSearchCriteriaDTO.builder()
						.build());
		contactESRepo.saveAll(resultList);
	}
}