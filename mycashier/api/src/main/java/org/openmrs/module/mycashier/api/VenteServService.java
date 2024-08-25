package org.openmrs.module.mycashier.api;

import org.openmrs.annotation.Authorized;
import org.openmrs.api.APIException;
import org.openmrs.api.OpenmrsService;
import org.openmrs.module.mycashier.LigneVenteService;
import org.openmrs.module.mycashier.MyService;
import org.openmrs.module.mycashier.VenteService;
//import org.openmrs.module.mycashier.LigneVenteService;
import org.openmrs.module.mycashier.MycashierConfig;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Component
@Service
public interface VenteServService {
	
	@Transactional(readOnly = true)
	VenteService getVenteServiceByUuid(String uuid) throws APIException;
	
	@Transactional(readOnly = true)
	VenteService getVenteServiceById(Integer venteServiceId) throws APIException;
	
	@Transactional(readOnly = true)
	List<VenteService> getAllVenteServices() throws APIException;
	
	@Authorized(MycashierConfig.MODULE_PRIVILEGE)
	@Transactional
	VenteService saveVenteService(VenteService venteService) throws APIException;
	
	@Transactional
	void addLigneToVenteService(Integer venteServiceId, Integer serviceId) throws APIException;
	
	@Authorized(MycashierConfig.MODULE_PRIVILEGE)
	@Transactional
	VenteService deleteVenteService(VenteService venteService) throws APIException;
	
	@Transactional(readOnly = true)
	LigneVenteService getLigneVenteServiceById(Integer venteServiceId, Integer serviceId) throws APIException;
	
	@Transactional(readOnly = true)
	List<LigneVenteService> getAllLigneVenteServices() throws APIException;
	
	@Authorized(MycashierConfig.MODULE_PRIVILEGE)
	@Transactional
	LigneVenteService saveLigneVenteService(LigneVenteService ligneVenteService) throws APIException;
	
	@Authorized(MycashierConfig.MODULE_PRIVILEGE)
	@Transactional
	LigneVenteService deleteLigneVenteService(LigneVenteService ligneVenteService) throws APIException;
	
}
