/*
 * Created on 30/Set/2003
 */
package net.sourceforge.fenixedu.applicationTier.Servico.manager;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.IExecutionCourse;
import net.sourceforge.fenixedu.domain.IProfessorship;
import net.sourceforge.fenixedu.domain.IResponsibleFor;
import net.sourceforge.fenixedu.domain.ISite;
import net.sourceforge.fenixedu.domain.ISummary;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IFrequentaPersistente;
import net.sourceforge.fenixedu.persistenceTier.IPersistentExamExecutionCourse;
import net.sourceforge.fenixedu.persistenceTier.IPersistentExecutionCourse;
import net.sourceforge.fenixedu.persistenceTier.IPersistentProfessorship;
import net.sourceforge.fenixedu.persistenceTier.IPersistentResponsibleFor;
import net.sourceforge.fenixedu.persistenceTier.IPersistentSite;
import net.sourceforge.fenixedu.persistenceTier.IPersistentSummary;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.ITurnoPersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;
import pt.utl.ist.berserk.logic.serviceManager.IService;

/**
 * @author lmac1 modified by Fernanda Quit�rio
 * 
 */

public class DeleteExecutionCourses implements IService {

    // delete a set of execution courses
    public List run(List internalIds) throws FenixServiceException {
        try {
            ISuportePersistente sp = PersistenceSupportFactory.getDefaultPersistenceSupport();
            IPersistentExecutionCourse persistentExecutionCourse = sp.getIPersistentExecutionCourse();
            ITurnoPersistente persistentShift = sp.getITurnoPersistente();
            IFrequentaPersistente persistentAttend = sp.getIFrequentaPersistente();
            IPersistentExamExecutionCourse persistentExamExecutionCourse = sp
                    .getIPersistentExamExecutionCourse();
            IPersistentProfessorship persistentProfessorShip = sp.getIPersistentProfessorship();
            IPersistentResponsibleFor persistentResponsibleFor = sp.getIPersistentResponsibleFor();
            IPersistentSite persistentSite = sp.getIPersistentSite();

            Iterator iter = internalIds.iterator();
            Iterator iterator;
            List shifts = null;
            List attends = null;
            List exams = null;
            List professorShips = null;
            List responsibles = null;
            Integer internalId;
            IProfessorship professorShip;
            IResponsibleFor responsibleFor;
            List undeletedExecutionCoursesCodes = new ArrayList();
            ISite site;

            while (iter.hasNext()) {
                internalId = (Integer) iter.next();
                IExecutionCourse executionCourse = (IExecutionCourse) persistentExecutionCourse
                        .readByOID(ExecutionCourse.class, internalId);
                if (executionCourse != null) {
                    shifts = persistentShift.readByExecutionCourse(executionCourse);
                    if (shifts != null && !shifts.isEmpty())
                        undeletedExecutionCoursesCodes.add(executionCourse.getSigla());
                    else {
                        attends = persistentAttend.readByExecutionCourse(executionCourse);
                        if (attends != null && !attends.isEmpty()) {
                            undeletedExecutionCoursesCodes.add(executionCourse.getSigla());
                        } else {
                            exams = persistentExamExecutionCourse.readByExecutionCourse(executionCourse
                                    .getSigla(), executionCourse.getExecutionPeriod().getName(),
                                    executionCourse.getExecutionPeriod().getExecutionYear().getYear());
                            if (exams != null && !exams.isEmpty()) {
                                undeletedExecutionCoursesCodes.add(executionCourse.getSigla());
                            } else {
                                persistentExecutionCourse.deleteExecutionCourse(executionCourse);
                                professorShips = persistentProfessorShip
                                        .readByExecutionCourse(executionCourse);
                                if (professorShips != null) {
                                    iterator = professorShips.iterator();
                                    while (iterator.hasNext()) {
                                        professorShip = (IProfessorship) iterator.next();

                                        IPersistentSummary persistentSummary = sp
                                                .getIPersistentSummary();
                                        List summaryList = persistentSummary.readByTeacher(professorShip
                                                .getExecutionCourse().getIdInternal(), professorShip
                                                .getTeacher().getTeacherNumber());
                                        if (summaryList != null && !summaryList.isEmpty()) {
                                            for (Iterator iteratorSummaries = summaryList.iterator(); iteratorSummaries
                                                    .hasNext();) {
                                                ISummary summary = (ISummary) iteratorSummaries.next();
                                                persistentSummary.simpleLockWrite(summary);
                                                summary.setProfessorship(null);
                                                summary.setKeyProfessorship(null);
                                            }
                                        }

                                        persistentProfessorShip.delete(professorShip);
                                    }
                                }
                                responsibles = persistentResponsibleFor
                                        .readByExecutionCourse(executionCourse);
                                if (responsibles != null) {
                                    iterator = responsibles.iterator();
                                    while (iterator.hasNext()) {
                                        responsibleFor = (IResponsibleFor) iterator.next();
                                        persistentResponsibleFor.delete(responsibleFor);
                                    }
                                }
                                site = persistentSite.readByExecutionCourse(executionCourse);
                                if (site != null) {
                                    persistentSite.delete(site);
                                }
                            }
                        }
                    }
                }
            }
            return undeletedExecutionCoursesCodes;
        } catch (ExcepcaoPersistencia e) {
            throw new FenixServiceException(e);
        }

    }

}