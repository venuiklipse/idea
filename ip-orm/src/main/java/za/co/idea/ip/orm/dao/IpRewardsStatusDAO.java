package za.co.idea.ip.orm.dao;

import java.util.List;

import org.hibernate.LockMode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

import za.co.idea.ip.orm.bean.IpRewardsStatus;

/**
 * A data access object (DAO) providing persistence and search support for
 * IpRewardsStatus entities. Transaction control of the save(), update() and
 * delete() operations can directly support Spring container-managed
 * transactions or they can be augmented to handle user-managed Spring
 * transactions. Each of these methods provides additional information for how
 * to configure it for the desired type of transaction control.
 * 
 * @see za.co.idea.ip.orm.bean.IpRewardsStatus
 * @author MyEclipse Persistence Tools
 */
@SuppressWarnings("rawtypes")
public class IpRewardsStatusDAO extends HibernateDaoSupport {
	private static final Logger log = LoggerFactory.getLogger(IpRewardsStatusDAO.class);
	// property constants
	public static final String RS_DESC = "rsDesc";

	protected void initDao() {
		// do nothing
	}

	public void save(IpRewardsStatus transientInstance) {
		log.debug("saving IpRewardsStatus instance");
		try {
			getHibernateTemplate().save(transientInstance);
			log.debug("save successful");
		} catch (RuntimeException re) {
			log.error("save failed", re);
			throw re;
		}
	}

	public void delete(IpRewardsStatus persistentInstance) {
		log.debug("deleting IpRewardsStatus instance");
		try {
			getHibernateTemplate().delete(persistentInstance);
			log.debug("delete successful");
		} catch (RuntimeException re) {
			log.error("delete failed", re);
			throw re;
		}
	}

	public IpRewardsStatus findById(java.lang.Integer id) {
		log.debug("getting IpRewardsStatus instance with id: " + id);
		try {
			IpRewardsStatus instance = (IpRewardsStatus) getHibernateTemplate().get("za.co.idea.ip.orm.bean.IpRewardsStatus", id);
			return instance;
		} catch (RuntimeException re) {
			log.error("get failed", re);
			throw re;
		}
	}

	public List findByExample(IpRewardsStatus instance) {
		log.debug("finding IpRewardsStatus instance by example");
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
		log.debug("finding IpRewardsStatus instance with property: " + propertyName + ", value: " + value);
		try {
			String queryString = "from IpRewardsStatus as model where model." + propertyName + "= ?";
			return getHibernateTemplate().find(queryString, value);
		} catch (RuntimeException re) {
			log.error("find by property name failed", re);
			throw re;
		}
	}

	public List findByRsDesc(Object rsDesc) {
		return findByProperty(RS_DESC, rsDesc);
	}

	public List findAll() {
		log.debug("finding all IpRewardsStatus instances");
		try {
			String queryString = "from IpRewardsStatus";
			return getHibernateTemplate().find(queryString);
		} catch (RuntimeException re) {
			log.error("find all failed", re);
			throw re;
		}
	}

	public IpRewardsStatus merge(IpRewardsStatus detachedInstance) {
		log.debug("merging IpRewardsStatus instance");
		try {
			IpRewardsStatus result = (IpRewardsStatus) getHibernateTemplate().merge(detachedInstance);
			log.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			log.error("merge failed", re);
			throw re;
		}
	}

	public void attachDirty(IpRewardsStatus instance) {
		log.debug("attaching dirty IpRewardsStatus instance");
		try {
			getHibernateTemplate().saveOrUpdate(instance);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void attachClean(IpRewardsStatus instance) {
		log.debug("attaching clean IpRewardsStatus instance");
		try {
			getHibernateTemplate().lock(instance, LockMode.NONE);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public static IpRewardsStatusDAO getFromApplicationContext(ApplicationContext ctx) {
		return (IpRewardsStatusDAO) ctx.getBean("IpRewardsStatusDAO");
	}
}