/*
 * Created on 28/Mai/2003
 *
 * To change the template for this generated file go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
package ServidorPersistente.OJB;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

import org.apache.ojb.broker.query.Criteria;
import org.odmg.QueryException;

import Dominio.IFrequenta;
import Dominio.IStudentGroup;
import Dominio.IStudentGroupAttend;
import Dominio.StudentGroupAttend;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentStudentGroupAttend;
import ServidorPersistente.exceptions.ExistingPersistentException;

/**
 * @author asnr and scpo
 *
 * To change the template for this generated type comment go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
public class StudentGroupAttendOJB extends ObjectFenixOJB implements IPersistentStudentGroupAttend{


	public IStudentGroupAttend readBy(IStudentGroup studentGroup,IFrequenta attend) throws ExcepcaoPersistencia {

			StudentGroupAttend studentGroupAttend = new StudentGroupAttend(studentGroup, attend);
			Criteria criteria1 = new Criteria();
			Criteria criteria2 = new Criteria();
		
			criteria1.addEqualTo("keyStudentGroup",studentGroup.getIdInternal());
			criteria2.addEqualTo("keyAttend",attend.getIdInternal());
		
			criteria1.addAndCriteria(criteria2);
			return (IStudentGroupAttend) queryObject(StudentGroupAttend.class, criteria1);
		}
		
		
	public List readAll() throws ExcepcaoPersistencia {

		try {
				ArrayList list = new ArrayList();
				String oqlQuery = "select all from " + StudentGroupAttend.class.getName();
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
					list.add((IStudentGroupAttend) iterator.next());
				}
				return list;
				} catch (QueryException ex) {
					throw new ExcepcaoPersistencia(ExcepcaoPersistencia.QUERY, ex);
				}
			}


	public void lockWrite(IStudentGroupAttend studentGroupAttendToWrite) throws ExcepcaoPersistencia {
       
		IStudentGroupAttend studentGroupAttendFromDB = null;
		if (studentGroupAttendToWrite == null)
		// If there is nothing to write, simply return.
			return;

		// read studentGroupAttend from DB	
		studentGroupAttendFromDB = readBy(studentGroupAttendToWrite.getStudentGroup(),studentGroupAttendToWrite.getAttend());
		

		// if (studentGroupAttend not in database) then write it
		if (studentGroupAttendFromDB == null)
			super.lockWrite(studentGroupAttendToWrite);
		// else if (item is mapped to the database then write any existing changes)
			 else if ((studentGroupAttendToWrite instanceof IStudentGroupAttend) &&
					   ((IStudentGroupAttend) studentGroupAttendFromDB).getIdInternal().equals(
						((IStudentGroupAttend) studentGroupAttendToWrite).getIdInternal())) {

						  super.lockWrite(studentGroupAttendToWrite);
				  // else throw an AlreadyExists exception.
			  } else
				  throw new ExistingPersistentException();
		  }
		  
	
		  
    
	public void delete(IStudentGroupAttend studentGroupAttend) throws ExcepcaoPersistencia {
			try {
				super.delete(studentGroupAttend);
			} catch (ExcepcaoPersistencia ex) {
				throw ex;
			}
		}
		
		
	public void deleteAll() throws ExcepcaoPersistencia {
			try {
				String oqlQuery = "select all from " + StudentGroupAttend.class.getName();
				super.deleteAll(oqlQuery);
			} catch (ExcepcaoPersistencia ex) {
				throw ex;
			}
		}
}
