package org.openmrs.module.mycashier.api;

import org.openmrs.annotation.Authorized;
import org.openmrs.api.APIException;
import org.openmrs.module.mycashier.MyService;
import org.openmrs.module.mycashier.MycashierConfig;
import org.openmrs.module.mycashier.TypeService;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Component
@Service
public interface ServService {
	
	@Transactional(readOnly = true)
	MyService getServiceByUuid(String uuid) throws APIException;
	
	@Transactional(readOnly = true)
	MyService getServiceById(Integer serviceId) throws APIException;
	
	@Transactional(readOnly = true)
	MyService getServiceByName(String serviceName) throws APIException;
	
	@Transactional(readOnly = true)
	List<MyService> getAllServices() throws APIException;
	
	@Transactional(readOnly = true)
	List<MyService> getAllServicesByTypeService(Integer typeServiceId) throws APIException;
	
	@Authorized(MycashierConfig.MODULE_PRIVILEGE)
	@Transactional
	MyService saveService(MyService service) throws APIException;
	
	@Authorized(MycashierConfig.MODULE_PRIVILEGE)
	@Transactional
	MyService deleteService(MyService service) throws APIException;
	
	@Transactional(readOnly = true)
	TypeService getTypeServiceByUuid(String uuid) throws APIException;
	
	@Transactional(readOnly = true)
	TypeService getTypeServiceById(Integer typeServiceId) throws APIException;
	
	@Transactional(readOnly = true)
	List<TypeService> getAllTypeServices() throws APIException;
	
	@Authorized(MycashierConfig.MODULE_PRIVILEGE)
	@Transactional
	TypeService saveTypeService(TypeService typeService) throws APIException;
	
	@Authorized(MycashierConfig.MODULE_PRIVILEGE)
	@Transactional
	TypeService deleteTypeService(TypeService typeService) throws APIException;
}
