package org.openmrs.module.mycashier.api.dao;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.openmrs.api.db.hibernate.DbSession;
import org.openmrs.api.db.hibernate.DbSessionFactory;
import org.openmrs.module.mycashier.MyDrugAvarie;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository("mycashier.MyDrugAvarieDao")
public class MyDrugAvarieDao {
	
	@Autowired
	private DbSessionFactory sessionFactory;
	
	public DbSessionFactory getSessionFactory() {
		return sessionFactory;
	}
	
	public void setSessionFactory(DbSessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}
	
	public MyDrugAvarie getMyDrugAvarieByUuid(String uuid) {
		DbSession session = sessionFactory.getCurrentSession();
		Criteria criteria = session.createCriteria(MyDrugAvarie.class);
		criteria.add(Restrictions.eq("uuid", uuid));
		return (MyDrugAvarie) criteria.uniqueResult();
	}
	
	public MyDrugAvarie getMyDrugAvarieById(Integer myDrugAvarieId) {
		DbSession session = sessionFactory.getCurrentSession();
		Criteria criteria = session.createCriteria(MyDrugAvarie.class);
		criteria.add(Restrictions.eq("id", myDrugAvarieId));
		return (MyDrugAvarie) criteria.uniqueResult();
	}
	
	public List<MyDrugAvarie> getAllMyDrugAvaries() {
		DbSession session = sessionFactory.getCurrentSession();
		Criteria criteria = session.createCriteria(MyDrugAvarie.class);
		return criteria.list();
	}
	
	public List<MyDrugAvarie> getAllMyDrugAvariesByEntrepot(Integer entrepotId) {
		DbSession session = sessionFactory.getCurrentSession();
		Criteria criteria = session.createCriteria(MyDrugAvarie.class);
		criteria.add(Restrictions.eq("entrepot.id", entrepotId));
		return criteria.list();
	}
	
	public List<MyDrugAvarie> getAllMyDrugAvariesByDrug(Integer myDrugId) {
		DbSession session = sessionFactory.getCurrentSession();
		Criteria criteria = session.createCriteria(MyDrugAvarie.class);
		criteria.add(Restrictions.eq("myDrug.id", myDrugId));
		return criteria.list();
	}
	
	public List<MyDrugAvarie> getAllMyDrugAvariesByEntrepotAndDrug(Integer myDrugId, Integer entrepotId) {
		DbSession session = sessionFactory.getCurrentSession();
		Criteria criteria = session.createCriteria(MyDrugAvarie.class);
		criteria.add(Restrictions.eq("myDrug.id", myDrugId));
		criteria.add(Restrictions.eq("entrepot.id", entrepotId));
		return criteria.list();
	}
	
	public MyDrugAvarie saveMyDrugAvarie(MyDrugAvarie myDrugAvarie) {
		DbSession session = sessionFactory.getCurrentSession();
		session.saveOrUpdate(myDrugAvarie);
		return myDrugAvarie;
	}
	
	public MyDrugAvarie deleteMyDrugAvarie(MyDrugAvarie myDrugAvarie) {
		DbSession session = sessionFactory.getCurrentSession();
		session.delete(myDrugAvarie);
		return myDrugAvarie;
	}
	
}
