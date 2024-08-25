package org.openmrs.module.commonlabtest.api.dao.impl;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.openmrs.api.APIException;
import org.openmrs.module.commonlabtest.api.dao.MyLabTestDAO;
import org.openmrs.module.mylabtest.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Component
public class MyLabTestDAOImpl implements MyLabTestDAO {

	@Autowired
	private SessionFactory sessionFactory;

	public SessionFactory getSessionFactory() {
		return sessionFactory;
	}

	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}

	@Override
	public MyLabTestTypeAttr saveLabTestAttr(MyLabTestTypeAttr myLabTestAttr) {

		sessionFactory.getCurrentSession().saveOrUpdate(myLabTestAttr);
		return myLabTestAttr;
	}

	@Override
	public MyLabTestTypeAttr getLabTestAttributeByUuid(String uuid) {
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(MyLabTestTypeAttr.class);
		criteria.add(Restrictions.eq("uuid", uuid.toLowerCase()));
		return (MyLabTestTypeAttr) criteria.uniqueResult();
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<MyLabTestTypeAttr> getAllLabTestAttributes() {
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(MyLabTestTypeAttr.class);
		criteria.addOrder(Order.asc("name")); // Tri par nom, changez selon votre besoin
		return (List<MyLabTestTypeAttr>) criteria.list();
	}

	@Override
	public MyLabTestType saveTestType(MyLabTestType myLabTestType) {
		sessionFactory.getCurrentSession().saveOrUpdate(myLabTestType);
		return myLabTestType;
	}

	@Override
	public MyLabTestType getTestTypeById(Integer id) {
		return (MyLabTestType) sessionFactory.getCurrentSession().get(MyLabTestType.class, id);
	}

	@Override
	public List<MyLabTestType> getAllLabTestTypes() {
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(MyLabTestType.class);
		criteria.addOrder(Order.asc("name")); // Tri par nom, changez selon votre besoin
		return (List<MyLabTestType>) criteria.list();
	}

	@Override
	public MyLabTestTypeAttr getLabTestAttributeById(Integer id) {
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(MyLabTestTypeAttr.class);
		criteria.add(Restrictions.eq("id", id));
		return (MyLabTestTypeAttr) criteria.uniqueResult();
	}

	@Override
	public LabTestOrder saveLabTestOrder(LabTestOrder labTestOrder) {
		sessionFactory.getCurrentSession().saveOrUpdate(labTestOrder);
		return labTestOrder;
	}

	@Override
	public List<LabTestOrder> getLabTestOrdersByPatientId(Integer patientId) throws APIException {
		Session session = sessionFactory.getCurrentSession();
		Criteria criteria = session.createCriteria(LabTestOrder.class);
		criteria.createAlias("patient", "p");
		criteria.add(Restrictions.eq("p.patientId", patientId));
		criteria.addOrder(Order.desc("datetime"));
		return criteria.list();
	}

	@Override
	public TestResult saveTestResult(TestResult testResult) throws APIException {
		sessionFactory.getCurrentSession().saveOrUpdate(testResult);
		return testResult;
	}

	@Override
	public TestResultAttribute saveTestResultAttribute(TestResultAttribute testResultAttribute) throws APIException {
		sessionFactory.getCurrentSession().saveOrUpdate(testResultAttribute);
		return testResultAttribute;
	}

	@Override
	public TestResult getTestResultById(Integer resultId) throws APIException {
		try {
			return (TestResult) sessionFactory.getCurrentSession().get(TestResult.class, resultId);
		}
		catch (Exception e) {
			throw new APIException("Error fetching TestResult by ID: " + resultId, e);
		}
	}

	@Override
	public TestResultAttribute getTestResultAttrById(Integer testResultAttrId) throws APIException {
		try {
			return (TestResultAttribute) sessionFactory.getCurrentSession().get(TestResultAttribute.class, testResultAttrId);
		}
		catch (Exception e) {
			throw new APIException("Error fetching TestResultAttribute by ID: " + testResultAttrId, e);
		}

	}

	@Override
	public LabTestOrder getLabTestOrderById(Integer labTestOrderId) throws APIException {
		try {
			return (LabTestOrder) sessionFactory.getCurrentSession().get(LabTestOrder.class, labTestOrderId);
		}
		catch (Exception e) {
			throw new APIException("Error fetching LabTestOrder by ID: " + labTestOrderId, e);
		}
	}

}
