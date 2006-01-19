/*
 * Created on 24/Ago/2004
 *
 */
package net.sourceforge.fenixedu.applicationTier.Servico.teacher;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.ExistingServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.InvalidSituationServiceException;
import net.sourceforge.fenixedu.domain.Attends;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.Grouping;
import net.sourceforge.fenixedu.domain.Student;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IFrequentaPersistente;
import net.sourceforge.fenixedu.persistenceTier.IPersistentStudent;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;
import net.sourceforge.fenixedu.applicationTier.Service;

/**
 * @author joaosa & rmalo
 * 
 */

public class InsertGroupingMembers extends Service {

	public Boolean run(Integer executionCourseCode, Integer groupPropertiesCode, List studentCodes)
			throws FenixServiceException, ExcepcaoPersistencia {

		IFrequentaPersistente persistentAttend = null;
		IPersistentStudent persistentStudent = null;

		List students = new ArrayList();

		ISuportePersistente persistentSupport = PersistenceSupportFactory.getDefaultPersistenceSupport();

		persistentStudent = persistentSupport.getIPersistentStudent();
		persistentAttend = persistentSupport.getIFrequentaPersistente();

		Grouping groupProperties = (Grouping) persistentSupport.getIPersistentObject().readByOID(Grouping.class,
				groupPropertiesCode);

		if (groupProperties == null) {
			throw new ExistingServiceException();
		}

		Iterator iterator = studentCodes.iterator();

		while (iterator.hasNext()) {
			Student student = (Student) persistentStudent.readByOID(Student.class, (Integer) iterator
					.next());
			students.add(student);
		}

		Iterator iterAttends = groupProperties.getAttends().iterator();

		while (iterAttends.hasNext()) {
			Attends existingAttend = (Attends) iterAttends.next();
			Student existingAttendStudent = existingAttend.getAluno();

			Iterator iteratorStudents = students.iterator();

			while (iteratorStudents.hasNext()) {

				Student student = (Student) iteratorStudents.next();
				if (student.equals(existingAttendStudent)) {
					throw new InvalidSituationServiceException();
				}
			}
		}

		Iterator iterStudents1 = students.iterator();

		while (iterStudents1.hasNext()) {
			Attends attend = null;
			Student student = (Student) iterStudents1.next();

			List listaExecutionCourses = new ArrayList();
			listaExecutionCourses.addAll(groupProperties.getExecutionCourses());
			Iterator iterExecutionCourse = listaExecutionCourses.iterator();
			while (iterExecutionCourse.hasNext() && attend == null) {
				ExecutionCourse executionCourse = (ExecutionCourse) iterExecutionCourse.next();
				attend = persistentAttend.readByAlunoAndDisciplinaExecucao(student.getIdInternal(),
						executionCourse.getIdInternal());
			}
			groupProperties.addAttends(attend);
		}

		return Boolean.TRUE;
	}
}