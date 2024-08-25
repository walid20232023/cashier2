package org.openmrs.module.mycashier.api.dao;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.openmrs.api.db.hibernate.DbSession;
import org.openmrs.api.db.hibernate.DbSessionFactory;
import org.openmrs.module.mycashier.Versement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Repository("mycashier.VersementDao")
public class VersementDao {
	
	@Autowired
	private DbSessionFactory sessionFactory;
	
	public DbSessionFactory getSessionFactory() {
		return sessionFactory;
	}
	
	public void setSessionFactory(DbSessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}
	
	// Retrieve a Versement by UUID
	public Versement getVersementByUuid(String uuid) {
		DbSession session = sessionFactory.getCurrentSession();
		Criteria criteria = session.createCriteria(Versement.class);
		criteria.add(Restrictions.eq("uuid", uuid));
		return (Versement) criteria.uniqueResult();
	}
	
	// Retrieve a Versement by ID
	public Versement getVersementById(Integer versementId) {
		DbSession session = sessionFactory.getCurrentSession();
		Criteria criteria = session.createCriteria(Versement.class);
		criteria.add(Restrictions.eq("id", versementId));
		return (Versement) criteria.uniqueResult();
	}
	
	// Retrieve all Versements within a date range
	public List<Versement> getAllVersements(LocalDateTime start, LocalDateTime end) {
		DbSession session = sessionFactory.getCurrentSession();
		Criteria criteria = session.createCriteria(Versement.class);
		criteria.add(Restrictions.between("dateVersement", start, end));
		return criteria.list();
	}
	
	// Retrieve all Versements by source agent within a date range
	public List<Versement> getAllVersementsByAgentSource(Integer agentSourceId, LocalDateTime start, LocalDateTime end) {
		DbSession session = sessionFactory.getCurrentSession();
		Criteria criteria = session.createCriteria(Versement.class);
		criteria.add(Restrictions.eq("caisseAgentSource.id", agentSourceId));
		criteria.add(Restrictions.between("dateVersement", start, end));
		return criteria.list();
	}
	
	// Retrieve all Versements by target agent within a date range
	public List<Versement> getAllVersementsByAgentCible(Integer agentCibleId, LocalDateTime start, LocalDateTime end) {
		DbSession session = sessionFactory.getCurrentSession();
		Criteria criteria = session.createCriteria(Versement.class);
		criteria.add(Restrictions.eq("caisseAgentCible.id", agentCibleId));
		criteria.add(Restrictions.between("dateVersement", start, end));
		return criteria.list();
	}
	
	// Save a Versement@Transactional
	@Transactional
	public Versement saveVersement(Versement versement) {
		DbSession session = sessionFactory.getCurrentSession();
		session.saveOrUpdate(versement);
		return versement;
	}
	
	// Delete a Versement
	public Versement deleteVersement(Versement versement, String motif) {
		DbSession session = sessionFactory.getCurrentSession();
		session.beginTransaction();
		session.delete(versement);
		session.getTransaction().commit();
		return versement;
	}
}
