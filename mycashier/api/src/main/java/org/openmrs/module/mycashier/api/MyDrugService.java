package org.openmrs.module.mycashier.api;

import org.openmrs.annotation.Authorized;
import org.openmrs.api.APIException;
import org.openmrs.api.OpenmrsService;
import org.openmrs.module.mycashier.MycashierConfig;
import org.openmrs.module.mycashier.MyDrug;
import org.openmrs.module.mycashier.Emballage;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Component
@Service
public interface MyDrugService {
	
	@Transactional(readOnly = true)
	MyDrug getMyDrugByUuid(String uuid) throws APIException;
	
	@Transactional(readOnly = true)
	MyDrug getMyDrugById(Integer myDrugId) throws APIException;
	
	@Transactional(readOnly = true)
	List<MyDrug> getAllMyDrugs() throws APIException;
	
	@Transactional(readOnly = true)
	List<MyDrug> getAllRetiredMyDrugs() throws APIException;
	
	@Authorized(MycashierConfig.MODULE_PRIVILEGE)
	@Transactional
	MyDrug saveMyDrug(MyDrug myDrug) throws APIException;
	
	@Transactional
	void saveMyDrugEmballageUnits(Integer emballageId, Integer drugId, Integer units) throws APIException;
	
	@Transactional
	Integer getMyDrugEmballageUnits(Emballage emballage, MyDrug myDrug) throws APIException;
	
	@Authorized(MycashierConfig.MODULE_PRIVILEGE)
	@Transactional
	MyDrug deleteMyDrug(MyDrug myDrug) throws APIException;
	
	@Transactional
	List<MyDrug> searchDrugs(String query);
}
