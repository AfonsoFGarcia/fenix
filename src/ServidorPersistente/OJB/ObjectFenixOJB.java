/*
 * ObjectFenixOJB.java
 *
 * Created on 22 de Agosto de 2002, 23:51
 */

package ServidorPersistente.OJB;

/**
 *
 * @author  ars
 */

import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Vector;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.ojb.broker.ManageableCollection;
import org.apache.ojb.broker.PersistenceBroker;
import org.apache.ojb.broker.metadata.ClassDescriptor;
import org.apache.ojb.broker.metadata.DescriptorRepository;
import org.apache.ojb.broker.metadata.FieldDescriptor;
import org.apache.ojb.broker.metadata.ObjectReferenceDescriptor;
import org.apache.ojb.broker.metadata.PersistentField;
import org.apache.ojb.broker.query.Criteria;
import org.apache.ojb.broker.query.Query;
import org.apache.ojb.broker.query.QueryFactory;
import org.apache.ojb.broker.util.logging.LoggerFactory;
import org.apache.ojb.odmg.HasBroker;
import org.apache.ojb.odmg.OJB;
import org.apache.ojb.odmg.OJBFactory;
import org.apache.ojb.odmg.oql.EnhancedOQLQuery;
import org.odmg.Database;
import org.odmg.Implementation;
import org.odmg.ODMGRuntimeException;
import org.odmg.OQLQuery;
import org.odmg.QueryException;
import org.odmg.Transaction;

import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentObject;

public abstract class ObjectFenixOJB implements IPersistentObject {
	protected Implementation odmg = null;
	protected Database db = null;
	protected OQLQuery query = null;
	protected Transaction tx = null;

	/** Creates a new instance of ObjectFenixOJB */
	public ObjectFenixOJB() {
		odmg = OJB.getInstance();
		query = odmg.newOQLQuery();
	}

	public void lockRead(List list) throws ExcepcaoPersistencia {
		try {
			ListIterator iterator = list.listIterator();
			tx = odmg.currentTransaction();
			if (tx == null)
				throw new ExcepcaoPersistencia("N�o h� transac��o em curso");
			while (iterator.hasNext()) {
				Object obj = iterator.next();
				tx.lock(obj, Transaction.READ);
			}
		} catch (ODMGRuntimeException ex) {
			throw new ExcepcaoPersistencia(ExcepcaoPersistencia.READ_LOCK, ex);
		}
	}

	public void lockWrite(Object obj) throws ExcepcaoPersistencia {
		try {
			tx = odmg.currentTransaction();
			tx.lock(obj, Transaction.WRITE);
		} catch (ODMGRuntimeException ex) {
			throw new ExcepcaoPersistencia(
				ExcepcaoPersistencia.UPGRADE_LOCK,
				ex);
		}
	}

	public void delete(Object obj) throws ExcepcaoPersistencia {
		try {
			tx = odmg.currentTransaction();
			tx.lock(obj, Transaction.WRITE);
			db = odmg.getDatabase(null);
			db.deletePersistent(obj);
		} catch (ODMGRuntimeException ex) {
			throw new ExcepcaoPersistencia(
				ExcepcaoPersistencia.UPGRADE_LOCK,
				ex);
		}
	}

	public void deleteAll(String oqlQuery) throws ExcepcaoPersistencia {
		try {
			query.create(oqlQuery);
			List result = (List) query.execute();
			ListIterator iterator = result.listIterator();
			while (iterator.hasNext())
				delete(iterator.next());
		} catch (QueryException ex) {
			throw new ExcepcaoPersistencia(ExcepcaoPersistencia.QUERY, ex);
		}
	}

	public void deleteByCriteria(Object obj) throws ExcepcaoPersistencia {
		List result = (List) readByCriteria(obj);
		ListIterator iterator = result.listIterator();
		while (iterator.hasNext())
			delete(iterator.next());

	}

