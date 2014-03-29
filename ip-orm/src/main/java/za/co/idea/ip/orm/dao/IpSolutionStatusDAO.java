package za.co.idea.ip.orm.dao;

import java.util.List;

import org.hibernate.LockMode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

import za.co.idea.ip.orm.bean.IpSolutionStatus;

/**
 * A data access object (DAO) providing persistence and search support for
 * IpSolutionStatus entities. Transaction control of the save(), update() and
 * delete() operations can directly support Spring container-managed
 * transactions or they can be augmented to handle user-managed Spring
 * transactions. Each of these methods provides additional information for how
 * to configure it for the desired type of transaction control.
 * 
 * @see za.co.idea.ip.orm.bean.IpSolutionStatus
 * @author MyEclipse Persistence Tools
 */
@SuppressWarnings("rawtypes")
public class IpSolutionStatusDAO extends HibernateDaoSupport {
	private static final Logger log = LoggerFactory.getLogger(IpSolutionStatusDAO.class);
	// property constants
	public static final String SS_DESC = "ssDesc";

	protected void initDao() {
		// do nothing
	}

	public void save(IpSolutionStatus transientInstance) {
		log.debug("saving IpSolutionStatus instance");
		try {
			getHibernateTemplate().save(transientInstance);
			log.debug("save successful");
		} catch (RuntimeException re) {
			log.error("save failed", re);
			throw re;
		}
	}

	public void delete(IpSolutionStatus persistentInstance) {
		log.debug("deleting IpSolutionStatus instance");
		try {
			getHibernateTemplate().delete(persistentInstance);
			log.debug("delete successful");
		} catch (RuntimeException re) {
			log.error("delete failed", re);
			throw re;
		}
	}

	public IpSolutionStatus findById(java.lang.Integer id) {
		log.debug("getting IpSolutionStatus instance with id: " + id);
		try {
			IpSolutionStatus instance = (IpSolutionStatus) getHibernateTemplate().get("za.co.idea.ip.orm.bean.IpSolutionStatus", id);
			return instance;
		} catch (RuntimeException re) {
			log.error("get failed", re);
			throw re;
		}
	}

	public List findByExample(IpSolutionStatus instance) {
		log.debug("finding IpSolutionStatus instance by example");
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
		log.debug("finding IpSolutionStatus instance with property: " + propertyName + ", value: " + value);
		try {
			String queryString = "from IpSolutionStatus as model where model." + propertyName + "= ?";
			return getHibernateTemplate().find(queryString, value);
		} catch (RuntimeException re) {
			log.error("find by property name failed", re);
			throw re;
		}
	}

	public List findBySsDesc(Object ssDesc) {
		return findByProperty(SS_DESC, ssDesc);
	}

	public List findAll() {
		log.debug("finding all IpSolutionStatus instances");
		try {
			String queryString = "from IpSolutionStatus";
			return getHibernateTemplate().find(queryString);
		} catch (RuntimeException re) {
			log.error("find all failed", re);
			throw re;
		}
	}

	public IpSolutionStatus merge(IpSolutionStatus detachedInstance) {
		log.debug("merging IpSolutionStatus instance");
		try {
			IpSolutionStatus result = (IpSolutionStatus) getHibernateTemplate().merge(detachedInstance);
			log.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			log.error("merge failed", re);
			throw re;
		}
	}

	public void attachDirty(IpSolutionStatus instance) {
		log.debug("attaching dirty IpSolutionStatus instance");
		try {
			getHibernateTemplate().saveOrUpdate(instance);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void attachClean(IpSolutionStatus instance) {
		log.debug("attaching clean IpSolutionStatus instance");
		try {
			getHibernateTemplate().lock(instance, LockMode.NONE);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public static IpSolutionStatusDAO getFromApplicationContext(ApplicationContext ctx) {
		return (IpSolutionStatusDAO) ctx.getBean("IpSolutionStatusDAO");
	}
}