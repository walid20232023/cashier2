package org.openmrs.module.mycashier.api.impl;

import org.openmrs.api.APIException;
import org.openmrs.api.impl.BaseOpenmrsService;
import org.openmrs.module.mycashier.MyService;
import org.openmrs.module.mycashier.MyService;
import org.openmrs.module.mycashier.TypeService;
import org.openmrs.module.mycashier.api.ServService;
import org.openmrs.module.mycashier.api.dao.ProvisionDao;
import org.openmrs.module.mycashier.api.dao.ServiceDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("servService")
public class ServServiceImpl implements ServService {
	
	@Autowired
	ServiceDao dao;
	
	public void setDao(ServiceDao dao) {
		this.dao = dao;
	}
	
	@Override
	public MyService getServiceByUuid(String uuid) throws APIException {
		return dao.getServiceByUuid(uuid);
	}
	
	@Override
	public MyService getServiceById(Integer serviceId) throws APIException {
		return dao.getServiceById(serviceId);
	}
	
	@Override
	public MyService getServiceByName(String serviceName) throws APIException {
		return dao.getServiceByName(serviceName);
	}
	
	@Override
	public List<MyService> getAllServices() throws APIException {
		return dao.getAllServices();
	}
	
	@Override
	public List<MyService> getAllServicesByTypeService(Integer typeServiceId) throws APIException {
		return dao.getAllServicesByTypeService(typeServiceId);
	}
	
	@Override
	public MyService saveService(MyService service) throws APIException {
		return dao.saveService(service);
	}
	
	@Override
	public MyService deleteService(MyService service) throws APIException {
		return dao.deleteService(service);
	}
	
	@Override
	public TypeService getTypeServiceByUuid(String uuid) throws APIException {
		return dao.getTypeServiceByUuid(uuid);
	}
	
	@Override
	public TypeService getTypeServiceById(Integer typeServiceId) throws APIException {
		return dao.getTypeServiceById(typeServiceId);
	}
	
	@Override
	public List<TypeService> getAllTypeServices() throws APIException {
		return dao.getAllTypeServices();
	}
	
	@Override
	public TypeService saveTypeService(TypeService typeService) throws APIException {
		return dao.saveTypeService(typeService);
	}
	
	@Override
	public TypeService deleteTypeService(TypeService typeService) throws APIException {
		return dao.deleteTypeService(typeService);
	}
}
