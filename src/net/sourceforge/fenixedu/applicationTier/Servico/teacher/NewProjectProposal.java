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

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.InvalidArgumentsServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.InvalidSituationServiceException;
import net.sourceforge.fenixedu.domain.Advisory;
import net.sourceforge.fenixedu.domain.AttendInAttendsSet;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.GroupProperties;
import net.sourceforge.fenixedu.domain.GroupPropertiesExecutionCourse;
import net.sourceforge.fenixedu.domain.IAdvisory;
import net.sourceforge.fenixedu.domain.IAttendInAttendsSet;
import net.sourceforge.fenixedu.domain.IAttends;
import net.sourceforge.fenixedu.domain.IAttendsSet;
import net.sourceforge.fenixedu.domain.IExecutionCourse;
import net.sourceforge.fenixedu.domain.IGroupProperties;
import net.sourceforge.fenixedu.domain.IGroupPropertiesExecutionCourse;
import net.sourceforge.fenixedu.domain.IPerson;
import net.sourceforge.fenixedu.domain.IProfessorship;
import net.sourceforge.fenixedu.domain.ITeacher;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IFrequentaPersistente;
import net.sourceforge.fenixedu.persistenceTier.IPersistentAttendInAttendsSet;
import net.sourceforge.fenixedu.persistenceTier.IPersistentExecutionCourse;
import net.sourceforge.fenixedu.persistenceTier.IPersistentGroupProperties;
import net.sourceforge.fenixedu.persistenceTier.IPersistentGroupPropertiesExecutionCourse;
import net.sourceforge.fenixedu.persistenceTier.IPersistentTeacher;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;
import net.sourceforge.fenixedu.util.ProposalState;
import pt.utl.ist.berserk.logic.serviceManager.IService;

/**
 * @author joaosa & rmalo
 *  
 */
public class NewProjectProposal implements IService {

