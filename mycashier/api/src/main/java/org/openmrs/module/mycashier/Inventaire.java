package org.openmrs.module.mycashier;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.UUID;

@Entity
@Table(name = "inventaire")
public class Inventaire {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	
	@ManyToOne
	@JoinColumn(name = "entrepot_id")
	private Entrepot entrepot;
	
	@ManyToOne
	@JoinColumn(name = "agent_id")
	private Agent agent;
	
	@ManyToOne
	@JoinColumn(name = "superviseur_id")
	private Agent superviseur;
	
	@Column(name = "date_creation")
	private LocalDateTime dateCreation = LocalDateTime.now();
	
	@Column(name = "uuid", unique = true, nullable = false, length = 38, updatable = false)
	private String uuid = UUID.randomUUID().toString();
	
	// Default constructor
	public Inventaire() {
	}
	
	// Getters and Setters
	
	public Integer getId() {
		return id;
	}
	
	public void setId(Integer id) {
		this.id = id;
	}
	
	public Entrepot getEntrepot() {
		return entrepot;
	}
	
	public void setEntrepot(Entrepot entrepot) {
		this.entrepot = entrepot;
	}
	
	public Agent getAgent() {
		return agent;
	}
	
	public void setAgent(Agent agent) {
		this.agent = agent;
	}
	
	public Agent getSuperviseur() {
		return superviseur;
	}
	
	public void setSuperviseur(Agent superviseur) {
		this.superviseur = superviseur;
	}
	
	public LocalDateTime getDateCreation() {
		return dateCreation;
	}
	
	public void setDateCreation(LocalDateTime dateCreation) {
		this.dateCreation = dateCreation;
	}
	
	public String getUuid() {
		return uuid;
	}
	
	public void setUuid(String uuid) {
		this.uuid = uuid;
	}
	
}
