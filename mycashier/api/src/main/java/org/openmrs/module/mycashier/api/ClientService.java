package org.openmrs.module.mycashier.api;

import org.openmrs.annotation.Authorized;
import org.openmrs.api.APIException;
import org.openmrs.api.OpenmrsService;
import org.openmrs.module.mycashier.Assurance;
import org.openmrs.module.mycashier.Client;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

@Component
@Service
public interface ClientService {
	
	@Transactional(readOnly = true)
	Client getClientByUuid(String uuid) throws APIException;
	
	@Transactional(readOnly = true)
	Client getClientById(Integer clientId) throws APIException;
	
	@Transactional
	Client saveClient(Client client) throws APIException;
	
	@Transactional
	void addAssuranceToClient(Assurance assurance, Client client) throws APIException;
	
	@Transactional
	void deleteAssuranceFromClient(Assurance assurance, Client client) throws APIException;
	
	@Transactional
	Client deleteClient(Client client) throws APIException;
	
	@Transactional(readOnly = true)
	List<Assurance> getAssurancesByClient(Client client) throws APIException;
	
	@Transactional(readOnly = true)
	List<Client> getAllClients() throws APIException;
	
	@Transactional(readOnly = true)
	List<Client> searchClients(String query) throws APIException;
}
