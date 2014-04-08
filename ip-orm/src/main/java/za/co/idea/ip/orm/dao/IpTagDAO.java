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

import za.co.idea.ip.orm.bean.IpTag;

/**
 * A data access object (DAO) providing persistence and search support for IpTag
 * entities. Transaction control of the save(), update() and delete() operations
 * can directly support Spring container-managed transactions or they can be
 * augmented to handle user-managed Spring transactions. Each of these methods
 * provides additional information for how to configure it for the desired type
 * of transaction control.
 * 
 * @see za.co.idea.ip.orm.bean.IpTag
 * @author MyEclipse Persistence Tools
 */
@SuppressWarnings("rawtypes")
public class IpTagDAO extends HibernateDaoSupport {
	private static final Logger log = LoggerFactory.getLogger(IpTagDAO.class);
	// property constants
	public static final String TAG_ENTITY_ID = "tagEntityId";
	public static final String TAG_TEXT = "tagText";

	protected void initDao() {
		// do nothing
	}

	public void save(IpTag transientInstance) {
		log.debug("saving IpTag instance");
		try {
			getHibernateTemplate().save(transientInstance);
			log.debug("save successful");
		} catch (RuntimeException re) {
			log.error("save failed", re);
			throw re;
		}
	}

	public void delete(IpTag persistentInstance) {
		log.debug("deleting IpTag instance");
		try {
			getHibernateTemplate().delete(persistentInstance);
			log.debug("delete successful");
		} catch (RuntimeException re) {
			log.error("delete failed", re);
			throw re;
		}
	}

	public IpTag findById(java.lang.Long id) {
		log.debug("getting IpTag instance with id: " + id);
		try {
			IpTag instance = (IpTag) getHibernateTemplate().get("za.co.idea.ip.orm.bean.IpTag", id);
			return instance;
		} catch (RuntimeException re) {
			log.error("get failed", re);
			throw re;
		}
	}

	public List findByExample(IpTag instance) {
		log.debug("finding IpTag instance by example");
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
		log.debug("finding IpTag instance with property: " + propertyName + ", value: " + value);
		try {
			String queryString = "from IpTag as model where model." + propertyName + "= ?";
			return getHibernateTemplate().find(queryString, value);
		} catch (RuntimeException re) {
			log.error("find by property name failed", re);
			throw re;
		}
	}

	public List findByTagEntityId(Object tagEntityId) {
		return findByProperty(TAG_ENTITY_ID, tagEntityId);
	}

	public List findByTagText(Object tagText) {
		return findByProperty(TAG_TEXT, tagText);
	}

	public List findAll() {
		log.debug("finding all IpTag instances");
		try {
			String queryString = "from IpTag";
			return getHibernateTemplate().find(queryString);
		} catch (RuntimeException re) {
			log.error("find all failed", re);
			throw re;
		}
	}

	public IpTag merge(IpTag detachedInstance) {
		log.debug("merging IpTag instance");
		try {
			IpTag result = (IpTag) getHibernateTemplate().merge(detachedInstance);
			log.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			log.error("merge failed", re);
			throw re;
		}
	}

	public void attachDirty(IpTag instance) {
		log.debug("attaching dirty IpTag instance");
		try {
			getHibernateTemplate().saveOrUpdate(instance);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void attachClean(IpTag instance) {
		log.debug("attaching clean IpTag instance");
		try {
			getHibernateTemplate().lock(instance, LockMode.NONE);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public List getTagByFilterA(Long entityId, Integer teId, Integer ttId) {
		log.debug("Fetching Tag by Query :: getTagByFilterA");
		Session session = getSession();
		try {
			Query query = session.getNamedQuery("getTagByFilterA");
			query.setLong("entityId", entityId);
			query.setInteger("ttId", ttId);
			query.setInteger("teId", teId);
			List ret = query.list();
			for (Object object : ret) {
				IpTag tag = (IpTag) object;
				Hibernate.initialize(tag.getIpUser());
				Hibernate.initialize(tag.getIpTagEntityType());
				Hibernate.initialize(tag.getIpTagType());
			}
			session.close();
			return ret;
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public List getTagByFilterB(Long entityId, Integer teId, Integer ttId, Long userId) {
		log.debug("Fetching Tag by Query :: getTagByFilterB");
		Session session = getSession();
		try {
			Query query = session.getNamedQuery("getTagByFilterB");
			query.setLong("entityId", entityId);
			query.setLong("userId", userId);
			query.setInteger("ttId", ttId);
			query.setInteger("teId", teId);
			List ret = query.list();
			for (Object object : ret) {
				IpTag tag = (IpTag) object;
				Hibernate.initialize(tag.getIpUser());
				Hibernate.initialize(tag.getIpTagEntityType());
				Hibernate.initialize(tag.getIpTagType());
			}
			session.close();
			return ret;
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public static IpTagDAO getFromApplicationContext(ApplicationContext ctx) {
		return (IpTagDAO) ctx.getBean("IpTagDAO");
	}
}