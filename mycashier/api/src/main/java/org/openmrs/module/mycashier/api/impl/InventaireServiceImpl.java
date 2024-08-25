package org.openmrs.module.mycashier.api.impl;

import org.openmrs.api.APIException;
import org.openmrs.module.mycashier.Agent;

import org.openmrs.module.mycashier.DrugInventaire;
import org.openmrs.module.mycashier.Inventaire;
import org.openmrs.module.mycashier.MyDrug;
import org.openmrs.module.mycashier.api.InventaireService;
import org.openmrs.module.mycashier.api.dao.InventaireDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("inventaireService")
public class InventaireServiceImpl implements InventaireService {
	
	@Autowired
	InventaireDao dao;
	
	public void setDao(InventaireDao dao) {
		this.dao = dao;
	}
	
	@Override
	public Inventaire getInventaireByUuid(String uuid) throws APIException {
		return dao.getInventaireByUuid(uuid);
	}
	
	@Override
	public Inventaire getInventaireById(Integer inventaireId) throws APIException {
		return dao.getInventaireById(inventaireId);
	}
	
	@Override
	public List<Inventaire> getAllInventaires() throws APIException {
		return dao.getAllInventaires();
	}
	
	@Override
	public List<DrugInventaire> getAllDrugInventairesByDrug(Integer myDrugId) throws APIException {
		return dao.getAllDrugInventairesByDrug(myDrugId);
	}
	
	@Override
	public Inventaire saveInventaire(Inventaire inventaire) throws APIException {
		return dao.saveInventaire(inventaire);
	}
	
	@Override
	public void addDrugInventaire(Inventaire inventaire, MyDrug myDrug, Integer realQuantity, Integer ecart, String motif)
	        throws APIException {
		dao.addDrugInventaire(inventaire, myDrug, realQuantity, ecart, motif);
	}
	
	@Override
	public void deleteDrugInventaire(Inventaire inventaire, MyDrug myDrug) throws APIException {
		dao.deleteDrugInventaire(inventaire, myDrug);
	}
	
	@Override
	public Inventaire deleteInventaire(Inventaire inventaire, String motif, Agent agent) throws APIException {
		return dao.deleteInventaire(inventaire, motif, agent);
	}
}
