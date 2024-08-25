package org.openmrs.module.mycashier.api;

import org.openmrs.User;
import org.openmrs.annotation.Authorized;
import org.openmrs.api.APIException;
import org.openmrs.api.OpenmrsService;
import org.openmrs.module.mycashier.Agent;
//import org.openmrs.module.mycashier.Approvisionnement;
import org.openmrs.module.mycashier.MycashierConfig;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Component
@Service
public interface AgentService {
	
	@Transactional(readOnly = true)
	Agent getAgentByUuid(String uuid) throws APIException;
	
	@Transactional(readOnly = true)
	Agent getAgentById(Integer agentId) throws APIException;


	@Transactional(readOnly = true)
	Agent getAgentByUserId(User user) throws APIException;
	
	@Transactional(readOnly = true)
	List<Agent> getAllAgents() throws APIException;
	
	@Authorized(MycashierConfig.MODULE_PRIVILEGE)
	@Transactional
	Agent saveAgent(Agent agent) throws APIException;
	
	@Authorized()
	@Transactional
	Agent deleteAgent(Agent agent) throws APIException;
	
}
