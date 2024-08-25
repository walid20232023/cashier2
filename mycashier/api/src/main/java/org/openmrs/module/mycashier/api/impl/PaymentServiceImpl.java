package org.openmrs.module.mycashier.api.impl;

import org.openmrs.api.APIException;
import org.openmrs.api.impl.BaseOpenmrsService;
import org.openmrs.module.mycashier.Payment;
import org.openmrs.module.mycashier.api.PaymentService;
import org.openmrs.module.mycashier.api.dao.MyDrugDao;
import org.openmrs.module.mycashier.api.dao.PaymentDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("paymentService ")
public class PaymentServiceImpl implements PaymentService {
	
	@Autowired
	PaymentDao dao;
	
	public void setDao(PaymentDao dao) {
		this.dao = dao;
	}
	
	@Override
	public Payment getPaymentByUuid(String uuid) throws APIException {
		return dao.getPaymentByUuid(uuid);
	}
	
	@Override
	public Payment getPaymentById(Integer paymentId) throws APIException {
		return dao.getPaymentById(paymentId);
	}
	
	@Override
	public List<Payment> getPaymentsByVenteService(Integer venteServiceId) throws APIException {
		return dao.getPaymentsByVenteService(venteServiceId);
	}
	
	@Override
	public List<Payment> getPaymentsByVenteDrug(Integer venteDrugId) throws APIException {
		return dao.getPaymentsByVenteDrug(venteDrugId);
	}
	
	@Override
	public List<Payment> getAllPayments() throws APIException {
		return dao.getAllPayments();
	}
	
	@Override
	public List<Payment> getAllRetiredPayments() throws APIException {
		return dao.getAllRetiredPayments();
	}
	
	@Override
	public Payment savePayment(Payment payment) throws APIException {
		return dao.savePayment(payment);
	}
	
	@Override
	public Payment deletePayment(Payment payment) throws APIException {
		return dao.deletePayment(payment);
	}
}
