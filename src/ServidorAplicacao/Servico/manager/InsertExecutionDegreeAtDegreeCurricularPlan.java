/*
 * Created on 14/Ago/2003
 */
package ServidorAplicacao.Servico.manager;

import DataBeans.InfoExecutionDegree;
import Dominio.Campus;
import Dominio.CursoExecucao;
import Dominio.DegreeCurricularPlan;
import Dominio.ExecutionYear;
import Dominio.ICampus;
import Dominio.ICursoExecucao;
import Dominio.IDegreeCurricularPlan;
import Dominio.IExecutionYear;
import Dominio.ITeacher;
import ServidorAplicacao.IServico;
import ServidorAplicacao.Servico.exceptions.ExistingServiceException;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorAplicacao.Servico.exceptions.NonExistingServiceException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.ICursoExecucaoPersistente;
import ServidorPersistente.IPersistentDegreeCurricularPlan;
import ServidorPersistente.IPersistentExecutionYear;
import ServidorPersistente.IPersistentTeacher;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;
import ServidorPersistente.exceptions.ExistingPersistentException;
import ServidorPersistente.places.campus.IPersistentCampus;

/**
 * @author lmac1
 */
public class InsertExecutionDegreeAtDegreeCurricularPlan implements IServico
{

    private static InsertExecutionDegreeAtDegreeCurricularPlan service =
        new InsertExecutionDegreeAtDegreeCurricularPlan();

    public static InsertExecutionDegreeAtDegreeCurricularPlan getService()
    {
        return service;
    }

    private InsertExecutionDegreeAtDegreeCurricularPlan()
    {
    }

    public final String getNome()
    {
        return "InsertExecutionDegreeAtDegreeCurricularPlan";
    }

    public void run(InfoExecutionDegree infoExecutionDegree) throws FenixServiceException
    {

        IExecutionYear executionYear = null;

        try
        {
            ISuportePersistente persistentSuport = SuportePersistenteOJB.getInstance();

            IPersistentCampus campusDAO = persistentSuport.getIPersistentCampus();
            IPersistentDegreeCurricularPlan persistentDegreeCurricularPlan =
                persistentSuport.getIPersistentDegreeCurricularPlan();

            ICampus campus =
                (ICampus) campusDAO.readByOId(
                    new Campus(infoExecutionDegree.getInfoCampus().getIdInternal()),
                    false);
            if (campus == null)
            {
                throw new NonExistingServiceException("message.nonExistingCampus", null);
            }

            IDegreeCurricularPlan degreeCurricularPlan =
                (IDegreeCurricularPlan) persistentDegreeCurricularPlan.readByOId(
                    new DegreeCurricularPlan(
                        infoExecutionDegree.getInfoDegreeCurricularPlan().getIdInternal()),
                    false);

            if (degreeCurricularPlan == null)
                throw new NonExistingServiceException("message.nonExistingDegreeCurricularPlan", null);

            IPersistentExecutionYear persistentExecutionYear =
                persistentSuport.getIPersistentExecutionYear();
            IExecutionYear execYear = new ExecutionYear();
            execYear.setIdInternal(infoExecutionDegree.getInfoExecutionYear().getIdInternal());
            executionYear = (IExecutionYear) persistentExecutionYear.readByOId(execYear, false);

            if (executionYear == null)
                throw new NonExistingServiceException("message.non.existing.execution.year", null);

            ICursoExecucaoPersistente persistentExecutionDegree =
                persistentSuport.getICursoExecucaoPersistente();

            IPersistentTeacher persistentTeacher = persistentSuport.getIPersistentTeacher();

            ITeacher coordinator =
                persistentTeacher.readByNumber(
                    infoExecutionDegree.getInfoCoordinator().getTeacherNumber());

            if (coordinator == null)
                throw new NonExistingServiceException("message.non.existing.teacher", null);

            ICursoExecucao executionDegree = new CursoExecucao();
            executionDegree.setCoordinator(coordinator);
            executionDegree.setCurricularPlan(degreeCurricularPlan);
            executionDegree.setExecutionYear(executionYear);
            executionDegree.setTemporaryExamMap(infoExecutionDegree.getTemporaryExamMap());
            executionDegree.setCampus(campus);
            persistentExecutionDegree.lockWrite(executionDegree);

        } catch (ExistingPersistentException existingException)
        {
            throw new ExistingServiceException(
                "O curso em execu��o referente ao ano lectivo em execu��o " + executionYear.getYear(),
                existingException);
        } catch (ExcepcaoPersistencia excepcaoPersistencia)
        {
            throw new FenixServiceException(excepcaoPersistencia);
        }
    }
}