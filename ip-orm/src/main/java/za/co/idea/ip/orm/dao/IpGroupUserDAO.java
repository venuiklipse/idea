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

import za.co.idea.ip.orm.bean.IpGroupUser;

/**
 * A data access object (DAO) providing persistence and search support for
 * IpGroup entities. Transaction control of the save(), update() and delete()
 * operations can directly support Spring container-managed transactions or they
 * can be augmented to handle user-managed Spring transactions. Each of these
 * methods provides additional information for how to configure it for the
 * desired type of transaction control.
 * 
 * @see za.co.idea.ip.orm.bean.IpGroup
 * @author MyEclipse Persistence Tools
 */
@SuppressWarnings("rawtypes")
public class IpGroupUserDAO extends HibernateDaoSupport {
	private static final Logger log = LoggerFactory.getLogger(IpGroupUserDAO.class);
	// property constants
	public static final String GROUP_NAME = "groupName";
	public static final String GROUP_STATUS = "groupStatus";
	public static final String GROUP_EMAIL = "groupEmail";

	protected void initDao() {
		// do nothing
	}

	public void save(IpGroupUser transientInstance) {
		log.debug("saving IpGroupUser instance");
		try {
			getHibernateTemplate().save(transientInstance);
			log.debug("save successful");
		} catch (RuntimeException re) {
			log.error("save failed", re);
			throw re;
		}
	}

	public void delete(IpGroupUser persistentInstance) {
		log.debug("deleting IpGroupUser instance");
		try {
			getHibernateTemplate().delete(persistentInstance);
			log.debug("delete successful");
		} catch (RuntimeException re) {
			log.error("delete failed", re);
			throw re;
		}
	}

	public IpGroupUser findById(java.lang.Long id) {
		log.debug("getting IpGroupUser instance with id: " + id);
		try {
			IpGroupUser instance = (IpGroupUser) getHibernateTemplate().get("za.co.idea.ip.orm.bean.IpGroupUser", id);
			return instance;
		} catch (RuntimeException re) {
			log.error("get failed", re);
			throw re;
		}
	}

	public List findByExample(IpGroupUser instance) {
		log.debug("finding IpGroupUser instance by example");
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
		log.debug("finding IpGroup instance with property: " + propertyName + ", value: " + value);
		try {
			String queryString = "from IpGroup as model where model." + propertyName + "= ?";
			return getHibernateTemplate().find(queryString, value);
		} catch (RuntimeException re) {
			log.error("find by property name failed", re);
			throw re;
		}
	}

	public List findByGroupName(Object groupName) {
		return findByProperty(GROUP_NAME, groupName);
	}

	public List findByGroupStatus(Object groupStatus) {
		return findByProperty(GROUP_STATUS, groupStatus);
	}

	public List findByGroupEmail(Object groupEmail) {
		return findByProperty(GROUP_EMAIL, groupEmail);
	}

	public List findAll() {
		log.debug("finding all IpGroup instances");
		try {
			String queryString = "from IpGroup";
			return getHibernateTemplate().find(queryString);
		} catch (RuntimeException re) {
			log.error("find all failed", re);
			throw re;
		}
	}

	public IpGroupUser merge(IpGroupUser detachedInstance) {
		log.debug("merging IpGroupUser instance");
		try {
			IpGroupUser result = (IpGroupUser) getHibernateTemplate().merge(detachedInstance);
			log.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			log.error("merge failed", re);
			throw re;
		}
	}

	public void attachDirty(IpGroupUser instance) {
		log.debug("attaching dirty IpGroupUser instance");
		try {
			getHibernateTemplate().saveOrUpdate(instance);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void attachClean(IpGroupUser instance) {
		log.debug("attaching clean IpGroupUser instance");
		try {
			getHibernateTemplate().lock(instance, LockMode.NONE);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void deleteByGroupId(Long id) {
		log.debug("Deleting Group Users By Id : " + id);
		Session session = getSession();
		try {
			Query query = session.getNamedQuery("deleteByGUGroupId");
			query.setLong("id", id);
			query.executeUpdate();
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public List fetchByGroupId(Long id) {
		log.debug("Fetching Group Users By Id : " + id);
		Session session = getSession();
		try {
			Query query = session.getNamedQuery("fetchGUByGroupId");
			query.setLong("id", id);
			List ret = query.list();
			for (Object object : ret) {
				IpGroupUser gu = (IpGroupUser) object;
				Hibernate.initialize(gu.getIpGroup());
				Hibernate.initialize(gu.getIpUser());
			}
			return ret;
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public List fetchByUserId(Long id) {
		log.debug("Fetching Group Users By Id : " + id);
		Session session = getSession();
		try {
			Query query = session.getNamedQuery("fetchGUByUserId");
			query.setLong("id", id);
			List ret = query.list();
			for (Object object : ret) {
				IpGroupUser gu = (IpGroupUser) object;
				Hibernate.initialize(gu.getIpGroup());
				Hibernate.initialize(gu.getIpUser());
			}
			return ret;
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public static IpGroupUserDAO getFromApplicationContext(ApplicationContext ctx) {
		return (IpGroupUserDAO) ctx.getBean("IpGroupDAO");
	}
}