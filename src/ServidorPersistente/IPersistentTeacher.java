/*
 * IPersistentTeacher.java
 */
package ServidorPersistente;
/**
 *
 * @author  EP 15
 * @author Ivo Brand�o
 */
import java.util.List;

import Dominio.ITeacher;
public interface IPersistentTeacher {

	/**
	 * 
	 * @param user
	 * @return
	 * @throws ExcepcaoPersistencia
	 */	
    public ITeacher readTeacherByUsername(String user) throws ExcepcaoPersistencia;
    
    /**
     * 
     * @param teacherNumber
     * @return
     * @throws ExcepcaoPersistencia
     */
    public ITeacher readTeacherByNumber(Integer teacherNumber) throws ExcepcaoPersistencia;
    
    /**
     * 
     * @param teacher
     * @throws ExcepcaoPersistencia
     */
    public void lockWrite(ITeacher teacher) throws ExcepcaoPersistencia;
    
    /**
     * 
     * @param teacher
     * @throws ExcepcaoPersistencia
     */
    public void delete(ITeacher teacher) throws ExcepcaoPersistencia;
    
    /**
     * 
     * @throws ExcepcaoPersistencia
     */
    public void deleteAll() throws ExcepcaoPersistencia;
	
	/**
	 * 
	 * @return
	 * @throws ExcepcaoPersistencia
	 */
	public List readAll() throws ExcepcaoPersistencia;

	
}
