package org.openmrs.module.mylabtest;

import org.hibernate.search.annotations.Field;
import org.openmrs.Concept;

import javax.persistence.*;
import java.util.UUID;

@Entity(name = "mylabtest.MyLabTestType")
@Table(name = "test_type_attr")
public class MyLabTestTypeAttr {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "attr_id")
	private Integer id;

	@Field
	@Column(name = "name")
	private String name;

	@ManyToOne
	@JoinColumn(name = "concept_id")
	private Concept concept;

	@Enumerated(EnumType.STRING)
	@Column(name = "datatype_config", length = 50)
	private TestValueType datatypeConfig;

	@Column(name = "uuid", unique = true, nullable = false, length = 38, updatable = false)
	private String uuid = UUID.randomUUID().toString();

	public MyLabTestTypeAttr() {
	}

	public Integer getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public Concept getConcept() {
		return concept;
	}

	public TestValueType getDatatypeConfig() {
		return datatypeConfig;
	}

	public String getUuid() {
		return uuid;
	}

	public void setLabTestTypeId(Integer labTestTypeId) {
		this.id = labTestTypeId;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setConcept(Concept concept) {
		this.concept = concept;
	}

	public void setDatatypeConfig(TestValueType datatypeConfig) {
		this.datatypeConfig = datatypeConfig;
	}

	@Override
	public String toString() {
		return "MyLabTestType{" + "labTestTypeId=" + id + ", name='" + name + '\'' + ", concept=" + concept
		        + ", datatypeConfig=" + datatypeConfig + ", uuid='" + uuid + '\'' + '}';
	}
}
