/*
 * Created on 4/Ago/2003
 */
package ServidorAplicacao.Servico.manager;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import DataBeans.util.Cloner;
import Dominio.DegreeCurricularPlan;
import Dominio.ICursoExecucao;
import Dominio.IDegreeCurricularPlan;
import ServidorAplicacao.IServico;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;

/**
 * @author lmac1
 */

public class ReadExecutionDegreesService implements IServico {

  private static ReadExecutionDegreesService service = new ReadExecutionDegreesService();

  /**
   * The singleton access method of this class.
   */
  public static ReadExecutionDegreesService getService() {
	return service;
  }

  /**
   * The constructor of this class.
   */
  private ReadExecutionDegreesService() { }

  /**
   * Service name
   */
  public final String getNome() {
	return "ReadExecutionDegreesService";
  }

  /**
   * Executes the service. Returns the current collection of infoExecutionDegrees.
   */
  public List run(Integer idDegreeCurricularPlan) throws FenixServiceException {
	ISuportePersistente sp;
	List allExecutionDegrees = null;
	try {
			sp = SuportePersistenteOJB.getInstance();
			IDegreeCurricularPlan degreeCurricularPlan = (IDegreeCurricularPlan) sp.getIPersistentDegreeCurricularPlan().readByOId(new DegreeCurricularPlan(idDegreeCurricularPlan), false);
			allExecutionDegrees = sp.getICursoExecucaoPersistente().readByDegreeCurricularPlan(degreeCurricularPlan);
	} catch (ExcepcaoPersistencia excepcaoPersistencia){
		throw new FenixServiceException(excepcaoPersistencia);
	}

	if (allExecutionDegrees == null || allExecutionDegrees.isEmpty()) 
		return allExecutionDegrees;

	// build the result of this service
	Iterator iterator = allExecutionDegrees.iterator();
	List result = new ArrayList(allExecutionDegrees.size());
    
	while (iterator.hasNext())
		result.add(Cloner.copyIExecutionDegree2InfoExecutionDegree((ICursoExecucao) iterator.next()));

	return result;
  }
}