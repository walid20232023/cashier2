package org.openmrs.module.mycashier.api.dao;

import org.hibernate.Criteria;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.openmrs.api.db.hibernate.DbSessionFactory;
import org.openmrs.module.mycashier.Agent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository("mycashier.AgentDao")
public class AgentDao {
	
	@Autowired
	private DbSessionFactory sessionFactory;
	
	public DbSessionFactory getSessionFactory() {
		return sessionFactory;
	}
	
	public void setSessionFactory(DbSessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}
	
	@Transactional
	public Agent getAgentByUuid(String uuid) {
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(Agent.class);
		criteria.add(Restrictions.eq("uuid", uuid.toLowerCase()));
		return (Agent) criteria.uniqueResult();
	}
	
	@Transactional
	public Agent getAgentById(Integer agentId) {
		return (Agent) sessionFactory.getCurrentSession().get(Agent.class, agentId);
	}
	
	@Transactional
	public List<Agent> getAllAgents() {
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(Agent.class);
		criteria.addOrder(Order.asc("username")); // Tri par username, changez selon votre besoin
		return (List<Agent>) criteria.list();
	}
	
	@Transactional
	public Agent saveAgent(Agent agent) {
		sessionFactory.getCurrentSession().saveOrUpdate(agent);
		return agent;
	}
	
	@Transactional
	public Agent deleteAgent(Agent agent) {
		sessionFactory.getCurrentSession().delete(agent);
		return agent;
	}
}
