package org.openmrs.module.mycashier.api.dao;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.openmrs.api.db.hibernate.DbSessionFactory;
import org.openmrs.module.mycashier.Emballage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository("mycashier.EmballageDao")
public class EmballageDao {
	
	@Autowired
	private DbSessionFactory sessionFactory;
	
	public DbSessionFactory getSessionFactory() {
		return sessionFactory;
	}
	
	public void setSessionFactory(DbSessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}
	
	@Transactional
	public Emballage getEmballageByUuid(String uuid) {
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(Emballage.class);
		criteria.add(Restrictions.eq("uuid", uuid.toLowerCase()));
		return (Emballage) criteria.uniqueResult();
	}
	
	@Transactional
	public Emballage getEmballageById(Integer emballageId) {
		return (Emballage) sessionFactory.getCurrentSession().get(Emballage.class, emballageId);
	}
	
	@Transactional
	@SuppressWarnings("unchecked")
	public List<Emballage> getAllEmballages() {
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(Emballage.class);
		criteria.addOrder(Order.asc("name")); // Tri par nom, changez selon votre besoin
		return (List<Emballage>) criteria.list();
	}
	
	@Transactional
	public Emballage saveEmballage(Emballage emballage) {
		sessionFactory.getCurrentSession().saveOrUpdate(emballage);
		return emballage;
	}
	
	@Transactional
	public Emballage deleteEmballage(Emballage emballage) {
		sessionFactory.getCurrentSession().delete(emballage);
		return emballage;
	}
}
