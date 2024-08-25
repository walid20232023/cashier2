package org.openmrs.module.mycashier;

import javax.persistence.*;
import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

@Entity
@Table(name = "ligne_vente_service")
public class LigneVenteService implements Serializable {
	
	@EmbeddedId
	private LigneVenteServiceId id;
	
	@ManyToOne
	@MapsId("venteServiceId")
	@JoinColumn(name = "vente_service_id")
	private VenteService venteService;
	
	@ManyToOne
	@MapsId("myServiceId")
	@JoinColumn(name = "service_id")
	private MyService myservice;
	
	//Construteurs
	
	public LigneVenteService() {
	}
	
	// Getters and Setters
	
	public LigneVenteServiceId getId() {
		return id;
	}
	
	public void setId(LigneVenteServiceId id) {
		this.id = id;
	}
	
	public VenteService getVenteService() {
		return venteService;
	}
	
	public void setVenteService(VenteService venteService) {
		this.venteService = venteService;
	}
	
	public MyService getMyservice() {
		return myservice;
	}
	
	public void setMyservice(MyService myservice) {
		this.myservice = myservice;
	}
	
	@Embeddable
	public static class LigneVenteServiceId implements Serializable {
		
		@Column(name = "vente_service_id")
		private Integer venteServiceId;
		
		@Column(name = "service_id")
		private Integer myServiceId;
		
		// Default constructor
		
		public LigneVenteServiceId() {
		}
		
		// Parameterized constructor
		
		public LigneVenteServiceId(Integer venteServiceId, Integer myServiceId) {
			this.venteServiceId = venteServiceId;
			this.myServiceId = myServiceId;
		}
		
		// Getters and Setters
		
		public Integer getVenteServiceId() {
			return venteServiceId;
		}
		
		public void setVenteServiceId(Integer venteServiceId) {
			this.venteServiceId = venteServiceId;
		}
		
		public Integer getMyServiceId() {
			return myServiceId;
		}
		
		public void setMyServiceId(Integer myServiceId) {
			this.myServiceId = myServiceId;
		}
	}
}
