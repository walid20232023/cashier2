package org.openmrs.module.mylabtest;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "test_type")
public class MyLabTestType {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Integer id;

	@Column(name = "name", nullable = false)
	private String name;

	@Column(name = "uuid", unique = true, nullable = false, length = 38, updatable = false)
	private String uuid = UUID.randomUUID().toString();

	@Enumerated(EnumType.STRING)
	@Column(name = "test_group", length = 50)
	private GroupEnum groupEnum;

	@ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	@JoinTable(name = "test_type_test_type_attr", joinColumns = @JoinColumn(name = "test_type_id", referencedColumnName = "id"), inverseJoinColumns = @JoinColumn(name = "test_type_attr_id", referencedColumnName = "attr_id"))
	private Set<MyLabTestTypeAttr> attributes = new HashSet<MyLabTestTypeAttr>();

	// Getters and Setters

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getUuid() {
		return uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

	public GroupEnum getGroupEnum() {
		return groupEnum;
	}

	public void setGroupEnum(GroupEnum groupEnum) {
		this.groupEnum = groupEnum;
	}

	public Set<MyLabTestTypeAttr> getAttributes() {
		return attributes;
	}

	public void setAttributes(Set<MyLabTestTypeAttr> attributes) {
		this.attributes = attributes;
	}

	public void addAttribute(MyLabTestTypeAttr attribute) {
		if (this.attributes == null) {
			this.attributes = new HashSet<MyLabTestTypeAttr>();
		}
		this.attributes.add(attribute);
	}

	@Override
	public String toString() {
		return "MyLabTestType{" + "id=" + id + ", name='" + name + '\'' + ", uuid='" + uuid + '\'' + ", groupEnum="
		        + groupEnum + ", attributes=" + attributes + '}';
	}
}
