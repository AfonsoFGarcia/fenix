package ServidorAplicacao.Servico.commons;

import pt.utl.ist.berserk.logic.serviceManager.IService;
import DataBeans.InfoExecutionDegree;
import DataBeans.util.Cloner;
import Dominio.CursoExecucao;
import Dominio.ExecutionYear;
import Dominio.ICursoExecucao;
import Dominio.IExecutionYear;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.ICursoExecucaoPersistente;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;

/**
 * @author Nuno Nunes (nmsn@rnl.ist.utl.pt)
 * @author Joana Mota (jccm@rnl.ist.utl.pt)
 */

public class ReadExecutionDegreeByExecutionYearAndDegreeCode implements IService
{

    /**
     * 
     */
    public ReadExecutionDegreeByExecutionYearAndDegreeCode()
    {
     
    }

    

    public InfoExecutionDegree run(String executionDegreeString, String degreeCode)
            throws FenixServiceException
    {

        InfoExecutionDegree result = null;
        try
        {
            ISuportePersistente sp = SuportePersistenteOJB.getInstance();

            IExecutionYear executionYear = new ExecutionYear();
            executionYear.setYear(executionDegreeString);

            ICursoExecucaoPersistente executionDegreeDAO = sp.getICursoExecucaoPersistente();
            ICursoExecucao executionDegree = executionDegreeDAO.readByDegreeCodeAndExecutionYear(
                    degreeCode, executionYear);

            if (executionDegree != null)
            {
                result = (InfoExecutionDegree) Cloner.get(executionDegree);
            }
        }
        catch (ExcepcaoPersistencia ex)
        {
            throw new FenixServiceException(ex);
        }

        return result;
    }

    public InfoExecutionDegree run(Integer executionDegreeCode) throws FenixServiceException
    {

        InfoExecutionDegree result = null;
        try
        {
            ISuportePersistente sp = SuportePersistenteOJB.getInstance();

            ICursoExecucao executionDegreeDomain = new CursoExecucao();
            executionDegreeDomain.setIdInternal(executionDegreeCode);

            ICursoExecucaoPersistente executionDegreeDAO = sp.getICursoExecucaoPersistente();
            ICursoExecucao executionDegree = (CursoExecucao) executionDegreeDAO.readByOId(
                    executionDegreeDomain, false);

            if (executionDegree != null)
            {
                result = (InfoExecutionDegree) Cloner.get(executionDegree);
            }
        }
        catch (ExcepcaoPersistencia ex)
        {
            throw new FenixServiceException(ex);
        }

        return result;
    }
   
}