package org.openmrs.module.mycashier.api.impl;

import org.openmrs.api.APIException;
import org.openmrs.api.impl.BaseOpenmrsService;
import org.openmrs.module.mycashier.Provision;
import org.openmrs.module.mycashier.api.ProvisionService;
import org.openmrs.module.mycashier.api.dao.ProvisionDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("provisionService")
public class ProvisionServiceImpl implements ProvisionService {
	
	@Autowired
	ProvisionDao dao;
	
	public void setDao(ProvisionDao dao) {
		this.dao = dao;
	}
	
	@Override
	public Provision getProvisionByUuid(String uuid) throws APIException {
		return dao.getProvisionByUuid(uuid);
	}
	
	@Override
	public Provision getProvisionById(Integer provisionId) throws APIException {
		return dao.getProvisionById(provisionId);
	}
	
	@Override
	public List<Provision> getAllProvisions() throws APIException {
		return dao.getAllProvisions();
	}
	
	@Override
	public Provision saveProvision(Provision provision) throws APIException {
		return dao.saveProvision(provision);
	}
	
	@Override
	public Provision deleteProvision(Provision provision) throws APIException {
		return dao.deleteProvision(provision);
	}
}
