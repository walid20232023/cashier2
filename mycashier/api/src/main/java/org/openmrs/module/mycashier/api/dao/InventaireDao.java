package org.openmrs.module.mycashier.api.dao;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.criterion.Restrictions;
import org.openmrs.api.db.hibernate.DbSession;
import org.openmrs.api.db.hibernate.DbSessionFactory;
import org.openmrs.module.mycashier.Agent;

import org.openmrs.module.mycashier.DrugInventaire;
import org.openmrs.module.mycashier.Inventaire;
import org.openmrs.module.mycashier.MyDrug;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository("mycashier.InventaireDao")
public class InventaireDao {
	
	@Autowired
	private DbSessionFactory sessionFactory;
	
	public DbSessionFactory getSessionFactory() {
		return sessionFactory;
	}
	
	public void setSessionFactory(DbSessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}
	
	@Transactional
	public Inventaire getInventaireByUuid(String uuid) {
		DbSession session = sessionFactory.getCurrentSession();
		
		Criteria criteria = session.createCriteria(Inventaire.class);
		criteria.add(Restrictions.eq("uuid", uuid));
		
		return (Inventaire) criteria.uniqueResult();
	}
	
	@Transactional
	public Inventaire getInventaireById(Integer inventaireId) {
		DbSession session = sessionFactory.getCurrentSession();
		
		Criteria criteria = session.createCriteria(Inventaire.class);
		criteria.add(Restrictions.eq("id", inventaireId));
		
		return (Inventaire) criteria.uniqueResult();
	}
	
	@Transactional
	public List<Inventaire> getAllInventaires() {
		DbSession session = sessionFactory.getCurrentSession();
		
		Criteria criteria = session.createCriteria(Inventaire.class);
		
		return criteria.list();
	}
	
	public Inventaire deleteInventaire(Inventaire inventaire, String motif, Agent agent) {
		return null;
	}
	
	@Transactional
	public Inventaire saveInventaire(Inventaire inventaire) {
		sessionFactory.getCurrentSession().saveOrUpdate(inventaire);
		return inventaire;
	}
	
	public List<DrugInventaire> getAllDrugInventairesByDrug(Integer myDrugId) {
		
		return null;
	}
	
	@Transactional
	public void addDrugInventaire(Inventaire inventaire, MyDrug myDrug, Integer realQuantity, Integer ecart, String motif) {
		DbSession session = sessionFactory.getCurrentSession();
		
		DrugInventaire drugInventaire = new DrugInventaire();
		DrugInventaire.DrugInventaireId id = new DrugInventaire.DrugInventaireId(myDrug.getId(), inventaire.getId());
		
		drugInventaire.setId(id);
		drugInventaire.setInventaire(inventaire);
		drugInventaire.setMyDrug(myDrug);
		drugInventaire.setRealQuantity(realQuantity);
		drugInventaire.setMotif(motif);
		drugInventaire.setEcart(ecart);
		
		session.saveOrUpdate(drugInventaire);
	}
	
	@Transactional
	public void deleteDrugInventaire(Inventaire inventaire, MyDrug myDrug) {
		DbSession session = sessionFactory.getCurrentSession();
		
		DrugInventaire.DrugInventaireId id = new DrugInventaire.DrugInventaireId(myDrug.getId(), inventaire.getId());
		DrugInventaire drugInventaire = (DrugInventaire) session.get(DrugInventaire.class, id);
		
		if (drugInventaire != null) {
			session.delete(drugInventaire);
		}
	}
}
