/*
 * TeacherOJB.java
 */
package ServidorPersistente.OJB;
import java.util.ArrayList;
import java.util.List;

import org.odmg.QueryException;

import Dominio.ITeacher;
import Dominio.Teacher;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentTeacher;
/**
 *
 * @author  EP 15
 */
public class TeacherOJB extends ObjectFenixOJB implements IPersistentTeacher {
    public ITeacher readTeacherByUsername(String user) throws ExcepcaoPersistencia {
        try {
            ITeacher teacher = null;
            
            String oqlQuery = "select teacher from " + Teacher.class.getName();
            oqlQuery += " where username = $1";
            
            query.create(oqlQuery);
            query.bind(user);
            
            List result = (List) query.execute();
            lockRead(result);
            if (result.size() != 0){
                teacher = (ITeacher) result.get(0);
            }
            
            return teacher;
        } catch (QueryException ex) {
            throw new ExcepcaoPersistencia(ExcepcaoPersistencia.QUERY, ex);
        }
    }
    public ITeacher readTeacherByNumber(Integer teacherNumber) throws ExcepcaoPersistencia {
        try {
            ITeacher teacher = null;
            
            String oqlQuery = "select teacher from " + Teacher.class.getName();
            oqlQuery += " where teacherNumber = $1";
            
            query.create(oqlQuery);
            query.bind(teacherNumber);
            
            List result = (List) query.execute();
            lockRead(result);
            if (result.size() != 0){
                teacher = (ITeacher) result.get(0);
            }
            
            return teacher;
            
        } catch (QueryException ex) {
            throw new ExcepcaoPersistencia(ExcepcaoPersistencia.QUERY, ex);
        }
    }
    public void lockWrite(ITeacher teacher) throws ExcepcaoPersistencia {
        super.lockWrite(teacher);
    }
    public void delete(ITeacher teacher) throws ExcepcaoPersistencia {
        super.delete(teacher);
    }
    public void deleteAll() throws ExcepcaoPersistencia {
        String oqlQuery = "select all from " + Teacher.class.getName();
        super.deleteAll(oqlQuery);
    }
    public List readOwnedSites(String whoOwns) throws ExcepcaoPersistencia {
        try {
            ITeacher teacher = null;
            
            String oqlQuery = "select teacher from " + Teacher.class.getName();
            oqlQuery += " where username = $1";

            query.create(oqlQuery);
            query.bind(whoOwns);
            List result = (List) query.execute();
            lockRead(result);
            if (result.size() != 0)
                teacher = (ITeacher) result.get(0);
            else
                return new ArrayList();
            return teacher.getSitesOwned();
            
        } catch (Exception ex) {
            throw new ExcepcaoPersistencia(ExcepcaoPersistencia.QUERY, ex);
        }
    }
    public List readOwnedSites(Integer teacherNumber) throws ExcepcaoPersistencia {
        try {
            ITeacher teacher = null;
            
            String oqlQuery = "select teacher from " + Teacher.class.getName();
            oqlQuery += " where teacherNumber = $1";
            query.create(oqlQuery);
            query.bind(teacherNumber);
            List result = (List) query.execute();
            lockRead(result);
            if (result.size() != 0)
                teacher = (ITeacher) result.get(0);
            else
                return new ArrayList();
			return teacher.getSitesOwned();
        } catch (Exception ex) {
            throw new ExcepcaoPersistencia(ExcepcaoPersistencia.QUERY, ex);
        }
    }
     public List readProfessorShipsSites(String whoTeaches) throws ExcepcaoPersistencia {
        try {
            ITeacher teacher = null;
            String oqlQuery = "select teacher from " + Teacher.class.getName();
            oqlQuery += " where username = $1";
            query.create(oqlQuery);
            query.bind(whoTeaches);
            List result = (List) query.execute();
            lockRead(result);
            if (result.size() != 0)
                teacher = (ITeacher) result.get(0);
            else
                return new ArrayList();
            return teacher.getProfessorShipsSites();
        } catch (Exception ex) {
            throw new ExcepcaoPersistencia(ExcepcaoPersistencia.QUERY, ex);
        }
    }
    public List readProfessorShipsSites(Integer teacherNumber) throws ExcepcaoPersistencia {
        try {
            ITeacher teacher = null;
            String oqlQuery = "select teacher from " + Teacher.class.getName();
            oqlQuery += " where teacherNumber = $1";
            query.create(oqlQuery);
            query.bind(teacherNumber);
            List result = (List) query.execute();
            lockRead(result);
            if (result.size() != 0)
                teacher = (ITeacher) result.get(0);
            else
                return new ArrayList();
            return teacher.getProfessorShipsSites();
        } catch (Exception ex) {
            throw new ExcepcaoPersistencia(ExcepcaoPersistencia.QUERY, ex);
        }
    }
}
