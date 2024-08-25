package org.openmrs.module.mylabtest;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "test_result_attributes")
public class TestResultAttribute {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Integer id;

	@ManyToOne
	@JoinColumn(name = "lab_test_order_id", nullable = false)
	private LabTestOrder labTestOrder;

	@Column(name = "test_type_attr_id", nullable = false)
	private Integer testTypeAttrId;

	@Column(name = "value_text", nullable = true)
	private String valueText;

	@Column(name = "value_number", nullable = true)
	private Double valueNumber;

	// Getters and Setters

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public LabTestOrder getLabTestOrder() {
		return labTestOrder;
	}

	public Integer getTestTypeAttrId() {
		return testTypeAttrId;
	}

	public void setLabTestOrder(LabTestOrder labTestOrder) {
		this.labTestOrder = labTestOrder;
	}

	public void setTestTypeAttrId(Integer testTypeAttrId) {
		this.testTypeAttrId = testTypeAttrId;
	}

	public String getValueText() {
		return valueText;
	}

	public void setValueText(String valueText) {
		this.valueText = valueText;
	}

	public Double getValueNumber() {
		return valueNumber;
	}

	public void setValueNumber(Double valueNumber) {
		this.valueNumber = valueNumber;
	}

}
