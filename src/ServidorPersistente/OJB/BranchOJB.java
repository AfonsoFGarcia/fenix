package ServidorPersistente.OJB;

import java.util.List;

import org.apache.ojb.broker.query.Criteria;

import Dominio.Branch;
import Dominio.IBranch;
import Dominio.ICursoExecucao;
import Dominio.IDegreeCurricularPlan;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentBranch;

/**
 * @author dcs-rjao
 * 
 * 20/Mar/2003
 */

public class BranchOJB extends ObjectFenixOJB implements IPersistentBranch
{

    public BranchOJB()
    {
    }

    

    public void delete(IBranch branch) throws ExcepcaoPersistencia
    {

        super.delete(branch);

    }

    public List readAll() throws ExcepcaoPersistencia
    {
        Criteria crit = new Criteria();
        return queryList(Branch.class, crit);
       
    }

    public List readByExecutionDegree(ICursoExecucao executionDegree) throws ExcepcaoPersistencia
    {
        Criteria crit = new Criteria();
        crit.addEqualTo("degreeCurricularPlan.idInternal",executionDegree.getCurricularPlan().getIdInternal());
        return queryList(Branch.class,crit);
      
    }

    public List readByDegreeCurricularPlan(IDegreeCurricularPlan degreeCurricularPlan)
        throws ExcepcaoPersistencia
    {

        Criteria crit = new Criteria();
        crit.addEqualTo("keyDegreeCurricularPlan", degreeCurricularPlan.getIdInternal());
        return queryList(Branch.class, crit);
    }

    public IBranch readByDegreeCurricularPlanAndBranchName(
        IDegreeCurricularPlan degreeCurricularPlan,
        String branchName)
        throws ExcepcaoPersistencia
    {
        Criteria crit = new Criteria();
        crit.addEqualTo("keyDegreeCurricularPlan", degreeCurricularPlan.getIdInternal());
        crit.addEqualTo("name", branchName);
        return (IBranch) queryObject(Branch.class, crit);
    }

    public IBranch readByDegreeCurricularPlanAndCode(
        IDegreeCurricularPlan degreeCurricularPlan,
        String code)
        throws ExcepcaoPersistencia
    {
        Criteria crit = new Criteria();
        crit.addEqualTo("keyDegreeCurricularPlan", degreeCurricularPlan.getIdInternal());
        crit.addEqualTo("code", code);
        return (IBranch) queryObject(Branch.class, crit);
    }

}