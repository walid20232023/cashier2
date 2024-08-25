package org.openmrs.module.mycashier.api.impl;

import org.openmrs.api.APIException;

import org.openmrs.module.mycashier.Entrepot;

import org.openmrs.module.mycashier.MyDrug;
import org.openmrs.module.mycashier.StockEntrepot;
import org.openmrs.module.mycashier.api.EntrepotService;

import org.openmrs.module.mycashier.api.dao.EntrepotDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service("entrepotService")
public class EntrepotServiceImpl implements EntrepotService {
	
	@Autowired
	EntrepotDao dao;
	
	public void setDao(EntrepotDao dao) {
		this.dao = dao;
	}
	
	@Override
	public Entrepot getEntrepotByUuid(String uuid) throws APIException {
		return dao.getEntrepotByUuid(uuid);
	}
	
	@Override
	public Entrepot getEntrepotById(Integer entrepotId) throws APIException {
		return dao.getEntrepotById(entrepotId);
	}
	
	@Override
	public List<Entrepot> getAllEntrepots() throws APIException {
		return dao.getAllEntrepots();
	}
	
	@Override
	public void saveDrugQuantityForEntrepot(MyDrug myDrug, Entrepot entrepot, Integer quantity) throws APIException {
		
		dao.saveDrugQuantityForEntrepot(myDrug, entrepot, quantity);
	}
	
	@Override
	public List<StockEntrepot> getAllStockEntrepotsByEntrepot(Integer entrepotId) throws APIException {
		return dao.getAllStockEntrepotsByEntrepot(entrepotId);
	}
	
	@Override
	public StockEntrepot getStockByEntrepotAndDrug(Integer entrepotId, Integer drugId) throws APIException {
		return dao.getStockByEntrepotAndDrug(entrepotId, drugId);
	}
	
	/* @Override public List<VenteDrug> getAllVenteDrugByEntrepot(LocalDateTime start, LocalDateTime
	*           end) { return dao.getAllVenteDrugByEntrepot(start, end); }
	* @Override public List<VenteDrug> getAllVenteDrugByEntrepotAndDrug(LocalDateTime start,
	*           LocalDateTime end, Integer myDrugId) throws APIException { return
	*           dao.getAllVenteDrugByEntrepotAndDrug(start, end, myDrugId); }
	* @Override public List<VenteDrug> getAllDrugAvarieByEntrepot(LocalDateTime start,
	*           LocalDateTime end) throws APIException { return
	*           dao.getAllDrugAvarieByEntrepot(start, end); }
	* @Override public List<Approvisionnement> getAllDrugApprovisByEntrepot(LocalDateTime start,
	*           LocalDateTime end) throws APIException { return
	*           dao.getAllDrugApprovisByEntrepot(start, end); }
	* @Override public List<VenteDrug> getAllDrugApprovisByEntrepotAndDrug(LocalDateTime start,
	*           LocalDateTime end, Integer myDrugId) throws APIException { return
	*           dao.getAllDrugApprovisByEntrepotAndDrug(start, end, myDrugId); }
	**/
	@Override
	public Entrepot saveEntrepot(Entrepot entrepot) throws APIException {
		return dao.saveEntrepot(entrepot);
	}
	
	@Override
	public Entrepot deleteEntrepot(Entrepot entrepot) throws APIException {
		return dao.deleteEntrepot(entrepot);
	}
}
