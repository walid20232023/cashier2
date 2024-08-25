package org.openmrs.module.mycashier.api;

import org.openmrs.annotation.Authorized;
import org.openmrs.api.APIException;
import org.openmrs.api.OpenmrsService;
import org.openmrs.module.mycashier.*;

import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Component
@Service
public interface InventaireService {
	
	@Transactional(readOnly = true)
	Inventaire getInventaireByUuid(String uuid) throws APIException;
	
	@Transactional(readOnly = true)
	Inventaire getInventaireById(Integer inventaireId) throws APIException;
	
	@Transactional(readOnly = true)
	List<Inventaire> getAllInventaires() throws APIException;
	
	@Transactional(readOnly = true)
	List<DrugInventaire> getAllDrugInventairesByDrug(Integer myDrugId) throws APIException;
	
	@Authorized(MycashierConfig.MODULE_PRIVILEGE)
	@Transactional
	Inventaire saveInventaire(Inventaire inventaire) throws APIException;
	
	@Transactional
	void addDrugInventaire(Inventaire inventaire, MyDrug myDrug, Integer realQuantity, Integer ecart, String motif)
	        throws APIException;
	
	@Transactional
	void deleteDrugInventaire(Inventaire inventaire, MyDrug myDrug) throws APIException;
	
	@Authorized(MycashierConfig.MODULE_PRIVILEGE)
	@Transactional
	Inventaire deleteInventaire(Inventaire inventaire, String motif, Agent agent) throws APIException;
}
