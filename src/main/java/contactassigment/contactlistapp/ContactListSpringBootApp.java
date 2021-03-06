package contactassigment.contactlistapp;

import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.elasticsearch.client.ClientConfiguration;
import org.springframework.data.elasticsearch.client.RestClients;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;
import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import lombok.extern.slf4j.Slf4j;

@SpringBootApplication
@EnableJpaRepositories(basePackages = "contactassigment.contactlistapp.domain.jparepository", considerNestedRepositories = false)
@EnableElasticsearchRepositories(basePackages = "contactassigment.contactlistapp.domain.esrepository", considerNestedRepositories = false)
@Slf4j
public class ContactListSpringBootApp {

	public static void main(String[] args) {
		log.debug("Start Contactlist App");
		SpringApplication.run(ContactListSpringBootApp.class, args);
	}

	@Value("${elasticsearch.server}")
	private String server;
	@Value("${elasticsearch.server.port}")
	private String port;
	
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

}