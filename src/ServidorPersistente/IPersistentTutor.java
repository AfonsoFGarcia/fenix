/*
 * Created on 2/Fev/2004
 *
 */
package ServidorPersistente;

import java.util.List;

import Dominio.IStudent;
import Dominio.ITeacher;

/**
 * @author T�nia Pous�o
 *
 */
public interface IPersistentTutor extends IPersistentObject
{
	public List readTeachersByStudent(IStudent student)  throws ExcepcaoPersistencia;
	public List readStudentsByTeacher(ITeacher teacher)  throws ExcepcaoPersistencia;
}
