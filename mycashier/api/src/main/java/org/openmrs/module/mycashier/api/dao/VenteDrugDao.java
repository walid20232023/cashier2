package org.openmrs.module.mycashier.api.dao;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;

import org.openmrs.api.db.hibernate.DbSession;
import org.openmrs.api.db.hibernate.DbSessionFactory;
import org.openmrs.module.mycashier.LigneVenteDrug;
import org.openmrs.module.mycashier.MyDrug;
import org.openmrs.module.mycashier.VenteDrug;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Repository("mycashier.VenteDrugDao")
public class VenteDrugDao {
	
	@Autowired
	private DbSessionFactory sessionFactory;
	
	public DbSessionFactory getSessionFactory() {
		return sessionFactory;
	}
	
	public void setSessionFactory(DbSessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}
	
	@Transactional
	public VenteDrug getVenteDrugByUuid(String uuid) {
		DbSession session = sessionFactory.getCurrentSession();
		Criteria criteria = session.createCriteria(VenteDrug.class);
		criteria.add(Restrictions.eq("uuid", uuid));
		return (VenteDrug) criteria.uniqueResult();
	}
	
	@Transactional
	public VenteDrug getVenteDrugById(Integer venteDrugId) {
		DbSession session = sessionFactory.getCurrentSession();
		Criteria criteria = session.createCriteria(VenteDrug.class);
		criteria.add(Restrictions.eq("id", venteDrugId));
		return (VenteDrug) criteria.uniqueResult();
	}
	
	@Transactional
	public List<VenteDrug> getAllVenteDrugs() {
		DbSession session = sessionFactory.getCurrentSession();
		Criteria criteria = session.createCriteria(VenteDrug.class);
		return criteria.list();
	}
	
	@Transactional
	public List<VenteDrug> getAllVenteDrugs(LocalDateTime start, LocalDateTime end) {
		DbSession session = sessionFactory.getCurrentSession();
		Criteria criteria = session.createCriteria(VenteDrug.class);
		criteria.add(Restrictions.between("dateVente", start, end));
		return criteria.list();
	}
	
	@Transactional
	public List<LigneVenteDrug> getAllLigneVenteDrugsByDrug(Integer myDrugId) {
		DbSession session = sessionFactory.getCurrentSession();
		Criteria criteria = session.createCriteria(LigneVenteDrug.class);
		criteria.add(Restrictions.eq("myDrug.id", myDrugId));
		return criteria.list();
	}
	
	@Transactional
	public VenteDrug saveVenteDrug(VenteDrug venteDrug) {
		DbSession session = sessionFactory.getCurrentSession();
		session.saveOrUpdate(venteDrug);
		return venteDrug;
	}
	
	@Transactional
	public void addLigneToVenteDrug(MyDrug myDrug, VenteDrug venteDrug, Integer quantity) {
		DbSession session = sessionFactory.getCurrentSession();
		
		// Check if LigneVenteDrug entry exists
		Criteria criteria = session.createCriteria(LigneVenteDrug.class);
		criteria.add(Restrictions.eq("id.venteDrugId", venteDrug.getId()));
		criteria.add(Restrictions.eq("id.myDrugId", myDrug.getId()));
		
		LigneVenteDrug ligneVenteDrug = (LigneVenteDrug) criteria.uniqueResult();
		
		if (ligneVenteDrug != null) {
			// Update quantity if the entry already exists
			// Here, you might need to implement a way to update the quantity.
			// For example, you can add a `quantity` field in the LigneVenteDrug class
			// and update it accordingly.
			// Assuming there's a `quantity` field in LigneVenteDrug:
			ligneVenteDrug.setQuantity(quantity);
			session.update(ligneVenteDrug);
		} else {
			// Create new LigneVenteDrug entry if it does not exist
			LigneVenteDrug newLigneVenteDrug = new LigneVenteDrug();
			LigneVenteDrug.LigneVenteDrugId ligneVenteDrugId = new LigneVenteDrug.LigneVenteDrugId(venteDrug.getId(),
			        myDrug.getId());
			newLigneVenteDrug.setId(ligneVenteDrugId);
			newLigneVenteDrug.setVenteDrug(venteDrug);
			newLigneVenteDrug.setMyDrug(myDrug);
			// Assuming there's a `quantity` field in LigneVenteDrug:
			newLigneVenteDrug.setQuantity(quantity);
			
			session.save(newLigneVenteDrug);
		}
	}
	
	@Transactional
	public VenteDrug deleteVenteDrug(VenteDrug venteDrug) {
		DbSession session = sessionFactory.getCurrentSession();
		session.delete(venteDrug);
		return venteDrug;
	}
	
	@Transactional
	public void deleteLigneFromVenteDrug(MyDrug myDrug, VenteDrug venteDrug) {
		DbSession session = sessionFactory.getCurrentSession();
		
		// Create Criteria to find the LigneVenteDrug entry for the given VenteDrug and MyDrug
		Criteria criteria = session.createCriteria(LigneVenteDrug.class);
		criteria.add(Restrictions.eq("id.venteDrugId", venteDrug.getId()));
		criteria.add(Restrictions.eq("id.myDrugId", myDrug.getId()));
		
		LigneVenteDrug ligneVenteDrug = (LigneVenteDrug) criteria.uniqueResult();
		
		// If the entry exists, delete it
		if (ligneVenteDrug != null) {
			session.delete(ligneVenteDrug);
		}
	}
	
}
