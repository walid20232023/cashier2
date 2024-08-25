package org.openmrs.module.mycashier.api.impl;

import org.openmrs.api.APIException;
import org.openmrs.api.impl.BaseOpenmrsService;
import org.openmrs.module.mycashier.MyDrugAvarie;
import org.openmrs.module.mycashier.api.MyDrugAvarieService;
import org.openmrs.module.mycashier.api.dao.InventaireDao;
import org.openmrs.module.mycashier.api.dao.MyDrugAvarieDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("myDrugAvarieService")
public class MyDrugAvarieServiceImpl implements MyDrugAvarieService {
	
	@Autowired
	MyDrugAvarieDao dao;
	
	public void setDao(MyDrugAvarieDao dao) {
		this.dao = dao;
	}
	
	@Override
	public MyDrugAvarie getMyDrugAvarieByUuid(String uuid) throws APIException {
		return dao.getMyDrugAvarieByUuid(uuid);
	}
	
	@Override
	public MyDrugAvarie getMyDrugAvarieById(Integer myDrugAvarieId) throws APIException {
		return dao.getMyDrugAvarieById(myDrugAvarieId);
	}
	
	@Override
	public List<MyDrugAvarie> getAllMyDrugAvaries() throws APIException {
		return dao.getAllMyDrugAvaries();
	}
	
	@Override
	public List<MyDrugAvarie> getAllMyDrugAvariesByEntrepot(Integer entrepotId) throws APIException {
		return dao.getAllMyDrugAvariesByEntrepot(entrepotId);
	}
	
	@Override
	public List<MyDrugAvarie> getAllMyDrugAvariesByDrug(Integer mydrugId) throws APIException {
		return dao.getAllMyDrugAvariesByDrug(mydrugId);
	}
	
	@Override
	public List<MyDrugAvarie> getAllMyDrugAvariesByEntrepotAndDrug(Integer mydrugId, Integer entrepotId) throws APIException {
		return dao.getAllMyDrugAvariesByEntrepotAndDrug(mydrugId, entrepotId);
	}
	
	@Override
	public MyDrugAvarie saveMyDrugAvarie(MyDrugAvarie myDrugAvarie) throws APIException {
		return dao.saveMyDrugAvarie(myDrugAvarie);
	}
	
	@Override
	public MyDrugAvarie deleteMyDrugAvarie(MyDrugAvarie myDrugAvarie) throws APIException {
		return dao.deleteMyDrugAvarie(myDrugAvarie);
	}
}
