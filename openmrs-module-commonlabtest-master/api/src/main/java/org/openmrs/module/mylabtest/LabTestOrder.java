package org.openmrs.module.mylabtest;

import org.openmrs.Patient;
import org.openmrs.User;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "lab_test_order")
public class LabTestOrder {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Integer id;

	@Column(name = "datetime", nullable = false)
	private LocalDateTime datetime;

	@Column(name = "uuid", unique = true, nullable = false, length = 38, updatable = false)
	private String uuid = UUID.randomUUID().toString();

	@ManyToOne
	@JoinColumn(name = "patient_id", nullable = false)
	private Patient patient;

	@ManyToOne
	@JoinColumn(name = "test_type_id", nullable = false)
	private MyLabTestType testType;

	@ManyToOne
	@JoinColumn(name = "user_id", nullable = false)
	private User user;

	@OneToMany(mappedBy = "labTestOrder")
	private List<TestResultAttribute> testResultAttributes;

	@Column(name = "result", length = 255)
	private String result;

	@ManyToOne
	@JoinColumn(name = "result_adder_id", nullable = true)
	// Modifiez le nom de la colonne ici
	private User resultAdder;

	@Column(name = "result_datetime")
	private LocalDateTime resultDatetime;

	public LabTestOrder() {
	}

	// Getters and Setters

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public LocalDateTime getDatetime() {
		return datetime;
	}

	public void setDatetime(LocalDateTime datetime) {
		this.datetime = datetime;
	}

	public String getUuid() {
		return uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

	public Patient getPatient() {
		return patient;
	}

	public void setPatient(Patient patient) {
		this.patient = patient;
	}

	public MyLabTestType getTestType() {
		return testType;
	}

	public void setTestType(MyLabTestType testType) {
		this.testType = testType;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public List<TestResultAttribute> getTestResultAttributes() {
		return testResultAttributes;
	}

	public void setTestResultAttributes(List<TestResultAttribute> testResultAttributes) {
		this.testResultAttributes = testResultAttributes;
	}

	public String getResult() {
		return result;
	}

	public void setResult(String result) {
		this.result = result;
	}

	public User getResultAdder() {
		return resultAdder;
	}

	public void setResultAdder(User resultAdder) {
		this.resultAdder = resultAdder;
	}

	public LocalDateTime getResultDatetime() {
		return resultDatetime;
	}

	public void setResultDatetime(LocalDateTime resultDatetime) {
		this.resultDatetime = resultDatetime;
	}

}
