package org.openmrs.module.mycashier.api;

import org.openmrs.annotation.Authorized;
import org.openmrs.api.APIException;
import org.openmrs.api.OpenmrsService;
import org.openmrs.module.mycashier.Payment;
import org.openmrs.module.mycashier.MycashierConfig;
import org.openmrs.module.mycashier.VenteService;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Component
@Service
public interface PaymentService {
	
	@Transactional(readOnly = true)
	Payment getPaymentByUuid(String uuid) throws APIException;
	
	@Transactional(readOnly = true)
	Payment getPaymentById(Integer paymentId) throws APIException;
	
	@Transactional(readOnly = true)
	List<Payment> getPaymentsByVenteService(Integer venteServiceId) throws APIException;
	
	@Transactional(readOnly = true)
	List<Payment> getPaymentsByVenteDrug(Integer venteDrugId) throws APIException;
	
	@Transactional(readOnly = true)
	List<Payment> getAllPayments() throws APIException;
	
	@Transactional(readOnly = true)
	List<Payment> getAllRetiredPayments() throws APIException;
	
	@Authorized(MycashierConfig.MODULE_PRIVILEGE)
	@Transactional
	Payment savePayment(Payment payment) throws APIException;
	
	@Authorized(MycashierConfig.MODULE_PRIVILEGE)
	@Transactional
	Payment deletePayment(Payment payment) throws APIException;
}
