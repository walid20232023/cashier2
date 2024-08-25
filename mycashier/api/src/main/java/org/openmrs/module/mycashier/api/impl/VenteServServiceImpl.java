package org.openmrs.module.mycashier.api.impl;

import org.openmrs.api.APIException;
import org.openmrs.module.mycashier.LigneVenteService;

import org.openmrs.module.mycashier.VenteService;
import org.openmrs.module.mycashier.api.VenteServService;
import org.openmrs.module.mycashier.api.dao.VenteServDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("venteServService")
public class VenteServServiceImpl implements VenteServService {
	
	@Autowired
	VenteServDao dao;
	
	public void setDao(VenteServDao dao) {
		this.dao = dao;
	}
	
	@Override
	public VenteService getVenteServiceByUuid(String uuid) throws APIException {
		return dao.getVenteServiceByUuid(uuid);
	}
	
	@Override
	public VenteService getVenteServiceById(Integer venteServiceId) throws APIException {
		return dao.getVenteServiceById(venteServiceId);
	}
	
	@Override
	public List<VenteService> getAllVenteServices() throws APIException {
		return dao.getAllVenteServices();
	}
	
	@Override
	public VenteService saveVenteService(VenteService venteService) throws APIException {
		return dao.saveVenteService(venteService);
	}
	
	@Override
	public void addLigneToVenteService(Integer venteServiceId, Integer serviceId) throws APIException {
		dao.addLigneToVenteService(venteServiceId, serviceId);
	}
	
	@Override
	public VenteService deleteVenteService(VenteService venteService) throws APIException {
		return dao.deleteVenteService(venteService);
	}
	
	@Override
	public LigneVenteService getLigneVenteServiceById(Integer venteServiceId, Integer serviceId) throws APIException {
		return dao.getLigneVenteServiceById(venteServiceId, serviceId);
	}
	
	@Override
	public List<LigneVenteService> getAllLigneVenteServices() throws APIException {
		return dao.getAllLigneVenteServices();
	}
	
	@Override
	public LigneVenteService saveLigneVenteService(LigneVenteService ligneVenteService) throws APIException {
		return dao.saveLigneVenteService(ligneVenteService);
	}
	
	@Override
	public LigneVenteService deleteLigneVenteService(LigneVenteService ligneVenteService) throws APIException {
		return dao.deleteLigneVenteService(ligneVenteService);
	}
}
