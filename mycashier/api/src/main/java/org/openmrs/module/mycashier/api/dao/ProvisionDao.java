package org.openmrs.module.mycashier.api.dao;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.openmrs.api.db.hibernate.DbSession;
import org.openmrs.api.db.hibernate.DbSessionFactory;
import org.openmrs.module.mycashier.Provision;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository("mycashier.ProvisionDao")
public class ProvisionDao {
	
	@Autowired
	private DbSessionFactory sessionFactory;
	
	public DbSessionFactory getSessionFactory() {
		return sessionFactory;
	}
	
	public void setSessionFactory(DbSessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}
	
	// Get Provision by UUID
	public Provision getProvisionByUuid(String uuid) {
		DbSession session = sessionFactory.getCurrentSession();
		Criteria criteria = session.createCriteria(Provision.class);
		criteria.add(Restrictions.eq("uuid", uuid));
		return (Provision) criteria.uniqueResult();
	}
	
	// Get Provision by ID
	public Provision getProvisionById(Integer provisionId) {
		DbSession session = sessionFactory.getCurrentSession();
		Criteria criteria = session.createCriteria(Provision.class);
		criteria.add(Restrictions.eq("id", provisionId));
		return (Provision) criteria.uniqueResult();
	}
	
	// Get All Provisions
	public List<Provision> getAllProvisions() {
		DbSession session = sessionFactory.getCurrentSession();
		Criteria criteria = session.createCriteria(Provision.class);
		return criteria.list();
	}
	
	// Save or Update Provision
	public Provision saveProvision(Provision provision) {
		DbSession session = sessionFactory.getCurrentSession();
		session.saveOrUpdate(provision);
		return provision;
	}
	
	// Delete Provision
	public Provision deleteProvision(Provision provision) {
		DbSession session = sessionFactory.getCurrentSession();
		session.delete(provision);
		return provision;
	}
}