	public Object executeQueryByCriteria(Class queryClass, Criteria crit)
		throws org.odmg.QueryException {

		Query query = QueryFactory.newQuery(queryClass, crit);
		//setBindIterator(flatten(query.getCriteria(), new Vector()).listIterator());
		//setQuery(query);

		try {
			//obtain current ODMG transaction
			Transaction tx = OJBFactory.getInstance().currentTransaction();

			// we allow queries even if no ODMG transaction is running.
			// thus we have to provide a pseudo tx if necessary
			boolean needsCommit = false;
			if (tx == null) {
				tx = OJBFactory.getInstance().newTransaction();
			}

			// we allow to work with unopened transactions.
			// we assume that such a tx is to be closed after performing the query
			if (!tx.isOpen()) {
				tx.begin();
				needsCommit = true;
			}
			// obtain a broker instance from the current transaction
			PersistenceBroker broker = ((HasBroker) tx).getBroker();

			//				  if(needsCommit) broker.beginTransaction();
			// ask the broker to perfom the query.
			// the concrete result type is configurable
			ManageableCollection result =
				(ManageableCollection) broker.getCollectionByQuery(
					/*this.getCollectionClass(),*/
					query);
			//				  if(needsCommit) broker.commitTransaction();

			// read-lock all resulting objects to the current transaction
			Iterator iter = result.ojbIterator();
			Object toBeLocked = null;
			while (iter.hasNext()) {
				toBeLocked = iter.next();
				/**
				 * we can only lock objects, not attributes
				 */
				if (broker.hasClassDescriptor(toBeLocked.getClass()))
					tx.lock(toBeLocked, Transaction.READ);
			}
			// if query was executed with pseudo tx or with unopened tx, commit it
			if (needsCommit) {
				tx.commit();
			}
			return result;

		} catch (Throwable t) {
			throw new org.odmg.QueryException(t.getMessage());
		}

	}

	public Object readDomainObjectByCriteria(Object obj) throws ExcepcaoPersistencia {
		List result = readByCriteria(obj);
		if (result != null && !result.isEmpty())
			return result.get(0);
		return null;
	}

	public List readByCriteria(Class queryClass, Criteria criteria)
		throws ExcepcaoPersistencia {

		if (queryClass == null)
			throw new IllegalArgumentException("Class to query cannot be null");

		try {
			//((EnhancedOQLQuery) query).create(queryClass, criteria);
			//List result = (List) query.execute();
			List result = (List) executeQueryByCriteria(queryClass, criteria);
			lockRead(result);
			return result;
		} catch (Exception e) {
			throw new ExcepcaoPersistencia(ExcepcaoPersistencia.QUERY, e);
		}

	}

	public List readByCriteria(Object obj) throws ExcepcaoPersistencia {

		if (obj == null)
			throw new IllegalArgumentException("Object to query cannot be null");

		return readByCriteria(obj.getClass(), criteriaBuilder(obj));

	}

	protected Criteria criteriaBuilder(Object obj) {
		return criteriaBuilder(new Criteria(), "", obj);
	}

	private static Criteria criteriaBuilder(
		Criteria criteria,
		String path,
		Object anExample) {
		ClassDescriptor cld =
			DescriptorRepository.getDefaultInstance().getDescriptorFor(
				anExample.getClass());
		FieldDescriptor[] fds = cld.getFieldDescriptions();
		PersistentField f;
		Object value;
		Vector ref = cld.getObjectReferenceDescriptors();
		Criteria crit = criteria;

		for (int j = 0; j < ref.size(); j++) {
			ObjectReferenceDescriptor objref =
				(ObjectReferenceDescriptor) ref.get(j);
			PropertyDescriptor[] xpto =
				PropertyUtils.getPropertyDescriptors(anExample.getClass());
			for (int k = 0; k < xpto.length; k++) {
				Method getM = xpto[k].getReadMethod();
				if (xpto[k].getName().equals(objref.getAttributeName())) {
					try {
						Object propValue = getM.invoke(anExample, null);
						if (propValue != null) {
							crit =
								criteriaBuilder(
									criteria,
									path + objref.getAttributeName() + ".",
									propValue);
						}
					} catch (IllegalAccessException ex) {
						LoggerFactory.getDefaultLogger().error(ex);
					} catch (InvocationTargetException ex) {
						LoggerFactory.getDefaultLogger().error(ex);
					}
				}
			}
		}

		for (int i = 0; i < fds.length; i++) {

			try {
				f = fds[i].getPersistentField();
				value = f.get(anExample);
				if (value != null) {
					crit.addEqualTo(path + f.getName(), value);
				}
			} catch (Throwable ex) {
				LoggerFactory.getDefaultLogger().error(ex);
			}
		}

		return crit;
	}

}
