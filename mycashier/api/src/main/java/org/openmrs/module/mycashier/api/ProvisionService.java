package org.openmrs.module.mycashier.api;

import org.openmrs.annotation.Authorized;
import org.openmrs.api.APIException;
import org.openmrs.api.OpenmrsService;
import org.openmrs.module.mycashier.MycashierConfig;
import org.openmrs.module.mycashier.Provision;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Component
@Service
public interface ProvisionService {
	
	@Transactional(readOnly = true)
	Provision getProvisionByUuid(String uuid) throws APIException;
	
	@Transactional(readOnly = true)
	Provision getProvisionById(Integer provisionId) throws APIException;
	
	@Transactional(readOnly = true)
	List<Provision> getAllProvisions() throws APIException;
	
	@Authorized(MycashierConfig.MODULE_PRIVILEGE)
	@Transactional
	Provision saveProvision(Provision provision) throws APIException;
	
	@Authorized(MycashierConfig.MODULE_PRIVILEGE)
	@Transactional
	Provision deleteProvision(Provision provision) throws APIException;
}
