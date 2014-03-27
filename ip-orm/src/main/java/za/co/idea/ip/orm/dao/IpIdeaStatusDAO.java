package za.co.idea.ip.orm.dao;

import java.util.List;

import org.hibernate.LockMode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

import za.co.idea.ip.orm.bean.IpIdeaStatus;

/**
 * A data access object (DAO) providing persistence and search support for
 * IpIdeaStatus entities. Transaction control of the save(), update() and
 * delete() operations can directly support Spring container-managed
 * transactions or they can be augmented to handle user-managed Spring
 * transactions. Each of these methods provides additional information for how
 * to configure it for the desired type of transaction control.
 * 
 * @see za.co.idea.ip.orm.bean.IpIdeaStatus
 * @author MyEclipse Persistence Tools
 */
@SuppressWarnings("unchecked")
public class IpIdeaStatusDAO extends HibernateDaoSupport {
	private static final Logger log = LoggerFactory
			.getLogger(IpIdeaStatusDAO.class);
	// property constants
	public static final String IS_DESC = "isDesc";

	protected void initDao() {
		// do nothing
	}

	public void save(IpIdeaStatus transientInstance) {
		log.debug("saving IpIdeaStatus instance");
		try {
			getHibernateTemplate().save(transientInstance);
			log.debug("save successful");
		} catch (RuntimeException re) {
			log.error("save failed", re);
			throw re;
		}
	}

	public void delete(IpIdeaStatus persistentInstance) {
		log.debug("deleting IpIdeaStatus instance");
		try {
			getHibernateTemplate().delete(persistentInstance);
			log.debug("delete successful");
		} catch (RuntimeException re) {
			log.error("delete failed", re);
			throw re;
		}
	}

	public IpIdeaStatus findById(java.lang.Integer id) {
		log.debug("getting IpIdeaStatus instance with id: " + id);
		try {
			IpIdeaStatus instance = (IpIdeaStatus) getHibernateTemplate().get(
					"za.co.idea.ip.orm.bean.IpIdeaStatus", id);
			return instance;
		} catch (RuntimeException re) {
			log.error("get failed", re);
			throw re;
		}
	}

	public List<IpIdeaStatus> findByExample(IpIdeaStatus instance) {
		log.debug("finding IpIdeaStatus instance by example");
		try {
			List<IpIdeaStatus> results = getHibernateTemplate().findByExample(
					instance);
			log.debug("find by example successful, result size: "
					+ results.size());
			return results;
		} catch (RuntimeException re) {
			log.error("find by example failed", re);
			throw re;
		}
	}

	public List<IpIdeaStatus> findByProperty(String propertyName, Object value) {
		log.debug("finding IpIdeaStatus instance with property: "
				+ propertyName + ", value: " + value);
		try {
			String queryString = "from IpIdeaStatus as model where model."
					+ propertyName + "= ?";
			return getHibernateTemplate().find(queryString, value);
		} catch (RuntimeException re) {
			log.error("find by property name failed", re);
			throw re;
		}
	}

	public List<IpIdeaStatus> findByIsDesc(Object isDesc) {
		return findByProperty(IS_DESC, isDesc);
	}

	public List<IpIdeaStatus> findAll() {
		log.debug("finding all IpIdeaStatus instances");
		try {
			String queryString = "from IpIdeaStatus";
			return getHibernateTemplate().find(queryString);
		} catch (RuntimeException re) {
			log.error("find all failed", re);
			throw re;
		}
	}

	public IpIdeaStatus merge(IpIdeaStatus detachedInstance) {
		log.debug("merging IpIdeaStatus instance");
		try {
			IpIdeaStatus result = (IpIdeaStatus) getHibernateTemplate().merge(
					detachedInstance);
			log.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			log.error("merge failed", re);
			throw re;
		}
	}

	public void attachDirty(IpIdeaStatus instance) {
		log.debug("attaching dirty IpIdeaStatus instance");
		try {
			getHibernateTemplate().saveOrUpdate(instance);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void attachClean(IpIdeaStatus instance) {
		log.debug("attaching clean IpIdeaStatus instance");
		try {
			getHibernateTemplate().lock(instance, LockMode.NONE);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public static IpIdeaStatusDAO getFromApplicationContext(
			ApplicationContext ctx) {
		return (IpIdeaStatusDAO) ctx.getBean("IpIdeaStatusDAO");
	}
}