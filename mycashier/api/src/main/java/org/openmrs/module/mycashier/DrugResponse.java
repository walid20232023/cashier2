package org.openmrs.module.mycashier;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.time.LocalDateTime;
import java.util.UUID;

public class DrugResponse {
	
	private Integer id;
	
	private Integer baseInam;
	
	private Integer price;
	
	private String name;
	
	private String cip;
	
	private String groupeTherap;
	
	private String dci;
	
	private LocalDateTime dateCreated;
	
	private String uuid;
	
	private Integer drugId;
	
	//---------Les attributs supplémentaires
	private Integer stockLocal;
	
	private Integer stockMag;
	
	public Integer getId() {
		return id;
	}
	
	public Integer getBaseInam() {
		return baseInam;
	}
	
	public Integer getPrice() {
		return price;
	}
	
	public String getName() {
		return name;
	}
	
	public String getCip() {
		return cip;
	}
	
	public String getGroupeTherap() {
		return groupeTherap;
	}
	
	public String getDci() {
		return dci;
	}
	
	public LocalDateTime getDateCreated() {
		return dateCreated;
	}
	
	public String getUuid() {
		return uuid;
	}
	
	public Integer getDrugId() {
		return drugId;
	}
	
	public Integer getStockLocal() {
		return stockLocal;
	}
	
	public Integer getStockMag() {
		return stockMag;
	}
	
	public void setId(Integer id) {
		this.id = id;
	}
	
	public void setBaseInam(Integer baseInam) {
		this.baseInam = baseInam;
	}
	
	public void setPrice(Integer price) {
		this.price = price;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public void setCip(String cip) {
		this.cip = cip;
	}
	
	public void setGroupeTherap(String groupeTherap) {
		this.groupeTherap = groupeTherap;
	}
	
	public void setDci(String dci) {
		this.dci = dci;
	}
	
	public void setDateCreated(LocalDateTime dateCreated) {
		this.dateCreated = dateCreated;
	}
	
	public void setUuid(String uuid) {
		this.uuid = uuid;
	}
	
	public void setDrugId(Integer drugId) {
		this.drugId = drugId;
	}
	
	public void setStockLocal(Integer stockLocal) {
		this.stockLocal = stockLocal;
	}
	
	public void setStockMag(Integer stockMag) {
		this.stockMag = stockMag;
	}
	
	//Constructeurs
	
	public DrugResponse() {
	}
	
	public DrugResponse drugToResponse(MyDrug myDrug) {
		
		DrugResponse drugResponse = new DrugResponse();
		drugResponse.setId(myDrug.getDrugId());
		drugResponse.setName(myDrug.getName());
		drugResponse.setDci(myDrug.getDci());
		drugResponse.setCip(myDrug.getCip());
		drugResponse.setBaseInam(myDrug.getBaseInam());
		drugResponse.setDateCreated(myDrug.getDateCreated());
		drugResponse.setGroupeTherap(myDrug.getGroupeTherap());
		drugResponse.setPrice(myDrug.getPrice());
		drugResponse.setUuid(myDrug.getUuid());
		drugResponse.setDrugId(myDrug.getDrugId());
		
		System.out.println("drugToResponse appelée");
		System.out.println("drugResponse :" + drugResponse.toString());
		
		return drugResponse;
		
	}
	
}
