/*
 * Created on 30/Set/2003
 */
package ServidorAplicacao.Servico.manager;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import Dominio.ExecutionCourse;
import Dominio.IExecutionCourse;
import Dominio.IProfessorship;
import Dominio.IResponsibleFor;
import Dominio.ISite;
import ServidorAplicacao.IServico;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentExecutionCourse;
import ServidorPersistente.IFrequentaPersistente;
import ServidorPersistente.IPersistentProfessorship;
import ServidorPersistente.IPersistentResponsibleFor;
import ServidorPersistente.IPersistentSite;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.ITurnoPersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;

/**
 * @author lmac1
 */

public class DeleteExecutionCourses implements IServico
{

    private static DeleteExecutionCourses service = new DeleteExecutionCourses();

    public static DeleteExecutionCourses getService()
    {
        return service;
    }

    private DeleteExecutionCourses()
    {
    }

    public final String getNome()
    {
        return "DeleteExecutionCourses";
    }

    // delete a set of execution courses
    public List run(List internalIds) throws FenixServiceException
    {

        try
        {

            ISuportePersistente sp = SuportePersistenteOJB.getInstance();
            IPersistentExecutionCourse persistentExecutionCourse =
                sp.getIDisciplinaExecucaoPersistente();
            ITurnoPersistente persistentShift = sp.getITurnoPersistente();
            IFrequentaPersistente persistentAttend = sp.getIFrequentaPersistente();
            IPersistentProfessorship persistentProfessorShip = sp.getIPersistentProfessorship();
            IPersistentResponsibleFor persistentResponsibleFor = sp.getIPersistentResponsibleFor();
            IPersistentSite persistentSite = sp.getIPersistentSite();

            Iterator iter = internalIds.iterator();
            Iterator iterator;
            List shifts, attends, professorShips, responsibles;
            Integer internalId;
            IProfessorship professorShip;
            IResponsibleFor responsibleFor;
            List undeletedExecutionCoursesCodes = new ArrayList();
            ISite site;

            while (iter.hasNext())
            {
                internalId = (Integer) iter.next();
                IExecutionCourse executionCourse =
                    (IExecutionCourse) persistentExecutionCourse.readByOId(
                        new ExecutionCourse(internalId),
                        false);
                if (executionCourse != null)
                {
                    shifts = persistentShift.readByExecutionCourse(executionCourse);
                    if (!shifts.isEmpty())
                        undeletedExecutionCoursesCodes.add(executionCourse.getSigla());
                    else
                    {
                        attends = persistentAttend.readByExecutionCourse(executionCourse);
                        if (attends.isEmpty())
                        {
                            persistentExecutionCourse.deleteExecutionCourse(executionCourse);
                            professorShips =
                                persistentProfessorShip.readByExecutionCourse(executionCourse);
                            iterator = professorShips.iterator();
                            while (iterator.hasNext())
                            {
                                professorShip = (IProfessorship) iterator.next();
                                persistentProfessorShip.delete(professorShip);
                            }
                            responsibles =
                                persistentResponsibleFor.readByExecutionCourse(executionCourse);
                            iterator = responsibles.iterator();
                            while (iterator.hasNext())
                            {
                                responsibleFor = (IResponsibleFor) iterator.next();
                                persistentResponsibleFor.delete(responsibleFor);
                            }

                            site = persistentSite.readByExecutionCourse(executionCourse);
                            persistentSite.delete(site);
                        } else
                            undeletedExecutionCoursesCodes.add(executionCourse.getSigla());
                    }
                }
            }

            return undeletedExecutionCoursesCodes;

        } catch (ExcepcaoPersistencia e)
        {
            throw new FenixServiceException(e);
        }

    }

}