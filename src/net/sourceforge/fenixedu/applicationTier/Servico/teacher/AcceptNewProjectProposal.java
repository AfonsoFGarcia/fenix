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

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.ExistingServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.InvalidSituationServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NotAuthorizedException;
import net.sourceforge.fenixedu.domain.Grouping;
import net.sourceforge.fenixedu.domain.IAdvisory;
import net.sourceforge.fenixedu.domain.IAttends;
import net.sourceforge.fenixedu.domain.IExecutionCourse;
import net.sourceforge.fenixedu.domain.IExportGrouping;
import net.sourceforge.fenixedu.domain.IGrouping;
import net.sourceforge.fenixedu.domain.IPerson;
import net.sourceforge.fenixedu.domain.IProfessorship;
import net.sourceforge.fenixedu.domain.ITeacher;
import net.sourceforge.fenixedu.domain.DomainFactory;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IFrequentaPersistente;
import net.sourceforge.fenixedu.persistenceTier.IPersistentExportGrouping;
import net.sourceforge.fenixedu.persistenceTier.IPersistentGrouping;
import net.sourceforge.fenixedu.persistenceTier.IPersistentTeacher;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;
import net.sourceforge.fenixedu.util.ProposalState;
import pt.utl.ist.berserk.logic.serviceManager.IService;

/**
 * @author joaosa & rmalo
 * 
 */
public class AcceptNewProjectProposal implements IService {

    public Boolean run(Integer executionCourseId, Integer groupPropertiesId,
            String acceptancePersonUserName) throws FenixServiceException {

        Boolean result = Boolean.FALSE;

        if (groupPropertiesId == null) {
            return result;
        }

        try {

            ISuportePersistente sp = PersistenceSupportFactory.getDefaultPersistenceSupport();
            IPersistentGrouping persistentGroupProperties = sp.getIPersistentGrouping();
            IPersistentExportGrouping persistentExportGrouping = sp.getIPersistentExportGrouping();
            IFrequentaPersistente persistentAttend = sp.getIFrequentaPersistente();           
            IPersistentTeacher persistentTeacher = sp.getIPersistentTeacher();

            IGrouping grouping = (IGrouping) persistentGroupProperties.readByOID(
                    Grouping.class, groupPropertiesId);

            if (grouping == null) {
                throw new NotAuthorizedException();
            }

            IExportGrouping groupPropertiesExecutionCourse = persistentExportGrouping
                    .readBy(groupPropertiesId, executionCourseId);

            if (groupPropertiesExecutionCourse == null) {
                throw new ExistingServiceException();
            }

            IPerson receiverPerson = persistentTeacher.readTeacherByUsername(acceptancePersonUserName)
                    .getPerson();

            IExecutionCourse executionCourseAux = groupPropertiesExecutionCourse.getExecutionCourse();
            if (executionCourseAux.getGroupingByName(groupPropertiesExecutionCourse
                    .getGrouping().getName()) != null) {
                String name = groupPropertiesExecutionCourse.getGrouping().getName();
                throw new InvalidSituationServiceException(name);
            }

            persistentExportGrouping.simpleLockWrite(groupPropertiesExecutionCourse);

            List attendsStudentNumbers = new ArrayList();
            List attends = groupPropertiesExecutionCourse.getGrouping().getAttends();
            Iterator iterAttendsInAttendsSet = attends.iterator();
            while (iterAttendsInAttendsSet.hasNext()) {
                IAttends attend = (IAttends) iterAttendsInAttendsSet.next();
                attendsStudentNumbers.add(attend.getAluno().getNumber());
            }

            IExecutionCourse executionCourse = groupPropertiesExecutionCourse.getExecutionCourse();
            List attendsAux = persistentAttend.readByExecutionCourse(executionCourse.getIdInternal());
            Iterator iterAttends = attendsAux.iterator();
            while (iterAttends.hasNext()) {
                IAttends attend = (IAttends) iterAttends.next();
                if (!attendsStudentNumbers.contains(attend.getAluno().getNumber())) 
                    grouping.addAttends(attend);                                    
            }

            IPerson senderPerson = groupPropertiesExecutionCourse.getSenderPerson();
            List groupPropertiesExecutionCourseList = grouping
                    .getExportGroupings();
            Iterator iterGroupPropertiesExecutionCourseList = groupPropertiesExecutionCourseList
                    .iterator();
            List groupTeachers = new ArrayList();
            while (iterGroupPropertiesExecutionCourseList.hasNext()) {
                IExportGrouping groupPropertiesExecutionCourseAux = (IExportGrouping) iterGroupPropertiesExecutionCourseList
                        .next();
                if (groupPropertiesExecutionCourseAux.getProposalState().getState().intValue() == 1
                        || groupPropertiesExecutionCourseAux.getProposalState().getState().intValue() == 2) {
                    IExecutionCourse personExecutionCourse = groupPropertiesExecutionCourseAux
                            .getExecutionCourse();
                    List professorships = groupPropertiesExecutionCourseAux.getExecutionCourse().getProfessorships();
                    Iterator iterProfessorship = professorships.iterator();
                    while (iterProfessorship.hasNext()) {
                        IProfessorship professorship = (IProfessorship) iterProfessorship.next();
                        ITeacher teacher = professorship.getTeacher();
                        if (!(teacher.getPerson()).equals(receiverPerson)
                                && !groupTeachers.contains(teacher.getPerson())) {
                            groupTeachers.add(teacher.getPerson());
                        }
                    }

                    // Create Advisory for Teachers already in Grouping
                    // executioncourses
                    IAdvisory advisory = createAcceptAdvisory(executionCourse, personExecutionCourse,
                            groupPropertiesExecutionCourse, receiverPerson, senderPerson);
                    for (final Iterator iterator = groupTeachers.iterator(); iterator.hasNext();) {
                        final IPerson person = (IPerson) iterator.next();
                        sp.getIPessoaPersistente().simpleLockWrite(person);

                        person.getAdvisories().add(advisory);
                        advisory.getPeople().add(person);
                    }

                }
            }

            List groupAux = new ArrayList();
            List professorshipsAux = executionCourse.getProfessorships();

            Iterator iterProfessorshipsAux = professorshipsAux.iterator();
            while (iterProfessorshipsAux.hasNext()) {
                IProfessorship professorshipAux = (IProfessorship) iterProfessorshipsAux.next();
                ITeacher teacherAux = professorshipAux.getTeacher();
                if (!(teacherAux.getPerson()).equals(receiverPerson)) {
                    groupAux.add(teacherAux.getPerson());
                }
            }

            // Create Advisory for teachers of the new executioncourse
            IAdvisory advisoryAux = createAcceptAdvisory(executionCourse, executionCourse,
                    groupPropertiesExecutionCourse, receiverPerson, senderPerson);
            for (final Iterator iterator = groupAux.iterator(); iterator.hasNext();) {
                final IPerson person = (IPerson) iterator.next();
                sp.getIPessoaPersistente().simpleLockWrite(person);

                person.getAdvisories().add(advisoryAux);
                advisoryAux.getPeople().add(person);
            }

            groupPropertiesExecutionCourse.setProposalState(new ProposalState(new Integer(2)));
            groupPropertiesExecutionCourse.setReceiverPerson(receiverPerson);
            result = Boolean.TRUE;
        } catch (ExcepcaoPersistencia e) {
            e.printStackTrace();
            throw new FenixServiceException("error.noProjectProposal");
        }

        return result;
    }

