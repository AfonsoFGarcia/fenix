package ServidorPersistente.OJB;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

import org.apache.ojb.broker.query.Criteria;
import org.odmg.QueryException;

import Dominio.EnrolmentEquivalenceRestriction;
import Dominio.IEnrolment;
import Dominio.IEnrolmentEquivalence;
import Dominio.IEnrolmentEquivalenceRestriction;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentEnrolmentEquivalenceRestriction;
import ServidorPersistente.exceptions.ExistingPersistentException;

/**
 * @author dcs-rjao
 *
 * 24/Mar/2003
 */

public class EnrolmentEquivalenceRestrictionOJB extends ObjectFenixOJB implements IPersistentEnrolmentEquivalenceRestriction {

	public void deleteAll() throws ExcepcaoPersistencia {
		try {
			String oqlQuery = "select all from " + EnrolmentEquivalenceRestriction.class.getName();
			super.deleteAll(oqlQuery);
		} catch (ExcepcaoPersistencia ex) {
			throw ex;
		}
	}

	public void lockWrite(IEnrolmentEquivalenceRestriction enrolmentEquivalenceRestrictionToWrite) throws ExcepcaoPersistencia, ExistingPersistentException {

		IEnrolmentEquivalenceRestriction equivalenceFromDB = null;

		// If there is nothing to write, simply return.
		if (enrolmentEquivalenceRestrictionToWrite == null) {
			return;
		}

		// Read IEnrolmentEquivalenceRestriction from database.

		equivalenceFromDB = readByEnrolmentEquivalenceAndEquivalentEnrolment(enrolmentEquivalenceRestrictionToWrite.getEnrolmentEquivalence(), enrolmentEquivalenceRestrictionToWrite.getEquivalentEnrolment());
		
		// If IEnrolmentEquivalenceRestriction is not in database, then write it.
		if (equivalenceFromDB == null) {
			super.lockWrite(enrolmentEquivalenceRestrictionToWrite);
			// else If the EnrolmentEquivalence is mapped to the database, then write any existing changes.
		} else if (
			(enrolmentEquivalenceRestrictionToWrite instanceof IEnrolmentEquivalenceRestriction)
				&& ((IEnrolmentEquivalenceRestriction) equivalenceFromDB).getIdInternal().equals(((IEnrolmentEquivalenceRestriction) enrolmentEquivalenceRestrictionToWrite).getIdInternal())) {
			super.lockWrite(enrolmentEquivalenceRestrictionToWrite);
			// else Throw an already existing exception
		} else
			throw new ExistingPersistentException();
	}

	public void delete(IEnrolmentEquivalenceRestriction enrolmentEquivalenceRestriction) throws ExcepcaoPersistencia {
		try {
			super.delete(enrolmentEquivalenceRestriction);
		} catch (ExcepcaoPersistencia ex) {
			throw ex;
		}
	}

	public IEnrolmentEquivalenceRestriction readByEnrolmentEquivalenceAndEquivalentEnrolment(IEnrolmentEquivalence enrolmentEquivalence, IEnrolment equivalentEnrolment) throws ExcepcaoPersistencia {
		try {
			Criteria criteria = new Criteria();
			criteria.addEqualTo("enrolmentEquivalenceKey", enrolmentEquivalence.getIdInternal());
			criteria.addEqualTo("equivalentEnrolmentKey", equivalentEnrolment.getIdInternal());
			return (IEnrolmentEquivalenceRestriction) queryObject(EnrolmentEquivalenceRestriction.class, criteria);
		} catch (ExcepcaoPersistencia e) {
			throw e;
		}
	}

	public ArrayList readAll() throws ExcepcaoPersistencia {

		try {
			ArrayList list = new ArrayList();
			String oqlQuery = "select all from " + EnrolmentEquivalenceRestriction.class.getName();
			query.create(oqlQuery);
			List result = (List) query.execute();

			try {
				lockRead(result);
			} catch (ExcepcaoPersistencia ex) {
				throw ex;
			}

			if ((result != null) && (result.size() != 0)) {
				ListIterator iterator = result.listIterator();
				while (iterator.hasNext())
					list.add((IEnrolmentEquivalenceRestriction) iterator.next());
			}
			return list;
		} catch (QueryException ex) {
			throw new ExcepcaoPersistencia(ExcepcaoPersistencia.QUERY, ex);
		}
	}

}