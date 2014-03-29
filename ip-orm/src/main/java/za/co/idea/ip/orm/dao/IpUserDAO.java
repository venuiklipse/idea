package za.co.idea.ip.orm.dao;

import java.util.List;

import org.hibernate.LockMode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

import za.co.idea.ip.orm.bean.IpUser;

/**
 * A data access object (DAO) providing persistence and search support for
 * IpUser entities. Transaction control of the save(), update() and delete()
 * operations can directly support Spring container-managed transactions or they
 * can be augmented to handle user-managed Spring transactions. Each of these
 * methods provides additional information for how to configure it for the
 * desired type of transaction control.
 * 
 * @see za.co.idea.ip.orm.bean.IpUser
 * @author MyEclipse Persistence Tools
 */
@SuppressWarnings("rawtypes")
public class IpUserDAO extends HibernateDaoSupport {
	private static final Logger log = LoggerFactory.getLogger(IpUserDAO.class);
	// property constants
	public static final String USER_FNAME = "userFName";
	public static final String USER_LNAME = "userLName";
	public static final String USER_MNAME = "userMName";
	public static final String USER_ID_NUM = "userIdNum";
	public static final String USER_SCREEN_NAME = "userScreenName";
	public static final String USER_EMAIL = "userEmail";
	public static final String USER_CONTACT = "userContact";
	public static final String USER_SKILLS = "userSkills";
	public static final String USER_BIO = "userBio";
	public static final String USER_FB_HANDLE = "userFbHandle";
	public static final String USER_TW_HANDLE = "userTwHandle";
	public static final String USER_AVATAR = "userAvatar";
	public static final String USER_STATUS = "userStatus";

	protected void initDao() {
		// do nothing
	}

	public void save(IpUser transientInstance) {
		log.debug("saving IpUser instance");
		try {
			getHibernateTemplate().save(transientInstance);
			log.debug("save successful");
		} catch (RuntimeException re) {
			log.error("save failed", re);
			throw re;
		}
	}

	public void delete(IpUser persistentInstance) {
		log.debug("deleting IpUser instance");
		try {
			getHibernateTemplate().delete(persistentInstance);
			log.debug("delete successful");
		} catch (RuntimeException re) {
			log.error("delete failed", re);
			throw re;
		}
	}

	public IpUser findById(java.lang.Long id) {
		log.debug("getting IpUser instance with id: " + id);
		try {
			IpUser instance = (IpUser) getHibernateTemplate().get("za.co.idea.ip.orm.bean.IpUser", id);
			return instance;
		} catch (RuntimeException re) {
			log.error("get failed", re);
			throw re;
		}
	}

	public List findByExample(IpUser instance) {
		log.debug("finding IpUser instance by example");
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
		log.debug("finding IpUser instance with property: " + propertyName + ", value: " + value);
		try {
			String queryString = "from IpUser as model where model." + propertyName + "= ?";
			return getHibernateTemplate().find(queryString, value);
		} catch (RuntimeException re) {
			log.error("find by property name failed", re);
			throw re;
		}
	}

	public List findByUserFName(Object userFName) {
		return findByProperty(USER_FNAME, userFName);
	}

	public List findByUserLName(Object userLName) {
		return findByProperty(USER_LNAME, userLName);
	}

	public List findByUserMName(Object userMName) {
		return findByProperty(USER_MNAME, userMName);
	}

	public List findByUserIdNum(Object userIdNum) {
		return findByProperty(USER_ID_NUM, userIdNum);
	}

	public List findByUserScreenName(Object userScreenName) {
		return findByProperty(USER_SCREEN_NAME, userScreenName);
	}

	public List findByUserEmail(Object userEmail) {
		return findByProperty(USER_EMAIL, userEmail);
	}

	public List findByUserContact(Object userContact) {
		return findByProperty(USER_CONTACT, userContact);
	}

	public List findByUserSkills(Object userSkills) {
		return findByProperty(USER_SKILLS, userSkills);
	}

	public List findByUserBio(Object userBio) {
		return findByProperty(USER_BIO, userBio);
	}

	public List findByUserFbHandle(Object userFbHandle) {
		return findByProperty(USER_FB_HANDLE, userFbHandle);
	}

	public List findByUserTwHandle(Object userTwHandle) {
		return findByProperty(USER_TW_HANDLE, userTwHandle);
	}

	public List findByUserAvatar(Object userAvatar) {
		return findByProperty(USER_AVATAR, userAvatar);
	}

	public List findByUserStatus(Object userStatus) {
		return findByProperty(USER_STATUS, userStatus);
	}

	public List findAll() {
		log.debug("finding all IpUser instances");
		try {
			String queryString = "from IpUser";
			return getHibernateTemplate().find(queryString);
		} catch (RuntimeException re) {
			log.error("find all failed", re);
			throw re;
		}
	}

	public IpUser merge(IpUser detachedInstance) {
		log.debug("merging IpUser instance");
		try {
			IpUser result = (IpUser) getHibernateTemplate().merge(detachedInstance);
			log.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			log.error("merge failed", re);
			throw re;
		}
	}

	public void attachDirty(IpUser instance) {
		log.debug("attaching dirty IpUser instance");
		try {
			getHibernateTemplate().saveOrUpdate(instance);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void attachClean(IpUser instance) {
		log.debug("attaching clean IpUser instance");
		try {
			getHibernateTemplate().lock(instance, LockMode.NONE);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public static IpUserDAO getFromApplicationContext(ApplicationContext ctx) {
		return (IpUserDAO) ctx.getBean("IpUserDAO");
	}
}