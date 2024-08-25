package org.openmrs.module.commonlabtest.api.impl;

import org.openmrs.api.APIException;
import org.openmrs.module.commonlabtest.api.MyLabTestService;
import org.openmrs.module.commonlabtest.api.dao.MyLabTestDAO;
import org.openmrs.module.mylabtest.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service("myLabTestService")
public class MyLabTestServiceImpl implements MyLabTestService {

	@Autowired
	MyLabTestDAO dao;

	public void setDao(MyLabTestDAO dao) {
		this.dao = dao;
	}

	@Override
	@Transactional
	public MyLabTestTypeAttr saveLabTestAttr(MyLabTestTypeAttr myLabTestTypeAttr) throws APIException {

		return dao.saveLabTestAttr(myLabTestTypeAttr);

	}

	@Override
	@Transactional
	public MyLabTestTypeAttr getLabTestAttributeByUuid(String uuid) throws APIException {
		return dao.getLabTestAttributeByUuid(uuid);
	}

	@Override
	@Transactional
	public List<MyLabTestTypeAttr> getAllLabTestAttributes() throws APIException {
		return dao.getAllLabTestAttributes();
	}

	@Override
	@Transactional
	public MyLabTestType saveMyLabTestType(MyLabTestType myLabTestType) throws APIException {
		return dao.saveTestType(myLabTestType);
	}

	@Override
	@Transactional
	public TestResult saveTestResult(TestResult testResult) throws APIException {
		return dao.saveTestResult(testResult);
	}

	@Override
	@Transactional
	public TestResultAttribute saveTestResultAttribute(TestResultAttribute testResultAttribute) throws APIException {
		return dao.saveTestResultAttribute(testResultAttribute);
	}

	@Override
	@Transactional
	public TestResult getTestResultById(Integer resultId) throws APIException {
		return dao.getTestResultById(resultId);
	}

	@Override
	@Transactional
	public TestResultAttribute getTestResultAttrById(Integer testResultAttrId) throws APIException {
		return dao.getTestResultAttrById(testResultAttrId);
	}

	@Override
	@Transactional
	public MyLabTestType getMyLabTestTypeById(Integer id) throws APIException {
		return dao.getTestTypeById(id);
	}

	@Override
	@Transactional
	public List<MyLabTestType> getAllMyLabTestTypes() {
		return dao.getAllLabTestTypes();
	}

	@Override
	@Transactional
	public MyLabTestTypeAttr getLabTestAttributeById(Integer id) {
		return dao.getLabTestAttributeById(id);
	}

	@Override
	@Transactional
	public List<MyLabTestType> getAllLabTestTypes() throws APIException {
		return dao.getAllLabTestTypes();
	}

	@Override
	@Transactional
	public LabTestOrder saveLabTestOrder(LabTestOrder labTestOrder) throws APIException {
		return dao.saveLabTestOrder(labTestOrder);
	}

	@Override
	public List<LabTestOrder> getLabTestOrdersByPatientId(Integer patientId) throws APIException {
		return dao.getLabTestOrdersByPatientId(patientId);
	}

	@Override
	@Transactional
	public LabTestOrder getLabTestOrderById(Integer labTestOrderId) throws APIException {
		return dao.getLabTestOrderById(labTestOrderId);
	}

}
