/*
 * Created on 9/Set/2004
 *
 */
package net.sourceforge.fenixedu.applicationTier.Servico.teacher;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.ExistingServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NotAuthorizedException;
import net.sourceforge.fenixedu.domain.Advisory;
import net.sourceforge.fenixedu.domain.DomainFactory;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.ExportGrouping;
import net.sourceforge.fenixedu.domain.Grouping;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.Professorship;
import net.sourceforge.fenixedu.domain.Teacher;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentExportGrouping;

/**
 * @author joaosa & rmalo
 * 
 */
public class RejectNewProjectProposal extends Service {

	public Boolean run(Integer executionCourseId, Integer groupPropertiesId, String rejectorUserName)
			throws FenixServiceException, ExcepcaoPersistencia {

		Boolean result = Boolean.FALSE;

		if (groupPropertiesId == null) {
			return result;
		}

		IPersistentExportGrouping persistentExportGrouping = persistentSupport.getIPersistentExportGrouping();
		
		Grouping groupProperties = (Grouping) persistentObject.readByOID(Grouping.class,
				groupPropertiesId);

		if (groupProperties == null) {
			throw new NotAuthorizedException();
		}

		ExportGrouping groupPropertiesExecutionCourse = persistentExportGrouping.readBy(
				groupPropertiesId, executionCourseId);

		if (groupPropertiesExecutionCourse == null) {
			throw new ExistingServiceException();
		}

		Person receiverPerson = Teacher.readTeacherByUsername(rejectorUserName).getPerson();

		ExecutionCourse executionCourse = groupPropertiesExecutionCourse.getExecutionCourse();
		groupPropertiesExecutionCourse.setReceiverPerson(receiverPerson);
		groupPropertiesExecutionCourse.getProposalState().setState(3);
		executionCourse.removeExportGroupings(groupPropertiesExecutionCourse);
		groupProperties.removeExportGroupings(groupPropertiesExecutionCourse);

		persistentExportGrouping.deleteByOID(ExportGrouping.class, groupPropertiesExecutionCourse
				.getIdInternal());

		List group = new ArrayList();

		List groupPropertiesExecutionCourseList = groupProperties.getExportGroupings();
		Iterator iterGroupPropertiesExecutionCourseList = groupPropertiesExecutionCourseList.iterator();

		while (iterGroupPropertiesExecutionCourseList.hasNext()) {

			ExportGrouping groupPropertiesExecutionCourseAux = (ExportGrouping) iterGroupPropertiesExecutionCourseList
					.next();
			if (groupPropertiesExecutionCourseAux.getProposalState().getState().intValue() == 1
					|| groupPropertiesExecutionCourseAux.getProposalState().getState().intValue() == 2) {

				List professorships = groupPropertiesExecutionCourseAux.getExecutionCourse()
						.getProfessorships();

				Iterator iterProfessorship = professorships.iterator();
				while (iterProfessorship.hasNext()) {
					Professorship professorship = (Professorship) iterProfessorship.next();
					Teacher teacher = professorship.getTeacher();

					if (!(teacher.getPerson()).equals(receiverPerson)
							&& !group.contains(teacher.getPerson())) {
						group.add(teacher.getPerson());
					}
				}
			}
		}

		List professorshipsAux = executionCourse.getProfessorships();

		Iterator iterProfessorshipsAux = professorshipsAux.iterator();
		while (iterProfessorshipsAux.hasNext()) {
			Professorship professorshipAux = (Professorship) iterProfessorshipsAux.next();
			Teacher teacherAux = professorshipAux.getTeacher();
			if (!(teacherAux.getPerson()).equals(receiverPerson)
					&& !group.contains(teacherAux.getPerson())) {
				group.add(teacherAux.getPerson());
			}
		}

		Person senderPerson = groupPropertiesExecutionCourse.getSenderPerson();

		// Create Advisory
		Advisory advisory = createRejectAdvisory(executionCourse, senderPerson, receiverPerson,
				groupPropertiesExecutionCourse);
		for (final Iterator iterator = group.iterator(); iterator.hasNext();) {
			final Person person = (Person) iterator.next();

			person.getAdvisories().add(advisory);
			advisory.getPeople().add(person);
		}

		result = Boolean.TRUE;

		return result;
	}

	private Advisory createRejectAdvisory(ExecutionCourse executionCourse, Person senderPerson,
			Person receiverPerson, ExportGrouping groupPropertiesExecutionCourse) {
		Advisory advisory = DomainFactory.makeAdvisory();
		advisory.setCreated(new Date(Calendar.getInstance().getTimeInMillis()));
		if (groupPropertiesExecutionCourse.getGrouping().getEnrolmentEndDay() != null) {
			advisory.setExpires(groupPropertiesExecutionCourse.getGrouping().getEnrolmentEndDay()
					.getTime());
		} else {
			advisory.setExpires(new Date(Calendar.getInstance().getTimeInMillis() + 1728000000));
		}
		advisory.setSender("Docente " + receiverPerson.getNome() + " da disciplina "
				+ executionCourse.getNome());

		advisory.setSubject("Proposta Enviada Rejeitada");

		String msg;
		msg = new String("A proposta de co-avalia��o do agrupamento "
				+ groupPropertiesExecutionCourse.getGrouping().getName() + ", enviada pelo docente "
				+ senderPerson.getNome() + " da disciplina "
				+ groupPropertiesExecutionCourse.getSenderExecutionCourse().getNome()
				+ " foi rejeitada pelo docente " + receiverPerson.getNome() + " da disciplina "
				+ executionCourse.getNome() + "!");

		advisory.setMessage(msg);
		return advisory;
	}
}