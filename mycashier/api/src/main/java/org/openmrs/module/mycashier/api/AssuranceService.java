package org.openmrs.module.mycashier.api;

import org.openmrs.annotation.Authorized;
import org.openmrs.api.APIException;
import org.openmrs.api.OpenmrsService;
import org.openmrs.module.mycashier.Assurance;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Component
@Service
public interface AssuranceService {
	
	@Transactional(readOnly = true)
	Assurance getAssuranceByUuid(String uuid) throws APIException;
	
	@Transactional(readOnly = true)
	Assurance getAssuranceById(Integer assuranceId) throws APIException;
	
	@Transactional(readOnly = true)
	List<Assurance> getAllAssurances() throws APIException;
	
	@Transactional(readOnly = true)
	List<Assurance> getAllClientsByAssurance(Assurance assurance) throws APIException;
	
	@Transactional
	Assurance saveAssurance(Assurance assurance) throws APIException;
	
	@Transactional
	Assurance deleteAssurance(Assurance assurance) throws APIException;
}