    public Boolean run(Integer objectCode, Integer goalExecutionCourseId, Integer groupPropertiesId,
            String senderPersonUsername) throws FenixServiceException, ExcepcaoPersistencia {

        Boolean result = Boolean.FALSE;

        if (groupPropertiesId == null) {
            return result;
        }
        ISuportePersistente sp = PersistenceSupportFactory.getDefaultPersistenceSupport();
        IPersistentGroupProperties persistentGroupProperties = sp.getIPersistentGroupProperties();
        IPersistentGroupPropertiesExecutionCourse persistentGroupPropertiesExecutionCourse = sp
                .getIPersistentGroupPropertiesExecutionCourse();
        IPersistentExecutionCourse persistentExecutionCourse = sp.getIPersistentExecutionCourse();
        IFrequentaPersistente persistentAttend = sp.getIFrequentaPersistente();
        IPersistentAttendInAttendsSet persistentAttendInAttendsSet = sp
                .getIPersistentAttendInAttendsSet();

        IGroupProperties groupProperties = (IGroupProperties) persistentGroupProperties.readByOID(
                GroupProperties.class, groupPropertiesId);
        IExecutionCourse goalExecutionCourse = (IExecutionCourse) persistentExecutionCourse.readByOID(
                ExecutionCourse.class, goalExecutionCourseId);
        IExecutionCourse startExecutionCourse = (IExecutionCourse) persistentExecutionCourse.readByOID(
                ExecutionCourse.class, objectCode);
        IPersistentTeacher persistentTeacher = sp.getIPersistentTeacher();
        IPerson senderPerson = persistentTeacher.readTeacherByUsername(senderPersonUsername).getPerson();

        if (groupProperties == null) {
            throw new InvalidArgumentsServiceException("error.noGroupProperties");
        }
        if (goalExecutionCourse == null) {
            throw new InvalidArgumentsServiceException("error.noGoalExecutionCourse");
        }
        if (startExecutionCourse == null) {
            throw new InvalidArgumentsServiceException("error.noSenderExecutionCourse");
        }
        if (senderPerson == null) {
            throw new InvalidArgumentsServiceException("error.noPerson");
        }

        List listaRelation = groupProperties.getGroupPropertiesExecutionCourse();
        Iterator iterRelation = listaRelation.iterator();
        while (iterRelation.hasNext()) {
            IGroupPropertiesExecutionCourse groupPropertiesExecutionCourse = (IGroupPropertiesExecutionCourse) iterRelation
                    .next();
            if (groupPropertiesExecutionCourse.getExecutionCourse().equals(goalExecutionCourse)
                    && groupPropertiesExecutionCourse.getProposalState().getState().intValue() == 1) {
                throw new InvalidSituationServiceException("error.GroupPropertiesCreator");
            }
            if (groupPropertiesExecutionCourse.getExecutionCourse().equals(goalExecutionCourse)
                    && groupPropertiesExecutionCourse.getProposalState().getState().intValue() == 2) {
                throw new InvalidSituationServiceException("error.AlreadyAcceptedProposal");
            }
            if (groupPropertiesExecutionCourse.getExecutionCourse().equals(goalExecutionCourse)
                    && groupPropertiesExecutionCourse.getProposalState().getState().intValue() == 3) {
                throw new InvalidSituationServiceException("error.WaitingProposal");
            }
        }

        boolean acceptProposal = false;

        IGroupPropertiesExecutionCourse groupPropertiesExecutionCourse = new GroupPropertiesExecutionCourse(
                groupProperties, goalExecutionCourse);

        persistentGroupPropertiesExecutionCourse.simpleLockWrite(groupPropertiesExecutionCourse);
        groupPropertiesExecutionCourse.setProposalState(new ProposalState(new Integer(3)));
        groupPropertiesExecutionCourse.setSenderPerson(senderPerson);
        groupPropertiesExecutionCourse.setSenderExecutionCourse(startExecutionCourse);
        groupProperties.addGroupPropertiesExecutionCourse(groupPropertiesExecutionCourse);
        goalExecutionCourse.addGroupPropertiesExecutionCourse(groupPropertiesExecutionCourse);

        List group = new ArrayList();
        List allOtherProfessors = new ArrayList();

        List professorships = persistentExecutionCourse.readExecutionCourseTeachers(goalExecutionCourse
                .getIdInternal());
        Iterator iterProfessorship = professorships.iterator();
        while (iterProfessorship.hasNext()) {
            IProfessorship professorship = (IProfessorship) iterProfessorship.next();
            ITeacher teacher = professorship.getTeacher();
            if (!(teacher.getPerson()).equals(senderPerson)) {

                group.add(teacher.getPerson());
            } else {
                acceptProposal = true;
            }
        }

        allOtherProfessors.addAll(group);

        // Create Advisory
        if (acceptProposal == false) {
            IAdvisory advisory = createNewProjectProposalAdvisory(goalExecutionCourse,
                    startExecutionCourse, groupProperties, senderPerson);
            sp.getIPersistentAdvisory().simpleLockWrite(advisory);
            for (final Iterator iterator = group.iterator(); iterator.hasNext();) {
                final IPerson person = (IPerson) iterator.next();
                sp.getIPessoaPersistente().simpleLockWrite(person);

                person.getAdvisories().add(advisory);
                advisory.getPeople().add(person);
            }
        }

        List groupAux = new ArrayList();

        List professorshipsAux = persistentExecutionCourse
                .readExecutionCourseTeachers(startExecutionCourse.getIdInternal());
        Iterator iterProfessorshipAux = professorshipsAux.iterator();
        while (iterProfessorshipAux.hasNext()) {
            IProfessorship professorshipAux = (IProfessorship) iterProfessorshipAux.next();
            ITeacher teacherAux = professorshipAux.getTeacher();
            IPerson pessoa = teacherAux.getPerson();
            if (!(pessoa.equals(senderPerson))) {
                groupAux.add(pessoa);
                if (!allOtherProfessors.contains(pessoa)) {

                    allOtherProfessors.add(pessoa);
                }
            }
        }

        // Create Advisory
        if (acceptProposal == true) {
            result = Boolean.TRUE;
            groupPropertiesExecutionCourse.setProposalState(new ProposalState(new Integer(2)));
            List attendsSetStudentNumbers = new ArrayList();
            IAttendsSet attendsSet = groupPropertiesExecutionCourse.getGroupProperties().getAttendsSet();
            List attendsInAttendsSet = attendsSet.getAttendInAttendsSet();
            Iterator iterAttendsInAttendsSet = attendsInAttendsSet.iterator();
            while (iterAttendsInAttendsSet.hasNext()) {
                IAttendInAttendsSet attendInAttendsSet = (IAttendInAttendsSet) iterAttendsInAttendsSet
                        .next();
                attendsSetStudentNumbers.add(attendInAttendsSet.getAttend().getAluno().getNumber());
            }

            List attends = persistentAttend.readByExecutionCourse(goalExecutionCourse.getIdInternal());
            Iterator iterAttends = attends.iterator();
            while (iterAttends.hasNext()) {
                IAttends attend = (IAttends) iterAttends.next();
                if (!attendsSetStudentNumbers.contains(attend.getAluno().getNumber())) {
                    IAttendInAttendsSet attendInAttendsSet = new AttendInAttendsSet(attend, attendsSet);
                    persistentAttendInAttendsSet.simpleLockWrite(attendInAttendsSet);
                    attendsSet.addAttendInAttendsSet(attendInAttendsSet);
                    attend.addAttendInAttendsSet(attendInAttendsSet);
                }
            }

            IAdvisory advisoryAux = createNewProjectProposalAcceptedAdvisory(goalExecutionCourse,
                    startExecutionCourse, groupProperties, senderPerson);
            sp.getIPersistentAdvisory().simpleLockWrite(advisoryAux);
            for (final Iterator iterator = allOtherProfessors.iterator(); iterator.hasNext();) {
                final IPerson person = (IPerson) iterator.next();
                sp.getIPessoaPersistente().simpleLockWrite(person);

                person.getAdvisories().add(advisoryAux);
                advisoryAux.getPeople().add(person);
            }

        } else {
            IAdvisory advisoryAux = createNewProjectProposalAdvisoryAux(goalExecutionCourse,
                    startExecutionCourse, groupProperties, senderPerson);
            sp.getIPersistentAdvisory().simpleLockWrite(advisoryAux);
            for (final Iterator iterator = groupAux.iterator(); iterator.hasNext();) {
                final IPerson person = (IPerson) iterator.next();
                sp.getIPessoaPersistente().simpleLockWrite(person);

                person.getAdvisories().add(advisoryAux);
                advisoryAux.getPeople().add(person);
            }
        }

        return result;
    }

