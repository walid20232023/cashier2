package org.openmrs.module.mycashier.api;

import org.openmrs.annotation.Authorized;
import org.openmrs.api.APIException;
import org.openmrs.api.OpenmrsService;

import org.openmrs.module.mycashier.MyDrug;
import org.openmrs.module.mycashier.MycashierConfig;
import org.openmrs.module.mycashier.Approvisionnement;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Component
@Service
public interface ApprovisionnementService {
	
	@Transactional(readOnly = true)
	Approvisionnement getApprovisionnementByUuid(String uuid) throws APIException;
	
	@Transactional(readOnly = true)
	Approvisionnement getApprovisionnementById(Integer approvisionnementId) throws APIException;
	
	@Transactional(readOnly = true)
	List<Approvisionnement> getAllApprovisionnements(LocalDateTime start, LocalDateTime end) throws APIException;
	
	/**
	 * @Transactional(readOnly = true) List<LigneApprovis> getAllLigneApprovisByDrug(LocalDateTime
	 *                         start, LocalDateTime end) throws APIException;
	 **/
	//Les approvisionnement effectués d'un entrepot durant une période donnée
	@Transactional(readOnly = true)
	List<Approvisionnement> getAllApprovisionnementsByEntrepotSource(LocalDateTime start, LocalDateTime end,
	        Integer entrepotSource) throws APIException;
	
	//Les approvisionnement reçus dans un entrepot durant une période donnée
	@Transactional(readOnly = true)
	List<Approvisionnement> getAllApprovisionnementsByEntrepotCible(LocalDateTime start, LocalDateTime end,
	        Integer entrepotCible) throws APIException;
	
	@Authorized(MycashierConfig.MODULE_PRIVILEGE)
	@Transactional
	Approvisionnement saveApprovisionnement(Approvisionnement approvisionnement) throws APIException;
	
	@Transactional
	void addLigneApprovisToApprovisionnement(Approvisionnement approvisionnement, MyDrug myDrug, Integer quantite)
	        throws APIException;
	
	@Transactional
	void deleteLigneApprovisFRomApprovisionnement(Integer LigneApprovisId) throws APIException;
	
	@Authorized(MycashierConfig.MODULE_PRIVILEGE)
	@Transactional
	Approvisionnement deleteApprovisionnement(Approvisionnement approvisionnement) throws APIException;
	
}
