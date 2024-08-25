package org.openmrs.module.mycashier.api.impl;

import org.openmrs.api.APIException;
import org.openmrs.api.impl.BaseOpenmrsService;
import org.openmrs.module.mycashier.Assurance;
import org.openmrs.module.mycashier.api.AssuranceService;
//import org.openmrs.module.mycashier.api.dao.ApprovisionnementDao;
import org.openmrs.module.mycashier.api.dao.AssuranceDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("assuranceService")
public class AssuranceServiceImpl implements AssuranceService {
	
	@Autowired
	AssuranceDao dao;
	
	public void setDao(AssuranceDao dao) {
		this.dao = dao;
	}
	
	@Override
	public Assurance getAssuranceByUuid(String uuid) throws APIException {
		return dao.getAssuranceByUuid(uuid);
	}
	
	@Override
	public Assurance getAssuranceById(Integer assuranceId) throws APIException {
		return dao.getAssuranceById(assuranceId);
	}
	
	@Override
	public List<Assurance> getAllAssurances() throws APIException {
		return dao.getAllAssurances();
	}
	
	@Override
	public List<Assurance> getAllClientsByAssurance(Assurance assurance) throws APIException {
		return null;
	}
	
	/**
	 * @Override public List<Assurance> getAllClientsByAssurance(Assurance assurance) throws
	 *           APIException { return dao.getAllClientsByAssurance(assurance); }
	 **/
	@Override
	public Assurance saveAssurance(Assurance assurance) throws APIException {
		return dao.saveAssurance(assurance);
	}
	
	@Override
	public Assurance deleteAssurance(Assurance assurance) throws APIException {
		return dao.deleteAssurance(assurance);
	}
}
