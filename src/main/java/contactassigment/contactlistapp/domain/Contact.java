package contactassigment.contactlistapp.domain;

import java.time.LocalDateTime;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import org.hibernate.annotations.CreationTimestamp;
import org.springframework.data.elasticsearch.annotations.DateFormat;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import lombok.AccessLevel;
import lombok.Data;
import lombok.Setter;

@Entity
@Document(indexName = "contactstore", createIndex = true)
@Data
public class Contact {
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Access(AccessType.PROPERTY)
	private Integer id;

	@Column(nullable = false, length = 30)
	private String firstName;

	@Column(nullable = false, length = 30)
	private String lastName;

	@Column
	@CreationTimestamp
	@Setter(value = AccessLevel.PRIVATE)
	@Field(type = FieldType.Date, format = DateFormat.basic_date_time_no_millis)
	private LocalDateTime created;

	@ManyToOne(fetch = FetchType.LAZY, optional = true, cascade = CascadeType.MERGE)
	private Organisation organisation;
}
