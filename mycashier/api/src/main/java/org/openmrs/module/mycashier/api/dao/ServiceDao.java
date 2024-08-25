package org.openmrs.module.mycashier.api.dao;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.openmrs.api.db.hibernate.DbSession;
import org.openmrs.api.db.hibernate.DbSessionFactory;
import org.openmrs.module.mycashier.MyService;
import org.openmrs.module.mycashier.TypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository("mycashier.ServiceDao ")
public class ServiceDao {
	
	@Autowired
	private DbSessionFactory sessionFactory;
	
	public DbSessionFactory getSessionFactory() {
		return sessionFactory;
	}
	
	public void setSessionFactory(DbSessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}
	
	// TypeService Methods
	
	public TypeService deleteTypeService(TypeService typeService) {
		DbSession session = sessionFactory.getCurrentSession();
		session.delete(typeService);
		return typeService;
	}
	
	public TypeService saveTypeService(TypeService typeService) {
		DbSession session = sessionFactory.getCurrentSession();
		session.saveOrUpdate(typeService);
		return typeService;
	}
	
	public List<TypeService> getAllTypeServices() {
		DbSession session = sessionFactory.getCurrentSession();
		Criteria criteria = session.createCriteria(TypeService.class);
		return criteria.list();
	}
	
	public TypeService getTypeServiceById(Integer typeServiceId) {
		DbSession session = sessionFactory.getCurrentSession();
		Criteria criteria = session.createCriteria(TypeService.class);
		criteria.add(Restrictions.eq("id", typeServiceId));
		return (TypeService) criteria.uniqueResult();
	}
	
	public TypeService getTypeServiceByUuid(String uuid) {
		DbSession session = sessionFactory.getCurrentSession();
		Criteria criteria = session.createCriteria(TypeService.class);
		criteria.add(Restrictions.eq("uuid", uuid));
		return (TypeService) criteria.uniqueResult();
	}
	
	// Service Methods
	
	public MyService deleteService(MyService service) {
		DbSession session = sessionFactory.getCurrentSession();
		session.delete(service);
		return service;
	}
	
	public MyService saveService(MyService service) {
		DbSession session = sessionFactory.getCurrentSession();
		session.saveOrUpdate(service);
		return service;
	}
	
	public List<MyService> getAllServicesByTypeService(Integer typeServiceId) {
		DbSession session = sessionFactory.getCurrentSession();
		Criteria criteria = session.createCriteria(MyService.class);
		criteria.add(Restrictions.eq("typeService.id", typeServiceId));
		return criteria.list();
	}
	
	public List<MyService> getAllServices() {
		DbSession session = sessionFactory.getCurrentSession();
		Criteria criteria = session.createCriteria(MyService.class);
		return criteria.list();
	}
	
	public MyService getServiceByName(String serviceName) {
		DbSession session = sessionFactory.getCurrentSession();
		Criteria criteria = session.createCriteria(MyService.class);
		criteria.add(Restrictions.eq("name", serviceName));
		return (MyService) criteria.uniqueResult();
	}
	
	public MyService getServiceById(Integer serviceId) {
		DbSession session = sessionFactory.getCurrentSession();
		Criteria criteria = session.createCriteria(MyService.class);
		criteria.add(Restrictions.eq("id", serviceId));
		return (MyService) criteria.uniqueResult();
	}
	
	public MyService getServiceByUuid(String uuid) {
		DbSession session = sessionFactory.getCurrentSession();
		Criteria criteria = session.createCriteria(MyService.class);
		criteria.add(Restrictions.eq("uuid", uuid));
		return (MyService) criteria.uniqueResult();
	}
}
