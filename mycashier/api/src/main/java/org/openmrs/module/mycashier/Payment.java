package org.openmrs.module.mycashier;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.UUID;

@Entity
@Table(name = "payment")
public class Payment implements Serializable {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	
	@Column(name = "date_payment")
	private LocalDateTime datePayment = LocalDateTime.now();
	
	@Column(name = "uuid", unique = true, nullable = false, length = 38, updatable = false)
	private String uuid = UUID.randomUUID().toString();
	
	@ManyToOne
	@JoinColumn(name = "agent_id")
	private Agent agent;
	
	@ManyToOne
	@JoinColumn(name = "vente_service_id")
	private VenteService venteService;
	
	@ManyToOne
	@JoinColumn(name = "vente_drug_id")
	private VenteDrug venteDrug;
	
	@Column(name = "montant")
	private Integer montant;
	
	@Column(name = "mode_payment")
	private String modePayment;
	
	//Constructeurs
	
	public Payment() {
	}
	
	// Getters and Setters
	
	public Integer getId() {
		return id;
	}
	
	public void setId(Integer id) {
		this.id = id;
	}
	
	public LocalDateTime getDatePayment() {
		return datePayment;
	}
	
	public void setDatePayment(LocalDateTime datePayment) {
		this.datePayment = datePayment;
	}
	
	public String getUuid() {
		return uuid;
	}
	
	public void setUuid(String uuid) {
		this.uuid = uuid;
	}
	
	public Agent getAgent() {
		return agent;
	}
	
	public void setAgent(Agent agent) {
		this.agent = agent;
	}
	
	public VenteService getVenteService() {
		return venteService;
	}
	
	public void setVenteService(VenteService venteService) {
		this.venteService = venteService;
	}
	
	public VenteDrug getVenteDrug() {
		return venteDrug;
	}
	
	public void setVenteDrug(VenteDrug venteDrug) {
		this.venteDrug = venteDrug;
	}
	
	public Integer getMontant() {
		return montant;
	}
	
	public void setMontant(Integer montant) {
		this.montant = montant;
	}
	
	public String getModePayment() {
		return modePayment;
	}
	
	public void setModePayment(String modePayment) {
		this.modePayment = modePayment;
	}
	
}
