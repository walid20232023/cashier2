package org.openmrs.module.mycashier;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.UUID;

@Entity
@Table(name = "vente_service")
public class VenteService {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	
	@Column(name = "date_vente")
	private LocalDateTime dateVente;
	
	@ManyToOne
	@JoinColumn(name = "agent_id")
	private Agent agent;
	
	@ManyToOne
	@JoinColumn(name = "client_id")
	private Client client;
	
	@Column(name = "uuid", unique = true, nullable = false, length = 38, updatable = false)
	private String uuid = UUID.randomUUID().toString();
	
	@Column(name = "date_creation")
	private LocalDateTime localDateTime = LocalDateTime.now();
	
	@Column(name = "vente_validate", columnDefinition = "BOOLEAN DEFAULT FALSE")
	private Boolean venteValidate;
	
	//Constructeurs
	
	public VenteService() {
	}
	
	// Getters and Setters
	
	public Integer getId() {
		return id;
	}
	
	public void setId(Integer id) {
		this.id = id;
	}
	
	public LocalDateTime getDateVente() {
		return dateVente;
	}
	
	public void setDateVente(LocalDateTime dateVente) {
		this.dateVente = dateVente;
	}
	
	public Agent getAgent() {
		return agent;
	}
	
	public void setAgent(Agent agent) {
		this.agent = agent;
	}
	
	public Client getClient() {
		return client;
	}
	
	public void setClient(Client client) {
		this.client = client;
	}
	
	public String getUuid() {
		return uuid;
	}
	
	public void setUuid(String uuid) {
		this.uuid = uuid;
	}
	
	public LocalDateTime getLocalDateTime() {
		return localDateTime;
	}
	
	public void setLocalDateTime(LocalDateTime localDateTime) {
		this.localDateTime = localDateTime;
	}
	
	public Boolean getVenteValidate() {
		return venteValidate;
	}
	
	public void setVenteValidate(Boolean venteValidate) {
		this.venteValidate = venteValidate;
	}
	
}
