package org.openmrs.module.mycashier.api.dao;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Disjunction;
import org.hibernate.criterion.Restrictions;
import org.openmrs.api.db.hibernate.DbSession;
import org.openmrs.api.db.hibernate.DbSessionFactory;
import org.openmrs.module.mycashier.Emballage;
import org.openmrs.module.mycashier.MyDrug;

import org.openmrs.module.mycashier.MyDrugEmballage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository("mycashier.MyDrugDao")
public class MyDrugDao {
	
	@Autowired
	private DbSessionFactory sessionFactory;
	
	public DbSessionFactory getSessionFactory() {
		return sessionFactory;
	}
	
	public void setSessionFactory(DbSessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}
	
	@Transactional
	public MyDrug getMyDrugByUuid(String uuid) {
		DbSession session = sessionFactory.getCurrentSession();
		Criteria criteria = session.createCriteria(MyDrug.class);
		criteria.add(Restrictions.eq("uuid", uuid));
		return (MyDrug) criteria.uniqueResult();
	}
	
	@Transactional
	public MyDrug getMyDrugById(Integer myDrugId) {
		DbSession session = sessionFactory.getCurrentSession();
		Criteria criteria = session.createCriteria(MyDrug.class);
		criteria.add(Restrictions.eq("id", myDrugId));
		return (MyDrug) criteria.uniqueResult();
	}
	
	@Transactional
	public List<MyDrug> getAllMyDrugs() {
		DbSession session = sessionFactory.getCurrentSession();
		Criteria criteria = session.createCriteria(MyDrug.class);
		return criteria.list();
	}
	
	@Transactional
	public List<MyDrug> getAllRetiredMyDrugs() {
		DbSession session = sessionFactory.getCurrentSession();
		Criteria criteria = session.createCriteria(MyDrug.class);
		criteria.add(Restrictions.eq("retired", 1)); // Assuming retired is marked with 1
		return criteria.list();
	}
	
	@Transactional
	public MyDrug saveMyDrug(MyDrug myDrug) {
		DbSession session = sessionFactory.getCurrentSession();
		session.saveOrUpdate(myDrug);
		return myDrug;
	}
	
	@Transactional
	public void saveMyDrugEmballageUnits(Integer emballageId, Integer drugId, Integer units) {
		DbSession session = sessionFactory.getCurrentSession();
		
		// Check if MyDrugEmballage entry exists
		Criteria criteria = session.createCriteria(MyDrugEmballage.class);
		criteria.createAlias("myDrug", "md");
		criteria.createAlias("emballage", "e");
		criteria.add(Restrictions.eq("md.id", drugId));
		criteria.add(Restrictions.eq("e.id", emballageId));
		
		MyDrugEmballage myDrugEmballage = (MyDrugEmballage) criteria.uniqueResult();
		
		if (myDrugEmballage != null) {
			// Update unit number
			myDrugEmballage.setUnitNumber(units);
			session.update(myDrugEmballage);
		} else {
			// Create new MyDrugEmballage entry
			MyDrug myDrug = (MyDrug) session.get(MyDrug.class, drugId);
			Emballage emballage = (Emballage) session.get(Emballage.class, emballageId);
			
			if (myDrug == null || emballage == null) {
				throw new IllegalArgumentException("Invalid MyDrug or Emballage ID");
			}
			
			MyDrugEmballage newMyDrugEmballage = new MyDrugEmballage();
			newMyDrugEmballage.setId(new MyDrugEmballage.MyDrugEmballageId(drugId, emballageId));
			newMyDrugEmballage.setMyDrug(myDrug);
			newMyDrugEmballage.setEmballage(emballage);
			newMyDrugEmballage.setUnitNumber(units);
			
			session.save(newMyDrugEmballage);
		}
	}
	
	@Transactional
	public Integer getMyDrugEmballageUnits(Emballage emballage, MyDrug myDrug) {
		DbSession session = sessionFactory.getCurrentSession();
		
		// Create Criteria to find MyDrugEmballage entry for the given Emballage and MyDrug
		Criteria criteria = session.createCriteria(MyDrugEmballage.class);
		criteria.add(Restrictions.eq("emballage", emballage));
		criteria.add(Restrictions.eq("myDrug", myDrug));
		
		MyDrugEmballage myDrugEmballage = (MyDrugEmballage) criteria.uniqueResult();
		
		// Return the unit number or null if not found
		return (myDrugEmballage != null) ? myDrugEmballage.getUnitNumber() : null;
	}
	
	public MyDrug deleteMyDrug(MyDrug myDrug) {
		DbSession session = sessionFactory.getCurrentSession();
		
		// Delete the MyDrug entity
		session.delete(myDrug);
		
		// Return the deleted entity or null if needed
		return myDrug;
	}
	
	@Transactional
	public List<MyDrug> searchDrugs(String query) {
		// Créer un objet Criteria pour MyDrug
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(MyDrug.class);
		
		// Créer une disjonction pour combiner les conditions OR
		Disjunction disjunction = Restrictions.disjunction();
		disjunction.add(Restrictions.ilike("name", "%" + query + "%"));
		disjunction.add(Restrictions.ilike("dci", "%" + query + "%"));
		disjunction.add(Restrictions.ilike("groupeTherap", "%" + query + "%"));
		
		// Ajouter la disjonction au critère
		criteria.add(disjunction);
		
		// Exécuter la requête et retourner les résultats
		return criteria.list();
	}
	
}