    private IAdvisory createAcceptAdvisory(IExecutionCourse executionCourse,
            IExecutionCourse personExecutionCourse,
            IExportGrouping groupPropertiesExecutionCourse, IPerson receiverPerson,
            IPerson senderPerson) {
        IAdvisory advisory = DomainFactory.makeAdvisory();
        advisory.setCreated(new Date(Calendar.getInstance().getTimeInMillis()));
        if (groupPropertiesExecutionCourse.getGrouping().getEnrolmentEndDay() != null) {
            advisory.setExpires(groupPropertiesExecutionCourse.getGrouping().getEnrolmentEndDay()
                    .getTime());
        } else {
            advisory.setExpires(new Date(Calendar.getInstance().getTimeInMillis() + 1728000000));
        }
        advisory.setSender("Docente " + receiverPerson.getNome() + " da disciplina "
                + executionCourse.getNome());

        advisory.setSubject("Proposta Enviada Aceite");

        String msg;
        msg = new String(
                "A proposta de co-avalia��o do agrupamento "
                        + groupPropertiesExecutionCourse.getGrouping().getName()
                        + ", enviada pelo docente "
                        + senderPerson.getNome()
                        + "da disciplina "
                        + groupPropertiesExecutionCourse.getSenderExecutionCourse().getNome()
                        + " foi aceite pelo docente "
                        + receiverPerson.getNome()
                        + " da disciplina "
                        + executionCourse.getNome()
                        + "!"
                        + "<br>A partir deste momento poder-se-� dirijir � �rea de gest�o de grupos da disciplina "
                        + personExecutionCourse.getNome() + " para gerir a nova co-avalia��o.");

        advisory.setMessage(msg);        
        return advisory;
    }

}