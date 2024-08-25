package org.openmrs.module.mycashier.api;

import org.openmrs.annotation.Authorized;
import org.openmrs.api.APIException;
import org.openmrs.api.OpenmrsService;

import org.openmrs.module.mycashier.LigneVenteDrug;
import org.openmrs.module.mycashier.MyDrug;
import org.openmrs.module.mycashier.VenteDrug;
import org.openmrs.module.mycashier.MycashierConfig;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Component
@Service
public interface VenteDrugService {
	
	@Transactional(readOnly = true)
	VenteDrug getVenteDrugByUuid(String uuid) throws APIException;
	
	@Transactional(readOnly = true)
	VenteDrug getVenteDrugById(Integer venteDrugId) throws APIException;
	
	@Transactional(readOnly = true)
	List<VenteDrug> getAllVenteDrugs() throws APIException;
	
	@Transactional(readOnly = true)
	List<VenteDrug> getAllVenteDrugs(LocalDateTime start, LocalDateTime end) throws APIException;
	
	@Transactional(readOnly = true)
	List<LigneVenteDrug> getAllLIgneVenteDrugsByDrug(Integer myDrugId) throws APIException;
	
	@Authorized(MycashierConfig.MODULE_PRIVILEGE)
	@Transactional
	VenteDrug saveVenteDrug(VenteDrug venteDrug) throws APIException;
	
	@Transactional
	void addLigneToVenteDrug(MyDrug myDrug, VenteDrug venteDrug, Integer quantity) throws APIException;
	
	@Transactional
	void deleteLigneFromVenteDrug(MyDrug myDrug, VenteDrug venteDrug) throws APIException;
	
	@Authorized(MycashierConfig.MODULE_PRIVILEGE)
	@Transactional
	VenteDrug deleteVenteDrug(VenteDrug venteDrug) throws APIException;

}
