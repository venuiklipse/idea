package za.co.idea.ip.orm.dao;

import java.util.List;

import org.hibernate.LockMode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

import za.co.idea.ip.orm.bean.IpPoints;

/**
 * A data access object (DAO) providing persistence and search support for
 * IpPoints entities. Transaction control of the save(), update() and delete()
 * operations can directly support Spring container-managed transactions or they
 * can be augmented to handle user-managed Spring transactions. Each of these
 * methods provides additional information for how to configure it for the
 * desired type of transaction control.
 * 
 * @see za.co.idea.ip.orm.bean.IpPoints
 * @author MyEclipse Persistence Tools
 */
@SuppressWarnings("rawtypes")
public class IpPointsDAO extends HibernateDaoSupport {
	private static final Logger log = LoggerFactory.getLogger(IpPointsDAO.class);

	// property constants

	protected void initDao() {
		// do nothing
	}

	public void save(IpPoints transientInstance) {
		log.debug("saving IpPoints instance");
		try {
			getHibernateTemplate().save(transientInstance);
			log.debug("save successful");
		} catch (RuntimeException re) {
			log.error("save failed", re);
			throw re;
		}
	}

	public void delete(IpPoints persistentInstance) {
		log.debug("deleting IpPoints instance");
		try {
			getHibernateTemplate().delete(persistentInstance);
			log.debug("delete successful");
		} catch (RuntimeException re) {
			log.error("delete failed", re);
			throw re;
		}
	}

	public IpPoints findById(java.lang.Long id) {
		log.debug("getting IpPoints instance with id: " + id);
		try {
			IpPoints instance = (IpPoints) getHibernateTemplate().get("za.co.idea.ip.orm.bean.IpPoints", id);
			return instance;
		} catch (RuntimeException re) {
			log.error("get failed", re);
			throw re;
		}
	}

	public List findByExample(IpPoints instance) {
		log.debug("finding IpPoints instance by example");
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
		log.debug("finding IpPoints instance with property: " + propertyName + ", value: " + value);
		try {
			String queryString = "from IpPoints as model where model." + propertyName + "= ?";
			return getHibernateTemplate().find(queryString, value);
		} catch (RuntimeException re) {
			log.error("find by property name failed", re);
			throw re;
		}
	}

	public List findAll() {
		log.debug("finding all IpPoints instances");
		try {
			String queryString = "from IpPoints";
			return getHibernateTemplate().find(queryString);
		} catch (RuntimeException re) {
			log.error("find all failed", re);
			throw re;
		}
	}

	public IpPoints merge(IpPoints detachedInstance) {
		log.debug("merging IpPoints instance");
		try {
			IpPoints result = (IpPoints) getHibernateTemplate().merge(detachedInstance);
			log.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			log.error("merge failed", re);
			throw re;
		}
	}

	public void attachDirty(IpPoints instance) {
		log.debug("attaching dirty IpPoints instance");
		try {
			getHibernateTemplate().saveOrUpdate(instance);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void attachClean(IpPoints instance) {
		log.debug("attaching clean IpPoints instance");
		try {
			getHibernateTemplate().lock(instance, LockMode.NONE);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public static IpPointsDAO getFromApplicationContext(ApplicationContext ctx) {
		return (IpPointsDAO) ctx.getBean("IpPointsDAO");
	}
}