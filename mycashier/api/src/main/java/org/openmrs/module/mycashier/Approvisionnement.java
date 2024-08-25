package org.openmrs.module.mycashier;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.Objects;
import java.util.UUID;

@Entity
@Table(name = "approvisionnement")
public class Approvisionnement {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	
	@Column(name = "date_approvisionnement")
	private LocalDateTime dateTimeApprovisionnement;
	
	@ManyToOne
	@JoinColumn(name = "entrepot_source_id")
	private Entrepot entrepotSource;
	
	@ManyToOne
	@JoinColumn(name = "entrepot_cible_id")
	private Entrepot entrepotCible;
	
	@ManyToOne
	@JoinColumn(name = "agent_id")
	private Agent agent;
	
	@Column(name = "uuid")
	private String uuid;
	
	@Column(name = "date_creation")
	private LocalDateTime localDateTime = LocalDateTime.now();
	
	//Constructeurs
	
	public Approvisionnement() {
	}
	
	// Getters and Setters
	
	public Integer getId() {
		return id;
	}
	
	public void setId(Integer id) {
		this.id = id;
	}
	
	public LocalDateTime getDateTimeApprovisionnement() {
		return dateTimeApprovisionnement;
	}
	
	public void setDateTimeApprovisionnement(LocalDateTime dateTimeApprovisionnement) {
		this.dateTimeApprovisionnement = dateTimeApprovisionnement;
	}
	
	public Entrepot getEntrepotSource() {
		return entrepotSource;
	}
	
	public void setEntrepotSource(Entrepot entrepotSource) {
		this.entrepotSource = entrepotSource;
	}
	
	public Entrepot getEntrepotCible() {
		return entrepotCible;
	}
	
	public void setEntrepotCible(Entrepot entrepotCible) {
		this.entrepotCible = entrepotCible;
	}
	
	public Agent getAgent() {
		return agent;
	}
	
	public void setAgent(Agent agent) {
		this.agent = agent;
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
}
