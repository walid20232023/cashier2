package org.openmrs.module.mycashier.api;

import org.openmrs.annotation.Authorized;
import org.openmrs.api.APIException;
import org.openmrs.module.mycashier.MycashierConfig;
import org.openmrs.module.mycashier.Versement;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Component
@Service
public interface VersementService {
	
	@Transactional(readOnly = true)
	Versement getVersementByUuid(String uuid) throws APIException;
	
	@Transactional(readOnly = true)
	Versement getVersementById(Integer versementId) throws APIException;
	
	//Tous les versements effectués dans une période donnée
	@Transactional(readOnly = true)
	List<Versement> getAllVersements(LocalDateTime start, LocalDateTime end) throws APIException;
	
	//Tous les versement effectués par un agent dans une période donnée
	@Transactional(readOnly = true)
	List<Versement> getAllVersementsByAgentSource(Integer agentSourceId, LocalDateTime start, LocalDateTime end)
	        throws APIException;
	
	//Tous les versement reçus par un agent dans une période donnée@Transactional(readOnly = true)
	@Transactional(readOnly = true)
	List<Versement> getAllVersementsByAgentCible(Integer agentCibleId, LocalDateTime start, LocalDateTime end)
	        throws APIException;
	
	@Authorized(MycashierConfig.MODULE_PRIVILEGE)
	@Transactional
	Versement saveVersement(Versement versement) throws APIException;
	
	@Authorized(MycashierConfig.MODULE_PRIVILEGE)
	@Transactional
	Versement deleteVersement(Versement versement, String motif) throws APIException;
}
