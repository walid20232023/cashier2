package org.openmrs.module.mycashier;

import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.annotate.JsonManagedReference;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "client")
public class Client {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	
	@Column(name = "name", nullable = false)
	private String name;
	
	@Column(name = "firstnames")
	private String firstnames;
	
	@Column(name = "birth_date")
	private Date birthDate;
	
	@Column(name = "age")
	private Integer age;
	
	@Column(name = "sex")
	private String sex;
	
	@Column(name = "address")
	private String address;
	
	@Column(name = "telephon")
	private String telephone;
	
	@Column(name = "date_creation")
	private LocalDateTime localDateTime;
	
	@Column(name = "uuid", unique = true, nullable = false, length = 38, updatable = false)
	private String uuid = UUID.randomUUID().toString();
	
	@Column(name = "user_id")
	private Integer userId;
	
	@Column(name = "patient_id")
	private Integer patientId;
	
	@ManyToMany
	@JoinTable(name = "client_assurance", joinColumns = @JoinColumn(name = "client_id"), inverseJoinColumns = @JoinColumn(name = "assurance_id"))
	private List<Assurance> assuranceList;
	
	// Constructeurs
	public Client() {
	}
	
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
	
	public String getFirstnames() {
		return firstnames;
	}
	
	public void setFirstnames(String firstnames) {
		this.firstnames = firstnames;
	}
	
	public Date getBirthDate() {
		return birthDate;
	}
	
	public void setBirthDate(Date birthDate) {
		this.birthDate = birthDate;
	}
	
	public Integer getAge() {
		return age;
	}
	
	public void setAge(Integer age) {
		this.age = age;
	}
	
	public String getSex() {
		return sex;
	}
	
	public void setSex(String sex) {
		this.sex = sex;
	}
	
	public String getAddress() {
		return address;
	}
	
	public void setAddress(String address) {
		this.address = address;
	}
	
	public String getTelephone() {
		return telephone;
	}
	
	public void setTelephone(String telephone) {
		this.telephone = telephone;
	}
	
	public LocalDateTime getLocalDateTime() {
		return localDateTime;
	}
	
	public void setLocalDateTime(LocalDateTime localDateTime) {
		this.localDateTime = localDateTime;
	}
	
	public String getUuid() {
		return uuid;
	}
	
	public void setUuid(String uuid) {
		this.uuid = uuid;
	}
	
	public Integer getUserId() {
		return userId;
	}
	
	public void setUserId(Integer userId) {
		this.userId = userId;
	}
	
	public Integer getPatientId() {
		return patientId;
	}
	
	public void setPatientId(Integer patientId) {
		this.patientId = patientId;
	}
	
	public List<Assurance> getAssuranceList() {
		return assuranceList;
	}
	
	public void setAssuranceList(List<Assurance> assuranceList) {
		this.assuranceList = assuranceList;
	}
}
