/*
 * IPersistentTeacher.java
 */
package ServidorPersistente;

/**
 * @author EP 15
 * @author Ivo Brand�o
 */
import java.util.List;

import Dominio.IDepartment;
import Dominio.ITeacher;

public interface IPersistentTeacher extends IPersistentObject
{

    /**
     * @param user
     * @return @throws
     *         ExcepcaoPersistencia
     */
    public ITeacher readTeacherByUsername(String user) throws ExcepcaoPersistencia;

    /**
     * @param teacher
     * @throws ExcepcaoPersistencia
     */
    public void delete(ITeacher teacher) throws ExcepcaoPersistencia;

    /**
     * @throws ExcepcaoPersistencia
     */
    public void deleteAll() throws ExcepcaoPersistencia;

    /**
     * @return @throws
     *         ExcepcaoPersistencia
     */
    public List readAll() throws ExcepcaoPersistencia;

    /**
     * @param department
     * @return
     */
    public List readByDepartment(IDepartment department) throws ExcepcaoPersistencia;

    /**
     * Reads a teacher with the number passed by argument.
     * 
     * @param teacherNumber
     * @return ITeacher or null if there's no teacher with that number
     * @throws ExcepcaoPersistencia
     *             if there's something wrong in database
     * @author jpvl
     */
    public ITeacher readByNumber(Integer teacherNumber) throws ExcepcaoPersistencia;
}