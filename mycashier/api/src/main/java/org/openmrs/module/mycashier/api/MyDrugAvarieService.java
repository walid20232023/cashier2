package org.openmrs.module.mycashier.api;

import org.openmrs.annotation.Authorized;
import org.openmrs.api.APIException;
import org.openmrs.api.OpenmrsService;
import org.openmrs.module.mycashier.MyDrugAvarie;
import org.openmrs.module.mycashier.MycashierConfig;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Component
@Service
public interface MyDrugAvarieService {
	
	@Transactional(readOnly = true)
	MyDrugAvarie getMyDrugAvarieByUuid(String uuid) throws APIException;
	
	@Transactional(readOnly = true)
	MyDrugAvarie getMyDrugAvarieById(Integer myDrugAvarieId) throws APIException;
	
	@Transactional(readOnly = true)
	List<MyDrugAvarie> getAllMyDrugAvaries() throws APIException;
	
	@Transactional(readOnly = true)
	List<MyDrugAvarie> getAllMyDrugAvariesByEntrepot(Integer entrepotId) throws APIException;
	
	@Transactional(readOnly = true)
	List<MyDrugAvarie> getAllMyDrugAvariesByDrug(Integer mydrugId) throws APIException;
	
	@Transactional(readOnly = true)
	List<MyDrugAvarie> getAllMyDrugAvariesByEntrepotAndDrug(Integer mydrugId, Integer entrepotId) throws APIException;
	
	@Authorized(MycashierConfig.MODULE_PRIVILEGE)
	@Transactional
	MyDrugAvarie saveMyDrugAvarie(MyDrugAvarie myDrugAvarie) throws APIException;
	
	@Authorized(MycashierConfig.MODULE_PRIVILEGE)
	@Transactional
	MyDrugAvarie deleteMyDrugAvarie(MyDrugAvarie myDrugAvarie) throws APIException;
}
