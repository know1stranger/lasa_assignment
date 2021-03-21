package contactassigment.contactlistapp.domain;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import org.springframework.data.elasticsearch.annotations.Document;

import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Document(indexName = "contactstore", createIndex = true)
public class Organisation {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Access(AccessType.PROPERTY)
	private Integer id;

	@Column(nullable = false, unique = true, length = 11)
	private String abn;

	@Column(nullable = false)
	private String name;

}
