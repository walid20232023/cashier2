package org.openmrs.module.mycashier;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.UUID;

@Entity
@Table(name = "versement")
public class Versement {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	
	@Column(name = "date_versement")
	private LocalDateTime dateVersement;
	
	@Column(name = "montant")
	private Integer montant;
	
	@ManyToOne
	@JoinColumn(name = "caisse_agent_source_id")
	private Agent caisseAgentSource;
	
	@ManyToOne
	@JoinColumn(name = "caisse_agent_cible_id")
	private Agent caisseAgentCible;
	
	@Column(name = "uuid", unique = true, nullable = false, length = 38, updatable = false)
	private String uuid = UUID.randomUUID().toString();
	
	@Column(name = "date_creation")
	private LocalDateTime dateCreation = LocalDateTime.now();; // Default constructor public
	
	public Versement() {
	}
	
	public LocalDateTime getDateCreation() {
		return dateCreation;
	}
	
	void setId(Integer id) {
		this.id = id;
	}
	
	public Integer getId() {
		return id;
	}
	
	public LocalDateTime getDateVersement() {
		return dateVersement;
	}
	
	public void setDateVersement(LocalDateTime dateVersement) {
		this.dateVersement = dateVersement;
	}
	
	public void setDateCreation(LocalDateTime dateCreation) {
		this.dateCreation = dateCreation;
	}
	
	public Integer getMontant() {
		return montant;
	}
	
	public void setMontant(Integer montant) {
		this.montant = montant;
	}
	
	public Agent getCaisseAgentSource() {
		return caisseAgentSource;
	}
	
	public void setCaisseAgentSource(Agent caisseAgentSource) {
		this.caisseAgentSource = caisseAgentSource;
	}
	
	public Agent getCaisseAgentCible() {
		return caisseAgentCible;
	}
	
	public void setCaisseAgentCible(Agent caisseAgentCible) {
		this.caisseAgentCible = caisseAgentCible;
	}
	
	public String getUuid() {
		return uuid;
	}
	
	public void setUuid(String uuid) {
		this.uuid = uuid;
	}
	
}