    private IAdvisory createNewProjectProposalAdvisory(IExecutionCourse goalExecutionCourse,
            IExecutionCourse startExecutionCourse, IGroupProperties groupProperties, IPerson senderPerson) {
        IAdvisory advisory = new Advisory();
        advisory.setCreated(new Date(Calendar.getInstance().getTimeInMillis()));
        if (groupProperties.getEnrolmentEndDay() != null) {
            advisory.setExpires(groupProperties.getEnrolmentEndDay().getTime());
        } else {
            advisory.setExpires(new Date(Calendar.getInstance().getTimeInMillis() + 1728000000));
        }
        advisory.setSender("Docente " + senderPerson.getNome() + " da disciplina "
                + startExecutionCourse.getNome());

        advisory.setSubject("Proposta de Co-Avalia��o");

        String msg;
        msg = new String("Recebeu uma proposta de co-avalia��o da disciplina "
                + startExecutionCourse.getNome() + " para a disciplina " + goalExecutionCourse.getNome()
                + " relativa ao agrupamento " + groupProperties.getName() + "!"
                + "<br>Para mais informa��es dirija-se � �rea de gest�o de grupos da disciplina "
                + goalExecutionCourse.getNome() + ".");

        advisory.setMessage(msg);
        advisory.setOnlyShowOnce(new Boolean(true));
        return advisory;
    }

    private IAdvisory createNewProjectProposalAdvisoryAux(IExecutionCourse goalExecutionCourse,
            IExecutionCourse startExecutionCourse, IGroupProperties groupProperties, IPerson senderPerson) {
        IAdvisory advisory = new Advisory();
        advisory.setCreated(new Date(Calendar.getInstance().getTimeInMillis()));
        if (groupProperties.getEnrolmentEndDay() != null) {
            advisory.setExpires(groupProperties.getEnrolmentEndDay().getTime());
        } else {
            advisory.setExpires(new Date(Calendar.getInstance().getTimeInMillis() + 1728000000));
        }
        advisory.setSender("Docente " + senderPerson.getNome() + " da disciplina "
                + startExecutionCourse.getNome());

        advisory.setSubject("Proposta de Co-Avalia��o");

        String msg;
        msg = new String("O Docente " + senderPerson.getNome() + " da disciplina "
                + startExecutionCourse.getNome()
                + " fez uma proposta de co-avalia��o para a disciplina " + goalExecutionCourse.getNome()
                + " relativa ao agrupamento " + groupProperties.getName() + "!");

        advisory.setMessage(msg);
        advisory.setOnlyShowOnce(new Boolean(true));
        return advisory;
    }

    private IAdvisory createNewProjectProposalAcceptedAdvisory(IExecutionCourse goalExecutionCourse,
            IExecutionCourse startExecutionCourse, IGroupProperties groupProperties, IPerson senderPerson) {
        IAdvisory advisory = new Advisory();
        advisory.setCreated(new Date(Calendar.getInstance().getTimeInMillis()));
        if (groupProperties.getEnrolmentEndDay() != null) {
            advisory.setExpires(groupProperties.getEnrolmentEndDay().getTime());
        } else {
            advisory.setExpires(new Date(Calendar.getInstance().getTimeInMillis() + 1728000000));
        }
        advisory.setSender("Docente " + senderPerson.getNome() + " da disciplina "
                + startExecutionCourse.getNome());

        advisory.setSubject("Realiza��o de Co-Avalia��o");

        String msg;
        msg = new String("O Docente " + senderPerson.getNome() + " da disciplina "
                + startExecutionCourse.getNome() + " criou uma co-avalia��o para a disciplina "
                + goalExecutionCourse.getNome() + " relativa ao agrupamento "
                + groupProperties.getName() + "!");

        advisory.setMessage(msg);
        advisory.setOnlyShowOnce(new Boolean(true));
        return advisory;
    }

}