/*
 * Created on 17/Set/2003
 */
package ServidorAplicacao.Servico.manager;

import DataBeans.InfoBranch;
import Dominio.Branch;
import Dominio.DegreeCurricularPlan;
import Dominio.IBranch;
import Dominio.IDegreeCurricularPlan;
import ServidorAplicacao.IServico;
import ServidorAplicacao.Servico.exceptions.ExistingServiceException;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorAplicacao.Servico.exceptions.NonExistingServiceException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentBranch;
import ServidorPersistente.IPersistentDegreeCurricularPlan;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;
import ServidorPersistente.exceptions.ExistingPersistentException;

/**
 * @author lmac1
 */

public class InsertBranch implements IServico {

	private static InsertBranch service = new InsertBranch();

	public static InsertBranch getService() {
		return service;
	}

	private InsertBranch() {
	}

	public final String getNome() {
		return "InsertBranch";
	}
	

	public void run(InfoBranch infoBranch) throws FenixServiceException {
	
		String code = null;
		try {
				ISuportePersistente persistentSuport = SuportePersistenteOJB.getInstance();
				
				Integer degreeCurricularPlanId = infoBranch.getInfoDegreeCurricularPlan().getIdInternal();
				IPersistentDegreeCurricularPlan persistentDegreeCurricularPlan = persistentSuport.getIPersistentDegreeCurricularPlan();
				IDegreeCurricularPlan degreeCurricularPlan = (IDegreeCurricularPlan) persistentDegreeCurricularPlan.readByOId(new DegreeCurricularPlan(degreeCurricularPlanId), false);
				
				if(degreeCurricularPlan == null)
					throw new NonExistingServiceException();
				
				String name = infoBranch.getName();
				code = infoBranch.getCode();
				
				IPersistentBranch persistentBranch = persistentSuport.getIPersistentBranch();
				
				IBranch branch = new Branch();	
				branch.setCode(code);
				branch.setName(name);							
				branch.setDegreeCurricularPlan(degreeCurricularPlan);
				
				persistentBranch.lockWrite(branch);
					
		} catch(ExistingPersistentException existingException) {
			throw new ExistingServiceException(); 
		} catch (ExcepcaoPersistencia excepcaoPersistencia) {
			throw new FenixServiceException(excepcaoPersistencia);
		}
	}
}