package org.openmrs.module.mycashier;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

@Entity
@Table(name = "ligne_approvis")
public class LigneApprovis {
	
	@EmbeddedId
	private LigneApprovisId id;
	
	@MapsId("approvisionnementId")
	@ManyToOne
	@JoinColumn(name = "approvisionnement_id")
	private Approvisionnement approvisionnement;
	
	@MapsId("myDrugId")
	@ManyToOne
	@JoinColumn(name = "my_drug_id")
	private MyDrug myDrug;
	
	@Column(name = "quantite")
	private Integer quantite;
	
	//Constructeurs
	
	public LigneApprovis() {
	}
	
	// Getters and Setters
	
	public LigneApprovisId getId() {
		return id;
	}
	
	public void setId(LigneApprovisId id) {
		this.id = id;
	}
	
	public Approvisionnement getApprovisionnement() {
		return approvisionnement;
	}
	
	public void setApprovisionnement(Approvisionnement approvisionnement) {
		this.approvisionnement = approvisionnement;
	}
	
	public MyDrug getMyDrug() {
		return myDrug;
	}
	
	public void setMyDrug(MyDrug myDrug) {
		this.myDrug = myDrug;
	}
	
	public Integer getQuantite() {
		return quantite;
	}
	
	public void setQuantite(Integer quantite) {
		this.quantite = quantite;
	}
	
	@Embeddable
	public static class LigneApprovisId implements Serializable {
		
		@Column(name = "approvisionnement_id")
		private Integer approvisionnementId;
		
		@Column(name = "my_drug_id")
		private Integer myDrugId;
		
		// Default constructor
		public LigneApprovisId() {
		}
		
		// Parameterized constructor
		public LigneApprovisId(Integer approvisionnementId, Integer myDrugId) {
			this.approvisionnementId = approvisionnementId;
			this.myDrugId = myDrugId;
		}
		
		// Getters and Setters
		public Integer getApprovisionnementId() {
			return approvisionnementId;
		}
		
		public void setApprovisionnementId(Integer approvisionnementId) {
			this.approvisionnementId = approvisionnementId;
		}
		
		public Integer getMyDrugId() {
			return myDrugId;
		}
		
		public void setMyDrugId(Integer myDrugId) {
			this.myDrugId = myDrugId;
		}
		
	}
}
