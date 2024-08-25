package org.openmrs.module.mycashier.api.dao;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.openmrs.api.db.hibernate.DbSession;
import org.openmrs.api.db.hibernate.DbSessionFactory;
import org.openmrs.module.mycashier.LigneVenteService;
import org.openmrs.module.mycashier.MyService;
import org.openmrs.module.mycashier.VenteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository("mycashier.VenteServDao")
public class VenteServDao {
	
	@Autowired
	private DbSessionFactory sessionFactory;
	
	public DbSessionFactory getSessionFactory() {
		return sessionFactory;
	}
	
	public void setSessionFactory(DbSessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}
	
	@Transactional
	public VenteService getVenteServiceByUuid(String uuid) {
		// Obtention de la session Hibernate actuelle
		DbSession session = sessionFactory.getCurrentSession();
		
		// Création d'une requête Criteria pour la classe VenteService
		Criteria criteria = session.createCriteria(VenteService.class);
		
		// Ajout d'une restriction pour filtrer par UUID
		criteria.add(Restrictions.eq("uuid", uuid));
		
		// Exécution de la requête et récupération du résultat
		return (VenteService) criteria.uniqueResult();
	}
	
	@Transactional
	public VenteService getVenteServiceById(Integer venteServiceId) {
		// Obtention de la session Hibernate actuelle
		DbSession session = sessionFactory.getCurrentSession();
		
		// Création d'une requête Criteria pour la classe VenteService
		Criteria criteria = session.createCriteria(VenteService.class);
		
		// Ajout d'une restriction pour filtrer par ID
		criteria.add(Restrictions.eq("id", venteServiceId));
		
		// Exécution de la requête et récupération du résultat
		return (VenteService) criteria.uniqueResult();
	}
	
	@Transactional
	public List<VenteService> getAllVenteServices() {
		// Obtention de la session Hibernate actuelle
		DbSession session = sessionFactory.getCurrentSession();
		
		// Création d'une requête Criteria pour la classe VenteService
		Criteria criteria = session.createCriteria(VenteService.class);
		
		// Exécution de la requête et récupération des résultats sous forme de liste
		return criteria.list();
	}
	
	@Transactional
	public VenteService saveVenteService(VenteService venteService) {
		// Obtention de la session Hibernate actuelle
		DbSession session = sessionFactory.getCurrentSession();
		// Sauvegarde ou mise à jour de l'objet VenteService
		session.saveOrUpdate(venteService);
		
		// Retourner l'objet VenteService sauvegardé/ mis à jour
		return venteService;
	}
	
	@Transactional
	public void addLigneToVenteService(Integer venteServiceId, Integer serviceId) {
		DbSession session = sessionFactory.getCurrentSession();
		
		// Check if the LigneVenteService entry exists
		Criteria criteria = session.createCriteria(LigneVenteService.class);
		criteria.add(Restrictions.eq("id.venteServiceId", venteServiceId));
		criteria.add(Restrictions.eq("id.myServiceId", serviceId));
		LigneVenteService existingLigne = (LigneVenteService) criteria.uniqueResult();
		
		if (existingLigne == null) {
			LigneVenteService newLigne = new LigneVenteService();
			LigneVenteService.LigneVenteServiceId id = new LigneVenteService.LigneVenteServiceId(venteServiceId, serviceId);
			newLigne.setId(id);
			newLigne.setVenteService((VenteService) session.load(VenteService.class, venteServiceId));
			newLigne.setMyservice((MyService) session.load(MyService.class, serviceId));
			session.save(newLigne);
		}
	}
	
	@Transactional
	public LigneVenteService getLigneVenteServiceById(Integer venteServiceId, Integer serviceId) {
		DbSession session = sessionFactory.getCurrentSession();
		
		// Création des critères pour la recherche
		Criteria criteria = session.createCriteria(LigneVenteService.class);
		criteria.add(Restrictions.eq("id.venteServiceId", venteServiceId));
		criteria.add(Restrictions.eq("id.myServiceId", serviceId));
		
		// Exécution de la requête et récupération du résultat
		return (LigneVenteService) criteria.uniqueResult();
	}
	
	@Transactional
	public VenteService deleteVenteService(VenteService venteService) {
		DbSession session = sessionFactory.getCurrentSession();
		// Récupération de l'entité VenteService à supprimer
		VenteService existingVenteService = (VenteService) session.get(VenteService.class, venteService.getId());
		
		if (existingVenteService != null) {
			// Suppression de l'entité trouvée
			session.delete(existingVenteService);
		}
		
		// Retourner l'objet supprimé ou null si non trouvé
		return existingVenteService;
	}
	
	@Transactional
	public List<LigneVenteService> getAllLigneVenteServices() {
		DbSession session = sessionFactory.getCurrentSession();
		// Création d'une requête Criteria pour récupérer toutes les lignes de vente de services
		Criteria criteria = session.createCriteria(LigneVenteService.class);
		
		// Exécution de la requête et récupération des résultats
		List<LigneVenteService> lignes = criteria.list();
		
		return lignes;
	}
	
	@Transactional
	public LigneVenteService saveLigneVenteService(LigneVenteService ligneVenteService) {
		DbSession session = sessionFactory.getCurrentSession();
		// Sauvegarde ou mise à jour de l'objet LigneVenteService
		session.saveOrUpdate(ligneVenteService);
		
		return ligneVenteService;
	}
	
	@Transactional
	public LigneVenteService deleteLigneVenteService(LigneVenteService ligneVenteService) {
		DbSession session = sessionFactory.getCurrentSession();
		// Suppression de l'objet LigneVenteService
		session.delete(ligneVenteService);
		
		return ligneVenteService;
	}
	
}
