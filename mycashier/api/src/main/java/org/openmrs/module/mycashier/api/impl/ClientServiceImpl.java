package org.openmrs.module.mycashier.api.impl;

import org.openmrs.api.APIException;
import org.openmrs.api.impl.BaseOpenmrsService;
import org.openmrs.module.mycashier.Assurance;
import org.openmrs.module.mycashier.Client;
import org.openmrs.module.mycashier.api.ClientService;
import org.openmrs.module.mycashier.api.dao.AssuranceDao;
import org.openmrs.module.mycashier.api.dao.ClientDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service("clientService")
public class ClientServiceImpl implements ClientService {
	
	@Autowired
	ClientDao dao;
	
	public void setDao(ClientDao dao) {
		this.dao = dao;
	}
	
	@Override
	public Client getClientByUuid(String uuid) throws APIException {
		return dao.getClientByUuid(uuid);
	}
	
	@Override
	public Client getClientById(Integer clientId) throws APIException {
		return dao.getClientById(clientId);
	}
	
	@Override
	public Client saveClient(Client client) throws APIException {
		return dao.saveClient(client);
	}
	
	@Override
	public void addAssuranceToClient(Assurance assurance, Client client) throws APIException {
		dao.addAssuranceToClient(assurance, client);
	}
	
	@Override
	public void deleteAssuranceFromClient(Assurance assurance, Client client) throws APIException {
		dao.deleteAssuranceFromClient(assurance, client);
	}
	
	@Override
	public Client deleteClient(Client client) throws APIException {
		return dao.deleteClient(client);
	}
	
	@Override
	public List<Assurance> getAssurancesByClient(Client client) throws APIException {
		return dao.getAssurancesByClient(client);
	}
	
	@Override
	public List<Client> getAllClients() throws APIException {
		return dao.getAllClients();
	}
	
	@Override
	public List<Client> searchClients(String query) throws APIException {
		return dao.searchClients(query);
	}
}
