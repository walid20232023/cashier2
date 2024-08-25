package org.openmrs.module.mycashier.api;

import org.openmrs.annotation.Authorized;
import org.openmrs.api.APIException;
import org.openmrs.api.OpenmrsService;
import org.openmrs.module.mycashier.Emballage;

import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Component
@Service
public interface EmballageService {
	
	@Transactional(readOnly = true)
	Emballage getEmballageByUuid(String uuid) throws APIException;
	
	@Transactional(readOnly = true)
	Emballage getEmballageById(Integer emballageId) throws APIException;
	
	@Transactional(readOnly = true)
	List<Emballage> getAllEmballages() throws APIException;
	
	@Transactional
	Emballage saveEmballage(Emballage emballage) throws APIException;
	
	@Transactional
	Emballage deleteEmballage(Emballage emballage) throws APIException;
}
