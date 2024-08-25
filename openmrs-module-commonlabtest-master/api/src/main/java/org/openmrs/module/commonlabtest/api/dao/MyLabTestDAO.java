package org.openmrs.module.commonlabtest.api.dao;

import org.openmrs.api.APIException;
import org.openmrs.module.commonlabtest.LabTestAttribute;
import org.openmrs.module.mylabtest.*;

import java.util.List;

public interface MyLabTestDAO {

	MyLabTestTypeAttr saveLabTestAttr(MyLabTestTypeAttr myLabTestAttr);

	MyLabTestTypeAttr getLabTestAttributeByUuid(String uuid);

	List<MyLabTestTypeAttr> getAllLabTestAttributes();

	MyLabTestType saveTestType(MyLabTestType myLabTestType) throws APIException;

	MyLabTestType getTestTypeById(Integer id);

	List<MyLabTestType> getAllLabTestTypes();

	MyLabTestTypeAttr getLabTestAttributeById(Integer id);

	LabTestOrder saveLabTestOrder(LabTestOrder labTestOrder);

	List<LabTestOrder> getLabTestOrdersByPatientId(Integer patientId) throws APIException;

	TestResult saveTestResult(TestResult testResult) throws APIException;

	TestResultAttribute saveTestResultAttribute(TestResultAttribute testResultAttribute) throws APIException;

	TestResult getTestResultById(Integer resultId) throws APIException;

	TestResultAttribute getTestResultAttrById(Integer testResultAttrId) throws APIException;

	LabTestOrder getLabTestOrderById(Integer labTestOrderId) throws APIException;
}
