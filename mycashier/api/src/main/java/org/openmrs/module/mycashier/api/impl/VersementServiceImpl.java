package org.openmrs.module.mycashier.api.impl;

import org.openmrs.api.APIException;
import org.openmrs.api.impl.BaseOpenmrsService;
import org.openmrs.module.mycashier.Versement;
import org.openmrs.module.mycashier.api.VersementService;
import org.openmrs.module.mycashier.api.dao.VersementDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service("versementService")
public class VersementServiceImpl implements VersementService {
	
	@Autowired
	VersementDao dao;
	
	public void setDao(VersementDao dao) {
		this.dao = dao;
	}
	
	@Override
	public Versement getVersementByUuid(String uuid) throws APIException {
		return dao.getVersementByUuid(uuid);
	}
	
	@Override
	public Versement getVersementById(Integer versementId) throws APIException {
		return dao.getVersementById(versementId);
	}
	
	@Override
	public List<Versement> getAllVersements(LocalDateTime start, LocalDateTime end) throws APIException {
		return dao.getAllVersements(start, end);
	}
	
	@Override
	public List<Versement> getAllVersementsByAgentSource(Integer agentSourceId, LocalDateTime start, LocalDateTime end)
	        throws APIException {
		return dao.getAllVersementsByAgentSource(agentSourceId, start, end);
	}
	
	@Override
	public List<Versement> getAllVersementsByAgentCible(Integer agentCibleId, LocalDateTime start, LocalDateTime end)
	        throws APIException {
		return dao.getAllVersementsByAgentCible(agentCibleId, start, end);
	}
	
	@Override
	public Versement saveVersement(Versement versement) throws APIException {
		return dao.saveVersement(versement);
	}
	
	@Override
	public Versement deleteVersement(Versement versement, String motif) throws APIException {
		return dao.deleteVersement(versement, motif);
	}
}
