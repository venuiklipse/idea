package za.co.idea.ip.orm.dao;

import java.util.List;

import org.hibernate.Hibernate;
import org.hibernate.LockMode;
import org.hibernate.Query;
import org.hibernate.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

import za.co.idea.ip.orm.bean.IpSolution;

/**
 * A data access object (DAO) providing persistence and search support for
 * IpSolution entities. Transaction control of the save(), update() and delete()
 * operations can directly support Spring container-managed transactions or they
 * can be augmented to handle user-managed Spring transactions. Each of these
 * methods provides additional information for how to configure it for the
 * desired type of transaction control.
 * 
 * @see za.co.idea.ip.orm.bean.IpSolution
 * @author MyEclipse Persistence Tools
 */
@SuppressWarnings("rawtypes")
public class IpSolutionDAO extends HibernateDaoSupport {
	private static final Logger log = LoggerFactory.getLogger(IpSolutionDAO.class);
	// property constants
	public static final String SOL_TITLE = "solTitle";
	public static final String SOL_DESC = "solDesc";
	public static final String SOL_TAGS = "solTags";
	public static final String SOL_BLOB = "solBlob";

	protected void initDao() {
		// do nothing
	}

	public void save(IpSolution transientInstance) {
		log.debug("saving IpSolution instance");
		try {
			getHibernateTemplate().save(transientInstance);
			log.debug("save successful");
		} catch (RuntimeException re) {
			log.error("save failed", re);
			throw re;
		}
	}

	public void delete(IpSolution persistentInstance) {
		log.debug("deleting IpSolution instance");
		try {
			getHibernateTemplate().delete(persistentInstance);
			log.debug("delete successful");
		} catch (RuntimeException re) {
			log.error("delete failed", re);
			throw re;
		}
	}

	public IpSolution findById(java.lang.Long id) {
		log.debug("getting IpSolution instance with id: " + id);
		try {
			IpSolution instance = (IpSolution) getHibernateTemplate().get("za.co.idea.ip.orm.bean.IpSolution", id);
			return instance;
		} catch (RuntimeException re) {
			log.error("get failed", re);
			throw re;
		}
	}

	public List findByExample(IpSolution instance) {
		log.debug("finding IpSolution instance by example");
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
		log.debug("finding IpSolution instance with property: " + propertyName + ", value: " + value);
		try {
			String queryString = "from IpSolution as model where model." + propertyName + "= ?";
			return getHibernateTemplate().find(queryString, value);
		} catch (RuntimeException re) {
			log.error("find by property name failed", re);
			throw re;
		}
	}

	public List findBySolTitle(Object solTitle) {
		return findByProperty(SOL_TITLE, solTitle);
	}

	public List findBySolDesc(Object solDesc) {
		return findByProperty(SOL_DESC, solDesc);
	}

	public List findBySolTags(Object solTags) {
		return findByProperty(SOL_TAGS, solTags);
	}

	public List findBySolBlob(Object solBlob) {
		return findByProperty(SOL_BLOB, solBlob);
	}

	public List findAll() {
		log.debug("finding all IpSolution instances");
		try {
			String queryString = "from IpSolution";
			return getHibernateTemplate().find(queryString);
		} catch (RuntimeException re) {
			log.error("find all failed", re);
			throw re;
		}
	}

	public IpSolution merge(IpSolution detachedInstance) {
		log.debug("merging IpSolution instance");
		try {
			IpSolution result = (IpSolution) getHibernateTemplate().merge(detachedInstance);
			log.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			log.error("merge failed", re);
			throw re;
		}
	}

	public void attachDirty(IpSolution instance) {
		log.debug("attaching dirty IpSolution instance");
		try {
			getHibernateTemplate().saveOrUpdate(instance);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void attachClean(IpSolution instance) {
		log.debug("attaching clean IpSolution instance");
		try {
			getHibernateTemplate().lock(instance, LockMode.NONE);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public static IpSolutionDAO getFromApplicationContext(ApplicationContext ctx) {
		return (IpSolutionDAO) ctx.getBean("IpSolutionDAO");
	}

	public List findByUserId(Long id) {
		log.debug("Fetching Challenge by Query :: getSolutionByUser");
		Session session = getSession();
		try {
			Query query = session.getNamedQuery("getSolutionByUser");
			query.setLong("id", id);
			List ret = query.list();
			for (Object object : ret) {
				IpSolution sol = (IpSolution) object;
				Hibernate.initialize(sol.getIpChallenge());
				Hibernate.initialize(sol.getIpSolutionCat());
				Hibernate.initialize(sol.getIpSolutionStatus());
				Hibernate.initialize(sol.getIpUser());
			}
			session.close();
			return ret;
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public List findByStatusId(Integer id) {
		log.debug("Fetching Challenge by Query :: getSolutionByStatus");
		Session session = getSession();
		try {
			Query query = session.getNamedQuery("getSolutionByUser");
			query.setLong("id", id);
			List ret = query.list();
			for (Object object : ret) {
				IpSolution sol = (IpSolution) object;
				Hibernate.initialize(sol.getIpChallenge());
				Hibernate.initialize(sol.getIpSolutionCat());
				Hibernate.initialize(sol.getIpSolutionStatus());
				Hibernate.initialize(sol.getIpUser());
			}
			session.close();
			return ret;
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}
}