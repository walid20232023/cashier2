package org.openmrs.module.mycashier;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

@Entity
@Table(name = "drug_inventaire")
public class DrugInventaire {
	
	@EmbeddedId
	private DrugInventaireId id;
	
	@ManyToOne
	@MapsId("myDrugId")
	@JoinColumn(name = "my_drug_id")
	private MyDrug myDrug;
	
	@ManyToOne
	@MapsId("inventaireId")
	@JoinColumn(name = "inventaire_id")
	private Inventaire inventaire;
	
	@Column(name = "real_quantity")
	private Integer realQuantity;
	
	@Column(name = "ecart")
	private Integer ecart;
	
	public void setEcart(Integer ecart) {
		this.ecart = ecart;
	}
	
	@Column(name = "motif")
	private String motif;
	
	public Integer getEcart() {
		return ecart;
	}
	
	// Default constructor
	public DrugInventaire() {
	}
	
	// Getters and Setters
	
	public DrugInventaireId getId() {
		return id;
	}
	
	public void setId(DrugInventaireId id) {
		this.id = id;
	}
	
	public MyDrug getMyDrug() {
		return myDrug;
	}
	
	public void setMyDrug(MyDrug myDrug) {
		this.myDrug = myDrug;
	}
	
	public Inventaire getInventaire() {
		return inventaire;
	}
	
	public void setInventaire(Inventaire inventaire) {
		this.inventaire = inventaire;
	}
	
	public Integer getRealQuantity() {
		return realQuantity;
	}
	
	public void setRealQuantity(Integer realQuantity) {
		this.realQuantity = realQuantity;
	}
	
	public String getMotif() {
		return motif;
	}
	
	public void setMotif(String motif) {
		this.motif = motif;
	}
	
	@Override
	public String toString() {
		return "DrugInventaire{" + "id=" + id + ", myDrug=" + myDrug + ", inventaire=" + inventaire + ", realQuantity="
		        + realQuantity + ", motif='" + motif + '\'' + '}';
	}
	
	@Embeddable
	public static class DrugInventaireId implements Serializable {
		
		@Column(name = "my_drug_id")
		private Integer myDrugId;
		
		@Column(name = "inventaire_id")
		private Integer inventaireId;
		
		// Default constructor
		public DrugInventaireId() {
		}
		
		// Parameterized constructor
		public DrugInventaireId(Integer myDrugId, Integer inventaireId) {
			this.myDrugId = myDrugId;
			this.inventaireId = inventaireId;
		}
		
		// Getters and Setters
		public Integer getMyDrugId() {
			return myDrugId;
		}
		
		public void setMyDrugId(Integer myDrugId) {
			this.myDrugId = myDrugId;
		}
		
		public Integer getInventaireId() {
			return inventaireId;
		}
		
		public void setInventaireId(Integer inventaireId) {
			this.inventaireId = inventaireId;
		}
		
		@Override
		public boolean equals(Object o) {
			if (this == o)
				return true;
			if (o == null || getClass() != o.getClass())
				return false;
			DrugInventaireId that = (DrugInventaireId) o;
			return myDrugId.equals(that.myDrugId) && inventaireId.equals(that.inventaireId);
		}
		
	}
}
