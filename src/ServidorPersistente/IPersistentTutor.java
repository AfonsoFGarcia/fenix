/*
 * Created on 2/Fev/2004
 *
 */
package ServidorPersistente;

import java.util.List;

import Dominio.IStudent;
import Dominio.ITeacher;
import Dominio.ITutor;

/**
 * @author T�nia Pous�o
 *
 */
public interface IPersistentTutor extends IPersistentObject
{
	public ITutor readTutorByTeacherAndStudent(ITeacher teacher, IStudent student)  throws ExcepcaoPersistencia;
	public ITutor readTeachersByStudent(IStudent student)  throws ExcepcaoPersistencia;
	public List readStudentsByTeacher(ITeacher teacher)  throws ExcepcaoPersistencia;	
}
