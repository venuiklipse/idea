package za.co.idea.ip.orm.dao;

import java.util.List;

import org.hibernate.LockMode;
import org.hibernate.SQLQuery;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

import za.co.idea.ip.orm.bean.IpFunctionConfig;

/**
 * A data access object (DAO) providing persistence and search support for
 * IpFunctionConfig entities. Transaction control of the save(), update() and
 * delete() operations can directly support Spring container-managed
 * transactions or they can be augmented to handle user-managed Spring
 * transactions. Each of these methods provides additional information for how
 * to configure it for the desired type of transaction control.
 * 
 * @see za.co.idea.ip.orm.bean.IpFunctionConfig
 * @author MyEclipse Persistence Tools
 */
@SuppressWarnings("rawtypes")
public class IpFunctionConfigDAO extends HibernateDaoSupport {
	private static final Logger log = LoggerFactory.getLogger(IpFunctionConfigDAO.class);

	// property constants

	protected void initDao() {
		// do nothing
	}

	public void save(IpFunctionConfig transientInstance) {
		log.debug("saving IpFunctionConfig instance");
		try {
			getHibernateTemplate().save(transientInstance);
			log.debug("save successful");
		} catch (RuntimeException re) {
			log.error("save failed", re);
			throw re;
		}
	}

	public void delete(IpFunctionConfig persistentInstance) {
		log.debug("deleting IpFunctionConfig instance");
		try {
			getHibernateTemplate().delete(persistentInstance);
			log.debug("delete successful");
		} catch (RuntimeException re) {
			log.error("delete failed", re);
			throw re;
		}
	}

	public IpFunctionConfig findById(java.lang.String id) {
		log.debug("getting IpFunctionConfig instance with id: " + id);
		try {
			IpFunctionConfig instance = (IpFunctionConfig) getHibernateTemplate().get("za.co.idea.ip.orm.bean.IpFunctionConfig", id);
			return instance;
		} catch (RuntimeException re) {
			log.error("get failed", re);
			throw re;
		}
	}

	public List findByExample(IpFunctionConfig instance) {
		log.debug("finding IpFunctionConfig instance by example");
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
		log.debug("finding IpFunctionConfig instance with property: " + propertyName + ", value: " + value);
		try {
			String queryString = "from IpFunctionConfig as model where model." + propertyName + "= ?";
			return getHibernateTemplate().find(queryString, value);
		} catch (RuntimeException re) {
			log.error("find by property name failed", re);
			throw re;
		}
	}

	public List findAll() {
		log.debug("finding all IpFunctionConfig instances");
		try {
			String queryString = "from IpFunctionConfig";
			return getHibernateTemplate().find(queryString);
		} catch (RuntimeException re) {
			log.error("find all failed", re);
			throw re;
		}
	}

	public IpFunctionConfig merge(IpFunctionConfig detachedInstance) {
		log.debug("merging IpFunctionConfig instance");
		try {
			IpFunctionConfig result = (IpFunctionConfig) getHibernateTemplate().merge(detachedInstance);
			log.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			log.error("merge failed", re);
			throw re;
		}
	}

	public void attachDirty(IpFunctionConfig instance) {
		log.debug("attaching dirty IpFunctionConfig instance");
		try {
			getHibernateTemplate().saveOrUpdate(instance);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void attachClean(IpFunctionConfig instance) {
		log.debug("attaching clean IpFunctionConfig instance");
		try {
			getHibernateTemplate().lock(instance, LockMode.NONE);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public static IpFunctionConfigDAO getFromApplicationContext(ApplicationContext ctx) {
		return (IpFunctionConfigDAO) ctx.getBean("IpFunctionConfigDAO");
	}

	public void deleteByFunctionId(Long funcId) {
		log.debug("deleting IpFunctionConfig by Function ID");
		try {
			SQLQuery query = getSession().createSQLQuery("delete from ip_function_config where fc_func_id=" + funcId);
			query.executeUpdate();
			log.debug("delete successful");
		} catch (RuntimeException re) {
			log.error("delete failed", re);
			throw re;
		}
	}
}