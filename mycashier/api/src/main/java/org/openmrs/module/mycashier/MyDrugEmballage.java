package org.openmrs.module.mycashier;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

@Entity
@Table(name = "my_drug_emballage")
public class MyDrugEmballage implements Serializable {
	
	@EmbeddedId
	private MyDrugEmballageId id;
	
	@ManyToOne
	@MapsId("myDrugId")
	@JoinColumn(name = "my_drug_id")
	private MyDrug myDrug;
	
	@ManyToOne
	@MapsId("emballageId")
	@JoinColumn(name = "emballage_id")
	private Emballage emballage;
	
	@Column(name = "unit_number")
	private Integer unitNumber;
	
	//Constructeurs
	
	public MyDrugEmballage() {
	}
	
	// Getters and Setters
	
	public MyDrugEmballageId getId() {
		return id;
	}
	
	public void setId(MyDrugEmballageId id) {
		this.id = id;
	}
	
	public MyDrug getMyDrug() {
		return myDrug;
	}
	
	public void setMyDrug(MyDrug myDrug) {
		this.myDrug = myDrug;
	}
	
	public Emballage getEmballage() {
		return emballage;
	}
	
	public void setEmballage(Emballage emballage) {
		this.emballage = emballage;
	}
	
	public Integer getUnitNumber() {
		return unitNumber;
	}
	
	public void setUnitNumber(Integer unitNumber) {
		this.unitNumber = unitNumber;
	}
	
	@Override
	public String toString() {
		return "MyDrugEmballage{" + "id=" + id + ", myDrug=" + myDrug + ", emballage=" + emballage + ", unitNumber="
		        + unitNumber + '}';
	}
	
	@Embeddable
	public static class MyDrugEmballageId implements Serializable {
		
		@Column(name = "my_drug_id")
		private Integer myDrugId;
		
		@Column(name = "emballage_id")
		private Integer emballageId;
		
		// Default constructor
		public MyDrugEmballageId() {
		}
		
		// Parameterized constructor
		public MyDrugEmballageId(Integer myDrugId, Integer emballageId) {
			this.myDrugId = myDrugId;
			this.emballageId = emballageId;
		}
		
		// Getters and Setters
		public Integer getMyDrugId() {
			return myDrugId;
		}
		
		public void setMyDrugId(Integer myDrugId) {
			this.myDrugId = myDrugId;
		}
		
		public Integer getEmballageId() {
			return emballageId;
		}
		
		public void setEmballageId(Integer emballageId) {
			this.emballageId = emballageId;
		}
		
	}
}
