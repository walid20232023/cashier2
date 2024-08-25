package org.openmrs.module.mycashier;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "ligne_vente_drug")
public class LigneVenteDrug {
	
	@EmbeddedId
	private LigneVenteDrugId id;
	
	@ManyToOne
	@MapsId("venteDrugId")
	@JoinColumn(name = "vente_drug_id")
	private VenteDrug venteDrug;
	
	@ManyToOne
	@MapsId("myDrugId")
	@JoinColumn(name = "my_drug_id")
	private MyDrug myDrug;
	
	@Column(name = "quantity")
	private Integer quantity;

	@Column(name = "price")
	private Integer price;


	//Constructeurs
	
	public LigneVenteDrug() {
	}
	
	// Getters and Setters

	public Integer getPrice() {
		return price;
	}

	public void setPrice(Integer price) {
		this.price = price;
	}

	public LigneVenteDrugId getId() {
		return id;
	}
	
	public void setId(LigneVenteDrugId id) {
		this.id = id;
	}
	
	public VenteDrug getVenteDrug() {
		return venteDrug;
	}
	
	public void setVenteDrug(VenteDrug venteDrug) {
		this.venteDrug = venteDrug;
	}
	
	public MyDrug getMyDrug() {
		return myDrug;
	}
	
	public void setMyDrug(MyDrug myDrug) {
		this.myDrug = myDrug;
	}
	
	public Integer getQuantity() {
		return quantity;
	}
	
	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}
	
	@Override
	public String toString() {
		return "LigneVenteDrug{" + "id=" + id + ", venteDrug=" + venteDrug + ", myDrug=" + myDrug + ", quantity=" + quantity
		        + '}';
	}
	
	@Embeddable
	public static class LigneVenteDrugId implements Serializable {
		
		@Column(name = "vente_drug_id")
		private Integer venteDrugId;
		
		@Column(name = "my_drug_id")
		private Integer myDrugId;
		
		// Default constructor
		
		public LigneVenteDrugId() {
		}
		
		// Parameterized constructor
		
		public LigneVenteDrugId(Integer venteDrugId, Integer myDrugId) {
			this.venteDrugId = venteDrugId;
			this.myDrugId = myDrugId;
		}
		
		// Getters and Setters
		
		public Integer getVenteDrugId() {
			return venteDrugId;
		}
		
		public void setVenteDrugId(Integer venteDrugId) {
			this.venteDrugId = venteDrugId;
		}
		
		public Integer getMyDrugId() {
			return myDrugId;
		}
		
		public void setMyDrugId(Integer myDrugId) {
			this.myDrugId = myDrugId;
		}
		
	}
}
