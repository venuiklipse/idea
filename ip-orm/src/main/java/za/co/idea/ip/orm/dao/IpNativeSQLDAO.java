package za.co.idea.ip.orm.dao;

import java.util.List;

import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.persister.entity.AbstractEntityPersister;

import za.co.idea.ip.orm.util.HibernateSessionFactory;

@SuppressWarnings("rawtypes")
public class IpNativeSQLDAO extends BaseHibernateDAO {
	private HibernateSessionFactory factory;

	public Long getNextId(Class clazz) {
		Session session = getSession();
		Transaction transaction = session.beginTransaction();
		Long ret = -1l;
		try {
			AbstractEntityPersister persister = (AbstractEntityPersister) factory.getSessionFactory().getClassMetadata(clazz);
			String sql = "select max(" + persister.getIdentifierColumnNames()[0] + ")+1 from " + persister.getTableName();
			SQLQuery query = session.createSQLQuery(sql);
			List res = query.list();
			if (res != null && res.size() > 0)
				ret = Long.valueOf(res.get(0).toString());
			transaction.commit();
		} catch (Exception e) {
			transaction.rollback();
			throw new RuntimeException(e);
		}
		return ret;
	}

	public HibernateSessionFactory getFactory() {
		return factory;
	}

	public void setFactory(HibernateSessionFactory factory) {
		this.factory = factory;
	}
}
