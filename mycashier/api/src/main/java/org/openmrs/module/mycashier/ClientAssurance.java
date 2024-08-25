package org.openmrs.module.mycashier;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "client_assurance")
public class ClientAssurance {
	
	@EmbeddedId
	private ClientAssuranceId id;
	
	@ManyToOne
	@MapsId("assuranceId")
	@JoinColumn(name = "assurance_id")
	private Assurance assurance;
	
	@ManyToOne
	@MapsId("clientId")
	@JoinColumn(name = "client_id")
	private Client client;
	
	// Constructeurs
	public ClientAssurance() {
	}
	
	// Getters and Setters
	
	public ClientAssuranceId getId() {
		return id;
	}
	
	public void setId(ClientAssuranceId id) {
		this.id = id;
	}
	
	public Assurance getAssurance() {
		return assurance;
	}
	
	public void setAssurance(Assurance assurance) {
		this.assurance = assurance;
	}
	
	public Client getClient() {
		return client;
	}
	
	public void setClient(Client client) {
		this.client = client;
	}
	
	@Override
	public String toString() {
		return "ClientAssurance{" + "id=" + id + ", assurance=" + assurance + ", client=" + client + '}';
	}
	
	@Embeddable
	public static class ClientAssuranceId implements Serializable {
		
		@Column(name = "assurance_id")
		private Integer assuranceId;
		
		@Column(name = "client_id")
		private Integer clientId;
		
		// Default constructor
		public ClientAssuranceId() {
		}
		
		// Parameterized constructor
		public ClientAssuranceId(Integer assuranceId, Integer clientId) {
			this.assuranceId = assuranceId;
			this.clientId = clientId;
		}
		
		// Getters and Setters
		
		public Integer getAssuranceId() {
			return assuranceId;
		}
		
		public void setAssuranceId(Integer assuranceId) {
			this.assuranceId = assuranceId;
		}
		
		public Integer getClientId() {
			return clientId;
		}
		
		public void setClientId(Integer clientId) {
			this.clientId = clientId;
		}
	}
}
