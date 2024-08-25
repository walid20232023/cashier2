package org.openmrs.module.mycashier.api;

import org.openmrs.annotation.Authorized;
import org.openmrs.api.APIException;
import org.openmrs.api.OpenmrsService;
import org.openmrs.module.mycashier.*;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Component
@Service
public interface EntrepotService {
	
	@Transactional(readOnly = true)
	Entrepot getEntrepotByUuid(String uuid) throws APIException;
	
	@Transactional(readOnly = true)
	Entrepot getEntrepotById(Integer entrepotId) throws APIException;
	
	@Transactional(readOnly = true)
	List<Entrepot> getAllEntrepots() throws APIException;
	
	@Transactional(readOnly = true)
	List<StockEntrepot> getAllStockEntrepotsByEntrepot(Integer entrepotId) throws APIException;
	
	@Transactional(readOnly = true)
	StockEntrepot getStockByEntrepotAndDrug(Integer entrepotId, Integer drugId) throws APIException;
	
	@Transactional
	void saveDrugQuantityForEntrepot(MyDrug myDrug, Entrepot entrepot, Integer quantity) throws APIException;
	
	/* * @Transactional(readOnly = true) List<VenteDrug> getAllVenteDrugByEntrepot(LocalDateTime
	 *                         start, LocalDateTime end);
	 * @Transactional(readOnly = true) List<VenteDrug>
	 *                         getAllVenteDrugByEntrepotAndDrug(LocalDateTime start, LocalDateTime
	 *                         end, Integer myDrugId) throws APIException;
	 * @Transactional(readOnly = true) List<VenteDrug> getAllDrugAvarieByEntrepot(LocalDateTime
	 *                         start, LocalDateTime end) throws APIException;
	 * @Transactional(readOnly = true) List<Approvisionnement>
	 *                         getAllDrugApprovisByEntrepot(LocalDateTime start, LocalDateTime end)
	 *                         throws APIException;
	 * @Transactional(readOnly = true) List<VenteDrug>
	 *                         getAllDrugApprovisByEntrepotAndDrug(LocalDateTime start,
	 *                         LocalDateTime end, Integer myDrugId) throws APIException;
	 **/
	@Authorized(MycashierConfig.MODULE_PRIVILEGE)
	@Transactional
	Entrepot saveEntrepot(Entrepot entrepot) throws APIException;
	
	@Authorized(MycashierConfig.MODULE_PRIVILEGE)
	@Transactional
	Entrepot deleteEntrepot(Entrepot entrepot) throws APIException;
}
