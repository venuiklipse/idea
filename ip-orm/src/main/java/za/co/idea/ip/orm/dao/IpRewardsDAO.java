package za.co.idea.ip.orm.dao;

import java.util.List;

import org.hibernate.LockMode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

import za.co.idea.ip.orm.bean.IpRewards;

/**
 * A data access object (DAO) providing persistence and search support for
 * IpRewards entities. Transaction control of the save(), update() and delete()
 * operations can directly support Spring container-managed transactions or they
 * can be augmented to handle user-managed Spring transactions. Each of these
 * methods provides additional information for how to configure it for the
 * desired type of transaction control.
 * 
 * @see za.co.idea.ip.orm.bean.IpRewards
 * @author MyEclipse Persistence Tools
 */
@SuppressWarnings("rawtypes")
public class IpRewardsDAO extends HibernateDaoSupport {
	private static final Logger log = LoggerFactory.getLogger(IpRewardsDAO.class);

	// property constants

	protected void initDao() {
		// do nothing
	}

	public void save(IpRewards transientInstance) {
		log.debug("saving IpRewards instance");
		try {
			getHibernateTemplate().save(transientInstance);
			log.debug("save successful");
		} catch (RuntimeException re) {
			log.error("save failed", re);
			throw re;
		}
	}

	public void delete(IpRewards persistentInstance) {
		log.debug("deleting IpRewards instance");
		try {
			getHibernateTemplate().delete(persistentInstance);
			log.debug("delete successful");
		} catch (RuntimeException re) {
			log.error("delete failed", re);
			throw re;
		}
	}

	public IpRewards findById(java.lang.Integer id) {
		log.debug("getting IpRewards instance with id: " + id);
		try {
			IpRewards instance = (IpRewards) getHibernateTemplate().get("za.co.idea.ip.orm.bean.IpRewards", id);
			return instance;
		} catch (RuntimeException re) {
			log.error("get failed", re);
			throw re;
		}
	}

	public List findByExample(IpRewards instance) {
		log.debug("finding IpRewards instance by example");
		try {
			List results = getHibernateTemplate().findByExample(instance);
			log.debug("find by example successful, result size: " + results.size());
			return results;
		} catch (RuntimeException re) {
			log.error("find by example failed", re);
			throw re;
		}
	}

	public List findByProperty(String propertyName, Object value) {
		log.debug("finding IpRewards instance with property: " + propertyName + ", value: " + value);
		try {
			String queryString = "from IpRewards as model where model." + propertyName + "= ?";
			return getHibernateTemplate().find(queryString, value);
		} catch (RuntimeException re) {
			log.error("find by property name failed", re);
			throw re;
		}
	}

	public List findAll() {
		log.debug("finding all IpRewards instances");
		try {
			String queryString = "from IpRewards";
			return getHibernateTemplate().find(queryString);
		} catch (RuntimeException re) {
			log.error("find all failed", re);
			throw re;
		}
	}

	public IpRewards merge(IpRewards detachedInstance) {
		log.debug("merging IpRewards instance");
		try {
			IpRewards result = (IpRewards) getHibernateTemplate().merge(detachedInstance);
			log.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			log.error("merge failed", re);
			throw re;
		}
	}

	public void attachDirty(IpRewards instance) {
		log.debug("attaching dirty IpRewards instance");
		try {
			getHibernateTemplate().saveOrUpdate(instance);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void attachClean(IpRewards instance) {
		log.debug("attaching clean IpRewards instance");
		try {
			getHibernateTemplate().lock(instance, LockMode.NONE);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public static IpRewardsDAO getFromApplicationContext(ApplicationContext ctx) {
		return (IpRewardsDAO) ctx.getBean("IpRewardsDAO");
	}
}