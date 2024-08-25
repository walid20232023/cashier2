package org.openmrs.module.commonlabtest.api;

import org.openmrs.api.APIException;
import org.openmrs.logic.op.In;
import org.openmrs.module.commonlabtest.LabTestAttribute;
import org.openmrs.module.commonlabtest.LabTestAttributeType;
import org.openmrs.module.mylabtest.*;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.List;

@Component
public interface MyLabTestService {

	MyLabTestTypeAttr saveLabTestAttr(MyLabTestTypeAttr myLabTestTypeAttr) throws APIException;

	MyLabTestTypeAttr getLabTestAttributeByUuid(String uuid) throws APIException;

	List<MyLabTestTypeAttr> getAllLabTestAttributes() throws APIException;

	MyLabTestType saveMyLabTestType(MyLabTestType myLabTestType) throws APIException;

	TestResult saveTestResult(TestResult testResult) throws APIException;

	TestResultAttribute saveTestResultAttribute(TestResultAttribute testResultAttribute) throws APIException;

	TestResult getTestResultById(Integer resultId) throws APIException;

	TestResultAttribute getTestResultAttrById(Integer testResultAttrId) throws APIException;

	MyLabTestType getMyLabTestTypeById(Integer id) throws APIException;

	List<MyLabTestType> getAllMyLabTestTypes();

	MyLabTestTypeAttr getLabTestAttributeById(Integer id);

	List<MyLabTestType> getAllLabTestTypes() throws APIException;

	LabTestOrder saveLabTestOrder(LabTestOrder labTestOrder) throws APIException;

	List<LabTestOrder> getLabTestOrdersByPatientId(Integer patientId) throws APIException;

	LabTestOrder getLabTestOrderById(Integer labTestOrderId) throws APIException;
}
