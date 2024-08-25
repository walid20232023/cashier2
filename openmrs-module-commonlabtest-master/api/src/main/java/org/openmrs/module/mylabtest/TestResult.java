package org.openmrs.module.mylabtest;

import javax.persistence.*;

import java.util.UUID;

@Entity
@Table(name = "test_results")
public class TestResult {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Integer id;

	@Column(name = "lab_test_order_id", nullable = false)
	private Integer labTestOrderId;

	@Column(name = "test_type_attr_id", nullable = false)
	private Integer testTypeAttrId;

	@Column(name = "result", nullable = false)
	private String result;

	@Column(name = "user_id", nullable = false)
	private Integer userId;

	@Column(name = "uuid", unique = true, nullable = false, length = 38, updatable = false)
	private String uuid = UUID.randomUUID().toString();

	// Getters and Setters

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getLabTestOrderId() {
		return labTestOrderId;
	}

	public void setLabTestOrderId(Integer labTestOrderId) {
		this.labTestOrderId = labTestOrderId;
	}

	public Integer getTestTypeAttrId() {
		return testTypeAttrId;
	}

	public void setTestTypeAttrId(Integer testTypeAttrId) {
		this.testTypeAttrId = testTypeAttrId;
	}

	public String getResult() {
		return result;
	}

	public void setResult(String result) {
		this.result = result;
	}

	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	public String getUuid() {
		return uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

	@Override
	public String toString() {
		return "TestResult{" + "id=" + id + ", labTestOrderId=" + labTestOrderId + ", testTypeAttrId=" + testTypeAttrId
		        + ", result='" + result + '\'' + ", userId=" + userId + ", uuid='" + uuid + '\'' + '}';
	}
}
